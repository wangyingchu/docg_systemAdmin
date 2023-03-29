package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItem;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
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
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetaConfigItemsConfigView extends VerticalLayout {

    public enum MetaConfigItemType {AttributeKind,AttributesViewKind,ConceptionKind,RelationAttachKind,RelationKind}
    private String metaConfigItemUID;
    private MetaConfigItemType metaConfigItemType;
    private String metaConfigItemName;

    public MetaConfigItemsConfigView(String metaConfigItemUID){
        this.metaConfigItemUID = metaConfigItemUID;
    }

    public MetaConfigItemsConfigView(MetaConfigItemType metaConfigItemType,String metaConfigItemName){
        this.metaConfigItemType = metaConfigItemType;
        this.metaConfigItemName = metaConfigItemName;
    }

    public void renderMetaConfigItemsConfigUI(){
        this.setWidth(100,Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button createMetaConfigItemButton= new Button("添加元属性");
        createMetaConfigItemButton.setIcon(VaadinIcon.PLUS.create());
        createMetaConfigItemButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        Tooltips.getCurrent().setTooltip(createMetaConfigItemButton, "添加元属性配置");
        buttonList.add(createMetaConfigItemButton);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            Icon editIcon = new Icon(VaadinIcon.EDIT);
            editIcon.setSize("20px");
            Button addItemButton = new Button(editIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    //renderConceptionKindQueryUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            addItemButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(addItemButton, "更新元属性值");

            Icon removeIcon = new Icon(VaadinIcon.MINUS);
            removeIcon.setSize("20px");
            Button removeItemButton = new Button(removeIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    //renderConceptionKindQueryUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeItemButton, "删除元属性");

            HorizontalLayout buttons = new HorizontalLayout(addItemButton,removeItemButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(10,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONTROLLER),"元属性配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);

        Grid<KindEntityAttributeRuntimeStatistics> conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100, Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0").setFlexGrow(0).setWidth("250px").setResizable(true);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("180px");
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性值").setKey("idx_2").setFlexGrow(1).setResizable(false);

        conceptionKindAttributesInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("120px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"元属性名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"元属性数据类型");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.INPUT,"元属性值");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx3 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3);
        conceptionKindAttributesInfoGrid.setHeight(200,Unit.PIXELS);
        add(conceptionKindAttributesInfoGrid);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        if(metaConfigItemUID != null){

        }else{
            if(metaConfigItemType != null && metaConfigItemName != null){
                switch(metaConfigItemType){
                    case ConceptionKind :
                        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(metaConfigItemName);
                        Map<String,Object> metaConfigItemsMap = targetConceptionKind.getMetaConfigItems();
                        renderMetaConfigItemsData(metaConfigItemsMap);
                }

            }
        }
    }

    private void renderMetaConfigItemsData(Map<String,Object> metaConfigItemsMap){

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
}
