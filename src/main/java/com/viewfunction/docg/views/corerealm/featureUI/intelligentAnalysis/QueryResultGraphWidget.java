package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentQueryResult;

public class QueryResultGraphWidget extends VerticalLayout {

    private DynamicContentQueryResult dynamicContentQueryResult;
    private boolean alreadyInsighted = false;
    private HorizontalLayout doesNotContainsGraphInfoMessage;

    public QueryResultGraphWidget(DynamicContentQueryResult dynamicContentQueryResult){
        this.setWidthFull();
        this.setHeightFull();
        this.setMargin(false);
        this.setPadding(false);
        this.setSpacing(false);
        this.dynamicContentQueryResult = dynamicContentQueryResult;

        doesNotContainsGraphInfoMessage = new HorizontalLayout();
        doesNotContainsGraphInfoMessage.setSpacing(true);
        doesNotContainsGraphInfoMessage.setPadding(true);
        doesNotContainsGraphInfoMessage.setMargin(true);
        doesNotContainsGraphInfoMessage.setWidth(100, Unit.PERCENTAGE);
        doesNotContainsGraphInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 当前探索结果中不包含图谱信息");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        doesNotContainsGraphInfoMessage.add(messageLogo,messageLabel);
        add(doesNotContainsGraphInfoMessage);
    }


    public void doDrawGraph(){
        System.out.println("execute do graph");
        if(!alreadyInsighted){
            alreadyInsighted = true;
        }
    }
}
