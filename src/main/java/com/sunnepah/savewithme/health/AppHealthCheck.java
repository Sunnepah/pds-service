package com.sunnepah.savewithme.health;

import com.codahale.metrics.health.HealthCheck;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sunnepah on 25/11/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class AppHealthCheck extends HealthCheck {

    private final Client client;

    public AppHealthCheck(Client client) {
        super();
        this.client = client;
    }

    @Override
    protected Result check() {
        WebTarget wt = client.target("http://localhost:3002");
        Invocation.Builder inB = wt.request(MediaType.TEXT_HTML);
        Response response = inB.get();

        if (response.getStatus() == 200) {
            return Result.healthy();
        }

        return Result.unhealthy("DOWN");
    }
}
