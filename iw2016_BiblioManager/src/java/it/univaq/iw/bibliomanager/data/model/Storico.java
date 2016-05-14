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
    
    String getEntry() throws DataLayerException;

    void setEntry(String entry);
    
    int getType() throws DataLayerException;

    void setType(int type);
    
    Timestamp getTimestamp() throws DataLayerException;
    
    void setTimestamp(Timestamp timestamp);
    
    Pubblicazione getPublication() throws DataLayerException;
    
    void setPublication(Pubblicazione publication);
    
    int getUser() throws DataLayerException;

    void setUser(int user);
    
}
