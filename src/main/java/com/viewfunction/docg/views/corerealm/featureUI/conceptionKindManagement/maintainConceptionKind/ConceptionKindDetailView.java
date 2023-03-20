package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.ConceptionKindCorrelationInfoChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.ConceptionKind;

@Route("conceptionKindDetailInfo/:conceptionKind")
public class ConceptionKindDetailView extends VerticalLayout implements BeforeEnterObserver {
    private String conceptionKind;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private int conceptionKindDetailViewHeightOffset = 135;
    private Grid<KindEntityAttributeRuntimeStatistics> conceptionKindAttributesInfoGrid;
    private ConceptionKindCorrelationInfoChart conceptionKindCorrelationInfoChart;
    private VerticalLayout leftSideContainerLayout;

    public ConceptionKindDetailView(){}

    public ConceptionKindDetailView(String conceptionKind){
        this.conceptionKind = conceptionKind;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.conceptionKind = beforeEnterEvent.getRouteParameters().get("conceptionKind").get();
        this.conceptionKindDetailViewHeightOffset = 70;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderConceptionKindData();
        loadConceptionKindInfoData();
    }

    private void renderConceptionKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();

        Label conceptionKindNameLabel = new Label(this.conceptionKind);
        conceptionKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(conceptionKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.conceptionKind,ConceptionKind);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

        Button conceptionKindMetaInfoButton= new Button("概念类型元数据");
        conceptionKindMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        conceptionKindMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        buttonList.add(conceptionKindMetaInfoButton);

        conceptionKindMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

                conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(500);
                leftSideContainerLayout.add(conceptionKindCorrelationInfoChart);

                CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                coreRealm.openGlobalSession();
                com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKind);
                List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetConceptionKind.statisticEntityAttributesDistribution(100000);
                Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = targetConceptionKind.getKindRelationDistributionStatistics();
                coreRealm.closeGlobalSession();

                conceptionKindCorrelationInfoChart.clearData();
                conceptionKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoSet,conceptionKind);

            }
        });

        MenuBar importMenuBar = new MenuBar();
        importMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem importDataMenu = createIconItem(importMenuBar, VaadinIcon.DOWNLOAD, "导入概念实体数据", null);
        SubMenu importSubItems = importDataMenu.getSubMenu();
        MenuItem csvImportItem = importSubItems.addItem("CSV 格式数据");
        MenuItem arrowImportItem = importSubItems.addItem("ARROW 格式数据");
        buttonList.add(importMenuBar);

        MenuBar exportMenuBar = new MenuBar();
        exportMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem exportDataMenu = createIconItem(exportMenuBar, VaadinIcon.UPLOAD, "导出概念实体数据", null);
        SubMenu exportSubItems = exportDataMenu.getSubMenu();
        MenuItem csvExportItem = exportSubItems.addItem("CSV 格式数据");
        MenuItem arrowExportItem = exportSubItems.addItem("ARROW 格式数据");
        buttonList.add(exportMenuBar);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"Conception Kind 概念类型  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(800, Unit.PIXELS);
        leftSideContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");
        VerticalLayout rightSideContainerLayout = new VerticalLayout();
        mainContainerLayout.add(rightSideContainerLayout);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"概念类型属性分布 (实体概略采样数 "+100000+")");
        leftSideContainerLayout.add(infoTitle1);

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);
        conceptionKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getSampleCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
                .setHeader("属性采样数").setKey("idx_2")
                .setFlexGrow(0).setWidth("100px").setResizable(false);
        conceptionKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getAttributeHitCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
                .setHeader("属性命中数").setKey("idx_3")
                .setFlexGrow(0).setWidth("100px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.EYEDROPPER,"属性采样数");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"属性命中数");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        conceptionKindAttributesInfoGrid.setHeight(300,Unit.PIXELS);

        leftSideContainerLayout.add(conceptionKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT),"概念类型实体关联分布");
        leftSideContainerLayout.add(infoTitle2);
        this.conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(500);




        leftSideContainerLayout.add(this.conceptionKindCorrelationInfoChart);
    }

    private void loadConceptionKindInfoData(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetConceptionKind.statisticEntityAttributesDistribution(100000);
        Set<ConceptionKindCorrelationInfo> conceptionKindCorrelationInfoSet = targetConceptionKind.getKindRelationDistributionStatistics();
        coreRealm.closeGlobalSession();

        conceptionKindAttributesInfoGrid.setItems(kindEntityAttributeRuntimeStatisticsList);

        //this.conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(500);
        //leftSideContainerLayout.add(conceptionKindCorrelationInfoChart);

        //conceptionKindCorrelationInfoChart.clearData();
        //conceptionKindCorrelationInfoChart.setData(conceptionKindCorrelationInfoSet,this.conceptionKind);




    }














    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel) {
        return createIconItem(menu, iconName, label, ariaLabel, false);
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,String label, String ariaLabel, boolean isChild) {
        Icon icon = new Icon(iconName);
        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }
        MenuItem item = menu.addItem(icon, e -> {
        });
        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }
        if (label != null) {
            item.add(new Text(label));
        }
        return item;
    }
}
