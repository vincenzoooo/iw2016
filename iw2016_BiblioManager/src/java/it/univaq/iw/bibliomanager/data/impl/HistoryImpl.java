/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import java.sql.Timestamp;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.User;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class HistoryImpl implements History{
    private int key;
    private String entry;
    private int type;
    private Timestamp timestamp;
    private int publication_key;
    private Publication publication;
    private int user_key;
    private User user;
    protected BiblioManagerDataLayer ownerDataLayer;

    public HistoryImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        entry = "";
        type = 0;
        timestamp = null;
        publication_key = 0;
        publication = null;
        user_key = 0;
        user = null;        
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
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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
    
    
}
