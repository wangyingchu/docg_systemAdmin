package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.List;

public class ContainerAttributesViewKindRuntimeConfigurationInfoView extends VerticalLayout {

    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private ContainerAttributesViewKindsConfigView containerAttributesViewKindsConfigView;
    private SecondaryTitleActionBar selectedAttributesViewKindTitleActionBar;
    private SecondaryTitleActionBar selectedAttributesViewKindUIDActionBar;
    private String attributeKindUID;
    private Grid<AttributeKind> attributeKindAttributesInfoGrid;
    private Grid<ConceptionKind> conceptionKindAttributesInfoGrid;

    public ContainerAttributesViewKindRuntimeConfigurationInfoView(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;

        setSpacing(false);
        setMargin(false);
        setPadding(false);
        this.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setSpacing(false);
        mainContainerLayout.setMargin(false);
        mainContainerLayout.setPadding(false);
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setWidth(750,Unit.PIXELS);
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setMargin(false);
        leftSideContainerLayout.setPadding(false);
        mainContainerLayout.add(leftSideContainerLayout);

        ContainerAttributesViewKindsConfigView.ContainerAttributesViewKindSelectedListener
                containerAttributesViewKindSelectedListener = new ContainerAttributesViewKindsConfigView.ContainerAttributesViewKindSelectedListener() {
            @Override
            public void attributesViewKindSelectedAction(AttributesViewKind selectedAttributesViewKind) {
                renderAttributesViewKindOverview(selectedAttributesViewKind);
            }
        };
        containerAttributesViewKindsConfigView = new ContainerAttributesViewKindsConfigView(this.attributeKindUID);
        containerAttributesViewKindsConfigView.setContainerAttributesViewKindSelectedListener(containerAttributesViewKindSelectedListener);

        leftSideContainerLayout.add(containerAttributesViewKindsConfigView);

        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setWidth(400, Unit.PIXELS);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);
        mainContainerLayout.add(rightSideContainerLayout);

        HorizontalLayout spaceDiv01Layout1 = new HorizontalLayout();
        spaceDiv01Layout1.setHeight(10,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout1);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"被选择属性视图类型概览");
        rightSideContainerLayout.add(filterTitle);

        HorizontalLayout spaceDiv01Layout2 = new HorizontalLayout();
        spaceDiv01Layout2.setHeight(2,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout2);

        selectedAttributesViewKindTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TASKS),"-",null,null,false);
        selectedAttributesViewKindTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.add(selectedAttributesViewKindTitleActionBar);

        selectedAttributesViewKindUIDActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.KEY_O),"-",null,null);
        selectedAttributesViewKindUIDActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.add(selectedAttributesViewKindUIDActionBar);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CUBE),"包含被选择属性视图类型的概念类型");
        rightSideContainerLayout.add(infoTitle2);

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.setHeight(200,Unit.PIXELS);
        conceptionKindAttributesInfoGrid.addColumn(ConceptionKind::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(ConceptionKind::getConceptionKindDesc).setHeader("概念类型显示名称").setKey("idx_1");
        LightGridColumnHeader gridColumnHeader0_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"概念类型名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader0_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader1_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"概念类型显示名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader1_idx1).setSortable(true);
        rightSideContainerLayout.add(conceptionKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.INPUT),"被选择属性视图类型包含的属性类型");
        rightSideContainerLayout.add(infoTitle1);

        attributeKindAttributesInfoGrid = new Grid<>();
        attributeKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        attributeKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        attributeKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        attributeKindAttributesInfoGrid.addColumn(AttributeKind::getAttributeKindName).setHeader("属性类型名称").setKey("idx_0");
        attributeKindAttributesInfoGrid.addColumn(AttributeKind::getAttributeKindUID).setHeader("属性类型 UID").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);
        attributeKindAttributesInfoGrid.addColumn(AttributeKind::getAttributeDataType).setHeader("数据数据类型").setKey("idx_2").setFlexGrow(0).setWidth("150px").setResizable(false);
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性类型名称");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.KEY_O,"属性类型 UID");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"数据数据类型");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        rightSideContainerLayout.add(attributeKindAttributesInfoGrid);
        // need use this layout to keep attributeKindAttributesInfoGrid not extends too long
        HorizontalLayout spaceDiv01Layout3 = new HorizontalLayout();
        spaceDiv01Layout3.setHeight(5,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout3);
    }

    public void setViewHeight(int viewHeight){
        containerAttributesViewKindsConfigView.setViewHeight(viewHeight);
        attributeKindAttributesInfoGrid.setHeight(viewHeight-500,Unit.PIXELS);
    }

    public void setViewWidth(int viewWidth){
        rightSideContainerLayout.setWidth(viewWidth-550,Unit.PIXELS);
    }

    private void renderAttributesViewKindOverview(AttributesViewKind attributesViewKind){
        String attributesViewKindName = attributesViewKind.getAttributesViewKindName();
        String attributesViewKindDesc = attributesViewKind.getAttributesViewKindDesc() != null ?
                attributesViewKind.getAttributesViewKindDesc():"未设置描述信息";
        String attributesViewKindUID = attributesViewKind.getAttributesViewKindUID();
        String attributeNameText = attributesViewKindName +" ( "+attributesViewKindDesc+" )";
        selectedAttributesViewKindTitleActionBar.updateTitleContent(attributeNameText);
        selectedAttributesViewKindUIDActionBar.updateTitleContent(attributesViewKindUID);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind selectedAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindUID);
        List<ConceptionKind> conceptionKindList = selectedAttributesViewKind.getContainerConceptionKinds();
        if(conceptionKindList != null){
            conceptionKindAttributesInfoGrid.setItems(conceptionKindList);
        }
        List<AttributeKind> containsAttributeKindList = selectedAttributesViewKind.getContainsAttributeKinds();
        if(containsAttributeKindList != null){
            attributeKindAttributesInfoGrid.setItems(containsAttributeKindList);
        }
        coreRealm.closeGlobalSession();
    }
}
