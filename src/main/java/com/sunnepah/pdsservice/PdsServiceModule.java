package com.sunnepah.pdsservice;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Created by sunnepah on 28/10/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class PdsServiceModule extends AbstractModule {

    private PdsServiceConfiguration conf;

    public PdsServiceModule(PdsServiceConfiguration conf) {
        this.conf = conf;
    }

    @Override
    protected void configure() {
        bind(PdsServiceConfiguration.class).toInstance(conf);
    }

    @Provides
    @Singleton
    MetricRegistry getMetrics() {
        return new MetricRegistry();
    }
}
