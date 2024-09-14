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
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.ArrayList;
import java.util.List;

public class AddRelationMatchLogicUI extends VerticalLayout {

    public interface AddRelationMatchLogicHelper{
        public void executeAddRelationMatchLogic(String relationKindName,String relationKindDesc,RelationDirection relationDirection);
    }

    private Dialog containerDialog;
    private ComboBox<KindMetaInfo> relationKindFilterSelect;
    private ComboBox<RelationDirection> relationDirectionComboBox;
    private NativeLabel errorMessage;
    private AddRelationMatchLogicHelper addRelationMatchLogicHelper;

    public AddRelationMatchLogicUI() {
        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top","3px").set("padding-bottom","3px");

        NativeLabel viewTitle = new NativeLabel("关系类型匹配逻辑信息:");
        viewTitle.getStyle().set("color","var(--lumo-contrast-50pct)").set("font-size","0.8rem");
        errorMessageContainer.add(viewTitle);
        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color","var(--lumo-error-text-color)").set("font-size","0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        this.relationKindFilterSelect = new ComboBox("关系类型 - RelationKind");
        this.relationKindFilterSelect.setPageSize(30);
        this.relationKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        this.relationKindFilterSelect.setRequiredIndicatorVisible(true);
        this.relationKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {
                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        this.relationKindFilterSelect.setRenderer(createRenderer());
        add(this.relationKindFilterSelect);

        relationDirectionComboBox = new ComboBox("关系方向 - RelationKind Direction");
        relationDirectionComboBox.setRequiredIndicatorVisible(true);
        List<RelationDirection> relationDirectionList = new ArrayList<>();
        relationDirectionList.add(RelationDirection.FROM);
        relationDirectionList.add(RelationDirection.TO);
        relationDirectionList.add(RelationDirection.TWO_WAY);
        relationDirectionComboBox.setItems(relationDirectionList);
        relationDirectionComboBox.setWidth(100, Unit.PERCENTAGE);
        relationDirectionComboBox.setValue(RelationDirection.TWO_WAY);
        relationDirectionComboBox.setAllowCustomValue(false);
        add(relationDirectionComboBox);

        Button confirmButton = new Button("确定添加关系类型匹配逻辑",new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                errorMessage.setVisible(false);
                if(relationKindFilterSelect.getValue()==null){
                    errorMessage.setText("请选择关系类型");
                    errorMessage.setVisible(true);
                }else if(relationDirectionComboBox.getValue()==null){
                    errorMessage.setText("请选择关系方向");
                    errorMessage.setVisible(true);
                }else{
                    doAddRelationMatchLogic();
                }
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            coreRealm.openGlobalSession();
            List<KindMetaInfo> runtimeRelationKindMetaInfoList = coreRealm.getRelationKindsMetaInfo();
            this.relationKindFilterSelect.setItems(runtimeRelationKindMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        } finally {
            coreRealm.closeGlobalSession();
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void doAddRelationMatchLogic(){
        if(addRelationMatchLogicHelper != null){
            KindMetaInfo selectedKind = this.relationKindFilterSelect.getValue();
            addRelationMatchLogicHelper.executeAddRelationMatchLogic(selectedKind.getKindName(),selectedKind.getKindDesc(),relationDirectionComboBox.getValue());
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

    public void setAddRelationMatchLogicHelper(AddRelationMatchLogicHelper addRelationMatchLogicHelper) {
        this.addRelationMatchLogicHelper = addRelationMatchLogicHelper;
    }
}
