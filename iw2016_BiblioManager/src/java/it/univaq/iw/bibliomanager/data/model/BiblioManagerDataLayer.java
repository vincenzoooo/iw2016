/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.model;

import it.univaq.iw.framework.data.DataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public interface BiblioManagerDataLayer extends DataLayer{
    Autore createAuthor();
    Editore createEditor();
    Metadato createMetadata();
    Pubblicazione createPubblicazione();
    Recensione createReview();
    Ristampa createReprint();
    Sorgente createSource();
    Storico createHistoria();
    Utente createUser();
    
    Autore getAuthor(int author_key) throws DataLayerException;
    List<Autore> getAuthors() throws DataLayerException;
    Editore getEditor(int editor_key) throws DataLayerException;
    List<Editore> getEditors() throws DataLayerException;
    Metadato getMetadata(int metadata_key) throws DataLayerException;
    List<Metadato> getMetadatas() throws DataLayerException;
    Pubblicazione getPublication(int publication_key) throws DataLayerException;
    List<Pubblicazione> getPublication() throws DataLayerException;
    Recensione getReview(int review_key) throws DataLayerException;
    List<Recensione> getReviews() throws DataLayerException;
    Ristampa getReprint(int reprint_key) throws DataLayerException;
    List<Ristampa> getReprints() throws DataLayerException;
    Sorgente getSource(int source_key) throws DataLayerException;
    List<Sorgente> getSource() throws DataLayerException;
    Storico getHistoria(int historia_key) throws DataLayerException;
    List<Storico> getHistorias() throws DataLayerException;
    Utente getUser(int user_key) throws DataLayerException;
    List<Utente> getUsers() throws DataLayerException;
    
    void storeAuthor(Autore author) throws DataLayerException;
    void storeEditor(Editore editor) throws DataLayerException;
    void storeMetadata(Metadato metadata) throws DataLayerException;
    void storePublication(Pubblicazione publication) throws DataLayerException;
    void storeReview(Recensione review) throws DataLayerException;
    void storeReprint(Ristampa reprint) throws DataLayerException;
    void storeSource(Sorgente source) throws DataLayerException;
    void storeHistoria(Storico historia) throws DataLayerException;
    void storeUser(Utente user) throws DataLayerException;
}
