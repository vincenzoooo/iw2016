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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.univaq.iw.bibliomanager.data.model.User;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class Login extends BiblioManagerBaseController {

    private void action_login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, NoSuchAlgorithmException, DataLayerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            String email = Utils.checkString(request.getParameter("email"));
            String password = Utils.checkString(request.getParameter("password"));
            if (!this.validator(request, response)) {
                String passEncrypted = Utils.SHA1(password);
                User user = getDataLayer().getUser(email, passEncrypted);
                if (user != null) {
                    SecurityLayer.createSession(request, email, user.getKey(), user.getState());
                    request.setAttribute("page_title", "Benvenuto");
                    request.setAttribute("user", user);
                    response.sendRedirect("home");
                    //res.activate("home.ftl.html", request, response);//TODO: Definire la home page
                } else {
                    request.setAttribute("message", "Credenziali errate, si invita a riprovare o ad iscriversi");
                    //res.activate("login.ftl.html", request, response);
                    res.activate("registration.ftl.html", request, response);
                }
            } else {
                res.activate("login.ftl.html", request, response);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel trovare l'utente: " + ex.getMessage());
        }
    }

    private boolean validator(HttpServletRequest request, HttpServletResponse response) {
        boolean error = false;
        String email = Utils.checkString(request.getParameter("email"));
        String password = Utils.checkString(request.getParameter("password"));
        if (email == null) {
            request.setAttribute("errorEmail", "Email non valorizzato");
            error = true;
        }
        if (password == null) {
            request.setAttribute("errorPassword", "Password non valorizzato");
            error = true;
        }
        if (!Utils.checkEmail(email)) {
            request.setAttribute("errorEmail", "L'Email non è nel formato corretto");
            error = true;
        }
        return error;
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
            if (request.getParameter("submitLogin") != null && SecurityLayer.checkSession(request) == null) {
                action_login(request, response);
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
        return "Servlet di gestione del login";
    }
}
