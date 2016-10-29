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
public interface Editor {

    int getKey();

    void setKey(int key);

    String getName();

    void setName(String name);

    void copyFrom(Editor editor) throws DataLayerException;
    
    void setDirty(boolean dirty);

    boolean isDirty();
}
