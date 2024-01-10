package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationAttachKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.ArrayList;
import java.util.List;

public class CreateNewRelationAttachLinkLogicView extends VerticalLayout {
    private Dialog containerDialog;
    private NativeLabel errorMessage;

    private ComboBox<KindEntityAttributeRuntimeStatistics> sourceConceptionKindPropertyFilterSelect;

    private ComboBox<KindEntityAttributeRuntimeStatistics> targetConceptionKindPropertyFilterSelect;

    public CreateNewRelationAttachLinkLogicView(){
        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("新建关系附着逻辑规则信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);
        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        RadioButtonGroup<RelationAttachKind.LinkLogicType>  linkLogicTypeRadioButtonGroup = new RadioButtonGroup<>("构建关联关系实体匹配逻辑类型");
        linkLogicTypeRadioButtonGroup.setRequiredIndicatorVisible(true);
        List<RelationAttachKind.LinkLogicType> linkLogicTypeOptions = new ArrayList<>();
        linkLogicTypeOptions.add(RelationAttachKind.LinkLogicType.DEFAULT);
        linkLogicTypeOptions.add(RelationAttachKind.LinkLogicType.AND);
        linkLogicTypeOptions.add(RelationAttachKind.LinkLogicType.OR);
        linkLogicTypeRadioButtonGroup.setItems(linkLogicTypeOptions);
        add(linkLogicTypeRadioButtonGroup);

        sourceConceptionKindPropertyFilterSelect = new ComboBox<>("源概念类型匹配属性");
        sourceConceptionKindPropertyFilterSelect.setPageSize(30);
        sourceConceptionKindPropertyFilterSelect.setWidthFull();
        sourceConceptionKindPropertyFilterSelect.setRequiredIndicatorVisible(true);
        sourceConceptionKindPropertyFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });
        sourceConceptionKindPropertyFilterSelect.setRenderer(createRenderer());
        sourceConceptionKindPropertyFilterSelect.getStyle().set("--vaadin-combo-box-overlay-width", "270px");
        add(sourceConceptionKindPropertyFilterSelect);

        ComboBox<RelationAttachKind.LinkLogicCondition> linkLogicConditionSelect = new ComboBox<>("构建关联关系实体匹配计算规则");
        linkLogicConditionSelect.setRequiredIndicatorVisible(true);
        linkLogicConditionSelect.setWidthFull();
        List<RelationAttachKind.LinkLogicCondition> linkLogicConditionOptions = new ArrayList<>();

        linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.Equal);
        linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.GreaterThanEqual);
        linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.GreaterThan);
        linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.LessThanEqual);
        linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.LessThan);
        linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.NotEqual);
        //linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.RegularMatch);
        linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.BeginWithSimilar);
        linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.EndWithSimilar);
        linkLogicConditionOptions.add(RelationAttachKind.LinkLogicCondition.ContainSimilar);
        linkLogicConditionSelect.setItems(linkLogicConditionOptions);
        add(linkLogicConditionSelect);

        targetConceptionKindPropertyFilterSelect = new ComboBox<>("目标概念类型匹配属性");
        targetConceptionKindPropertyFilterSelect.setPageSize(30);
        targetConceptionKindPropertyFilterSelect.setWidthFull();
        targetConceptionKindPropertyFilterSelect.setRequiredIndicatorVisible(true);
        targetConceptionKindPropertyFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });
        targetConceptionKindPropertyFilterSelect.setRenderer(createRenderer());
        targetConceptionKindPropertyFilterSelect.getStyle().set("--vaadin-combo-box-overlay-width", "270px");
        add(targetConceptionKindPropertyFilterSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确定添加关系附着逻辑规则",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);

        loadPropertySelectorComboBox("EnterpriseMember","Firm");
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
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

    private void loadPropertySelectorComboBox(String sourceConceptionKindName, String targetConceptionKindName){
        int entityAttributesDistributionStatisticSampleRatio = 20000;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind sourceConceptionKind = coreRealm.getConceptionKind(sourceConceptionKindName);
        List<KindEntityAttributeRuntimeStatistics> sourceKindEntityAttributeRuntimeStatisticsList =
                sourceConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(targetConceptionKindName);
        List<KindEntityAttributeRuntimeStatistics> targetKindEntityAttributeRuntimeStatisticsList =
                targetConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
        coreRealm.closeGlobalSession();
        sourceConceptionKindPropertyFilterSelect.setItems(sourceKindEntityAttributeRuntimeStatisticsList);
        targetConceptionKindPropertyFilterSelect.setItems(targetKindEntityAttributeRuntimeStatisticsList);
    }
}
