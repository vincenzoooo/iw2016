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
import it.univaq.iw.bibliomanager.data.model.Metadata;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.bibliomanager.data.model.Source;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class PublicationImpl implements Publication{
    private int key;
    private String title;
    private String description;
    private List<String> index;
    private int like;
    private int editor_key;
    private Editor editor;
    private List<Author> authors;
    private List<Source> sources;
    private List<Metadata> metadatas;
    private List<Reprint> reprints;
    protected BiblioManagerDataLayer ownerDataLayer;
    protected boolean dirty;    

    public PublicationImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        title = "";
        description = "";
        index = null;
        like = 0;
        editor_key = 0;
        editor = null;
        authors = null;
        sources = null;
        metadatas = null;
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
    public List<String> getIndex() {
        return index;
    }

    @Override
    public void setIndex(List<String> index) {
        this.index = index;
    }

    @Override
    public int getNumberOfLikes() {
        return like;
    }

    @Override
    public void setNumberOfLikes() {
        this.like++;
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
            sources = ownerDataLayer.getSource();
        }
        return sources;
    }

    @Override
    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    @Override
    public List<Metadata> getMetadatas() throws DataLayerException {
        if (metadatas == null) {
            metadatas = ownerDataLayer.getMetadatas();
        }
        return metadatas;
    }

    @Override
    public void setMetadatas(List<Metadata> metadatas) {
        this.metadatas = metadatas;
    }

    @Override
    public List<Reprint> getReprints() throws DataLayerException {
        if (reprints == null) {
            reprints = ownerDataLayer.getReprints();
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
        metadatas = publication.getMetadatas();
        reprints = publication.getReprints();
        this.dirty = true;
    }
    
    
}
