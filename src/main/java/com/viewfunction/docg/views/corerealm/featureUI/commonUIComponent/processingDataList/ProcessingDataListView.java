package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import org.vaadin.tabs.PagedTabs;

public class ProcessingDataListView extends VerticalLayout {

    public ProcessingDataListView(int heightValue){
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);
        VerticalLayout container = new VerticalLayout();
        container.setPadding(false);
        container.setMargin(false);
        container.setSpacing(false);

        VerticalLayout conceptionEntityDataListContainer = new VerticalLayout();
        VerticalLayout relationEntityDataListContainer = new VerticalLayout();

        ProcessingConceptionEntityListView processingConceptionEntityListView = new ProcessingConceptionEntityListView(heightValue);
        processingConceptionEntityListView.showControllerToolbar(true);
        conceptionEntityDataListContainer.add(processingConceptionEntityListView);

        ProcessingRelationEntityListView processingRelationEntityListView = new ProcessingRelationEntityListView(heightValue);
        processingRelationEntityListView.showControllerToolbar(true);
        relationEntityDataListContainer.add(processingRelationEntityListView);

        PagedTabs tabs = new PagedTabs(container);
        tabs.getElement().getStyle().set("width","100%");

        Tab tab0 = tabs.add("",conceptionEntityDataListContainer,false);
        Span relationInfoSpan =new Span();
        Icon relationInfoIcon = new Icon(VaadinIcon.CUBE);
        relationInfoIcon.setSize("20px");
        Label relationInfoLabel = new Label(" 概念实体数据");
        relationInfoSpan.add(relationInfoIcon,relationInfoLabel);
        tab0.add(relationInfoSpan);

        Tab tab1 = tabs.add("", relationEntityDataListContainer,false);
        Span networkGraphSpan =new Span();
        Icon networkGraphIcon = new Icon(VaadinIcon.CONNECT_O);
        networkGraphIcon.setSize("20px");
        Label networkGraphLabel = new Label(" 关系实体数据");
        networkGraphSpan.add(networkGraphIcon,networkGraphLabel);
        tab1.add(networkGraphSpan);

        add(tabs,container);
    }
}
