package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.showConceptionEntity;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

public class ConceptionEntityAttributesEditorView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;

    public ConceptionEntityAttributesEditorView(String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;

        SecondaryIconTitle viewTitle = new SecondaryIconTitle(new Icon(VaadinIcon.COMBOBOX),"实体属性");
        add(viewTitle);







    }
}
