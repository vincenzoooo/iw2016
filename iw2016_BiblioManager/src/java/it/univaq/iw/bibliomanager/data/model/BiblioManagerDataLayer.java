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
    Author createAuthor();
    Editor createEditor();
    Metadata createMetadata();
    Publication createPublication();
    Review createReview();
    Reprint createReprint();
    Source createSource();
    History createHistory();
    User createUser();
    
    Author getAuthor(int author_key) throws DataLayerException;
    List<Author> getAuthors() throws DataLayerException;
    Editor getEditor(int editor_key) throws DataLayerException;
    List<Editor> getEditors() throws DataLayerException;
    Metadata getMetadata(int metadata_key) throws DataLayerException;
    List<Metadata> getMetadatas(int publication_key) throws DataLayerException;
    Publication getPublication(int publication_key) throws DataLayerException;
    List<Publication> getPublications() throws DataLayerException;
    Review getReview(int review_key) throws DataLayerException;
    List<Review> getReviews(int publication_key) throws DataLayerException;
    Reprint getReprint(int reprint_key) throws DataLayerException;
    List<Reprint> getReprints(int publication_key) throws DataLayerException;
    Source getSource(int source_key) throws DataLayerException;
    List<Source> getSource() throws DataLayerException;
    History getHistory(int historia_key) throws DataLayerException;
    List<History> getHistories() throws DataLayerException;
    List<History> getHistories(User user) throws DataLayerException;
    User getUser(int user_key) throws DataLayerException;
    User getUser(String email) throws DataLayerException;
    User getUser(String email, String password) throws DataLayerException;
    List<User> getUsers() throws DataLayerException;
    List<Author> getPublicationAuthors(int publication_key) throws DataLayerException;
    List<Source> getPublicationSources(int publication_key) throws DataLayerException;
    List<Publication> getLastInsertedPublication () throws DataLayerException;
    List<Publication> getLastModifiedPublication () throws DataLayerException;
    List<User> getMoreActiveUsers () throws DataLayerException;
    
    void storeAuthor(Author author) throws DataLayerException;
    void storeEditor(Editor editor) throws DataLayerException;
    void storeMetadata(Metadata metadata) throws DataLayerException;
    void storePublication(Publication publication) throws DataLayerException;
    void storeReview(Review review) throws DataLayerException;
    void storeReprint(Reprint reprint) throws DataLayerException;
    void storeSource(Source source) throws DataLayerException;
    void storeHistory(History historia) throws DataLayerException;
    void storeUser(User user) throws DataLayerException;
}
