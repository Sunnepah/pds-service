package com.sunnepah.savewithme;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.meltmedia.dropwizard.mongo.MongoBundle;
import com.sunnepah.savewithme.api.TweeMeRestService;
import com.sunnepah.savewithme.db.UserDAO;
import com.sunnepah.savewithme.resources.ClientResource;
import com.sunnepah.savewithme.resources.MongoResource;
import com.sunnepah.savewithme.web.UserServlet;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.client.Client;
import com.mongodb.MongoClient;
import com.mongodb.DB;

import com.sunnepah.savewithme.auth.AuthFilter;
import com.sunnepah.savewithme.core.User;
import com.sunnepah.savewithme.resources.AuthResource;
import com.sunnepah.savewithme.resources.UserResource;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.util.EnumSet;

public class SaveWithMeApplication extends Application<SaveWithMeConfiguration> {
    public static void main(final String[] args) throws Exception {
        new SaveWithMeApplication().run(args);
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
        bootstrap.addBundle(new AssetsBundle("/assets/stylesheets", "/stylesheets", null, "css"));
        bootstrap.addBundle(new AssetsBundle("/assets/directives", "/directives", null, "directives"));
        bootstrap.addBundle(new AssetsBundle("/assets/controllers", "/controllers", null, "controllers"));
        bootstrap.addBundle(new AssetsBundle("/assets/services", "/services", null, "services"));
        bootstrap.addBundle(new AssetsBundle("/assets/vendor", "/vendor", null, "vendor"));
        bootstrap.addBundle(new AssetsBundle("/assets/partials", "/partials", null, "partials"));
    }

    @Override
    public void run(final SaveWithMeConfiguration configuration, final Environment environment)
            throws ClassNotFoundException {

        // assemble in Guice module and integrate it into Dropwizard here.
        Injector injector = Guice.createInjector(new SaveWithMeModule(configuration));
        environment.jersey().register(injector.getInstance(TweeMeRestService.class));

        environment.getApplicationContext().addServlet(new ServletHolder(injector.getInstance(UserServlet.class)), "/api/v1/users");

        /*
         * Mongo Client
//         */
//        MongoClient mongoClient = mongoBundle.getClient();
//        DB db = mongoBundle.getDB();

        environment.jersey().register(new MongoResource(mongoBundle.getDB()));

        /*
         * Add Health Checks
         */

        /*
         * Filters
         */
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter("allowedOrigins", "*");
        filter.setInitParameter("allowedHeaders", "Authorization,Content-Type,X-Api-Key,Accept,Origin");
        filter.setInitParameter("allowedMethods", "GET,POST,PUT,DELETE,OPTIONS");
        filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
        filter.setInitParameter("allowCredentials", "true");

    /*
     * Metrics Reporting
     */
        //

        final UserDAO dao = new UserDAO(hibernateBundle.getSessionFactory());
        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClient()).build(getName());
        final SaveWithMeConfiguration.ClientSecretsConfiguration clientSecrets = configuration.getClientSecrets();

        environment.jersey().register(new ClientResource());
        environment.jersey().register(new UserResource(dao));
        environment.jersey().register(new AuthResource(client, dao, clientSecrets));

        environment.servlets().addFilter("AuthFilter", new AuthFilter()).addMappingForUrlPatterns(null, true, "/api/me");
        environment.servlets().addFilter("AuthFilter", new AuthFilter()).addMappingForUrlPatterns(null, true, "/api/v1/users");
    }
}
