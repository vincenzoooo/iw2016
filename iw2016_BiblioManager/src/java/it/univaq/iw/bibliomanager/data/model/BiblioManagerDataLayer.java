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
import java.util.Map;

/**
 *
 * @author Vincenzo Lanzieri
 */
public interface BiblioManagerDataLayer extends DataLayer {

    /**
     * Crea un nuovo autore
     *
     * @return Nuovo autore
     */
    Author createAuthor();

    /**
     * Crea un nuovo editore
     *
     * @return Nuovo editore
     */
    Editor createEditor();

    /**
     * Crea una nuova keyword
     *
     * @return Nuova keyword
     */
    Keyword createKeyword();

    /**
     * Crea una nuova pubblicazione
     *
     * @return Nuova pubblicazione
     */
    Publication createPublication();

    /**
     * Crea una nuova recensione
     *
     * @return Nuova recensione
     */
    Review createReview();

    /**
     * Crea una nuova ristampa
     *
     * @return Nuova ristampa
     */
    Reprint createReprint();

    /**
     * Crea una nuova risorsa
     *
     * @return Nuova risorsa
     */
    Source createSource();

    /**
     * Crea un nuovo storico
     *
     * @return Nuovo storico
     */
    History createHistory();

    /**
     * Crea un nuovo utente
     *
     * @return Nuovo utente
     */
    User createUser();

    /**
     * Crea un nuovo capitolo
     *
     * @return Nuovo capitolo
     */
    Chapter createChapter();

    /**
     * Crea una nuova sezione
     *
     * @return Nuova sezione
     */
    Section createSection();

    /**
     * Restituisce i dati l'autore a partire dal suo id
     *
     * @param author_key
     * @return
     * @throws DataLayerException
     */
    Author getAuthor(int author_key) throws DataLayerException;

    /**
     * Restituisce una lista di autori presenti nel database
     *
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Author> getAuthors(int limit, int offset) throws DataLayerException;

    /**
     * Restituisce tutti gli autori di una pubblicazione
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    List<Author> getPublicationAuthors(int publication_key) throws DataLayerException;

    /**
     * Restituisce gli autori in base al loro nome
     *
     * @param name
     * @return
     * @throws DataLayerException
     */
    List<Author> getAuthorsByName(String name) throws DataLayerException;

    /**
     * Restituisce i dati dell'editore a partire dal suo id
     *
     * @param editor_key
     * @return
     * @throws DataLayerException
     */
    Editor getEditor(int editor_key) throws DataLayerException;

    /**
     * Restituisce una lista di editori
     *
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Editor> getEditors(int limit, int offset) throws DataLayerException;

    /**
     * Restituisce gli editori con un dato nome
     *
     * @param name
     * @return
     * @throws DataLayerException
     */
    List<Editor> getEditorsByName(String name) throws DataLayerException;

    /**
     * Restituisce i dati di una keyword dal suo id
     *
     * @param keyword_key
     * @return
     * @throws DataLayerException
     */
    Keyword getKeyword(int keyword_key) throws DataLayerException;

    /**
     * Restituisce una lista di keyword
     *
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Keyword> getKeywords(int limit, int offset) throws DataLayerException;

    /**
     * Restituisce tutte le parole chiave di una data pubblicazione
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    List<Keyword> getPublicationKeywords(int publication_key) throws DataLayerException;

    /**
     * Restituisce i dati di una pubblicazione a partire dal suo id
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    Publication getPublication(int publication_key) throws DataLayerException;

    /**
     * Restituisce i dati di una pubblicazione a partire dal suo codice ISBN
     *
     * @param isbn
     * @return
     * @throws DataLayerException
     */
    Publication getPublicationByISBN(String isbn) throws DataLayerException;

    /**
     * Restituisce tutte le pubblicazioni
     *
     * @return
     * @throws DataLayerException
     */
    List<Publication> getPublications() throws DataLayerException;

