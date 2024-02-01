package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeTypeConvert;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class ConvertEntityAttributeToTemporalTypeView extends VerticalLayout {
    public enum KindType {ConceptionKind,RelationKind}
    private String kindName;
    private String attributeName;
    private KindType kindType;
    private Dialog containerDialog;
    private ComboBox<String> dateTimeFormatterSelect;

    public ConvertEntityAttributeToTemporalTypeView(KindType kindType,String kindName,String attributeName){
        this.kindName = kindName;
        this.attributeName = attributeName;
        this.kindType = kindType;
        this.setWidthFull();

        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");

        Icon attributeIcon = VaadinIcon.BULLETS.create();
        attributeIcon.setSize("12px");
        attributeIcon.getStyle().set("padding-left","3px").set("padding-right","2px");

        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon,this.kindName));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributeIcon,this.attributeName));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        dateTimeFormatterSelect = new ComboBox<>("时间日期定义格式");
        dateTimeFormatterSelect.setAllowCustomValue(true);
        dateTimeFormatterSelect.setItems(
                "yyyy-MM-dd HH:mm:ss",
                "yyyy/mm/dd hh:mm:ss",
                "MM/dd/yyyy HH:mm:ss a",
                "MM/dd/yyyy hh:mm:ss a",
                "yyyy/m/d h:mm:ss",
                "yyyy/m/dd h:mm:ss",
                "yyyyMMdd",
                "yyyy-MM-dd",
                "yyyymmdd",
                "yyyy/mm/dd",
                "yyyy/m/d"
        );
        dateTimeFormatterSelect.setPageSize(30);
        dateTimeFormatterSelect.setPlaceholder("选择或输入时间日期定义格式");
        dateTimeFormatterSelect.setWidthFull();
        add(dateTimeFormatterSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认转换数据类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //attachConceptionKindEntitiesToTimeFlow();
            }
        });
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }


}
