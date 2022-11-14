package com.viewfunction.docg.views.generalinformation;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.views.MainLayout;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("数海云图 - 概览 [ General Information ]")
@Route(value = "general-info", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
public class GeneralInformationView extends Div {

    public GeneralInformationView() {
        addClassName("general-information-view");
        add(new Text("Content placeholder"));
    }

}
