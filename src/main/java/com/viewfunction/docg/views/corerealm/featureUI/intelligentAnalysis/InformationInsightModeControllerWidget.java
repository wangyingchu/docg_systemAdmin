package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

public class InformationInsightModeControllerWidget extends HorizontalLayout{

    private Button setInsightAreaButton;

    public InformationInsightModeControllerWidget() {
        this.setSpacing(false);
        this.setPadding(false);
        this.setMargin(false);
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);

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
                    setInsightAreaButton.setEnabled(true);
                }else{
                    setInsightAreaButton.setEnabled(false);
                }
            }
        });
        add(talkModeRadioGroup);

        setInsightAreaButton = new Button();
        setInsightAreaButton.setIcon(LineAwesomeIconsSvg.BUROMOBELEXPERTE.create());
        setInsightAreaButton.setTooltipText("洞察范围");
        setInsightAreaButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY_INLINE);
        setInsightAreaButton.setEnabled(false);
        add(setInsightAreaButton);
    }
}
