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
public interface Chapter{
    int getKey();

    void setKey(int key);

    int getNumber();
    
    void setNumber(int number);
    
    String getTitle();

    void setTitle(String title);
    
    int getPublicationKey();
    
    void setPublicationKey(int publication_key);
    
    List<Section> getSections() throws DataLayerException;

    void setSections(List<Section> sections);
    
    //--
    void copyFrom(Chapter element) throws DataLayerException;
}
