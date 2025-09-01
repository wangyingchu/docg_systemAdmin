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
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.ConceptionKindDetailUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConceptionDataRealtimeInfoWidget extends VerticalLayout {

    private IntelligentAnalysisView containerIntelligentAnalysisView;

    public ConceptionDataRealtimeInfoWidget(IntelligentAnalysisView containerIntelligentAnalysisView){
        this.getStyle().set("background-color", "white");
        this.setSpacing(false);
        this.containerIntelligentAnalysisView = containerIntelligentAnalysisView;
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

                Icon configIcon = new Icon(VaadinIcon.COG);
                configIcon.setSize("18px");
                Button configConceptionKind = new Button(configIcon, event -> {
                    renderConceptionKindConfigurationUI(conceptionKindName);
                });
                configConceptionKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_CONTRAST);
                configConceptionKind.setTooltipText("配置概念类型定义");

                Icon configIcon2 = LineAwesomeIconsSvg.AUDIBLE.create();
                configIcon2.setSize("16px");
                Button addToInsightScope = new Button(configIcon2, event -> {
                    containerIntelligentAnalysisView.addConceptionKindToInsightScope(conceptionKindName);
                });
                addToInsightScope.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_CONTRAST);
                addToInsightScope.setTooltipText("加入知识洞察范围");

                if(isSystemKind){
                    horizontalLayout.add(conceptionKindIcon,conceptionKindNameLabel,isInnerKindIcon,conceptionEntitiesCountBadge,configConceptionKind,addToInsightScope);
                }else{
                    horizontalLayout.add(conceptionKindIcon,conceptionKindNameLabel,conceptionEntitiesCountBadge,configConceptionKind,addToInsightScope);
                }

                Details conceptionKindAttributesDetails = new Details(horizontalLayout);
                conceptionKindAttributesDetails.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
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

    private void renderConceptionKindConfigurationUI(String targetConceptionKindName){
        ConceptionKindDetailUI conceptionKindDetailUI = new ConceptionKindDetailUI(targetConceptionKindName);
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

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindName = new NativeLabel(targetConceptionKindName);
        titleDetailLayout.add(conceptionKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"概念类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionKindDetailUI);
        fullScreenWindow.show();
    }
}
