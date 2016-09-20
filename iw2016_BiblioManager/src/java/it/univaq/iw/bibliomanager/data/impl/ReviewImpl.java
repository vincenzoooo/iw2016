/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.bibliomanager.data.model.Review;
import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.User;
import java.sql.Timestamp;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ReviewImpl implements Review {

    private int key;
    private String text;
    private boolean status;
    private Timestamp review_date;
    private int history_key;
    private History history;
    private int user_key;
    private User author;
    private int publication_key;
    protected BiblioManagerDataLayer ownerDataLayer;

    public ReviewImpl(BiblioManagerDataLayer ownerDataLayer) {
        this.ownerDataLayer = ownerDataLayer;
        key = 0;
        text = "";
        status = false;
        review_date = null;
        history_key = 0;
        history = null;
        user_key = 0;
        author = null;
        publication_key = 0;
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
    public Timestamp getReviewDate() {
        return review_date;
    }

    @Override
    public void setReviewDate(Timestamp date) {
        this.review_date = date;
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
    public int getPublicationKey() {
        return publication_key;
    }

    @Override
    public void setPublicationKey(int publication_key) {
        this.publication_key = publication_key;
    }

    @Override
    public void copyFrom(Review review) throws DataLayerException {
        key = review.getKey();
        history = review.getHistory();
        publication_key = review.getPublicationKey();
        status = review.getStatus();
        text = review.getText();
        user_key = review.getAuthor().getKey();
        if(review.getHistory() != null){
            history_key = review.getHistory().getKey();
        }
    }
}
