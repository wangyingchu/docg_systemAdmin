package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EntityAttributeNamesMappingView extends VerticalLayout {

    public EntityAttributeNamesMappingView(List<String> attributesList,List<String> existingKindAttributesList){
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);
        this.setWidth(100, Unit.PERCENTAGE);

        if(attributesList != null){
            for(String currentAttribute : attributesList){
                EntityAttributeNameMapperWidget currentEntityAttributeNameMapperWidget = new EntityAttributeNameMapperWidget(currentAttribute,existingKindAttributesList);
                add(currentEntityAttributeNameMapperWidget);
            }
        }
    }

    public void refreshEntityAttributeNamesMappingInfo(List<String> attributesList,List<String> existingKindAttributesList){
        this.removeAll();
        if(attributesList != null){
            for(String currentAttribute : attributesList){
                EntityAttributeNameMapperWidget currentEntityAttributeNameMapperWidget = new EntityAttributeNameMapperWidget(currentAttribute,existingKindAttributesList);
                add(currentEntityAttributeNameMapperWidget);
            }
        }
    }

    public Map<String,String> getAttributesMapping(){
        Map<String,String> attributesMapping = new HashMap<>();
        this.getChildren().forEach(new Consumer<Component>() {
            @Override
            public void accept(Component component) {
                if(component instanceof EntityAttributeNameMapperWidget){
                    EntityAttributeNameMapperWidget currentEntityAttributeNameMapperWidget = (EntityAttributeNameMapperWidget)component;
                    attributesMapping.put(currentEntityAttributeNameMapperWidget.getAttributeName(),currentEntityAttributeNameMapperWidget.getAttributeMapping());
                }
            }
        });
        return attributesMapping;
    }
}
