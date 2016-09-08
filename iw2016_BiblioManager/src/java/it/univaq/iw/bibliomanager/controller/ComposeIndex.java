/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Editor;
import it.univaq.iw.bibliomanager.data.model.IndexElement;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Angelo Iezzi
 */
public class ComposeIndex extends BiblioManagerBaseController {

    private void action_composeChapter(HttpServletRequest request, HttpServletResponse response) {
        try{
            HttpSession session = SecurityLayer.checkSession(request);
            IndexElement chapter = null;
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapterTitle", Utils.checkString(request.getParameter("chapterTitle")));
            params.put("chapterNumber", Utils.checkString(request.getParameter("chapterNumber")));
            if (!validator(params, request, response)) {
                chapter = getDataLayer().createChapter();
                chapter.setNumber(Integer.parseInt(params.get("chapterNumber")));
                chapter.setTitle(params.get("chapterTitle"));
                Publication publication = getDataLayer().getPublication((int) session.getAttribute("publicationId"), true);
                chapter.setPublication(publication);
                getDataLayer().storeChapter(chapter);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il capitolo: " + ex.getMessage());
        }
    }

    private void action_updateChapter(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void action_composeSection(HttpServletRequest request, HttpServletResponse response) {
        try{
            HttpSession session = SecurityLayer.checkSession(request);
            IndexElement section = null;
            Map<String, String> params = new HashMap<String, String>();
            params.put("chapter", Utils.checkString(request.getParameter("chapter")));
            params.put("sectionTitle", Utils.checkString(request.getParameter("sectionTitle")));
            params.put("sectionNumber", Utils.checkString(request.getParameter("sectionNumber")));
            if (!validator(params, request, response)) {
                section = getDataLayer().createSection();
                section.setNumber(Integer.parseInt(params.get("sectionNumber")));
                section.setTitle(params.get("sectionTitle"));
                IndexElement chapter = getDataLayer().getChapter(Integer.parseInt(params.get("chapter")));
                section.setAncestor(chapter);
                getDataLayer().storeSection(section);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare il sezione: " + ex.getMessage());
        }
    }

    private void action_updateSection(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            throws ServletException{
        try {
            request.setAttribute("page_title", "Gestione Indice");
            TemplateResult res = new TemplateResult(getServletContext());
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if (request.getParameter("submitChapter") != null) {
                    action_composeChapter(request, response);
                }
                if (request.getParameter("submitSection") != null) {
                    action_composeSection(request, response);
                }
                List<IndexElement> chapters = getDataLayer().getChapters((int) session.getAttribute("publicationId"));
                request.setAttribute("chapters", chapters);
//                List<IndexElement> sections = getDataLayer().getSections(Integer.parseInt(request.getParameter("chapter")));
//                if (request.getParameter("submitIndex") != null && request.getAttribute("authorId") == null) {
//                    action_composeIndex(request, response);
//                }
//                if (request.getParameter("submitIndex") != null && request.getAttribute("authorId") != null) {
//                    action_updateIndex(request, response);
//                    request.removeAttribute("authorId");
//                }
                res.activate("index.ftl.html", request, response);

            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
               action_error(request, response, "Error: " + ex.getMessage());
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
