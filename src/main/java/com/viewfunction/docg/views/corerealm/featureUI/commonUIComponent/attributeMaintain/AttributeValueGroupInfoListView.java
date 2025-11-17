package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeMaintain;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttributeValueGroupInfoListView extends VerticalLayout {
    public enum AttributeKindType {ConceptionKind,RelationKind}
    private String kindName;
    private String attributeName;
    private AttributeKindType attributeKindType;
    private int entityStatisticNumber = 100000;
    private Grid<AttributeValueGroupVO> attributeValueGroupGrid;

    public AttributeValueGroupInfoListView(AttributeKindType attributeKindType, String kindName, String attributeName){

        this.attributeKindType = attributeKindType;
        this.kindName = kindName;
        this.attributeName = attributeName;

        Icon kindIcon = VaadinIcon.CUBE.create();
        switch (this.attributeKindType){
            case ConceptionKind :
                kindIcon = VaadinIcon.CUBE.create();
                break;
            case RelationKind : kindIcon = VaadinIcon.CONNECT_O.create();
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

        attributeValueGroupGrid = new Grid<>();
        attributeValueGroupGrid.setWidth(100, Unit.PERCENTAGE);
        attributeValueGroupGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributeValueGroupGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        attributeValueGroupGrid.addColumn(AttributeValueGroupVO::getGroupValue).setHeader("属性值").setKey("idx_0").setFlexGrow(1).setResizable(true).
                setTooltipGenerator(attributeValueGroupVO->
                        attributeValueGroupVO.getGroupValue() != null ? attributeValueGroupVO.getGroupValue().toString():"NULL - 空值");
        attributeValueGroupGrid.addColumn(AttributeValueGroupVO::getGroupSize).setHeader("具有当前值实体数量").setKey("idx_1").setFlexGrow(0).setWidth("140px").setResizable(true);
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.EYE,"属性值");
        attributeValueGroupGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.LIST_OL.create() ,"具有当前值实体数量");
        attributeValueGroupGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        add(attributeValueGroupGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderAttributeValueGroupInfo();
    }

    private class AttributeValueGroupVO{
        private Object groupValue;
        private Number groupSize;

        public AttributeValueGroupVO(Object groupValue,Number groupSize){
            this.groupValue = groupValue != null ? groupValue : "NULL - 空值";
            this.groupSize = groupSize;
        }

        public Object getGroupValue() {
            return groupValue;
        }

        public Number getGroupSize() {
            return groupSize;
        }
    }

    private void renderAttributeValueGroupInfo(){
        List<AttributeValueGroupVO> attributeValueGroupVOList = new ArrayList();
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        try {
            QueryParameters queryParameters = new QueryParameters();
            queryParameters.setResultNumber(entityStatisticNumber);
            switch (this.attributeKindType){
                case ConceptionKind :
                    ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.kindName);
                    Map<Object, Number> resultMap = targetConceptionKind.statisticEntityGroupByAttributeValue(queryParameters,this.attributeName);
                    Set<Object> valueSet = resultMap.keySet();
                    for(Object currentValue : valueSet){
                        attributeValueGroupVOList.add(new AttributeValueGroupVO(currentValue,resultMap.get(currentValue)));
                    }
                    break;
                case RelationKind :
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        coreRealm.closeGlobalSession();
        attributeValueGroupGrid.setItems(attributeValueGroupVOList);
    }
}
