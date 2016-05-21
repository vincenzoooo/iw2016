/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.model;

import it.univaq.iw.framework.data.DataLayerException;
import java.sql.Timestamp;

/**
 *
 * @author Vincenzo Lanzieri
 */
public interface Storico {
    int getKey();
    
    String getEntry();

    void setEntry(String entry);
    
    int getType();

    void setType(int type);
    
    Timestamp getTimestamp();
    
    void setTimestamp(Timestamp timestamp);
    
    Pubblicazione getPublication() throws DataLayerException;
    
    void setPublication(Pubblicazione publication);
    
    Utente getUser() throws DataLayerException;

    void setUser(Utente user);
    
}
