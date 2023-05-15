package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.List;

public class RelateClassificationView extends VerticalLayout {

    private ClassificationConfigView.ClassificationRelatedObjectType classificationRelatedObjectType;
    private String relatedObjectID;
    private Dialog containerDialog;
    private ClassificationConfigView containerClassificationConfigView;

    public RelateClassificationView(ClassificationConfigView.ClassificationRelatedObjectType
                                            classificationRelatedObjectType,String relatedObjectID){
        this.classificationRelatedObjectType = classificationRelatedObjectType;
        this.relatedObjectID = relatedObjectID;
        this.setMargin(false);
        this.setWidthFull();

        Icon kindIcon = VaadinIcon.CUBE.create();
        String viewTitleText = "概念类型索引信息";
        switch (this.classificationRelatedObjectType){
            case ConceptionKind :
                kindIcon = VaadinIcon.CUBE.create();
                viewTitleText = "概念类型索引信息";
                break;
            case RelationKind :
                kindIcon = VaadinIcon.CONNECT_O.create();
                viewTitleText = "关系类型索引信息";
                break;
            case AttributeKind:
                kindIcon = VaadinIcon.INPUT.create();
                viewTitleText = "关系类型索引信息";
                break;
            case AttributesViewKind:
                kindIcon = VaadinIcon.TASKS.create();
                viewTitleText = "关系类型索引信息";
                break;
            case ConceptionEntity:
                kindIcon = LineAwesomeIconsSvg.KEYCDN.create();
                viewTitleText = "关系类型索引信息";
                break;
        }
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, relatedObjectID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setContainerClassificationConfigView(ClassificationConfigView containerClassificationConfigView) {
        this.containerClassificationConfigView = containerClassificationConfigView;
    }
}
