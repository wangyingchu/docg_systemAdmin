package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;

import com.viewfunction.docg.element.eventHandling.AttributesViewKindAttachedToConceptionKindEvent;
import com.viewfunction.docg.element.eventHandling.AttributesViewKindDetachedFromConceptionKindEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ContainerConceptionKindsConfigView extends VerticalLayout implements
        AttributesViewKindAttachedToConceptionKindEvent.AttributesViewKindAttachedToConceptionKindListener,
        AttributesViewKindDetachedFromConceptionKindEvent.AttributesViewKindDetachedFromConceptionKindListener{
    private String attributesViewKindUID;
    private Grid<ConceptionKind> conceptionKindGrid;
    private int containerHeight;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private SecondaryTitleActionBar selectedConceptionKindTitleActionBar;
    private Grid<AttributesViewKind> attributesViewKindAttributesInfoGrid;
    private Registration listener;
    private ConceptionKind lastSelectedConceptionKind;
    private Grid<KindEntityAttributeRuntimeStatistics> conceptionKindAttributesInfoGrid;
    public ContainerConceptionKindsConfigView(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;
        this.setWidth(100, Unit.PERCENTAGE);
        setSpacing(false);
        setMargin(false);
        setPadding(false);
        this.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setMargin(false);
        mainContainerLayout.setPadding(false);
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setWidth(850,Unit.PIXELS);
        leftSideContainerLayout.setMargin(false);
        leftSideContainerLayout.setPadding(false);
        mainContainerLayout.add(leftSideContainerLayout);

        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setWidth(400, Unit.PIXELS);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);
        mainContainerLayout.add(rightSideContainerLayout);

        HorizontalLayout spaceDiv01Layout0 = new HorizontalLayout();
        spaceDiv01Layout0.setHeight(1,Unit.PIXELS);
        leftSideContainerLayout.add(spaceDiv01Layout0);

        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button createMetaConfigItemButton= new Button("附加新的概念类型");
        createMetaConfigItemButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        createMetaConfigItemButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        createMetaConfigItemButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAttachNewConceptionKindUI();
            }
        });
        buttonList.add(createMetaConfigItemButton);

        Button refreshMetaConfigItemsInfoButton = new Button("刷新应用的概念类型信息",new Icon(VaadinIcon.REFRESH));
        refreshMetaConfigItemsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshMetaConfigItemsInfoButton.addClickListener((ClickEvent<Button> click) ->{
            refreshConceptionKindsInfo();
        });
        buttonList.add(refreshMetaConfigItemsInfoButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"概念类型配置管理 ",secTitleElementsList,buttonList);
        leftSideContainerLayout.add(metaConfigItemConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(conceptionKind -> {
            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeAttributeKind = new Button(deleteKindIcon, event -> {});
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeAttributeKind.setTooltipText("移除与概念类型的关联");
            removeAttributeKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionKind instanceof ConceptionKind){
                        renderDetachConceptionKindUI((ConceptionKind)conceptionKind);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(removeAttributeKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        conceptionKindGrid = new Grid<>();
        conceptionKindGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionKindGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindGrid.addColumn(ConceptionKind::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getConceptionKindName());
        conceptionKindGrid.addColumn(ConceptionKind::getConceptionKindDesc).setHeader("概念类型描述").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getConceptionKindDesc());
        conceptionKindGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("90px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"概念类型名称");
        conceptionKindGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"概念类型描述");
        conceptionKindGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2);

        conceptionKindGrid.appendFooterRow();
        conceptionKindGrid.addSelectionListener(new SelectionListener<Grid<ConceptionKind>, ConceptionKind>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ConceptionKind>, ConceptionKind> selectionEvent) {
                Set<ConceptionKind> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    conceptionKindGrid.select(lastSelectedConceptionKind);
                }else{
                    ConceptionKind selectedConceptionKind = selectedItemSet.iterator().next();
                    renderConceptionKindKindOverview(selectedConceptionKind);
                    lastSelectedConceptionKind = selectedConceptionKind;
                }
            }
        });
        leftSideContainerLayout.add(conceptionKindGrid);

        HorizontalLayout spaceDiv01Layout1 = new HorizontalLayout();
        spaceDiv01Layout1.setHeight(10,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout1);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"被选择概念类型概览");
        rightSideContainerLayout.add(filterTitle);

        HorizontalLayout spaceDiv01Layout2 = new HorizontalLayout();
        spaceDiv01Layout2.setHeight(2,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout2);

        selectedConceptionKindTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"-",null,null);
        selectedConceptionKindTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.add(selectedConceptionKindTitleActionBar);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.TASKS),"包含的属性视图类型");
        rightSideContainerLayout.add(infoTitle1);

        attributesViewKindAttributesInfoGrid = new Grid<>();
        attributesViewKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        attributesViewKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        attributesViewKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        attributesViewKindAttributesInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindName).setHeader("属性视图名称").setKey("idx_0");
        attributesViewKindAttributesInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindUID).setHeader("属性视图 UID").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);
        attributesViewKindAttributesInfoGrid.addColumn(AttributesViewKind::getAttributesViewKindDataForm).setHeader("数据存储结构").setKey("idx_2").setFlexGrow(0).setWidth("150px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性视图类型名称");
        attributesViewKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.KEY_O,"属性视图类型 UID");
        attributesViewKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.COMBOBOX,"视图数据存储结构");
        attributesViewKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        attributesViewKindAttributesInfoGrid.setHeight(200,Unit.PIXELS);
        rightSideContainerLayout.add(attributesViewKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"概念类型属性分布 (实体概略采样数 "+10000+")");
        rightSideContainerLayout.add(infoTitle2);

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.setHeight(200,Unit.PIXELS);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("130px").setResizable(false);
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

        LightGridColumnHeader gridColumnHeader_1_idx0A = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0A).setSortable(true)
                .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> getAttributeName(kindEntityAttributeRuntimeStatistics));
        LightGridColumnHeader gridColumnHeader_1_idx1A = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1A).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2A = new LightGridColumnHeader(VaadinIcon.EYEDROPPER,"属性采样数");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2A).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3A = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"属性命中数");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3A).setSortable(true);
        rightSideContainerLayout.add(conceptionKindAttributesInfoGrid);

        //need use this layout to keep attributeKindAttributesInfoGrid not extends too long
        HorizontalLayout spaceDiv01Layout3 = new HorizontalLayout();
        spaceDiv01Layout3.setHeight(1,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout3);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        List<ConceptionKind> conceptionKindsList = targetAttributesViewKind.getContainerConceptionKinds();
        conceptionKindGrid.setItems(conceptionKindsList);
        coreRealm.closeGlobalSession();

        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.rightSideContainerLayout.setWidth(event.getWidth()-1450,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            this.rightSideContainerLayout.setWidth(receiver.getBodyClientWidth()-1450,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderAttachNewConceptionKindUI(){
        AttachNewConceptionKindView attachNewConceptionKindView = new AttachNewConceptionKindView(this.attributesViewKindUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"附加概念类型",null,true,490,200,false);
        fixSizeWindow.setWindowContent(attachNewConceptionKindView);
        fixSizeWindow.setModel(true);
        attachNewConceptionKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderDetachConceptionKindUI(ConceptionKind attributeKind){
        DetachConceptionKindView detachConceptionKindView = new DetachConceptionKindView(this.attributesViewKindUID,attributeKind);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"移除概念类型",null,true,600,210,false);
        fixSizeWindow.setWindowContent(detachConceptionKindView);
        fixSizeWindow.setModel(true);
        detachConceptionKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    public void setHeight(int heightValue){
        containerHeight = heightValue;
        this.conceptionKindGrid.setHeight(containerHeight-190,Unit.PIXELS);
    }

    public void refreshConceptionKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        List<ConceptionKind> conceptionKindsList = targetAttributesViewKind.getContainerConceptionKinds();
        conceptionKindGrid.setItems(conceptionKindsList);
        resetAttributesViewKindsInfo();
    }

    @Override
    public void receivedAttributesViewKindAttachedToConceptionKindEvent(AttributesViewKindAttachedToConceptionKindEvent event) {
        if(event.getAttributesViewKindUID() != null &&event.getConceptionKind() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                ListDataProvider dtaProvider=(ListDataProvider)conceptionKindGrid.getDataProvider();
                dtaProvider.getItems().add(event.getConceptionKind());
                dtaProvider.refreshAll();
            }
        }
    }

    @Override
    public void receivedAttributesViewKindDetachedFromConceptionKindEvent(AttributesViewKindDetachedFromConceptionKindEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getConceptionKindName() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                ListDataProvider dataProvider=(ListDataProvider)conceptionKindGrid.getDataProvider();
                Collection<ConceptionKind> itemsCollection = dataProvider.getItems();
                if(itemsCollection != null){
                    for(ConceptionKind currentConceptionKind:itemsCollection){
                        if(event.getConceptionKindName().equals(currentConceptionKind.getConceptionKindName())){
                            itemsCollection.remove(currentConceptionKind);
                            break;
                        }
                    }
                    dataProvider.refreshAll();
                }
            }
            if(lastSelectedConceptionKind != null && lastSelectedConceptionKind.getConceptionKindName().equals(event.getConceptionKindName())){
                resetAttributesViewKindsInfo();
            }
        }
    }

    private void  renderConceptionKindKindOverview(ConceptionKind selectedConceptionKind){
        String conceptionKindName = selectedConceptionKind.getConceptionKindName();
        String conceptionKindDesc = selectedConceptionKind.getConceptionKindDesc() != null ?
                selectedConceptionKind.getConceptionKindDesc():"未设置描述信息";
        String conceptionKindNameText = conceptionKindName +" ( "+conceptionKindDesc+" )";
        selectedConceptionKindTitleActionBar.updateTitleContent(conceptionKindNameText);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(selectedConceptionKind.getConceptionKindName());
        List<AttributesViewKind> containesAttributesViewKindList = targetConceptionKind.getContainsAttributesViewKinds();
        if(containesAttributesViewKindList != null) {
            attributesViewKindAttributesInfoGrid.setItems(containesAttributesViewKindList);
        }
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList = targetConceptionKind.statisticEntityAttributesDistribution(10000);
        if(kindEntityAttributeRuntimeStatisticsList != null){
            conceptionKindAttributesInfoGrid.setItems(kindEntityAttributeRuntimeStatisticsList);
        }
        coreRealm.closeGlobalSession();
    }

    private String getAttributeName(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics){
        return kindEntityAttributeRuntimeStatistics.getAttributeName();
    }

    private void resetAttributesViewKindsInfo(){
        this.conceptionKindAttributesInfoGrid.setItems(new ArrayList<>());
        this.attributesViewKindAttributesInfoGrid.setItems(new ArrayList<>());
        this.selectedConceptionKindTitleActionBar.updateTitleContent("-");
        this.lastSelectedConceptionKind = null;
    }
}
