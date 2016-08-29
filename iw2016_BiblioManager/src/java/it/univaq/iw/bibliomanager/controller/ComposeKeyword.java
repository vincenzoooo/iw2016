/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Keyword;
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

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ComposeKeyword extends BiblioManagerBaseController {

    private Keyword action_composeKeyword(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        int idPublication = Integer.parseInt(request.getParameter("idPublication"));
        Keyword keyword = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", Utils.checkString(request.getParameter("keyName")));
            if (!validator(params, request, response)) {
                keyword = getDataLayer().createKeyword();
                keyword.setName(params.get("name"));
                getDataLayer().storeKeyword(keyword);
                getDataLayer().storePublicationHasKeyword(keyword.getKey(), idPublication);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage());
        }
        return keyword;
    }

    private Keyword action_updateKeyword(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        int idPublication = Integer.parseInt(request.getParameter("idPublication"));
        Keyword keyword = null;
        try {
            keyword = getDataLayer().getKeyword(Integer.parseInt(request.getParameter("idkeyword")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", Utils.checkString(request.getParameter("keyName")));
            if (!validator(params, request, response)) {
                keyword.setName(params.get("name"));
                getDataLayer().storeKeyword(keyword);
                getDataLayer().storePublicationHasKeyword(keyword.getKey(), idPublication);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage());
        }
        return keyword;
    }

    @Override
    protected void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (SecurityLayer.checkSession(request) == null) {
            request.setAttribute("page_title", "Login to Biblio");
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("login.ftl.html", request, response);
        }
        else{
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("key.ftl.html", request, response);//DA impostare il nome effettivamente usato
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
            request.setAttribute("page_title", "Gestione Parole Chiave");
            TemplateResult res = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("idkeword") != null) {
                    Keyword keyword = getDataLayer().getKeyword(Integer.parseInt(request.getParameter("idkeyword")));
                    request.setAttribute("keyword", keyword);
                    res.activate("key.ftl.html", request, response);//DA impostare il nome effettivamente usato
                }
                if (request.getParameter("submitKeyword") != null && request.getParameter("idkeyword") != null) {
                    Keyword keyword = action_updateKeyword(request, response);
                    request.setAttribute("keyword", keyword);
                    res.activate("key.ftl.html", request, response);//DA impostare il nome effettivamente usato
                }
                //TODO: Verificarne la correttezza
                if (request.getParameter("submitKeyword") != null && request.getParameter("idkeyword") == null) {
                    action_composeKeyword(request, response);
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
