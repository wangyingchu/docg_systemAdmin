package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndex;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeSystemInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.SearchIndexInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator.SearchIndexType.*;

public class CreateKindIndexView extends VerticalLayout {

    private KindIndexConfigView.KindIndexType kindIndexType;
    private String kindName;
    private H6 errorMessage;
    private TextField kindIndexNameField;
    private ComboBox<SystemMaintenanceOperator.SearchIndexType> searchIndexTypeSelect;
    private MultiSelectComboBox<AttributeSystemInfo> indexPropertiesComboBox;
    private Dialog containerDialog;
    private KindIndexConfigView containerKindIndexConfigView;

    public CreateKindIndexView(KindIndexConfigView.KindIndexType kindIndexType, String kindName){
        this.kindIndexType = kindIndexType;
        this.kindName = kindName;
        this.setMargin(false);
        this.setWidthFull();

        Icon kindIcon = VaadinIcon.CUBE.create();
        String viewTitleText = "概念类型索引信息";
        switch (this.kindIndexType){
            case ConceptionKind :
                    kindIcon = VaadinIcon.CUBE.create();
                    viewTitleText = "概念类型索引信息";
                    break;
            case RelationKind :
                    kindIcon = VaadinIcon.CONNECT_O.create();
                    viewTitleText = "关系类型索引信息";
        }
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, kindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6(viewTitleText);
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.kindIndexNameField = new TextField("类型索引名称 - KindIndex Name");
        this.kindIndexNameField.setWidthFull();
        this.kindIndexNameField.setRequired(true);
        this.kindIndexNameField.setRequiredIndicatorVisible(true);
        this.kindIndexNameField.setTitle("请输入类型索引名称");
        add(this.kindIndexNameField);

        this.searchIndexTypeSelect = new ComboBox("索引类型 - SearchIndex Type");
        this.searchIndexTypeSelect.setWidthFull();
        this.searchIndexTypeSelect.setRequired(true);
        this.searchIndexTypeSelect.setRequiredIndicatorVisible(true);
        this.searchIndexTypeSelect.setPageSize(10);
        SystemMaintenanceOperator.SearchIndexType[] searchIndexTypeSelectOption = new SystemMaintenanceOperator.SearchIndexType[]{
                BTREE, FULLTEXT, LOOKUP
        };
        this.searchIndexTypeSelect.setItems(searchIndexTypeSelectOption);
        this.searchIndexTypeSelect.setValue(BTREE);
        add(this.searchIndexTypeSelect);

        ItemLabelGenerator<AttributeSystemInfo> itemLabelGenerator = new ItemLabelGenerator<AttributeSystemInfo>() {
            @Override
            public String apply(AttributeSystemInfo attributeSystemInfo) {
                return attributeSystemInfo.getAttributeName()+ " ("+attributeSystemInfo.getDataType()+")";
            }
        };
        this.indexPropertiesComboBox = new MultiSelectComboBox<>("索引包含属性 - SearchIndex Properties");
        this.indexPropertiesComboBox.setWidthFull();
        this.indexPropertiesComboBox.setRequired(true);
        this.indexPropertiesComboBox.setItemLabelGenerator(itemLabelGenerator);
        add(this.indexPropertiesComboBox);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认创建类型索引",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateKindIndex();
            }
        });
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void doCreateKindIndex(){
        String kindIndexName = this.kindIndexNameField.getValue();
        SystemMaintenanceOperator.SearchIndexType searchIndexType = this.searchIndexTypeSelect.getValue();
        Set<AttributeSystemInfo> indexProperties = this.indexPropertiesComboBox.getValue();
        boolean inputValidateResult = true;
        if(kindIndexName.equals("")){
            inputValidateResult = false;
            this.kindIndexNameField.setInvalid(true);
        }
        if(searchIndexType == null){
            inputValidateResult = false;
            this.searchIndexTypeSelect.setInvalid(true);
        }
        if(indexProperties.size() ==0){
            inputValidateResult = false;
            this.indexPropertiesComboBox.setInvalid(true);
        }
        if(inputValidateResult){
            hideErrorMessage();
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
            Set<SearchIndexInfo> searchIndexInfoSet = null;
            switch(this.kindIndexType){
                case ConceptionKind -> searchIndexInfoSet = systemMaintenanceOperator.listConceptionKindSearchIndex();
                case RelationKind -> searchIndexInfoSet = systemMaintenanceOperator.listRelationKindSearchIndex();
            }
            boolean indexNameValidate = true;
            for(SearchIndexInfo currentSearchIndexInfo:searchIndexInfoSet){
                String indexName = currentSearchIndexInfo.getIndexName();
                String kindName = currentSearchIndexInfo.getSearchKindName();
                if(this.kindName.equals(kindName) && kindIndexName.equals(indexName)){
                    showErrorMessage("类型索引 "+kindIndexName+" 已经存在");
                    indexNameValidate = false;
                }
            }
            if(indexNameValidate){
                Set<String> indexPropertiesSet = new HashSet<>();
                for(AttributeSystemInfo currentAttributeSystemInfo:indexProperties){
                    indexPropertiesSet.add(currentAttributeSystemInfo.getAttributeName());
                }
                boolean createIndexResult = false;
                try {
                    switch(this.kindIndexType){
                        case ConceptionKind -> createIndexResult = systemMaintenanceOperator.createConceptionKindSearchIndex(kindIndexName,searchIndexType,kindName,indexPropertiesSet);
                        case RelationKind -> createIndexResult = systemMaintenanceOperator.createRelationKindSearchIndex(kindIndexName,searchIndexType,kindName,indexPropertiesSet);
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
                if(createIndexResult){
                    if(this.containerDialog != null){
                        this.containerDialog.close();
                    }
                    if(containerKindIndexConfigView != null){
                        containerKindIndexConfigView.refreshKindIndex();
                    }
                    CommonUIOperationUtil.showPopupNotification("为类型 "+kindName+" 创建索引 "+kindIndexName+" 成功", NotificationVariant.LUMO_SUCCESS);
                }else{
                    CommonUIOperationUtil.showPopupNotification("为类型 "+kindName+" 创建索引 "+kindIndexName+" 失败", NotificationVariant.LUMO_ERROR);
                }
            }
        }else{
            showErrorMessage("请输入全部类型索引创建信息");
            CommonUIOperationUtil.showPopupNotification("类型索引创建信息输入错误",NotificationVariant.LUMO_ERROR);
        }
    }

    private void showErrorMessage(String errorMessageTxt){
        this.errorMessage.setText(errorMessageTxt);
        this.errorMessage.setVisible(true);
    }

    private void hideErrorMessage(){
        this.errorMessage.setVisible(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        List<AttributeSystemInfo> attributeSystemInfoList = systemMaintenanceOperator.getConceptionKindAttributesSystemInfo(this.kindName);
        this.indexPropertiesComboBox.setItems(attributeSystemInfoList);
    }

    public void setContainerKindIndexConfigView(KindIndexConfigView containerKindIndexConfigView) {
        this.containerKindIndexConfigView = containerKindIndexConfigView;
    }
}
