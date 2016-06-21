/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Utente;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class Register extends BiblioManagerBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("page_title", "Register to Biblio");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("register.ftl.html", request, response);
    }

    private void action_register(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DataLayerException, NoSuchAlgorithmException, MessagingException {
        TemplateResult res = new TemplateResult(getServletContext());
        String name = Utils.checkString(request.getParameter("name"));
        String surname = Utils.checkString(request.getParameter("surname"));
        String password = Utils.checkString(request.getParameter("password"));
        String email = Utils.checkString(request.getParameter("email"));
        if (!Utils.checkEmail(email)) {
            //TODO: invio degli errori indietro alla pagina
            throw new IOException("Input error: [" + email + "] is not a correct email"); //TODO: Brutale da cambiare
        } else {
            Utente newUser = getDataLayer().createUser();
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setPassword(Utils.encryptPassword(password));
            newUser.setEmail(email);
            getDataLayer().storeUser(newUser);
            String text = "Benvenuto su BiblioManager!";
            Utils.sendEmail(email, text);
            res.activate("index.html", request, response);
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
            if (request.getParameterNames() != null && request.getParameter("name") != null && request.getParameter("surname") != null && request.getParameter("password") != null && request.getParameter("email") != null) {
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
