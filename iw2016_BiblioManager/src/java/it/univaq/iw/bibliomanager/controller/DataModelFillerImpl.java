/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.iw.bibliomanager.controller;

/**
 *
 * @author Angelo Iezzi
 */
import it.univaq.iw.framework.result.DataModelFiller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModelFillerImpl implements DataModelFiller {

    public void fillDataModel(Map datamodel) {
        List<Map> menuitems = new ArrayList();
        datamodel.put("menu", menuitems);

        //un bean sarebbe più appropriato
        //a bean would be more appropriate
        Map menuitem = new HashMap();
        menuitem.put("label", "Home");
        menuitem.put("link", "home");
        menuitems.add(menuitem);

        menuitem = new HashMap();
        menuitem.put("label", "Catalogo");
        menuitem.put("link", "catalog");
        menuitems.add(menuitem);

        menuitem = new HashMap();
        menuitem.put("label", "Profilo");
        menuitem.put("link", "profile");
        menuitems.add(menuitem);

        menuitem = new HashMap();
        menuitem.put("label", "Community");
        menuitem.put("link", "community");
        menuitems.add(menuitem);
    }

}
