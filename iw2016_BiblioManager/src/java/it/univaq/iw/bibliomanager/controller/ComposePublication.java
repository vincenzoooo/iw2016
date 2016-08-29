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
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.bibliomanager.data.model.User;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
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
            Publication publication = null;
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
            request.setAttribute("publication", publication);
            /**this.action_composeHistory(request, response);*/
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("publication.ftl.html", request, response);
        } catch (DataLayerException ex) {
            action_error(request, response, "Error: " + ex.getMessage());
        }
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
