/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Source;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class SourceImpl implements Source{
    private int key;
    private String type;
    private String uri;
    private String format;
    private String description;
    private int publication_key;
    private Publication publication;
    protected BiblioManagerDataLayer ownerDataLayer;

    public SourceImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        type = "";
        uri = "";
        format = "";
        description = "";
        publication_key = 0;
        publication = null;        
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getURI() {
        return uri;
    }

    @Override
    public void setURI(String uri) {
        this.uri = uri;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public void setFormat(String format) {
        this.format = format;
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
    public Publication getPublication() throws DataLayerException {
        if (publication == null && publication_key > 0) {
            publication = ownerDataLayer.getPublication(publication_key);
        }
        return publication;
    }

    @Override
    public void setPublication(Publication publication) {
        this.publication = publication;
    }
        
}
