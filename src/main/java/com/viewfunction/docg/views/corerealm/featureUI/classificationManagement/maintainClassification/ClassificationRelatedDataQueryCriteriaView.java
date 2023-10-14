package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.List;

public class ClassificationRelatedDataQueryCriteriaView extends HorizontalLayout {
    public interface ClassificationRelatedDataQueryHelper{
        void executeQuery(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel);
    }
    private ClassificationRelatedDataQueryHelper classificationRelatedDataQueryHelper;
    private ComboBox<KindMetaInfo> relationKindSelect;
    private ComboBox relationDirectionSelect;
    private Checkbox includeOffspringClassificationsCheckbox;
    private TextField offspringLevelField;

    public ClassificationRelatedDataQueryCriteriaView(){
        setSpacing(false);
        setMargin(false);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.SEARCH),"查询条件");
        add(filterTitle);
        setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80, Unit.PIXELS);

        relationKindSelect = new ComboBox();
        relationKindSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        relationKindSelect.setPageSize(30);
        relationKindSelect.setPlaceholder("选择关系类型定义");
        relationKindSelect.setWidth(315,Unit.PIXELS);
        relationKindSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo kindMetaInfo) {
                String itemLabelValue = kindMetaInfo.getKindName()+ " ("+
                        kindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<KindMetaInfo> reltionKindsList = coreRealm.getRelationKindsMetaInfo();
            relationKindSelect.setItems(reltionKindsList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        relationKindSelect.setRenderer(createRenderer());
        add(relationKindSelect);
        setVerticalComponentAlignment(Alignment.CENTER, relationKindSelect);

        HorizontalLayout divLayout1 = new HorizontalLayout();
        divLayout1.setWidth(6,Unit.PIXELS);
        add(divLayout1);

        relationDirectionSelect = new ComboBox();
        relationDirectionSelect.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationDirectionSelect.setPageSize(3);
        relationDirectionSelect.setPlaceholder("选择关系方向");
        relationDirectionSelect.setWidth(100,Unit.PIXELS);
        relationDirectionSelect.setItems("FROM", "TO","TWO_WAY");
        add(relationDirectionSelect);

        includeOffspringClassificationsCheckbox = new Checkbox();
        add(includeOffspringClassificationsCheckbox);
        setVerticalComponentAlignment(Alignment.CENTER, includeOffspringClassificationsCheckbox);
        includeOffspringClassificationsCheckbox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> checkboxBooleanComponentValueChangeEvent) {
                boolean lastValue = checkboxBooleanComponentValueChangeEvent.getValue();
                if(lastValue){
                    offspringLevelField.setValue("1");
                    offspringLevelField.setEnabled(true);
                }else{
                    offspringLevelField.setValue("");
                    offspringLevelField.setEnabled(false);
                }
            }
        });

        NativeLabel checkboxDescLabel = new NativeLabel("包含后代分类关联");
        checkboxDescLabel.addClassNames("text-tertiary");
        checkboxDescLabel.getStyle().set("font-size","0.6rem");
        checkboxDescLabel.setWidth(40,Unit.PIXELS);
        add(checkboxDescLabel);
        setVerticalComponentAlignment(Alignment.CENTER, checkboxDescLabel);

        offspringLevelField = new TextField();
        offspringLevelField.setPlaceholder("后代分类层数");
        offspringLevelField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        offspringLevelField.setWidth(80,Unit.PIXELS);
        offspringLevelField.setEnabled(false);
        add(offspringLevelField);
        setVerticalComponentAlignment(Alignment.CENTER, offspringLevelField);

        Button searchClassificationsButton = new Button("查询",new Icon(VaadinIcon.SEARCH));
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        searchClassificationsButton.setWidth(60,Unit.PIXELS);
        add(searchClassificationsButton);
        setVerticalComponentAlignment(Alignment.CENTER,searchClassificationsButton);
        searchClassificationsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                executeQueryLogic();
            }
        });
    }

    private Renderer<KindMetaInfo> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-m); color: var(--lumo-primary-text-color);\">${item.attributeKindName}</span>");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeKindDesc}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<KindMetaInfo>of(tpl.toString())
                .withProperty("attributeKindName", KindMetaInfo::getKindName)
                .withProperty("attributeKindDesc", KindMetaInfo::getKindDesc);
    }

    public ClassificationRelatedDataQueryHelper getClassificationRelatedDataQueryHelper() {
        return classificationRelatedDataQueryHelper;
    }

    public void setClassificationRelatedDataQueryHelper(ClassificationRelatedDataQueryHelper classificationRelatedDataQueryHelper) {
        this.classificationRelatedDataQueryHelper = classificationRelatedDataQueryHelper;
    }

    private void executeQueryLogic(){
        if(this.relationKindSelect.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("请选择关系类型定义", NotificationVariant.LUMO_WARNING);
            return;
        }
        if(this.relationDirectionSelect.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("请选择关系方向", NotificationVariant.LUMO_WARNING);
            return;
        }
        String relationKindName = this.relationKindSelect.getValue().getKindName();
        RelationDirection relationDirection = null;
        String relationDirectionStr = this.relationDirectionSelect.getValue().toString();
        if(relationDirectionStr.equals("FROM")){
            relationDirection = RelationDirection.FROM;
        }else if(relationDirectionStr.equals("TO")){
            relationDirection = RelationDirection.TO;
        }else{
            relationDirection = RelationDirection.TWO_WAY;
        }
        boolean includeOffspringClassifications = includeOffspringClassificationsCheckbox.getValue();

        if(includeOffspringClassifications){
            String offspringLevelFieldStr = offspringLevelField.getValue();
            if (offspringLevelFieldStr.equals("")) {
                CommonUIOperationUtil.showPopupNotification("请输入后代分类层数", NotificationVariant.LUMO_WARNING);
                return;
            }else{
                try {
                    int offspringLevel = Integer.parseInt(offspringLevelFieldStr);
                    if(offspringLevel<1){
                        CommonUIOperationUtil.showPopupNotification("后代分类层数必须是大于等于 1 的整数", NotificationVariant.LUMO_ERROR);
                        return;
                    }else{
                        if(getClassificationRelatedDataQueryHelper()!= null){
                            getClassificationRelatedDataQueryHelper().executeQuery(relationKindName,relationDirection,true,offspringLevel);
                        }
                    }
                } catch(NumberFormatException e){
                    CommonUIOperationUtil.showPopupNotification("后代分类层数必须是大于等于 1 的整数", NotificationVariant.LUMO_ERROR);
                }
            }
        }else{
            if(getClassificationRelatedDataQueryHelper()!= null){
                getClassificationRelatedDataQueryHelper().executeQuery(relationKindName,relationDirection,false,0);
            }
        }
    }
}
