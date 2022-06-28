package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.eventHandling.ConceptionKindQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;

public class ConceptionKindQueryParametersView extends VerticalLayout {

    private String conceptionKindName;
    public ConceptionKindQueryParametersView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
        Button executeQueryButton = new Button("查询概念实体");
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                queryConceptionEntities();
            }
        });
        add(executeQueryButton);
    }

    private void queryConceptionEntities(){
        ConceptionKindQueriedEvent conceptionKindQueriedEvent = new ConceptionKindQueriedEvent();
        conceptionKindQueriedEvent.setConceptionKindName(this.conceptionKindName);
        ResourceHolder.getApplicationBlackboard().fire(conceptionKindQueriedEvent);
    }
}
