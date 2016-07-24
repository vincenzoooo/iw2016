/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Author;
import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class Search extends BiblioManagerBaseController {

    //ISBN, titolo, autore, editore, anno di pubblicazione, parole chiave, lingua, presenza di sorgenti di tipo “download” (quindi pubblicazioni gratuite).
    private void action_search(HttpServletRequest request, HttpServletResponse response) throws DataLayerException, ServletException, IOException {
        String authors = action_searchAuthor(request.getParameter("authors"));
        String editors = action_searchEditor(request.getParameter("editors"));
        List<Publication> publications = getDataLayer().getPublications();
        request.setAttribute("publicationsSearch", publications);
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("catalog.ftl.html", request, response);
    }

    private String action_searchAuthor(String authors) throws DataLayerException {
        List<Author> authorsDB = new ArrayList();
        String res = "";
        String[] params = authors.split("[, |-]");
        for (String param : params) {
            authorsDB.addAll(getDataLayer().getAuthorsByName(param));
        }
        for (Author author : authorsDB) {
            res += author.getKey();
        }
        return res;
    }

    private String action_searchEditor(String editors) throws DataLayerException {
        List<Editor> editorsDB = new ArrayList();
        String res = "";
        String[] params = editors.split("[, |-]");
        for (String param : params) {
            editorsDB.addAll(getDataLayer().getEditorsByName(param));
        }
        for (Editor editor : editorsDB) {
            res += editor.getKey();
        }
        return res;
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("page_title", "Search");
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
                action_search(request, response);
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
