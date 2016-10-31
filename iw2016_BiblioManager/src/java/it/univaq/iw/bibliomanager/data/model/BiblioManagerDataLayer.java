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
     * Create a new Author
     *
     * @return Nuovo autore
     */
    Author createAuthor();

    /**
     * Create a new Editor
     *
     * @return Nuovo editore
     */
    Editor createEditor();

    /**
     * Create a new Keyword
     *
     * @return Nuova keyword
     */
    Keyword createKeyword();

    /**
     * Create a new Publication
     *
     * @return Nuova pubblicazione
     */
    Publication createPublication();

    /**
     * Create a new Revision
     *
     * @return Nuova recensione
     */
    Review createReview();

    /**
     * Create a new Review
     *
     * @return Nuova ristampa
     */
    Reprint createReprint();

    /**
     * Create a new Resource
     *
     * @return Nuova risorsa
     */
    Source createSource();

    /**
     * Create a new History
     *
     * @return Nuovo storico
     */
    History createHistory();

    /**
     * Create a new User
     *
     * @return Nuovo utente
     */
    User createUser();

    /**
     * Create a new Chapter
     *
     * @return Nuovo capitolo
     */
    Chapter createChapter();

    /**
     * Create a new Section
     *
     * @return Nuova sezione
     */
    Section createSection();

    /**
     * Get an Author by ID
     *
     * @param author_key
     * @return
     * @throws DataLayerException
     */
    Author getAuthor(int author_key) throws DataLayerException;

    /**
     * Get an Author list
     *
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Author> getAuthors(int limit, int offset) throws DataLayerException;

    /**
     * Get all Publication's Authors
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    List<Author> getPublicationAuthors(int publication_key) throws DataLayerException;

    /**
     * Get Authors by name
     *
     * @param name
     * @return
     * @throws DataLayerException
     */
    List<Author> getAuthorsByName(String name) throws DataLayerException;

    /**
     * Get an Editor by ID
     *
     * @param editor_key
     * @return
     * @throws DataLayerException
     */
    Editor getEditor(int editor_key) throws DataLayerException;

    /**
     * Get an Editors list
     *
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Editor> getEditors(int limit, int offset) throws DataLayerException;

    /**
     * Get Editors by name
     *
     * @param name
     * @return
     * @throws DataLayerException
     */
    List<Editor> getEditorsByName(String name) throws DataLayerException;

    /**
     * Get a Keyword by ID
     *
     * @param keyword_key
     * @return
     * @throws DataLayerException
     */
    Keyword getKeyword(int keyword_key) throws DataLayerException;

    /**
     * Get a Keywords list
     *
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Keyword> getKeywords(int limit, int offset) throws DataLayerException;

    /**
     * Get all Publication's Keywords
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    List<Keyword> getPublicationKeywords(int publication_key) throws DataLayerException;

    /**
     * Get a Publication by ID
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    Publication getPublication(int publication_key) throws DataLayerException;

    /**
     * Get a Publication by ISBN
     *
     * @param isbn
     * @return
     * @throws DataLayerException
     */
    Publication getPublicationByISBN(String isbn) throws DataLayerException;

    /**
     * Get all Publications
     *
     * @return
     * @throws DataLayerException
     */
    List<Publication> getPublications() throws DataLayerException;

    /**
     * Get the last inserted Publication
     *
     * @return
     * @throws DataLayerException
     */
    List<Publication> getLastInsertedPublication() throws DataLayerException;

    /**
     * Get the last modified Publication
     *
     * @return
     * @throws DataLayerException
     */
    List<Publication> getLastModifiedPublication() throws DataLayerException;

    /**
     * Get a Publication's filter
     *
     * @param filters
     * @return
     * @throws DataLayerException
     */
    List<Publication> getPublicationsByFilters(Map<String, String> filters) throws DataLayerException;

    /**
     * Get a Review by ID
     *
     * @param review_key
     * @return
     * @throws DataLayerException
     */
    Review getReview(int review_key) throws DataLayerException;

    /**
     * Get a Reviews list by Publication
     *
     * @param publication_key
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Review> getReviews(int publication_key, int limit, int offset) throws DataLayerException;

    /**
     * Get the last Review
     *
     * @param publication_key
     * @param limit
     * @return
     * @throws DataLayerException
     */
    List<Review> getLastReviews(int publication_key, int limit) throws DataLayerException;

    /**
     * Get a Review by ID
     *
     * @param reprint_key
     * @return
     * @throws DataLayerException
     */
    Reprint getReprint(int reprint_key) throws DataLayerException;

    /**
     * Get a Review list
     *
     * @param publication_key
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Reprint> getReprints(int publication_key, int limit, int offset) throws DataLayerException;

    /**
     * Get a Source by ID
     *
     * @param source_key
     * @return
     * @throws DataLayerException
     */
    Source getSource(int source_key) throws DataLayerException;

    /**
     * Get all Source
     *
     * @return
     * @throws DataLayerException
     */
    List<Source> getSources() throws DataLayerException;

    /**
     * Get a list Source by Publication
     *
     * @param publication_key
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<Source> getPublicationSources(int publication_key, int limit, int offset) throws DataLayerException;

    /**
     * Get an History by ID
     *
     * @param historia_key
     * @return
     * @throws DataLayerException
     */
    History getHistory(int historia_key) throws DataLayerException;

    /**
     * Get a list of History
     *
     * @return
     * @throws DataLayerException
     */
    List<History> getHistories() throws DataLayerException;

    /**
     * Get an Historys list by User
     *
     * @param user_key
     * @return
     * @throws DataLayerException
     */
    List<History> getHistoriesByUser(int user_key) throws DataLayerException;

    /**
     * Get an Historys list by Publication
     *
     * @param publication_key
     * @param limit
     * @param offset
     * @return
     * @throws DataLayerException
     */
    List<History> getHistoriesByPublication(int publication_key, int limit, int offset) throws DataLayerException;

    /**
     * Get an User by ID
     *
     * @param user_key
     * @return
     * @throws DataLayerException
     */
    User getUser(int user_key) throws DataLayerException;

    /**
     * Get an User by email
     *
     * @param email
     * @return
     * @throws DataLayerException
     */
    User getUser(String email) throws DataLayerException;

    /**
     * Get an User by email and password
     *
     * @param email
     * @param password
     * @return
     * @throws DataLayerException
     */
    User getUser(String email, String password) throws DataLayerException;

    /**
     * Get the Admin
     *
     * @return
     * @throws DataLayerException
     */
    User getUserAdministrator() throws DataLayerException;

    /**
     * Get an User list by filter
     *
     * @param filter
     * @return
     * @throws DataLayerException
     */
    List<User> getUsers(String filter) throws DataLayerException;

    /**
     * Get an active User list by filter
     *
     * @param filter
     * @return
     * @throws DataLayerException
     */
    List<User> getUsersActive(String filter) throws DataLayerException;

    /**
     * Get a passive User list by filter passato
     *
     * @param filter
     * @return
     * @throws DataLayerException
     */
    List<User> getUsersPassive(String filter) throws DataLayerException;

    /**
     * Get the most active Users
     *
     * @return
     * @throws DataLayerException
     */
    List<User> getMoreActiveUsers() throws DataLayerException;

    /**
     * Get a Chapter by ID
     *
     * @param chapter_key
     * @return
     * @throws DataLayerException
     */
    Chapter getChapter(int chapter_key) throws DataLayerException;

    /**
     * Get a Section by ID
     *
     * @param section_key
     * @return
     * @throws DataLayerException
     */
    Section getSection(int section_key) throws DataLayerException;

    /**
     * Get all Chapters of a Publication
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    List<Chapter> getChapters(int publication_key) throws DataLayerException;

    /**
     * Get all Sections of a Chapter
     *
     * @param chapter_key
     * @return
     * @throws DataLayerException
     */
    List<Section> getSections(int chapter_key) throws DataLayerException;

    /**
     * Verify if a User liked a Publication
     *
     * @param publication_key
     * @param user_key
     * @return
     * @throws DataLayerException
     */
    boolean getUsersLike(int publication_key, int user_key) throws DataLayerException;

    /**
     * Save an Author
     *
     * @param author
     * @throws DataLayerException
     */
    void storeAuthor(Author author) throws DataLayerException;

    /**
     * Save an Editor
     *
     * @param editor
     * @throws DataLayerException
     */
    void storeEditor(Editor editor) throws DataLayerException;

    /**
     * Save a Keyword
     *
     * @param keyword
     * @throws DataLayerException
     */
    void storeKeyword(Keyword keyword) throws DataLayerException;

    /**
     * Save a Publication
     *
     * @param publication
     * @throws DataLayerException
     */
    void storePublication(Publication publication) throws DataLayerException;

    /**
     * Save a Review
     *
     * @param review
     * @throws DataLayerException
     */
    void storeReview(Review review) throws DataLayerException;

    /**
     * Save a Reprint
     *
     * @param reprint
     * @throws DataLayerException
     */
    void storeReprint(Reprint reprint) throws DataLayerException;

    /**
     * Save a Source
     *
     * @param source
     * @throws DataLayerException
     */
    void storeSource(Source source) throws DataLayerException;

    /**
     * Save a History
     *
     * @param historia
     * @throws DataLayerException
     */
    void storeHistory(History historia) throws DataLayerException;

    /**
     * Save a User
     *
     * @param user
     * @throws DataLayerException
     */
    void storeUser(User user) throws DataLayerException;

    /**
     * Save a Chapter
     *
     * @param chapter
     * @throws DataLayerException
     */
    void storeChapter(Chapter chapter) throws DataLayerException;

    /**
     * Save a Section
     *
     * @param section
     * @throws DataLayerException
     */
    void storeSection(Section section) throws DataLayerException;

    /**
     * Save an User Like
     *
     * @param publication_key
     * @param user_key
     * @throws DataLayerException
     */
    void storeLike(int publication_key, int user_key) throws DataLayerException;

    /**
     * Delete an Author
     *
     * @param author
     * @return
     * @throws DataLayerException
     */
    int deleteAuthor(Author author) throws DataLayerException;

    /**
     * Delete an Editor
     *
     * @param editor
     * @return
     * @throws DataLayerException
     */
    int deleteEditor(Editor editor) throws DataLayerException;

    /**
     * Delete a Keyword
     *
     * @param keyword
     * @return
     * @throws DataLayerException
     */
    int deleteKeyword(Keyword keyword) throws DataLayerException;

    /**
     * Delete a Publication
     *
     * @param publication
     * @throws DataLayerException
     */
    void deletePublication(Publication publication) throws DataLayerException;

    /**
     * Delete an incomplete Publication
     *
     * @throws DataLayerException
     */
    void deleteIncompletePublication() throws DataLayerException;

    /**
     * Delete a Review
     *
     * @param review
     * @throws DataLayerException
     */
    void deleteReview(Review review) throws DataLayerException;

    /**
     * Delete a Review
     *
     * @param reprint
     * @throws DataLayerException
     */
    void deleteReprint(Reprint reprint) throws DataLayerException;

    /**
     * Delete a Source
     *
     * @param source
     * @throws DataLayerException
     */
    void deleteSource(Source source) throws DataLayerException;

    /**
     * Delete a Chapter
     *
     * @param chapter
     * @return
     * @throws DataLayerException
     */
    int deleteChapter(Chapter chapter) throws DataLayerException;

    /**
     * Delete a Section
     *
     * @param section
     * @throws DataLayerException
     */
    void deleteSection(Section section) throws DataLayerException;

    /**
     * Delete a History
     *
     * @param history
     * @throws DataLayerException
     */
    void deleteHistory(History history) throws DataLayerException;

    /**
     * Save a link between an Author and a Publication
     *
     * @param idAuthor
     * @param idPublication
     * @throws DataLayerException
     */
    void storePublicationHasAuthor(int idAuthor, int idPublication) throws DataLayerException;

    /**
     * Delete all relations between a Publication and the Authors
     *
     * @param idPublication
     * @throws DataLayerException
     */
    void deletePublicationHasAuthor(int idPublication) throws DataLayerException;

    /**
     * Save a relation between a Keyword and a Publication
     *
     * @param idKeyword
     * @param idPublication
     * @throws DataLayerException
     */
    void storePublicationHasKeyword(int idKeyword, int idPublication) throws DataLayerException;

    /**
     * Delete all relations between a Publication and the Keywords
     *
     * @param idPublication
     * @throws DataLayerException
     */
    void deletePublicationHasKeyword(int idPublication) throws DataLayerException;

    /**
     * Delete a link between an Author and a Publication
     *
     * @param idPublication
     * @param idAuthor
     * @throws DataLayerException
     */
    void deleteAuthorFromPublication(int idPublication, int idAuthor) throws DataLayerException;

    /**
     * Delete a link between a Keyword and a Publication
     *
     * @param idPublication
     * @param idKeyword
     * @throws DataLayerException
     */
    void deleteKeywordFromPublication(int idPublication, int idKeyword) throws DataLayerException;

    /**
     * Delete all likes from a Publication
     *
     * @param idPublication
     * @throws DataLayerException
     */
    void deletePublicationLike(int idPublication) throws DataLayerException;

    /**
     * Save a research filter
     *
     * @param filters
     * @return
     * @throws DataLayerException
     */
    int storeFilters(Map<String, String> filters) throws DataLayerException;

    /**
     * Delete the used filters
     *
     * @throws DataLayerException
     */
    void deleteFilters() throws DataLayerException;

    /**
     * Get a filter by ID
     *
     * @param filter_key
     * @return
     * @throws DataLayerException
     */
    Map<String, String> getFilters(int filter_key) throws DataLayerException;

    /**
     * Get the number of Reviews of a Publication
     *
     * @param publication_key
     * @return
     * @throws DataLayerException
     */
    int getCountReview(int publication_key) throws DataLayerException;

}
