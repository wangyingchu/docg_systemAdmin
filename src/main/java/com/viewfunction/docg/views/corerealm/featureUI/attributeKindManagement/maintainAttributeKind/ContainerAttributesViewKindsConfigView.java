package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;

public class ContainerAttributesViewKindsConfigView extends VerticalLayout {
    private String attributeKindUID;
    private Grid<AttributeKind> attributeKindGrid;
    public ContainerAttributesViewKindsConfigView(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;

        this.setWidth(100, Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button createMetaConfigItemButton= new Button("附加新的属性视图类型");
        createMetaConfigItemButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        createMetaConfigItemButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        createMetaConfigItemButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAttachNewAttributeKindUI();
            }
        });
        buttonList.add(createMetaConfigItemButton);

        Button refreshMetaConfigItemsInfoButton = new Button("刷新包含的属性视图类型信息",new Icon(VaadinIcon.REFRESH));
        refreshMetaConfigItemsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshMetaConfigItemsInfoButton.addClickListener((ClickEvent<Button> click) ->{
            //refreshAttributeTypesInfo();
        });
        buttonList.add(refreshMetaConfigItemsInfoButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TASKS),"属性视图类型配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);






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
                        //renderConfigAttributeKindAttachmentMetaDataUI((AttributeKind)attributeKind);
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
                        //renderDetachAttributeKindUI((AttributeKind)attributeKind);
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
        add(attributeKindGrid);
    }
}
