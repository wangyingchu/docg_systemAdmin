package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class EntityAttributeNamesMappingView extends VerticalLayout {

    public EntityAttributeNamesMappingView(List<String> attributesList){
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);
        this.setWidth(100, Unit.PERCENTAGE);
        if(attributesList != null){
            for(String currentAttribute : attributesList){
                EntityAttributeNameMapperWidget currentEntityAttributeNameMapperWidget = new EntityAttributeNameMapperWidget(currentAttribute);
                add(currentEntityAttributeNameMapperWidget);
            }
        }
    }
}
