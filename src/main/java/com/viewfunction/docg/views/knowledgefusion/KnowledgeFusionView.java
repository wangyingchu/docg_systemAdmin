package com.viewfunction.docg.views.knowledgefusion;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.views.MainLayout;

@PageTitle("Knowledge Fusion")
@Route(value = "knowledge-fusion", layout = MainLayout.class)
public class KnowledgeFusionView extends Div {

    public KnowledgeFusionView() {
        addClassName("knowledge-fusion-view");
        add(new Text("Content placeholder"));
    }

}
