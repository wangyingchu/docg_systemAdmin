package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class InformationInsightModeControllerWidget extends HorizontalLayout{

    public InformationInsightModeControllerWidget() {
        this.setSpacing(false);
        this.setPadding(false);
        this.setMargin(false);
        NativeLabel talkModeMessage = new NativeLabel("对话模式:");
        talkModeMessage.getStyle().set("font-size","8px").set("padding-left","5px");
        add(talkModeMessage);
    }
}
