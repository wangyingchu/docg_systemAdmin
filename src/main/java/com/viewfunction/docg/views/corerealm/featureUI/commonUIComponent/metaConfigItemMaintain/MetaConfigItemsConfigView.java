package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain;

import com.vaadin.flow.component.*;
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

import com.viewfunction.docg.coreRealm.realmServiceCore.feature.MetaConfigItemFeatureSupportable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

public class MetaConfigItemsConfigView extends VerticalLayout {

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

    public enum MetaConfigItemType {AttributeKind,AttributesViewKind,ConceptionKind,RelationAttachKind,RelationKind}
    private String metaConfigItemUID;
    private MetaConfigItemType metaConfigItemType;
    private String metaConfigItemName;
    private Grid<MetaConfigItemValueObject> metaConfigItemValueGrid;
    private MetaConfigItemFeatureSupportable metaConfigItemFeatureSupportable;
    private Component customizeComponent;

    public MetaConfigItemsConfigView(String metaConfigItemUID){
        this.metaConfigItemUID = metaConfigItemUID;
    }

    public MetaConfigItemsConfigView(MetaConfigItemType metaConfigItemType,String metaConfigItemName){
        this.metaConfigItemType = metaConfigItemType;
        this.metaConfigItemName = metaConfigItemName;
    }

    public void renderMetaConfigItemsConfigUI(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);

        this.setWidth(100,Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        if(this.customizeComponent != null){
            buttonList.add(this.customizeComponent);
        }

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

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.BOOKMARK),"元属性配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(metaConfigItemValueObjectInfo -> {

