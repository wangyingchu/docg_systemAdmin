package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.CoreRealmStorageImplTech;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeKindMaintain.AttributeKindSummaryInfoView;

import java.util.*;

public class BatchAttachNewAttributeKindsView extends VerticalLayout {

    private String attributesViewKindUID;
    private Dialog containerDialog;
    private Grid<AttributeKindMetaInfo> attributeKindMetaInfoGrid;
    private GridListDataView<AttributeKindMetaInfo> attributeKindsMetaInfoView;
    private TextField attributeKindNameFilterField;
    private TextField attributeKindDescFilterField;
    private ComboBox<AttributeDataType> attributeDataTypeFilterSelect;
    private AttributesViewKind containerAttributesViewKind;
    private ContainsAttributeKindsConfigView containerContainsAttributeKindsConfigView;
    private Popover attributeKindUIContainerPopover;

    public BatchAttachNewAttributeKindsView(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;

        HorizontalLayout attributeKindsSearchElementsContainerLayout = new HorizontalLayout();
        attributeKindsSearchElementsContainerLayout.setSpacing(false);
        attributeKindsSearchElementsContainerLayout.setMargin(false);
        add(attributeKindsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        attributeKindsSearchElementsContainerLayout.add(filterTitle);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        attributeKindNameFilterField = new TextField();
        attributeKindNameFilterField.setPlaceholder("属性类型名称");
        attributeKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributeKindNameFilterField.setWidth(170,Unit.PIXELS);
        attributeKindsSearchElementsContainerLayout.add(attributeKindNameFilterField);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, attributeKindNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        attributeKindsSearchElementsContainerLayout.add(plusIcon);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        attributeKindDescFilterField = new TextField();
        attributeKindDescFilterField.setPlaceholder("属性类型描述");
        attributeKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        attributeKindDescFilterField.setWidth(170,Unit.PIXELS);
        attributeKindsSearchElementsContainerLayout.add(attributeKindDescFilterField);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, attributeKindDescFilterField);

        Icon plusIcon2 = new Icon(VaadinIcon.PLUS);
        plusIcon2.setSize("12px");
        attributeKindsSearchElementsContainerLayout.add(plusIcon2);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon2);

        attributeDataTypeFilterSelect = new ComboBox();
        attributeDataTypeFilterSelect.setPlaceholder("属性类型数据类型");
        attributeDataTypeFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        attributeKindDescFilterField.setWidth(170,Unit.PIXELS);
        attributeDataTypeFilterSelect.setPageSize(30);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        String coreRealmName = coreRealm.getCoreRealmName();
        AttributeDataType[] attributeDataTypesArray = coreRealm.getStorageImplTech().equals(CoreRealmStorageImplTech.NEO4J) ?
                new AttributeDataType[]{
                        AttributeDataType.BOOLEAN,
                        AttributeDataType.LONG,
                        AttributeDataType.DOUBLE,
                        AttributeDataType.TIMESTAMP,
                        AttributeDataType.DATE,
                        AttributeDataType.DATETIME,
                        AttributeDataType.TIME,
                        AttributeDataType.STRING
                }
                :
                new AttributeDataType[]{
                        AttributeDataType.BOOLEAN,
                        AttributeDataType.INT,
                        AttributeDataType.SHORT,
                        AttributeDataType.LONG,
                        AttributeDataType.FLOAT,
                        AttributeDataType.DOUBLE,
                        AttributeDataType.TIMESTAMP,
                        AttributeDataType.DATE,
                        AttributeDataType.DATETIME,
                        AttributeDataType.TIME,
                        AttributeDataType.STRING,
                        AttributeDataType.BYTE,
                        AttributeDataType.DECIMAL
                };
        this.attributeDataTypeFilterSelect.setItems(attributeDataTypesArray);

