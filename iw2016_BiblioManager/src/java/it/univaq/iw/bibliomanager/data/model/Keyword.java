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
public interface Keyword {

    int getKey();

    void setKey(int key);

    String getName();

    void setName(String name);

    void copyFrom(Keyword metadata) throws DataLayerException;
    
    void setDirty(boolean dirty);

    boolean isDirty();
    
}
