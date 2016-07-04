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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeEditor(Editor editor) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeMetadata(Metadata metadata) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storePublication(Publication publication) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeReview(Review review) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeReprint(Reprint reprint) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeSource(Source source) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeHistory(History historia) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                uUser.setString(3, user.getEmail());
                uUser.setInt(4, user.getState());

                uUser.executeUpdate();
            } else { //insert
                iUser.setString(1, user.getName());
                iUser.setString(2, user.getSurname());
                iUser.setString(3, user.getPassword());
                iUser.setString(4, user.getEmail());
                iUser.setInt(5, user.getState());

                if (iUser.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    keys = iUser.getGeneratedKeys();
                    //il valore restituito è un ResultSet con un record
                    //per ciascuna chiave generata (uno solo nel nostro caso)
                    //the returned value is a ResultSet with a distinct record for
                    //each generated key (only one in our case)
                    if (keys.next()) {
                        //i campi del record sono le componenti della chiave
                        //(nel nostro caso, un solo intero)
                        //the record fields are the key componenets
                        //(a single integer in our case)
                        key = keys.getInt(1);
                    }
                }
            }
            //restituiamo l'oggetto appena inserito RICARICATO
            //dal database tramite le API del modello. In tal
            //modo terremo conto di ogni modifica apportata
            //durante la fase di inserimento
            //we return the just-inserted object RELOADED from the
            //database through our API. In this way, the resulting
            //object will ambed any data correction performed by
            //the DBMS
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
