package com.viewfunction.docg.views.corerealm;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.views.MainLayout;

@PageTitle("Core Realm")
@Route(value = "core-realm", layout = MainLayout.class)
public class CoreRealmView extends Div {

    public CoreRealmView() {
        addClassName("core-realm-view");
        add(new Text("Content placeholder"));
    }

}
