/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
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

public class ComposeIndex extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("page_title", "Gestione Indice");
            TemplateResult res = new TemplateResult(getServletContext());
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
//                if (request.getParameter("indexId") != null) {
//                    request.setAttribute("indexId", request.getParameter("indexId"));
//                }
//                if (request.getParameter("submitIndex") != null && request.getAttribute("authorId") == null) {
//                    action_composeIndex(request, response);
//                }
//                if (request.getParameter("submitIndex") != null && request.getAttribute("authorId") != null) {
//                    action_updateIndex(request, response);
//                    request.removeAttribute("authorId");
//                }
//                if(request.getParameter("linkAuthor") != null){
//                    action_LinkIndex(request, response);
//                }
//                List<Author> authors = getDataLayer().getAuthors();
//                List<Author> publicationAuthors = getDataLayer().getPublicationAuthors((int) session.getAttribute("publicationId"));
//                request.setAttribute("authors", authors);
//                request.setAttribute("publicationAuthors", publicationAuthors);
                res.activate("index.ftl.html", request, response);
                
            } else {
                
            }
        } catch (Exception ex) {
            
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
    }// </editor-fold>

    private void action_composeIndex(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void action_updateIndex(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void action_LinkIndex(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
