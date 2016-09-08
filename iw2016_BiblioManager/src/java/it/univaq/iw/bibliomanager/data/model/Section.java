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
public interface Section{
    int getKey();

    void setKey(int key);

    int getNumber();
    
    void setNumber(int number);
    
    String getTitle();

    void setTitle(String title);

    int getChapterKey();
    
    void setChapterKey(int chapter_key);
    
    //--
    void copyFrom(Section element) throws DataLayerException;
}
