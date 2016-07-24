/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Source;
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
public class ComposeSource extends BiblioManagerBaseController {

    private Source action_composeSource(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        Source source = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", Utils.checkString(request.getParameter("sourceType")));
            params.put("uri", Utils.checkString(request.getParameter("sourceUri")));
            params.put("format", Utils.checkString(request.getParameter("sourceFormat")));
            params.put("description", Utils.checkString(request.getParameter("sourceDescription")));
            if (!validator(params, request, response)) {
                source = getDataLayer().createSource();
                source.setType(params.get("type"));
                source.setURI(params.get("uri"));
                source.setFormat(params.get("format"));
                source.setDescription(params.get("description"));
                getDataLayer().storeSource(source);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage());
        }
        return source;
    }

    private Source action_updateSource(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        Source source = null;
        try {
            source = getDataLayer().getSource(Integer.parseInt(request.getParameter("idsource")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", Utils.checkString(request.getParameter("sourceType")));
            params.put("uri", Utils.checkString(request.getParameter("sourceUri")));
            params.put("format", Utils.checkString(request.getParameter("sourceFormat")));
            params.put("description", Utils.checkString(request.getParameter("sourceDescription")));
            if (!validator(params, request, response)) {
                source.setType(params.get("type"));
                source.setURI(params.get("uri"));
                source.setFormat(params.get("format"));
                source.setDescription(params.get("description"));
                getDataLayer().storeSource(source);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage());
        }
        return source;
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
            request.setAttribute("page_title", "Gestione Sorgenti");
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("idsource") != null) {
                    Source source = getDataLayer().getSource(Integer.parseInt(request.getParameter("idsource")));
                    request.setAttribute("source", source);
                }
                if (request.getParameter("submitSource") != null && request.getParameter("idsource") != null) {
                    action_updateSource(request, response);
                }
                //TODO: Verificarne la correttezza
                if (request.getParameter("submitSource") != null && request.getParameter("idsource") == null) {
                    action_composeSource(request, response);
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
