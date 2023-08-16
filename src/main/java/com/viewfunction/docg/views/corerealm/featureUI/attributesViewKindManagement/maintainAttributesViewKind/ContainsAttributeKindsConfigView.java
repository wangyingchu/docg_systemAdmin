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
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.AttributeKindAttachedToAttributesViewKindEvent;
import com.viewfunction.docg.element.eventHandling.AttributeKindDetachedFromAttributesViewKindEvent;
import com.viewfunction.docg.util.ResourceHolder;

import com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind.AttributeKindDetailUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContainsAttributeKindsConfigView extends VerticalLayout implements
        AttributeKindAttachedToAttributesViewKindEvent.AttributeKindAttachedToAttributesViewKindListener,
        AttributeKindDetachedFromAttributesViewKindEvent.AttributeKindDetachedFromAttributesViewKindListener{
    private String attributesViewKindUID;
    private Grid<AttributeKind> attributeKindGrid;
    private int containerHeight;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private SecondaryTitleActionBar selectedAttributesViewKindTitleActionBar;
    private SecondaryTitleActionBar selectedAttributesViewKindUIDActionBar;
    private Grid<AttributesViewKind> attributeKindAttributesInfoGrid;
    private Registration listener;
    //private Grid<> conceptionKindContainsAttributeInfoGrid;
    public ContainsAttributeKindsConfigView(String attributesViewKindUID){
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
        //leftSideContainerLayout.setSpacing(false);
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

        Button createMetaConfigItemButton= new Button("附加新的属性类型");
        createMetaConfigItemButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        createMetaConfigItemButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        createMetaConfigItemButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAttachNewAttributeKindUI();
            }
        });
        buttonList.add(createMetaConfigItemButton);

        Button refreshMetaConfigItemsInfoButton = new Button("刷新包含的属性类型信息",new Icon(VaadinIcon.REFRESH));
        refreshMetaConfigItemsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshMetaConfigItemsInfoButton.addClickListener((ClickEvent<Button> click) ->{
            refreshAttributeTypesInfo();
        });
        buttonList.add(refreshMetaConfigItemsInfoButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.INPUT),"属性类型配置管理 ",secTitleElementsList,buttonList);
        leftSideContainerLayout.add(metaConfigItemConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(attributeKind -> {
            Icon configAttachmentInfoIcon = new Icon(VaadinIcon.BULLETS);
            configAttachmentInfoIcon.setSize("18px");
            Button configAttachmentInfoButton = new Button(configAttachmentInfoIcon, event -> {});
            configAttachmentInfoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configAttachmentInfoButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

            Tooltips.getCurrent().setTooltip(configAttachmentInfoButton, "属性类型附加元数据配置管理");
            configAttachmentInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(attributeKind instanceof AttributeKind){
                        renderConfigAttributeKindAttachmentMetaDataUI((AttributeKind)attributeKind);
                    }
                }
            });

            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeAttributeKindButton = new Button(deleteKindIcon, event -> {});
            removeAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeAttributeKindButton, "移除属性类型");
            removeAttributeKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(attributeKind instanceof AttributeKind){
                        renderDetachAttributeKindUI((AttributeKind)attributeKind);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configAttachmentInfoButton,removeAttributeKindButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        attributeKindGrid = new Grid<>();
        attributeKindGrid.setWidth(100,Unit.PERCENTAGE);
        attributeKindGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        attributeKindGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributeKindGrid.addColumn(AttributeKind::getAttributeKindName).setHeader("属性类型名称").setKey("idx_0").setFlexGrow(1);
        attributeKindGrid.addColumn(AttributeKind::getAttributeKindDesc).setHeader("属性类型描述").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getAttributeKindDesc());
        attributeKindGrid.addColumn(AttributeKind::getAttributeDataType).setHeader("属性数据类型").setKey("idx_2")
                .setFlexGrow(0).setWidth("130px").setResizable(false);
        attributeKindGrid.addColumn(AttributeKind::getAttributeKindUID).setHeader("属性类型 UID").setKey("idx_3")
                .setFlexGrow(0).setWidth("150px").setResizable(false);
        attributeKindGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("90px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性类型名称");
        attributeKindGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"属性类型描述");
        attributeKindGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"属性数据类型");
        attributeKindGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.KEY_O,"属性类型 UID");
        attributeKindGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        attributeKindGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);

        attributeKindGrid.appendFooterRow();
        leftSideContainerLayout.add(attributeKindGrid);



        HorizontalLayout spaceDiv01Layout1 = new HorizontalLayout();
        spaceDiv01Layout1.setHeight(10,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout1);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"被选择属性类型概览");
        rightSideContainerLayout.add(filterTitle);

        HorizontalLayout spaceDiv01Layout2 = new HorizontalLayout();
        spaceDiv01Layout2.setHeight(2,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout2);

        selectedAttributesViewKindTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.INPUT),"-",null,null,false);
        selectedAttributesViewKindTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.add(selectedAttributesViewKindTitleActionBar);

        selectedAttributesViewKindUIDActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.KEY_O),"-",null,null);
        selectedAttributesViewKindUIDActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.add(selectedAttributesViewKindUIDActionBar);





        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.TASKS),"所属属性视图类型");
        rightSideContainerLayout.add(infoTitle1);

        attributeKindAttributesInfoGrid = new Grid<>();
        attributeKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
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
        attributeKindAttributesInfoGrid.setHeight(200,Unit.PIXELS);
        rightSideContainerLayout.add(attributeKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CUBE),"所属概念类型");
        rightSideContainerLayout.add(infoTitle2);

