package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntitiesJoinNewKindWidget extends VerticalLayout {

    private List<KindMetaInfo> conceptionKindsInfoList;
    private String conceptionKind;
    private String attributeName;
    private Checkbox exitCurrentConceptionKindCheckbox;
    public ConceptionEntitiesJoinNewKindWidget(String conceptionKind,String attributeName,Object entityAttributeValue,
                                               Number matchedEntitiesCount,List<KindMetaInfo> conceptionKindsMetaInfoList){
        this.conceptionKind = conceptionKind;
        this.attributeName = attributeName;
        this.setWidthFull();
        this.setSpacing(true);

        HorizontalLayout valueTitleContainer = new HorizontalLayout();
        this.add(valueTitleContainer);

        Icon valueIcon = VaadinIcon.EYE.create();
        valueIcon.setSize("12px");
        valueIcon.getStyle().set("padding-right","3px");
        valueTitleContainer.add(valueIcon);
        valueTitleContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        Span matchedEntitiesCountSpan = new Span(matchedEntitiesCount.toString());
        matchedEntitiesCountSpan.getElement().getThemeList().add("badge contrast");
        valueTitleContainer.add(matchedEntitiesCountSpan);

        H6 valueContentHeader = new H6(entityAttributeValue.toString());
        valueTitleContainer.add(valueContentHeader);

        HorizontalLayout newConceptionKindsContainer = new HorizontalLayout();
        newConceptionKindsContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.add(newConceptionKindsContainer);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        conceptionKindIcon.getStyle().set("padding-right","2px");
        conceptionKindIcon.setColor("var(--lumo-contrast-50pct)");
        newConceptionKindsContainer.add(conceptionKindIcon);

        MultiSelectComboBox conceptionKindFilterSelect = new MultiSelectComboBox();
        conceptionKindFilterSelect.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);
        conceptionKindFilterSelect.setSelectedItemsOnTop(true);
        conceptionKindFilterSelect.setPageSize(30);
        conceptionKindFilterSelect.setPlaceholder("选择要加入的概念类型");
        conceptionKindFilterSelect.setWidth(500, Unit.PIXELS);
        conceptionKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {
                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        conceptionKindFilterSelect.setRenderer(createRenderer());
        newConceptionKindsContainer.add(conceptionKindFilterSelect);

        this.conceptionKindsInfoList = new ArrayList<>();
        if(conceptionKindsMetaInfoList != null){
            conceptionKindsMetaInfoList.forEach(kindMetaInfo -> {
                if(!kindMetaInfo.getKindName().equals(conceptionKind)){
                    KindMetaInfo currentKindMetaInfo =
                            new KindMetaInfo(kindMetaInfo.getKindName(),kindMetaInfo.getKindDesc(),null,null,null,null,null);
                    conceptionKindsInfoList.add(currentKindMetaInfo);
                }
            });
        }
        conceptionKindFilterSelect.setItems(this.conceptionKindsInfoList);

        Button confirmButton = new Button("确认加入",new Icon(VaadinIcon.CHECK));
        confirmButton.setWidth(90,Unit.PIXELS);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newConceptionKindsContainer.add(confirmButton);
        confirmButton.addClickListener(buttonClickEvent -> {
            addInNewConceptionKinds();
        });
        exitCurrentConceptionKindCheckbox = new Checkbox("同时退出当前概念类型");
        newConceptionKindsContainer.add(exitCurrentConceptionKindCheckbox);

        this.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
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

    private void addInNewConceptionKinds(){




    }
}
