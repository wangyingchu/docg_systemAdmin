package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.util.List;

public class ClassificationRelatedDataQueryCriteriaView extends HorizontalLayout {

    public ClassificationRelatedDataQueryCriteriaView(){
        setSpacing(false);
        setMargin(false);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        add(filterTitle);
        setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80, Unit.PIXELS);

        ComboBox relationKindSelect = new ComboBox();
        relationKindSelect.addThemeVariants(TextFieldVariant.LUMO_SMALL);
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
        add(relationKindSelect);
        setVerticalComponentAlignment(Alignment.CENTER, relationKindSelect);

        HorizontalLayout divLayout1 = new HorizontalLayout();
        divLayout1.setWidth(6,Unit.PIXELS);
        add(divLayout1);

        ComboBox relationDirectionSelect = new ComboBox();
        relationDirectionSelect.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        relationDirectionSelect.setPageSize(3);
        relationDirectionSelect.setPlaceholder("选择关系方向");
        relationDirectionSelect.setWidth(100,Unit.PIXELS);
        relationDirectionSelect.setItems("FROM", "TO","TWO_WAY");
        add(relationDirectionSelect);

        Checkbox includeOffspringClassificationsCheckbox = new Checkbox();
        includeOffspringClassificationsCheckbox.setTooltipText("包含后代分类关联");
        add(includeOffspringClassificationsCheckbox);
        setVerticalComponentAlignment(Alignment.CENTER, includeOffspringClassificationsCheckbox);

        NativeLabel checkboxDescLabel = new NativeLabel("包含后代分类关联");
        checkboxDescLabel.addClassNames("text-tertiary");
        checkboxDescLabel.getStyle().set("font-size","0.6rem");
        checkboxDescLabel.setWidth(40,Unit.PIXELS);
        add(checkboxDescLabel);
        setVerticalComponentAlignment(Alignment.CENTER, checkboxDescLabel);

        TextField offspringLevelField = new TextField();
        offspringLevelField.setPlaceholder("后代分类层数");
        offspringLevelField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        offspringLevelField.setWidth(80,Unit.PIXELS);
        add(offspringLevelField);
        setVerticalComponentAlignment(Alignment.CENTER, offspringLevelField);

        Button searchClassificationsButton = new Button("查询",new Icon(VaadinIcon.SEARCH));
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        add(searchClassificationsButton);
        setVerticalComponentAlignment(Alignment.CENTER,searchClassificationsButton);
        searchClassificationsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //filterClassifications();
            }
        });
    }
}
