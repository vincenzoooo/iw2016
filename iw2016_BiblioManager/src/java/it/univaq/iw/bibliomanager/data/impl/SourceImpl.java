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
public class SourceImpl implements Source {

    private int key;
    private String type;
    private String uri;
    private String format;
    private String description;
    private boolean cover;
    private boolean download;
    private int publication_key;
    private Publication publication;
    protected BiblioManagerDataLayer ownerDataLayer;

    public SourceImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        type = "";
        uri = "";
        format = "";
        publication_key = 0;
        publication = null;
        description = "";
        cover = false;
        download = false;
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
    public String getUri() {
        return uri;
    }

    @Override
    public void setUri(String uri) {
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
    public boolean getCover(){
        return cover;
    }
    
    @Override
    public void setCover(boolean cover){
        this.cover = cover;
    }
    
    @Override
    public boolean getDownload(){
        return download;
    }
    
    @Override
    public void setDownload(boolean download){
        this.download = download;
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

    @Override
    public void copyFrom(Source source) throws DataLayerException {
        key = source.getKey();
        description = source.getDescription();
        format = source.getFormat();
        uri = source.getUri();
        type = source.getType();
    }
}
