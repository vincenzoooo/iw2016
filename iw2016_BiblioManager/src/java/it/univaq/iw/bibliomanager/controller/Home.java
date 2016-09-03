/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.univaq.iw.bibliomanager.data.model.User;
import it.univaq.iw.framework.result.HTMLResult;
import java.util.List;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Angelo Iezzi
 */
public class Home extends BiblioManagerBaseController {

    @Override
    protected void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("page_title", "Home");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("home.ftl.html", request, response);
    }
    
    private void action_anonymous(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HTMLResult result = new HTMLResult(getServletContext());
        request.setAttribute("page_title", "Homepage");
        if(request.getParameter("passiveUser")!=null){
            request.setAttribute("passiveUser", "");
        }
        if(request.getParameter("sesExp")!=null){
            request.setAttribute("sesExp", "");
        }
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("Homepage.ftl.html", request, response);
    }

    private void action_logged(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {

        HttpSession session = SecurityLayer.checkSession(request);
        if (session != null) {
            request.setAttribute("page_title", "Homepage");
            // recupero lo userId e il nome
            int userId = (int) session.getAttribute("userid");
            int status = (int) session.getAttribute("userStatus");
            User user = getDataLayer().getUser(userId);
            String nameLogged = SecurityLayer.stripSlashes(user.getName());
            request.setAttribute("nameLogged", nameLogged);
            request.setAttribute("usernameLogged", "Homepage");
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("userStatus", status);
            res.activate("home.ftl.html", request, response);
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
            List<Publication> publicationsInsert = getDataLayer().getLastInsertedPublication();
            List<Publication> publicationsUpdate = getDataLayer().getLastModifiedPublication();
            List<User> activeUsers = getDataLayer().getMoreActiveUsers();
            request.setAttribute("lastPublicationsInsert", publicationsInsert);
            request.setAttribute("lastPublicationsUpdate", publicationsUpdate);
            request.setAttribute("activeUsers", activeUsers);
            if (SecurityLayer.checkSession(request) != null) {
                action_logged(request, response);
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, "OPS: " + ex.getMessage());
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

}
