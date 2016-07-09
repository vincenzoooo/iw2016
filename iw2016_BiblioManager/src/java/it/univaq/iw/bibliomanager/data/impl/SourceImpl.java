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
import java.util.List;

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
    private List<Publication> publications;
    protected BiblioManagerDataLayer ownerDataLayer;

    public SourceImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        type = "";
        uri = "";
        format = "";
        description = "";
        publications = null;
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
    public List<Publication> getPublications() throws DataLayerException {
        if(publications == null){
            publications = this.ownerDataLayer.getPublications();
        }
        return publications;
    }

    @Override
    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }
 
    @Override
    public void copyFrom(Source source) throws DataLayerException {
        key = source.getKey();
        description = source.getDescription();
        format = source.getFormat();
        uri = source.getURI();
        type = source.getType();
        publications = source.getPublications();
    }
}
