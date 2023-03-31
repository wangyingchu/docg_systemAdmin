package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndex;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeSystemInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.SearchIndexInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator.SearchIndexType.*;

public class CreateKindIndexView extends VerticalLayout {

    private KindIndexConfigView.KindIndexType kindIndexType;
    private String kindName;
    private H6 errorMessage;
    private TextField kindIndexNameField;
    private ComboBox<SystemMaintenanceOperator.SearchIndexType> searchIndexTypeSelect;
    private MultiSelectComboBox<String> indexPropertiesComboBox;
    private Dialog containerDialog;

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

        String[] options = new String[]{"a","b","c"};
        this.indexPropertiesComboBox = new MultiSelectComboBox<>("索引包含属性 - SearchIndex Properties");
        this.indexPropertiesComboBox.setWidthFull();
        this.indexPropertiesComboBox.setRequired(true);
        this.indexPropertiesComboBox.setItems(options);
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
        Set<String> indexProperties = this.indexPropertiesComboBox.getValue();

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();

        List<AttributeSystemInfo> attributeSystemInfoList = systemMaintenanceOperator.getConceptionKindAttributesSystemInfo(this.kindName);
    }
}
