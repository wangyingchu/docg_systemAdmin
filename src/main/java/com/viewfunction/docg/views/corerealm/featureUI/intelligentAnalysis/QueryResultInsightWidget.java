package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.docg.ai.llm.naturalLanguageAnalysis.util.DynamicContentInsightUtil;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentQueryResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentValue;

import java.util.*;

public class QueryResultInsightWidget extends VerticalLayout {

    private DynamicContentQueryResult dynamicContentQueryResult;
    private boolean alreadyInsighted = false;
    private HorizontalLayout doesNotContainsInsightInfoMessage;
    private Markdown insightResultMarkdown;

    public QueryResultInsightWidget(DynamicContentQueryResult dynamicContentQueryResult){
        this.setWidthFull();
        this.setHeightFull();
        this.setMargin(false);
        this.setPadding(false);
        this.setSpacing(false);
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

        insightResultMarkdown = new Markdown();
        insightResultMarkdown.setWidth(100, Unit.PERCENTAGE);
        insightResultMarkdown.setHeight(300,Unit.PIXELS);
        add(insightResultMarkdown);
        insightResultMarkdown.setVisible(false);
    }

    public void doInsight(){
        System.out.println("execute do insight");
        if(!alreadyInsighted){
            alreadyInsighted = true;
            List<Map<String, DynamicContentValue>> contentValueList = dynamicContentQueryResult.getDynamicContentResultValueList();
            if(contentValueList != null && ! contentValueList.isEmpty()){
                doesNotContainsInsightInfoMessage.setVisible(false);
                doInsightLogic();
            }
        }
    }

    private void doInsightLogic(){
        insightResultMarkdown.setVisible(true);
        String insightResult = DynamicContentInsightUtil.insightToDynamicContent(dynamicContentQueryResult);
        insightResultMarkdown.setContent(insightResult);
    }
}
