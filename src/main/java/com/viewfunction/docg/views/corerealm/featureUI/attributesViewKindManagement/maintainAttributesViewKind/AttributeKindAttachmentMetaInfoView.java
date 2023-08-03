package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;

import dev.mett.vaadin.tooltip.Tooltips;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

public class AttributeKindAttachmentMetaInfoView extends VerticalLayout {
    private class MetaConfigItemValueObject{
        private String itemName;
        private Object itemValue;
        private AttributeDataType itemType;

        public MetaConfigItemValueObject(String itemName,AttributeDataType itemType,Object itemValue){
            this.itemName = itemName;
            this.itemType = itemType;
            this.itemValue = itemValue;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public Object getItemValue() {
            return itemValue;
        }

        public void setItemValue(Object itemValue) {
            this.itemValue = itemValue;
        }

        public AttributeDataType getItemType() {
            return itemType;
        }

        public void setItemType(AttributeDataType itemType) {
            this.itemType = itemType;
        }
    }

    private String attributesViewKindUID;
    private AttributeKind attributeKind;
    private Grid<MetaConfigItemValueObject> metaConfigItemValueGrid;

    public AttributeKindAttachmentMetaInfoView(String attributesViewKindUID,AttributeKind attributeKind){
        this.setMargin(false);
        this.setSpacing(false);
        this.attributesViewKindUID = attributesViewKindUID;
        this.attributeKind = attributeKind;

        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        Icon attributesViewKindIcon = VaadinIcon.TASKS.create();
        attributesViewKindIcon.setSize("12px");
        attributesViewKindIcon.getStyle().set("padding-right","3px");
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributesViewKindIcon, attributesViewKindUID));
        Icon attributeKindIcon = VaadinIcon.INPUT.create();
        attributeKindIcon.setSize("12px");
        attributeKindIcon.getStyle().set("padding-right","3px");
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributeKindIcon, this.attributeKind.getAttributeKindName()+"("+this.attributeKind.getAttributeKindUID()+")"));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout spacingLayout = new HorizontalLayout();
        spacingLayout.setHeight(10,Unit.PIXELS);
        add(spacingLayout);

        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button createMetaConfigItemButton= new Button("添加元属性");
        createMetaConfigItemButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        createMetaConfigItemButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        createMetaConfigItemButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddNewConfigItemUI();
            }
        });
        buttonList.add(createMetaConfigItemButton);

        Button refreshMetaConfigItemsInfoButton = new Button("刷新元属性信息",new Icon(VaadinIcon.REFRESH));
        refreshMetaConfigItemsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshMetaConfigItemsInfoButton.addClickListener((ClickEvent<Button> click) ->{
            refreshMetaConfigItemsInfo();
        });
        buttonList.add(refreshMetaConfigItemsInfoButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.BULLETS),"属性类型附加元属性配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(metaConfigItemValueObject -> {
            Icon removeIcon = new Icon(VaadinIcon.ERASER);
            removeIcon.setSize("20px");
            Button removeItemButton = new Button(removeIcon, event -> {
                renderDeleteConfigItemUI((MetaConfigItemValueObject)metaConfigItemValueObject);
            });
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeItemButton, "删除元属性");

            HorizontalLayout buttons = new HorizontalLayout(removeItemButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(10, Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        metaConfigItemValueGrid = new Grid<>();
        metaConfigItemValueGrid.setWidth(100, Unit.PERCENTAGE);
        metaConfigItemValueGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        metaConfigItemValueGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        metaConfigItemValueGrid.addColumn(MetaConfigItemValueObject::getItemName).setHeader("属性名称").setKey("idx_0").setFlexGrow(0).setWidth("250px").setResizable(true);
        metaConfigItemValueGrid.addColumn(MetaConfigItemValueObject::getItemType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("180px");
        metaConfigItemValueGrid.addColumn(MetaConfigItemValueObject::getItemValue).setHeader("属性值").setKey("idx_2").setFlexGrow(1).setResizable(false);
        metaConfigItemValueGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("100px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"元属性名称");
        metaConfigItemValueGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"元属性数据类型");
        metaConfigItemValueGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.INPUT,"元属性值");
        metaConfigItemValueGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx3 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        metaConfigItemValueGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3);
        metaConfigItemValueGrid.setHeight(300,Unit.PIXELS);
        add(metaConfigItemValueGrid);

        if(this.attributeKind != null && this.attributesViewKindUID != null){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
            Map<String,Object> metaConfigItemsMap = targetAttributesViewKind.getAttributeKindAllAttachMetaInfo(this.attributeKind.getAttributeKindUID());
            renderMetaConfigItemsData(metaConfigItemsMap);
            coreRealm.closeGlobalSession();
        }
    }

    private void renderMetaConfigItemsData(Map<String,Object> metaConfigItemsMap){
        if(metaConfigItemsMap != null){
            List<MetaConfigItemValueObject> metaConfigItemValueObjectList = new ArrayList<>();
            Set<String> itemNameSet = metaConfigItemsMap.keySet();
            for(String currentItemName:itemNameSet){
                Object itemValue = metaConfigItemsMap.get(currentItemName);
                AttributeDataType attributeDataType = checkAttributeDataType(itemValue);
                MetaConfigItemValueObject currentMetaConfigItemValueObject =
                        new MetaConfigItemValueObject(currentItemName,attributeDataType,itemValue);
                metaConfigItemValueObjectList.add(currentMetaConfigItemValueObject);
            }
            metaConfigItemValueGrid.setItems(metaConfigItemValueObjectList);
        }
    }

    private static AttributeDataType checkAttributeDataType(Object attributeValueObject){
        if(attributeValueObject instanceof Boolean){
            return AttributeDataType.BOOLEAN;
        }
        if(attributeValueObject instanceof Integer){
            return AttributeDataType.INT;
        }
        if(attributeValueObject instanceof Short){
            return AttributeDataType.SHORT;
        }
        if(attributeValueObject instanceof Long){
            return AttributeDataType.LONG;
        }
        if(attributeValueObject instanceof Float){
            return AttributeDataType.FLOAT;
        }
        if(attributeValueObject instanceof Double){
            return AttributeDataType.DOUBLE;
        }
        if(attributeValueObject instanceof BigDecimal){
            return AttributeDataType.DECIMAL;
        }
        if(attributeValueObject instanceof String){
            return AttributeDataType.STRING;
        }
        if(attributeValueObject instanceof Byte){
            return AttributeDataType.BINARY;
        }
        if(attributeValueObject instanceof ZonedDateTime){
            return AttributeDataType.TIMESTAMP;
        }
        if(attributeValueObject instanceof LocalDateTime){
            return AttributeDataType.DATETIME;
        }
        if(attributeValueObject instanceof LocalDate){
            return AttributeDataType.DATE;
        }
        if(attributeValueObject instanceof LocalTime){
            return AttributeDataType.TIME;
        }
        if(attributeValueObject instanceof Date){
            return AttributeDataType.TIMESTAMP;
        }
        return null;
    }

    private void renderAddNewConfigItemUI(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(null,null, null);
        addEntityAttributeView.setMetaConfigItemFeatureSupportable(null);

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加元属性",null,true,480,190,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);

        addEntityAttributeView.setAttributeValueOperateHandler(new AttributeValueOperateHandler() {
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                if(attributeValue != null){
                    String configItemName = attributeValue.getAttributeName();
                    Object configItemValue = attributeValue.getAttributeValue();

                    CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                    AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindUID);
                    Object existValue = targetAttributesViewKind.getAttributeKindAttachMetaInfo(attributeKind.getAttributeKindUID(),configItemName);
                    if(existValue != null){
                        CommonUIOperationUtil.showPopupNotification("UID 为 "+ attributesViewKindUID +" 的属性视图类型的属性类型附加元属性 "+configItemName+" 已经存在", NotificationVariant.LUMO_WARNING);
                        return;
                    }

                    Map<String,Object> metaConfigValueMap = new HashMap<>();
                    metaConfigValueMap.put(configItemName,configItemValue);
                    List<String> resultItemNameList = targetAttributesViewKind.setAttributeKindAttachMetaInfo(attributeKind.getAttributeKindUID(),metaConfigValueMap);

                    if(resultItemNameList != null && resultItemNameList.get(0).equals(configItemName)){
                        AttributeDataType attributeDataType = checkAttributeDataType(configItemValue);
                        MetaConfigItemValueObject newMetaConfigItemValueObject =
                                new MetaConfigItemValueObject(configItemName,attributeDataType,configItemValue);
                        ListDataProvider dtaProvider=(ListDataProvider)metaConfigItemValueGrid.getDataProvider();
                        dtaProvider.getItems().add(newMetaConfigItemValueObject);
                        dtaProvider.refreshAll();
                        fixSizeWindow.close();
                        CommonUIOperationUtil.showPopupNotification("为 UID 为 "+ attributesViewKindUID +" 的属性视图类型添加属性类型附加元属性信息 "+configItemName+" 成功", NotificationVariant.LUMO_SUCCESS);
                    }else{
                        CommonUIOperationUtil.showPopupNotification("为 UID 为 "+ attributesViewKindUID +" 的属性视图类型添加属性类型附加元属性信息 "+configItemName+" 成功", NotificationVariant.LUMO_ERROR);
                    }
                }
            }
        });
        fixSizeWindow.show();
    }

    private void renderDeleteConfigItemUI(MetaConfigItemValueObject metaConfigItemValueObject){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除元属性",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行删除元属性 "+metaConfigItemValueObject.itemName+" 的操作",actionButtonList,400,180);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doDeleteConfigItem(metaConfigItemValueObject,confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doDeleteConfigItem(MetaConfigItemValueObject metaConfigItemValueObject, ConfirmWindow confirmWindow){
        try {
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindUID);
            boolean deleteResult = targetAttributesViewKind.removeAttributeKindAttachMetaInfo(attributeKind.getAttributeKindUID(),metaConfigItemValueObject.itemName);
            coreRealm.closeGlobalSession();
            if(deleteResult){
                CommonUIOperationUtil.showPopupNotification("删除元属性 "+ metaConfigItemValueObject.itemName +" 成功", NotificationVariant.LUMO_SUCCESS);
                confirmWindow.closeConfirmWindow();
                ListDataProvider dtaProvider=(ListDataProvider)metaConfigItemValueGrid.getDataProvider();
                dtaProvider.getItems().remove(metaConfigItemValueObject);
                dtaProvider.refreshAll();
            }else{
                CommonUIOperationUtil.showPopupNotification("删除元属性 "+ metaConfigItemValueObject.itemName +" 失败", NotificationVariant.LUMO_ERROR);
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshMetaConfigItemsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        Map<String,Object> metaConfigItemsMap = targetAttributesViewKind.getAttributeKindAllAttachMetaInfo(this.attributeKind.getAttributeKindUID());
        renderMetaConfigItemsData(metaConfigItemsMap);
        coreRealm.closeGlobalSession();

        List<MetaConfigItemValueObject> metaConfigItemValueObjectList = new ArrayList<>();
        Set<String> itemNameSet = metaConfigItemsMap.keySet();
        for(String currentItemName:itemNameSet){
            Object itemValue = metaConfigItemsMap.get(currentItemName);
            AttributeDataType attributeDataType = checkAttributeDataType(itemValue);
            MetaConfigItemValueObject currentMetaConfigItemValueObject =
                    new MetaConfigItemValueObject(currentItemName,attributeDataType,itemValue);
            metaConfigItemValueObjectList.add(currentMetaConfigItemValueObject);
        }
        ListDataProvider dataProvider=(ListDataProvider)metaConfigItemValueGrid.getDataProvider();
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(metaConfigItemValueObjectList);
        dataProvider.refreshAll();
    }
}
