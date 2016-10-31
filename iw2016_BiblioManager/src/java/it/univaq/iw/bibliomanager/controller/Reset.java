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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.univaq.iw.bibliomanager.data.model.User;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public class Reset extends BiblioManagerBaseController {

    /**
     * User
     */
    private User user = null;

    /**
     * Verify and update a User password
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @throws DataLayerException
     * @throws NoSuchAlgorithmException
     */
    private void action_reset(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, DataLayerException, NoSuchAlgorithmException {
        String newPassword = Utils.checkString(request.getParameter("password"));
        String newRePassword = Utils.checkString(request.getParameter("re-password"));
        if (newPassword.equals(newRePassword) && user != null) {
            user.setPassword(Utils.SHA1(newPassword));
            user.setDirty(true);
            getDataLayer().storeUser(user);
            request.setAttribute("resetted", 1);
            action_redirect(request, response, "/login");
        } else {
            request.setAttribute("errorReset", "Le password non corrispondono. Reinserire le password.");
            action_default(request, response);
        }
    }

    /**
     * Verify if the passed email correspond to a registered User
     *
     * @param request
     * @param response
     * @throws DataLayerException
     * @throws ServletException
     * @throws IOException
     */
    private void action_isUser(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException, ServletException, IOException {
        String email = Utils.checkString(request.getParameter("email"));
        User registeredUser = getDataLayer().getUser(email);
        if (registeredUser != null) {
            this.user = registeredUser;
        } else {
            request.setAttribute("errorReset", "L'email non è stata registrata. Immetere una e-mail valita.");
        }
        action_default(request, response);
    }

    /**
     * Compile the template for display it
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws DataLayerException
     */
    @Override
    protected void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Reset");
        TemplateResult res = new TemplateResult(getServletContext());
        if (user != null) {
            request.setAttribute("showPass", "1");
        }
        res.activate("reset.ftl.html", request, response);
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
            if (request.getParameter("submitEmailReset") != null) {
                action_isUser(request, response);
            } else if (request.getParameter("submitPassReset") != null) {
                action_reset(request, response);
            } else {
                user = null;
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | NoSuchAlgorithmException | ServletException ex) {
            action_error(request, response, "Error: " + ex.getMessage(), 501);
        }
    }
}
