/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.User;
import java.sql.Timestamp;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class HistoryImpl implements History {

    private int key;
    private String entry;
    private int type;
    private Timestamp date;
    private int publication_key;
    private String publication_title;
    private int user_key;
    private User user;
    private boolean dirty;
    protected BiblioManagerDataLayer ownerDataLayer;

    public HistoryImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        entry = "";
        type = 0;
        date = null;
        publication_key = 0;
        user_key = 0;
        user = null;
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
    public String getEntry() {
        return entry;
    }

    @Override
    public void setEntry(String entry) {
        this.entry = entry;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public Timestamp getDate() {
        return date;
    }

    @Override
    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public int getPublicationKey() {
        return publication_key;
    }

    @Override
    public void setPublicationKey(int publication_key) {
        this.publication_key = publication_key;
    }

    @Override
    public String getPublicationTitle() {
        return publication_title;
    }

    @Override
    public void setPublicationTitle(String publication_key) {
        this.publication_title = publication_key;
    }

    @Override
    public User getUser() throws DataLayerException {
        if (user == null && user_key > 0) {
            user = ownerDataLayer.getUser(user_key);
        }
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
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
    public void copyFrom(History history) throws DataLayerException {
        key = history.getKey();
        entry = history.getEntry();
        type = history.getType();
        date = history.getDate();
        publication_key = history.getPublicationKey();
        user_key = history.getUser().getKey();
        dirty = false;
    }
}
