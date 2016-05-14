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
public interface Utente {
    int getKey();

    String getName() throws DataLayerException;
    
    void setName(String name);
    
    String getSurname() throws DataLayerException;
    
    void setSurname(String surname);
    
    String getEmail() throws DataLayerException;

    void setEmail(String email);
    
    String getPassword() throws DataLayerException;

    void setPassword(String password);
    
    String getState() throws DataLayerException;

    void setState(String state);
}
