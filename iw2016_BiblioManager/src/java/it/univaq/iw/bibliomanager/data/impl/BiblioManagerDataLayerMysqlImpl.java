/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.data.impl;

import it.univaq.iw.bibliomanager.data.model.Autore;
import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.Editore;
import it.univaq.iw.bibliomanager.data.model.Metadato;
import it.univaq.iw.bibliomanager.data.model.Pubblicazione;
import it.univaq.iw.bibliomanager.data.model.Recensione;
import it.univaq.iw.bibliomanager.data.model.Ristampa;
import it.univaq.iw.bibliomanager.data.model.Sorgente;
import it.univaq.iw.bibliomanager.data.model.Storico;
import it.univaq.iw.bibliomanager.data.model.Utente;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.data.DataLayerMysqlImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class BiblioManagerDataLayerMysqlImpl extends DataLayerMysqlImpl implements BiblioManagerDataLayer {

    //Statements declaration
    private PreparedStatement sUserByEmail, sUserByEmailPassword;
    private PreparedStatement uUser, iUser;

    public BiblioManagerDataLayerMysqlImpl(DataSource datasource) throws SQLException, NamingException {
        super(datasource);
    }

    @Override
    public void init() throws DataLayerException {
        try {
            super.init();
            //Statement inizializzazione
            this.sUserByEmail = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE email = ?");
            this.sUserByEmailPassword = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE email = ? AND password = ?");
            this.uUser = connection.prepareStatement("UPDATE iw2016.utente SET nome = ?, cognome = ?, password = ?, email = ?, stato = ? WHERE idutente = ?");
            this.iUser = connection.prepareStatement("INSERT INTO iw2016.utente (nome, cognome, password, email, stato) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new DataLayerException("Error initializing newspaper data layer", ex);
        }
    }
    //Implemented methods

    @Override
    public Autore createAuthor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Editore createEditor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Metadato createMetadata() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pubblicazione createPubblicazione() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Recensione createReview() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Ristampa createReprint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sorgente createSource() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Storico createArchive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utente createUser() {
        return new UtenteImpl(this);
    }

    public Utente createUser(ResultSet rs) throws DataLayerException {
        try {
            UtenteImpl user = new UtenteImpl(this);
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
    public Autore getAuthor(int author_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Autore> getAuthors() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Editore getEditor(int editor_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Editore> getEditors() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Metadato getMetadata(int metadata_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Metadato> getMetadatas() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pubblicazione getPublication(int publication_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Pubblicazione> getPublication() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Recensione getReview(int review_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Recensione> getReviews() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Ristampa getReprint(int reprint_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Ristampa> getReprints() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sorgente getSource(int source_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Sorgente> getSource() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Storico getArchive(int historia_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Storico> getArchives() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utente getUser(int user_key) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utente getUser(String email) throws DataLayerException {
        Utente result = null;
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
    public Utente getUser(String email, String password) throws DataLayerException {
        Utente result = null;
        ResultSet rs = null;
        try {
            sUserByEmailPassword.setString(1, email);
            sUserByEmailPassword.setString(2, password);
            rs = sUserByEmailPassword.executeQuery();
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
    public List<Utente> getUsers() throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeAuthor(Autore author) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeEditor(Editore editor) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeMetadata(Metadato metadata) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storePublication(Pubblicazione publication) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeReview(Recensione review) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeReprint(Ristampa reprint) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeSource(Sorgente source) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeArchive(Storico historia) throws DataLayerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeUser(Utente user) throws DataLayerException {
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
                iUser.setString(3, user.getEmail());
                iUser.setInt(4, user.getState());

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
//            if (key > 0) {
//                user.copyFrom(getArticle(key));
//            }
//            article.setDirty(false);
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
