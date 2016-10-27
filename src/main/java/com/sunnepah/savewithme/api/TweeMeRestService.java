package com.sunnepah.savewithme.api;

import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sunnepah on 28/10/2016.
 * Sunday Ayandokun @sundayayandokun
 */

@Path("/v1/twees")
public class TweeMeRestService {
    private final static Logger LOG = LoggerFactory.getLogger(TweeMeRestService.class);

    @Inject
    public TweeMeRestService() {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTwees() {
        JSONObject json = new JSONObject();
        json.put("name", "Hello");

        return Response.ok(json.toJSONString(), MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("/{Id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTwee(@PathParam("Id") String Id) {
        LOG.info("removing twee: {} ", Id);

        try {
            /** TODO */
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }
}
