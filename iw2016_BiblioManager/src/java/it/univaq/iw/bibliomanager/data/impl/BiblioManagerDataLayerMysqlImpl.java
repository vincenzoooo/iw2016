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
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Review;
import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.User;
import java.sql.Date;
import java.util.Calendar;
import it.univaq.iw.bibliomanager.data.model.Keyword;
import it.univaq.iw.framework.utils.Utils;
import java.util.Map;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class BiblioManagerDataLayerMysqlImpl extends DataLayerMysqlImpl implements BiblioManagerDataLayer {

    //Statements declaration
    private PreparedStatement sUsers, sUserByEmail, sUserByEmailPassword, sUserById, sUserByNumberOfPublications, sUserAdministrator, sUsersByStatus;
    private PreparedStatement uUser, iUser;
    private PreparedStatement sHistories, sHistoriesByUser, sHistoriesByPublication, sHistoryById;
    private PreparedStatement uHistory, iHistory;
    private PreparedStatement sPublications, sPublicationById, sPublicationsByInsertDate, sPublicationsByUpdateDate, sPublicationsByFilters, sPublicationsByISBN;
    private PreparedStatement uPublication, iPublication;
    private PreparedStatement sSources, sSourceById, sSourceByPublication;
    private PreparedStatement uSource, iSource;
    private PreparedStatement sReprintsByPublication, sReprintById;
    private PreparedStatement uReprint, iReprint;
    private PreparedStatement sEditors, sEditorsByName, sEditorById;
    private PreparedStatement uEditor, iEditor;
    private PreparedStatement sAuthors, sAuthorsByName, sAuthorById, sAuthorByPublication;
    private PreparedStatement uAuthor, iAuthor;
    private PreparedStatement sReviewsByPublication, sReviewById;
    private PreparedStatement uReview, iReview;
    private PreparedStatement sKeywords, sKeywordsByPublication, sKeywordById;
    private PreparedStatement uKeyword, iKeyword;
    private PreparedStatement sPublicationHasAuthor, sPublicationHasAuthorByPublication, sPublicationHasAuthorByAuthor;
    private PreparedStatement sPublicationHasSource, sPublicationHasSourceByPublication, sPublicationHasSourceBySource;
    private PreparedStatement sPublicationHasKeyword, sPublicationHasKeywordByPublication, sPublicationHasKeywordByKeyword;
    private PreparedStatement iPublicationHasAuthor, iPublicationHasSource, iPublicationHasKeyword;
    private PreparedStatement dPublicationHasAuthor, dPublicationHasSource, dPublicationHasKeyword;

    public BiblioManagerDataLayerMysqlImpl(DataSource datasource) throws SQLException, NamingException {
        super(datasource);
    }

    @Override
    public void init() throws DataLayerException {
        try {
            super.init();
            //Statement inizializzazione
            this.sUsers = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE cognome LIKE ? ORDER BY cognome");
            this.sUserByEmail = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE email = ?");
            this.sUserByEmailPassword = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE email = ? AND password = ?");
            this.sUserById = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE idutente = ?");
            this.sUserByNumberOfPublications = connection.prepareStatement("SELECT utente, COUNT(*) AS operazioni FROM storico JOIN utente ON idutente = utente GROUP BY utente HAVING operazioni > 1 ORDER BY operazioni");
            this.sUserAdministrator = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE stato = 0 ORDER BY cognome");
            this.sUsersByStatus = connection.prepareStatement("SELECT * FROM iw2016.utente WHERE cognome LIKE ? AND stato = ? ORDER BY cognome");
            this.uUser = connection.prepareStatement("UPDATE iw2016.utente SET nome = ?, cognome = ?, password = ?, email = ?, stato = ? WHERE idutente = ?");
            this.iUser = connection.prepareStatement("INSERT INTO iw2016.utente (nome, cognome, password, email, stato) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.sHistories = connection.prepareStatement("SELECT * FROM iw2016.storico");
            this.sHistoriesByUser = connection.prepareStatement("SELECT * FROM iw2016.storico WHERE utente = ?");
            this.sHistoriesByPublication = connection.prepareStatement("SELECT * FROM iw2016.storico WHERE pubblicazione = ?");
            this.sHistoryById = connection.prepareStatement("SELECT * FROM iw2016.storico WHERE idstorico = ?");
            this.uHistory = connection.prepareStatement("UPDATE iw2016.storico SET entry = ?, tipo = ?, data_operazione = ?, pubblicazione = ?, utente = ? WHERE idstorico = ?");
            this.iHistory = connection.prepareStatement("INSERT INTO iw2016.storico (entry, tipo, data_operazione, pubblicazione, utente) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.sPublications = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione WHERE incompleta = 0 ORDER BY ?");
            this.sPublicationById = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione WHERE idpubblicazione = ? AND incompleta = ?");
            this.sPublicationsByInsertDate = connection.prepareStatement("SELECT pubblicazione FROM storico WHERE data_operazione >= ? AND data_operazione <= ? AND tipo = 0");
            this.sPublicationsByUpdateDate = connection.prepareStatement("SELECT pubblicazione FROM storico WHERE data_operazione >= ? AND data_operazione <= ? AND tipo = 1");
            this.sPublicationsByFilters = connection.prepareStatement("SELECT * FROM pubblicazione p LEFT JOIN ristampa r ON r.pubblicazione = p.idpubblicazione LEFT JOIN editore e ON e.ideditore = p.editore " + 
                    "LEFT JOIN autore_has_pubblicazione ap ON ap.pubblicazione_idpubblicazione = p.idpubblicazione LEFT JOIN autore a ON a.idautore = ap.autore_idautore LEFT JOIN pubblicazione_has_sorgente ps ON ps.pubblicazione_idpubblicazione = p.idpubblicazione " + 
                    "LEFT JOIN sorgente sr ON sr.idsorgente = ps.sorgente_idsorgente LEFT JOIN pubblicazione_has_keyword pk ON pk.pubblicazione_idpubblicazione = p.idpubblicazione LEFT JOIN keyword k ON k.idkeyword = pk.keyword_idkeyword " +
                    "LEFT JOIN storico st ON st.pubblicazione = p.idpubblicazione LEFT JOIN utente u ON u.idutente = st.utente " + 
                    "WHERE p.isbn LIKE ? AND p.titolo LIKE ? AND concat(a.nome, a.cognome) LIKE ? AND e.nome LIKE ? AND k.nome LIKE ? AND lingua LIKE ? AND p.data_pubblicazione >= ? AND p.data_pubblicazione < ? AND p.incompleta = 0 "+
                    "ORDER BY ?");
            this.sPublicationsByISBN = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione WHERE isbn = ? AND incompleta = 0");
            this.uPublication = connection.prepareStatement("UPDATE iw2016.pubblicazione SET titolo = ?, descrizione = ?, editore = ?, indice = ?, n_consigli = ? , isbn = ?, n_pagine = ?, lingua = ?, data_pubblicazione = ?, incompleta = ? WHERE idpubblicazione = ?");
            this.iPublication = connection.prepareStatement("INSERT INTO iw2016.pubblicazione (titolo, descrizione, editore, indice, n_consigli, isbn, n_pagine, lingua, data_pubblicazione, incompleta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.sSources = connection.prepareStatement("SELECT * FROM iw2016.sorgente");
            this.sSourceById = connection.prepareStatement("SELECT * FROM iw2016.sorgente WHERE idsorgente = ?");
            this.sSourceByPublication = connection.prepareStatement("SELECT * FROM iw2016.sorgente JOIN pubblicazione_has_sorgente ON idsorgente = sorgente_idsorgente WHERE pubblicazione_idpubblicazione = ?");
            this.uSource = connection.prepareStatement("UPDATE iw2016.sorgente SET tipo = ?, URI = ?, formato = ?, descrizione = ? WHERE idsorgente = ?");
            this.iSource = connection.prepareStatement("INSERT INTO iw2016.sorgente (tipo, URI, formato, descrizione) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.sReprintsByPublication = connection.prepareStatement("SELECT * FROM iw2016.ristampa WHERE pubblicazione = ?");
            this.sReprintById = connection.prepareStatement("SELECT * FROM iw2016.ristampa WHERE idristampa = ?");
            this.uReprint = connection.prepareStatement("UPDATE iw2016.ristampa SET numero = ?, data = ?, pubblicazione = ? WHERE idristampa = ?");
            this.iReprint = connection.prepareStatement("INSERT INTO iw2016.ristampa (numero, data, pubblicazione) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.sEditors = connection.prepareStatement("SELECT * FROM iw2016.editore");
            this.sEditorsByName = connection.prepareStatement("SELECT * FROM iw2016.editor WHERE nome LIKE '%?%'");
            this.sEditorById = connection.prepareStatement("SELECT * FROM iw2016.editore WHERE ideditore = ?");
            this.uEditor = connection.prepareStatement("UPDATE iw2016.editore SET nome = ? WHERE ideditore = ?");
            this.iEditor = connection.prepareStatement("INSERT INTO iw2016.editore (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            this.sAuthors = connection.prepareStatement("SELECT * FROM iw2016.autore");
            this.sAuthorsByName = connection.prepareStatement("SELECT * FROM iw2016.autore WHERE nome LIKE '%?%'");
            this.sAuthorById = connection.prepareStatement("SELECT * FROM iw2016.autore WHERE idautore = ?");
            this.sAuthorByPublication = connection.prepareStatement("SELECT * FROM iw2016.autore JOIN autore_has_pubblicazione ON idautore = autore_idautore WHERE pubblicazione_idpubblicazione = ?");
            this.uAuthor = connection.prepareStatement("UPDATE iw2016.autore SET nome = ?, cognome = ? WHERE idautore = ?");
            this.iAuthor = connection.prepareStatement("INSERT INTO iw2016.autore (nome, cognome) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.sReviewsByPublication = connection.prepareStatement("SELECT * FROM iw2016.recensione WHERE pubblicazione = ? AND moderata = 1");
            this.sReviewById = connection.prepareStatement("SELECT * FROM iw2016.recensione WHERE idrecensione = ?");
            this.uReview = connection.prepareStatement("UPDATE iw2016.recensione SET testo = ?, moderata = ?, utente_autore = ?, pubblicazione = ?, storico = ? WHERE idrecensione = ?");
            this.iReview = connection.prepareStatement("INSERT INTO iw2016.recensione (testo, moderata, utente_autore, pubblicazione, storico) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.sKeywords = connection.prepareStatement("SELECT * FROM iw2016.keyword");
            this.sKeywordById = connection.prepareStatement("SELECT * FROM iw2016.keyword WHERE idkeyword = ?");
            this.sKeywordsByPublication = connection.prepareStatement("SELECT * FROM iw2016.keyword JOIN pubblicazione_has_keyword ON idkeyword = keyword_idkeyword WHERE pubblicazione_idpubblicazione = ?");
            this.uKeyword = connection.prepareStatement("UPDATE iw2016.keyword SET nome = ? WHERE idkeyword = ?");
            this.iKeyword = connection.prepareStatement("INSERT INTO iw2016.keyword (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            this.sPublicationHasAuthor = connection.prepareStatement("SELECT * FROM iw2016.autore_has_pubblicazione");
            this.sPublicationHasAuthorByAuthor = connection.prepareStatement("SELECT * FROM iw2016.autore_has_pubblicazione WHERE autore_idautore = ?");
            this.sPublicationHasAuthorByPublication = connection.prepareStatement("SELECT * FROM iw2016.autore_has_pubblicazione WHERE pubblicazione_idpubblicazione = ?");
            this.sPublicationHasSource = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione_has_sorgente");
            this.sPublicationHasSourceByPublication = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione_has_sorgente WHERE pubblicazione_idpubblicazione = ?");
            this.sPublicationHasSourceBySource = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione_has_sorgente WHERE sorgente_idsorgente = ?");
            this.sPublicationHasKeyword = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione_has_keyword");
            this.sPublicationHasKeywordByPublication = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione_has_keyword WHERE pubblicazione_idpubblicazione = ?");
            this.sPublicationHasKeywordByKeyword = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione_has_keyword WHERE keyword_idkeyword = ?");
            this.iPublicationHasAuthor = connection.prepareStatement("INSERT INTO iw2016.autore_has_pubblicazione(autore_idautore,pubblicazione_idpubblicazione) VALUES (?,?)");
            this.dPublicationHasAuthor = connection.prepareStatement("DELETE FROM iw2016.autore_has_pubblicazione WHERE pubblicazione_idpubblicazione = ?");
            this.iPublicationHasKeyword = connection.prepareStatement("INSERT INTO iw2016.pubblicazione_has_keyword (pubblicazione_idpubblicazione, keyword_idkeyword) VALUES (?,?)");
            this.dPublicationHasKeyword = connection.prepareStatement("DELETE FROM iw2016.pubblicazione_has_keyword WHERE pubblicazione_idpubblicazione = ?");
            this.iPublicationHasSource = connection.prepareStatement("INSERT INTO iw2016.pubblicazione_has_sorgente (pubblicazione_idpubblicazione, sorgente_idsorgente) VALUES (?,?)");
            this.dPublicationHasSource = connection.prepareStatement("DELETE FROM iw2016.pubblicazione_has_sorgente WHERE pubblicazione_idpubblicazione = ?");
        } catch (SQLException ex) {
            throw new DataLayerException("Error initializing newspaper data layer", ex);
        }
    }
    //Implemented methods

    @Override
    public Author createAuthor() {
        return new AuthorImpl(this);
    }

    public Author createAuthor(ResultSet rs) throws DataLayerException {
        try {
            AuthorImpl author = new AuthorImpl(this);
            author.setKey(rs.getInt("idautore"));
            author.setName(rs.getString("nome"));
            author.setSurname(rs.getString("cognome"));
            return author;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public Editor createEditor() {
        return new EditorImpl(this);
    }

    public Editor createEditor(ResultSet rs) throws DataLayerException {
        try {
            EditorImpl editor = new EditorImpl(this);
            editor.setKey(rs.getInt("ideditore"));
            editor.setName(rs.getString("nome"));
            return editor;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public Keyword createKeyword() {
        return new KeywordImpl(this);
    }

    public Keyword createKeyword(ResultSet rs) throws DataLayerException {
        try {
            KeywordImpl keyword = new KeywordImpl(this);
            keyword.setKey(rs.getInt("idkeyword"));
            keyword.setName(rs.getString("nome"));
            return keyword;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public Publication createPublication() {
        return new PublicationImpl(this);
    }

    public Publication createPublication(ResultSet rs) throws DataLayerException {
        try {
            PublicationImpl publication = new PublicationImpl(this);
            publication.setKey(rs.getInt("idpubblicazione"));
            publication.setTitle(rs.getString("titolo"));
            publication.setDescription(rs.getString("descrizione"));
            publication.setEditor(getEditor(rs.getInt("editore")));
            publication.setIndex(rs.getString("indice"));
            publication.setLike(rs.getInt("n_consigli"));
            publication.setIsbn(rs.getString("isbn"));
            publication.setLanguage(rs.getString("lingua"));
            publication.setPublicationDate(rs.getDate("data_pubblicazione"));
            publication.setPageNumber(rs.getInt("n_pagine"));
            publication.setEditor(getEditor(rs.getInt("editore")));
            publication.setAuthor(getPublicationAuthors(rs.getInt("idpubblicazione")));
            publication.setSources(getPublicationSources(rs.getInt("idpubblicazione")));
            publication.setKeywords(getPublicationKeywords(rs.getInt("idpubblicazione")));
            publication.setReprints(getReprints(rs.getInt("idpubblicazione")));
            publication.setIncomplete(rs.getBoolean("incompleta"));
            return publication;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create publication object form ResultSet", ex);
        }
    }

    @Override
    public Review createReview() {
        return new ReviewImpl(this);
    }

    public Review createReview(ResultSet rs) throws DataLayerException {
        try {
            ReviewImpl review = new ReviewImpl(this);
            review.setKey(rs.getInt("idrecensione"));
            review.setText(rs.getString("testo"));
            review.setStatus(rs.getBoolean("moderata"));
            review.setAuthor(getUser(rs.getInt("utente_autore")));
            review.setPublication(getPublication(rs.getInt("pubblicazione")));
            review.setHistory(getHistory(rs.getInt("storico")));
            return review;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public Reprint createReprint() {
        return new ReprintImpl(this);
    }

    public Reprint createReprint(ResultSet rs) throws DataLayerException {
        try {
            ReprintImpl reprint = new ReprintImpl(this);
            reprint.setKey(rs.getInt("idristampa"));
            reprint.setNumber(rs.getInt("numero"));
            reprint.setDate(rs.getDate("data"));
            reprint.setPublication(getPublication(rs.getInt("pubblicazione")));
            return reprint;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public Source createSource() {
        return new SourceImpl(this);
    }

    public Source createSource(ResultSet rs) throws DataLayerException {
        try {
            SourceImpl source = new SourceImpl(this);
            source.setKey(rs.getInt("idsorgente"));
            source.setType(rs.getString("tipo"));
            source.setUri(rs.getString("URI"));
            source.setFormat(rs.getString("formato"));
            source.setDescription(rs.getString("descrizione"));
            return source;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public History createHistory() {
        return new HistoryImpl(this);
    }

    public History createHistory(ResultSet rs) throws DataLayerException {
        try {
            HistoryImpl history = new HistoryImpl(this);
            history.setKey(rs.getInt("idstorico"));
            history.setEntry(rs.getString("entry"));
            history.setType(rs.getInt("tipo"));
            history.setDate(rs.getDate("data_operazione"));
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
            user.setState(rs.getInt("stato"));
            user.setPassword(rs.getString("password"));
            return user;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public Author getAuthor(int author_key) throws DataLayerException {
        Author result = null;
        ResultSet rs = null;
        try {
            sAuthorById.setInt(1, author_key);
            rs = sAuthorById.executeQuery();
            if (rs.next()) {
                result = createAuthor(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load author by id", ex);
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
    public List<Author> getAuthors() throws DataLayerException {
        List<Author> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sAuthors.executeQuery();
            while (rs.next()) {
                result.add(getAuthor(rs.getInt("idautore")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load autors", ex);
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
    public List<Author> getAuthorsByName(String name) throws DataLayerException {
        List<Author> result = new ArrayList();
        ResultSet rs = null;
        try {
            sAuthorsByName.setString(1, name);
            rs = sAuthorsByName.executeQuery();
            while (rs.next()) {
                result.add(getAuthor(rs.getInt("idautore")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load autors", ex);
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
    public Editor getEditor(int editor_key) throws DataLayerException {
        Editor result = null;
        ResultSet rs = null;
        try {
            sEditorById.setInt(1, editor_key);
            rs = sEditorById.executeQuery();
            if (rs.next()) {
                result = createEditor(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load editor by id", ex);
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
    public List<Editor> getEditors() throws DataLayerException {
        List<Editor> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sEditors.executeQuery();
            while (rs.next()) {
                result.add(getEditor(rs.getInt("ideditore")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load editors", ex);
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
    public List<Editor> getEditorsByName(String name) throws DataLayerException {
        List<Editor> result = new ArrayList();
        ResultSet rs = null;
        try {
            sEditorsByName.setString(1, name);
            rs = sEditorsByName.executeQuery();
            while (rs.next()) {
                result.add(getEditor(rs.getInt("ideditore")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load editors", ex);
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
    public Keyword getKeyword(int keyword_key) throws DataLayerException {
        Keyword result = null;
        ResultSet rs = null;
        try {
            sKeywordById.setInt(1, keyword_key);
            rs = sKeywordById.executeQuery();
            if (rs.next()) {
                result = createKeyword(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load keyword by id", ex);
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
    public List<Keyword> getKeywords() throws DataLayerException {
        List<Keyword> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sKeywords.executeQuery();
            while (rs.next()) {
                result.add(getKeyword(rs.getInt("idkeyword")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load keywords", ex);
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
    public List<Keyword> getPublicationKeywords(int publication_key) throws DataLayerException {
        List<Keyword> result = new ArrayList();
        ResultSet rs = null;
        try {
            sKeywordsByPublication.setInt(1, publication_key);
            rs = sKeywordsByPublication.executeQuery();
            while (rs.next()) {
                result.add(getKeyword(rs.getInt("idkeyword")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load keywords", ex);
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
    public Publication getPublication(int publication_key) throws DataLayerException {
        Publication result = null;
        ResultSet rs = null;
        try {
            sPublicationById.setInt(1, publication_key);
            sPublicationById.setBoolean(2, false);
            rs = sPublicationById.executeQuery();
            if (rs.next()) {
                result = createPublication(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load publication by id", ex);
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
    public Publication getPublication(int publication_key, boolean incomplete) throws DataLayerException {
        Publication result = null;
        ResultSet rs = null;
        try {
            sPublicationById.setInt(1, publication_key);
            sPublicationById.setBoolean(2, incomplete);
            rs = sPublicationById.executeQuery();
            if (rs.next()) {
                result = createPublication(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load publication by id", ex);
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
    public Publication getPublicationByISBN(String isbn) throws DataLayerException {
        Publication result = null;
        ResultSet rs = null;
        try {
            sPublicationsByISBN.setString(1, isbn);
            rs = sPublicationsByISBN.executeQuery();
            if (rs.next()) {
                result = createPublication(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load publication by ISBN", ex);
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
    public List<Publication> getPublications(String orderBy) throws DataLayerException {
        List<Publication> result = new ArrayList();
        ResultSet rs = null;
        try {
            sPublications.setString(1, orderBy);
            rs = sPublications.executeQuery();
            while (rs.next()) {
                result.add(getPublication(rs.getInt("idpubblicazione")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load publications", ex);
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
    public List<Publication> getPublicationsByFilters(Map<String,String> filters) throws DataLayerException {
        List<Publication> result = new ArrayList();
        ResultSet rs = null;
        try {
            sPublicationsByFilters.setString(1, Utils.getArrayParameter(filters, "publicationIsbn"));
            sPublicationsByFilters.setString(2, Utils.getArrayParameter(filters, "publicationTitle"));
            sPublicationsByFilters.setString(3, Utils.getArrayParameter(filters, "publicationAuthor"));
            sPublicationsByFilters.setString(4, Utils.getArrayParameter(filters, "publicationEditor"));
            sPublicationsByFilters.setString(5, Utils.getArrayParameter(filters, "publicationKeyword"));
            sPublicationsByFilters.setString(6, Utils.getArrayParameter(filters, "publicationLanguage"));
            sPublicationsByFilters.setString(7, Utils.getArrayParameter(filters, "publicationYear"));
            sPublicationsByFilters.setString(8, Utils.getArrayParameter(filters, "publicationYearEnd"));
            sPublicationsByFilters.setString(9, Utils.getArrayParameter(filters, "order_by"));
            rs = sPublicationsByFilters.executeQuery();
            while (rs.next()) {
                result.add(getPublication(rs.getInt("idpubblicazione")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load publications", ex);
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
    public List<Author> getPublicationAuthors(int publication_key) throws DataLayerException {
        List<Author> result = new ArrayList();
        ResultSet rs = null;
        try {
            sAuthorByPublication.setInt(1, publication_key);
            rs = sAuthorByPublication.executeQuery();
            while (rs.next()) {
                result.add(getAuthor(rs.getInt("autore_idautore")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load authors of publication", ex);
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
    public List<Source> getPublicationSources(int publication_key) throws DataLayerException {
        List<Source> result = new ArrayList();
        ResultSet rs = null;
        try {
            sSourceByPublication.setInt(1, publication_key);
            rs = sSourceByPublication.executeQuery();
            while (rs.next()) {
                result.add(getSource(rs.getInt("sorgente_idsorgente")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load sources of publication", ex);
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
    public List<Publication> getLastInsertedPublication() throws DataLayerException {
        List<Publication> result = new ArrayList();
        ResultSet rs = null;
        try {
            Date today = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.add(Calendar.DATE, -7);
            sPublicationsByInsertDate.setDate(1, new Date(cal.getTimeInMillis()));
            sPublicationsByInsertDate.setDate(2, today);
            rs = sPublicationsByInsertDate.executeQuery();
            while (rs.next()) {
                result.add(getPublication(rs.getInt("pubblicazione")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load last inserted publications", ex);
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
    public List<Publication> getLastModifiedPublication() throws DataLayerException {
        List<Publication> result = new ArrayList();
        ResultSet rs = null;
        try {
            Date today = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.add(Calendar.DATE, -7);
            sPublicationsByUpdateDate.setDate(1, new Date(cal.getTimeInMillis()));
            sPublicationsByUpdateDate.setDate(2, today);
            rs = sPublicationsByUpdateDate.executeQuery();
            while (rs.next()) {
                result.add(getPublication(rs.getInt("pubblicazione")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load last modified publications", ex);
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
    public List<User> getMoreActiveUsers() throws DataLayerException {
        List<User> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sUserByNumberOfPublications.executeQuery();
            while (rs.next()) {
                result.add(getUser(rs.getInt("utente")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load more active users", ex);
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
    public Review getReview(int review_key) throws DataLayerException {
        Review result = null;
        ResultSet rs = null;
        try {
            sReviewById.setInt(1, review_key);
            rs = sReviewById.executeQuery();
            if (rs.next()) {
                result = createReview(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load review by id", ex);
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
    public List<Review> getReviews(int publication_key) throws DataLayerException {
        List<Review> result = new ArrayList();
        ResultSet rs = null;
        try {
            sReviewsByPublication.setInt(1, publication_key);
            rs = sReviewsByPublication.executeQuery();
            while (rs.next()) {
                result.add(getReview(rs.getInt("pubblicazione")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load review of publication", ex);
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
    public Reprint getReprint(int reprint_key) throws DataLayerException {
        Reprint result = null;
        ResultSet rs = null;
        try {
            sReprintById.setInt(1, reprint_key);
            rs = sReprintById.executeQuery();
            if (rs.next()) {
                result = createReprint(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load reprint by id", ex);
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
    public List<Reprint> getReprints(int publication_key) throws DataLayerException {
        List<Reprint> result = new ArrayList();
        ResultSet rs = null;
        try {
            sReprintsByPublication.setInt(1, publication_key);
            rs = sReprintsByPublication.executeQuery();
            while (rs.next()) {
                result.add(getReprint(rs.getInt("pubblicazione")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load reprints of publication", ex);
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
    public Source getSource(int source_key) throws DataLayerException {
        Source result = null;
        ResultSet rs = null;
        try {
            sSourceById.setInt(1, source_key);
            rs = sSourceById.executeQuery();
            if (rs.next()) {
                result = createSource(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load source by id", ex);
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
    public List<Source> getSources() throws DataLayerException {
        List<Source> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sSources.executeQuery();
            while (rs.next()) {
                result.add(getSource(rs.getInt("idsorgente")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load sources", ex);
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
    public List<History> getHistoriesByUser(int user_key) throws DataLayerException {
        List<History> result = new ArrayList();
        ResultSet rs = null;
        try {
            sHistoriesByUser.setInt(1, user_key);
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
    public List<History> getHistoriesByPublication(int publication_key) throws DataLayerException {
        List<History> result = new ArrayList();
        ResultSet rs = null;
        try {
            sHistoriesByPublication.setInt(1, publication_key);
            rs = sHistoriesByPublication.executeQuery();
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
    public User getUserAdministrator() throws DataLayerException {
        User result = null;
        ResultSet rs = null;
        try {
            rs = sUserAdministrator.executeQuery();
            if (rs.next()) {
                result = createUser(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load administrator user", ex);
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
    public List<User> getUsers(String filter) throws DataLayerException {
        List<User> result = new ArrayList();
        ResultSet rs = null;
        try {
            sUsers.setString(1, filter);
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
    public List<User> getUsersActive(String filter) throws DataLayerException {
        List<User> result = new ArrayList();
        ResultSet rs = null;
        try {
            sUsersByStatus.setString(1, filter);
            sUsersByStatus.setInt(2, 1);
            rs = sUsersByStatus.executeQuery();
            while (rs.next()) {
                result.add(getUser(rs.getInt("idutente")));

            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load active users", ex);
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
    public List<User> getUsersPassive(String filter) throws DataLayerException {
        List<User> result = new ArrayList();
        ResultSet rs = null;
        try {
            sUsers.setString(1, filter);
            sUsersByStatus.setInt(2, 2);
            rs = sUsersByStatus.executeQuery();
            while (rs.next()) {
                result.add(getUser(rs.getInt("idutente")));

            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load passive users", ex);
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
                uAuthor.setInt(3, key);

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
            throw new DataLayerException("Unable to store author", ex);
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
                uEditor.setInt(2, key);
                
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
    public void storeKeyword(Keyword keyword) throws DataLayerException {
        ResultSet keys = null;
        int key = keyword.getKey();
        try {
            if (keyword.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
//                if (!article.isDirty()) {
//                    return;
//                }
                uKeyword.setString(1, keyword.getName());
                uKeyword.setInt(2, key);

                uKeyword.executeUpdate();
            } else { //insert
                iKeyword.setString(1, keyword.getName());

                if (iKeyword.executeUpdate() == 1) {
                    keys = iKeyword.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                keyword.copyFrom(getKeyword(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store keyword", ex);
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
        //TODO: Gestione dell'inserimento e update delle tabelle nan autore, sorgente e keyword
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
                uPublication.setInt(5, publication.getLike());
                uPublication.setString(6, publication.getIsbn());
                uPublication.setInt(7, publication.getPageNumber());
                uPublication.setString(8, publication.getLanguage());
                uPublication.setDate(9, publication.getPublicationDate());
                uPublication.setBoolean(10, publication.getIncomplete());
                uPublication.setInt(11, key);

                uPublication.executeUpdate();
            } else { //insert
                iPublication.setString(1, publication.getTitle());
                iPublication.setString(2, publication.getDescription());
                iPublication.setInt(3, publication.getEditor().getKey());
                iPublication.setString(4, publication.getIndex());
                iPublication.setInt(5, publication.getLike());
                iPublication.setString(6, publication.getIsbn());
                iPublication.setInt(7, publication.getPageNumber());
                iPublication.setString(8, publication.getLanguage());
                iPublication.setDate(9, publication.getPublicationDate());
                iPublication.setBoolean(10, publication.getIncomplete());

                if (iPublication.executeUpdate() == 1) {
                    keys = iPublication.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                if(!publication.getIncomplete()){
                    publication.copyFrom(getPublication(key));
                }
                else{
                    publication.copyFrom(getPublication(key, publication.getIncomplete()));
                }
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
                uReview.setInt(6, key);

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
                uReprint.setInt(4, key);

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
                uSource.setString(2, source.getUri());
                uSource.setString(3, source.getFormat());
                uSource.setString(4, source.getDescription());
                uSource.setInt(5, key);

                uSource.executeUpdate();
            } else { //insert
                iSource.setString(1, source.getType());
                iSource.setString(2, source.getUri());
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
                uHistory.setDate(3, historia.getDate());
                uHistory.setInt(4, historia.getPublication().getKey());
                uHistory.setInt(5, historia.getUser().getKey());
                uHistory.setInt(6, key);

                uHistory.executeUpdate();
            } else { //insert
                iHistory.setString(1, historia.getEntry());
                iHistory.setInt(2, historia.getType());
                iHistory.setDate(3, historia.getDate());
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
                uUser.setInt(6, key);

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

    @Override
    public void storePublicationHasAuthor(int idAuthor, int idPublication) throws DataLayerException {
        ResultSet res = null;
        try {
            iPublicationHasAuthor.setInt(1, idAuthor);
            iPublicationHasAuthor.setInt(2, idPublication);
            
            iPublicationHasAuthor.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot relate author with publication", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void deletePublicationHasAuthor(int idPublication) throws DataLayerException {
        ResultSet res = null;
        try {
            dPublicationHasAuthor.setInt(1, idPublication);
            
            dPublicationHasAuthor.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete author relation", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void storePublicationHasKeyword(int idKeyword, int idPublication) throws DataLayerException {
        ResultSet res = null;
        try {
            iPublicationHasKeyword.setInt(1, idPublication);
            iPublicationHasKeyword.setInt(2, idKeyword);
            
            iPublicationHasKeyword.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot relate key with publication", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }

    @Override
    public void deletePublicationHasKeyword(int idPublication) throws DataLayerException {
        ResultSet res = null;
        try {
            dPublicationHasKeyword.setInt(1, idPublication);
            
            dPublicationHasKeyword.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete key relation", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }
    
    @Override
    public void storePublicationHasSource(int idSource, int idPublication) throws DataLayerException {
        ResultSet res = null;
        try {
            iPublicationHasSource.setInt(1, idPublication);
            iPublicationHasSource.setInt(2, idSource);
            
            iPublicationHasSource.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot relate source with publication", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }
    
    @Override
    public void deletePublicationHasSource(int idPublication) throws DataLayerException {
        ResultSet res = null;
        try {
            dPublicationHasSource.setInt(1, idPublication);
            
            dPublicationHasSource.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete source relation", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
    }
}
