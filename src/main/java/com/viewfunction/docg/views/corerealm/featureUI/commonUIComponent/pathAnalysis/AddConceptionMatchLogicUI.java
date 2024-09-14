package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.ConceptionKindMatchLogic;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.ArrayList;
import java.util.List;

public class AddConceptionMatchLogicUI extends VerticalLayout {

    public interface AddConceptionMatchLogicHelper{
        public void executeAddConceptionMatchLogic(String conceptionKindName, String conceptionKindDesc, ConceptionKindMatchLogic.ConceptionKindExistenceRule conceptionKindExistenceRule);
    }

    private Dialog containerDialog;
    private ComboBox<KindMetaInfo> conceptionKindFilterSelect;
    private ComboBox<ConceptionKindMatchLogic.ConceptionKindExistenceRule> conceptionKindExistenceRuleComboBox;
    private NativeLabel errorMessage;
    private AddConceptionMatchLogicHelper addConceptionMatchLogicHelper;

    public AddConceptionMatchLogicUI() {
        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("概念类型匹配逻辑信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);
        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        this.conceptionKindFilterSelect = new ComboBox("概念类型 - ConceptionKind");
        this.conceptionKindFilterSelect.setPageSize(30);
        this.conceptionKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.conceptionKindFilterSelect.setRequiredIndicatorVisible(true);
        this.conceptionKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {
                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        this.conceptionKindFilterSelect.setRenderer(createRenderer());
        add(this.conceptionKindFilterSelect);

        conceptionKindExistenceRuleComboBox = new ComboBox("概念类型存在规则 - ConceptionKind ExistenceRule");
        conceptionKindExistenceRuleComboBox.setRequiredIndicatorVisible(true);
        List<ConceptionKindMatchLogic.ConceptionKindExistenceRule> conceptionKindExistenceRuleList = new ArrayList<>();
        conceptionKindExistenceRuleList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.MUST_HAVE);
        conceptionKindExistenceRuleList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.END_WITH);
        conceptionKindExistenceRuleList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.NOT_ALLOW);
        conceptionKindExistenceRuleList.add(ConceptionKindMatchLogic.ConceptionKindExistenceRule.TERMINATE_AT);

        conceptionKindExistenceRuleComboBox.setItems(conceptionKindExistenceRuleList);
        conceptionKindExistenceRuleComboBox.setWidth(100, Unit.PERCENTAGE);
        conceptionKindExistenceRuleComboBox.setValue(ConceptionKindMatchLogic.ConceptionKindExistenceRule.MUST_HAVE);
        conceptionKindExistenceRuleComboBox.setAllowCustomValue(false);
        add(conceptionKindExistenceRuleComboBox);

        Button confirmButton = new Button("确定添加概念类型匹配逻辑",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                errorMessage.setVisible(false);
                if(conceptionKindFilterSelect.getValue()==null){
                    errorMessage.setText("请选择概念类型");
                    errorMessage.setVisible(true);
                }else if(conceptionKindExistenceRuleComboBox.getValue()==null){
                    errorMessage.setText("请选择概念类型存在规则");
                    errorMessage.setVisible(true);
                }
                else{
                    doAddConceptionMatchLogic();
                }
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            coreRealm.openGlobalSession();
            List<KindMetaInfo> runtimeRelationKindMetaInfoList = coreRealm.getConceptionKindsMetaInfo();
            this.conceptionKindFilterSelect.setItems(runtimeRelationKindMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        } finally {
            coreRealm.closeGlobalSession();
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void doAddConceptionMatchLogic(){
        if(addConceptionMatchLogicHelper != null){
            KindMetaInfo selectedKind = this.conceptionKindFilterSelect.getValue();
            addConceptionMatchLogicHelper.executeAddConceptionMatchLogic(selectedKind.getKindName(),selectedKind.getKindDesc(), conceptionKindExistenceRuleComboBox.getValue());
        }
        if(this.containerDialog != null){
            this.containerDialog.close();
        }
    }

    private Renderer<KindMetaInfo> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-xl); color: var(--lumo-primary-text-color);\">${item.attributeKindName}</span>");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeKindDesc}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<KindMetaInfo>of(tpl.toString())
                .withProperty("attributeKindName", KindMetaInfo::getKindName)
                .withProperty("attributeKindDesc", KindMetaInfo::getKindDesc);
    }

    public void setAddConceptionMatchLogicHelper(AddConceptionMatchLogicHelper addConceptionMatchLogicHelper) {
        this.addConceptionMatchLogicHelper = addConceptionMatchLogicHelper;
    }
}
