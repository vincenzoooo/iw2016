/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Author;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class ComposeAuthor extends BiblioManagerBaseController {

    private void action_composeAuthor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Author author = null;
            Map<String, String> params = new HashMap<String, String>();
            params.put("authorName", Utils.checkString(request.getParameter("authorName")));
            params.put("authorSurname", Utils.checkString(request.getParameter("authorSurname")));
            if (!validator(params, request, response)) {
                author = getDataLayer().createAuthor();
                author.setName(params.get("authorName"));
                author.setSurname(params.get("authorSurname"));
                getDataLayer().storeAuthor(author);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'autore: " + ex.getMessage());
        }
    }

    private void action_updateAuthor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Author author = null;
            author = getDataLayer().getAuthor(Integer.parseInt(request.getParameter("authorId")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("authorName", Utils.checkString(request.getParameter("authorName")));
            params.put("authorSurname", Utils.checkString(request.getParameter("authorSurname")));
            if (!validator(params, request, response)) {
                author.setName(params.get("authorName"));
                author.setSurname(params.get("authorSurname"));
                getDataLayer().storeAuthor(author);
                request.setAttribute("saveResult", "Salvataggio effettuato con successo");
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'autore: " + ex.getMessage());
        }
    }

    @Override
    protected void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (SecurityLayer.checkSession(request) == null) {
            request.setAttribute("page_title", "Gestione Autore");
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("login.ftl.html", request, response);
        }
        else{
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("author.ftl.html", request, response);//DA impostare il nome effettivamente usato
        }
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
            request.setAttribute("page_title", "Gestione Autore");
            TemplateResult res = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("submitAuthor") != null && request.getParameter("authorId") == null) {
                    action_composeAuthor(request, response);
                }
                if (request.getParameter("submitAuthor") != null && request.getParameter("authorId") != null) {
                    action_updateAuthor(request, response);
                }
                if (request.getParameter("authorId") != null) {
                    request.setAttribute("currentNameAuthor", request.getParameter("currentNameAuthor"));
                    request.setAttribute("currentSurnameAuthor", request.getParameter("currentSurnameAuthor"));
                    request.setAttribute("authorId", request.getParameter("authorId"));
                }
                List<Author> authors = getDataLayer().getAuthors();
                request.setAttribute("authors", authors);
            
                res.activate("author.ftl.html", request, response);
                
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, "OPS" + ex.getMessage());
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
