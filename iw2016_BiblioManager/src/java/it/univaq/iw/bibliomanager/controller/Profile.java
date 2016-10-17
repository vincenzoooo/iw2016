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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class Profile extends BiblioManagerBaseController {

    private void action_view(HttpServletRequest request, HttpServletResponse response) throws DataLayerException, ServletException, IOException {
        try {
            request.setAttribute("page_title", "Profile");
            HttpSession session = SecurityLayer.checkSession(request);
            int userKey;
            if (request.getParameter("userId") != null) {
                userKey = Integer.parseInt(request.getParameter("userId"));
                request.setAttribute("displaynone", "display:none");
            } else {
                userKey = (int) session.getAttribute("userId");
            }
            User user = getDataLayer().getUser(userKey);
            List<Publication> publications = new ArrayList();
            List<History> histories = getDataLayer().getHistoriesByUser(user.getKey());
            for (History history : histories) {
                //Type 0 == Insert
                if (history.getType() == 0) {
                    publications.add(getDataLayer().getPublication(history.getPublicationKey()));
                }
                history.setPublicationTitle(getDataLayer().getPublication(history.getPublicationKey()).getTitle());
            }
            if (!publications.isEmpty()) {
                request.setAttribute("publications", publications);
            }
            request.setAttribute("user", user);
            request.setAttribute("userActions", histories);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("profile.ftl.html", request, response);
        } catch (DataLayerException | ServletException ex) {
            action_error(request, response, "Error build the template: " + ex.getMessage(), 511);
        }
    }

    private void action_updateProfile(HttpServletRequest request, HttpServletResponse response) throws DataLayerException, ServletException, IOException{
        HttpSession session = SecurityLayer.checkSession(request);
        User user = getDataLayer().getUser((int) session.getAttribute("userId"));
        request.setAttribute("user", user);
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("manageProfile.ftl.html", request, response);
    }
    private void action_save(HttpServletRequest request, HttpServletResponse response) throws DataLayerException, ServletException, IOException, NoSuchAlgorithmException {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            int userKey = (int) session.getAttribute("userId");
            User user = getDataLayer().getUser(userKey);
            String password = Utils.checkString(request.getParameter("password"));
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", Utils.checkString(request.getParameter("name")));
            params.put("surname", Utils.checkString(request.getParameter("surname")));
            params.put("email", Utils.checkString(request.getParameter("email")));
            if (!validator(params, request, response)) {
                user.setName(params.get("name"));
                user.setSurname(params.get("surname"));
                user.setEmail(params.get("email"));
                if(!Utils.isNullOrEmpty(password)){
                    password = Utils.SHA1(password);
                    user.setPassword(password);
                }
                getDataLayer().storeUser(user);
                request.setAttribute("userUpdated", 1);
                request.setAttribute("user", user);
            } else {
                action_updateProfile(request, response);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 510);
        }
    }

    @Override
    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        boolean error = super.validator(params, request, response);
        if (!error) {
            String password = Utils.checkString(request.getParameter("password"));
            String repassword = Utils.checkString(request.getParameter("re-password"));
            if(password != null && repassword == null){
                request.setAttribute("errorPassword", "Ripetere la password");
                error = true;
            }
            if(password == null && repassword != null){
                request.setAttribute("errorPassword", "Inserire password valida");
                error = true;
            }
            if(password != null && repassword != null && !password.equals(repassword)){
                request.setAttribute("errorPassword", "Le password non corrispondono");
                error = true;
            }
            if(!Utils.checkEmail(params.get("email"))){
                request.setAttribute("errorEmail", "email non valida");
                error = true;
            }
        }
        return error;
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if (request.getParameter("submitData") != null) {
                    action_save(request, response);
                }
                if(request.getParameter("update") != null){
                    action_updateProfile(request, response);
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | NoSuchAlgorithmException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 501);
        }
    }

}
