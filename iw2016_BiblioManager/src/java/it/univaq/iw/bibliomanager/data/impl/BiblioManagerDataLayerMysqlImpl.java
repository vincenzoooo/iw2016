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
    public Utente getUser(String email, String password) throws DataLayerException{
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
