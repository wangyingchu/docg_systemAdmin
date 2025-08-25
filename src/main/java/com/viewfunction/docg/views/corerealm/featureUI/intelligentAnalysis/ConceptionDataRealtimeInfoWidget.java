package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConceptionDataRealtimeInfoWidget extends VerticalLayout {

    public ConceptionDataRealtimeInfoWidget(){
        this.getStyle().set("background-color", "white");
        Span name = new Span("Sophia Williams");
        Span email = new Span("sophia.williams@company.com");
        Span phone = new Span("(501) 555-9128");

        VerticalLayout content = new VerticalLayout(name, email, phone);
        content.setSpacing(false);
        content.setPadding(false);

        Details details = new Details("Contact information", content);
        details.setOpened(true);

        add(details);
    }
}