    /**
     * Restituisce le pubblicazioni in ordine di data di inserimento
     *
     * @return
     * @throws DataLayerException
     */
    List<Publication> getLastInsertedPublication() throws DataLayerException;

    /**
     * Restituisce le pubblicazioni in ordine di data di modifica
     *
     * @return
     * @throws DataLayerException
     */
    List<Publication> getLastModifiedPublication() throws DataLayerException;

    /**
     * Restituisce tutte le pubblicazioni che rispettano i filtri passati in
     * input
     *
     * @param filters
     * @return
     * @throws DataLayerException
     */
    List<Publication> getPublicationsByFilters(Map<String, String> filters) throws DataLayerException;

    /**
     * Restituisce i dati di una recensione in base al suo id
     *
     * @param review_key
     * @return
     * @throws DataLayerException
     */
    Review getReview(int review_key) throws DataLayerException;

    /**
     * Restituisce una lista di recensioni di una data pubblicazione
     *
     * @param publication_key
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Review> getReviews(int publication_key, int limit, int offset) throws DataLayerException;

    /**
     * Restituisce le ultime recensioni inserite
     *
     * @param publication_key
     * @param limit
     * @return
     * @throws DataLayerException
     */
    List<Review> getLastReviews(int publication_key, int limit) throws DataLayerException;

    /**
     * Restituisce i dati di una ristampa a partire dal suo id
     *
     * @param reprint_key
     * @return
     * @throws DataLayerException
     */
    Reprint getReprint(int reprint_key) throws DataLayerException;

    /**
     * Restituisce una lista di ristampe
     *
     * @param publication_key
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Reprint> getReprints(int publication_key, int limit, int offset) throws DataLayerException;

    /**
     * Restituisce i dati di una risorsa a partire dal suo id
     *
     * @param source_key
     * @return
     * @throws DataLayerException
     */
    Source getSource(int source_key) throws DataLayerException;

    /**
     * Restituisce tutte le risorse
     *
     * @return
     * @throws DataLayerException
     */
    List<Source> getSources() throws DataLayerException;

    /**
     * Restituisce tutte le risorse di una data pubblicazione
     *
     * @param publication_key
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Source> getPublicationSources(int publication_key, int limit, int offset) throws DataLayerException;

    /**
     * Restituisce i dati di uno storico a partire dal suo id
     *
     * @param historia_key
     * @return
     * @throws DataLayerException
     */
    History getHistory(int historia_key) throws DataLayerException;

    /**
     * Restituisce tutti gli storici
     *
     * @return
     * @throws DataLayerException
     */
    List<History> getHistories() throws DataLayerException;

    /**
     * Restituisce la lista degli storici in cui ha partecipato un dato utente
     *
     * @param user_key
     * @return
     * @throws DataLayerException
     */
    List<History> getHistoriesByUser(int user_key) throws DataLayerException;

    /**
     * Restituisce la lista degli storici di una data pubblicazione
     *
     * @param publication_key
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<History> getHistoriesByPublication(int publication_key, int limit, int offset) throws DataLayerException;

    /**
     * Restituisce i dati di un utente a partire dal suo id
     *
     * @param user_key
     * @return
     * @throws DataLayerException
     */
    User getUser(int user_key) throws DataLayerException;

    /**
     * Restituisce i dati di un utente a partire dalla sua email
     *
     * @param email
     * @return
     * @throws DataLayerException
     */
    User getUser(String email) throws DataLayerException;

    /**
     * Restituisce i dati di un utente a partire dall'email e password
     *
     * @param email
     * @param password
     * @return
     * @throws DataLayerException
     */
    User getUser(String email, String password) throws DataLayerException;

    /**
     * Restituisce i dati dell'utente amministratore del sistema
     *
     * @return
     * @throws DataLayerException
     */
    User getUserAdministrator() throws DataLayerException;

    /**
     * Restituisce la lista degli utenti che rispettano il filtro passato
     *
     * @param filter
     * @return
     * @throws DataLayerException
     */
    List<User> getUsers(String filter) throws DataLayerException;

