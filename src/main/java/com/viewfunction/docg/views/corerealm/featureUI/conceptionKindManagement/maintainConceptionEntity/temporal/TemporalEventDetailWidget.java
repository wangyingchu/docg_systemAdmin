package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
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
                currentAttributeValue.getAttributeName();
                currentAttributeValue.getAttributeValue();
                currentAttributeValue.getAttributeDataType();
                Label label = new Label(currentAttributeValue.getAttributeName()+":"+currentAttributeValue.getAttributeValue());
                add(label);
                AttributeValueInfoWidget attributeValueInfoWidget = new AttributeValueInfoWidget();
                add(attributeValueInfoWidget);
            }
        }



        //List<Classification> classificationList = timeScaleEvent.getAttachedClassifications(null,null);
    }
}
