/*
 * Copyright (c) 2008, University of Bristol
 * Copyright (c) 2008, University of Manchester
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3) Neither the names of the University of Bristol and the
 *    University of Manchester nor the names of their
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.caboto.rest.resources;

import com.hp.hpl.jena.rdf.model.Model;
import com.sun.jersey.api.client.ClientResponse;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.caboto.RdfMediaType;
import org.caboto.domain.Annotation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pldms
 */
public class AboutResourceTest extends AbstractResourceTest {
    @Before
    @Override
    public void setUp() {
        formatDataStore();
        startJettyWithSecurity();
    }

    @After
    @Override
    public void tearDown() {
        stopJetty();
    }

    private int count = 0;

    @Override
    public Annotation createTestAnnotation(String graphUri) {
        Annotation annotation = super.createTestAnnotation(graphUri);
        Map<String, String> body = annotation.getBody();
        body.put("title", "title" + count);
        count++;
        return annotation;
    }

    /**
     * Test of findAnnotations method, of class AboutResource.
     */
    @Test
    public void testFindAnnotations() throws Exception {
        createAndSaveAnnotation(userPublicUriOne);
        String url = createAndSaveAnnotation(userPublicUriOne);
        ClientResponse response = createGetClientResponse(null, null,
                baseUri + "about/?rdf:type=foo",
                RdfMediaType.APPLICATION_RDF_XML);
        assertEquals("Got nothing", Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus());
        response = createGetClientResponse(null, null,
                url,
                RdfMediaType.APPLICATION_RDF_XML);
        response = createGetClientResponse(null, null,
                baseUri + "about/?dc:title=title1",
                RdfMediaType.APPLICATION_RDF_XML);
        assertEquals("Got something", Response.Status.OK.getStatusCode(),
                response.getStatus());
        Model model = response.getEntity(Model.class);
        assertEquals("Got one annotation", 7, model.size());
        response = createGetClientResponse(null, null,
                baseUri + "about/?dc:description=A%20description",
                RdfMediaType.APPLICATION_RDF_XML);
        model = response.getEntity(Model.class);
        assertEquals("Got two annotations", 14, model.size());
    }
    
    /**
     * Test LARQ business
     */
    @Test
    public void testFindByTest() throws Exception {
    	createAndSaveAnnotation(userPublicUriOne);
        String url = createAndSaveAnnotation(userPublicUriOne);
        ClientResponse response = createGetClientResponse(null, null,
                baseUri + "about/?search=foo",
                RdfMediaType.APPLICATION_RDF_XML);
        assertEquals("Got nothing", Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus());
        response = createGetClientResponse(null, null,
                url,
                RdfMediaType.APPLICATION_RDF_XML);
        response = createGetClientResponse(null, null,
                baseUri + "about/?search=title1",
                RdfMediaType.APPLICATION_RDF_XML);
        assertEquals("Got something", Response.Status.OK.getStatusCode(),
                response.getStatus());
        Model model = response.getEntity(Model.class);
        assertEquals("Got one annotation", 7, model.size());
        response = createGetClientResponse(null, null,
                baseUri + "about/?search=A%20description",
                RdfMediaType.APPLICATION_RDF_XML);
        model = response.getEntity(Model.class);
        assertEquals("Got two annotations", 14, model.size());
    }
}