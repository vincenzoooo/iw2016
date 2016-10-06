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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class Login extends BiblioManagerBaseController {

    private void action_login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, NoSuchAlgorithmException, DataLayerException {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", Utils.checkString(request.getParameter("email")));
            params.put("password", Utils.checkString(request.getParameter("password")));
            if (!validator(params, request, response)) {
                String passEncrypted = Utils.SHA1(params.get("password"));
                User user = getDataLayer().getUser(params.get("email"), passEncrypted);
                if (user != null) {
                    SecurityLayer.createSession(request, params.get("email"), user.getKey(), user.getState());
                    request.setAttribute("page_title", "Benvenuto");
                    request.setAttribute("user", user);
                    request.setAttribute("logged", 1);
                    action_redirect(request, response, "/home");
                } else {
                    request.setAttribute("errorLogin", "Credenziali errate, si invita a riprovare o ad iscriversi");
                    action_default(request, response);
                }
            } else {
                
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel trovare l'utente: " + ex.getMessage(), 502);
        }
    }

    @Override
    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        boolean error = super.validator(params, request, response);
        if(!error){
            if (!Utils.checkEmail(params.get("email"))) {
                request.setAttribute("errorEmail", "L'Email non è nel formato corretto");
                error = true;
            }
        }
        return error;
    }

    private void action_view(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("login.ftl.html", request, response);
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
            if (request.getParameter("submitLogin") != null && SecurityLayer.checkSession(request) == null) {
                action_login(request, response);
                action_view(request, response);
                
            } else {
                request.setAttribute("isLogin", 1);
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | NoSuchAlgorithmException | ServletException ex) {
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
        return "Servlet di gestione del login";
    }
}
