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
import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.bibliomanager.data.model.Keyword;
import java.sql.Date;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class PublicationImpl implements Publication {

    private int key;
    private String title;
    private String description;
    private String index;
    private int like;
    private int editor_key;
    private int isbn;
    private int pageNumber;
    private String language;
    private Date publicationDate;
    private Editor editor;
    private List<Author> authors;
    private List<Source> sources;
    private List<Keyword> keywords;
    private List<Reprint> reprints;
    protected BiblioManagerDataLayer ownerDataLayer;
    protected boolean dirty;

    public PublicationImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        title = "";
        description = "";
        index = "";
        like = 0;
        isbn = 0;
        pageNumber = 0;
        language = "";
        publicationDate = null;
        editor_key = 0;
        editor = null;
        authors = null;
        sources = null;
        keywords = null;
        reprints = null;
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
    public String getIndex() {
        return index;
    }

    @Override
    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public int getNumberOfLikes() {
        return like;
    }

    @Override
    public void setNumberOfLikes(int likes) {
        this.like += likes;
    }

    @Override
    public int getISBN() {
        return isbn;
    }

    @Override
    public void setISBN(int isbn) {
        this.isbn = isbn;
    }

    @Override
    public int getPages() {
        return pageNumber;
    }

    @Override
    public void setPages(int pages) {
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
    public Date getPublicationDate() {
        return publicationDate;
    }

    @Override
    public void setPublicationDate(Date date) {
        publicationDate = date;
    }

    @Override
    public Editor getEditor() throws DataLayerException {
        if (editor == null && editor_key > 0) {
            editor = ownerDataLayer.getEditor(editor_key);
        }
        return editor;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
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
            sources = ownerDataLayer.getSources();
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
            keywords = ownerDataLayer.getKeywords(key);
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
        like = publication.getNumberOfLikes();
        editor = publication.getEditor();
        authors = publication.getAuthors();
        sources = publication.getSources();
        keywords = publication.getKeywords();
        reprints = publication.getReprints();
        this.dirty = true;
    }

}
