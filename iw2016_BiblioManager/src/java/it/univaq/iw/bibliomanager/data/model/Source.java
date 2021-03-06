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
public interface Source {

    int getKey();

    void setKey(int key);

    String getType();

    void setType(String type);

    String getUri();

    void setUri(String uri);

    String getFormat();

    void setFormat(String format);

    String getDescription();

    void setDescription(String description);

    boolean getCover();

    void setCover(boolean cover);

    boolean getDownload();

    void setDownload(boolean download);

    int getPublicationKey();

    void setPublicationKey(int publication_key);

    void copyFrom(Source source) throws DataLayerException;
    
    void setDirty(boolean dirty);

    boolean isDirty();
}
