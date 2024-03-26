package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeMaintain;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.AttributesParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.filteringItem.NullValueFilteringItem;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AttributesValueListView extends VerticalLayout {

    public enum AttributeKindType {ConceptionKind,RelationKind}
    private String kindName;
    private String attributeName;
    private AttributeKindType attributeKindType;
    private int attributeValueSize = 100;
    private Grid<AttributeValueVO> attributeValueGrid;

    public AttributesValueListView(AttributeKindType attributeKindType,String kindName,String attributeName){

        this.attributeKindType = attributeKindType;
        this.kindName = kindName;
        this.attributeName = attributeName;

        String uidColumnName = "概念实体 UID";
        Icon kindIcon = VaadinIcon.CUBE.create();
        switch (this.attributeKindType){
            case ConceptionKind :
                kindIcon = VaadinIcon.CUBE.create();
                break;
            case RelationKind : kindIcon = VaadinIcon.CONNECT_O.create();
            uidColumnName = "关系实体 UID";
        }

        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.kindName));

        Icon attributeIcon = VaadinIcon.INPUT.create();
        attributeIcon.setSize("18px");
        attributeIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributeIcon, this.attributeName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        attributeValueGrid = new Grid<>();
        attributeValueGrid.setWidth(100, Unit.PERCENTAGE);
        attributeValueGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributeValueGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        attributeValueGrid.addColumn(AttributeValueVO::getEntityUID).setHeader(uidColumnName).setKey("idx_0").setFlexGrow(0).setWidth("120px").setResizable(true);
        attributeValueGrid.addColumn(AttributeValueVO::getAttributeValue).setHeader("属性值").setKey("idx_1").setFlexGrow(1).setResizable(true);
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.KEY_O,uidColumnName);
        attributeValueGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.EYE,"属性值");
        attributeValueGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);

        add(attributeValueGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderAttributeValue();
    }

    private class AttributeValueVO{
        private String entityUID;
        private String attributeName;
        private Object attributeValue;

        public AttributeValueVO(String entityUID,String attributeName,Object attributeValue){
            this.entityUID = entityUID;
            this.attributeName = attributeName;
            this.attributeValue = attributeValue;
        }

        public String getEntityUID() {
            return entityUID;
        }

        public void setEntityUID(String entityUID) {
            this.entityUID = entityUID;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public Object getAttributeValue() {
            return attributeValue;
        }

        public void setAttributeValue(Object attributeValue) {
            this.attributeValue = attributeValue;
        }
    }

    private void renderAttributeValue(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesParameters attributesParameters = new AttributesParameters();
        NullValueFilteringItem queryFilteringItem = new NullValueFilteringItem(this.attributeName);
        queryFilteringItem.reverseCondition();
        attributesParameters.setDefaultFilteringItem(queryFilteringItem);
        List<AttributeValueVO> attributeValueVOList = new ArrayList<>();
        try {
            switch (this.attributeKindType){
                case ConceptionKind :
                    ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.kindName);
                    Set<ConceptionEntity> resultConceptionEntitySet = targetConceptionKind.getRandomEntities(attributesParameters,false,this.attributeValueSize);
                    for(ConceptionEntity currentConceptionEntity:resultConceptionEntitySet){
                        String conceptionEntityUID = currentConceptionEntity.getConceptionEntityUID();
                        Object conceptionEntityAttributeValue = currentConceptionEntity.getAttribute(this.attributeName).getAttributeValue();
                        AttributeValueVO currentAttributeValueVO = new AttributeValueVO(conceptionEntityUID,this.attributeName,conceptionEntityAttributeValue);
                        attributeValueVOList.add(currentAttributeValueVO);
                    }
                    break;
                case RelationKind :
                    RelationKind targetRelationKind = coreRealm.getRelationKind(this.kindName);
                    Set<RelationEntity> resultRelationEntitySet = targetRelationKind.getRandomEntities(attributesParameters,false,this.attributeValueSize);
                    for(RelationEntity currentRelationEntity:resultRelationEntitySet){
                        String relationEntityUID = currentRelationEntity.getRelationEntityUID();
                        Object relationEntityAttributeValue = currentRelationEntity.getAttribute(this.attributeName).getAttributeValue();
                        AttributeValueVO currentAttributeValueVO = new AttributeValueVO(relationEntityUID,this.attributeName,relationEntityAttributeValue);
                        attributeValueVOList.add(currentAttributeValueVO);
                    }
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
        coreRealm.closeGlobalSession();
        attributeValueGrid.setItems(attributeValueVOList);
    }

    public int getAttributeValueSize() {
        return attributeValueSize;
    }

    public void setAttributeValueSize(int attributeValueSize) {
        this.attributeValueSize = attributeValueSize;
    }
}
