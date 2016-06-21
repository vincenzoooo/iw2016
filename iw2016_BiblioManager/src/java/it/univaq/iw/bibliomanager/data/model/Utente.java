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

    String getName();
    
    void setName(String name);
    
    String getSurname();
    
    void setSurname(String surname);
    
    String getEmail();

    void setEmail(String email);
    
    String getPassword();

    void setPassword(String password);
    
    int getState();

    void setState(int state);
}
