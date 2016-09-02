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
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class ComposeKeyword extends BiblioManagerBaseController {

    private void action_composeKeyword(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            Keyword keyword = null;
            Map<String, String> params = new HashMap<String, String>();
            params.put("keyName", Utils.checkString(request.getParameter("keyName")));
            if (!validator(params, request, response)) {
                keyword = getDataLayer().createKeyword();
                keyword.setName(params.get("keyName"));
                getDataLayer().storeKeyword(keyword);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la parola chiave: " + ex.getMessage());
        }
    }

    private Keyword action_updateKeyword(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        Keyword keyword = null;
        try {
            keyword = getDataLayer().getKeyword(Integer.parseInt(request.getParameter("keywordId")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("keyName", Utils.checkString(request.getParameter("keyName")));
            if (!validator(params, request, response)) {
                keyword.setName(params.get("keyName"));
                getDataLayer().storeKeyword(keyword);
                request.setAttribute("saveResult", "Salvataggio effettuato con successo");
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage());
        }
        return keyword;
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
                if (request.getParameter("keywordId") != null) {
                    request.setAttribute("currentKeyword", request.getParameter("currentKeyword"));
                    request.setAttribute("keywordId", request.getParameter("keywordId"));
                }
                if (request.getParameter("submitKeyword") != null && request.getAttribute("keywordId") == null) {
                    action_composeKeyword(request, response);
                }
                if (request.getParameter("submitKeyword") != null && request.getAttribute("keywordId") != null) {
                    action_updateKeyword(request, response);
                }
                List<Keyword> keywords = getDataLayer().getKeywords();
                request.setAttribute("keywords", keywords);
            
                res.activate("keyword.ftl.html", request, response);
 
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
