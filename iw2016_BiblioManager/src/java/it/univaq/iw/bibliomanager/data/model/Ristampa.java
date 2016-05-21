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
public interface Ristampa {
    int getKey();
    
    int getNumber();

    void setNumber(int number);
    
    Date getDate();

    void setDate(Date date);
    
    Pubblicazione getPublication() throws DataLayerException;
    
    void setPublication(Pubblicazione publication);
}
