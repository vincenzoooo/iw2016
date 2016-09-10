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

    private void action_composeEditor(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            Editor editor = getDataLayer().createEditor();
            Map<String, String> params = new HashMap<String, String>();
            params.put("editorName", Utils.checkString(request.getParameter("editorName")));
            if (!validator(params, request, response)) {
                editor.setName(params.get("editorName"));
                getDataLayer().storeEditor(editor);
                Publication publication = getDataLayer().getPublication((int) session.getAttribute("publicationId"));
                publication.setEditor(editor);
                getDataLayer().storePublication(publication);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'editore: " + ex.getMessage());
        }
    }

    private Editor action_updateEditor(HttpServletRequest request, HttpServletResponse response) {
        Editor editor = null;
        try {
            editor = getDataLayer().getEditor(Integer.parseInt(request.getParameter("editorId")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("editorName", Utils.checkString(request.getParameter("editorName")));
            if (!validator(params, request, response)) {
                editor.setName(params.get("editorName"));
                getDataLayer().storeEditor(editor);
                request.setAttribute("saveResult", "Salvataggio effettuato con successo");
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'editore: " + ex.getMessage());
        }
        return editor;
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
            request.setAttribute("page_title", "Gestione Editore");
            TemplateResult res = new TemplateResult(getServletContext());
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if (request.getParameter("editorId") != null) {
                    request.setAttribute("currentEditor", request.getParameter("currentEditor"));
                    request.setAttribute("editorId", request.getParameter("editorId"));
                }
                if (request.getParameter("submitEditor") != null && request.getAttribute("editorId") == null) {
                    action_composeEditor(request, response);
                }
                if (request.getParameter("submitEditor") != null && request.getAttribute("editorId") != null) {
                    action_updateEditor(request, response);
                    request.removeAttribute("editorId");
                }
                List<Editor> editors = getDataLayer().getEditors();
                request.setAttribute("editors", editors);

                res.activate("editor.ftl.html", request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage());
        }
    }
}
