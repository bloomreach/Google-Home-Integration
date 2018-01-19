package org.example.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.api.services.dialogflow.v2beta1.model.WebhookRequest;
import com.google.api.services.dialogflow.v2beta1.model.WebhookResponse;

import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.onehippo.cms7.essentials.components.rest.ctx.DefaultRestContext;
import org.example.beans.Blogpost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version "$Id$"
 */

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Path("/Blogpost/")
public class BlogpostResource extends BaseRestResource {

    private static Logger log = LoggerFactory.getLogger(BlogpostResource.class);

/*
    @GET
    @Path("/")
    public Pageable<Blogpost> index(@Context HttpServletRequest request) {
        return findBeans(new DefaultRestContext(this, request), Blogpost.class);
    }
*/

    @POST
    @Path("/")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_JSON)
    public WebhookResponse index(com.google.api.client.json.GenericJson webhookRequest) throws IOException {
        log.debug(webhookRequest.toPrettyString());
        return new WebhookResponse();
    }

    @GET
    @Path("/page/{page}")
    public Pageable<Blogpost> page(@Context HttpServletRequest request, @PathParam("page") int page) {
        return findBeans(new DefaultRestContext(this, request, page, DefaultRestContext.PAGE_SIZE), Blogpost.class);
    }

    @GET
    @Path("/page/{page}/{pageSize}")
    public Pageable<Blogpost> pageForSize(@Context HttpServletRequest request, @PathParam("page") int page, @PathParam("pageSize") int pageSize) {
        return findBeans(new DefaultRestContext(this, request, page, pageSize), Blogpost.class);
    }

}
