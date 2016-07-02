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

    int getISBN();

    void setISBN(int isbn);
    
    int getPages();

    void setPages(int pages);
    
    String getLanguage();

    void setLanguage(String language);
    
    Date getPublicationDate();

    void setPublicationDate(Date date);
    
    List<String> getKeywords();

    void setKeywords(List<String> keywords);
    
    Publication getPublication() throws DataLayerException;
    
    void setPublication(Publication publication);
}
