/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.Recensione;
import it.univaq.iw.bibliomanager.data.model.Storico;
import it.univaq.iw.bibliomanager.data.model.Utente;
import it.univaq.iw.bibliomanager.data.model.Pubblicazione;
import it.univaq.iw.framework.data.DataLayerException;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class RecensioneImpl implements Recensione{
    private int key;
    private String text;
    private boolean status;
    private int archive_key;
    private Storico archive;
    private int user_key;
    private Utente author;
    private int publication_key;
    private Pubblicazione publication;
    protected BiblioManagerDataLayer ownerDataLayer;

    public RecensioneImpl(BiblioManagerDataLayer ownerDataLayer) {
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
    public Storico getArchive() throws DataLayerException {
        if (archive == null && archive_key > 0) {
            archive = ownerDataLayer.getArchive(archive_key);
        }
        return archive;
    }

    @Override
    public void setArchive(Storico archive) {
        this.archive = archive;
    }

    @Override
    public Utente getAuthor() throws DataLayerException {
        if (author == null && user_key > 0) {
            author = ownerDataLayer.getUser(user_key);
        }
        return author;
    }

    @Override
    public void setAuthor(Utente author) {
        this.author = author;
    }

    @Override
    public Pubblicazione getPubblication() throws DataLayerException {
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