            Icon editIcon = new Icon(VaadinIcon.EDIT);
            editIcon.setSize("18px");
            Button addItemButton = new Button(editIcon, event -> {
                if(metaConfigItemValueObjectInfo instanceof MetaConfigItemValueObject){
                    renderUpdateConfigItemUI((MetaConfigItemValueObject)metaConfigItemValueObjectInfo);
                }
            });
            addItemButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL);
            addItemButton.setTooltipText("更新元属性值");

            Icon removeIcon = new Icon(VaadinIcon.ERASER);
            removeIcon.setSize("20px");
            Button removeItemButton = new Button(removeIcon, event -> {
                if(metaConfigItemValueObjectInfo instanceof MetaConfigItemValueObject){
                    renderDeleteConfigItemUI((MetaConfigItemValueObject)metaConfigItemValueObjectInfo);
                }
            });
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeItemButton.setTooltipText("删除元属性");

            HorizontalLayout buttons = new HorizontalLayout(addItemButton,removeItemButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(10,Unit.PIXELS);
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
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"元属性值");
        metaConfigItemValueGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx3 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        metaConfigItemValueGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3);
        metaConfigItemValueGrid.setHeight(150,Unit.PIXELS);
        add(metaConfigItemValueGrid);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        if(metaConfigItemUID != null){

        }else{
            if(metaConfigItemType != null && metaConfigItemName != null){
                Map<String,Object> metaConfigItemsMap = null;
                switch(metaConfigItemType){
                    case ConceptionKind :
                        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(metaConfigItemName);
                        this.metaConfigItemFeatureSupportable = targetConceptionKind;
                        metaConfigItemsMap = targetConceptionKind.getMetaConfigItems();
                        renderMetaConfigItemsData(metaConfigItemsMap);
                        break;
                    case RelationKind:
                        RelationKind targetRelationKind = coreRealm.getRelationKind(metaConfigItemName);
                        this.metaConfigItemFeatureSupportable = targetRelationKind;
                        metaConfigItemsMap = targetRelationKind.getMetaConfigItems();
                        renderMetaConfigItemsData(metaConfigItemsMap);
                        break;
                    case AttributeKind :
                        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(metaConfigItemName);
                        this.metaConfigItemFeatureSupportable = targetAttributeKind;
                        metaConfigItemsMap = targetAttributeKind.getMetaConfigItems();
                        renderMetaConfigItemsData(metaConfigItemsMap);
                        break;
                    case AttributesViewKind:
                        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(metaConfigItemName);
                        this.metaConfigItemFeatureSupportable = targetAttributesViewKind;
                        metaConfigItemsMap = targetAttributesViewKind.getMetaConfigItems();
                        renderMetaConfigItemsData(metaConfigItemsMap);
                }
            }
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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderMetaConfigItemsConfigUI();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
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
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(this.metaConfigItemName,null, null);
        addEntityAttributeView.setMetaConfigItemFeatureSupportable(this.metaConfigItemFeatureSupportable);
        addEntityAttributeView.setAttributeValueOperateHandler(new AttributeValueOperateHandler() {
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                if(attributeValue != null){
                    /*
                    String configItemName = attributeValue.getAttributeName();
                    Object configItemValue = attributeValue.getAttributeValue();
                    AttributeDataType attributeDataType = checkAttributeDataType(configItemValue);
                    MetaConfigItemValueObject newMetaConfigItemValueObject =
                            new MetaConfigItemValueObject(configItemName,attributeDataType,configItemValue);
                    ListDataProvider dtaProvider=(ListDataProvider)metaConfigItemValueGrid.getDataProvider();
                    dtaProvider.getItems().add(newMetaConfigItemValueObject);
                    dtaProvider.refreshAll();
                    */
                    //just refresh grid to get rid of already exist same name config item
                    refreshMetaConfigItemsInfo();
                }
            }
        });
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"添加元属性",null,true,480,190,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
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

    private void renderUpdateConfigItemUI(MetaConfigItemValueObject metaConfigItemValueObject){
        UpdateMetaConfigItemValueView updateMetaConfigItemValueView =
                new UpdateMetaConfigItemValueView(this.metaConfigItemFeatureSupportable,metaConfigItemValueObject.itemName,metaConfigItemValueObject.itemType,metaConfigItemValueObject.itemValue);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.EDIT.create(),"更新元属性",null,true,480,190,false);
        fixSizeWindow.setWindowContent(updateMetaConfigItemValueView);
        updateMetaConfigItemValueView.setContainerDialog(fixSizeWindow);
        updateMetaConfigItemValueView.setContainerMetaConfigItemsConfigView(this);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void doDeleteConfigItem(MetaConfigItemValueObject metaConfigItemValueObject,ConfirmWindow confirmWindow){
        boolean deleteResult = this.metaConfigItemFeatureSupportable.deleteMetaConfigItem(metaConfigItemValueObject.itemName);
        if(deleteResult){
            CommonUIOperationUtil.showPopupNotification("删除元属性 "+ metaConfigItemValueObject.itemName +" 成功", NotificationVariant.LUMO_SUCCESS);
            confirmWindow.closeConfirmWindow();
            ListDataProvider dtaProvider=(ListDataProvider)metaConfigItemValueGrid.getDataProvider();
            dtaProvider.getItems().remove(metaConfigItemValueObject);
            dtaProvider.refreshAll();
        }else{
            CommonUIOperationUtil.showPopupNotification("删除元属性 "+ metaConfigItemValueObject.itemName +" 失败", NotificationVariant.LUMO_ERROR);
        }
    }

    public void refreshMetaConfigItemsInfo(){
        Map<String,Object> metaConfigItemsMap = null;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        switch(metaConfigItemType){
            case ConceptionKind :
                ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(metaConfigItemName);
                metaConfigItemsMap = targetConceptionKind.getMetaConfigItems();
                break;
            case RelationKind:
                RelationKind targetRelationKind = coreRealm.getRelationKind(metaConfigItemName);
                metaConfigItemsMap = targetRelationKind.getMetaConfigItems();
                break;
            case AttributeKind:
                AttributeKind targetAttributeKind = coreRealm.getAttributeKind(metaConfigItemName);
                metaConfigItemsMap = targetAttributeKind.getMetaConfigItems();
                break;
            case AttributesViewKind:
                AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(metaConfigItemName);
                metaConfigItemsMap = targetAttributesViewKind.getMetaConfigItems();
        }
        List<MetaConfigItemValueObject> metaConfigItemValueObjectList = new ArrayList<>();
        Set<String> itemNameSet = metaConfigItemsMap.keySet();
        for(String currentItemName:itemNameSet){
            Object itemValue = metaConfigItemsMap.get(currentItemName);
            AttributeDataType attributeDataType = checkAttributeDataType(itemValue);
            MetaConfigItemValueObject currentMetaConfigItemValueObject =
                    new MetaConfigItemValueObject(currentItemName,attributeDataType,itemValue);
            metaConfigItemValueObjectList.add(currentMetaConfigItemValueObject);
        }
        ListDataProvider dtaProvider=(ListDataProvider)metaConfigItemValueGrid.getDataProvider();
        dtaProvider.getItems().clear();
        dtaProvider.getItems().addAll(metaConfigItemValueObjectList);
        dtaProvider.refreshAll();
    }

    public void setViewHeight(int viewHeight){
        this.metaConfigItemValueGrid.setHeight(viewHeight - 50,Unit.PIXELS);
    }

    public void setCustomizeComponent(Component customizeComponent){
        this.customizeComponent = customizeComponent;
    }
}
