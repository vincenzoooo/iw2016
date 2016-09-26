/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.univaq.iw.bibliomanager.data.model.User;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class Register extends BiblioManagerBaseController {

    private void action_register(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", Utils.checkString(request.getParameter("name")));
            params.put("surname", Utils.checkString(request.getParameter("surname")));
            params.put("password", Utils.SHA1(request.getParameter("password")));
            params.put("re-password", Utils.SHA1(request.getParameter("re-password")));
            params.put("email", Utils.checkString(request.getParameter("email")));
            params.put("re-email", Utils.checkString(request.getParameter("re-email")));
            params.put("privacy", Utils.checkString(request.getParameter("privacy")));
            if (!validator(params, request, response)) {
                User newUser = getDataLayer().createUser();
                newUser.setName(params.get("name"));
                newUser.setSurname(params.get("surname"));
                newUser.setPassword(params.get("password"));
                newUser.setEmail(params.get("email"));
                getDataLayer().storeUser(newUser);
                action_redirect(request, response, "/home");
            } else {
                request.setAttribute("name", params.get("name"));
                request.setAttribute("surname", params.get("surname"));
            }
        } catch (NoSuchAlgorithmException | DataLayerException | ServletException ex) {
            action_error(request, response, "Error register: " + ex.getMessage(), 510);
        } catch (UnsupportedEncodingException ex) {
            action_error(request, response, "Error register: " + ex.getMessage(), 501);
        } catch (IOException ex) {
            action_error(request, response, "Error register: " + ex.getMessage(), 501);
        }
    }

    @Override
    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        boolean error = false;
        User user = null;
        try {
            String password = params.get("password");
            String rePassword = params.get("re-password");
            String email = params.get("email");
            String reEmail = params.get("re-email");
            if (params.get("name") == null) {
                request.setAttribute("errorName", "Nome non valorizzato");
                error = true;
            }
            if (params.get("surname") == null) {
                request.setAttribute("errorSurname", "Cognome non valorizzato");
                error = true;
            }

            if (password == null || rePassword == null) {
                request.setAttribute("errorPassword", "Password o Repassword non valorizzato");
                error = true;
            } else if (!password.equals(rePassword)) {
                request.setAttribute("errorPassword", "I campi Password e Repassword non sono uguali");
                error = true;
            }

            if (email == null || reEmail == null) {
                request.setAttribute("errorEmail", "Email non valorizzata");
                error = true;
            } else if (!email.equals(reEmail)) {
                request.setAttribute("errorEmail", "I campi email e Reemail non sono uguali");
                error = true;
            } else {
                user = getDataLayer().getUser(email);
            }
            if (params.get("privacy") == null) {
                request.setAttribute("errorPrivacy", "Bisogna accettare i termini di legge sulla privacy");
                error = true;
            }
            if (!Utils.checkEmail(email)) {
                request.setAttribute("errorEmail", "L'Email passata non è valida");
                error = true;
            }
            if (user != null) {
                request.setAttribute("errorEmail", "Questa email è già registrata");
                error = true;
            }

        } catch (DataLayerException ex) {
            action_error(request, response, "Data layer exception: " + ex.getMessage(), 503);;
        }
        return error;
    }

    @Override
    protected void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("page_title", "Register");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("registration.ftl.html", request, response);
    }

    protected void action_view(HttpServletRequest request, HttpServletResponse response){
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("registration.ftl.html", request, response);
        } catch (ServletException | IOException ex) {
            action_error(request, response, "Error build the template: " + ex.getMessage(), 511);
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
            action_view(request, response);
            if (request.getParameter("submitRegistration") != null) {
                action_register(request, response);
            } else {
                action_default(request, response);
            }
        } catch (IOException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 501);
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
