package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class RemoveEntitiesAttributeView extends VerticalLayout {

    public enum KindType {ConceptionKind,RelationKind,Classification}
    private Dialog containerDialog;
    private List<String> entityUIDsList;
    private String kindName;
    private RemoveEntitiesAttributeView.KindType entityKindType = RemoveEntitiesAttributeView.KindType.ConceptionKind;
    private FootprintMessageBar entityInfoFootprintMessageBar;
    private ComboBox<KindEntityAttributeRuntimeStatistics> queryCriteriaFilterSelect;


    public RemoveEntitiesAttributeView(String kindName, List<String> entityUIDsList, RemoveEntitiesAttributeView.KindType entityKindType){


        this.setMargin(false);
        this.setSpacing(false);

        this.kindName = kindName;
        this.entityKindType = entityKindType;


        if(entityKindType != null){
            this.entityKindType = entityKindType;
        }
        Icon kindIcon = VaadinIcon.CUBE.create();
        switch (this.entityKindType){
            case ConceptionKind ->  kindIcon = VaadinIcon.CUBE.create();
            case RelationKind -> kindIcon = VaadinIcon.CONNECT_O.create();
            case Classification -> kindIcon = VaadinIcon.TAGS.create();
        }
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        String kindNameStr = kindName != null ? kindName : "[...]";
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, kindNameStr));

        entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);


        queryCriteriaFilterSelect = new ComboBox();
        queryCriteriaFilterSelect.setPageSize(30);
        queryCriteriaFilterSelect.setPlaceholder("选择或输入待删数属性名称");
        queryCriteriaFilterSelect.setMinWidth(340, Unit.PIXELS);
        //queryCriteriaFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);

        queryCriteriaFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });

        queryCriteriaFilterSelect.addValueChangeListener(new HasValue.
                ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<KindEntityAttributeRuntimeStatistics>,
                KindEntityAttributeRuntimeStatistics>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<KindEntityAttributeRuntimeStatistics>,
                    KindEntityAttributeRuntimeStatistics> comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent) {
                KindEntityAttributeRuntimeStatistics changedItem = comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent.getValue();
                if(changedItem != null){
                    //queryCriteriaFilterSelect.setValue(null);
                    String selectedAttribute = changedItem.getAttributeName();
                    //addQueryConditionItem(selectedAttribute,changedItem.getAttributeDataType());
                }
            }
        });
        queryCriteriaFilterSelect.setRenderer(createRenderer());
        queryCriteriaFilterSelect.getStyle().set("--vaadin-combo-box-overlay-width", "270px");



        HorizontalLayout attributeValueFieldContainerLayout = new HorizontalLayout();
        add(attributeValueFieldContainerLayout);
       // attributeValueInputContainer  = new HorizontalLayout();
      //  attributeValueInputContainer.setSpacing(false);
       // attributeValueInputContainer.setMargin(false);
       // attributeValueInputContainer.setPadding(false);
        attributeValueFieldContainerLayout.add(queryCriteriaFilterSelect);


        add(attributeValueFieldContainerLayout);
        //buttonSpaceDivLayout.setFlexGrow(1,queryCriteriaFilterSelect);



        Button confirmButton = new Button("确定",new Icon(VaadinIcon.CHECK));
        confirmButton.setWidth(80,Unit.PIXELS);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        attributeValueFieldContainerLayout.add(confirmButton);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadQueryCriteriaComboBox();
    }


    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }


    private void loadQueryCriteriaComboBox(){
        if(entityKindType.equals(RemoveEntitiesAttributeView.KindType.ConceptionKind)){
            int entityAttributesDistributionStatisticSampleRatio = 100000;
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(kindName);
            List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList =
                    targetConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
            coreRealm.closeGlobalSession();
            queryCriteriaFilterSelect.setItems(kindEntityAttributeRuntimeStatisticsList);
        }
    }

    private Renderer<KindEntityAttributeRuntimeStatistics> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    ${item.attributeName}");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeDataType}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<KindEntityAttributeRuntimeStatistics>of(tpl.toString())
                .withProperty("attributeName", KindEntityAttributeRuntimeStatistics::getAttributeName)
                .withProperty("attributeDataType", KindEntityAttributeRuntimeStatistics::getAttributeDataType);
    }
}
