package com.viewfunction.docg.views.computegrid;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.views.MainLayout;

@PageTitle("Compute Grid")
@Route(value = "compute-grid", layout = MainLayout.class)
public class ComputeGridView extends Div {

    public ComputeGridView() {
        addClassName("compute-grid-view");
        add(new Text("Content placeholder"));
    }

}
