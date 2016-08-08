/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Author;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ComposeAuthor extends BiblioManagerBaseController {

    private Author action_composeAuthor(HttpServletRequest request, HttpServletResponse response) {
        int idPublication = Integer.parseInt(request.getParameter("idPublication"));
        Author author = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", Utils.checkString(request.getParameter("authorName")));
            params.put("surname", Utils.checkString(request.getParameter("authorSurname")));
            if (!validator(params, request, response)) {
                author = getDataLayer().createAuthor();
                author.setName(params.get("name"));
                author.setSurname(params.get("name"));
                getDataLayer().storeAuthor(author);
                getDataLayer().storePublicationHasAuthor(author.getKey(), idPublication);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'autore: " + ex.getMessage());
        }
        return author;
    }

    private Author action_updateAuthor(HttpServletRequest request, HttpServletResponse response) {
        int idPublication = Integer.parseInt(request.getParameter("idPublication"));
        Author author = null;
        try {
            author = getDataLayer().getAuthor(Integer.parseInt(request.getParameter("idauthor")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", Utils.checkString(request.getParameter("authorName")));
            params.put("surname", Utils.checkString(request.getParameter("authorSurname")));
            if (!validator(params, request, response)) {
                author.setName(params.get("name"));
                author.setSurname(params.get("name"));
                getDataLayer().storeAuthor(author);
                getDataLayer().storePublicationHasAuthor(author.getKey(), idPublication);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'autore: " + ex.getMessage());
        }
        return author;
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
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("idauthor") != null) {
                    Author author = getDataLayer().getAuthor(Integer.parseInt(request.getParameter("idauthor")));
                    request.setAttribute("author", author);
                }
                if (request.getParameter("submitAuthor") != null && request.getParameter("idauthor") != null) {
                    action_updateAuthor(request, response);
                }
                //TODO: Verificarne la correttezza
                if (request.getParameter("submitAuthor") != null && request.getParameter("idauthor") == null) {
                    action_composeAuthor(request, response);
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
