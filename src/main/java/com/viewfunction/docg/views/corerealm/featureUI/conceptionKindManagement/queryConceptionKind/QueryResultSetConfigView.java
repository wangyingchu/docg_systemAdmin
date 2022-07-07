package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class QueryResultSetConfigView extends VerticalLayout {

    private Dialog containerDialog;

    public QueryResultSetConfigView(){

        Label attributeTypeLabel = new Label("设置查询最大返回结果数将忽略查询起始页和查询结束页中的设置");
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeTypeLabel.getStyle().set("font-size","0.9rem").set("color","var(--lumo-contrast-90pct)").set("padding-left","20px");
        add(attributeTypeLabel);




    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