        attributeKindsSearchElementsContainerLayout.add(attributeDataTypeFilterSelect);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,attributeDataTypeFilterSelect);

        Button searchAttributeKindsButton = new Button("查找属性类型",new Icon(VaadinIcon.SEARCH));
        searchAttributeKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchAttributeKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindsSearchElementsContainerLayout.add(searchAttributeKindsButton);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchAttributeKindsButton);
        searchAttributeKindsButton.setWidth(115,Unit.PIXELS);
        searchAttributeKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterAttributeKinds();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        attributeKindsSearchElementsContainerLayout.add(divIcon);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        attributeKindsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        attributeKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterAttributeKinds();
            }
        });

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(attributeKindMetaInfo -> {
            Icon attributeKindInfoIcon = new Icon(VaadinIcon.INFO_CIRCLE_O);
            attributeKindInfoIcon.setSize("14px");
            Button showAttributeKindInfo = new Button(attributeKindInfoIcon, event -> {
                Button sourceButton = event.getSource();
                AttributeKindMetaInfo targetAttributeKindMetaInfo = (AttributeKindMetaInfo)attributeKindMetaInfo;
                showAttributeKindInfo(sourceButton,targetAttributeKindMetaInfo);

            });
            showAttributeKindInfo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            showAttributeKindInfo.addThemeVariants(ButtonVariant.LUMO_SMALL);
            showAttributeKindInfo.setTooltipText("属性类型定义概览");
            HorizontalLayout buttons = new HorizontalLayout(showAttributeKindInfo);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(60,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        attributeKindMetaInfoGrid = new Grid<>();
        attributeKindMetaInfoGrid.setWidth(100, Unit.PERCENTAGE);
        attributeKindMetaInfoGrid.setHeight(500,Unit.PIXELS);
        attributeKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        attributeKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getKindName).setHeader("属性类型名称").setKey("idx_0").setFlexGrow(1);
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getKindDesc).setHeader("属性类型描述").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getKindDesc());
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getAttributeDataType).setHeader("属性数据类型").setKey("idx_2")
                .setFlexGrow(0).setWidth("130px").setResizable(false);
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getKindUID).setHeader("属性类型 UID").setKey("idx_3")
                .setFlexGrow(0).setWidth("150px").setResizable(false);
        attributeKindMetaInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_6").setFlexGrow(0).setWidth("60px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性类型名称");
        attributeKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"属性类型描述");
        attributeKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"属性数据类型");
        attributeKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.KEY_O,"属性类型 UID");
        attributeKindMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx6 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        attributeKindMetaInfoGrid.getColumnByKey("idx_6").setHeader(gridColumnHeader_idx6);

        attributeKindMetaInfoGrid.appendFooterRow();
        add(attributeKindMetaInfoGrid);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确定设置附加属性类型",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                attachAttributeKinds(attributeKindMetaInfoGrid.getSelectedItems());
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadAttributeKindsInfo();
    }

    private void loadAttributeKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<AttributeKindMetaInfo> gridAttributeKindMetaInfoList = new ArrayList<>();
        try {
            List<AttributeKindMetaInfo> runtimeAttributeKindMetaInfoList = coreRealm.getAttributeKindsMetaInfo();
            gridAttributeKindMetaInfoList.addAll(runtimeAttributeKindMetaInfoList);
            this.attributeKindNameFilterField.setValue("");
            this.attributeKindDescFilterField.setValue("");
            this.attributeDataTypeFilterSelect.setValue(null);
            this.attributeKindsMetaInfoView = attributeKindMetaInfoGrid.setItems(gridAttributeKindMetaInfoList);
            this.containerAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
            if(this.containerAttributesViewKind != null){
                List<AttributeKind> containsAttributeKinds = this.containerAttributesViewKind.getContainsAttributeKinds();
                if(containsAttributeKinds != null && !containsAttributeKinds.isEmpty()){
                    Set<String> attributeKindUIDsSet= new HashSet<>();
                    containsAttributeKinds.forEach( attributeKind -> {
                        String attributeKindUID = attributeKind.getAttributeKindUID();
                        attributeKindUIDsSet.add(attributeKindUID);
                    });
                    ListDataProvider<AttributeKindMetaInfo> dtaProvider=(ListDataProvider)this.attributeKindMetaInfoGrid.getDataProvider();
                    dtaProvider.getItems().forEach( item ->{
                        if(attributeKindUIDsSet.contains(item.getKindUID())){
                            attributeKindMetaInfoGrid.select(item);
                        }
                    });
                }
            }

            //logic to filter AttributeKinds already loaded from server
            this.attributeKindsMetaInfoView.addFilter(item->{
                String entityKindName = item.getKindName().toUpperCase();
                String entityKindDesc = item.getKindDesc().toUpperCase();
                String entityDataType = item.getAttributeDataType();
                boolean attributeKindNameFilterResult = true;
                if(!attributeKindNameFilterField.getValue().trim().equals("")){
                    if(entityKindName.contains(attributeKindNameFilterField.getValue().trim().toUpperCase())){
                        attributeKindNameFilterResult = true;
                    }else{
                        attributeKindNameFilterResult = false;
                    }
                }

                boolean attributeKindDescFilterResult = true;
                if(!attributeKindDescFilterField.getValue().trim().equals("")){
                    if(entityKindDesc.contains(attributeKindDescFilterField.getValue().trim().toUpperCase())){
                        attributeKindDescFilterResult = true;
                    }else{
                        attributeKindDescFilterResult = false;
                    }
                }

                boolean attributeDataTypeFilterResult = true;
                if(attributeDataTypeFilterSelect.getValue() != null){
                    if(entityDataType.equals(attributeDataTypeFilterSelect.getValue().toString())){
                        attributeDataTypeFilterResult = true;
                    }else{
                        attributeDataTypeFilterResult = false;
                    }
                }
                return attributeKindNameFilterResult & attributeKindDescFilterResult & attributeDataTypeFilterResult;
            });


        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void filterAttributeKinds(){
        String attributeKindFilterValue = attributeKindNameFilterField.getValue().trim();
        String attributeKindDescFilterValue = attributeKindDescFilterField.getValue().trim();
        AttributeDataType dataType= attributeDataTypeFilterSelect.getValue();
        if(attributeKindFilterValue.equals("")&attributeKindDescFilterValue.equals("")&dataType == null){
            CommonUIOperationUtil.showPopupNotification("请输入属性类型名称 和/或 属性类型描述 和/或 属性数据类型", NotificationVariant.LUMO_ERROR);
        }else{
            this.attributeKindsMetaInfoView.refreshAll();
        }
    }

    private void cancelFilterAttributeKinds(){
        attributeKindNameFilterField.setValue("");
        attributeKindDescFilterField.setValue("");
        attributeDataTypeFilterSelect.setValue(null);
        this.attributeKindsMetaInfoView.refreshAll();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void attachAttributeKinds(Set<AttributeKindMetaInfo> selectedAttributeKindMetaInfos){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认设置附加属性类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","确定为属性视图类型 "+ containerAttributesViewKind.getAttributesViewKindName()+" 批量设置 "+selectedAttributeKindMetaInfos.size()+" 项附加属性类型",actionButtonList,650,190);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doAttachAttributeType(selectedAttributeKindMetaInfos,confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doAttachAttributeType(Set<AttributeKindMetaInfo> selectedAttributeKindMetaInfos,ConfirmWindow confirmWindow){
        confirmWindow.closeConfirmWindow();
        Set<String> newAttachedAttributeKindIDs = new HashSet<>();
        selectedAttributeKindMetaInfos.forEach(attributeKindMetaInfo -> {
            newAttachedAttributeKindIDs.add(attributeKindMetaInfo.getKindUID());
        });

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        this.containerAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        if(this.containerAttributesViewKind != null) {
            List<AttributeKind> containsAttributeKinds = this.containerAttributesViewKind.getContainsAttributeKinds();
            Set<String> remainAttributeKindsUIDs = new HashSet<>();

            containsAttributeKinds.forEach(attributeKind -> {
                String currentAttributeKindID = attributeKind.getAttributeKindUID();
                if (newAttachedAttributeKindIDs.contains(currentAttributeKindID)) {
                    remainAttributeKindsUIDs.add(currentAttributeKindID);
                } else {
                    try {
                        containerAttributesViewKind.detachAttributeKind(currentAttributeKindID);
                    } catch (CoreRealmServiceRuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            newAttachedAttributeKindIDs.forEach(newAttachedAttributeKindID -> {
                if(!remainAttributeKindsUIDs.contains(newAttachedAttributeKindID)){
                    try {
                        this.containerAttributesViewKind.attachAttributeKind(newAttachedAttributeKindID);
                    } catch (CoreRealmServiceRuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        coreRealm.closeGlobalSession();

        if(this.containerContainsAttributeKindsConfigView != null){
            this.containerContainsAttributeKindsConfigView.refreshAttributeTypesInfo();
        }
        if(containerDialog != null){
            containerDialog.close();
        }
        CommonUIOperationUtil.showPopupNotification("为属性视图类型 "+ containerAttributesViewKind.getAttributesViewKindName()+" 批量设置 "+selectedAttributeKindMetaInfos.size()+" 项附加属性类型操作成功", NotificationVariant.LUMO_SUCCESS);
    }

    public void setContainerContainsAttributeKindsConfigView(ContainsAttributeKindsConfigView containerContainsAttributeKindsConfigView) {
        this.containerContainsAttributeKindsConfigView = containerContainsAttributeKindsConfigView;
    }

    public void showAttributeKindInfo(Button sourceButton,AttributeKindMetaInfo targetAttributeKindMetaInfo){
        if(attributeKindUIContainerPopover == null){
            attributeKindUIContainerPopover = new Popover();
            attributeKindUIContainerPopover.setModal(true,true);
            attributeKindUIContainerPopover.addThemeVariants(PopoverVariant.ARROW);
            attributeKindUIContainerPopover.setModal(true, true);
            attributeKindUIContainerPopover.setHeight("700px");
            attributeKindUIContainerPopover.setWidth("620px");
        }
        attributeKindUIContainerPopover.setTarget(sourceButton);
        attributeKindUIContainerPopover.removeAll();
        AttributeKindSummaryInfoView attributeKindSummaryInfoView = new AttributeKindSummaryInfoView(targetAttributeKindMetaInfo);
        attributeKindUIContainerPopover.add(attributeKindSummaryInfoView);
        attributeKindUIContainerPopover.open();
    }
}
