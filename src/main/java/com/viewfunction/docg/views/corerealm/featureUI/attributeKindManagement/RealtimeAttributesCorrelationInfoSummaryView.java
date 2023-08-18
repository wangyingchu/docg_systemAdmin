package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.util.Set;

public class RealtimeAttributesCorrelationInfoSummaryView extends HorizontalLayout {

    public RealtimeAttributesCorrelationInfoSummaryView(int viewHeight){
        this.setWidthFull();
        this.setHeight(viewHeight,Unit.PIXELS);

        VerticalLayout leftSideContainer = new VerticalLayout();
        leftSideContainer.setSpacing(false);

        VerticalLayout rightSideContainer = new VerticalLayout();
        add(leftSideContainer,rightSideContainer);

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"领域实时属性概览");
        filterTitle1.getStyle().set("padding-bottom", "var(--lumo-space-s)");
        leftSideContainer.add(filterTitle1);

        Div attributesContainerDiv = new Div();
        attributesContainerDiv.setWidth(500, Unit.PIXELS);

        Scroller scroller = new Scroller(attributesContainerDiv);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        scroller.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("border-top", "1px solid var(--lumo-contrast-20pct)");
        leftSideContainer.add(scroller);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Set<String> realtimeAttributesSet = coreRealm.getSystemMaintenanceOperator().getRealtimeAttributesStatistics();
        for(String currentAttribute:realtimeAttributesSet){
            Button showDetailButton = new Button(currentAttribute);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    renderAttributeRealtimeInfo(currentAttribute);
                }
            });

            attributesContainerDiv.add(showDetailButton);
            Span pendingSpan = new Span(" ");
            pendingSpan.setWidth("10px");
            attributesContainerDiv.add(pendingSpan);
        }
    }

    private void renderAttributeRealtimeInfo(String attributeName){

    }
}
