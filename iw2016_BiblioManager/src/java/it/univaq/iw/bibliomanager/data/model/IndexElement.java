/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.model;

import it.univaq.iw.framework.data.DataLayerException;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public interface IndexElement {
    int getKey();

    void setKey(int key);

    int getNumber();
    
    void setNumber(int number);
    
    String getTitle();

    void setTitle(String title);
    
    Publication getPublication() throws DataLayerException;

    void setPublication(Publication publication);
    
    IndexElement getAncestor() throws DataLayerException;

    void setAncestor(IndexElement ancestor);

    //--
    void copyFrom(IndexElement element) throws DataLayerException;
}
