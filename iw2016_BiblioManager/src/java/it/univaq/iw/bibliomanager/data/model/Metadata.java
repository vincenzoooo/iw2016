/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.model;

import it.univaq.iw.framework.data.DataLayerException;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public interface Metadata {
    int getKey();

    void setKey(int key);
    
    int getISBN();

    void setISBN(int isbn);
    
    int getPages();

    void setPages(int pages);
    
    String getLanguage();

    void setLanguage(String language);
    
    Date getPublicationDate();

    void setPublicationDate(Date date);
    
    String getKeywords();

    void setKeywords(String keywords);
    
    Publication getPublication() throws DataLayerException;
    
    void setPublication(Publication publication);
    
    void copyFrom(Metadata metadata) throws DataLayerException;
}
