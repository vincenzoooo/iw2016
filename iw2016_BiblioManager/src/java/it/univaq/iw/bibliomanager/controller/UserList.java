/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.univaq.iw.bibliomanager.data.model.User;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class UserList extends BiblioManagerBaseController {

    private void action_list(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException, IOException, ServletException {
        List<User> users = getDataLayer().getUsers();
        request.setAttribute("users", users);
        request.setAttribute("page_title", "Users Manage");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("users.ftl.html", request, response);
    }

    private void action_upgrade(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                User active = getDataLayer().getUser((int) session.getAttribute("userid"));
                if (active != null && active.getState() < 2 && request.getParameter("user-selected") != null) {
                    User user = getDataLayer().getUser(request.getParameter("user-selected"));
                    if (user.getState() == 2) {
                        user.setState(1);
                    }
                }
            }
        } catch (Exception ex) {
            throw new Exception("Non puoi promuovere questo utente", ex);
        }
    }

    private void action_downgrade(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                User admin = getDataLayer().getUser((int) session.getAttribute("userid"));
                if (admin != null && admin.getState() == 0 && request.getParameter("user-selected") != null) {
                    User user = getDataLayer().getUser(request.getParameter("user-selected"));
                    if (user.getState() == 1) {
                        user.setState(2);
                    }
                }
            }
        } catch (Exception ex) {
            throw new Exception("Non puoi degradare questo utente", ex);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                User user = getDataLayer().getUser((int) session.getAttribute("userid"));
                if (request.getParameter("op") == null) {
                    action_list(request, response);
                }
                if (user.getState() != 2 && Integer.parseInt(request.getParameter("op")) == 1) {
                    action_upgrade(request, response);
                } else {
                    action_downgrade(request, response);
                }
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, ex.getMessage());
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
