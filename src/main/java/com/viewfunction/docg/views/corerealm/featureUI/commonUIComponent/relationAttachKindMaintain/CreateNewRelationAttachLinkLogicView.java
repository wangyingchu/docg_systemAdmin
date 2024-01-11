package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachLinkLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationAttachKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;

public class CreateNewRelationAttachLinkLogicView extends VerticalLayout {
    private Dialog containerDialog;
    private NativeLabel errorMessage;
    private ComboBox<KindEntityAttributeRuntimeStatistics> sourceConceptionKindPropertyFilterSelect;
    private ComboBox<KindEntityAttributeRuntimeStatistics> targetConceptionKindPropertyFilterSelect;
    private RelationAttachKind relationAttachKind;
    private RadioButtonGroup<RelationAttachKind.LinkLogicType>  linkLogicTypeRadioButtonGroup;
    private ComboBox<RelationAttachKind.LinkLogicCondition> linkLogicConditionSelect;
    private CreateRelationAttachLinkLogicCallback createRelationAttachLinkLogicCallback;

    public void setCreateRelationAttachLinkLogicCallback(CreateRelationAttachLinkLogicCallback createRelationAttachLinkLogicCallback) {
        this.createRelationAttachLinkLogicCallback = createRelationAttachLinkLogicCallback;
    }

    public interface CreateRelationAttachLinkLogicCallback{
        public void onSuccess(RelationAttachLinkLogic resultRelationAttachLinkLogic);
    }

    public CreateNewRelationAttachLinkLogicView(RelationAttachKind relationAttachKind){
        this.relationAttachKind = relationAttachKind;
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

        linkLogicTypeRadioButtonGroup = new RadioButtonGroup<>("构建关联关系实体匹配逻辑类型");
        linkLogicTypeRadioButtonGroup.setRequiredIndicatorVisible(true);
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

        linkLogicConditionSelect = new ComboBox<>("构建关联关系实体匹配计算规则");
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
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateRelationAttachLinkLogic();
            }
        });

        loadInputElementsData();
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

    private void loadInputElementsData(){
        sourceConceptionKindPropertyFilterSelect.setHelperText(relationAttachKind.getSourceConceptionKindName());
        targetConceptionKindPropertyFilterSelect.setHelperText(relationAttachKind.getTargetConceptionKindName());
        int entityAttributesDistributionStatisticSampleRatio = 100000;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind sourceConceptionKind = coreRealm.getConceptionKind(relationAttachKind.getSourceConceptionKindName());
        List<KindEntityAttributeRuntimeStatistics> sourceKindEntityAttributeRuntimeStatisticsList =
                sourceConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(relationAttachKind.getTargetConceptionKindName());
        List<KindEntityAttributeRuntimeStatistics> targetKindEntityAttributeRuntimeStatisticsList =
                targetConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);

        boolean haveDefaultLogic = false;
        RelationAttachKind targetRelationAttachKind = coreRealm.getRelationAttachKind(relationAttachKind.getRelationAttachKindUID());
        List<RelationAttachLinkLogic> containsRelationAttachLinkLogicList =  targetRelationAttachKind.getRelationAttachLinkLogic();
        if(containsRelationAttachLinkLogicList != null){
            for(RelationAttachLinkLogic currentRelationAttachLinkLogic:containsRelationAttachLinkLogicList){
                RelationAttachKind.LinkLogicType currentLogicLinkLogicType =currentRelationAttachLinkLogic.getLinkLogicType();
                if(currentLogicLinkLogicType.equals(RelationAttachKind.LinkLogicType.DEFAULT)){
                    haveDefaultLogic = true;
                    break;
                }
            }
        }
        if(haveDefaultLogic){
            List<RelationAttachKind.LinkLogicType> linkLogicTypeOptions = new ArrayList<>();
            linkLogicTypeOptions.add(RelationAttachKind.LinkLogicType.AND);
            linkLogicTypeOptions.add(RelationAttachKind.LinkLogicType.OR);
            linkLogicTypeRadioButtonGroup.setItems(linkLogicTypeOptions);
            linkLogicTypeRadioButtonGroup.setValue(RelationAttachKind.LinkLogicType.AND);
        }else{
            List<RelationAttachKind.LinkLogicType> linkLogicTypeOptions = new ArrayList<>();
            linkLogicTypeOptions.add(RelationAttachKind.LinkLogicType.DEFAULT);
            linkLogicTypeRadioButtonGroup.setItems(linkLogicTypeOptions);
            linkLogicTypeRadioButtonGroup.setValue(RelationAttachKind.LinkLogicType.DEFAULT);
        }

        coreRealm.closeGlobalSession();
        sourceConceptionKindPropertyFilterSelect.setItems(sourceKindEntityAttributeRuntimeStatisticsList);
        targetConceptionKindPropertyFilterSelect.setItems(targetKindEntityAttributeRuntimeStatisticsList);
    }

    private void doCreateRelationAttachLinkLogic(){
        RelationAttachKind.LinkLogicType linkLogicType = linkLogicTypeRadioButtonGroup.getValue();
        KindEntityAttributeRuntimeStatistics sourceProperty = sourceConceptionKindPropertyFilterSelect.getValue();
        RelationAttachKind.LinkLogicCondition linkLogicCondition = linkLogicConditionSelect.getValue();
        KindEntityAttributeRuntimeStatistics targetProperty = targetConceptionKindPropertyFilterSelect.getValue();
        errorMessage.setVisible(false);
        if(linkLogicType == null || sourceProperty == null || linkLogicCondition == null || targetProperty == null){
            errorMessage.setText("所有待输入信息均为必选项");
            errorMessage.setVisible(true);
        }else{
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            RelationAttachKind targetRelationAttachKind = coreRealm.getRelationAttachKind(relationAttachKind.getRelationAttachKindUID());
            RelationAttachLinkLogic newRelationAttachLinkLogic = new RelationAttachLinkLogic();
            newRelationAttachLinkLogic.setLinkLogicType(linkLogicType);
            newRelationAttachLinkLogic.setSourceEntityLinkAttributeName(sourceProperty.getAttributeName());
            newRelationAttachLinkLogic.setLinkLogicCondition(linkLogicCondition);
            newRelationAttachLinkLogic.setTargetEntitiesLinkAttributeName(targetProperty.getAttributeName());
            RelationAttachLinkLogic resultRelationAttachLinkLogic = null;
            try {
                resultRelationAttachLinkLogic = targetRelationAttachKind.createRelationAttachLinkLogic(newRelationAttachLinkLogic);
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }finally {
                coreRealm.closeGlobalSession();
            }
            if(resultRelationAttachLinkLogic != null){
                CommonUIOperationUtil.showPopupNotification("为关系附着规则类型 "+relationAttachKind.getRelationAttachKindName() +" 添加关系附着逻辑规则成功", NotificationVariant.LUMO_SUCCESS);
                if(containerDialog != null){
                    containerDialog.close();
                }
                if(createRelationAttachLinkLogicCallback != null){
                    createRelationAttachLinkLogicCallback.onSuccess(resultRelationAttachLinkLogic);
                }
            }
            else{
                CommonUIOperationUtil.showPopupNotification("为关系附着规则类型 "+relationAttachKind.getRelationAttachKindName() +" 添加关系附着逻辑规则失败", NotificationVariant.LUMO_ERROR);
            }
        }
    }
}
