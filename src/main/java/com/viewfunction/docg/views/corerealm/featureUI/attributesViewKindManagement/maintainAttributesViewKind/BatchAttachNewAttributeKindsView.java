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
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.CoreRealmStorageImplTech;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.*;

public class BatchAttachNewAttributeKindsView extends VerticalLayout {

    private String attributesViewKindUID;
    private Dialog containerDialog;
    private NativeLabel errorMessage;
    private Grid<AttributeKindMetaInfo> attributeKindMetaInfoGrid;
    private GridListDataView<AttributeKindMetaInfo> attributeKindsMetaInfoView;
    private TextField attributeKindNameFilterField;
    private TextField attributeKindDescFilterField;
    private ComboBox<AttributeDataType> attributeDataTypeFilterSelect;

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
            attributeKindInfoIcon.setSize("10px");
            attributeKindInfoIcon.setTooltipText("属性类型定义概览");

            HorizontalLayout buttons = new HorizontalLayout(attributeKindInfoIcon);
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
                /*
                errorMessage.setVisible(false);
                if(attributeKindFilterSelect.getValue()==null){
                    errorMessage.setText("请选择属性类型");
                    errorMessage.setVisible(true);
                }else{
                    doAttachAttributeType(attributeKindFilterSelect.getValue());
                }

                */
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

            AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
            if(targetAttributesViewKind != null){
                List<AttributeKind> containsAttributeKinds = targetAttributesViewKind.getContainsAttributeKinds();
                if(containsAttributeKinds != null && !containsAttributeKinds.isEmpty()){
                    Set<String> attributeKindUIDsSet= new HashSet<>();
                    containsAttributeKinds.forEach( attributeKind -> {
                        String attributeKindUID = attributeKind.getAttributeKindUID();
                        attributeKindUIDsSet.add(attributeKindUID);
                    });
                    ListDataProvider<AttributeKindMetaInfo> dtaProvider=(ListDataProvider)attributeKindMetaInfoGrid.getDataProvider();
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
}
