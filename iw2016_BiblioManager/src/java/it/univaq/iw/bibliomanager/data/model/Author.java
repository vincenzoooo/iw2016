/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.model;

import it.univaq.iw.framework.data.DataLayerException;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public interface Author {
    int getKey();
    
    List<Publication> getPublications() throws DataLayerException;

    void setPublication(List<Publication> publications);
    
    String getName();
    
    void setName(String name);
    
    String getSurname();
    
    void setSurname(String surname);
}
