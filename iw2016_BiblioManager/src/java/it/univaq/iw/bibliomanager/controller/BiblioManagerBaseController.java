package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.impl.BiblioManagerDataLayerMysqlImpl;
import it.univaq.iw.bibliomanager.data.model.BiblioManagerDataLayer;
import it.univaq.iw.bibliomanager.data.model.User;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.FailureResult;
import it.univaq.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi
 */
public abstract class BiblioManagerBaseController extends HttpServlet {
    
    protected final String SUCCESS = "success";
    protected final String WARNING = "warning";
    protected final String ERROR = "error";
    
    private BiblioManagerDataLayer datalayer;
    
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            datalayer = new BiblioManagerDataLayerMysqlImpl((DataSource) getServletContext().getAttribute("datasource"));
            datalayer.init();
            HttpSession s = SecurityLayer.checkSession(request);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            //SecurityLayer.redirectToHttps(request, response);

            //WARNING: never declare DB-related objects including references to Connection and Statement (as our data layer)
            //as class variables of a servlet. Since servlet instances are reused, concurrent requests may conflict on such
            //variables leading to unexpected results. To always have different connections and statements on a per-request
            //(i.e., per-thread) basis, declare them in the doGet, doPost etc. (or in methods called by them) and 
            //(possibly) pass such variables through the request.
            request.setAttribute("datalayer", datalayer);
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace(); //for debugging only
            (new FailureResult(getServletContext())).activate(
                    500, (ex.getMessage() != null || ex.getCause() == null) ? ex.getMessage() : ex.getCause().getMessage(), request, response);
        } finally {
            if (datalayer != null) {
                try {
                    datalayer.destroy();
                    datalayer = null;
                } catch (DataLayerException ex) {
                    Logger.getLogger(BiblioManagerBaseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Get the logged user from the SESSION and set it in the request
     * @param request 
     * @param response
     * @param session 
     */
    protected void currentUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            int userId = (int) session.getAttribute("userId");
            User user = getDataLayer().getUser(userId);
            request.setAttribute("me", user);
        } catch (DataLayerException ex) {
            action_error(request, response, ex.getMessage(), 500);
        }
    }

    /**
     * Set error message and code for to display it
     * @param request
     * @param response
     * @param message
     * @param code 
     */
    protected void action_error(HttpServletRequest request, HttpServletResponse response, String message, int code) {
        (new FailureResult(getServletContext())).activate(code,message, request, response);
    }

    /**
     * Default action of the site
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws DataLayerException 
     */
    protected void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Login to Biblio");
        getDataLayer().deleteIncompletePublication();
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("login.ftl.html", request, response);
    }

    /**
     * Redirect to the specified url
     * @param request
     * @param response
     * @param url
     * @throws ServletException
     * @throws IOException 
     */
    protected void action_redirect(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

    /**
     * Verify if the passed params are empty, in that case return an error
     * @param params
     * @param request
     * @param response
     * @return 
     */
    protected boolean validator(Map<String, String> params, HttpServletRequest request, HttpServletResponse response){
        boolean error = false;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null || entry.getValue().equals("")) {
                String fieldName = Character.toUpperCase(entry.getKey().charAt(0)) + entry.getKey().substring(1);
                request.setAttribute("error" + fieldName, "Il campo non può essere vuoto");
                error = true;
            }
            else{
                noAction(entry.getValue(), request, response);
                entry.setValue(SecurityLayer.addSlashes(entry.getValue()));
            }
        }
        return error;
    }

    /**
     * Secure all the query from SQL injection
     * @param param
     * @param request
     * @param response 
     */
    protected void noAction(String param, HttpServletRequest request, HttpServletResponse response){
        if((param.contains("SELECT")&&param.contains("FROM"))||param.contains("DELETE FROM")||param.contains("INSERT INTO")||(param.contains("UPDATE")&&param.contains("SET"))||param.contains("CREATE TABLE")){
            try {
                request.setAttribute("noOperation", 1);
                action_redirect(request, response, "/logout");
            } catch (ServletException | IOException ex) {
                action_error(request, response, ex.getMessage(), 500);
            }
        }
    }
    
    /**
     * Paginate function
     * @param request
     * @param response
     * @param pages
     * @param options 
     */
    protected void pagination(HttpServletRequest request, HttpServletResponse response, Map<Integer, String> pages, Map<String, Integer> options){
        int totElements = (int)request.getAttribute("totElements");
        int pageNumber = totElements / options.get("limit");
        int totOffset = 0;
        int page = options.get("offset")/options.get("limit");
        if(totElements < options.get("end")){
            options.put("end", totElements);
        }
        if(pageNumber < options.get("slice")){
            options.put("slice", pageNumber+1);
        }
        for (int i = 0; i <= pageNumber; i++){
            String editorUrl = request.getAttribute("paginationUrl") + "?offset=" + totOffset;
            pages.put(i, editorUrl);
            totOffset += options.get("limit");
        }
        action_pagination_next(options, pageNumber);
        action_pagination_previous(options, pageNumber);
        action_pagination_first(options);
        action_pagination_last(options, pageNumber);

        request.setAttribute("pages", getSlice(pages, options.get("start"), options.get("end")).entrySet());
        request.setAttribute("first", pages.get(0));
        request.setAttribute("last", pages.get(pages.size()-1));
        
        if(page > 0){
            request.setAttribute("previous", pages.get(page-1));
        }
        if(page < pageNumber){
            request.setAttribute("next", pages.get(page+1));
        }
        request.setAttribute("curr", page);
    }
    
    /**
     * Get only a portion of all calculated pages
     * @param map
     * @param start Start page of the pagination
     * @param end End page of the pagination
     * @return 
     */
    protected Map<Integer, String> getSlice(Map<Integer, String> map, int start, int end){
        Map<Integer, String> slice = new HashMap<>();
        if(map.size()>1){
            for (int i = start; i < end; i++) {
                slice.put(i, map.get(i));
            }
        }
        return slice;
    }
    
    /**
     * Recalc the pagination in case of return to the first page
     * @param options 
     */
    protected void action_pagination_first(Map<String, Integer> options){
        int page = options.get("offset")/options.get("limit");
        if(page == 0){
            options.put("start", 0);
            options.put("end", options.get("slice"));
        }
    }
    
    /**
     * Recalc the pagination in case of go to the last page
     * @param options
     * @param pageNumber 
     */
    protected void action_pagination_last(Map<String, Integer> options, int pageNumber){
        int page = options.get("offset")/options.get("limit");
        if(page == pageNumber){
            int start = pageNumber-options.get("slice");
            if(start < 0){
                start = 0;
            }
            options.put("start", start);
            //TODO: da rivedere se invece serve pageNumber
            options.put("end", options.get("slice"));
        }
    }
    
    /**
     * Recalc the pagination in case of go to next page
     * @param options
     * @param pageNumber 
     */
    protected void action_pagination_next(Map<String, Integer> options,  int pageNumber){
        int offset = options.get("offset");
        int limit = options.get("limit");
        int slice = options.get("slice");
        int start = options.get("start");
        int end = options.get("end");
        int page = offset/limit;
        int step = page+1 - (slice/2+1);
        if(page+1 > (slice/2+1) && end != pageNumber){
            if(step > 1){
                if(end + step > pageNumber){
                    start = pageNumber - slice;
                    end = pageNumber;
                }
                else{
                    start = step;
                    end = slice + step;
                }
            }
            else{
                start++;
                end++;
            }
        }
        options.put("start", start);
        options.put("end", end);
    }

    /**
     * Recalc the pagination in case of go to previous page
     * @param options
     * @param pageNumber 
     */
    protected void action_pagination_previous(Map<String, Integer> options, int pageNumber){
        int offset = options.get("offset");
        int limit = options.get("limit");
        int slice = options.get("slice");
        int start = options.get("start");
        int end = options.get("end");  
        int page = offset/limit;
        int step = (slice/2+1) - (page+1);
        if(page+1 < (pageNumber-(slice/2+1)) && end != slice){
            if(step > 1){
                if(start + step < slice){
                    start = 0;
                    end = slice;
                }
                else{
                    start = step;
                    end = slice + step;
                }
            }
            else{
                start--;
                end--;
            }
        }
        options.put("start", start);
        options.put("end", end);
    }
    
    /**
     * Create notify message to show
     * @param request
     * @param response
     * @param notifyType
     * @param notifyMessage 
     */
    protected void action_createNotifyMessage(HttpServletRequest request, HttpServletResponse response, String notifyType, String notifyMessage, boolean margin){
        switch(notifyType){
            case SUCCESS:
                request.setAttribute("notifyClass", "alert-success");
                break;
            case ERROR:
                request.setAttribute("notifyClass", "alert-danger");
                break;
            case WARNING:
                request.setAttribute("notifyClass", "alert-warning");
                break;
            default:
                return;
        }
        request.setAttribute("haveNotify", true);
        request.setAttribute("notifyMessage", notifyMessage);
        if(margin){
            request.setAttribute("notifyMargin", "margin-top-20");
        }
    }
    /**
     * Get the data layer
     * @return 
     */
    public BiblioManagerDataLayer getDataLayer() {
        return datalayer;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

}
