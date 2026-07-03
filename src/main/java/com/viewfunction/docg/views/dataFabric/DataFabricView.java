package com.viewfunction.docg.views.dataFabric;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;
import com.viewfunction.docg.views.MainLayout;

//@PageTitle("数海云图 - 数据编织 [ Data Fabric ]")
@Route(value = "data-fabric", layout = MainLayout.class)
public class DataFabricView extends Div implements HasDynamicTitle {

    public DataFabricView() {
        addClassName("data-fabric-view");
        //add(new Text("Content placeholder"));
    }

    private final String SYSTEM_TITLE_PREFIX = SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.SYSTEM_TITLE_PREFIX);
    @Override
    public String getPageTitle() {
        if(SYSTEM_TITLE_PREFIX != null){
            return SYSTEM_TITLE_PREFIX+" - 数据编织 [ Data Fabric ]";
        }else{
            return "数海云图 - 数据编织 [ Data Fabric ]";
        }
    }
}
