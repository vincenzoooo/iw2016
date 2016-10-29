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
import it.univaq.iw.bibliomanager.data.model.Chapter;
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
import it.univaq.iw.bibliomanager.data.model.Section;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.util.HashMap;
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
    private PreparedStatement uHistory, iHistory, dHistory, dHistoryByPublication;
    private PreparedStatement sPublications, sPublicationById, sPublicationsByInsertDate, sPublicationsByUpdateDate, sPublicationsByFilters, sPublicationsByISBN, sIncompletePublications;
    private PreparedStatement uPublication, iPublication, iPublicationNoEditor, dPublication, dIncompletePublication;
    private PreparedStatement sSources, sSourceById, sSourceByPublication;
    private PreparedStatement uSource, iSource, dSource, dSourceByPublication;
    private PreparedStatement sReprintsByPublication, sReprintById;
    private PreparedStatement uReprint, iReprint, dReprint, dReprintByPublication;
    private PreparedStatement sEditors, sEditorsByName, sEditorById;
    private PreparedStatement uEditor, iEditor, dEditor;
    private PreparedStatement sAuthors, sAuthorsByName, sAuthorById, sAuthorByPublication;
    private PreparedStatement uAuthor, iAuthor, dAuthor;
    private PreparedStatement sReviewsByPublication, sReviewById, sLastModeratedReviews;
    private PreparedStatement uReview, iReview, dReview, dReviewByPublication, cReview;
    private PreparedStatement sKeywords, sKeywordsByPublication, sKeywordById;
    private PreparedStatement uKeyword, iKeyword, dKeyword;
    private PreparedStatement sChapters, sChapterById;
    private PreparedStatement iChapter, uChapter, dChapter, dChapterByPublication;
    private PreparedStatement sSections, sSectionById;
    private PreparedStatement iSection, uSection, dSection, dSectionByChapter;
    private PreparedStatement iPublicationHasAuthor, iPublicationHasKeyword;
    private PreparedStatement dPublicationHasAuthor, dPublicationHasKeyword, dAuthorFromPublication, dKeywordFromPublication;
    private PreparedStatement sUserLike, iUserLike, dUserLike;
    private PreparedStatement sFilters, iFilters, dFilters;
    private PreparedStatement dLikeByPublication;

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
            this.sHistoriesByUser = connection.prepareStatement("SELECT * FROM iw2016.storico WHERE utente = ? ORDER BY data_operazione DESC LIMIT 10");
            this.sHistoryById = connection.prepareStatement("SELECT * FROM iw2016.storico WHERE idstorico = ?");
            this.uHistory = connection.prepareStatement("UPDATE iw2016.storico SET entry = ?, tipo = ?, data_operazione = ?, pubblicazione = ?, utente = ? WHERE idstorico = ?");
            this.iHistory = connection.prepareStatement("INSERT INTO iw2016.storico (entry, tipo, data_operazione, pubblicazione, utente) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dHistory = connection.prepareStatement("DELETE FROM iw2016.storico WHERE idstorico = ?");
            this.dHistoryByPublication = connection.prepareStatement("DELETE FROM iw2016.storico WHERE pubblicazione = ?");
            this.sPublications = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione WHERE incompleta = 0");
            this.sPublicationById = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione WHERE idpubblicazione = ?");
            this.sPublicationsByInsertDate = connection.prepareStatement("SELECT pubblicazione FROM storico WHERE tipo = 0 ORDER BY data_operazione DESC LIMIT 6");
            this.sPublicationsByUpdateDate = connection.prepareStatement("SELECT DISTINCT pubblicazione FROM storico WHERE tipo = 1 ORDER BY data_operazione DESC LIMIT 6");
            this.sPublicationsByISBN = connection.prepareStatement("SELECT * FROM iw2016.pubblicazione WHERE isbn = ? AND incompleta = 0");
            this.sIncompletePublications = connection.prepareStatement("SELECT idpubblicazione FROM iw2016.pubblicazione WHERE incompleta = 1 AND timestamp < ?");
            this.uPublication = connection.prepareStatement("UPDATE iw2016.pubblicazione SET titolo = ?, descrizione = ?, editore = ?, n_consigli = ? , isbn = ?, n_pagine = ?, lingua = ?, data_pubblicazione = ?, incompleta = ?, timestamp = ? WHERE idpubblicazione = ?");
            this.iPublication = connection.prepareStatement("INSERT INTO iw2016.pubblicazione (titolo, descrizione, editore, n_consigli, isbn, n_pagine, lingua, data_pubblicazione, incompleta, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.iPublicationNoEditor = connection.prepareStatement("INSERT INTO iw2016.pubblicazione (titolo, descrizione, n_consigli, isbn, n_pagine, lingua, data_pubblicazione, incompleta, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dPublication = connection.prepareStatement("DELETE FROM iw2016.pubblicazione WHERE idpubblicazione = ?");
            this.dIncompletePublication = connection.prepareStatement("DELETE FROM iw2016.pubblicazione WHERE incompleta = 1 AND idPubblicazione = ? AND (timestamp < ? OR timestamp IS NULL)");
            this.sSources = connection.prepareStatement("SELECT * FROM iw2016.sorgente");
            this.sSourceById = connection.prepareStatement("SELECT * FROM iw2016.sorgente WHERE idsorgente = ?");
            this.uSource = connection.prepareStatement("UPDATE iw2016.sorgente SET tipo = ?, URI = ?, formato = ?, descrizione = ?, copertina=?, download=?, pubblicazione = ? WHERE idsorgente = ?");
            this.iSource = connection.prepareStatement("INSERT INTO iw2016.sorgente (tipo, URI, formato, descrizione, copertina, download, pubblicazione) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dSource = connection.prepareStatement("DELETE FROM iw2016.sorgente WHERE idsorgente = ?");
            this.dSourceByPublication = connection.prepareStatement("DELETE FROM iw2016.sorgente WHERE pubblicazione = ?");
            this.sReprintById = connection.prepareStatement("SELECT * FROM iw2016.ristampa WHERE idristampa = ?");
            this.uReprint = connection.prepareStatement("UPDATE iw2016.ristampa SET numero = ?, data = ?, pubblicazione = ? WHERE idristampa = ?");
            this.iReprint = connection.prepareStatement("INSERT INTO iw2016.ristampa (numero, data, pubblicazione) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dReprint = connection.prepareStatement("DELETE FROM iw2016.ristampa WHERE idristampa = ?");
            this.dReprintByPublication = connection.prepareStatement("DELETE FROM iw2016.ristampa WHERE pubblicazione = ?");
            this.sEditorsByName = connection.prepareStatement("SELECT * FROM iw2016.editor WHERE nome LIKE '%?%'");
            this.sEditorById = connection.prepareStatement("SELECT * FROM iw2016.editore WHERE ideditore = ?");
            this.uEditor = connection.prepareStatement("UPDATE iw2016.editore SET nome = ? WHERE ideditore = ?");
            this.iEditor = connection.prepareStatement("INSERT INTO iw2016.editore (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            this.dEditor = connection.prepareStatement("DELETE FROM iw2016.editore WHERE ideditore = ? AND (SELECT COUNT(*) FROM pubblicazione WHERE editore = ?) = 0");
            this.sAuthorsByName = connection.prepareStatement("SELECT * FROM iw2016.autore WHERE nome LIKE '%?%'");
            this.sAuthorById = connection.prepareStatement("SELECT * FROM iw2016.autore WHERE idautore = ?");
            this.sAuthorByPublication = connection.prepareStatement("SELECT * FROM iw2016.autore JOIN autore_has_pubblicazione ON idautore = autore_idautore WHERE pubblicazione_idpubblicazione = ?");
            this.uAuthor = connection.prepareStatement("UPDATE iw2016.autore SET nome = ?, cognome = ? WHERE idautore = ?");
            this.iAuthor = connection.prepareStatement("INSERT INTO iw2016.autore (nome, cognome) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dAuthor = connection.prepareStatement("DELETE FROM iw2016.autore WHERE idautore = ? AND (SELECT COUNT(*) FROM autore_has_pubblicazione WHERE autore_idautore = ?) = 0");
            this.sReviewById = connection.prepareStatement("SELECT * FROM iw2016.recensione WHERE idrecensione = ?");
            this.sLastModeratedReviews = connection.prepareStatement("SELECT * FROM iw2016.recensione WHERE pubblicazione = ? AND moderata = 1 ORDER BY data_recensione DESC LIMIT ?");
            this.uReview = connection.prepareStatement("UPDATE iw2016.recensione SET testo = ?, moderata = ?, data_recensione = ?, utente_autore = ?, pubblicazione = ?, storico = ? WHERE idrecensione = ?");
            this.iReview = connection.prepareStatement("INSERT INTO iw2016.recensione (testo, moderata, data_recensione, utente_autore, pubblicazione) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dReview = connection.prepareStatement("DELETE FROM iw2016.recensione WHERE idrecensione = ?");
            this.cReview = connection.prepareStatement("SELECT COUNT(*) AS contatore FROM iw2016.recensione WHERE pubblicazione = ? AND moderata=0");
            this.dReviewByPublication = connection.prepareStatement("DELETE FROM iw2016.recensione WHERE pubblicazione = ?");
            this.sKeywordById = connection.prepareStatement("SELECT * FROM iw2016.keyword WHERE idkeyword = ?");
            this.sKeywordsByPublication = connection.prepareStatement("SELECT * FROM iw2016.keyword JOIN pubblicazione_has_keyword ON idkeyword = keyword_idkeyword WHERE pubblicazione_idpubblicazione = ?");
            this.uKeyword = connection.prepareStatement("UPDATE iw2016.keyword SET nome = ? WHERE idkeyword = ?");
            this.iKeyword = connection.prepareStatement("INSERT INTO iw2016.keyword (nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            this.dKeyword = connection.prepareStatement("DELETE FROM iw2016.keyword WHERE idkeyword = ? AND (SELECT COUNT(*) FROM pubblicazione_has_keyword WHERE keyword_idkeyword = ?) = 0");
            this.sChapters = connection.prepareStatement("SELECT * FROM iw2016.capitolo WHERE pubblicazione = ? ORDER BY numero");
            this.sChapterById = connection.prepareStatement("SELECT * FROM iw2016.capitolo WHERE idcapitolo = ?");
            this.uChapter = connection.prepareStatement("UPDATE iw2016.capitolo SET numero = ?, titolo = ?, pubblicazione = ? WHERE idcapitolo = ?");
            this.iChapter = connection.prepareStatement("INSERT INTO iw2016.capitolo (numero,titolo,pubblicazione) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dChapter = connection.prepareStatement("DELETE FROM iw2016.capitolo WHERE idcapitolo = ? AND (SELECT COUNT(*) FROM sezione WHERE capitolo = ?) = 0");
            this.dChapterByPublication = connection.prepareStatement("DELETE FROM iw2016.capitolo WHERE pubblicazione = ?");
            this.sSections = connection.prepareStatement("SELECT * FROM iw2016.sezione WHERE capitolo = ? ORDER BY numero");
            this.sSectionById = connection.prepareStatement("SELECT * FROM iw2016.sezione WHERE idsezione = ?");
            this.uSection = connection.prepareStatement("UPDATE iw2016.sezione SET numero = ?, titolo = ?, capitolo = ? WHERE idsezione = ?");
            this.iSection = connection.prepareStatement("INSERT INTO iw2016.sezione (numero, titolo, capitolo) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dSection = connection.prepareStatement("DELETE FROM iw2016.sezione WHERE idsezione = ?");
            this.dSectionByChapter = connection.prepareStatement("DELETE FROM iw2016.sezione WHERE capitolo = ?");
            this.iPublicationHasAuthor = connection.prepareStatement("INSERT INTO iw2016.autore_has_pubblicazione(autore_idautore,pubblicazione_idpubblicazione) VALUES (?,?)");
            this.dPublicationHasAuthor = connection.prepareStatement("DELETE FROM iw2016.autore_has_pubblicazione WHERE pubblicazione_idpubblicazione = ?");
            this.iPublicationHasKeyword = connection.prepareStatement("INSERT INTO iw2016.pubblicazione_has_keyword (pubblicazione_idpubblicazione, keyword_idkeyword) VALUES (?,?)");
            this.dPublicationHasKeyword = connection.prepareStatement("DELETE FROM iw2016.pubblicazione_has_keyword WHERE pubblicazione_idpubblicazione = ?");
            this.dAuthorFromPublication = connection.prepareStatement("DELETE FROM iw2016.autore_has_pubblicazione WHERE pubblicazione_idpubblicazione = ? AND autore_idautore = ?");
            this.dKeywordFromPublication = connection.prepareStatement("DELETE FROM iw2016.pubblicazione_has_keyword WHERE pubblicazione_idpubblicazione = ? AND keyword_idkeyword = ?");
            this.sUserLike = connection.prepareStatement("SELECT * FROM iw2016.consigli_utente WHERE pubblicazione_idpubblicazione = ? AND utente_idutente = ?");
            this.iUserLike = connection.prepareStatement("INSERT INTO iw2016.consigli_utente (pubblicazione_idpubblicazione, utente_idutente) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dUserLike = connection.prepareStatement("DELETE FROM iw2016.consigli_utente WHERE pubblicazione_idpubblicazione = ?");
            this.sFilters = connection.prepareStatement("SELECT * FROM iw2016.filtri WHERE idfiltri = ?");
            this.iFilters = connection.prepareStatement("INSERT INTO iw2016.filtri (isbn, titolo, autore, editore, anno_inizio, anno_fine, keyword, lingua, download, utente, timestamp) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            this.dFilters = connection.prepareStatement("DELETE FROM iw2016.filtri WHERE timestamp < ?");
            this.dLikeByPublication = connection.prepareStatement("DELETE FROM iw2016.consigli_utente WHERE pubblicazione_idpubblicazione = ?");
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
            publication.setLike(rs.getInt("n_consigli"));
            publication.setIsbn(rs.getString("isbn"));
            publication.setLanguage(rs.getString("lingua"));
            publication.setPublicationDate(rs.getDate("data_pubblicazione"));
            publication.setPageNumber(rs.getInt("n_pagine"));
            publication.setEditor(getEditor(rs.getInt("editore")));
            publication.setAuthor(getPublicationAuthors(rs.getInt("idpubblicazione")));
            publication.setSources(getPublicationSources(rs.getInt("idpubblicazione"), 0, 0));
            publication.setKeywords(getPublicationKeywords(rs.getInt("idpubblicazione")));
            publication.setReprints(getReprints(rs.getInt("idpubblicazione"), 0, 0));
            publication.setIndex(getChapters(rs.getInt("idpubblicazione")));
            publication.setReviews(getReviews(rs.getInt("idpubblicazione"), 0, 0));
            publication.setIncomplete(rs.getBoolean("incompleta"));
            publication.setTimestamp(rs.getTimestamp("timestamp"));
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
            review.setReviewDate(rs.getTimestamp("data_recensione"));
            review.setAuthor(getUser(rs.getInt("utente_autore")));
            review.setPublicationKey(rs.getInt("pubblicazione"));
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
            reprint.setPublicationKey(rs.getInt("pubblicazione"));
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
            source.setCover(rs.getBoolean("copertina"));
            source.setPublicationKey(rs.getInt("pubblicazione"));
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
            history.setDate(rs.getTimestamp("data_operazione"));
            history.setPublicationKey(rs.getInt("pubblicazione"));
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
    public Chapter createChapter() {
        return new ChapterImpl(this);
    }

    public Chapter createChapter(ResultSet rs) throws DataLayerException {
        try {
            ChapterImpl chapter = new ChapterImpl(this);
            chapter.setKey(rs.getInt("idcapitolo"));
            chapter.setNumber(rs.getInt("numero"));
            chapter.setTitle(rs.getString("titolo"));
            chapter.setPublicationKey(rs.getInt("pubblicazione"));
            chapter.setSections(getSections(rs.getInt("idcapitolo")));
            return chapter;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create chapter object form ResultSet", ex);
        }
    }

    @Override
    public Section createSection() {
        return new SectionImpl(this);
    }

    public Section createSection(ResultSet rs) throws DataLayerException {
        try {
            SectionImpl section = new SectionImpl(this);
            section.setKey(rs.getInt("idsezione"));
            section.setNumber(rs.getInt("numero"));
            section.setTitle(rs.getString("titolo"));
            section.setChapterKey(rs.getInt("capitolo"));
            return section;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create section object form ResultSet", ex);
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
    public List<Author> getAuthors(int limit, int offset) throws DataLayerException {
        List<Author> result = new ArrayList();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM iw2016.autore ORDER BY cognome, nome";
            if (limit > 0) {
                query += " LIMIT 10 ";
            }
            if (offset > 0) {
                query += "OFFSET " + offset;
            }
            sAuthors = connection.prepareStatement(query);
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
    public List<Editor> getEditors(int limit, int offset) throws DataLayerException {
        List<Editor> result = new ArrayList();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM iw2016.editore ORDER BY nome";
            if (limit > 0) {
                query += " LIMIT 10 ";
            }
            if (offset > 0) {
                query += "OFFSET " + offset;
            }
            sEditors = connection.prepareStatement(query);
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
    public List<Keyword> getKeywords(int limit, int offset) throws DataLayerException {
        List<Keyword> result = new ArrayList();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM iw2016.keyword ORDER BY nome";
            if (limit > 0) {
                query += " LIMIT 10 ";
            }
            if (offset > 0) {
                query += "OFFSET " + offset;
            }
            sKeywords = connection.prepareStatement(query);
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
    public List<Publication> getPublications() throws DataLayerException {
        List<Publication> result = new ArrayList();
        ResultSet rs = null;
        try {
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
    public List<Publication> getPublicationsByFilters(Map<String, String> filters) throws DataLayerException {
        List<Publication> result = new ArrayList();
        ResultSet rs = null;
        try {
            String query = "SELECT p.idpubblicazione FROM pubblicazione p LEFT JOIN ristampa r ON r.pubblicazione = p.idpubblicazione LEFT JOIN editore e ON e.ideditore = p.editore "
                    + "LEFT JOIN autore_has_pubblicazione ap ON ap.pubblicazione_idpubblicazione = p.idpubblicazione LEFT JOIN autore a ON a.idautore = ap.autore_idautore "
                    + "LEFT JOIN sorgente sr ON sr.pubblicazione = p.idpubblicazione LEFT JOIN pubblicazione_has_keyword pk ON pk.pubblicazione_idpubblicazione = p.idpubblicazione LEFT JOIN keyword k ON k.idkeyword = pk.keyword_idkeyword "
                    + "LEFT JOIN storico st ON st.pubblicazione = p.idpubblicazione LEFT JOIN utente u ON u.idutente = st.utente WHERE p.incompleta = 0 ";
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                if (entry.getKey().equals("isbn") && entry.getValue() != null) {
                    query += " AND p.isbn LIKE '%" + SecurityLayer.addSlashes(entry.getValue()) + "%' ";
                }
                if (entry.getKey().equals("titolo") && entry.getValue() != null) {
                    query += " AND p.titolo LIKE '%" + SecurityLayer.addSlashes(entry.getValue()) + "%' ";
                }
                if (entry.getKey().equals("autore") && entry.getValue() != null) {
                    query += " AND concat(a.nome, ' ',a.cognome) LIKE '%" + SecurityLayer.addSlashes(entry.getValue()) + "%' ";
                }
                if (entry.getKey().equals("editore") && entry.getValue() != null) {
                    query += " AND e.nome LIKE '%" + SecurityLayer.addSlashes(entry.getValue()) + "%' ";
                }
                if (entry.getKey().equals("keyword") && entry.getValue() != null) {
                    query += " AND k.nome LIKE '%" + SecurityLayer.addSlashes(entry.getValue()) + "%' ";
                }
                if (entry.getKey().equals("lingua") && entry.getValue() != null) {
                    query += " AND p.lingua LIKE '%" + SecurityLayer.addSlashes(entry.getValue()) + "%' ";
                }
                if (entry.getKey().equals("anno_inizio") && entry.getValue() != null) {
                    query += " AND p.data_pubblicazione >= '" + SecurityLayer.addSlashes(entry.getValue()) + "' ";
                }
                if (entry.getKey().equals("anno_fine") && entry.getValue() != null) {
                    query += " AND p.data_pubblicazione < '" + SecurityLayer.addSlashes(entry.getValue()) + "' ";
                }
                if (entry.getKey().equals("download") && entry.getValue() != null) {
                    query += " AND sr.download = 1 ";
                }
                if (entry.getKey().equals("utente") && entry.getValue() != null) {
                    query += " AND CONCAT(u.nome, ' ',u.cognome) LIKE '%" + SecurityLayer.addSlashes(entry.getValue()) + "%' ";
                }
            }
            query += "GROUP BY p.idpubblicazione ORDER BY " + SecurityLayer.addSlashes(Utils.getArrayParameter(filters, "order_by")) + " " + SecurityLayer.addSlashes(Utils.getArrayParameter(filters, "order_mode"));
            if (!Utils.isNullOrEmpty(Utils.getArrayParameter(filters, "limit"))) {
                query += " LIMIT 5 ";
            }
            if (!Utils.isNullOrEmpty(Utils.getArrayParameter(filters, "offset")) && !"0".equals(Utils.getArrayParameter(filters, "offset"))) {
                query += "OFFSET " + Utils.getArrayParameter(filters, "offset");
            }
            sPublicationsByFilters = connection.prepareStatement(query);
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
    public List<Source> getPublicationSources(int publication_key, int limit, int offset) throws DataLayerException {
        List<Source> result = new ArrayList();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM iw2016.sorgente WHERE pubblicazione = ?";
            if (limit > 0) {
                query += " LIMIT 10 ";
            }
            if (offset > 0) {
                query += "OFFSET " + offset;
            }
            sSourceByPublication = connection.prepareStatement(query);
            sSourceByPublication.setInt(1, publication_key);
            rs = sSourceByPublication.executeQuery();
            while (rs.next()) {
                result.add(getSource(rs.getInt("idsorgente")));
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
//            Date today = new Date(System.currentTimeMillis());
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(today);
//            cal.add(Calendar.DATE, -7);
//            sPublicationsByInsertDate.setDate(1, new Date(cal.getTimeInMillis()));
//            sPublicationsByInsertDate.setDate(2, today);
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
//            Date today = new Date(System.currentTimeMillis());
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(today);
//            cal.add(Calendar.DATE, -7);
//            sPublicationsByUpdateDate.setDate(1, new Date(cal.getTimeInMillis()));
//            sPublicationsByUpdateDate.setDate(2, today);
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
    public List<Review> getReviews(int publication_key, int limit, int offset) throws DataLayerException {
        List<Review> result = new ArrayList();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM iw2016.recensione WHERE pubblicazione = ? ORDER BY data_recensione DESC";
            if (limit > 0) {
                query += " LIMIT 7 ";
            }
            if (offset > 0) {
                query += "OFFSET " + offset;
            }
            sReviewsByPublication = connection.prepareStatement(query);
            sReviewsByPublication.setInt(1, publication_key);
            rs = sReviewsByPublication.executeQuery();
            while (rs.next()) {
                result.add(getReview(rs.getInt("idrecensione")));
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
    public int getCountReview(int publication_key) throws DataLayerException {
        int result = 0;
        ResultSet rs = null;
        try {
            cReview.setInt(1, publication_key);
            rs = cReview.executeQuery();
            if (rs.next()) {
                result = rs.getInt("contatore");
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to count review", ex);
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
    public List<Review> getLastReviews(int publication_key, int limit) throws DataLayerException {
        List<Review> result = new ArrayList();
        ResultSet rs = null;
        try {
            sLastModeratedReviews.setInt(1, publication_key);
            sLastModeratedReviews.setInt(2, limit);
            rs = sLastModeratedReviews.executeQuery();
            while (rs.next()) {
                result.add(getReview(rs.getInt("idrecensione")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load last moderated review of publication", ex);
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
    public List<Reprint> getReprints(int publication_key, int limit, int offset) throws DataLayerException {
        List<Reprint> result = new ArrayList();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM iw2016.ristampa WHERE pubblicazione = ?";
            if (limit > 0) {
                query += " LIMIT 10 ";
            }
            if (offset > 0) {
                query += "OFFSET " + offset;
            }
            sReprintsByPublication = connection.prepareStatement(query);
            sReprintsByPublication.setInt(1, publication_key);
            rs = sReprintsByPublication.executeQuery();
            while (rs.next()) {
                result.add(getReprint(rs.getInt("idristampa")));
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
    public List<History> getHistoriesByPublication(int publication_key, int limit, int offset) throws DataLayerException {
        List<History> result = new ArrayList();
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM iw2016.storico WHERE pubblicazione = ?";
            if (limit > 0) {
                query += " LIMIT 10 ";
            }
            if (offset > 0) {
                query += "OFFSET " + offset;
            }
            sHistoriesByPublication = connection.prepareStatement(query);
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
    public boolean getUsersLike(int publication_key, int user_key) throws DataLayerException {
        ResultSet rs = null;
        boolean liked = false;
        try {
            sUserLike.setInt(1, publication_key);
            sUserLike.setInt(2, user_key);
            rs = sUserLike.executeQuery();
            while (rs.next()) {
                liked = true;
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
        return liked;
    }

    @Override
    public Chapter getChapter(int chapter_key) throws DataLayerException {
        Chapter result = null;
        ResultSet rs = null;
        try {
            sChapterById.setInt(1, chapter_key);
            rs = sChapterById.executeQuery();
            if (rs.next()) {
                result = createChapter(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load publication chapter", ex);
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
    public Section getSection(int section_key) throws DataLayerException {
        Section result = null;
        ResultSet rs = null;
        try {
            sSectionById.setInt(1, section_key);
            rs = sSectionById.executeQuery();
            if (rs.next()) {
                result = createSection(rs);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load chapter section", ex);
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
    public List<Chapter> getChapters(int publication_key) throws DataLayerException {
        List<Chapter> result = new ArrayList();
        ResultSet rs = null;
        try {
            sChapters.setInt(1, publication_key);
            rs = sChapters.executeQuery();
            while (rs.next()) {
                result.add(getChapter(rs.getInt("idcapitolo")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load chapters from publication", ex);
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
    public List<Section> getSections(int chapter_key) throws DataLayerException {
        List<Section> result = new ArrayList();
        ResultSet rs = null;
        try {
            sSections.setInt(1, chapter_key);
            rs = sSections.executeQuery();
            while (rs.next()) {
                result.add(getSection(rs.getInt("idsezione")));

            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load sections from chapter", ex);
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
                if (!author.isDirty()) {
                    return;
                }
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
                if (!editor.isDirty()) {
                    return;
                }
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
                if (!keyword.isDirty()) {
                    return;
                }
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
                if (!publication.isDirty()) {
                    return;
                }
                uPublication.setString(1, publication.getTitle());
                uPublication.setString(2, publication.getDescription());
                uPublication.setInt(3, publication.getEditor().getKey());
                uPublication.setInt(4, publication.getLike());
                uPublication.setString(5, publication.getIsbn());
                uPublication.setInt(6, publication.getPageNumber());
                uPublication.setString(7, publication.getLanguage());
                uPublication.setDate(8, publication.getPublicationDate());
                uPublication.setBoolean(9, publication.getIncomplete());
                uPublication.setTimestamp(10, publication.getTimestamp());
                uPublication.setInt(11, key);

                uPublication.executeUpdate();
            } else //insert
            if (publication.getEditor().getKey() == 0) {
                iPublicationNoEditor.setString(1, publication.getTitle());
                iPublicationNoEditor.setString(2, publication.getDescription());
                iPublicationNoEditor.setInt(3, publication.getLike());
                iPublicationNoEditor.setString(4, publication.getIsbn());
                iPublicationNoEditor.setInt(5, publication.getPageNumber());
                iPublicationNoEditor.setString(6, publication.getLanguage());
                iPublicationNoEditor.setDate(7, publication.getPublicationDate());
                iPublicationNoEditor.setBoolean(8, publication.getIncomplete());
                iPublicationNoEditor.setTimestamp(9, publication.getTimestamp());
                if (iPublicationNoEditor.executeUpdate() == 1) {
                    keys = iPublicationNoEditor.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            } else {
                iPublication.setString(1, publication.getTitle());
                iPublication.setString(2, publication.getDescription());
                iPublication.setInt(3, publication.getEditor().getKey());
                iPublication.setInt(4, publication.getLike());
                iPublication.setString(5, publication.getIsbn());
                iPublication.setInt(6, publication.getPageNumber());
                iPublication.setString(7, publication.getLanguage());
                iPublication.setDate(8, publication.getPublicationDate());
                iPublication.setBoolean(9, publication.getIncomplete());
                iPublication.setTimestamp(10, publication.getTimestamp());

                if (iPublication.executeUpdate() == 1) {
                    keys = iPublication.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                if (!publication.getIncomplete()) {
                    publication.copyFrom(getPublication(key));
                } else {
                    publication.copyFrom(getPublication(key));
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
                if (!review.isDirty()) {
                    return;
                }
                uReview.setString(1, review.getText());
                uReview.setBoolean(2, review.getStatus());
                uReview.setTimestamp(3, review.getReviewDate());
                uReview.setInt(4, review.getAuthor().getKey());
                uReview.setInt(5, review.getPublicationKey());
                uReview.setInt(6, review.getHistory().getKey());
                uReview.setInt(7, key);

                uReview.executeUpdate();
            } else { //insert
                iReview.setString(1, review.getText());
                iReview.setBoolean(2, review.getStatus());
                iReview.setTimestamp(3, review.getReviewDate());
                iReview.setInt(4, review.getAuthor().getKey());
                iReview.setInt(5, review.getPublicationKey());

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
                if (!reprint.isDirty()) {
                    return;
                }
                uReprint.setInt(1, reprint.getNumber());
                uReprint.setDate(2, reprint.getDate());
                uReprint.setInt(3, reprint.getPublicationKey());
                uReprint.setInt(4, key);

                uReprint.executeUpdate();
            } else { //insert
                iReprint.setInt(1, reprint.getNumber());
                iReprint.setDate(2, reprint.getDate());
                iReprint.setInt(3, reprint.getPublicationKey());

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
                if (!source.isDirty()) {
                    return;
                }
                uSource.setString(1, source.getType());
                uSource.setString(2, source.getUri());
                uSource.setString(3, source.getFormat());
                uSource.setString(4, source.getDescription());
                uSource.setBoolean(5, source.getCover());
                uSource.setBoolean(6, source.getDownload());
                uSource.setInt(7, source.getPublicationKey());
                uSource.setInt(8, key);

                uSource.executeUpdate();
            } else { //insert
                iSource.setString(1, source.getType());
                iSource.setString(2, source.getUri());
                iSource.setString(3, source.getFormat());
                iSource.setString(4, source.getDescription());
                iSource.setBoolean(5, source.getCover());
                iSource.setBoolean(6, source.getDownload());
                iSource.setInt(7, source.getPublicationKey());

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
                if (!historia.isDirty()) {
                    return;
                }
                uHistory.setString(1, historia.getEntry());
                uHistory.setInt(2, historia.getType());
                uHistory.setTimestamp(3, historia.getDate());
                uHistory.setInt(4, historia.getPublicationKey());
                uHistory.setInt(5, historia.getUser().getKey());
                uHistory.setInt(6, key);

                uHistory.executeUpdate();
            } else { //insert
                iHistory.setString(1, historia.getEntry());
                iHistory.setInt(2, historia.getType());
                iHistory.setTimestamp(3, historia.getDate());
                iHistory.setInt(4, historia.getPublicationKey());
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
                if (!user.isDirty()) {
                    return;
                }
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
    public void storeChapter(Chapter chapter) throws DataLayerException {
        ResultSet keys = null;
        int key = chapter.getKey();
        try {
            if (chapter.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!chapter.isDirty()) {
                    return;
                }
                uChapter.setInt(1, chapter.getNumber());
                uChapter.setString(2, chapter.getTitle());
                uChapter.setInt(3, chapter.getPublicationKey());
                uChapter.setInt(4, chapter.getKey());

                uChapter.executeUpdate();
            } else { //insert
                iChapter.setInt(1, chapter.getNumber());
                iChapter.setString(2, chapter.getTitle());
                iChapter.setInt(3, chapter.getPublicationKey());

                if (iChapter.executeUpdate() == 1) {
                    keys = iChapter.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                chapter.copyFrom(getChapter(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store chapter", ex);
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
    public void storeSection(Section section) throws DataLayerException {
        ResultSet keys = null;
        int key = section.getKey();
        try {
            if (section.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!section.isDirty()) {
                    return;
                }
                uSection.setInt(1, section.getNumber());
                uSection.setString(2, section.getTitle());
                uSection.setInt(3, section.getChapterKey());
                uSection.setInt(4, section.getKey());

                uSection.executeUpdate();
            } else { //insert
                iSection.setInt(1, section.getNumber());
                iSection.setString(2, section.getTitle());
                iSection.setInt(3, section.getChapterKey());

                if (iSection.executeUpdate() == 1) {
                    keys = iSection.getGeneratedKeys();
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
            if (key > 0) {
                section.copyFrom(getSection(key));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store section", ex);
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
    public void deleteAuthorFromPublication(int idPublication, int idAuthor) throws DataLayerException {
        ResultSet res = null;
        try {
            dAuthorFromPublication.setInt(1, idPublication);
            dAuthorFromPublication.setInt(2, idAuthor);
            dAuthorFromPublication.executeUpdate();
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
    public void deleteKeywordFromPublication(int idPublication, int idKeyword) throws DataLayerException {
        ResultSet res = null;
        try {
            dKeywordFromPublication.setInt(1, idPublication);
            dKeywordFromPublication.setInt(2, idKeyword);
            dKeywordFromPublication.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete keyword relation", ex);
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
    public int deleteAuthor(Author author) throws DataLayerException {
        ResultSet res = null;
        int rs = 0;
        try {
            dAuthor.setInt(1, author.getKey());
            dAuthor.setInt(2, author.getKey());
            rs = dAuthor.executeUpdate();
            
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete author", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
        return rs;
    }

    @Override
    public int deleteEditor(Editor editor) throws DataLayerException {
        ResultSet res = null;
        int rs = 0;
        try {
            dEditor.setInt(1, editor.getKey());
            dEditor.setInt(2, editor.getKey());
            rs = dEditor.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete editor", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
        return rs;
    }

    @Override
    public int deleteKeyword(Keyword keyword) throws DataLayerException {
        ResultSet res = null;
        int rs = 0;
        try {
            dKeyword.setInt(1, keyword.getKey());
            dKeyword.setInt(2, keyword.getKey());
            rs = dKeyword.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete keyword", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
        return rs;
    }

    @Override
    public void deletePublication(Publication publication) throws DataLayerException {
        ResultSet res = null;
        try {
            if (publication.getIndex() != null) {
                for (Chapter chapter : publication.getIndex()) {
                    dSectionByChapter.setInt(1, chapter.getKey());
                    dSectionByChapter.executeUpdate();
                }
            }
            dChapterByPublication.setInt(1, publication.getKey());
            dChapterByPublication.executeUpdate();
            dSourceByPublication.setInt(1, publication.getKey());
            dSourceByPublication.executeUpdate();
            dReprintByPublication.setInt(1, publication.getKey());
            dReprintByPublication.executeUpdate();
            dReviewByPublication.setInt(1, publication.getKey());
            dReviewByPublication.executeUpdate();
            dHistoryByPublication.setInt(1, publication.getKey());
            dHistoryByPublication.executeUpdate();
            deletePublicationHasAuthor(publication.getKey());
            deletePublicationHasKeyword(publication.getKey());
            dPublication.setInt(1, publication.getKey());
            dPublication.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete publication", ex);
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
    public void deleteIncompletePublication() throws DataLayerException {
        ResultSet res = null;
        try {
            sIncompletePublications.setDate(1, new Date(System.currentTimeMillis()));
            res = sIncompletePublications.executeQuery();
            while (res.next()) {
                List<Chapter> chap = getChapters(res.getInt("idpubblicazione"));
                if (chap != null) {
                    for (Chapter chapter : chap) {
                        dSectionByChapter.setInt(1, chapter.getKey());
                        dSectionByChapter.executeUpdate();
                    }
                }
                dChapterByPublication.setInt(1, res.getInt("idpubblicazione"));
                dChapterByPublication.executeUpdate();
                dSourceByPublication.setInt(1, res.getInt("idpubblicazione"));
                dSourceByPublication.executeUpdate();
                dReprintByPublication.setInt(1, res.getInt("idpubblicazione"));
                dReprintByPublication.executeUpdate();
                dReviewByPublication.setInt(1, res.getInt("idpubblicazione"));
                dReviewByPublication.executeUpdate();
                dHistoryByPublication.setInt(1, res.getInt("idpubblicazione"));
                dHistoryByPublication.executeUpdate();
                dLikeByPublication.setInt(1, res.getInt("idpubblicazione"));
                dLikeByPublication.execute();
                deletePublicationHasAuthor(res.getInt("idpubblicazione"));
                deletePublicationHasKeyword(res.getInt("idpubblicazione"));
                dIncompletePublication.setInt(1, res.getInt("idpubblicazione"));
                dIncompletePublication.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                dIncompletePublication.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete incomplete publications", ex);
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
    public void deleteReview(Review review) throws DataLayerException {
        ResultSet res = null;
        try {
            dReview.setInt(1, review.getKey());

            dReview.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete review", ex);
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
    public void deleteReprint(Reprint reprint) throws DataLayerException {
        ResultSet res = null;
        try {
            dReprint.setInt(1, reprint.getKey());

            dReprint.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete reprint", ex);
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
    public void deleteSource(Source source) throws DataLayerException {
        ResultSet res = null;
        try {
            dSource.setInt(1, source.getKey());

            dSource.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete source", ex);
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
    public int deleteChapter(Chapter chapter) throws DataLayerException {
        ResultSet res = null;
        int rs = 0;
        try {
            dChapter.setInt(1, chapter.getKey());
            dChapter.setInt(2, chapter.getKey());
            rs = dChapter.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete chapter", ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException ex) {
                //
            }
        }
        return rs;
    }

    @Override
    public void deleteSection(Section section) throws DataLayerException {
        ResultSet res = null;
        try {
            dSection.setInt(1, section.getKey());

            dSection.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete section", ex);
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
    public void deleteHistory(History history) throws DataLayerException {
        ResultSet res = null;
        try {
            dHistory.setInt(1, history.getKey());

            dHistory.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete history", ex);
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
    public void storeLike(int publication_key, int user_key) throws DataLayerException {
        ResultSet res = null;
        try {
            iUserLike.setInt(1, publication_key);
            iUserLike.setInt(2, user_key);

            iUserLike.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot relate user with publication", ex);
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
    public void deletePublicationLike(int idPublication) throws DataLayerException {
        ResultSet res = null;
        try {
            dUserLike.setInt(1, idPublication);

            dUserLike.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete like", ex);
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
    public int storeFilters(Map<String, String> filters) throws DataLayerException {
        ResultSet keys = null;
        int key = 0;
        try {
            //insert
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 1);
            iFilters.setString(1, Utils.getArrayParameter(filters, "isbn"));
            iFilters.setString(2, Utils.getArrayParameter(filters, "titolo"));
            iFilters.setString(3, Utils.getArrayParameter(filters, "autore"));
            iFilters.setString(4, Utils.getArrayParameter(filters, "editore"));
            iFilters.setString(5, Utils.getArrayParameter(filters, "anno_inizio"));
            iFilters.setString(6, Utils.getArrayParameter(filters, "anno_fine"));
            iFilters.setString(7, Utils.getArrayParameter(filters, "keyword"));
            iFilters.setString(8, Utils.getArrayParameter(filters, "lingua"));
            iFilters.setString(9, Utils.getArrayParameter(filters, "download"));
            iFilters.setString(10, Utils.getArrayParameter(filters, "utente"));
            iFilters.setTimestamp(11, new java.sql.Timestamp(calendar.getTime().getTime()));
            if (iFilters.executeUpdate() == 1) {
                keys = iFilters.getGeneratedKeys();
                if (keys.next()) {
                    key = keys.getInt(1);
                }
            }
            return key;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store filters", ex);
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
    public void deleteFilters() throws DataLayerException {
        //dFilters
        ResultSet res = null;
        try {
            Calendar time = Calendar.getInstance();
            dFilters.setTimestamp(1, new java.sql.Timestamp(time.getTimeInMillis()));

            dFilters.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Cannot delete filters", ex);
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
    public Map<String, String> getFilters(int filter_key) throws DataLayerException {
        //sFilters,
        Map<String, String> result = new HashMap<>();
        ResultSet rs = null;
        try {
            sFilters.setInt(1, filter_key);
            rs = sFilters.executeQuery();
            if (rs.next()) {
                result.put("isbn", rs.getString("isbn"));
                result.put("titolo", rs.getString("titolo"));
                result.put("autore", rs.getString("autore"));
                result.put("editore", rs.getString("editore"));
                result.put("anno_inizio", rs.getString("anno_inizio"));
                result.put("anno_fine", rs.getString("anno_fine"));
                result.put("keyword", rs.getString("keyword"));
                result.put("lingua", rs.getString("lingua"));
                result.put("download", rs.getString("download"));
                result.put("utente", rs.getString("utente"));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load filters", ex);
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

}
