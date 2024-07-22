package com.viewfunction.docg.views.about;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.element.externalTechFeature.conceptionEntitiesRelationsChart.ConceptionEntitiesRelationsChart;
import com.viewfunction.docg.element.externalTechFeature.relatedConceptionEntitiesDandelionGraph.RelatedConceptionEntitiesDandelionGraphChart;
import com.viewfunction.docg.element.externalTechFeature.rgbColorPicker.RgbaColorPicker;
import com.viewfunction.docg.views.MainLayout;

@PageTitle("数海云图 - 关于 [ About ]")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends Div {

    public AboutView() {
        addClassName("about-view");
        add(new Text("Content placeholder"));


        RgbaColorPicker rgbaColorPicker = new RgbaColorPicker();
        add(rgbaColorPicker);

        add(new Button("DIV"));

        ConceptionEntitiesRelationsChart conceptionEntitiesRelationsChart = new ConceptionEntitiesRelationsChart();
        add(conceptionEntitiesRelationsChart);


        //VerticalLayout containerVerticalLayout = new VerticalLayout();
       // containerVerticalLayout.setWidth(500, Unit.PIXELS);
        //containerVerticalLayout.setHeight(600,Unit.PIXELS);
       // add(containerVerticalLayout);
        RelatedConceptionEntitiesDandelionGraphChart relatedConceptionEntitiesDandelionGraphChart = new RelatedConceptionEntitiesDandelionGraphChart();
        add(relatedConceptionEntitiesDandelionGraphChart);

        //containerVerticalLayout.add(relatedConceptionEntitiesDandelionGraphChart);
    }

}
