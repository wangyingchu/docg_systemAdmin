package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.chart.CartesianHeatmapChart;

public class RelationAndConceptionKindAttachInfoWidget extends VerticalLayout {

    public RelationAndConceptionKindAttachInfoWidget(){
        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMPRESS_SQUARE),"概念与关系实体入度统计");
        add(infoTitle1);
        CartesianHeatmapChart cartesianHeatmapChart1 = new CartesianHeatmapChart(380,270);
        cartesianHeatmapChart1.setColorRange("WhiteSmoke","#168eea");
        cartesianHeatmapChart1.setData();
        add(cartesianHeatmapChart1);

        HorizontalLayout spaceDiv01 = new HorizontalLayout();
        spaceDiv01.setHeight(5, Unit.PIXELS);
        add(spaceDiv01);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.EXPAND_SQUARE),"概念与关系实体出度统计");
        add(infoTitle2);
        CartesianHeatmapChart cartesianHeatmapChart2 = new CartesianHeatmapChart(380,270);
        cartesianHeatmapChart2.setColorRange("WhiteSmoke","#323b43");
        cartesianHeatmapChart2.setData();
        add(cartesianHeatmapChart2);
    }

    public void refreshRelationAndConceptionKindAttachInfo(){}
}
