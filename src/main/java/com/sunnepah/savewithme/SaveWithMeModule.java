package com.sunnepah.savewithme;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Created by sunnepah on 28/10/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class SaveWithMeModule extends AbstractModule {

    private SaveWithMeConfiguration conf;

    public SaveWithMeModule(SaveWithMeConfiguration conf) {
        this.conf = conf;
    }

    @Override
    protected void configure() {
        bind(SaveWithMeConfiguration.class).toInstance(conf);
    }

    @Provides
    @Singleton
    MetricRegistry getMetrics() {
        return new MetricRegistry();
    }
}
