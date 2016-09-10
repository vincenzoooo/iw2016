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
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.univaq.iw.bibliomanager.data.model.User;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class Reset extends BiblioManagerBaseController {

    private void action_reset(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DataLayerException, NoSuchAlgorithmException {
        HttpSession session = SecurityLayer.checkSession(request);
        if (request.getAttribute("rs") == session.getAttribute("rs")) {
            TemplateResult res = new TemplateResult(getServletContext());
            String newPassword = Utils.checkString(request.getParameter("new-password"));
            User user = getDataLayer().getUser((int) session.getAttribute("userID"));
            if (user != null) {
                user.setPassword(Utils.SHA1(newPassword));
                getDataLayer().storeUser(user);
            }
            res.activate("login.html", request, response); //TODO: redirect to login
        } else {
            throw new ServletException("No reset request found"); //TODO
        }
    }

    private void action_isUser(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException, MessagingException {
        HttpSession session = SecurityLayer.checkSession(request);
        session.setAttribute("rs", (int) (Math.random() * 1000));
        String email = Utils.checkString("email");
        User user = getDataLayer().getUser(email);
        if (user != null) {
            String text = "Here the link to reset your password: http://localhost:8084/bibliomanager/reset?rs=" + session.getAttribute("rs");
            Utils.sendEmail(email, text);
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
            if (request.getParameter("email") != null) {
                action_isUser(request, response);
            } else if (request.getParameter("rs") != null && SecurityLayer.checkSession(request) != null) {
                action_reset(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | MessagingException | NoSuchAlgorithmException | ServletException ex) {
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
