/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Keyword;
import it.univaq.iw.bibliomanager.data.model.Source;
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
 * @author Vincenzo Lanzieri
 */
public class ComposeSource extends BiblioManagerBaseController {

    private void action_composeSource(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            Source source = null;
            Map<String, String> params = new HashMap<String, String>();
            params.put("sourceType", Utils.checkString(request.getParameter("sourceType")));
            params.put("sourceUri", Utils.checkString(request.getParameter("sourceUri")));
            params.put("sourceFormat", Utils.checkString(request.getParameter("sourceFormat")));
            params.put("sourceDescription", Utils.checkString(request.getParameter("sourceDescription")));
            if (!validator(params, request, response)) {
                source = getDataLayer().createSource();
                source.setType(params.get("sourceType"));
                source.setUri(params.get("sourceUri"));
                source.setFormat(params.get("sourceFormat"));
                source.setDescription(params.get("sourceDescription"));
                getDataLayer().storeSource(source);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage());
        }
    }

    private void action_updateSource(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            Source source = getDataLayer().getSource(Integer.parseInt(request.getParameter("idSource")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("sourceType", Utils.checkString(request.getParameter("sourceType")));
            params.put("sourceUri", Utils.checkString(request.getParameter("sourceUri")));
            params.put("sourceFormat", Utils.checkString(request.getParameter("sourceFormat")));
            params.put("sourceDescription", Utils.checkString(request.getParameter("sourceDescription")));
            if (!validator(params, request, response)) {
                source.setType(params.get("sourceType"));
                source.setUri(params.get("sourceUri"));
                source.setFormat(params.get("sourceFormat"));
                source.setDescription(params.get("sourceDescription"));
                getDataLayer().storeSource(source);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage());
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
            request.setAttribute("page_title", "Gestione Sorgenti");
            TemplateResult res = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("submitSource") != null && request.getParameter("idSource") == null) {
                    action_composeSource(request, response);
                }
                if (request.getParameter("submitSource") != null && request.getParameter("idSource") != null) {
                    action_updateSource(request, response);
                }
                List<Source> sources = getDataLayer().getSources();
                request.setAttribute("sources", sources);
                
                res.activate("source.ftl.html", request, response);
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, "Errore: " + ex.getMessage());
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
