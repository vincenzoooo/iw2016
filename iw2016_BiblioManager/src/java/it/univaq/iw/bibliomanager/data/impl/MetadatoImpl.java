/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.Metadato;
import it.univaq.iw.bibliomanager.data.model.Pubblicazione;
import it.univaq.iw.framework.data.DataLayerException;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class MetadatoImpl implements Metadato{
    private int key;
    private int isbn;
    private int pageNumber;
    private String language;
    private Date publicationDate;
    private List<String> keywords;
    private int publication_key;
    private Pubblicazione publication;
    protected BiblioManagerDataLayer ownerDataLayer;

    public MetadatoImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        isbn = 0;
        pageNumber = 0;
        language = "";
        publicationDate = null;
        keywords = null;
        publication_key = 0;
        publication = null;        
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public int getISBN(){
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
    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public Pubblicazione getPublication() throws DataLayerException {
        if (publication == null && publication_key > 0) {
            publication = ownerDataLayer.getPublication(publication_key);
        }
        return publication;
    }

    @Override
    public void setPublication(Pubblicazione publication) {
        this.publication = publication;
    }
    
    
}
