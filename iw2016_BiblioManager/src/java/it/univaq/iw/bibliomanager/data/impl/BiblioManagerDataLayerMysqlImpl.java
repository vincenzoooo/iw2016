/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.data.DataLayerMysqlImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import it.univaq.iw.bibliomanager.data.model.Author;
import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.Metadata;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Review;
import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.User;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class BiblioManagerDataLayerMysqlImpl extends DataLayerMysqlImpl implements BiblioManagerDataLayer {

    //Statements declaration
    private PreparedStatement sUsers, sUserByEmail, sUserByEmailPassword, sUserById;
    private PreparedStatement uUser, iUser;
    private PreparedStatement sHistories, sHistoriesByUser, sHistoryById;
    private PreparedStatement uHistory, iHistory;
    private PreparedStatement sPublications, sPublicationById; // TODO: Select con altri parametri
    private PreparedStatement uPublication, iPublication;
    private PreparedStatement sSources, sSourceById, sSourceByPublication;
    private PreparedStatement uSource, iSource;
    private PreparedStatement sReprints, sReprintById;
    private PreparedStatement uReprint, iReprint;
    private PreparedStatement sEditors, sEditorById;
    private PreparedStatement uEditor, iEditor;
    private PreparedStatement sAuthors, sAuthorById;
    private PreparedStatement uAuthor, iAuthor;
    private PreparedStatement sReviewsByPublication, sReviewById, sReviewByAuthor;
    private PreparedStatement uReview, iReview;
    private PreparedStatement sMetadatasByPublication, sMetadataById;
    private PreparedStatement uMetadata, iMetadata;
    
    public BiblioManagerDataLayerMysqlImpl(DataSource datasource) throws SQLException, NamingException {
        super(datasource);
    }

    @Override
    public void init() throws DataLayerException {
        try {
            super.init();
            //Statement inizializzazione
            this.sUsers = connection.prepareStatement("SELECT * FROM iw2016.utente");
            this.sUserByEmail = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE email = ?");
            this.sUserByEmailPassword = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE email = ? AND password = ?");
            this.sUserById = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE idutente = ?");
            this.uUser = connection.prepareStatement("UPDATE iw2016.utente SET nome = ?, cognome = ?, password = ?, email = ?, stato = ? WHERE idutente = ?");
            this.iUser = connection.prepareStatement("INSERT INTO iw2016.utente (nome, cognome, password, email, stato) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.sHistories = connection.prepareStatement("SELECT * FROM iw2016.storico");
            this.sHistoriesByUser = connection.prepareStatement("SELECT * FROM iw2016.storico WHERE utente = ?");
            this.sHistoryById = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE idstorico = ?");
            
        } catch (SQLException ex) {
            throw new DataLayerException("Error initializing newspaper data layer", ex);
        }
    }
    //Implemented methods

    @Override
    public Author createAuthor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Editor createEditor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Metadata createMetadata() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Publication createPublication() {
        return new PublicationImpl(this);
    }

    public Publication createPublication(ResultSet rs) throws DataLayerException{
        try {
            PublicationImpl publication = new PublicationImpl(this);
            publication.setKey(rs.getInt("idpubblicazione"));
            //TODO
            return publication;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }
    
    @Override
    public Review createReview() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Reprint createReprint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Source createSource() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public History createHistory() {
        return new HistoryImpl(this);
    }

    public History createHistory(ResultSet rs) throws DataLayerException{
        try {
            HistoryImpl history = new HistoryImpl(this);
            history.setKey(rs.getInt("idstorico"));
            history.setEntry(rs.getString("entry"));
            history.setType(rs.getInt("tipo"));
            history.setTimestamp(rs.getTimestamp("timestamp"));
            history.setPublication(getPublication(rs.getInt("pubblicazione")));
            history.setUser(getUser(rs.getInt("utente")));
            return history;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }
    
    @Override
    public User createUser() {
        return new UserImpl(this);
    }

    public User createUser(ResultSet rs) throws DataLayerException {
        try {
            UserImpl user = new UserImpl(this);
            user.setKey(rs.getInt("idutente"));
            user.setName(rs.getString("nome"));
            user.setSurname(rs.getString("cognome"));
            user.setEmail(rs.getString("email"));
            return user;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public Author getAuthor(int author_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Author> getAuthors() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Editor getEditor(int editor_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Editor> getEditors() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Metadata getMetadata(int metadata_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Metadata> getMetadatas() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Publication getPublication(int publication_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Publication> getPublications() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Review getReview(int review_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Review> getReviews() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Reprint getReprint(int reprint_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Reprint> getReprints() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Source getSource(int source_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Source> getSource() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public History getHistory(int historia_key) throws DataLayerException {
        History result = null;
        ResultSet rs = null;
        try {
            sHistoryById.setInt(1, historia_key);
            rs = sHistoryById.executeQuery();
            if (rs.next()) {
                result = createHistory(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load history by id", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                //Nothing to return
            }
        }
        return result;
    }

    @Override
    public List<History> getHistories() throws DataLayerException {
        List<History> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sHistories.executeQuery();
            while (rs.next()) {
                result.add(getHistory(rs.getInt("idstorico")));

            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load histories", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
        return result;
    }

    @Override
    public List<History> getHistories(User user) throws DataLayerException {
        List<History> result = new ArrayList();
        ResultSet rs = null;
        try {
            sHistoriesByUser.setInt(1, user.getKey());
            rs = sHistoriesByUser.executeQuery();
            while (rs.next()) {
                result.add(getHistory(rs.getInt("idstorico")));

            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load histories", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
        return result;
    }
    
    @Override
    public User getUser(int user_key) throws DataLayerException {
        User result = null;
        ResultSet rs = null;
        try {
            sUserById.setInt(1, user_key);
            rs = sUserById.executeQuery();
            if (rs.next()) {
                result = createUser(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load user by id", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                //Nothing to return
            }
        }
        return result;
    }

    @Override
    public User getUser(String email) throws DataLayerException {
        User result = null;
        ResultSet rs = null;
        try {
            sUserByEmail.setString(1, email);
            rs = sUserByEmail.executeQuery();
            if (rs.next()) {
                result = createUser(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load user by Email", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                //Nothing to return
            }
        }
        return result;
    }

    @Override
    public User getUser(String email, String password) throws DataLayerException {
        User result = null;
        ResultSet rs = null;
        try {
            sUserByEmailPassword.setString(1, email);
            sUserByEmailPassword.setString(2, password);
            rs = sUserByEmailPassword.executeQuery();
            if (rs.next()) {
                result = createUser(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load user for login", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                //Nothing to return
            }
        }
        return result;
    }

    @Override
    public List<User> getUsers() throws DataLayerException {
        List<User> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sUsers.executeQuery();
            while (rs.next()) {
                result.add(getUser(rs.getInt("idutente")));

            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load users", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
        return result;
    }

    @Override
    public void storeAuthor(Author author) throws DataLayerException {
        ResultSet keys = null;
        int key = author.getKey();
        try {
            if (author.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                //TODO: Salvataggio anche per la tabella autore_has_pubblicazione
                uAuthor.setString(1, author.getName());
                uAuthor.setString(2, author.getSurname());
                
                uAuthor.executeUpdate();
            } else { //insert
                iAuthor.setString(1, author.getName());
                iAuthor.setString(2, author.getSurname());
                
                if (iAuthor.executeUpdate() == 1) {
                    keys = iAuthor.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                author.copyFrom(getAuthor(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store editor", ex);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void storeEditor(Editor editor) throws DataLayerException {
        ResultSet keys = null;
        int key = editor.getKey();
        try {
            if (editor.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                uEditor.setString(1, editor.getName());
                
                uEditor.executeUpdate();
            } else { //insert
                iEditor.setString(1, editor.getName());
                
                if (iEditor.executeUpdate() == 1) {
                    keys = iEditor.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                editor.copyFrom(getEditor(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store editor", ex);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void storeMetadata(Metadata metadata) throws DataLayerException {
        ResultSet keys = null;
        int key = metadata.getKey();
        try {
            if (metadata.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                uMetadata.setInt(1, metadata.getISBN());
                uMetadata.setInt(2, metadata.getPages());
                uMetadata.setString(3, metadata.getLanguage());
                uMetadata.setDate(4, metadata.getPublicationDate());
                uMetadata.setString(5, metadata.getKeywords());
                uMetadata.setInt(6, metadata.getPublication().getKey());

                uMetadata.executeUpdate();
            } else { //insert
                iMetadata.setInt(1, metadata.getISBN());
                iMetadata.setInt(2, metadata.getPages());
                iMetadata.setString(3, metadata.getLanguage());
                iMetadata.setDate(4, metadata.getPublicationDate());
                iMetadata.setString(5, metadata.getKeywords());
                iMetadata.setInt(6, metadata.getPublication().getKey());

                if (iMetadata.executeUpdate() == 1) {
                    keys = iMetadata.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                metadata.copyFrom(getMetadata(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store metadata", ex);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void storePublication(Publication publication) throws DataLayerException {
        ResultSet keys = null;
        int key = publication.getKey();
        try {
            if (publication.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                uPublication.setString(1, publication.getTitle());
                uPublication.setString(2, publication.getDescription());
                uPublication.setInt(3, publication.getEditor().getKey());
                uPublication.setString(4, publication.getIndex());
                uPublication.setInt(5, publication.getNumberOfLikes());

                uPublication.executeUpdate();
            } else { //insert
                iPublication.setString(1, publication.getTitle());
                iPublication.setString(2, publication.getDescription());
                iPublication.setInt(3, publication.getEditor().getKey());
                iPublication.setString(4, publication.getIndex());
                iPublication.setInt(5, publication.getNumberOfLikes());

                if (iPublication.executeUpdate() == 1) {
                    keys = iPublication.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                publication.copyFrom(getPublication(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store review", ex);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void storeReview(Review review) throws DataLayerException {
        ResultSet keys = null;
        int key = review.getKey();
        try {
            if (review.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                uReview.setString(1, review.getText());
                uReview.setBoolean(2, review.getStatus());
                uReview.setInt(3, review.getAuthor().getKey());
                uReview.setInt(4, review.getPublication().getKey());
                uReview.setInt(5, review.getHistory().getKey());

                uReview.executeUpdate();
            } else { //insert
                iReview.setString(1, review.getText());
                iReview.setBoolean(2, review.getStatus());
                iReview.setInt(3, review.getAuthor().getKey());
                iReview.setInt(4, review.getPublication().getKey());
                iReview.setInt(5, review.getHistory().getKey());

                if (iReview.executeUpdate() == 1) {
                    keys = iReview.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                review.copyFrom(getReview(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store review", ex);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void storeReprint(Reprint reprint) throws DataLayerException {
        ResultSet keys = null;
        int key = reprint.getKey();
        try {
            if (reprint.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                uReprint.setInt(1, reprint.getNumber());
                uReprint.setDate(2, reprint.getDate());
                uReprint.setInt(3, reprint.getPublication().getKey());

                uReprint.executeUpdate();
            } else { //insert
                iReprint.setInt(1, reprint.getNumber());
                iReprint.setDate(2, reprint.getDate());
                iReprint.setInt(3, reprint.getPublication().getKey());

                if (iReprint.executeUpdate() == 1) {
                    keys = iReprint.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                reprint.copyFrom(getReprint(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store reprint", ex);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void storeSource(Source source) throws DataLayerException {
        ResultSet keys = null;
        int key = source.getKey();
        try {
            if (source.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                //TODO: Salvataggio anche per la tabella pubblicazione_has_sorgente
                uSource.setString(1, source.getType());
                uSource.setString(2, source.getURI());
                uSource.setString(3, source.getFormat());
                uSource.setString(4, source.getDescription());

                uSource.executeUpdate();
            } else { //insert
                iSource.setString(1, source.getType());
                iSource.setString(2, source.getURI());
                iSource.setString(3, source.getFormat());
                iSource.setString(4, source.getDescription());

                if (iSource.executeUpdate() == 1) {
                    keys = iSource.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                source.copyFrom(getSource(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store source", ex);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void storeHistory(History historia) throws DataLayerException {
        ResultSet keys = null;
        int key = historia.getKey();
        try {
            if (historia.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                uHistory.setString(1, historia.getEntry());
                uHistory.setInt(2, historia.getType());
                uHistory.setTimestamp(3, historia.getTimestamp());
                uHistory.setInt(4, historia.getPublication().getKey());
                uHistory.setInt(5, historia.getUser().getKey());

                uHistory.executeUpdate();
            } else { //insert
                iHistory.setString(1, historia.getEntry());
                iHistory.setInt(2, historia.getType());
                iHistory.setTimestamp(3, historia.getTimestamp());
                iHistory.setInt(4, historia.getPublication().getKey());
                iHistory.setInt(5, historia.getUser().getKey());

                if (iHistory.executeUpdate() == 1) {
                    keys = iHistory.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                historia.copyFrom(getHistory(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store history", ex);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void storeUser(User user) throws DataLayerException {
        ResultSet keys = null;
        int key = user.getKey();
        try {
            if (user.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                uUser.setString(1, user.getName());
                uUser.setString(2, user.getSurname());
                uUser.setString(3, user.getPassword());
                uUser.setString(4, user.getEmail());
                uUser.setInt(5, user.getState());

                uUser.executeUpdate();
            } else { //insert
                iUser.setString(1, user.getName());
                iUser.setString(2, user.getSurname());
                iUser.setString(3, user.getPassword());
                iUser.setString(4, user.getEmail());
                iUser.setInt(5, user.getState());

                if (iUser.executeUpdate() == 1) {
                    keys = iUser.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                user.copyFrom(getUser(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store user", ex);
        } finally {
            try {
                if (keys != null) {
                    keys.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

}
