package org.example.beans;

import java.util.Calendar;
import java.util.List;
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

@XmlRootElement(name = "blogpost")
@XmlAccessorType(XmlAccessType.NONE)
@HippoEssentialsGenerated(internalName = "myhippoproject:blogpost")
@Node(jcrType = "myhippoproject:blogpost")
public class Blogpost extends HippoDocument implements Authors {
    public static final String TITLE = "myhippoproject:title";
    public static final String INTRODUCTION = "myhippoproject:introduction";
    public static final String CONTENT = "myhippoproject:content";
    public static final String PUBLICATION_DATE = "myhippoproject:publicationdate";
    public static final String CATEGORIES = "myhippoproject:categories";
    public static final String AUTHOR = "myhippoproject:author";
    public static final String AUTHOR_NAMES = "myhippoproject:authornames";
    public static final String LINK = "myhippoproject:link";
    public static final String AUTHORS = "myhippoproject:authors";
    public static final String TAGS = "hippostd:tags";

    @XmlElement
    @HippoEssentialsGenerated(internalName = "myhippoproject:publicationdate")
    public Calendar getPublicationDate() {
        return getProperty(PUBLICATION_DATE);
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
        return getProperty(TITLE);
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
        return getProperty(INTRODUCTION);
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
}
