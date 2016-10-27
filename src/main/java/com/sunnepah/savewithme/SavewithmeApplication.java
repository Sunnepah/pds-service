package com.sunnepah.savewithme;

import com.sunnepah.savewithme.db.UserDAO;
import com.sunnepah.savewithme.resources.ClientResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

import com.sunnepah.savewithme.auth.AuthFilter;
import com.sunnepah.savewithme.core.User;
import com.sunnepah.savewithme.resources.AuthResource;
import com.sunnepah.savewithme.resources.UserResource;

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
    return "SaveWithMe";
  }

  @Override
  public void initialize(final Bootstrap<SaveWithMeConfiguration> bootstrap) {
    bootstrap.addBundle(new MigrationsBundle<SaveWithMeConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(final SaveWithMeConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    });

    bootstrap.addBundle(hibernateBundle);

    bootstrap.addBundle(new AssetsBundle("/assets/app.js", "/app.js", null, "app"));
    bootstrap.addBundle(new AssetsBundle("/assets/stylesheets", "/stylesheets", null, "css"));
    bootstrap.addBundle(new AssetsBundle("/assets/directives", "/directives", null, "directives"));
    bootstrap
        .addBundle(new AssetsBundle("/assets/controllers", "/controllers", null, "controllers"));
    bootstrap.addBundle(new AssetsBundle("/assets/services", "/services", null, "services"));
    bootstrap.addBundle(new AssetsBundle("/assets/vendor", "/vendor", null, "vendor"));
    bootstrap.addBundle(new AssetsBundle("/assets/partials", "/partials", null, "partials"));
  }

  @Override
  public void run(final SaveWithMeConfiguration configuration, final Environment environment)
      throws ClassNotFoundException {

    final UserDAO dao = new UserDAO(hibernateBundle.getSessionFactory());
    final Client client =
        new JerseyClientBuilder(environment).using(configuration.getJerseyClient())
            .build(getName());
    final SaveWithMeConfiguration.ClientSecretsConfiguration clientSecrets = configuration.getClientSecrets();

    environment.jersey().register(new ClientResource());
    environment.jersey().register(new UserResource(dao));
    environment.jersey().register(new AuthResource(client, dao, clientSecrets));

    environment.servlets().addFilter("AuthFilter", new AuthFilter())
        .addMappingForUrlPatterns(null, true, "/api/me");
  }
}
