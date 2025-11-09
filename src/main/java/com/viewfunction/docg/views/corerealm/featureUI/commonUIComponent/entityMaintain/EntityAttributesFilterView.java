package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class EntityAttributesFilterView extends VerticalLayout {

    public interface EntityAttributesFilterSetListener{
        public void entityAttributesFilterSetAction(String currentFilterText);
    }

    public enum KindType {ConceptionKind,RelationKind}
    private EntityAttributesFilterSetListener entityAttributesFilterSetListener;
    private Popover containerPopover;

    public EntityAttributesFilterView(String kindName,String entityUID,KindType kindType,EntityAttributesFilterSetListener entityAttributesFilterSetListener){
        this.setMargin(false);
        this.setSpacing(false);
        this.entityAttributesFilterSetListener = entityAttributesFilterSetListener;

        Icon kindIcon = VaadinIcon.CUBE.create();
        switch (kindType){
            case ConceptionKind ->  kindIcon = VaadinIcon.CUBE.create();
            case RelationKind -> kindIcon = VaadinIcon.CONNECT_O.create();

        }
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        String kindNameStr = kindName != null ? kindName : "[...]";
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, kindNameStr));
        if(entityUID != null ){
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(entityIcon, entityUID));
        }
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);
        if(entityUID == null){
            entityInfoFootprintMessageBar.setVisible(false);
        }

        NativeLabel viewTitle = new NativeLabel("属性过滤信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        add(viewTitle);

        HorizontalLayout attributeValueFieldContainerLayout = new HorizontalLayout();
        add(attributeValueFieldContainerLayout);

        TextField attributeNameField = new TextField();
        attributeValueFieldContainerLayout.add(attributeNameField);
        attributeNameField.setPlaceholder("属性名称");
        attributeNameField.setWidth(220, Unit.PIXELS);

        Button confirmButton = new Button("过滤",new Icon(VaadinIcon.CHECK));
        confirmButton.setWidth(80,Unit.PIXELS);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        attributeValueFieldContainerLayout.add(confirmButton);
        confirmButton.addClickListener(buttonClickEvent -> {
            if(entityAttributesFilterSetListener != null){
                if(!attributeNameField.getValue().equals("")){
                    entityAttributesFilterSetListener.entityAttributesFilterSetAction(attributeNameField.getValue());
                }
            }
        });

        Button cancelButton = new Button("取消",new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        cancelButton.setWidth(80,Unit.PIXELS);
        attributeValueFieldContainerLayout.add(cancelButton);
        cancelButton.addClickListener(buttonClickEvent -> {
            if(entityAttributesFilterSetListener != null){
                attributeNameField.setValue("");
                entityAttributesFilterSetListener.entityAttributesFilterSetAction(null);
                if(this.containerPopover != null){
                    this.containerPopover.close();
                }
            }
        });
    }

    public void setContainerPopover(Popover containerPopover) {
        this.containerPopover = containerPopover;
    }
}
