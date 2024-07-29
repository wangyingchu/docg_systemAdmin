package com.viewfunction.docg.views.about;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

import com.viewfunction.docg.element.externalTechFeature.conceptionEntitiesRelationsChart.ConceptionEntitiesRelationsChart;
import com.viewfunction.docg.element.externalTechFeature.rgbColorPicker.RgbaColorPicker;
import com.viewfunction.docg.views.MainLayout;
import com.viewfunction.docg.views.corerealm.featureUI.coreRealmData.DataRelationDistributionChart_React;

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

        DataRelationDistributionChart_React dataRelationDistributionChart_React = new DataRelationDistributionChart_React();
        dataRelationDistributionChart_React.setChartWidth(200);
        dataRelationDistributionChart_React.setChartHeight(300);

        add(dataRelationDistributionChart_React);
    }

}
