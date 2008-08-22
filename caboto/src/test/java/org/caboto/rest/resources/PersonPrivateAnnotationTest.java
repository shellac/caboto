package org.caboto.rest.resources;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.caboto.profile.ProfileRepositoryException;
import org.caboto.RdfMediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.Client;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Mike Jones (mike.a.jones@bristol.ac.uk)
 * @version $Id$
 */
public class PersonPrivateAnnotationTest extends AbstractResourceTest {

    @Before
    public void setUp() {
        formatDataStore();
        startJetty();
    }

    @After
    public void tearDown() {
        stopJetty();
    }

    @Test
    public void testAddPrivateAnnotation() {

        ClientResponse clientResponse = createPostClientResponse(userPrivateUriOne,
                MediaType.APPLICATION_FORM_URLENCODED, validPostData);

        assertEquals("A 201 response should be returned", Response.Status.CREATED.getStatusCode(),
                clientResponse.getStatus());

        System.out.println(clientResponse.getStatus());
        System.out.println(clientResponse.getLocation());

        assertTrue("The created location should start with " + userPrivateUriOne,
                clientResponse.getLocation().toString().startsWith(userPrivateUriOne));

    }

    @Test
    public void testAddPrivateAnnotationWithGarbage() {

        ClientResponse clientResponse = createPostClientResponse(userPrivateUriOne,
                MediaType.APPLICATION_FORM_URLENCODED, garbagePostData);

        assertEquals("A 400 response should be returned",
                Response.Status.BAD_REQUEST.getStatusCode(), clientResponse.getStatus());
    }

    @Test
    public void testGetPrivateAnnotationAsJson() throws ProfileRepositoryException, JSONException {

        String url = createAndSaveAnnotation(userPrivateUriOne);

        ClientResponse clientResponse =
                createGetClientResponse(url, MediaType.APPLICATION_JSON);

        JSONObject object = clientResponse.getEntity(JSONObject.class);

        assertEquals("A 200 response should be returned", Response.Status.OK.getStatusCode(),
                clientResponse.getStatus());

        assertEquals("The default type should be application/json", MediaType.APPLICATION_JSON_TYPE,
                clientResponse.getType());

        assertEquals("The IDs do not match", url, object.getString("id"));
    }

    @Test
    public void testGetPrivateAnnotationAsRdfXml() throws ProfileRepositoryException, JSONException {

        String url = createAndSaveAnnotation(userPrivateUriOne);

        ClientResponse clientResponse =
                createGetClientResponse(url, RdfMediaType.APPLICATION_RDF_XML);

        Model model = clientResponse.getEntity(Model.class);

        assertEquals("A 200 response should be returned", Response.Status.OK.getStatusCode(),
                clientResponse.getStatus());

        assertTrue("The URI is not in the model",
                model.containsResource(ResourceFactory.createResource(url)));

        assertEquals("The wrong media type", RdfMediaType.APPLICATION_RDF_XML_TYPE,
                clientResponse.getType());

    }

    @Test
    public void testGetPublicAnnotationAsRdfN3() throws ProfileRepositoryException, JSONException {

        String url = createAndSaveAnnotation(userPrivateUriOne);

        ClientResponse clientResponse =
                createGetClientResponse(url, RdfMediaType.TEXT_RDF_N3);

        Model model = clientResponse.getEntity(Model.class);

        assertEquals("A 200 response should be returned", Response.Status.OK.getStatusCode(),
                clientResponse.getStatus());

        assertTrue("The URI is not in the model",
                model.containsResource(ResourceFactory.createResource(url)));

        assertEquals("The wrong media type", RdfMediaType.TEXT_RDF_N3_TYPE,
                clientResponse.getType());

    }

    @Test
    public void testGetMissingResource() {

        ClientResponse clientResponse =
                createGetClientResponse(userPrivateUriOne + "aresourcethatdoesntexist",
                        MediaType.APPLICATION_JSON);

        assertEquals("A 404 response should be returned", Response.Status.NOT_FOUND
                .getStatusCode(), clientResponse.getStatus());

    }

    @Test
    public void testDeleteResource() throws ProfileRepositoryException {

        // create an annotation to delete
        String url = createAndSaveAnnotation(userPrivateUriOne);

        // check that the thing we want to delete actually exists
        ClientResponse clientResponse1 =
                createGetClientResponse(url, MediaType.APPLICATION_JSON);
        assertEquals("The resource sould return a 200", Response.Status.OK.getStatusCode(),
                clientResponse1.getStatus());

        // delete the resource
        Client c = Client.create();
        ClientResponse deleteResponse = c.resource(url).delete(ClientResponse.class);
        assertEquals("A 200 should be returned", Response.Status.OK.getStatusCode(),
                deleteResponse.getStatus());

        // make sure its not found
        ClientResponse clientResponse2 = createGetClientResponse(url, MediaType.APPLICATION_JSON);
        assertEquals("A 404 should be returned", Response.Status.NOT_FOUND.getStatusCode(),
                clientResponse2.getStatus());
    }

    @Test
    public void testDeleteResourceThatDoesNotExist() {

        Client c = Client.create();
        ClientResponse deleteResponse = c.resource(userPrivateUriOne + "doesnotexist")
                .delete(ClientResponse.class);
        assertEquals("A 404 should be returned", Response.Status.NOT_FOUND.getStatusCode(),
                deleteResponse.getStatus());
    }

}