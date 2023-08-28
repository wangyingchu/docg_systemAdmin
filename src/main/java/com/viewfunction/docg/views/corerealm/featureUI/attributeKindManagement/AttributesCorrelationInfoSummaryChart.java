package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.util.List;
@JavaScript("./visualization/feature/attributesCorrelationInfoSummaryChart-connector.js")
public class AttributesCorrelationInfoSummaryChart extends VerticalLayout {

    public AttributesCorrelationInfoSummaryChart(){
        //https://www.amcharts.com/demos/rectangular-voronoi-tree-map/
        this.setWidth(400, Unit.PIXELS);
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/index.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/hierarchy.js");
        UI.getCurrent().getPage().addJavaScript("js/amcharts/5.4.5/themes/Animated.js");
        SecondaryIconTitle secondaryIconTitle = new SecondaryIconTitle(VaadinIcon.PIE_CHART.create(), "关联时间序列统计信息");
        add(secondaryIconTitle);
        initConnector();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.feature_AttributesCorrelationInfoSummaryChart.initLazy($0)", getElement()));
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public void setSummaryData(List<RealtimeAttributesCorrelationInfoSummaryView.AttributesDistributionInfo> conceptionAttributesDistributionInfoList,
                               List<RealtimeAttributesCorrelationInfoSummaryView.AttributesDistributionInfo> relationAttributesDistributionInfoList){

        //initConnector();
    }
}
