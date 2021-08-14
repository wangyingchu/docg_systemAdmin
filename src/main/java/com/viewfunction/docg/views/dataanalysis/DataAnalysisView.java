package com.viewfunction.docg.views.dataanalysis;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.views.MainLayout;

@PageTitle("Data Analysis")
@Route(value = "data-analysis", layout = MainLayout.class)
public class DataAnalysisView extends Div {

    public DataAnalysisView() {
        addClassName("data-analysis-view");
        add(new Text("Content placeholder"));
    }

}
