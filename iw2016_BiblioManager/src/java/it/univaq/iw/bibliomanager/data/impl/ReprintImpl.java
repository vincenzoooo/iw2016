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
import it.univaq.iw.bibliomanager.data.model.Reprint;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ReprintImpl implements Reprint {

    private int key;
    private int number;
    private Date date;
    private int publication_key;
    private boolean dirty;
    protected BiblioManagerDataLayer ownerDataLayer;

    public ReprintImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        number = 0;
        date = null;
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
    public int getPublicationKey() throws DataLayerException {
        return publication_key;
    }

    @Override
    public void setPublicationKey(int publication_key) {
        this.publication_key = publication_key;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public void copyFrom(Reprint reprint) throws DataLayerException {
        key = reprint.getKey();
        date = reprint.getDate();
        number = reprint.getNumber();
        publication_key = reprint.getPublicationKey();
        dirty = false;
    }
}
