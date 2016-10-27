package com.sunnepah.savewithme;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class SavewithmeApplication extends Application<SavewithmeConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SavewithmeApplication().run(args);
    }

    @Override
    public String getName() {
        return "Savewithme";
    }

    @Override
    public void initialize(final Bootstrap<SavewithmeConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final SavewithmeConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
