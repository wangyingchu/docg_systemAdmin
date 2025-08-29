package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

public class InformationInsightView extends VerticalLayout {

    private Scroller insightContentScroller;
    private VerticalLayout insightContentContainerLayout;
    private TextArea questionTextArea;
    private InformationInsightModeControllerWidget informationInsightModeControllerWidget;

    public InformationInsightView() {
        this.setWidthFull();
        this.insightContentScroller = new Scroller();
        this.insightContentScroller.setWidthFull();
        this.add(this.insightContentScroller);
        this.insightContentContainerLayout = new VerticalLayout();
        this.insightContentContainerLayout.setWidthFull();
        this.insightContentScroller.setContent(this.insightContentContainerLayout);
        this.informationInsightModeControllerWidget = new InformationInsightModeControllerWidget();

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
        buttonsControllerLayout.add(askButton);
    }

    public void setInsightContentHeight(int heightValue){
        this.insightContentScroller.setHeight(heightValue, Unit.PIXELS);
    }
}
