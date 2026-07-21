package com.viewfunction.docg.views.about;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;
import com.viewfunction.docg.views.MainLayout;

//@PageTitle("数海云图 - 关于 [ About ]")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends Div implements HasDynamicTitle {

    public AboutView() {
        addClassName("about-view");
        add(new Text("Content placeholder"));
    }

    private final String SYSTEM_TITLE_PREFIX = SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.SYSTEM_TITLE_PREFIX);
    @Override
    public String getPageTitle() {
        if(SYSTEM_TITLE_PREFIX != null){
            return SYSTEM_TITLE_PREFIX+" - 关于 [ About ]";
        }else{
            return "数海云图 - 关于 [ About ]";
        }
    }
}
