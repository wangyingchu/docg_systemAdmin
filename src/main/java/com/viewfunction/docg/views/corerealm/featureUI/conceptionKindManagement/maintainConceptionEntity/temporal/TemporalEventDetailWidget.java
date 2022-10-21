package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEvent;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.common.AttributeValueInfoWidget;

import java.util.List;

public class TemporalEventDetailWidget extends VerticalLayout {
    private TimeScaleEvent timeScaleEvent;
    private TimeScaleEntity timeScaleEntity;

    public TemporalEventDetailWidget(TimeScaleEvent timeScaleEvent, TimeScaleEntity timeScaleEntity){
        this.timeScaleEvent = timeScaleEvent;
        this.timeScaleEntity = timeScaleEntity;

        List<AttributeValue> attributeValueList = timeScaleEvent.getAttributes();
        if(attributeValueList != null){
            for(AttributeValue currentAttributeValue:attributeValueList){
                AttributeValueInfoWidget attributeValueInfoWidget = new AttributeValueInfoWidget(currentAttributeValue);
                add(attributeValueInfoWidget);
            }
        }
    }
}
