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

    Author createAuthor();

    Editor createEditor();

    Keyword createKeyword();

    Publication createPublication();

    Review createReview();

    Reprint createReprint();

    Source createSource();

    History createHistory();

    User createUser();

    Chapter createChapter();

    Section createSection();

    Author getAuthor(int author_key) throws DataLayerException;

    List<Author> getAuthors() throws DataLayerException;

    List<Author> getPublicationAuthors(int publication_key) throws DataLayerException;

    List<Author> getAuthorsByName(String name) throws DataLayerException;

    Editor getEditor(int editor_key) throws DataLayerException;

    List<Editor> getEditors() throws DataLayerException;

    List<Editor> getEditorsByName(String name) throws DataLayerException;

    Keyword getKeyword(int keyword_key) throws DataLayerException;

    List<Keyword> getKeywords() throws DataLayerException;

    List<Keyword> getPublicationKeywords(int publication_key) throws DataLayerException;

    Publication getPublication(int publication_key) throws DataLayerException;

    Publication getPublicationByISBN(String isbn) throws DataLayerException;

    List<Publication> getPublications() throws DataLayerException;

    List<Publication> getLastInsertedPublication() throws DataLayerException;

    List<Publication> getLastModifiedPublication() throws DataLayerException;

    List<Publication> getPublicationsByFilters(Map<String, String> filters) throws DataLayerException;

    Review getReview(int review_key) throws DataLayerException;

    List<Review> getReviews(int publication_key, int limit, int offset) throws DataLayerException;
    
    List<Review> getLastReviews(int publication_key, int limit) throws DataLayerException;

    Reprint getReprint(int reprint_key) throws DataLayerException;

    List<Reprint> getReprints(int publication_key) throws DataLayerException;

    Source getSource(int source_key) throws DataLayerException;

    List<Source> getSources() throws DataLayerException;

    List<Source> getPublicationSources(int publication_key) throws DataLayerException;

    History getHistory(int historia_key) throws DataLayerException;

    List<History> getHistories() throws DataLayerException;

    List<History> getHistoriesByUser(int user_key) throws DataLayerException;

    List<History> getHistoriesByPublication(int publication_key, int limit, int offset) throws DataLayerException;

    User getUser(int user_key) throws DataLayerException;

    User getUser(String email) throws DataLayerException;

    User getUser(String email, String password) throws DataLayerException;

    User getUserAdministrator() throws DataLayerException;

    List<User> getUsers(String filter) throws DataLayerException;

    List<User> getUsersActive(String filter) throws DataLayerException;

    List<User> getUsersPassive(String filter) throws DataLayerException;

    List<User> getMoreActiveUsers() throws DataLayerException;

    Chapter getChapter(int chapter_key) throws DataLayerException;

    Section getSection(int section_key) throws DataLayerException;

    List<Chapter> getChapters(int publication_key) throws DataLayerException;

    List<Section> getSections(int chapter_key) throws DataLayerException;

    boolean getUsersLike(int publication_key, int user_key) throws DataLayerException;
    
    void storeAuthor(Author author) throws DataLayerException;

    void storeEditor(Editor editor) throws DataLayerException;

    void storeKeyword(Keyword keyword) throws DataLayerException;

    void storePublication(Publication publication) throws DataLayerException;

    void storeReview(Review review) throws DataLayerException;

    void storeReprint(Reprint reprint) throws DataLayerException;

    void storeSource(Source source) throws DataLayerException;

    void storeHistory(History historia) throws DataLayerException;

    void storeUser(User user) throws DataLayerException;

    void storeChapter(Chapter chapter) throws DataLayerException;

    void storeSection(Section section) throws DataLayerException;
    
    void storeLike(int publication_key, int user_key) throws DataLayerException;

    void deleteAuthor(Author author) throws DataLayerException;

    void deleteEditor(Editor editor) throws DataLayerException;

    void deleteKeyword(Keyword keyword) throws DataLayerException;

    void deletePublication(Publication publication) throws DataLayerException;

    void deleteIncompletePublication() throws DataLayerException;

    void deleteReview(Review review) throws DataLayerException;

    void deleteReprint(Reprint reprint) throws DataLayerException;

    void deleteSource(Source source) throws DataLayerException;

    void deleteChapter(Chapter chapter) throws DataLayerException;

    void deleteSection(Section section) throws DataLayerException;
    
    void deleteHistory(History history) throws DataLayerException;

    void storePublicationHasAuthor(int idAuthor, int idPublication) throws DataLayerException;

    void deletePublicationHasAuthor(int idPublication) throws DataLayerException;

    void storePublicationHasKeyword(int idKeyword, int idPublication) throws DataLayerException;

    void deletePublicationHasKeyword(int idPublication) throws DataLayerException;
    
    void deleteAuthorFromPublication(int idPublication, int idAuthor) throws DataLayerException;
    
    void deleteKeywordFromPublication(int idPublication, int idKeyword) throws DataLayerException;
    
    void deletePublicationLike(int idPublication) throws DataLayerException;
    
    int storeFilters(Map<String, String> filters) throws DataLayerException;
    
    void deleteFilters() throws DataLayerException;
    
    Map<String, String> getFilters(int filter_key) throws DataLayerException;
    
    int getCountReview(int publication_key) throws DataLayerException;

}
