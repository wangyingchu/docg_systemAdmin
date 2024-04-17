package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.DataSlicePropertyType;

import java.util.ArrayList;
import java.util.List;

public class DataSlicePropertyNameMapperWidget extends VerticalLayout {

    private String attributeName;
    private DataSlicePropertyType dataSlicePropertyType;
    private ComboBox<KindEntityAttributeRuntimeStatistics> attributeRuntimeMappingSelect;

    public DataSlicePropertyNameMapperWidget(String attributeName,DataSlicePropertyType dataSlicePropertyType, boolean isPKProperty,
                                             List<KindEntityAttributeRuntimeStatistics> attributeRuntimeMapping){
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(true);
        this.setWidth(100, Unit.PERCENTAGE);
        this.attributeName = attributeName;
        this.dataSlicePropertyType = dataSlicePropertyType;

        HorizontalLayout attributeOriginalNameInfo = new HorizontalLayout();
        attributeOriginalNameInfo.setSpacing(false);
        attributeOriginalNameInfo.setMargin(false);
        attributeOriginalNameInfo.setPadding(false);
        attributeOriginalNameInfo.getStyle().set("padding-top","2px");
        attributeOriginalNameInfo.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(attributeOriginalNameInfo);

        Icon icon = VaadinIcon.INPUT.create();
        icon.setSize("10px");
        attributeOriginalNameInfo.add(icon);

        NativeLabel attributeNameLabel = new NativeLabel(this.attributeName);
        attributeNameLabel.getStyle()
                .set("padding-left","5px")
                .set("font-size","0.8rem")
                .set("font-weight","bold");
        attributeOriginalNameInfo.add(attributeNameLabel);

        Icon isPKIcon = VaadinIcon.KEY.create();
        isPKIcon.getStyle()
                .set("padding-left","5px")
                .set("font-weight","bold");
        isPKIcon.setSize("12px");
        attributeOriginalNameInfo.add(isPKIcon);
        isPKIcon.setVisible(isPKProperty);
        isPKIcon.setTooltipText("数据切片主键");

        NativeLabel attributeTypeLabel = new NativeLabel(dataSlicePropertyType.toString());
        attributeTypeLabel.getStyle()
                .set("padding-left","5px")
                .set("font-size","0.6rem");
        attributeTypeLabel.addClassNames("text-tertiary");
        attributeOriginalNameInfo.add(attributeTypeLabel);

        attributeRuntimeMappingSelect = new ComboBox<>();
        attributeRuntimeMappingSelect.setWidth(98,Unit.PERCENTAGE);
        attributeRuntimeMappingSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        attributeRuntimeMappingSelect.setPlaceholder("选择对应的类型属性");
        attributeRuntimeMappingSelect.setItems(filterTypeMatchedAttributeRuntimeMapping(attributeRuntimeMapping));
        attributeRuntimeMappingSelect.setRequired(false);
        attributeRuntimeMappingSelect.setAllowCustomValue(true);
        attributeRuntimeMappingSelect.setPageSize(10);
        attributeRuntimeMappingSelect.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics attributeKindMetaInfo) {
                String itemLabelValue = attributeKindMetaInfo.getAttributeName()+ " ("+
                        attributeKindMetaInfo.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });
        attributeRuntimeMappingSelect.setRenderer(createRenderer());
        add(attributeRuntimeMappingSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(10,Unit.PIXELS);
        spaceDivLayout.setWidth(98,Unit.PERCENTAGE);
        spaceDivLayout.getStyle()
                .set("border-top", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);
    }

    public String getAttributeName(){
        return this.attributeName;
    }

    public String getAttributeMapping(){
        KindEntityAttributeRuntimeStatistics currentMapping = attributeRuntimeMappingSelect.getValue();
        return currentMapping != null ? currentMapping.getAttributeName() : null;
    }

    private Renderer<KindEntityAttributeRuntimeStatistics> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-l); color: var(--lumo-primary-text-color);\">${item.attributeKindName}</span>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-m);\">[${item.attributeKindDataType}]</span>");
        tpl.append("  </div>");
        tpl.append("</div>");
        return LitRenderer.<KindEntityAttributeRuntimeStatistics>of(tpl.toString())
                .withProperty("attributeKindName", KindEntityAttributeRuntimeStatistics::getAttributeName)
                .withProperty("attributeKindDataType",KindEntityAttributeRuntimeStatistics::getAttributeDataType);
    }

    public boolean isValid(){
        KindEntityAttributeRuntimeStatistics currentMapping = attributeRuntimeMappingSelect.getValue();
        return currentMapping != null && currentMapping.getAttributeName() != null;
    }

    private List<KindEntityAttributeRuntimeStatistics> filterTypeMatchedAttributeRuntimeMapping(List<KindEntityAttributeRuntimeStatistics> attributeRuntimeMapping){
        List<KindEntityAttributeRuntimeStatistics> filteredAttributeRuntimeMapping = new ArrayList<>();
        if(this.dataSlicePropertyType.equals(DataSlicePropertyType.STRING)){
            KindEntityAttributeRuntimeStatistics conceptionEntityUIDProperty =
                    new KindEntityAttributeRuntimeStatistics("ALL","DOCG_ConceptionEntity_UID","STRING",0,0);
            filteredAttributeRuntimeMapping.add(conceptionEntityUIDProperty);
        }
        for(KindEntityAttributeRuntimeStatistics currentKindEntityAttributeRuntimeStatistics : attributeRuntimeMapping){
            AttributeDataType attributeDataType = currentKindEntityAttributeRuntimeStatistics.getAttributeDataType();
            switch(this.dataSlicePropertyType){
                case STRING :
                    filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    break;
                case BOOLEAN:
                    if(attributeDataType.equals(AttributeDataType.BOOLEAN)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case INT:
                    if(attributeDataType.equals(AttributeDataType.INT) ||
                            attributeDataType.equals(AttributeDataType.SHORT)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case SHORT:
                    if(attributeDataType.equals(AttributeDataType.SHORT)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case LONG:
                    if(attributeDataType.equals(AttributeDataType.LONG) ||
                            attributeDataType.equals(AttributeDataType.INT) ||
                            attributeDataType.equals(AttributeDataType.SHORT)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case FLOAT:
                    if(attributeDataType.equals(AttributeDataType.FLOAT) ||
                            attributeDataType.equals(AttributeDataType.LONG) ||
                            attributeDataType.equals(AttributeDataType.INT) ||
                            attributeDataType.equals(AttributeDataType.SHORT)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case DOUBLE:
                    if(attributeDataType.equals(AttributeDataType.DOUBLE) ||
                            attributeDataType.equals(AttributeDataType.FLOAT) ||
                            attributeDataType.equals(AttributeDataType.LONG) ||
                            attributeDataType.equals(AttributeDataType.INT) ||
                            attributeDataType.equals(AttributeDataType.SHORT)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case DATE:
                    if(attributeDataType.equals(AttributeDataType.DATE)||
                            attributeDataType.equals(AttributeDataType.DATETIME)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case TIME:
                    if(attributeDataType.equals(AttributeDataType.TIME)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case TIMESTAMP:
                    if(attributeDataType.equals(AttributeDataType.TIMESTAMP)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case BYTE:
                    if(attributeDataType.equals(AttributeDataType.BYTE)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case DECIMAL:
                    if(attributeDataType.equals(AttributeDataType.DECIMAL) ||
                            attributeDataType.equals(AttributeDataType.DOUBLE) ||
                            attributeDataType.equals(AttributeDataType.FLOAT) ||
                            attributeDataType.equals(AttributeDataType.LONG) ||
                            attributeDataType.equals(AttributeDataType.INT) ||
                            attributeDataType.equals(AttributeDataType.SHORT)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case BINARY:
                    if(attributeDataType.equals(AttributeDataType.BINARY)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case GEOMETRY:
                    if(attributeDataType.equals(AttributeDataType.STRING)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
                case UUID:
                    if(attributeDataType.equals(AttributeDataType.STRING)){
                        filteredAttributeRuntimeMapping.add(currentKindEntityAttributeRuntimeStatistics);
                    }
                    break;
            }
        }
        return filteredAttributeRuntimeMapping;
    }
}
