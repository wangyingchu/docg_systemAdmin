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

public class RelationDataRealtimeInfoWidget extends VerticalLayout {

    public RelationDataRealtimeInfoWidget(){
        this.getStyle().set("background-color", "white");
        this.setSpacing(false);
    }

    public void  renderRelationDataRealtimeInfo(List<EntityStatisticsInfo> realtimeRelationList,
                                                Map<String, List<AttributeSystemInfo>> relationKindsAttributesSystemInfo){
        if(realtimeRelationList != null && relationKindsAttributesSystemInfo != null){
            for(EntityStatisticsInfo currentEntityStatisticsInfo:realtimeRelationList){
                String relationKindName = currentEntityStatisticsInfo.getEntityKindName();
                String relationKindDesc = currentEntityStatisticsInfo.getEntityKindDesc();
                long relationEntitiesCount = currentEntityStatisticsInfo.getEntitiesCount();
                boolean isSystemKind = currentEntityStatisticsInfo.isSystemKind();

                Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
                relationKindIcon.setSize("12px");
                relationKindIcon.getStyle().set("padding-right","3px");

                NativeLabel relationKindNameLabel = relationKindDesc != null?
                        new NativeLabel(relationKindName+"("+relationKindDesc+")")
                        :
                        new NativeLabel(relationKindName);

                Span relationEntitiesCountBadge = new Span("" +relationEntitiesCount);
                relationEntitiesCountBadge.getElement().getThemeList().add("badge success");

                Icon isInnerKindIcon = LineAwesomeIconsSvg.COG_SOLID.create();
                isInnerKindIcon.setSize("6px");
                relationKindIcon.getStyle().set("padding-right","3px");
                isInnerKindIcon.setTooltipText("系统内部概念类型");

                HorizontalLayout horizontalLayout = new HorizontalLayout();
                horizontalLayout.setAlignItems(Alignment.CENTER);

                if(isSystemKind){
                    horizontalLayout.add(relationKindIcon,relationKindNameLabel,isInnerKindIcon,relationEntitiesCountBadge);
                }else{
                    horizontalLayout.add(relationKindIcon,relationKindNameLabel,relationEntitiesCountBadge);
                }

                Details relationKindAttributesDetails = new Details(horizontalLayout);
                relationKindAttributesDetails.setOpened(false);
                add(relationKindAttributesDetails);

                if(relationKindsAttributesSystemInfo.containsKey(relationKindName)){
                    List<AttributeSystemInfo> currentRelationKindAttributesSysInfo = relationKindsAttributesSystemInfo.get(relationKindName);

                    for(AttributeSystemInfo currentAttributeSystemInfo :currentRelationKindAttributesSysInfo){
                        String attributeName = currentAttributeSystemInfo.getAttributeName();
                        String dataType = currentAttributeSystemInfo.getDataType();

                        Span attributeInfo = new Span(attributeName +" ("+dataType+")");
                        attributeInfo.getElement().getThemeList().add("badge contrast");
                        relationKindAttributesDetails.add(attributeInfo);

                        Span spaceDivSpan = new Span(" ");
                        spaceDivSpan.getElement().getStyle().set("padding-right","5px");
                        relationKindAttributesDetails.add(spaceDivSpan);
                    }
                }
            }
        }
    }
}
