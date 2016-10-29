/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.bibliomanager.controller;

import it.univaq.iw.bibliomanager.data.model.Source;
import it.univaq.iw.framework.data.DataLayerException;
import it.univaq.iw.framework.result.TemplateResult;
import it.univaq.iw.framework.security.SecurityLayer;
import it.univaq.iw.framework.utils.Utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vincenzo Lanzieri, Angelo Iezzi;
 */
public class ComposeSource extends BiblioManagerBaseController {

    private final String updateMessage = "Modifica avvenuta con successo.";
    private final String addMessage = "Inserimento avvenuto con successo.";
    private final String deleteMessage = "Risorsa eliminata con successo.";
    /**
     * Pubblicazione
     */
    private int publicationId;
    /**
     * Url da cui proviene la richiesta
     */
    private String url;
    /**
     * Sorgente
     */
    private int sourceId;
    /**
     * Dati della risorsa corrente che si vuole aggiornare
     */
    private Source currentSource;
    /**
     * Pagine
     */
    private final Map<Integer, String> pages = new HashMap<>();
    /**
     * Opzioni per la paginazione
     */
    private final Map<String, Integer> options = new HashMap<>();
    
    /**
     * Verifica e salva una nuova risorsa
     * @param request
     * @param response
     * @throws DataLayerException 
     */
    private void action_composeSource(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            Source source = getDataLayer().createSource();
            boolean isCover = false;
            if(request.getParameter("sourceCover") != null){
                isCover = true;
            }
            boolean isDownload = false;
            if(request.getParameter("sourceDownload") != null){
                isDownload = true;
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("sourceType", Utils.checkString(request.getParameter("sourceType")));
            params.put("sourceUri", Utils.checkString(request.getParameter("sourceUri")));
            params.put("sourceFormat", Utils.checkString(request.getParameter("sourceFormat")));
            params.put("sourceDescription", Utils.checkString(request.getParameter("sourceDescription")));
            if (!validator(params, request, response)) {
                source.setType(params.get("sourceType"));
                source.setUri(params.get("sourceUri"));
                source.setFormat(params.get("sourceFormat"));
                source.setDescription(params.get("sourceDescription"));
                source.setCover(isCover);
                source.setDownload(isDownload);
                source.setPublicationKey(publicationId);
                getDataLayer().storeSource(source);
                action_createNotifyMessage(request, response, SUCCESS, addMessage, true);
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage(), 510);
        }
    }
/**
 * Verifica e salva le modifiche di una risorsa
 * @param request
 * @param response
 * @throws DataLayerException 
 */
    private void action_updateSource(HttpServletRequest request, HttpServletResponse response) throws DataLayerException {
        try {
            boolean isCover = false;
            if(request.getParameter("sourceCover") != null){
                isCover = true;
            }
            boolean isDownload = false;
            if(request.getParameter("sourceDownload") != null){
                isDownload = true;
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("updateSourceType", Utils.checkString(request.getParameter("sourceType")));
            params.put("updateSourceUri", Utils.checkString(request.getParameter("sourceUri")));
            params.put("updateSourceFormat", Utils.checkString(request.getParameter("sourceFormat")));
            params.put("updateSourceDescription", Utils.checkString(request.getParameter("sourceDescription")));
            if (!validator(params, request, response)) {
                if(!currentSource.getType().equals(params.get("updateSourceType"))){
                    currentSource.setType(params.get("updateSourceType"));
                    currentSource.setDirty(true);
                }
                if(!currentSource.getUri().equals(params.get("updateSourceUri"))){
                    currentSource.setUri(params.get("updateSourceUri"));
                    currentSource.setDirty(true);
                }
                if(!currentSource.getFormat().equals(params.get("updateSourceFormat"))){
                    currentSource.setFormat(params.get("updateSourceFormat"));
                    currentSource.setDirty(true);
                }
                if(!currentSource.getDescription().equals(params.get("updateSourceDescription"))){
                    currentSource.setDescription(params.get("updateSourceDescription"));
                    currentSource.setDirty(true);
                }
                if(currentSource.getCover() != isCover){
                    currentSource.setCover(isCover);
                    currentSource.setDirty(true);
                }
                if(currentSource.getDownload() != isDownload){
                    currentSource.setDownload(isDownload);
                    currentSource.setDirty(true);
                }
                if(publicationId != currentSource.getPublicationKey()){
                    currentSource.setPublicationKey(publicationId);
                    currentSource.setDirty(true);
                }
                getDataLayer().storeSource(currentSource);
                action_createNotifyMessage(request, response, SUCCESS, updateMessage, true);
                sourceId = 0;
            }
        } catch (DataLayerException ex) {
            action_error(request, response, "Errore nel salvare la risorsa: " + ex.getMessage(), 510);
        }
    }
    /**
     * Delete the specified Source
     *
     * @param request
     * @param response
     * @throws DataLayerException
     */
    private void action_deleteSource(HttpServletRequest request, HttpServletResponse response)
            throws DataLayerException {
        Source source = getDataLayer().getSource((int) request.getAttribute("sourceToDelete"));
        getDataLayer().deleteSource(source);
        action_createNotifyMessage(request, response, SUCCESS, deleteMessage, true);
    }
/**
 * Compila i template da restituire a video
 * @param request
 * @param response
 * @throws ServletException
 * @throws IOException
 * @throws DataLayerException 
 */
    private void action_view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataLayerException {
        request.setAttribute("page_title", "Gestione Sorgenti");
        TemplateResult res = new TemplateResult(getServletContext());
        List<Source> sources = getDataLayer().getPublicationSources(publicationId,options.get("limit"), options.get("offset"));
        
        request.setAttribute("totElements", getDataLayer().getPublicationSources(publicationId, 0, 0).size());
        request.setAttribute("paginationUrl", "reprint");
        pagination(request, response, pages, options);
        
        request.setAttribute("sources", sources);
        request.setAttribute("publicationId", publicationId);
        request.setAttribute("url", url);
        request.setAttribute("sourceId", sourceId);
        if(sourceId != 0){
            request.setAttribute("currentDescriptionSource", currentSource.getDescription());
            request.setAttribute("currentFormatSource", currentSource.getFormat());
            request.setAttribute("currentTypeSource", currentSource.getType());
            request.setAttribute("currentUriSource", currentSource.getUri());
            request.setAttribute("currentCoverSource", currentSource.getCover());
            request.setAttribute("currentDownloadSource", currentSource.getDownload());
        }
        res.activate("source.ftl.html", request, response);
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
            HttpSession session = SecurityLayer.checkSession(request);
            if (session != null) {
                currentUser(request, response, session);
                if(request.getAttribute("publicationId") != null){
                    publicationId = (int) request.getAttribute("publicationId");
                }
                if(request.getAttribute("url") != null){
                    url = (String) request.getAttribute("url");
                }
                if (sourceId == 0 && request.getParameter("sourceId") != null) {
                    sourceId = Integer.parseInt(request.getParameter("sourceId"));
                    currentSource = getDataLayer().getSource(sourceId);
                }
                if (request.getParameter("sourceToDelete") != null) {
                    request.setAttribute("sourceToDelete", Integer.parseInt(request.getParameter("sourceToDelete")));
                    action_deleteSource(request, response);
                }
                if (request.getParameter("submitSource") != null){
                    if(sourceId == 0) {
                        action_composeSource(request, response);
                    }
                    else{
                        action_updateSource(request, response);
                    }
                }
                if (request.getParameter("offset") != null){
                   options.put("offset", Integer.parseInt(request.getParameter("offset")));
                }
                else{
                   pages.clear();
                   options.put("limit", 10);
                   options.put("offset", 0);
                   options.put("slice", 10);
                   options.put("start", 0);
                   options.put("end", 10);
                }
                action_view(request, response);
            } else {
                action_default(request, response);
            }
        } catch (DataLayerException | IOException | ServletException ex) {
            action_error(request, response, "Errore: " + ex.getMessage(), 501);
        }
    }
}
