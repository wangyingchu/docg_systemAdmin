package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentValue;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class QueryResultAttributeValuesInfoWidget extends VerticalLayout {

    private class AttributeValueMapVO {
        private String attributeName;
        private String attributeValueType;

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getAttributeValueType() {
            return attributeValueType;
        }

        public void setAttributeValueType(String attributeValueType) {
            this.attributeValueType = attributeValueType;
        }
    }

    private Grid<AttributeValueMapVO> queryResultAttributesInfoGrid;

    public QueryResultAttributeValuesInfoWidget(Map<String, DynamicContentValue.ContentValueType> contentAttributesValueMap) {
        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(LineAwesomeIconsSvg.LIST_SOLID.create(),"探索结果属性值信息");
        infoTitle.setWidthFull();
        infoTitle.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        add(infoTitle);

        queryResultAttributesInfoGrid = new Grid<>();
        queryResultAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        queryResultAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        queryResultAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        queryResultAttributesInfoGrid.addColumn(AttributeValueMapVO::getAttributeName).setHeader("属性名称").setKey("idx_0");
        queryResultAttributesInfoGrid.addColumn(AttributeValueMapVO::getAttributeValueType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        queryResultAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        queryResultAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        queryResultAttributesInfoGrid.setHeight(230,Unit.PIXELS);
        add(queryResultAttributesInfoGrid);

        if(contentAttributesValueMap != null){
            List<AttributeValueMapVO> attributeValueMapVOList = new ArrayList<>();
            contentAttributesValueMap.forEach((key, value) -> {

                AttributeValueMapVO attributeValueMapVO = new AttributeValueMapVO();
                attributeValueMapVO.setAttributeName(key);
                String attributeValue = value.toString();
                if("ENTITIES_PATH".equals(attributeValue)){
                    attributeValueMapVO.setAttributeValueType("实体路径");
                }else if("CONCEPTION_ENTITY".equals(attributeValue)){
                    attributeValueMapVO.setAttributeValueType("概念实体");
                }else if("RELATION_ENTITY".equals(attributeValue)){
                    attributeValueMapVO.setAttributeValueType("关系实体");
                }else{
                    attributeValueMapVO.setAttributeValueType(attributeValue);
                }
                attributeValueMapVOList.add(attributeValueMapVO);
            });
            queryResultAttributesInfoGrid.setItems(attributeValueMapVOList);
        }
    }
}
