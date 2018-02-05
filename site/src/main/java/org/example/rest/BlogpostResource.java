package org.example.rest;

import java.io.IOException;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.example.beans.Blogpost;
import org.example.beans.NewsDocument;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.ObjectBeanPersistenceException;
import org.hippoecm.hst.content.beans.manager.workflow.BaseWorkflowCallbackHandler;
import org.hippoecm.hst.content.beans.manager.workflow.WorkflowPersistenceManagerImpl;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
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
@Path("/brcms/")
public class BlogpostResource extends BaseRestResource {

    public static final String TEXT_PREFIX = "{\n" +
            "  \"fulfillmentText\": \"";
    public static final String TEXT_SUFFIX_MESSAGE = "\", \n" +
            "  \"fulfillmentMessages\": [\n" +
            "  {\n" +
            "    \"text\": {\n" +
            "      \"text\": [\"";
    public static final String MESSAGE_SUFFIX = "\"]\n" +
            "    }\n" +
            "  }\n" +
            "  ]\n" +
            "};";
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
                JsonElement fulfillmentText = queryRes.get("fulfillmentText");

                if (displayName.getAsString().toLowerCase().contains("create") &&
                        displayName.getAsString().toLowerCase().contains("document") &&
                        queryText != null &&
                        !queryText.toString().isEmpty() &&
                        queryText.toString().toLowerCase().contains("create") &&
                        queryText.toString().toLowerCase().contains("document")
                        ) {
                    return TEXT_PREFIX + "\"What is the title of the document?\"" + TEXT_SUFFIX_MESSAGE + "\"What is the title of the document?\"" + MESSAGE_SUFFIX;

                }

                if (displayName.getAsString().toLowerCase().contains("create") &&
                        displayName.getAsString().toLowerCase().contains("document") &&
                        queryText != null &&
                        !queryText.toString().isEmpty() &&
                        !queryText.toString().toLowerCase().contains("create") &&
                        !queryText.toString().toLowerCase().contains("document")
                        ) {
                    createDocument(queryText.toString(), request);

                    String responseMessage = "Cool! I just created a blogpost document with title " + queryText.toString().replace("\"", " ");


                    return TEXT_PREFIX + responseMessage + TEXT_SUFFIX_MESSAGE + responseMessage + MESSAGE_SUFFIX;

                }

                if (displayName.getAsString().toLowerCase().contains("published blogposts") &&
                        queryText != null &&
                        !queryText.toString().isEmpty() &&
                        queryText.toString().toLowerCase().contains("how") &&
                        queryText.toString().toLowerCase().contains("many") &&
                        (queryText.toString().toLowerCase().contains("documents") || queryText.toString().toLowerCase().contains("blogposts"))
                        ) {
                    Long docNumber = getDocumentCount(request);
                    String responseMessage = "There are ";
                    if (docNumber.toString().isEmpty() || docNumber.toString() == null) {
                        responseMessage = responseMessage.concat("zero");
                    } else {
                        responseMessage = responseMessage.concat(docNumber.toString());
                    }
                    responseMessage = responseMessage.concat(" published blogpost documents");

                    return TEXT_PREFIX + responseMessage + TEXT_SUFFIX_MESSAGE + responseMessage + MESSAGE_SUFFIX;
                }


                if (displayName.getAsString().toLowerCase().contains("latest") &&
                        displayName.getAsString().toLowerCase().contains("news") &&
                        queryText != null &&
                        !queryText.toString().isEmpty() &&
                        queryText.toString().toLowerCase().contains("latest") &&
                        queryText.toString().toLowerCase().contains("news")
                        ) {
                    String newsTitle = retrieveLatestNews(request);

                    String responseMessage = "The title of the latest news article is " + newsTitle;

                    return TEXT_PREFIX + responseMessage + TEXT_SUFFIX_MESSAGE + responseMessage + MESSAGE_SUFFIX;

                }

            }
        }

        return TEXT_PREFIX + "\"Sorry, I could not understand\"" + TEXT_SUFFIX_MESSAGE + "\"Sorry, I could not understand\"" + MESSAGE_SUFFIX;
    }

    private String retrieveLatestNews(HttpServletRequest request){
        try {
            //Session session = this.getPersistableSession(getRequestContext(request));
            Node newsNode = this.getScope(request, "news");
            HstQuery query = RequestContextProvider.get().getQueryManager().createQuery(newsNode, NewsDocument.class);
            query.addOrderByDescending("myhippoproject:date");
            NewsDocument latestNewsBean = getSingleBean(query);
            return latestNewsBean.getTitle();

        } catch (RepositoryException | QueryException e) {
            log.error("Failed to retrieve latest news: {}", e.getMessage(), e);
        }

        return "";
    }


    private Long getDocumentCount(HttpServletRequest request) {
        Pageable<Blogpost> pageable = findBeans(new DefaultRestContext(this, request, 1, 1), Blogpost.class);
        return pageable.getTotal();
    }

    private void createDocument(String parsedTitle, HttpServletRequest request) {
        parsedTitle = parsedTitle.replace("\"", "");
        parsedTitle = parsedTitle.substring(0, 1).toUpperCase() + parsedTitle.substring(1);
        WorkflowPersistenceManagerImpl wpm = null;
        try {

            char[] password = "admin".toCharArray();
            final Session session = this.getPersistableSession(this.getRequestContext(request), new SimpleCredentials("admin", password));
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
