package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class InformationInsightView extends VerticalLayout {

    private Scroller insightContentScroller;
    private VerticalLayout insightContentContainerLayout;
    private TextArea questionTextArea;

    public InformationInsightView() {
        this.setWidthFull();
        this.insightContentScroller = new Scroller();
        this.insightContentScroller.setWidthFull();
        this.add(this.insightContentScroller);
        this.insightContentContainerLayout = new VerticalLayout();
        this.insightContentContainerLayout.setWidthFull();
        this.insightContentScroller.setContent(this.insightContentContainerLayout);
        this.questionTextArea = new TextArea();
        this.questionTextArea.setWidthFull();
        this.questionTextArea.setHeight(150, Unit.PIXELS);
        this.questionTextArea.setLabel("Comment");
        //questionTextArea.setMaxLength(charLimit);
        //questionTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        this.questionTextArea.addValueChangeListener(e -> {
            //e.getSource().setHelperText(e.getValue().length() + "/" + charLimit);
        });
        this.questionTextArea.setValue("Great job. This is excellent!");
        this.add(this.questionTextArea);
    }


    public void setInsightContentHeight(int heightValue){
        this.insightContentScroller.setHeight(heightValue, Unit.PIXELS);
    }
}
