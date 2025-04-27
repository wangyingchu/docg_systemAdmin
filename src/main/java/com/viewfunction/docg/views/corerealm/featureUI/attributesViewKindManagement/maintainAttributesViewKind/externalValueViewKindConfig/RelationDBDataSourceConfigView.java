package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind.externalValueViewKindConfig;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class RelationDBDataSourceConfigView extends VerticalLayout {

    private AttributesViewKind attributesViewKind;

    public RelationDBDataSourceConfigView(AttributesViewKind attributesViewKind){
        this.setWidthFull();
        this.attributesViewKind = attributesViewKind;

        Icon attributesViewKindIcon = VaadinIcon.TASKS.create();
        attributesViewKindIcon.setSize("12px");
        attributesViewKindIcon.getStyle().set("padding-right","3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributesViewKindIcon,this.attributesViewKind.getAttributesViewKindName()+" ( "+this.attributesViewKind.getAttributesViewKindUID()+" ) "));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        TextField eventCommentField = new TextField("Database Address");
        eventCommentField.setRequired(true);
        eventCommentField.setRequiredIndicatorVisible(true);
        eventCommentField.setWidthFull();
        add(eventCommentField);

        TextField eventCommentField2 = new TextField("Database Port");
        eventCommentField2.setRequired(true);
        eventCommentField2.setRequiredIndicatorVisible(true);
        eventCommentField2.setWidthFull();
        add(eventCommentField2);

        TextField eventCommentField21 = new TextField("User Account");
        eventCommentField21.setRequired(true);
        eventCommentField21.setRequiredIndicatorVisible(true);
        eventCommentField21.setWidthFull();
        add(eventCommentField21);

        TextField eventCommentField22 = new TextField("User Password");
        eventCommentField22.setRequired(true);
        eventCommentField22.setRequiredIndicatorVisible(true);
        eventCommentField22.setWidthFull();
        add(eventCommentField22);

        TextField eventCommentField3 = new TextField("Table Name");
        eventCommentField3.setRequired(true);
        eventCommentField3.setRequiredIndicatorVisible(true);
        eventCommentField3.setWidthFull();
        add(eventCommentField3);



    }
}
