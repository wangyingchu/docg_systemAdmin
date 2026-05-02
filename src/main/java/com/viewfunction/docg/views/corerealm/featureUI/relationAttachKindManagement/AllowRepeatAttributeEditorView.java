package com.viewfunction.docg.views.corerealm.featureUI.relationAttachKindManagement;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class AllowRepeatAttributeEditorView extends VerticalLayout {

    private Popover containerPopover;

    public AllowRepeatAttributeEditorView(String relationAttachKindName) {
        Icon kindIcon = VaadinIcon.FLIP_H.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");

       // kindIcon.setSize("12px");
       // kindIcon.getStyle().set("padding-right","3px");
       // Icon entityIcon = VaadinIcon.FLIP_H.create();
       // entityIcon.setSize("18px");
       // entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, relationAttachKindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);
    }

    public void setContainerPopover(Popover containerPopover) {
        this.containerPopover = containerPopover;
    }
}
