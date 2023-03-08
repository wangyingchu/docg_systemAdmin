package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import org.vaadin.tabs.PagedTabs;

import java.util.function.Consumer;

public class RelationEntityIntegratedInfoView extends VerticalLayout {

    private String relationKind;
    private String relationEntityUID;
    private RelationEntityConnectedConceptionEntitiesPairView relationEntityConnectedConceptionEntitiesPairView;

    public RelationEntityIntegratedInfoView(String relationKind,String relationEntityUID,int relationEntityIntegratedInfoViewHeightOffset){
        this.relationKind = relationKind;
        this.relationEntityUID = relationEntityUID;

        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);
        VerticalLayout container = new VerticalLayout();

        PagedTabs tabs = new PagedTabs(container);
        tabs.getElement().getStyle().set("width","100%");
        this.relationEntityConnectedConceptionEntitiesPairView = new RelationEntityConnectedConceptionEntitiesPairView(this.relationKind,this.relationEntityUID,relationEntityIntegratedInfoViewHeightOffset);

        Tab tab0 = tabs.add("", relationEntityConnectedConceptionEntitiesPairView,false);
        Span relationConceptionEntitiesPairInfoSpan =new Span();
        FontAwesome.Solid.Icon relationConceptionEntitiesPairInfoIcon = FontAwesome.Solid.LINK.create();
        relationConceptionEntitiesPairInfoIcon.setSize("20px");
        Label relationConceptionEntitiesPairInfoLabel = new Label(" 关系实体数据信息");
        relationConceptionEntitiesPairInfoSpan.add(relationConceptionEntitiesPairInfoIcon,relationConceptionEntitiesPairInfoLabel);
        tab0.add(relationConceptionEntitiesPairInfoSpan);

        add(tabs,container);

        tabs.addSelectedChangeListener(new Consumer<Tab>() {
            @Override
            public void accept(Tab tab) {}
        });
    }
}
