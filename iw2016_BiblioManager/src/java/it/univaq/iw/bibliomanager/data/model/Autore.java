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
public interface Autore {
    int getKey();
    
    List<Pubblicazione> getPublications() throws DataLayerException;

    void setPublication(List<Pubblicazione> publication);
    
    String getName() throws DataLayerException;
    
    void setName(String name);
    
    String getSurname() throws DataLayerException;
    
    void setSurname(String surname);
}
