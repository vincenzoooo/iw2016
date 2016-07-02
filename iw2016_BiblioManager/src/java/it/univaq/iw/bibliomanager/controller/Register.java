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

/**
 *
 * @author Vincenzo Lanzieri
 */
public class Register extends BiblioManagerBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("page_title", "Register to Biblio");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("registration.ftl.html", request, response);
    }

    private void action_register(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DataLayerException, NoSuchAlgorithmException, MessagingException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            String name = Utils.checkString(request.getParameter("name"));
            String surname = Utils.checkString(request.getParameter("surname"));
            String password = Utils.checkString(request.getParameter("password"));
            String email = Utils.checkString(request.getParameter("email"));
            if (!this.validator(request, response)) {
                User newUser = getDataLayer().createUser();
                newUser.setName(name);
                newUser.setSurname(surname);
                newUser.setPassword(Utils.encryptPassword(password));
                newUser.setEmail(email);
                getDataLayer().storeUser(newUser);
                //TODO: Controllare l'invio email
                //String text = "Benvenuto su BiblioManager!";
                //Utils.sendEmail(email, text);
                res.activate("index.html", request, response);
            } else {
                request.setAttribute("name", name);
                request.setAttribute("surname", surname);
                res.activate("registration.ftl.html", request, response);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Error: " + ex.getMessage());
        }
    }

    private boolean validator(HttpServletRequest request, HttpServletResponse response) {
        boolean error = false;
        String name = Utils.checkString(request.getParameter("name"));
        String surname = Utils.checkString(request.getParameter("surname"));
        String password = Utils.checkString(request.getParameter("password"));
        String rePassword = Utils.checkString(request.getParameter("re-password"));
        String email = Utils.checkString(request.getParameter("email"));
        String reEmail = Utils.checkString(request.getParameter("re-email"));
        String privacy = Utils.checkString(request.getParameter("privacy"));
        if (name == null) {
            request.setAttribute("errorName", "Nome non valorizzato");
            error = true;
        }
        if (surname == null) {
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
        }
        if (privacy == null) {
            request.setAttribute("errorPrivacy", "Bisogna accettare i termini di legge sulla privacy");
            error = true;
        }
        if (!Utils.checkEmail(email)) {
            request.setAttribute("errorEmail", "L'Email passata non è valida");
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
            if (request.getParameter("submitRegistration") != null) {
                action_register(request, response);
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
