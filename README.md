## SaveWithMe

#### - Bootstrapped with [Dropwizard](https://github.com/dropwizard/dropwizard) + [satellizer](https://github.com/sahat/satellizer) (A token-based authentication module for [AngularJS](http://angularjs.org/)) sample project.

How to start the Savewithme application
---

1. Run `mvn clean install` to build your application
1. Run `cp example-config.yml config.yml` - Modify the configuration for your environment.
1. Start application with `java -jar target/Savewithme-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:3002`

Health Check
---

To see your applications health enter url `http://localhost:3002/healthcheck`


# Introduction

The drop wizard example application was developed to, as its name implies, provide examples of some of the features
present in drop wizard.

# Overview

Included with this application is an example of the optional db API module. The examples provided illustrate a few of
the features available in [JDBI](http://jdbi.org), along with demonstrating how these are used from within dropwizard.

This database example is comprised of the following classes.

* The `PersonDAO` illustrates using the [SQL Object Queries](http://jdbi.org/sql_object_api_queries/) and string template
features in JDBI.

* All the SQL statements for use in the `PersonDAO` are located in the `Person` class.

* `migrations.xml` illustrates the usage of `dropwizard-migrations` which can create your database prior to running
your application for the first time.

* The `PersonResource` and `PeopleResource` are the REST resource which use the PersonDAO to retrieve data from the database, note the injection
of the PersonDAO in their constructors.

As with all the modules the db example is wired up in the `initialize` function of the `SaveWithMeMainApplication`.

# Running The Application

* To package the example run.

        mvn package

* To setup the h2 database run.

        java -jar target/Savewithme-1.0-SNAPSHOT.jar db migrate example.yml

* To run the server run.

        java -jar target/Savewithme-1.0-SNAPSHOT.jar server example.yml

* To hit this url to access the service example.

	http://localhost:3002/

* To post data into the application you have use your login credentials in example (first line should be done once).

    curl -s -H "Content-Type: application/json" -X POST -d '{"email":"test@test.com","password":"testtest"}' http://localhost:3002/auth/login | grep -ioE '"[^"]*"' | tail -1 | grep -ioE '[^"]*[^"]' | awk '{print "Authorization : OAuth " $1}' > a.txt

	curl -H "Content-Type: application/json" -H "$(cat a.txt)" -X PUT -d '{"displayName":"Other Person","email":"other@test.com"}' http://localhost:3002/api/me

    curl -H "$(cat a.txt)" http://localhost:3002/api/me