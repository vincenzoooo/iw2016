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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class ComposeKeyword extends BiblioManagerBaseController {

    private int publicationId;
    private String url;
    private int keywordId;
    private String currentKeyword;
    
    private void action_composeKeyword(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        try {
            Keyword keyword = getDataLayer().createKeyword();
            Map<String, String> params = new HashMap<String, String>();
            params.put("keyName", Utils.checkString(request.getParameter("keyName")));
            if (!validator(params, request, response)) {
                keyword.setName(params.get("keyName"));
                getDataLayer().storeKeyword(keyword);
                request.setAttribute("keywordAdded", 1);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la parola chiave: " + ex.getMessage(), 510);
        }
    }

    private void action_updateKeyword(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        try {
            Keyword keyword = getDataLayer().getKeyword(keywordId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("keyName", Utils.checkString(request.getParameter("keyName")));
            if (!validator(params, request, response)) {
                keyword.setName(params.get("keyName"));
                getDataLayer().storeKeyword(keyword);
                request.setAttribute("keywordUpdated", 1);
                request.setAttribute("saveResult", "Salvataggio effettuato con successo");
                keywordId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage(), 510);
        }
    }

    private void action_LinkKeyword(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        List<String> values = new ArrayList<String>(Arrays.asList(request.getParameterValues("keywordSelected")));
        getDataLayer().deletePublicationHasKeyword(publicationId);
        for (String value : values) {
            getDataLayer().storePublicationHasKeyword(Integer.parseInt(value), publicationId);
            request.setAttribute("keywordConnected", 1);
        }
    }

    private void action_view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Gestione Parole Chiave");
        TemplateResult res = new TemplateResult(getServletContext());
        List<Keyword> keywords = getDataLayer().getKeywords();
        List<Keyword> publicationKeywords = getDataLayer().getPublicationKeywords(publicationId);
        request.setAttribute("keywords", keywords);
        request.setAttribute("publicationKeywords", publicationKeywords);
        request.setAttribute("publicationId", publicationId);
        request.setAttribute("url", url);
        request.setAttribute("keywordId", keywordId);
        request.setAttribute("currentKeyword", currentKeyword);
        res.activate("keyword.ftl.html", request, response);
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
            
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if(request.getAttribute("publicationId") != null){
                    publicationId = (int) request.getAttribute("publicationId");
                }
                if(request.getAttribute("url") != null){
                    url = (String) request.getAttribute("url");
                }
                if (request.getParameter("keywordId") != null) {
                    currentKeyword = request.getParameter("currentKeyword");
                    keywordId = Integer.parseInt(request.getParameter("keywordId"));
                }
                if (request.getParameter("submitKeyword") != null){
                    if(keywordId == 0) {
                        action_composeKeyword(request, response);
                    }
                    else{
                        action_updateKeyword(request, response);
                    }
                }
                if (request.getParameter("linkKeyword") != null) {
                    action_LinkKeyword(request, response);
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 501);
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
