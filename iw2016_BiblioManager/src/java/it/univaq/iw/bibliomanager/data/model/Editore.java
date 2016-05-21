/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.model;


/**
 *
 * @author Vincenzo Lanzieri
 */
public interface Editore {
    int getKey();
    
    String getName();
    
    void setName(String name);
}
