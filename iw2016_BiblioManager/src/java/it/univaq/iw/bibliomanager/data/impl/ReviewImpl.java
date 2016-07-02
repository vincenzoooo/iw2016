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
    private int archive_key;
    private History archive;
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
        archive_key = 0;
        archive = null;
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
    public History getArchive() throws DataLayerException {
        if (archive == null && archive_key > 0) {
            archive = ownerDataLayer.getArchive(archive_key);
        }
        return archive;
    }

    @Override
    public void setArchive(History archive) {
        this.archive = archive;
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
    public Publication getPubblication() throws DataLayerException {
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
