/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.impl.UtenteImpl;
import it.univaq.iw.bibliomanager.data.model.Utente;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class Profile extends BiblioManagerBaseController {

    private void action_profile(HttpServletRequest request, HttpServletResponse response) throws DataLayerException, ServletException, IOException {
        HttpSession session = SecurityLayer.checkSession(request);
        int userKey = (int) session.getAttribute("userid");
        Utente user = getDataLayer().getUser(userKey);
        request.setAttribute("user", user);
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("profile.ftl.html", request, response);
    }

    private void action_save(HttpServletRequest request, HttpServletResponse response) throws DataLayerException, ServletException, IOException, NoSuchAlgorithmException {
        HttpSession session = SecurityLayer.checkSession(request);
        int userKey = (int) session.getAttribute("userid");
        Utente user = getDataLayer().getUser(userKey);
        String password = Utils.encryptPassword(request.getParameter("password"));
        if (!password.equals(user.getPassword())) {
            user.setPassword(password);
        }
        getDataLayer().storeUser(user);
        request.setAttribute("user", user);
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("profile.ftl.html", request, response);
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("page_title", "Login to Biblio");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("login.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            request.setAttribute("page_title", "Profile");
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("dirty") != null && request.getParameter("password") != null) {
                    action_save(request, response);
                }
                action_profile(request, response);
            } else {
                action_default(request, response);
            }
        } catch (Exception ex) {
            action_error(request, response, "OPS");
        }
    }

}
