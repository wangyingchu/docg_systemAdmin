package com.viewfunction.docg.views.dataFabric;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.views.MainLayout;

@PageTitle("数海云图 - 数据编织 [ Data Fabric ]")
@Route(value = "data-fabric", layout = MainLayout.class)
public class DataFabricView extends Div {

    public DataFabricView() {
        addClassName("data-fabric-view");
        add(new Text("Content placeholder"));
    }

}
