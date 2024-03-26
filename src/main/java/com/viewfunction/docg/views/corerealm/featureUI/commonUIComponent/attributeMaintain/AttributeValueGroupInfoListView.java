package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeMaintain;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class AttributeValueGroupInfoListView extends VerticalLayout {
    public enum AttributeKindType {ConceptionKind,RelationKind}
    private String kindName;
    private String attributeName;
    private AttributeKindType attributeKindType;
    public AttributeValueGroupInfoListView(AttributeKindType attributeKindType, String kindName, String attributeName){

        this.attributeKindType = attributeKindType;
        this.kindName = kindName;
        this.attributeName = attributeName;

        String uidColumnName = "概念实体 UID";
        Icon kindIcon = VaadinIcon.CUBE.create();
        switch (this.attributeKindType){
            case ConceptionKind :
                kindIcon = VaadinIcon.CUBE.create();
                break;
            case RelationKind : kindIcon = VaadinIcon.CONNECT_O.create();
                uidColumnName = "关系实体 UID";
        }

        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.kindName));

        Icon attributeIcon = VaadinIcon.INPUT.create();
        attributeIcon.setSize("18px");
        attributeIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributeIcon, this.attributeName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

    }
}