    /**
     * Restituisce la lista degli utenti attivi che rispettano il filtro passato
     *
     * @param filter
     * @return
     * @throws DataLayerException
     */
    List<User> getUsersActive(String filter) throws DataLayerException;

    /**
     * Restituisce la lista degli utenti passivi che rispettano il filtro
     * passato
     *
     * @param filter
     * @return
     * @throws DataLayerException
     */
    List<User> getUsersPassive(String filter) throws DataLayerException;

    /**
     * Restituisce la lista degli utenti attivi che hanno effettuato piu'
     * operazioni
     *
     * @return
     * @throws DataLayerException
     */
    List<User> getMoreActiveUsers() throws DataLayerException;

    /**
     * Restituisce i dati di un capitolo a partire dal suo id
     *
     * @param chapter_key
     * @return
     * @throws DataLayerException
     */
    Chapter getChapter(int chapter_key) throws DataLayerException;

    /**
     * Restituisce i dati di una sezione a partire dal suo id
     *
     * @param section_key
     * @return
     * @throws DataLayerException
     */
    Section getSection(int section_key) throws DataLayerException;

    /**
     * Restituisce i capitoli di una data pubblicazione
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    List<Chapter> getChapters(int publication_key) throws DataLayerException;

    /**
     * Restituisce le sezioni di un dato capitolo
     *
     * @param chapter_key
     * @return
     * @throws DataLayerException
     */
    List<Section> getSections(int chapter_key) throws DataLayerException;

    /**
     * Verifica se un utente ha gia' consigliato una pubblicazione
     *
     * @param publication_key
     * @param user_key
     * @return
     * @throws DataLayerException
     */
    boolean getUsersLike(int publication_key, int user_key) throws DataLayerException;

    /**
     * Salva un autore
     *
     * @param author
     * @throws DataLayerException
     */
    void storeAuthor(Author author) throws DataLayerException;

    /**
     * salva un editore
     *
     * @param editor
     * @throws DataLayerException
     */
    void storeEditor(Editor editor) throws DataLayerException;

    /**
     * Salva una keyword
     *
     * @param keyword
     * @throws DataLayerException
     */
    void storeKeyword(Keyword keyword) throws DataLayerException;

    /**
     * Salva una pubblicazione
     *
     * @param publication
     * @throws DataLayerException
     */
    void storePublication(Publication publication) throws DataLayerException;

    /**
     * Salva una recensione
     *
     * @param review
     * @throws DataLayerException
     */
    void storeReview(Review review) throws DataLayerException;

    /**
     * Salva una ristampa
     *
     * @param reprint
     * @throws DataLayerException
     */
    void storeReprint(Reprint reprint) throws DataLayerException;

    /**
     * Salva una risorsa
     *
     * @param source
     * @throws DataLayerException
     */
    void storeSource(Source source) throws DataLayerException;

    /**
     * Salva uno storico
     *
     * @param historia
     * @throws DataLayerException
     */
    void storeHistory(History historia) throws DataLayerException;

    /**
     * Salva un utente
     *
     * @param user
     * @throws DataLayerException
     */
    void storeUser(User user) throws DataLayerException;

    /**
     * Salva un capitolo
     *
     * @param chapter
     * @throws DataLayerException
     */
    void storeChapter(Chapter chapter) throws DataLayerException;

    /**
     * Salva una sezione
     *
     * @param section
     * @throws DataLayerException
     */
    void storeSection(Section section) throws DataLayerException;

    /**
     * Salva un consiglio utente
     *
     * @param publication_key
     * @param user_key
     * @throws DataLayerException
     */
    void storeLike(int publication_key, int user_key) throws DataLayerException;

    /**
     * Elimina un autore
     * 
     * @param author
     * @return
     * @throws DataLayerException 
     */
    int deleteAuthor(Author author) throws DataLayerException;

    /**
     * Elimina un editore
     *
     * @param editor
     * @return
     * @throws DataLayerException
     */
    int deleteEditor(Editor editor) throws DataLayerException;

