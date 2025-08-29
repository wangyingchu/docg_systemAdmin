package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeSystemInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind.RelationKindDetailUI;

import java.util.ArrayList;
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
                isInnerKindIcon.setTooltipText("系统内部关系类型");

                Icon configIcon = new Icon(VaadinIcon.COG);
                configIcon.setSize("18px");
                Button configRelationKind = new Button(configIcon, event -> {
                    renderRelationKindConfigurationUI(relationKindName);
                });
                configRelationKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_CONTRAST);
                configRelationKind.setTooltipText("配置关系类型定义");

                Icon configIcon2 = LineAwesomeIconsSvg.AUDIBLE.create();
                configIcon2.setSize("16px");
                Button addToInsightScope = new Button(configIcon2, event -> {
                    //renderConceptionKindConfigurationUI(conceptionKindName);
                });
                addToInsightScope.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_CONTRAST);
                addToInsightScope.setTooltipText("加入知识洞察范围");

                HorizontalLayout horizontalLayout = new HorizontalLayout();
                horizontalLayout.setAlignItems(Alignment.CENTER);

                if(isSystemKind){
                    horizontalLayout.add(relationKindIcon,relationKindNameLabel,isInnerKindIcon,relationEntitiesCountBadge,configRelationKind,addToInsightScope);
                }else{
                    horizontalLayout.add(relationKindIcon,relationKindNameLabel,relationEntitiesCountBadge,configRelationKind,addToInsightScope);
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

    private void renderRelationKindConfigurationUI(String targetRelationKindName){
        RelationKindDetailUI relationKindDetailUI = new RelationKindDetailUI(targetRelationKindName);
        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8, Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("10px");
        titleDetailLayout.add(relationKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel relationKindName = new NativeLabel(targetRelationKindName);
        titleDetailLayout.add(relationKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"关系类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationKindDetailUI);
        fullScreenWindow.show();
    }
}
