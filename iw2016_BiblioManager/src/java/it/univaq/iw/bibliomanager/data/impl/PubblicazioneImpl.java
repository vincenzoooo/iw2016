/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.Pubblicazione;
import it.univaq.iw.bibliomanager.data.model.Editore;
import it.univaq.iw.bibliomanager.data.model.Autore;
import it.univaq.iw.bibliomanager.data.model.Metadato;
import it.univaq.iw.bibliomanager.data.model.Ristampa;
import it.univaq.iw.bibliomanager.data.model.Sorgente;
import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class PubblicazioneImpl implements Pubblicazione{
    private int key;
    private String title;
    private String description;
    private List<String> index;
    private int like;
    private int editor_key;
    private Editore editor;
    private List<Autore> authors;
    private List<Sorgente> sources;
    private List<Metadato> metadatas;
    private List<Ristampa> reprints;
    protected BiblioManagerDataLayer ownerDataLayer;
    protected boolean dirty;    

    public PubblicazioneImpl(BiblioManagerDataLayer ownerDataLayer) {
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
    public Editore getEditor() throws DataLayerException {
        if (editor == null && editor_key > 0) {
            editor = ownerDataLayer.getEditor(editor_key);
        }
        return editor;
    }

    @Override
    public void setEditor(Editore editor) {
        this.editor = editor;
    }

    @Override
    public List<Autore> getAuthors() throws DataLayerException {
        if (authors == null) {
            authors = ownerDataLayer.getAuthors();
        }
        return authors;
    }

    @Override
    public void setAuthor(List<Autore> authors) {
        this.authors = authors;
    }

    @Override
    public List<Sorgente> getSources() throws DataLayerException {
        if (sources == null) {
            sources = ownerDataLayer.getSource();
        }
        return sources;
    }

    @Override
    public void setSources(List<Sorgente> sources) {
        this.sources = sources;
    }

    @Override
    public List<Metadato> getMetadatas() throws DataLayerException {
        if (metadatas == null) {
            metadatas = ownerDataLayer.getMetadatas();
        }
        return metadatas;
    }

    @Override
    public void setMetadatas(List<Metadato> metadatas) {
        this.metadatas = metadatas;
    }

    @Override
    public List<Ristampa> getReprints() throws DataLayerException {
        if (reprints == null) {
            reprints = ownerDataLayer.getReprints();
        }
        return reprints;
    }

    @Override
    public void setReprints(List<Ristampa> reprints) {
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
    public void copyFrom(Pubblicazione publication) throws DataLayerException {
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
