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
public interface Publication {

    int getKey();

    void setKey(int key);

    List<Author> getAuthors() throws DataLayerException;

    void setAuthor(List<Author> authors);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    Editor getEditor() throws DataLayerException;

    void setEditor(Editor editor);

    String getIndex();

    void setIndex(String index);

    int getLike();

    void setLike(int likes);

    String getIsbn();

    void setIsbn(String isbn);

    int getPageNumber();

    void setPageNumber(int pages);

    String getLanguage();

    void setLanguage(String language);

    Date getPublicationDate();

    void setPublicationDate(Date date);

    List<Keyword> getKeywords() throws DataLayerException;

    void setKeywords(List<Keyword> keywords);

    List<Source> getSources() throws DataLayerException;

    void setSources(List<Source> sources);
    
    Source getCover() throws DataLayerException;
    
    void setCover(Source cover);

    List<Reprint> getReprints() throws DataLayerException;

    void setReprints(List<Reprint> reprints);

    //--
    void copyFrom(Publication publication) throws DataLayerException;

    void setDirty(boolean dirty);

    boolean isDirty();
}
