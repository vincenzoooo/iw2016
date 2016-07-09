/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Author;
import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.Metadata;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.bibliomanager.data.model.User;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ComposePublication extends BiblioManagerBaseController {

    private void action_composePublication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        try {
        Publication publication = null;
        Editor editor = null;
        Source source = null;
        Author author = null;
        Reprint reprint = this.action_composeReprint(request, response);
        Metadata metadata = this.action_composeMetadata(request, response);
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", Utils.checkString(request.getParameter("publicationTitle")));
        params.put("description", Utils.checkString(request.getParameter("publicationDescription")));
        params.put("editor", Utils.checkString(request.getParameter("publicationEditor")));
        params.put("index", Utils.checkString(request.getParameter("publicationIndex")));
        if(request.getParameter("editorId").isEmpty()){
            editor = this.action_composeEditor(request, response);
        }
        else{
            editor = getDataLayer().getEditor(Integer.parseInt(request.getParameter("editorId")));
        }
        if(request.getParameter("sourceId").isEmpty()){
            source = this.action_composeSource(request, response);
        }
        else{
            source = getDataLayer().getSource(Integer.parseInt(request.getParameter("sourceId")));
        }
        if(request.getParameter("authorId").isEmpty()){
            author = this.action_composeAuthor(request, response);
        }
        else{
            author = getDataLayer().getAuthor(Integer.parseInt(request.getParameter("authorId")));
        }
        if (!validator(params, request, response)) {
            publication.setTitle(params.get("title"));
            publication.setDescription(params.get("description"));
            publication.setIndex(params.get("index"));
            publication.setEditor(editor);
        }
        getDataLayer().storePublication(publication);
        reprint.setPublication(publication);
        getDataLayer().storeReprint(reprint);
        metadata.setPublication(publication);
        getDataLayer().storeMetadata(metadata);
        request.setAttribute("publication", publication);
        this.action_composeHistory(request, response);
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("publication.ftl.html", request, response);
        } catch (DataLayerException | ParseException ex) {
            action_error(request, response, "Error: " + ex.getMessage());
        }
    }

    private boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        boolean error = false;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                String fieldName = Character.toUpperCase(entry.getKey().charAt(0)) + entry.getKey().substring(1);
                request.setAttribute("error" + fieldName, "Non valorizzato");
                error = true;
            }
        }
        return error;
    }

    private Source action_composeSource(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        Source newSource = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", Utils.checkString(request.getParameter("sourceType")));
            params.put("uri", Utils.checkString(request.getParameter("sourceUri")));
            params.put("format", Utils.checkString(request.getParameter("sourceFormat")));
            params.put("description", Utils.checkString(request.getParameter("sourceDescription")));
            if (!validator(params, request, response)) {
                newSource = datalayer.createSource();
                newSource.setType(params.get("type"));
                newSource.setURI(params.get("uri"));
                newSource.setFormat(params.get("format"));
                newSource.setDescription(params.get("description"));
                datalayer.storeSource(newSource);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage());
        }
        return newSource;
    }

    private Reprint action_composeReprint(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
        Reprint newReprint = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("number", Utils.checkString(request.getParameter("reprintNumber")));
            params.put("date", Utils.checkString(request.getParameter("reprintDate")));
            if (!validator(params, request, response)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(params.get("date"));
                newReprint = datalayer.createReprint();
                newReprint.setNumber(Integer.parseInt(params.get("number")));
                newReprint.setDate(new java.sql.Date(date.getTime()));
            }
        } catch (NumberFormatException | ParseException ex) {
            action_error(request, response, "Errore nel parsare i dati: " + ex.getMessage());
        }
        return newReprint;
    }

    private Editor action_composeEditor(HttpServletRequest request, HttpServletResponse response) {
        Editor editor = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("editorName", Utils.checkString(request.getParameter("editorName")));
            editor = datalayer.getEditor(Integer.parseInt(request.getParameter("id")));
                if (!validator(params, request, response)) {
                    editor = datalayer.createEditor();
                    editor.setName(params.get("name"));
                    datalayer.storeEditor(editor);
                }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'editore: " + ex.getMessage());
        }
        return editor;
    }

    private Author action_composeAuthor(HttpServletRequest request, HttpServletResponse response) {
        Author author = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", Utils.checkString(request.getParameter("authorName")));
        params.put("surname", Utils.checkString(request.getParameter("authorSurname")));
            if (!validator(params, request, response)) {
                author = datalayer.createAuthor();
                author.setName(params.get("name"));
                author.setSurname(params.get("name"));
            }
        return author;
    }

    private Metadata action_composeMetadata(HttpServletRequest request, HttpServletResponse response) throws ParseException{
        Metadata metadata = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("isbn", Utils.checkString(request.getParameter("metadataIsbn")));
        params.put("pages", Utils.checkString(request.getParameter("metadataPages")));
        params.put("language", Utils.checkString(request.getParameter("metadataLanguage")));
        params.put("publicationDate", Utils.checkString(request.getParameter("metadataPublicationDate")));
        params.put("keys", Utils.checkString(request.getParameter("metadataKey")));
            if (!validator(params, request, response)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(params.get("publicationDate"));
                metadata = datalayer.createMetadata();
                metadata.setISBN(Integer.parseInt(params.get("isbn")));
                metadata.setPages(Integer.parseInt(params.get("pages")));
                metadata.setLanguage(params.get("language"));
                metadata.setPublicationDate(new java.sql.Date(date.getTime()));
                metadata.setKeywords(params.get("keys"));
            }
        return metadata;
    }
    
    private void action_composeHistory(HttpServletRequest request, HttpServletResponse response) throws DataLayerException{
        HttpSession session = SecurityLayer.checkSession(request);
        User user = getDataLayer().getUser((int)session.getAttribute("userid"));
        History history = getDataLayer().createHistory();
        history.setEntry("Insert");
        history.setType(0);
        history.setUser(user);
        history.setPublication((Publication) request.getAttribute("publication"));
        history.setTimestamp(new Timestamp(System.currentTimeMillis()));
        getDataLayer().storeHistory(history);
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("page_title", "Login to Biblio");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("login.ftl.html", request, response);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            request.setAttribute("page_title", "Nuova Pubblicazione");
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("submitPublication") != null) {
                    action_composePublication(request, response);
                } else {
                    //TODO: Azione per rendere disponibile la form di inserimento della pubblicazione
                }

            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, "OPS");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
