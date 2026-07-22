package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeKindMaintain;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.AttributeInConceptionKindDistributionInfoChart;

import java.util.ArrayList;
import java.util.List;

public class AttributeKindSummaryInfoView extends VerticalLayout {

    private SecondaryTitleActionBar secondaryTitleActionBar;
    private SecondaryTitleActionBar secondaryTitleActionBar2;
    private Grid<AttributesViewKind> attributeKindAttributesInfoGrid;
    private AttributeInConceptionKindDistributionInfoChart attributeInConceptionKindDistributionInfoChart;
    private AttributeKindMetaInfo attributeKindMetaInfo;

    public AttributeKindSummaryInfoView(AttributeKindMetaInfo attributeKindMetaInfo){
        setSpacing(true);
        setMargin(false);
        setPadding(true);
        this.attributeKindMetaInfo = attributeKindMetaInfo;
        HorizontalLayout singleAttributeKindInfoElementsContainerLayout = new HorizontalLayout();
        singleAttributeKindInfoElementsContainerLayout.setSpacing(false);
        singleAttributeKindInfoElementsContainerLayout.setMargin(false);
        singleAttributeKindInfoElementsContainerLayout.setHeight("30px");
        add(singleAttributeKindInfoElementsContainerLayout);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"属性类型概览");
        singleAttributeKindInfoElementsContainerLayout.add(filterTitle2);
        singleAttributeKindInfoElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle2);

        secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.INPUT),"-",null,null,false);
        secondaryTitleActionBar.setWidth(600, Unit.PIXELS);
        add(secondaryTitleActionBar);

        secondaryTitleActionBar2 = new SecondaryTitleActionBar(new Icon(VaadinIcon.KEY_O),"-",null,null);
        secondaryTitleActionBar2.setWidth(600,Unit.PIXELS);
        add(secondaryTitleActionBar2);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.TASKS),"所属属性视图类型");
        add(infoTitle1);

        attributeKindAttributesInfoGrid = new Grid<>();
        attributeKindAttributesInfoGrid.setWidth(600,Unit.PIXELS);
        attributeKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        attributeKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        attributeKindAttributesInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindName).setHeader("属性视图名称").setKey("idx_0");
        attributeKindAttributesInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindUID).setHeader("属性视图 UID").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);
        attributeKindAttributesInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindDataForm).setHeader("数据存储结构").setKey("idx_2").setFlexGrow(0).setWidth("150px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性视图类型名称");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.KEY_O,"属性视图类型 UID");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.COMBOBOX,"视图数据存储结构");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        attributeKindAttributesInfoGrid.setHeight(150,Unit.PIXELS);
        add(attributeKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.BAR_CHART_H),"属性类型在概念实体中的数据分布");
        add(infoTitle2);

        attributeInConceptionKindDistributionInfoChart = new AttributeInConceptionKindDistributionInfoChart();
        attributeInConceptionKindDistributionInfoChart.setChartSize(600,200);
        add(attributeInConceptionKindDistributionInfoChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        String attributeKindName = attributeKindMetaInfo.getKindName();
        String attributeDataType = attributeKindMetaInfo.getAttributeDataType();
        String attributeKindDesc = attributeKindMetaInfo.getKindDesc() != null ?
                attributeKindMetaInfo.getKindDesc():"未设置描述信息";
        String attributeKindUID = attributeKindMetaInfo.getKindUID();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        List<AttributesViewKind> containerAttributesViewKindsList = new ArrayList<>();
        AttributeKind attributeKind = coreRealm.getAttributeKind(attributeKindMetaInfo.getKindUID());
        if(attributeKind != null){
            containerAttributesViewKindsList.addAll(attributeKind.getContainerAttributesViewKinds());
            attributeKindAttributesInfoGrid.setItems(containerAttributesViewKindsList);
            attributeInConceptionKindDistributionInfoChart.refreshDistributionInfo(attributeKindMetaInfo.getKindUID());
        }
        coreRealm.closeGlobalSession();

        String attributeNameText = attributeKindName +" ( "+attributeKindDesc+" )";
        this.secondaryTitleActionBar.updateTitleContent(attributeNameText);
        String attributeKindIdText = attributeKindUID+ " - "+attributeDataType;
        this.secondaryTitleActionBar2.updateTitleContent(attributeKindIdText);
    }
}
