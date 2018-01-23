package org.example.beans;

import java.util.Calendar;
import java.util.List;

import org.apache.jackrabbit.commons.JcrUtils;
import org.hippoecm.hst.content.beans.ContentNodeBinder;
import org.hippoecm.hst.content.beans.ContentNodeBindingException;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.components.model.Authors;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.onehippo.cms7.essentials.components.rest.adapters.HippoHtmlAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "blogpost")
@XmlAccessorType(XmlAccessType.NONE)
@HippoEssentialsGenerated(internalName = "myhippoproject:blogpost")
@Node(jcrType = "myhippoproject:blogpost")
public class Blogpost extends HippoDocument implements Authors, ContentNodeBinder {

    private static final Logger log = LoggerFactory.getLogger(Blogpost.class);

    private static final String TITLE = "myhippoproject:title";
    private static final String INTRODUCTION = "myhippoproject:introduction";
    public static final String CONTENT = "myhippoproject:content";
    private static final String PUBLICATION_DATE = "myhippoproject:publicationdate";
    public static final String CATEGORIES = "myhippoproject:categories";
    private static final String AUTHOR = "myhippoproject:author";
    public static final String AUTHOR_NAMES = "myhippoproject:authornames";
    public static final String LINK = "myhippoproject:link";
    public static final String AUTHORS = "myhippoproject:authors";
    public static final String TAGS = "hippostd:tags";

    private String title;
    private String introduction;
    private Calendar publicationDate;



    @XmlElement
    @HippoEssentialsGenerated(internalName = "myhippoproject:publicationdate")
    public Calendar getPublicationDate() {
        return (publicationDate == null) ? getProperty(PUBLICATION_DATE) : publicationDate;
    }

    @XmlElement
    @HippoEssentialsGenerated(internalName = "myhippoproject:authornames")
    public String[] getAuthorNames() {
        return getProperty(AUTHOR_NAMES);
    }

    @XmlElement
    public String getAuthor() {
        final String[] authorNames = getAuthorNames();
        if (authorNames != null && authorNames.length > 0) {
            return authorNames[0];
        }
        return null;
    }

    @XmlElement
    @HippoEssentialsGenerated(internalName = "myhippoproject:title")
    public String getTitle() {
        return (title == null) ? getProperty(TITLE): title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIntroduction(String introduction){
        this.introduction = introduction;
    }

    public void setPublicationDate(Calendar publicationDate){
        this.publicationDate = publicationDate;
    }

    @XmlJavaTypeAdapter(HippoHtmlAdapter.class)
    @XmlElement
    @HippoEssentialsGenerated(internalName = "myhippoproject:content")
    public HippoHtml getContent() {
        return getHippoHtml(CONTENT);
    }

    @XmlElement
    @HippoEssentialsGenerated(internalName = "myhippoproject:introduction")
    public String getIntroduction() {
        return (introduction == null) ? getProperty(INTRODUCTION): introduction;
    }

    @XmlElement
    @HippoEssentialsGenerated(internalName = "myhippoproject:categories")
    public String[] getCategories() {
        return getProperty(CATEGORIES);
    }

    @Override
    @HippoEssentialsGenerated(internalName = "myhippoproject:authors")
    public List<Author> getAuthors() {
        return getLinkedBeans(AUTHORS, Author.class);
    }

    @Override
    public boolean bind(Object content, javax.jcr.Node node) throws ContentNodeBindingException {
        if (content instanceof Blogpost) {
            try {
                Blogpost blogpost = (Blogpost) content;

                node.setProperty(TITLE, blogpost.getTitle());
                node.setProperty(INTRODUCTION, blogpost.getIntroduction());
                node.setProperty(PUBLICATION_DATE, blogpost.getPublicationDate());

                javax.jcr.Node linkNode = JcrUtils.getOrAddNode(node, "myhippoproject:authors", "hippo:mirror");
                linkNode.setProperty("hippo:docbase", "5a6e1136-dea0-4927-9130-9ce0a47b0189");


            } catch (Exception e) {
                log.error("Unable to bind the content to the JCR Node" + e.getMessage(), e);
                throw new ContentNodeBindingException(e);
            }
            return true;
        }
        return false;
    }
}
