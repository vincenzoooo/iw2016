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
public interface History {
    int getKey();
    
    void setKey(int key);
    
    String getEntry();

    void setEntry(String entry);
    
    int getType();

    void setType(int type);
    
    Timestamp getTimestamp();
    
    void setTimestamp(Timestamp timestamp);
    
    Publication getPublication() throws DataLayerException;
    
    void setPublication(Publication publication);
    
    User getUser() throws DataLayerException;

    void setUser(User user);
    
    void copyFrom(History history) throws DataLayerException;
}