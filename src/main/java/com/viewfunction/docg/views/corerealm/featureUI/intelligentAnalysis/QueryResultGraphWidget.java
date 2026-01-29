package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentQueryResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentValue;

import java.util.Collection;
import java.util.Map;

public class QueryResultGraphWidget extends VerticalLayout {

    private DynamicContentQueryResult dynamicContentQueryResult;
    private boolean alreadyInsighted = false;
    private HorizontalLayout doesNotContainsGraphInfoMessage;
    private ExplorationResultGraphChart explorationResultGraphChart;

    public QueryResultGraphWidget(){
        this.setWidthFull();
        this.setHeightFull();
        this.setMargin(false);
        this.setPadding(false);
        this.setSpacing(false);

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

        explorationResultGraphChart = new ExplorationResultGraphChart();
        add(explorationResultGraphChart);
        explorationResultGraphChart.setVisible(false);
    }


    public void doDrawGraph(DynamicContentQueryResult dynamicContentQueryResult){
        this.dynamicContentQueryResult = dynamicContentQueryResult;
        System.out.println("execute do graph");
        if(!alreadyInsighted){
            alreadyInsighted = true;
        }
        boolean containsGraphInfo = false;
        if(dynamicContentQueryResult != null){
            Map<String, DynamicContentValue.ContentValueType> resultContentValueTypeMap = dynamicContentQueryResult.getDynamicContentAttributesValueTypeMap();
            if(resultContentValueTypeMap != null && !resultContentValueTypeMap.isEmpty()){
                Collection<DynamicContentValue.ContentValueType> contentValueTypes= resultContentValueTypeMap.values();
                for(DynamicContentValue.ContentValueType contentValueType:contentValueTypes){
                    if(DynamicContentValue.ContentValueType.CONCEPTION_ENTITY.equals(contentValueType)||
                            DynamicContentValue.ContentValueType.RELATION_ENTITY.equals(contentValueType)||
                            DynamicContentValue.ContentValueType.ENTITIES_PATH.equals(contentValueType)
                    ){
                        containsGraphInfo = true;
                    }
                }
            }
        }

        if(containsGraphInfo){
            explorationResultGraphChart.setVisible(true);
            doesNotContainsGraphInfoMessage.setVisible(false);
        }else{
            explorationResultGraphChart.setVisible(false);
            doesNotContainsGraphInfoMessage.setVisible(true);
        }

        System.out.println("---------------------------");
        System.out.println(containsGraphInfo);
        System.out.println(containsGraphInfo);
        System.out.println(containsGraphInfo);
        System.out.println("---------------------------");
    }
}
