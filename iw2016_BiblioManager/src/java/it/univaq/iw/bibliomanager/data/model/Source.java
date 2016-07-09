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
public interface Source {
    int getKey();
    
    void setKey(int key);
    
    String getType();
    
    void setType(String type);
    
    String getURI();
    
    void setURI(String uri);
    
    String getFormat();
    
    void setFormat(String format);
    
    String getDescription();
    
    void setDescription(String description);
    
    List<Publication> getPublications() throws DataLayerException;

    void setPublications(List<Publication> publications);
    
    void copyFrom(Source source) throws DataLayerException;
}