    /**
     * Elimina una keyword
     *
     * @param keyword
     * @return
     * @throws DataLayerException
     */
    int deleteKeyword(Keyword keyword) throws DataLayerException;

    /**
     * Elimina una pubblicazione
     *
     * @param publication
     * @throws DataLayerException
     */
    void deletePublication(Publication publication) throws DataLayerException;

    /**
     * Elimina una pubblicazione incompleta
     *
     * @throws DataLayerException
     */
    void deleteIncompletePublication() throws DataLayerException;

    /**
     * Elimina una recensione
     *
     * @param review
     * @throws DataLayerException
     */
    void deleteReview(Review review) throws DataLayerException;

    /**
     * Elimina una ristampa
     *
     * @param reprint
     * @throws DataLayerException
     */
    void deleteReprint(Reprint reprint) throws DataLayerException;

    /**
     * Elimina una risorsa
     *
     * @param source
     * @throws DataLayerException
     */
    void deleteSource(Source source) throws DataLayerException;

    /**
     * Elimina un capitolo
     *
     * @param chapter
     * @return
     * @throws DataLayerException
     */
    int deleteChapter(Chapter chapter) throws DataLayerException;

    /**
     * Elimina una sezione
     *
     * @param section
     * @throws DataLayerException
     */
    void deleteSection(Section section) throws DataLayerException;

    /**
     * Elimina uno storico
     *
     * @param history
     * @throws DataLayerException
     */
    void deleteHistory(History history) throws DataLayerException;

    /**
     * Salva la relazione tra una pubblicazione e un autore
     *
     * @param idAuthor
     * @param idPublication
     * @throws DataLayerException
     */
    void storePublicationHasAuthor(int idAuthor, int idPublication) throws DataLayerException;

    /**
     * Elimina tutte le relazioni di una pubblicazione con gli autori
     *
     * @param idPublication
     * @throws DataLayerException
     */
    void deletePublicationHasAuthor(int idPublication) throws DataLayerException;

    /**
     * Salva la relazione tra una pubblicazione e una keyword
     *
     * @param idKeyword
     * @param idPublication
     * @throws DataLayerException
     */
    void storePublicationHasKeyword(int idKeyword, int idPublication) throws DataLayerException;

    /**
     * Elimina tutte le relazioni di una pubblicazione con le keyword
     *
     * @param idPublication
     * @throws DataLayerException
     */
    void deletePublicationHasKeyword(int idPublication) throws DataLayerException;

    /**
     * Elimina una relazione tra un autore e una pubblicazione
     *
     * @param idPublication
     * @param idAuthor
     * @throws DataLayerException
     */
    void deleteAuthorFromPublication(int idPublication, int idAuthor) throws DataLayerException;

    /**
     * Elimina una relazione tra una keyword e una pubblicazione
     *
     * @param idPublication
     * @param idKeyword
     * @throws DataLayerException
     */
    void deleteKeywordFromPublication(int idPublication, int idKeyword) throws DataLayerException;

    /**
     * Elimina tutti i consigli di una pubblicazione
     *
     * @param idPublication
     * @throws DataLayerException
     */
    void deletePublicationLike(int idPublication) throws DataLayerException;

    /**
     * Salva i filtri di una ricerca
     *
     * @param filters
     * @return
     * @throws DataLayerException
     */
    int storeFilters(Map<String, String> filters) throws DataLayerException;

    /**
     * Elimina i filtri di ricerca già usati
     *
     * @throws DataLayerException
     */
    void deleteFilters() throws DataLayerException;

    /**
     * Restituisce i filtri a partire dal suo id
     *
     * @param filter_key
     * @return
     * @throws DataLayerException
     */
    Map<String, String> getFilters(int filter_key) throws DataLayerException;

    /**
     * Restituisce il numero di recensioni di una data pubblicazione
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    int getCountReview(int publication_key) throws DataLayerException;

}
