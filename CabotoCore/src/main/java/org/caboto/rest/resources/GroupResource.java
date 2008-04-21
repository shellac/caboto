package org.caboto.rest.resources;

import com.sun.ws.rest.spi.resource.Singleton;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.ConsumeMime;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * <p>Class details</p>
 *
 * @author Mike Jones (mike.a.jones@bristol.ac.uk)
 * @version $Id$
 */
@Path("/group/")
@ProduceMime("application/json")
@ConsumeMime("application/json")
@Singleton
public class GroupResource {

    // ----------------------- JSON Methods -----------------------

    @POST
    public Response addPersonJson(JSONObject group) throws JSONException {

        return Response.status(405).build();
    }

    @GET
    @Path("{gid}/")
    public Response getPersonJson(@PathParam("uid")String uid) throws JSONException {

        return Response.status(405).build();
    }

    @PUT
    @Path("{gid}/")
    public Response updatePersonJson(@PathParam("uid")String uid, JSONObject person)
            throws JSONException {

       return Response.status(405).build();
    }

    @DELETE
    @Path("{gid}/")
    public Response deletePublicPersonJson(@PathParam("uid")String uid) throws JSONException {

        return Response.status(405).build();
    }

    @Context
    UriInfo uriInfo;
}