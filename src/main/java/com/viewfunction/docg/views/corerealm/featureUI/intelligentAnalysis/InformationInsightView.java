package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.docg.ai.llm.naturalLanguageAnalysis.util.Text2QueryUtil;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;

public class InformationInsightView extends VerticalLayout {

    private Scroller insightContentScroller;
    private VerticalLayout insightContentContainerLayout;
    private TextArea questionTextArea;
    private InformationInsightModeControllerWidget informationInsightModeControllerWidget;
    private List<String> insightScopeConceptionKindList;
    private List<String> insightScopeRelationKindList;
    private List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList;

    public InformationInsightView() {
        this.setWidthFull();
        this.insightContentScroller = new Scroller();
        this.insightContentScroller.setWidthFull();
        this.add(this.insightContentScroller);
        this.insightContentContainerLayout = new VerticalLayout();
        this.insightContentContainerLayout.setWidthFull();
        this.insightContentScroller.setContent(this.insightContentContainerLayout);
        this.insightScopeConceptionKindList = new ArrayList<>();
        this.insightScopeRelationKindList = new ArrayList<>();
        this.insightScopeConceptionKindCorrelationList = new ArrayList<>();
        this.informationInsightModeControllerWidget = new InformationInsightModeControllerWidget(
                this.insightScopeConceptionKindList,this.insightScopeRelationKindList,this.insightScopeConceptionKindCorrelationList);

        HorizontalLayout inputElementContainerLayout = new HorizontalLayout();
        inputElementContainerLayout.setWidthFull();
        inputElementContainerLayout.setWidthFull();
        inputElementContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.add(inputElementContainerLayout);

        this.questionTextArea = new TextArea();
        this.questionTextArea.setWidthFull();
        this.questionTextArea.setHeight(155, Unit.PIXELS);
        //questionTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        this.questionTextArea.addValueChangeListener(e -> {
            //e.getSource().setHelperText(e.getValue().length() + "/" + charLimit);
        });
        this.questionTextArea.setHelperComponent(this.informationInsightModeControllerWidget);
        this.questionTextArea.setPlaceholder("Message for the bot");

        inputElementContainerLayout.add(this.questionTextArea);
        inputElementContainerLayout.setFlexGrow(1, this.questionTextArea);
        HorizontalLayout buttonsControllerLayout = new HorizontalLayout();
        inputElementContainerLayout.add(buttonsControllerLayout);

        Button askButton = new Button(" 对话");
        askButton.getStyle().set("top","-15px").set("position","relative");
        askButton.setIcon(LineAwesomeIconsSvg.COMMENTS.create());
        askButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        askButton.setWidth(30,Unit.PIXELS);
        askButton.setHeight(70,Unit.PIXELS);
        askButton.addClickListener(e -> {
            doAITalk();
        });
        buttonsControllerLayout.add(askButton);
    }

    public void setInsightContentHeight(int heightValue){
        this.insightContentScroller.setHeight(heightValue, Unit.PIXELS);
    }

    public void addConceptionKindToInsightScope(String conceptionKindName){
        if(!insightScopeConceptionKindList.contains(conceptionKindName)){
            insightScopeConceptionKindList.add(conceptionKindName);
        }
        CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKindName+" 加入洞察范围成功", NotificationVariant.LUMO_SUCCESS,2000, Notification.Position.BOTTOM_START);
    }

    public void addRelationKindToInsightScope(String relationKindName){
        if(!insightScopeRelationKindList.contains(relationKindName)){
            insightScopeRelationKindList.add(relationKindName);
        }
        CommonUIOperationUtil.showPopupNotification("关系类型 "+relationKindName+" 加入洞察范围成功", NotificationVariant.LUMO_SUCCESS,2000, Notification.Position.BOTTOM_START);
    }

    public void addConceptionKindCorrelationToInsightScope(ConceptionKindCorrelationInfo conceptionKindCorrelationInfo){
        CommonUIOperationUtil.showPopupNotification("概念关联 "+conceptionKindCorrelationInfo.getSourceConceptionKindName()+
                " - "+conceptionKindCorrelationInfo.getRelationKindName()+" - "+
                conceptionKindCorrelationInfo.getTargetConceptionKindName()+" 加入洞察范围成功", NotificationVariant.LUMO_SUCCESS,2000, Notification.Position.BOTTOM_START);
        String correlationPattern = conceptionKindCorrelationInfo.getSourceConceptionKindName()+" - "+conceptionKindCorrelationInfo.getRelationKindName()+" - "+conceptionKindCorrelationInfo.getTargetConceptionKindName();
        for(ConceptionKindCorrelationInfo currentConceptionKindCorrelationInfo:this.insightScopeConceptionKindCorrelationList){
            String currentCorrelationPattern = currentConceptionKindCorrelationInfo.getSourceConceptionKindName()+" - "+currentConceptionKindCorrelationInfo.getRelationKindName()+" - "+currentConceptionKindCorrelationInfo.getTargetConceptionKindName();
            if(currentCorrelationPattern.equals(correlationPattern)){
                return;
            }
        }
        this.insightScopeConceptionKindCorrelationList.add(conceptionKindCorrelationInfo);
    }

    private void doAITalk(){
        String question = this.questionTextArea.getValue();
        if(question == null || question.trim().length() == 0){
            CommonUIOperationUtil.showPopupNotification("请输入问题", NotificationVariant.LUMO_ERROR,1500, Notification.Position.MIDDLE);
        }else{
            String cql = Text2QueryUtil.generateQueryCypher(question);
            this.insightContentContainerLayout.add(new Span(cql));
        }
    }
}
