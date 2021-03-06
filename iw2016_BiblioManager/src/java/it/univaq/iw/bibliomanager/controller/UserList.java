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
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class UserList extends BiblioManagerBaseController {

    /**
     * Notify messages
     */
    private final String userUpgradeMessage = "Utente <a href=\"profile?userId=${userUpgraded.key}\" class=\"alert-link\">%s %s</a> promosso con successo.";
    private final String userDowngradeMessage = "Utente <a href=\"profile?userId=${userUpgraded.key}\" class=\"alert-link\">%s %s</a> promosso con successo.";

    /**
     * Compile the template for display it
     *
     * @param request
     * @param response
     */
    private void action_view(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filter = "%";
            if (request.getParameter("filter") != null) {
                filter = request.getParameter("filter") + "%";
            }
            User admin = getDataLayer().getUserAdministrator();
            List<User> activeUsers = getDataLayer().getUsersActive(filter);
            List<User> passiveUsers = getDataLayer().getUsersPassive(filter);
            request.setAttribute("admin", admin);
            request.setAttribute("activeUsers", activeUsers);
            request.setAttribute("passiveUsers", passiveUsers);
            request.setAttribute("page_title", "Users Manage");
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("users.ftl.html", request, response);
        } catch (ServletException | IOException | DataLayerException ex) {
            action_error(request, response, "Error build the template: " + ex.getMessage(), 511);
        }
    }

    /**
     * Promote a passive user to active
     *
     * @param request
     * @param response
     */
    private void action_upgrade(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                User active = getDataLayer().getUser((int) session.getAttribute("userId"));
                if (active != null && active.getState() < 2 && request.getParameter("user") != null) {
                    User user = getDataLayer().getUser(Integer.parseInt(request.getParameter("user")));
                    if (user.getState() == 2) {
                        user.setState(1);
                        user.setDirty(true);
                        getDataLayer().storeUser(user);
                        action_createNotifyMessage(request, response, SUCCESS, String.format(userUpgradeMessage, user.getName(), user.getSurname()), true);
                    }
                }
            }
        } catch (DataLayerException | NumberFormatException ex) {
            action_error(request, response, "Error while upgrading an user: " + ex.getMessage(), 510);
        }
    }

    /**
     * Downgradee an active user to passive
     *
     * @param request
     * @param response
     */
    private void action_downgrade(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                User admin = getDataLayer().getUser((int) session.getAttribute("userId"));
                if (admin != null && admin.getState() == 0 && request.getParameter("user") != null) {
                    User user = getDataLayer().getUser(Integer.parseInt(request.getParameter("user")));
                    if (user.getState() == 1) {
                        user.setState(2);
                        user.setDirty(true);
                        getDataLayer().storeUser(user);
                        action_createNotifyMessage(request, response, ERROR, String.format(userDowngradeMessage, user.getName(), user.getSurname()), true);
                    }
                }
            }
        } catch (DataLayerException | NumberFormatException ex) {
            action_error(request, response, "Error while downgrading an user: " + ex.getMessage(), 510);
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
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                User user = getDataLayer().getUser((int) session.getAttribute("userId"));
                request.setAttribute("me", user);
                if (user.getState() != 2 && request.getParameter("op") != null) {
                    if (Integer.parseInt(request.getParameter("op")) == 1) {
                        action_upgrade(request, response);
                    } else {
                        action_downgrade(request, response);
                    }
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (IOException | DataLayerException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 501);
        }
    }
}
