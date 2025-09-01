package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.List;

public class InformationInsightModeControllerWidget extends HorizontalLayout{

    private Button setInsightScopeButton;
    private InformationInsightScopeManagementWidget informationInsightScopeManagementWidget;
    private List<String> insightScopeConceptionKindList;
    private List<String> insightScopeRelationKindList;
    private List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList;

    public InformationInsightModeControllerWidget(List<String> insightScopeConceptionKindList,
                                                  List<String> insightScopeRelationKindList,List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList) {
        this.setSpacing(false);
        this.setPadding(false);
        this.setMargin(false);
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.insightScopeConceptionKindList = insightScopeConceptionKindList;
        this.insightScopeRelationKindList = insightScopeRelationKindList;
        this.insightScopeConceptionKindCorrelationList = insightScopeConceptionKindCorrelationList;

        Icon infoIcon = LineAwesomeIconsSvg.COMMENT.create();
        infoIcon.setSize("14px");
        infoIcon.getStyle().set("padding-left","5px");
        add(infoIcon);

        NativeLabel talkModeMessage = new NativeLabel("对话模式 : ");
        talkModeMessage.getStyle().set("font-size","8px");
        add(talkModeMessage);

        RadioButtonGroup talkModeRadioGroup = new RadioButtonGroup<>();
        //<theme-editor-local-classname>
        talkModeRadioGroup.addClassName("geospatial-region-detail-ui-radio-group-1");
        talkModeRadioGroup.getStyle().set("top","2px").set("position","relative");
        talkModeRadioGroup.setItems("探索", "洞察");
        talkModeRadioGroup.setValue("探索");

        talkModeRadioGroup.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String> radioButtonGroupStringComponentValueChangeEvent) {
                String newValue = radioButtonGroupStringComponentValueChangeEvent.getValue();
                if("洞察".equals(newValue)){
                    setInsightScopeButton.setEnabled(true);
                }else{
                    setInsightScopeButton.setEnabled(false);
                }
            }
        });
        add(talkModeRadioGroup);

        informationInsightScopeManagementWidget = new InformationInsightScopeManagementWidget(
                insightScopeConceptionKindList,insightScopeRelationKindList,insightScopeConceptionKindCorrelationList);

        setInsightScopeButton = new Button();
        setInsightScopeButton.setIcon(LineAwesomeIconsSvg.BUROMOBELEXPERTE.create());
        setInsightScopeButton.setTooltipText("洞察范围");
        setInsightScopeButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        setInsightScopeButton.setEnabled(false);
        setInsightScopeButton.addClickListener(event -> {
            informationInsightScopeManagementWidget.refreshScopeManagementUI();
        });
        add(setInsightScopeButton);

        Popover popover = new Popover();
        popover.setTarget(setInsightScopeButton);
        popover.setWidth("1600px");
        popover.setHeight("600px");
        popover.addThemeVariants(PopoverVariant.ARROW,PopoverVariant.LUMO_NO_PADDING);
        popover.setPosition(PopoverPosition.TOP);
        popover.setModal(true,true);
        popover.add(informationInsightScopeManagementWidget);
    }
}
