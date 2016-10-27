package com.sunnepah.savewithme;

import com.google.inject.AbstractModule;

/**
 * Created by sunnepah on 28/10/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class SaveWithMeModule extends AbstractModule {

    private SaveWithMeConfiguration configuration;

    public SaveWithMeModule(SaveWithMeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
    }
}
