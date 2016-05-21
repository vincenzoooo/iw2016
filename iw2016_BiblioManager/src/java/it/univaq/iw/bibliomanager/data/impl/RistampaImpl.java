/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.Ristampa;
import it.univaq.iw.bibliomanager.data.model.Pubblicazione;
import it.univaq.iw.framework.data.DataLayerException;
import java.sql.Date;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class RistampaImpl implements Ristampa{
    private int key;
    private int number;
    private Date date;
    private int publication_key;
    private Pubblicazione publication;
    protected BiblioManagerDataLayer ownerDataLayer;

    public RistampaImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        number = 0;
        date = null;
        publication_key = 0;
        publication = null;        
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
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
