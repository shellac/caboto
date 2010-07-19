/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.caboto.rest.resources;

import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.ws.rs.core.Response;
import org.caboto.RdfMediaType;
import com.sun.jersey.api.client.ClientResponse;
import org.junit.After;
import org.caboto.profile.ProfileRepositoryException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pldms
 */
public class SPARQLTest extends AbstractResourceTest {

    private final static String QUERY_TYPE = "select * { graph ?g { ?s a ?o } }";

    private String requestUri;

    public SPARQLTest() {
    }

    @Before
    public void setUp() throws ProfileRepositoryException, UnsupportedEncodingException {
        formatDataStore();
        startJettyWithSecurity();

        requestUri = baseUri + "query?query=" + URLEncoder.encode(QUERY_TYPE, "UTF-8");

        String publicAnnotationUrlOne = createAndSaveAnnotation(userPublicUriOne);
        String publicAnnotationUrlTwo = createAndSaveAnnotation(userPublicUriOne);
        String privateAnnotationUrlOne = createAndSaveAnnotation(userPrivateUriOne);
    }

    @After
    public void tearDown() {
        stopJetty();
    }

    @Test
    public void testQueryUnauthenticated() {

        ClientResponse clientResponse = createGetClientResponse(null, null, requestUri,
                RdfMediaType.APPLICATION_XML);

        assertEquals("A 200 response should be returned", Response.Status.OK.getStatusCode(),
                clientResponse.getStatus());

        ResultSet res =
                ResultSetFactory.fromXML(clientResponse.getEntityInputStream());

        ResultSetRewindable resrw = ResultSetFactory.makeRewindable(res);

        assertEquals(2, resrw.size()); // got the two public types
    }

    @Test
    public void testQueryAuthenticated() {

        ClientResponse clientResponse = createGetClientResponse(usernameOne, passwordOne,
                requestUri, RdfMediaType.APPLICATION_XML);

        assertEquals("A 200 response should be returned", Response.Status.OK.getStatusCode(),
                clientResponse.getStatus());

        ResultSet res =
                ResultSetFactory.fromXML(clientResponse.getEntityInputStream());

        ResultSetRewindable resrw = ResultSetFactory.makeRewindable(res);

        assertEquals(3, resrw.size()); // got the two public and one private type
    }
}
