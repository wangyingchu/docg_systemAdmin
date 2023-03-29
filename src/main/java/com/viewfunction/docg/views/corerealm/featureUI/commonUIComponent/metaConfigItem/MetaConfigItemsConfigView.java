package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItem;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;

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




        Grid<KindEntityAttributeRuntimeStatistics> conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100, Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("130px").setResizable(false);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性值").setKey("idx_2").setFlexGrow(0).setWidth("130px").setResizable(false);




        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
               // .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> getAttributeName(kindEntityAttributeRuntimeStatistics));
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.EYEDROPPER,"属性值");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx1).setSortable(true);

        conceptionKindAttributesInfoGrid.setHeight(218,Unit.PIXELS);

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
