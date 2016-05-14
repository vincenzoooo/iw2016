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
public interface Pubblicazione {
    int getKey();

    List<Autore> getAuthors() throws DataLayerException;

    void setAuthor(List<Autore> author);

    String getTitle();

    void setTitle(String title);
    
    String getDescription();

    void setDescription(String description);
    
    Editore getEditor() throws DataLayerException;
    
    void setEditor(Editore editor);
    
    List<String> getIndex() throws DataLayerException;
    
    void setIndex(List<String> index);
    
    int getNumberOfLikes() throws DataLayerException;
    
    void setNumberOfLikes();

    List<Sorgente> getSources() throws DataLayerException;
    
    void setSources(List<Sorgente> sources);
    
    List<Metadato> getMetadatas() throws DataLayerException;
    
    void setMetadatas(List<Metadato> metadatas);
    
    List<Ristampa> getReprints() throws DataLayerException;
    
    void setReprints(List<Ristampa> reprints);
    
    //--
    void copyFrom(Pubblicazione publication) throws DataLayerException;

    void setDirty(boolean dirty);

    boolean isDirty();  
}
