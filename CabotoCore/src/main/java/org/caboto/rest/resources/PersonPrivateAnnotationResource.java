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
 * @author Mike Jones (mike.a.jones@bristol.ac.uk)
 * @version $Id$
 */

@Path("/person/{uid}/private/")
@ProduceMime("application/json")
@ConsumeMime("application/json")
@Singleton
public class PersonPrivateAnnotationResource {

    // ----------------------- JSON Methods -----------------------

    @POST
    public Response addPrivateAnnotationJson(@PathParam("uid")String uid, JSONObject annotation)
            throws JSONException {

        return Response.status(405).build();
    }

    @GET
    @Path("{id}/")
    public Response getPrivateAnnotationJson(@PathParam("uid")String uid,
                                              @PathParam("id")String id) throws JSONException {

       return Response.status(405).build();
    }

    @PUT
    @Path("{id}/")
    public Response updatePrivateAnnotationJson(@PathParam("uid")String uid,
                                               @PathParam("id")String id,
                                               JSONObject annotation) throws JSONException {

        return Response.status(405).build();
    }

    @DELETE
    @Path("{id}/")
    public Response deletePrivateAnnotationJson(@PathParam("uid")String uid,
                                               @PathParam("id")String id) throws JSONException {

        return Response.status(405).build();
    }

    @Context
    UriInfo uriInfo;
}