/*
        conceptionKindContainsAttributeInfoGrid = new Grid<>();
        conceptionKindContainsAttributeInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindContainsAttributeInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindContainsAttributeInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindContainsAttributeInfoGrid.addColumn(AttributeKindDetailUI.AttributeInConceptionKindDistributionInfo::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindContainsAttributeInfoGrid.addColumn(AttributeKindDetailUI.AttributeInConceptionKindDistributionInfo::getConceptionKindDesc).setHeader("概念类型显示名称").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);
        conceptionKindContainsAttributeInfoGrid.addColumn(new NumberRenderer<>(AttributeKindDetailUI.AttributeInConceptionKindDistributionInfo::getConceptionEntityCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getConceptionEntityCount() - entityStatisticsInfo2.getConceptionEntityCount()))
                .setHeader("类型包含实体数量").setKey("idx_2")
                .setFlexGrow(0).setWidth("120px").setResizable(false);
        conceptionKindContainsAttributeInfoGrid.addColumn(new NumberRenderer<>(AttributeKindDetailUI.AttributeInConceptionKindDistributionInfo::getPropertyExistInEntityCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getPropertyExistInEntityCount() - entityStatisticsInfo2.getPropertyExistInEntityCount()))
                .setHeader("包含当前属性实体数量").setKey("idx_3")
                .setFlexGrow(0).setWidth("120px").setResizable(false);
        conceptionKindContainsAttributeInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("60px").setResizable(false);
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"概念类型名称");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true)
                .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> kindEntityAttributeRuntimeStatistics.conceptionKindName);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"概念类型显示名称");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true)
                .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> kindEntityAttributeRuntimeStatistics.conceptionKindDesc);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.STOCK,"类型实体数量");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"属性类型命中数量");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindContainsAttributeInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);
        conceptionKindContainsAttributeInfoGrid.setHeight(350,Unit.PIXELS);
        rightSideContainerLayout.add(conceptionKindContainsAttributeInfoGrid);
*/

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        List<AttributeKind> attributeKindsList = targetAttributesViewKind.getContainsAttributeKinds();
        coreRealm.closeGlobalSession();
        attributeKindGrid.setItems(attributeKindsList);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.rightSideContainerLayout.setWidth(event.getWidth()-1430,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            this.rightSideContainerLayout.setWidth(receiver.getBodyClientWidth()-1430,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderAttachNewAttributeKindUI(){
        AttachNewAttributeKindView attachNewAttributeKindView = new AttachNewAttributeKindView(this.attributesViewKindUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"附加属性类型",null,true,490,200,false);
        fixSizeWindow.setWindowContent(attachNewAttributeKindView);
        fixSizeWindow.setModel(true);
        attachNewAttributeKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderDetachAttributeKindUI(AttributeKind attributeKind){
        DetachAttributeKindView detachAttributeKindView = new DetachAttributeKindView(this.attributesViewKindUID,attributeKind);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"移除属性类型",null,true,600,210,false);
        fixSizeWindow.setWindowContent(detachAttributeKindView);
        fixSizeWindow.setModel(true);
        detachAttributeKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderConfigAttributeKindAttachmentMetaDataUI(AttributeKind attributeKind){
        AttributeKindAttachmentMetaInfoView attributeKindAttachmentMetaInfoView = new AttributeKindAttachmentMetaInfoView(this.attributesViewKindUID,attributeKind);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"属性类型附加元属性信息",null,true,700,450,false);
        fixSizeWindow.setWindowContent(attributeKindAttachmentMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    public void refreshAttributeTypesInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        List<AttributeKind> attributeKindsList = targetAttributesViewKind.getContainsAttributeKinds();
        coreRealm.closeGlobalSession();
        attributeKindGrid.setItems(attributeKindsList);
    }

    @Override
    public void receivedAttributeKindAttachedToAttributesViewKindEvent(AttributeKindAttachedToAttributesViewKindEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getAttributeKindUID() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                if(event.getAttributeKind() != null){
                    ListDataProvider dtaProvider=(ListDataProvider)attributeKindGrid.getDataProvider();
                    dtaProvider.getItems().add(event.getAttributeKind());
                    dtaProvider.refreshAll();
                }
            }
        }
    }

    @Override
    public void receivedAttributeKindDetachedFromAttributesViewKindEvent(AttributeKindDetachedFromAttributesViewKindEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getAttributeKindUID() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                ListDataProvider dataProvider=(ListDataProvider)attributeKindGrid.getDataProvider();
                Collection<AttributeKind> itemsCollection = dataProvider.getItems();
                if(itemsCollection != null){
                    for(AttributeKind currentAttributeKind:itemsCollection){
                        if(event.getAttributeKindUID().equals(currentAttributeKind.getAttributeKindUID())){
                            itemsCollection.remove(currentAttributeKind);
                            break;
                        }
                    }
                    dataProvider.refreshAll();
                }
            }
        }
    }

    public void setHeight(int heightValue){
        containerHeight = heightValue;
        this.attributeKindGrid.setHeight(containerHeight-190,Unit.PIXELS);
    }
}
