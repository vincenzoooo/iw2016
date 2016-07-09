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
import it.univaq.iw.bibliomanager.data.model.Publication;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class AuthorImpl implements Author{    
    private int key;
    private String name;
    private String surname;
    private List<Publication> publications;
    
    protected BiblioManagerDataLayer ownerDataLayer;

    public AuthorImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        this.key = 0;
        this.name = "";
        this.surname = "";
        this.publications = null;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
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
    public void copyFrom(Author author) throws DataLayerException {
        key = author.getKey();
        name = author.getName();
        surname = author.getSurname();
        publications = author.getPublications();
    }
}
