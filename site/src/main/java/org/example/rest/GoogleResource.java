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
public class GoogleResource extends BaseRestResource {


    public static final String ACTION_GET_PUBLISHED_DOCUMENTS = "action.get.published.documents";
    public static final String ACTION_CREATE_DOCUMENT = "action.create.document";
    public static final String ACTION_GET_NEWS = "action.get.news";

    private static Logger log = LoggerFactory.getLogger(GoogleResource.class);

    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String index(String webhookRequest, @Context HttpServletRequest request) throws IOException {

        GoogleResponseValue googleResponseValue = JSON.fromJson(webhookRequest, GoogleResponseValue.class);

        switch (googleResponseValue.getQueryResult().getAction()) {
            case ACTION_CREATE_DOCUMENT:
                return createDocument(googleResponseValue.getQueryResult().getQueryText(), request);
            case ACTION_GET_NEWS:
                return retrieveLatestNews(request);
            case ACTION_GET_PUBLISHED_DOCUMENTS:
                return getDocumentCount(request);
        }
        return null;
    }

    private String retrieveLatestNews(HttpServletRequest request) {
        try {
            Node newsNode = this.getScope(request, "news");
            HstQuery query = RequestContextProvider.get().getQueryManager().createQuery(newsNode, NewsDocument.class);
            query.addOrderByDescending("myhippoproject:date");
            NewsDocument latestNewsBean = getSingleBean(query);
            String responseMessage = "The title of the latest news article is " + latestNewsBean.getTitle();
            return responseMessage;

        } catch (RepositoryException | QueryException e) {
            log.error("Failed to retrieve latest news: {}", e.getMessage(), e);
        }

        return "";
    }


    private String getDocumentCount(HttpServletRequest request) {
        Pageable<Blogpost> pageable = findBeans(new DefaultRestContext(this, request, 1, 1), Blogpost.class);
        return "There are " + pageable.getTotal() + " published blogpost documents";
    }

    private String createDocument(String parsedTitle, HttpServletRequest request) {
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

            final String documentPath = wpm.createAndReturn("/content/documents/myhippoproject/blog/2018/01",
                    "myhippoproject:blogpost", parsedTitle, true);

            final Blogpost blogpost = (Blogpost) wpm.getObject(documentPath);
            if (blogpost == null) {
                throw new HstComponentException("Failed to add document");
            }

            final Calendar currentDate = Calendar.getInstance();
            populateBlogpost(currentDate, parsedTitle, blogpost);
            wpm.update(blogpost);
            wpm.save();

            String responseMessage = "Cool! I just created a blogpost document with title " + parsedTitle.replace("\"", " ");
            return responseMessage;
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
        return null;
    }

    private void populateBlogpost(final Calendar currentDate, final String title, final Blogpost blogpost) {
        blogpost.setTitle(title);
        blogpost.setPublicationDate(currentDate);
    }
}
