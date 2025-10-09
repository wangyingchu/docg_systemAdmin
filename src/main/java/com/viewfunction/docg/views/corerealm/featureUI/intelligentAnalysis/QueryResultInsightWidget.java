package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentQueryResult;

public class QueryResultInsightWidget extends VerticalLayout {

    private DynamicContentQueryResult dynamicContentQueryResult;
    private boolean alreadyInsighted = false;
    private HorizontalLayout doesNotContainsInsightInfoMessage;

    public QueryResultInsightWidget(DynamicContentQueryResult dynamicContentQueryResult){
        this.setWidthFull();
        this.setHeightFull();
        this.dynamicContentQueryResult = dynamicContentQueryResult;

        doesNotContainsInsightInfoMessage = new HorizontalLayout();
        doesNotContainsInsightInfoMessage.setSpacing(true);
        doesNotContainsInsightInfoMessage.setPadding(true);
        doesNotContainsInsightInfoMessage.setMargin(true);
        doesNotContainsInsightInfoMessage.setWidth(100, Unit.PERCENTAGE);
        doesNotContainsInsightInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 当前探索结果中不包含可以解读的信息");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        doesNotContainsInsightInfoMessage.add(messageLogo,messageLabel);
        add(doesNotContainsInsightInfoMessage);
    }


    public void doInsight(){
        System.out.println("execute do insight");
        if(!alreadyInsighted){
            alreadyInsighted = true;
        }
    }
}
