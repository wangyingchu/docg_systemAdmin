package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialScaleEvent;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.common.AttributeValueInfoWidget;

import java.util.List;

public class GeospatialEventDetailWidget extends VerticalLayout {

    private GeospatialScaleEvent geospatialScaleEvent;
    private GeospatialScaleEntity geospatialScaleEntity;

    public GeospatialEventDetailWidget(GeospatialScaleEvent geospatialScaleEvent, GeospatialScaleEntity geospatialScaleEntity){
        this.geospatialScaleEvent = geospatialScaleEvent;
        this.geospatialScaleEntity = geospatialScaleEntity;

        List<AttributeValue> attributeValueList = geospatialScaleEvent.getAttributes();
        if(attributeValueList != null){
            for(AttributeValue currentAttributeValue:attributeValueList){
                AttributeValueInfoWidget attributeValueInfoWidget = new AttributeValueInfoWidget(currentAttributeValue);
                add(attributeValueInfoWidget);
            }
        }
    }
}
