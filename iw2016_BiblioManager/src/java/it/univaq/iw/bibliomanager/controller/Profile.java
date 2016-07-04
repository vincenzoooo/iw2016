/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.History;
import it.univaq.iw.bibliomanager.data.model.Publication;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.univaq.iw.bibliomanager.data.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class Profile extends BiblioManagerBaseController {

    private void action_profile(HttpServletRequest request, HttpServletResponse response) throws DataLayerException, ServletException, IOException {
        try{
            HttpSession session = SecurityLayer.checkSession(request);
            int userKey = 0;
            if(request.getParameter("userkey") != null){
                userKey = Integer.parseInt(request.getParameter("userkey"));
                //TODO: Gestire la modifica in caso non si è il proprietario del profilo
                request.setAttribute("displaynone", "display:none");
            }
            else{
                userKey = (int) session.getAttribute("userid");
            }
            User user = getDataLayer().getUser(userKey);
            List<Publication> publications = new ArrayList();
            List<History> histories = getDataLayer().getHistories(user);
            for (History history : histories) {
                //Type 0 == Insert
                if(history.getType() == 0){
                    publications.add(history.getPublication());
                }
            }
            if(!publications.isEmpty()){
                request.setAttribute("publications", publications);
            }
            request.setAttribute("user", user);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("profile.ftl.html", request, response);
        }
        catch(DataLayerException ex){
            action_error(request, response, "Error: " + ex.getMessage());
        }
    }

    private void action_save(HttpServletRequest request, HttpServletResponse response) throws DataLayerException, ServletException, IOException, NoSuchAlgorithmException {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            int userKey =  (int) session.getAttribute("userid");
            User user = getDataLayer().getUser(userKey);
            String password = Utils.SHA1(request.getParameter("password"));
            if (!password.equals(user.getPassword())) {
                user.setPassword(password);
            }
            getDataLayer().storeUser(user);
            request.setAttribute("user", user);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("profile.ftl.html", request, response);
        } catch (DataLayerException ex) {
            action_error(request, response, "Error: " + ex.getMessage());
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("page_title", "Login to Biblio");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("login.ftl.html", request, response);
    }

    private boolean validator(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException{
        boolean error = false;
        String name = Utils.checkString(request.getParameter("name"));
        String surname = Utils.checkString(request.getParameter("surname"));
        String password = Utils.encryptPassword(request.getParameter("password"));
        String newPassword = Utils.encryptPassword(request.getParameter("newpassword"));
        if (name == null) {
            request.setAttribute("errorName", "Nome non valorizzato");
            error = true;
        }
        if (surname == null) {
            request.setAttribute("errorSurname", "Cognome non valorizzato");
            error = true;
        }
        if (password == null) {
            request.setAttribute("errorPassword", "Password o Repassword non valorizzato");
            error = true;
        } 
        if (newPassword == null) {
            request.setAttribute("errorPassword", "Newpassword non valorizzato");
            error = true;
        } 
        return error;
    }
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            request.setAttribute("page_title", "Profile");
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("submitRegistration") != null) {
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
