/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import java.sql.Date;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Reprint;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ReprintImpl implements Reprint{
    private int key;
    private int number;
    private Date date;
    private int publication_key;
    private Publication publication;
    protected BiblioManagerDataLayer ownerDataLayer;

    public ReprintImpl(BiblioManagerDataLayer ownerDataLayer) {
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
