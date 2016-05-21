/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.model;

import it.univaq.iw.framework.data.DataLayerException;

/**
 *
 * @author Vincenzo Lanzieri
 */
public interface Recensione {
    int getKey();
    
    String getText();

    void setText(String text);
    
    Boolean getStatus();
    
    void setStatus(Boolean status);
    
    Utente getAuthor() throws DataLayerException;
    
    void setAuthor(Utente author);
    
    Pubblicazione getPubblication() throws DataLayerException;
    
    void setPublication(Pubblicazione publication);
    
    Storico getArchive() throws DataLayerException;
    
    void setArchive(Storico archive);
}
