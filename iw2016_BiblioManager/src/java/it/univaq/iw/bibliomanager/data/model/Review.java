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
public interface Review {
    int getKey();
    
    void setKey(int key);
    
    String getText();

    void setText(String text);
    
    Boolean getStatus();
    
    void setStatus(Boolean status);
    
    User getAuthor() throws DataLayerException;
    
    void setAuthor(User author);
    
    Publication getPublication() throws DataLayerException;
    
    void setPublication(Publication publication);
    
    History getHistory() throws DataLayerException;
    
    void setHistory(History archive);
    
    void copyFrom(Review review) throws DataLayerException;
}
