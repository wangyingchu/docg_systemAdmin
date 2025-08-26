package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeSystemInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.List;
import java.util.Map;

public class ConceptionDataRealtimeInfoWidget extends VerticalLayout {

    public ConceptionDataRealtimeInfoWidget(){
        this.getStyle().set("background-color", "white");
        this.setSpacing(false);
    }

    public void renderConceptionDataRealtimeInfo(List<EntityStatisticsInfo> realtimeConceptionList,
                                                 Map<String, List<AttributeSystemInfo>> conceptionKindsAttributesSystemInfo){
        if(realtimeConceptionList != null && conceptionKindsAttributesSystemInfo != null){
            for(EntityStatisticsInfo currentEntityStatisticsInfo:realtimeConceptionList){
                String conceptionKindName = currentEntityStatisticsInfo.getEntityKindName();
                String conceptionKindDesc = currentEntityStatisticsInfo.getEntityKindDesc();
                long conceptionEntitiesCount = currentEntityStatisticsInfo.getEntitiesCount();
                boolean isSystemKind = currentEntityStatisticsInfo.isSystemKind();

                Icon conceptionKindIcon = VaadinIcon.CUBE.create();
                conceptionKindIcon.setSize("12px");
                conceptionKindIcon.getStyle().set("padding-right","3px");

                NativeLabel conceptionKindNameLabel = conceptionKindDesc != null?
                        new NativeLabel(conceptionKindName+"("+conceptionKindDesc+")")
                        :
                        new NativeLabel(conceptionKindName);

                Span conceptionEntitiesCountBadge = new Span("" +conceptionEntitiesCount);
                conceptionEntitiesCountBadge.getElement().getThemeList().add("badge success");

                Icon isInnerKindIcon = LineAwesomeIconsSvg.COG_SOLID.create();
                isInnerKindIcon.setSize("6px");
                conceptionKindIcon.getStyle().set("padding-right","3px");
                isInnerKindIcon.setTooltipText("系统内部概念类型");

                HorizontalLayout horizontalLayout = new HorizontalLayout();
                horizontalLayout.setAlignItems(Alignment.CENTER);

                if(isSystemKind){
                    horizontalLayout.add(conceptionKindIcon,conceptionKindNameLabel,isInnerKindIcon,conceptionEntitiesCountBadge);
                }else{
                    horizontalLayout.add(conceptionKindIcon,conceptionKindNameLabel,conceptionEntitiesCountBadge);
                }

                Details conceptionKindAttributesDetails = new Details(horizontalLayout);
                conceptionKindAttributesDetails.setOpened(false);
                add(conceptionKindAttributesDetails);

                if(conceptionKindsAttributesSystemInfo.containsKey(conceptionKindName)){
                    List<AttributeSystemInfo> currentConceptionKindAttributesSysInfo = conceptionKindsAttributesSystemInfo.get(conceptionKindName);

                    for(AttributeSystemInfo currentAttributeSystemInfo :currentConceptionKindAttributesSysInfo){
                        String attributeName = currentAttributeSystemInfo.getAttributeName();
                        String dataType = currentAttributeSystemInfo.getDataType();

                        Span attributeInfo = new Span(attributeName +" ("+dataType+")");
                        attributeInfo.getElement().getThemeList().add("badge contrast");
                        conceptionKindAttributesDetails.add(attributeInfo);

                        Span spaceDivSpan = new Span(" ");
                        spaceDivSpan.getElement().getStyle().set("padding-right","5px");
                        conceptionKindAttributesDetails.add(spaceDivSpan);
                    }
                }
            }
        }
    }
}
