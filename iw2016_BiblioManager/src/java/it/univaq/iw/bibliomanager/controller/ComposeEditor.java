/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.Publication;
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
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class ComposeEditor extends BiblioManagerBaseController {

    private int publicationId;
    private String url;
    private int editorId;
    private String currentEditor;
    private final Map<Integer, String> pages = new HashMap<>();
    private final Map<String, Integer> options = new HashMap<>();
    
    private void action_composeEditor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Editor editor = getDataLayer().createEditor();
            Map<String, String> params = new HashMap<String, String>();
            params.put("editorName", Utils.checkString(request.getParameter("editorName")));
            if (!validator(params, request, response)) {
                editor.setName(params.get("editorName"));
                getDataLayer().storeEditor(editor);
                request.setAttribute("editorAdded", 1);
                Publication publication = getDataLayer().getPublication(publicationId);
                publication.setEditor(editor);
                getDataLayer().storePublication(publication);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'editore: " + ex.getMessage(), 510);
        }
    }

    private void action_updateEditor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Editor editor = getDataLayer().getEditor(editorId);
            Map<String, String> params = new HashMap<String, String>();
            params.put("editorName", Utils.checkString(request.getParameter("editorName")));
            if (!validator(params, request, response)) {
                editor.setName(params.get("editorName"));
                getDataLayer().storeEditor(editor);
                request.setAttribute("editorUpdated", 1);
                request.setAttribute("saveResult", "Salvataggio effettuato con successo");
                editorId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'editore: " + ex.getMessage(), 510);
        }
    }

    private void action_view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Gestione Editore");
        TemplateResult res = new TemplateResult(getServletContext());
        List<Editor> editors = getDataLayer().getEditors(options.get("limit"), options.get("offset"));
        
        request.setAttribute("totElements", getDataLayer().getEditors(0, 0).size());
        request.setAttribute("paginationUrl", "editor");
        pagination(request, response, pages, options);
        
        request.setAttribute("editors", editors);
        request.setAttribute("publicationId", publicationId);
        request.setAttribute("url", url);
        request.setAttribute("editorId", editorId);
        request.setAttribute("currentEditor", currentEditor);
        res.activate("editor.ftl.html", request, response);
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
                if (request.getParameter("editorId") != null) {
                    currentEditor = request.getParameter("currentEditor");
                    editorId = Integer.parseInt(request.getParameter("editorId"));
                }
                if (request.getParameter("submitEditor") != null){
                    if(editorId == 0) {
                        action_composeEditor(request, response);
                    }
                    else{
                        action_updateEditor(request, response);
                    }
                }
                if (request.getParameter("offset") != null){
                   options.put("offset", Integer.parseInt(request.getParameter("offset")));
                }
                else{
                   pages.clear();
                   options.put("limit", 10);
                   options.put("offset", 0);
                   options.put("slice", 10);
                   options.put("start", 0);
                   options.put("end", 10);
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 501);
        }
    }
}
