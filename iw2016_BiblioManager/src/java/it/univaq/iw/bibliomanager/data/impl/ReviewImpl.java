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
import it.univaq.iw.bibliomanager.data.model.Review;
import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.User;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ReviewImpl implements Review{
    private int key;
    private String text;
    private boolean status;
    private int history_key;
    private History history;
    private int user_key;
    private User author;
    private int publication_key;
    private Publication publication;
    protected BiblioManagerDataLayer ownerDataLayer;

    public ReviewImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        text = "";        
        status = false;
        history_key = 0;
        history = null;
        user_key = 0;
        author = null;
        publication_key = 0;
        publication = null;        
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
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Boolean getStatus() {
        return status;
    }

    @Override
    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public History getHistory() throws DataLayerException {
        if (history == null && history_key > 0) {
            history = ownerDataLayer.getHistory(history_key);
        }
        return history;
    }

    @Override
    public void setHistory(History history) {
        this.history = history;
    }

    @Override
    public User getAuthor() throws DataLayerException {
        if (author == null && user_key > 0) {
            author = ownerDataLayer.getUser(user_key);
        }
        return author;
    }

    @Override
    public void setAuthor(User author) {
        this.author = author;
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
    public void copyFrom(Review review) throws DataLayerException {
        key = review.getKey();
        history_key = review.getHistory().getKey();
        publication_key = review.getPublication().getKey();
        status = review.getStatus();
        text = review.getText();
        user_key = review.getAuthor().getKey();
    }
}
