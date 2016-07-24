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
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.bibliomanager.data.model.User;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
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
import it.univaq.iw.bibliomanager.data.model.Keyword;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ComposePublication extends BiblioManagerBaseController {

    private void action_composePublication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        try {
            //TODO: Aggiungere qui i metadati e cambiare gestione degli elementi
            Publication publication = null;
            Reprint reprint = this.action_composeReprint(request, response);
            Map<String, String> params = new HashMap<String, String>();
            params.put("title", Utils.checkString(request.getParameter("publicationTitle")));
            params.put("description", Utils.checkString(request.getParameter("publicationDescription")));
            params.put("editor", Utils.checkString(request.getParameter("publicationEditor")));
            params.put("index", Utils.checkString(request.getParameter("publicationIndex")));
            params.put("editor", Utils.checkString(request.getParameter("editorId")));
            params.put("author", Utils.checkString(request.getParameter("authorId")));
            params.put("keywords", Utils.checkString(request.getParameter("keywordId")));
            params.put("sources", Utils.checkString(request.getParameter("sourceId")));
            if (!validator(params, request, response)) {
                publication.setTitle(params.get("title"));
                publication.setDescription(params.get("description"));
                publication.setIndex(params.get("index"));
                publication.setEditor(getDataLayer().getEditor(Integer.parseInt(params.get("editorId"))));
            }
            getDataLayer().storePublication(publication);
            reprint.setPublication(publication);
            getDataLayer().storeReprint(reprint);
            request.setAttribute("publication", publication);
            this.action_composeHistory(request, response);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("publication.ftl.html", request, response);
        } catch (DataLayerException | ParseException ex) {
            action_error(request, response, "Error: " + ex.getMessage());
        }
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
                newReprint = getDataLayer().createReprint();
                newReprint.setNumber(Integer.parseInt(params.get("number")));
                newReprint.setDate(new java.sql.Date(date.getTime()));
            }
        } catch (NumberFormatException | ParseException ex) {
            action_error(request, response, "Errore nel parsare i dati: " + ex.getMessage());
        }
        return newReprint;
    }

    private void action_composeHistory(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        HttpSession session = SecurityLayer.checkSession(request);
        User user = getDataLayer().getUser((int) session.getAttribute("userid"));
        History history = getDataLayer().createHistory();
        history.setEntry("Insert new publication");
        history.setType(0);
        history.setUser(user);
        history.setPublication((Publication) request.getAttribute("publication"));
        history.setDate(new java.sql.Date(System.currentTimeMillis()));
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
            List<Author> authors = getDataLayer().getAuthors();
            List<Editor> editors = getDataLayer().getEditors();
            List<Keyword> keywords = getDataLayer().getKeywords();
            List<Source> sources = getDataLayer().getSources();
            request.setAttribute("authors", authors);
            request.setAttribute("editors", editors);
            request.setAttribute("keywords", keywords);
            request.setAttribute("sources", sources);
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("submitPublication") != null) {
                    action_composePublication(request, response);
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
