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
public interface Sorgente {
    int getKey();
    
    String getType() throws DataLayerException;
    
    void setType(String name);
    
    String getURI() throws DataLayerException;
    
    void setURI(String uri);
    
    String getFormat() throws DataLayerException;
    
    void setFormat(String format);
    
    String getDescription() throws DataLayerException;
    
    void setDescription(String Description);
    
    Pubblicazione getPublication() throws DataLayerException;
    
    void setPublication(Pubblicazione publication);
}
