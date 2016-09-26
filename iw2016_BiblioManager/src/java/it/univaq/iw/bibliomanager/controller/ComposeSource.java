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
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi;
 */
public class ComposeSource extends BiblioManagerBaseController {

    private int publicationId;
    private String url;
    private int sourceId;
    private String currentTypeSource;
    private String currentUriSource;
    private String currentFormatSource;
    private String currentDescriptionSource;
    
    private void action_composeSource(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            Source source = getDataLayer().createSource();
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
                source.setPublication(getDataLayer().getPublication(publicationId));
                getDataLayer().storeSource(source);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage(), 510);
        }
    }

    private void action_updateSource(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            Source source = getDataLayer().getSource(sourceId);
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
                source.setPublication(getDataLayer().getPublication(publicationId));
                getDataLayer().storeSource(source);
                sourceId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage(), 510);
        }
    }

    private void action_view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Gestione Sorgenti");
        TemplateResult res = new TemplateResult(getServletContext());
        List<Source> sources = getDataLayer().getPublicationSources(publicationId);
        request.setAttribute("sources", sources);
        request.setAttribute("publicationId", publicationId);
        request.setAttribute("url", url);
        request.setAttribute("sourceId", sourceId);
        request.setAttribute("currentDescriptionSource", currentDescriptionSource);
        request.setAttribute("currentFormatSource", currentFormatSource);
        request.setAttribute("currentTypeSource", currentTypeSource);
        request.setAttribute("currentUriSource", currentUriSource);
        res.activate("source.ftl.html", request, response);
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
                if (request.getParameter("sourceId") != null) {
                    currentTypeSource = request.getParameter("currentTypeSource");
                    currentUriSource = request.getParameter("currentUriSource");
                    currentFormatSource = request.getParameter("currentFormatSource");
                    currentDescriptionSource = request.getParameter("currentDescriptionSource");
                    sourceId = Integer.parseInt(request.getParameter("sourceId"));
                }
                if (request.getParameter("submitSource") != null){
                    if(sourceId == 0) {
                        action_composeSource(request, response);
                    }
                    else{
                        action_updateSource(request, response);
                    }
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | ServletException ex) {
            action_error(request, response, "Errore: " + ex.getMessage(), 501);
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
