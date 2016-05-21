/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.Autore;
import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.Pubblicazione;
import it.univaq.iw.framework.data.DataLayerException;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class AutoreImpl implements Autore{
    private int key;
    private String name;
    private String surname;
    private List<Pubblicazione> publications;
    
    protected BiblioManagerDataLayer ownerDataLayer;

    public AutoreImpl(BiblioManagerDataLayer ownerDataLayer) {
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
    public List<Pubblicazione> getPublications() throws DataLayerException {
        if(publications == null){
            publications = this.ownerDataLayer.getPublication();
        }
        return publications;
    }

    @Override
    public void setPublication(List<Pubblicazione> publications) {
        this.publications = publications;
    }
        
}
