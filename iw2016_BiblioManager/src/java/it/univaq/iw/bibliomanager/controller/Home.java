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

    private void action_logged(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataLayerException {

        HttpSession session = SecurityLayer.checkSession(request);
        if (session != null) {
            request.setAttribute("page_title", "Homepage");
            // recupero lo userId e il nome
            int userId = (int) session.getAttribute("userid");
            int role = (int) session.getAttribute("userRole");
            User user = getDataLayer().getUser(userId);
            String nameLogged = SecurityLayer.stripSlashes(user.getName());
            request.setAttribute("nameLogged", nameLogged);
            request.setAttribute("usernameLogged", "Homepage");
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("role", role);
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
            List<Publication> publicationsInsert = getDataLayer().getLastInsertedPublication(); //Select BY InsertDate
            List<Publication> publicationsUpdate = getDataLayer().getLastModifiedPublication(); //Select BY LastUpdate
            List<User> activeUsers = getDataLayer().getMoreActiveUsers(); //Select BY Max publication insert
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
