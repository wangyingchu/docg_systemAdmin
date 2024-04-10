package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.DataSlicePropertyType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class DataSlicePropertiesMappingView extends VerticalLayout {

    private Map<String, DataSlicePropertyType> dataSlicePropertiesMap;
    private Set<String> primaryKeyPropertiesNameSet;

    public DataSlicePropertiesMappingView(){
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);
        this.setWidth(100, Unit.PERCENTAGE);
    }

    public void renderDataSlicePropertiesMapping(Map<String, DataSlicePropertyType> dataSlicePropertiesMap, Set<String> primaryKeyPropertiesNameSet,List<String> existingKindAttributesList){
        this.dataSlicePropertiesMap = dataSlicePropertiesMap;
        this.primaryKeyPropertiesNameSet = primaryKeyPropertiesNameSet;
        this.removeAll();
        if(dataSlicePropertiesMap != null && dataSlicePropertiesMap.size() > 0){
            /*
            for(String currentAttribute : attributesList){
                DataSlicePropertyNameMapperWidget currentEntityAttributeNameMapperWidget = new DataSlicePropertyNameMapperWidget(currentAttribute,existingKindAttributesList);
                add(currentEntityAttributeNameMapperWidget);
            }

             */
        }
    }

    public void clearMappingInfo(){
        this.removeAll();
    }

    public void refreshEntityAttributeNamesMappingInfo(List<String> attributesList,List<String> existingKindAttributesList){
        this.removeAll();
        if(attributesList != null){
            for(String currentAttribute : attributesList){
                DataSlicePropertyNameMapperWidget currentEntityAttributeNameMapperWidget = new DataSlicePropertyNameMapperWidget(currentAttribute,existingKindAttributesList);
                add(currentEntityAttributeNameMapperWidget);
            }
        }
    }

    public Map<String,String> getAttributesMapping(){
        Map<String,String> attributesMapping = new HashMap<>();
        this.getChildren().forEach(new Consumer<Component>() {
            @Override
            public void accept(Component component) {
                if(component instanceof DataSlicePropertyNameMapperWidget){
                    DataSlicePropertyNameMapperWidget currentEntityAttributeNameMapperWidget = (DataSlicePropertyNameMapperWidget)component;
                    attributesMapping.put(currentEntityAttributeNameMapperWidget.getAttributeName(),currentEntityAttributeNameMapperWidget.getAttributeMapping());
                }
            }
        });
        return attributesMapping;
    }
}
