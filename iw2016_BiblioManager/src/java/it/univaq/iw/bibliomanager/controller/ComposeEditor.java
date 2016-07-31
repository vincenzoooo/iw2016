/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Editor;
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
public class ComposeEditor extends BiblioManagerBaseController {

    private Editor action_composeEditor(HttpServletRequest request, HttpServletResponse response) {
        Editor editor = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("editorName", Utils.checkString(request.getParameter("editorName")));
            if (!validator(params, request, response)) {
                editor = getDataLayer().createEditor();
                editor.setName(params.get("name"));
                getDataLayer().storeEditor(editor);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare l'editore: " + ex.getMessage());
        }
        return editor;
    }

    private Editor action_updateEditor(HttpServletRequest request, HttpServletResponse response) {
        Editor editor = null;
        try {
            editor = getDataLayer().getEditor(Integer.parseInt(request.getParameter("ideditor")));
            Map<String, String> params = new HashMap<String, String>();
            params.put("editorName", Utils.checkString(request.getParameter("editorName")));
            if (!validator(params, request, response)) {
                editor.setName(params.get("name"));
                getDataLayer().storeEditor(editor);
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
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("ideditor") != null) {
                    Editor editor = getDataLayer().getEditor(Integer.parseInt(request.getParameter("ideditor")));
                    request.setAttribute("editor", editor);
                }
                if (request.getParameter("submitEditor") != null && request.getParameter("ideditor") != null) {
                    action_updateEditor(request, response);
                }
                //TODO: Verificarne la correttezza
                if (request.getParameter("submitEditor") != null && request.getParameter("ideditor") == null) {
                    action_composeEditor(request, response);
                }
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, "OPS");
        }
    }
}
