/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.model;

import it.univaq.iw.framework.data.DataLayerException;
import java.sql.Date;

/**
 *
 * @author Vincenzo Lanzieri
 */
public interface Reprint {
    int getKey();
    
    void setKey(int key);
    
    int getNumber();

    void setNumber(int number);
    
    Date getDate();

    void setDate(Date date);
    
    Publication getPublication() throws DataLayerException;
    
    void setPublication(Publication publication);
    
    void copyFrom(Reprint reprint) throws DataLayerException;
}
