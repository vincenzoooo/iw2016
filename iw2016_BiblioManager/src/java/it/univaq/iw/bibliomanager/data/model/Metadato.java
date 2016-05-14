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
public interface Metadato {
    int getKey();

    int getISBN() throws DataLayerException;

    void setISBN(int isbn);
    
    int getPages() throws DataLayerException;

    void setPages(int pages);
    
    String getLanguage() throws DataLayerException;

    void setLanguage(String language);
    
    Date getPublicationDate() throws DataLayerException;

    void setPublicationDate(Date date);
    
    List<String> getKeywords() throws DataLayerException;

    void setKeywords(List<String> keywords);
    
    Pubblicazione getPublication() throws DataLayerException;
    
    void setPublication(Pubblicazione publication);
}
