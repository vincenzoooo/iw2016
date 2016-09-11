/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import java.util.List;
import it.univaq.iw.bibliomanager.data.model.Author;
import it.univaq.iw.bibliomanager.data.model.Chapter;
import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.bibliomanager.data.model.Keyword;
import it.univaq.iw.bibliomanager.data.model.Review;
import it.univaq.iw.bibliomanager.data.model.User;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class PublicationImpl implements Publication {

    private int key;
    private String title;
    private String description;
    private List<Chapter> index;
    private int like;
    private String isbn;
    private int pageNumber;
    private String language;
    private Timestamp publicationDate;
    private Editor editor;
    private boolean incomplete;
    private Source cover;
    private List<Author> authors;
    private List<Source> sources;
    private List<Keyword> keywords;
    private List<Reprint> reprints;
    private List<Review> reviews;
    private List<User> usersLike;
    protected BiblioManagerDataLayer ownerDataLayer;
    protected boolean dirty;

    public PublicationImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        title = "";
        description = "";
        index = null;
        like = 0;
        isbn = "";
        pageNumber = 0;
        language = "";
        publicationDate = null;
        editor = null;
        incomplete = false;
        cover = null;
        authors = null;
        sources = null;
        keywords = null;
        reprints = null;
        reviews = null;
        usersLike = null;
        dirty = false;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<Chapter> getIndex() {
        return index;
    }

    @Override
    public void setIndex(List<Chapter> index) {
        this.index = index;
    }

    @Override
    public int getLike() {
        return like;
    }

    @Override
    public void setLike(int likes) {
        this.like += likes;
    }

    @Override
    public String getIsbn() {
        return isbn;
    }

    @Override
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public void setPageNumber(int pages) {
        pageNumber = pages;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    @Override
    public void setPublicationDate(Timestamp date) {
        publicationDate = date;
    }

    @Override
    public Editor getEditor() throws DataLayerException {
        return editor;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public boolean getIncomplete() {
        return this.incomplete;
    }

    @Override
    public void setIncomplete(boolean incomplete) {
        this.incomplete = incomplete;
    }

    @Override
    public Source getCover() throws DataLayerException {
        if (cover == null && sources != null) {
            for (Source source : sources) {
                if (source.getDescription().equals("copertina")) {
                    cover = source;
                }
            }
        }
        return cover;
    }

    @Override
    public void setCover(Source cover) {
        this.cover = cover;
    }

    @Override
    public List<Author> getAuthors() throws DataLayerException {
        if (authors == null) {
            authors = ownerDataLayer.getAuthors();
        }
        return authors;
    }

    @Override
    public void setAuthor(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public List<Source> getSources() throws DataLayerException {
        if (sources == null) {
            sources = ownerDataLayer.getPublicationSources(key);
        }
        return sources;
    }

    @Override
    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    @Override
    public List<Keyword> getKeywords() throws DataLayerException {
        if (keywords == null) {
            keywords = ownerDataLayer.getPublicationKeywords(key);
        }
        return keywords;
    }

    @Override
    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    @Override
    public List<Reprint> getReprints() throws DataLayerException {
        if (reprints == null) {
            reprints = ownerDataLayer.getReprints(key);
        }
        return reprints;
    }

    @Override
    public void setReprints(List<Reprint> reprints) {
        this.reprints = reprints;
    }

    @Override
    public List<Review> getReviews() throws DataLayerException {
        return reviews;
    }

    @Override
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public List<User> getUserLike() throws DataLayerException {
        return usersLike;
    }

    @Override
    public void setUserLike(List<User> userLike) {
        this.usersLike = userLike;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void copyFrom(Publication publication) throws DataLayerException {
        key = publication.getKey();
        title = publication.getTitle();
        description = publication.getDescription();
        index = publication.getIndex();
        like = publication.getLike();
        isbn = publication.getIsbn();
        pageNumber = publication.getPageNumber();
        language = publication.getLanguage();
        publicationDate = publication.getPublicationDate();
        editor = publication.getEditor();
        incomplete = publication.getIncomplete();
        authors = publication.getAuthors();
        sources = publication.getSources();
        keywords = publication.getKeywords();
        reprints = publication.getReprints();
        reviews = publication.getReviews();
        this.dirty = true;
    }

}
