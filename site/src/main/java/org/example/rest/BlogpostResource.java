package org.example.rest;

import java.io.IOException;
import java.util.Calendar;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.api.services.dialogflow.v2beta1.model.IntentMessage;
import com.google.api.services.dialogflow.v2beta1.model.WebhookResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.example.beans.Blogpost;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.ObjectBeanPersistenceException;
import org.hippoecm.hst.content.beans.manager.workflow.BaseWorkflowCallbackHandler;
import org.hippoecm.hst.content.beans.manager.workflow.WorkflowPersistenceManagerImpl;
import org.hippoecm.hst.core.component.HstComponentException;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.onehippo.cms7.essentials.components.rest.ctx.DefaultRestContext;
import org.onehippo.repository.documentworkflow.DocumentWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version "$Id$"
 */

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Path("/Blogpost/")
public class BlogpostResource extends BaseRestResource {

    private static Logger log = LoggerFactory.getLogger(BlogpostResource.class);

    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String index(String stringWebhookRequest, @Context HttpServletRequest request) throws IOException {

        JsonParser parser = new JsonParser();

        JsonElement jsonTree = parser.parse(stringWebhookRequest);
        if (jsonTree.isJsonObject()) {
            JsonObject jsonObject = jsonTree.getAsJsonObject();

            JsonElement session = jsonObject.get("session");
            JsonElement responseId = jsonObject.get("responseId");
            JsonElement queryResult = jsonObject.get("queryResult");
            JsonElement originalDetectIntentRequest = jsonObject.get("originalDetectIntentRequest");


            JsonElement displayName = null;
            if (queryResult.isJsonObject()) {
                JsonObject queryRes = queryResult.getAsJsonObject();
                JsonElement intent = queryRes.get("intent");
                JsonElement fulfillmentMessages = queryRes.get("fulfillmentMessages");

                JsonObject intentObj = intent.getAsJsonObject();
                if (intentObj.isJsonObject()) {
                    displayName = intentObj.get("displayName");
                }

                JsonElement queryText = queryRes.get("queryText");

                if (displayName.getAsString().toLowerCase().contains("create") &&
                        displayName.getAsString().toLowerCase().contains("document") &&
                        queryText != null &&
                        !queryText.toString().isEmpty() &&
                        !queryText.toString().toLowerCase().contains("create") &&
                        !queryText.toString().toLowerCase().contains("document")
                        ) {
                    createDocumet(queryText.toString(), request);
                }

                JsonElement fulfillmentText = queryRes.get("fulfillmentText");

            }
        }


// Returning a dummy WebhookResponse
        return "{\n" +
                "  \"result\": {\n" +
                "    \"source\": \"agent\",\n" +
                "    \"resolvedQuery\": \"city\",\n" +
                "    \"action\": \"tell.facts\",\n" +
                "    \"actionIncomplete\": false,\n" +
                "    \"parameters\": {\n" +
                "      \"facts-category\": \"city\"\n" +
                "    },\n" +
                "    \"contexts\": [],\n" +
                "    \"metadata\": {\n" +
                "      \"intentId\": \"873b1895-cdfc-42a4-b61b-5a1703c72a4d\",\n" +
                "      \"webhookUsed\": \"true\",\n" +
                "      \"webhookForSlotFillingUsed\": \"false\",\n" +
                "      \"webhookResponseTime\": 417,\n" +
                "      \"intentName\": \"tell-facts\"\n" +
                "    },\n" +
                "    \"fulfillment\": {\n" +
                "      \"speech\": \"Amsterdam\",\n" +
                "      \"messages\": [\n" +
                "        {\n" +
                "          \"type\": 0,\n" +
                "          \"speech\": \"Amsterdam\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"score\": 1\n" +
                "  }\n" +
                "}";
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


    private void createDocumet(String parsedTitle, HttpServletRequest request) {

        WorkflowPersistenceManagerImpl wpm = null;
        try {

            char[] password = "admin".toCharArray();

            final Session session = this.getPersistableSession(this.getRequestContext(request), new SimpleCredentials("admin", password));
            /* session = session.impersonate(new SimpleCredentials(session.getUserID(), new char[0]));*/


            wpm = new WorkflowPersistenceManagerImpl(session, RequestContextProvider.get().getContentBeansTool().getObjectConverter(), null);


            wpm.setWorkflowCallbackHandler(new BaseWorkflowCallbackHandler<DocumentWorkflow>() {
                public void processWorkflow(DocumentWorkflow wf) throws Exception {
                    wf.requestPublication();
                }
            });

            final String documentPath = wpm.createAndReturn("/content/documents/myhippoproject/blog/2018/01", "myhippoproject:blogpost", parsedTitle, true);

            final Blogpost blogpost = (Blogpost) wpm.getObject(documentPath);

            if (blogpost == null) {
                throw new HstComponentException("Failed to add document");
            }

            final Calendar currentDate = Calendar.getInstance();
            populateBlogpost(currentDate, parsedTitle, blogpost);
            wpm.update(blogpost);
            wpm.save();

            log.debug("A new document was added at " + blogpost.getPath());

        } catch (Exception e) {
            log.error("Failed to persist document: {}", e.getMessage(), e);
            if (wpm != null) {
                try {
                    wpm.refresh();
                } catch (ObjectBeanPersistenceException e1) {
                    log.warn("Failed to refresh: ", e1);
                }
            }
        }

    }

    private void populateBlogpost(final Calendar currentDate, final String title, final Blogpost blogpost) {
        blogpost.setTitle(title);
        blogpost.setPublicationDate(currentDate);
    }


}
