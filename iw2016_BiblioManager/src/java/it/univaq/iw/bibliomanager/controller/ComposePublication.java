/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Author;
import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.bibliomanager.data.model.Reprint;
import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class ComposePublication extends BiblioManagerBaseController {
    
    private void action_composePublication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
        HttpSession session = SecurityLayer.checkSession(request);
        Publication publication = null;

//            int userKey =  (int) session.getAttribute("userid");
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", Utils.checkString(request.getParameter("title")));
        params.put("description", Utils.checkString(request.getParameter("description")));
        params.put("editor", Utils.checkString(request.getParameter("editor")));
        params.put("index", Utils.checkString(request.getParameter("index")));
        if (!validator(params, request, response)) {
//                publication.setTitle(title);
//                publication.setDescription(description);
//                publication.setEditor(editor);
//                publication.setIndex(index);
        }
//            getDataLayer().storeUser(user);
//            request.setAttribute("user", user);
//            TemplateResult res = new TemplateResult(getServletContext());
//            res.activate("profile.ftl.html", request, response);
//        } catch (DataLayerException ex) {
//            action_error(request, response, "Error: " + ex.getMessage());
//        }
    }
    
    private boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        boolean error = false;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                String fieldName = Character.toUpperCase(entry.getKey().charAt(0)) + entry.getKey().substring(1);
                request.setAttribute("error" + fieldName, "Non valorizzato");
                error = true;
            }
        }
        return error;
    }
    
    private void action_composeSource(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", Utils.checkString(request.getParameter("type")));
            params.put("uri", Utils.checkString(request.getParameter("uri")));
            params.put("format", Utils.checkString(request.getParameter("format")));
            params.put("description", Utils.checkString(request.getParameter("description")));
            //TODO: Inserire ID pubblicazione
            if (!validator(params, request, response)) {
                Source newSource = datalayer.createSource();
                newSource.setType(params.get("type"));
                newSource.setURI(params.get("uri"));
                newSource.setFormat(params.get("format"));
                newSource.setDescription(params.get("description"));
                datalayer.storeSource(newSource);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Impossibile salvare al risorsa: " + ex.getMessage());
        }
    }
    
    private void action_composeReprint(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("number", Utils.checkString(request.getParameter("number")));
            params.put("date", Utils.checkString(request.getParameter("date")));
            //TODO: Inserire ID pubblicazione
            if (!validator(params, request, response)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(params.get("date"));
                Reprint newReprint = datalayer.createReprint();
                newReprint.setNumber(Integer.parseInt(params.get("number")));
                newReprint.setDate(new java.sql.Date(date.getTime()));
                datalayer.storeReprint(newReprint);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Impossibile salvare la ristampa: " + ex.getMessage());
        } catch (NumberFormatException | ParseException ex) {
            action_error(request, response, "Impossibile parsare i dati: " + ex.getMessage());
        }
    }
    
    private void action_composeEditor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", Utils.checkString(request.getParameter("name")));
            Editor editor = datalayer.getEditor(Integer.parseInt(request.getParameter("id")));
            if (editor == null) {
                if (!validator(params, request, response)) {
                    Editor newEditor = datalayer.createEditor();
                    newEditor.setName(params.get("name"));
                    datalayer.storeEditor(newEditor);
                }
            }
            //TODO: Salvare l'editore in base all'id/Fare un get dell'editore in base all'id
            request.setAttribute("EditorX", editor);
        } catch (DataLayerException ex) {
            action_error(request, response, "Impossibile salvare la ristampa: " + ex.getMessage());
        }
    }
    
    private void action_composeAuthor(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", Utils.checkString(request.getParameter("name")));
            params.put("surname", Utils.checkString(request.getParameter("surname")));
            Author author = datalayer.getAuthor(Integer.parseInt(request.getParameter("id")));
            if (author == null) {
                if (!validator(params, request, response)) {
                    Author newAuthor = datalayer.createAuthor();
                    newAuthor.setName(params.get("name"));
                    newAuthor.setSurname(params.get("name"));
                    datalayer.storeAuthor(newAuthor);
                }
            }
            //TODO: Salvare l'editore in base all'id/Fare un get dell'editore in base all'id
            request.setAttribute("AuthorX", author);
        } catch (DataLayerException ex) {
            action_error(request, response, "Impossibile salvare la ristampa: " + ex.getMessage());
        }
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
            request.setAttribute("page_title", "Nuova Pubblicazione");
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("submitPublication") != null) {
                    action_composePublication(request, response);
                } else {
                    //TODO: Azione per rendere disponibile la form di inserimento della pubblicazione
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
