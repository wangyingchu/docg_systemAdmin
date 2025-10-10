package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentQueryResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentValue;

import java.util.*;

public class QueryResultInsightWidget extends VerticalLayout {

    private DynamicContentQueryResult dynamicContentQueryResult;
    private boolean alreadyInsighted = false;
    private HorizontalLayout doesNotContainsInsightInfoMessage;

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
        List<Map<String, DynamicContentValue>> contentValueList = dynamicContentQueryResult.getDynamicContentResultValueList();
        Map<String, DynamicContentValue.ContentValueType> contentValueTypeMap = dynamicContentQueryResult.getDynamicContentAttributesValueTypeMap();
        List<String> fixedProperties = new ArrayList<>();
        fixedProperties.addAll( contentValueTypeMap.keySet());

        StringBuilder sb = new StringBuilder();

        StringBuilder headerInfo = new StringBuilder();
        fixedProperties.forEach( propertyName -> {
            headerInfo.append(propertyName).append(",");
        });
        headerInfo.deleteCharAt(headerInfo.length() - 1);
        sb.append(headerInfo).append("\n");

        for(Map<String, DynamicContentValue> currentDataMap:contentValueList){
            StringBuilder currentDataInfo = new StringBuilder();
            fixedProperties.forEach( propertyName -> {
                DynamicContentValue currentColumnContentValue = currentDataMap.get(propertyName);
                currentDataInfo.append(currentColumnContentValue.getValueObject().toString()).append(",");
            });
            currentDataInfo.deleteCharAt(currentDataInfo.length() - 1);


            sb.append(currentDataInfo).append("\n");
        }
        //分析以下CSV格式的数据内容：
        System.out.println(sb.toString());
    }
}
