package com.sunnepah.savewithme;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.meltmedia.dropwizard.mongo.MongoBundle;
import com.sunnepah.savewithme.api.TweeMeRestService;
import com.sunnepah.savewithme.db.UserDAO;
import com.sunnepah.savewithme.db.UserRepository;
import com.sunnepah.savewithme.health.AppHealthCheck;
import com.sunnepah.savewithme.resources.ClientResource;
import com.sunnepah.savewithme.resources.MongoResource;
import com.sunnepah.savewithme.web.UserServlet;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.metrics.collectd.Collectd;
import io.dropwizard.metrics.collectd.CollectdReporter;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.client.Client;

import com.sunnepah.savewithme.auth.AuthFilter;
import com.sunnepah.savewithme.core.User;
import com.sunnepah.savewithme.resources.AuthResource;
import com.sunnepah.savewithme.resources.UserResource;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jongo.Jongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class SaveWithMeMainApplication extends Application<SaveWithMeConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(SaveWithMeMainApplication.class);

    public static void main(final String[] args) throws Exception {
        new SaveWithMeMainApplication().run(args);
    }

    private final HibernateBundle<SaveWithMeConfiguration> hibernateBundle =
            new HibernateBundle<SaveWithMeConfiguration>(User.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(final SaveWithMeConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "SaveWithMe " + getVersion();
    }

    private String getVersion() {
        String version = null;

        Package aPackage = Package.getPackage("com.sunnepah.savewithme");
        if (aPackage != null) {
            version = aPackage.getImplementationVersion();
        }

        return version;
    }

    private MongoBundle<SaveWithMeConfiguration> mongoBundle;

    @Override
    public void initialize(final Bootstrap<SaveWithMeConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<SaveWithMeConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(final SaveWithMeConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(mongoBundle = MongoBundle.<SaveWithMeConfiguration> builder()
                .withConfiguration(SaveWithMeConfiguration::getMongo).build());

        bootstrap.addBundle(hibernateBundle);

//        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html", "static"));
        bootstrap.addBundle(new AssetsBundle("/assets/app.js", "/app.js", null, "app"));
        bootstrap.addBundle(new AssetsBundle("/assets/stylesheets", "/stylesheets", null, "stylesheets"));
        bootstrap.addBundle(new AssetsBundle("/assets/directives", "/directives", null, "directives"));
        bootstrap.addBundle(new AssetsBundle("/assets/controllers", "/controllers", null, "controllers"));
        bootstrap.addBundle(new AssetsBundle("/assets/services", "/services", null, "services"));
        bootstrap.addBundle(new AssetsBundle("/assets/vendor", "/vendor", null, "vendor"));
        bootstrap.addBundle(new AssetsBundle("/assets/partials", "/partials", null, "partials"));
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "css"));
        bootstrap.addBundle(new AssetsBundle("/assets/img", "/img", null, "img"));
        bootstrap.addBundle(new AssetsBundle("/assets/js", "/js", null, "js"));
        bootstrap.addBundle(new AssetsBundle("/assets/vendors", "/vendors", null, "vendors"));
        bootstrap.addBundle(new AssetsBundle("/assets/fonts", "/fonts", null, "fonts"));
    }

    @Override
    public void run(SaveWithMeConfiguration conf, Environment env) throws ClassNotFoundException {
        // assemble in Guice module and integrate it into Dropwizard here.
        Injector injector = Guice.createInjector(new SaveWithMeModule(conf));
        env.jersey().register(injector.getInstance(TweeMeRestService.class));

        env.getApplicationContext().addServlet(new ServletHolder(injector.getInstance(UserServlet.class)), "/api/v1/users");

        /*
         * Filters
         */
        FilterRegistration.Dynamic filter = env.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter("allowedOrigins", "*");
        filter.setInitParameter("allowedHeaders", "Authorization,Content-Type,X-Api-Key,Accept,Origin");
        filter.setInitParameter("allowedMethods", "GET,POST,PUT,DELETE,OPTIONS");
        filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
        filter.setInitParameter("allowCredentials", "true");


        MetricRegistry metrics = injector.getInstance(MetricRegistry.class);

        final UserDAO dao = new UserDAO(hibernateBundle.getSessionFactory());
        final Client client = new JerseyClientBuilder(env).using(conf.getJerseyClient()).build(getName());
        final Jongo jongo = new Jongo(mongoBundle.getDB());
        final UserRepository userRepository = new UserRepository(mongoBundle.getDB(), jongo);

        env.jersey().register(new ClientResource());
        env.jersey().register(new UserResource(dao));
        env.jersey().register(new AuthResource(client, dao, conf, userRepository, metrics));
        env.jersey().register(new MongoResource(mongoBundle.getDB()));

        env.servlets().addFilter("AuthFilter", new AuthFilter()).addMappingForUrlPatterns(null, true, "/api/me");
        env.servlets().addFilter("AuthFilter", new AuthFilter()).addMappingForUrlPatterns(null, true, "/api/v1/users");

         /*
         * Add Health Checks
         */
        env.healthChecks().register("AppHealthCheck", new AppHealthCheck(client));
        /*
         * Metrics Reporting
         */
        if (conf.getMetrics().getServer() != null) {
            Collectd collectd = new Collectd(new InetSocketAddress(conf.getMetrics().getServer(), conf.getMetrics().getPort()));
            CollectdReporter reporter = CollectdReporter.forRegistry(metrics)
                    .prefixedWith(conf.getMetrics().getPrefix())
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .filter(MetricFilter.ALL)
                    .build(collectd);
            reporter.start(10, TimeUnit.SECONDS);

            /* This is only relevant for dev env to display metric report in the console */
            Slf4jReporter reporter2 = Slf4jReporter.forRegistry(metrics)
                    .outputTo(LoggerFactory.getLogger("com.sunnepah.savewithme"))
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .prefixedWith(conf.getMetrics().getPrefix())
                    .filter(MetricFilter.ALL)
                    .build();
            reporter2.start(4, TimeUnit.SECONDS);

        }
    }
}
