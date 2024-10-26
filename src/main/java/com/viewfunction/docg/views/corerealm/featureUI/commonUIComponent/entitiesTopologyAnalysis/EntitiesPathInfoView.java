package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EntitiesPathInfoView extends VerticalLayout {

    public EntitiesPathInfoView(EntitiesPath entitiesPath,int viewHeight) {
        this.setWidth(450, Unit.PIXELS);
        this.setHeight(viewHeight-10, Unit.PIXELS);

        NativeLabel pathJumpValue = new NativeLabel("路径跳数："+entitiesPath.getPathJumps());
        pathJumpValue.addClassNames("text-xs","font-bold");
        pathJumpValue.getStyle().set("padding-right","10px");

        List<Component> pathActionComponents = new LinkedList<>();
        pathActionComponents.add(pathJumpValue);
        SectionActionBar infoTitle = new SectionActionBar(LineAwesomeIconsSvg.PROJECT_DIAGRAM_SOLID.create(),"概念实体路径信息",pathActionComponents);
        add(infoTitle);

        VerticalLayout pathMetaInfoContainer = new VerticalLayout();
        pathMetaInfoContainer.setMargin(false);
        pathMetaInfoContainer.setPadding(false);
        pathMetaInfoContainer.setSpacing(false);

        LinkedList<ConceptionEntity> pathConceptionEntitiesList = entitiesPath.getPathConceptionEntities();

        List<Component> startEntityTitleComponentsList = new LinkedList<>();
        FootprintMessageBar startConceptionEntityInfo = getPathConceptionEntityInfo(pathConceptionEntitiesList.getFirst());
        startEntityTitleComponentsList.add(startConceptionEntityInfo);
        SecondaryTitleActionBar startEntitySecondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.SUN_RISE.create(),"起点：",startEntityTitleComponentsList,null,false);
        pathMetaInfoContainer.add(startEntitySecondaryTitleActionBar);

        List<Component> endEntityTitleComponentsList = new LinkedList<>();
        FootprintMessageBar endConceptionEntityInfo = getPathConceptionEntityInfo(pathConceptionEntitiesList.getLast());
        endEntityTitleComponentsList.add(endConceptionEntityInfo);

        SecondaryTitleActionBar endEntitySecondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.SUN_DOWN.create(),"终点：",endEntityTitleComponentsList,null,true);
        pathMetaInfoContainer.add(endEntitySecondaryTitleActionBar);

        add(pathMetaInfoContainer);
    }

    private FootprintMessageBar getPathConceptionEntityInfo(ConceptionEntity conceptionEntity){
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,conceptionEntity.getConceptionKindName()));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,conceptionEntity.getConceptionEntityUID()));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList,true);
        entityInfoFootprintMessageBar.getStyle().set("font-size","10px");

        Icon eyeIcon = new Icon(VaadinIcon.EYE);
        eyeIcon.setSize("20px");
        Button displayEntityDetailButton = new Button(eyeIcon, event -> {
            renderConceptionEntityUI(conceptionEntity.getConceptionKindName(),conceptionEntity.getConceptionEntityUID());
        });
        displayEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        displayEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        displayEntityDetailButton.setTooltipText("显示概念实体详情");
        entityInfoFootprintMessageBar.addAdditionalComponent(displayEntityDetailButton);

        return entityInfoFootprintMessageBar;
    }

    private void renderConceptionEntityUI(String conceptionKindName,String conceptionEntityUID){
        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(conceptionKindName,conceptionEntityUID);

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
        NativeLabel conceptionKindNameLabel = new NativeLabel(conceptionKindName);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(conceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }
}
