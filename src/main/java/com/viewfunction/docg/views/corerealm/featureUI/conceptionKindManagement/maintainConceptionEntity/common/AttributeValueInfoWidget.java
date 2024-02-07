package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.common;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;

public class AttributeValueInfoWidget extends HorizontalLayout {

    public AttributeValueInfoWidget(AttributeValue attributeValue){
        this.setWidthFull();
        this.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        String attributeName = attributeValue.getAttributeName();
        Object attributeNameValue = attributeValue.getAttributeValue();
        AttributeDataType attributeDataType = attributeValue.getAttributeDataType();

        Icon propertyTypeIcon = null;
        if(attributeName.startsWith(RealmConstant.RealmInnerTypePerFix) ||
                attributeName.equals(RealmConstant._createDateProperty) ||
                attributeName.equals(RealmConstant._lastModifyDateProperty) ||
                attributeName.equals(RealmConstant._creatorIdProperty)||
                attributeName.equals(RealmConstant._dataOriginProperty)
        ){
            propertyTypeIcon = VaadinIcon.ELLIPSIS_CIRCLE_O.create();
        }else{
            propertyTypeIcon = VaadinIcon.ELLIPSIS_CIRCLE.create();
        }

        propertyTypeIcon.setSize("12px");
        add(propertyTypeIcon);
        setVerticalComponentAlignment(Alignment.CENTER,propertyTypeIcon);

        NativeLabel attributeNameLabel = new NativeLabel(attributeName);
        attributeNameLabel.getStyle().set("font-size","0.75rem").set("font-weight","bold");
        add(attributeNameLabel);
        setVerticalComponentAlignment(Alignment.CENTER,attributeNameLabel);

        NativeLabel attributeTypeLabel = new NativeLabel(attributeDataType.toString());
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeTypeLabel.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-70pct)").set("padding-right","8px");
        add(attributeTypeLabel);
        setVerticalComponentAlignment(Alignment.CENTER,attributeTypeLabel);

        NativeLabel attributeValueLabel = new NativeLabel(attributeNameValue.toString());
        attributeValueLabel.getStyle().set("font-size","var(--lumo-font-size-m)")
                .set("color","#2e4e7e").set("font-weight","bold");
        add(attributeValueLabel);
        setVerticalComponentAlignment(Alignment.CENTER,attributeValueLabel);

        /*
        Button showAttributeKindInfoButton = new Button("属性类型详情");
        showAttributeKindInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showAttributeKindInfoButton.getStyle().set("font-size","12px");
        showAttributeKindInfoButton.setIcon(VaadinIcon.EYE.create());
        showAttributeKindInfoButton.setTooltipText("显示属性类型详情");
        showAttributeKindInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        add(showAttributeKindInfoButton);
        setVerticalComponentAlignment(Alignment.CENTER,showAttributeKindInfoButton);
        */
    }
}
