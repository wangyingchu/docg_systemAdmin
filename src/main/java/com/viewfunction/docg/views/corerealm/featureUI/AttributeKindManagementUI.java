package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;
import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.CreateAttributeKindView;

import java.util.ArrayList;
import java.util.List;

public class AttributeKindManagementUI extends VerticalLayout {

    public AttributeKindManagementUI(){

        Button refreshDataButton = new Button("刷新属性类型数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            //loadConceptionKindsInfo();
            //resetSingleConceptionKindSummaryInfoArea();
        });

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();

        Icon realmIcon = VaadinIcon.ARCHIVE.create();
        realmIcon.getStyle().set("padding", "var(--lumo-space-xs");
        Span realmNameSpan = new Span( realmIcon,new Span("Default CoreRealm"));
        realmNameSpan.addClassName("text-2xs");
        realmNameSpan.getElement().getThemeList().add("badge contrast");
        secTitleElementsList.add(realmNameSpan);

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Attribute Kind 属性类型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> attributeKindManagementOperationButtonList = new ArrayList<>();

        Button attributeKindRuntimeStatusGuideButton = new Button("实体属性分布概览",new Icon(VaadinIcon.DASHBOARD));
        attributeKindRuntimeStatusGuideButton.setDisableOnClick(true);
        attributeKindRuntimeStatusGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        attributeKindRuntimeStatusGuideButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindManagementOperationButtonList.add(attributeKindRuntimeStatusGuideButton);
        attributeKindRuntimeStatusGuideButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderConceptionKindsCorrelationInfoSummaryUI(attributeKindRuntimeStatusGuideButton);
            }
        });

        Button createAttributeKindButton = new Button("创建属性类型",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindManagementOperationButtonList.add(createAttributeKindButton);
        createAttributeKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddAttributeKindView();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"属性类型定义:",attributeKindManagementOperationButtonList);
        add(sectionActionBar);
    }

    private void renderAddAttributeKindView(){
        CreateAttributeKindView createAttributeKindView = new CreateAttributeKindView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.PLUS_SQUARE_O.create(),"创建属性类型",null,true,500,350,false);
        fixSizeWindow.setWindowContent(createAttributeKindView);
        createAttributeKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
