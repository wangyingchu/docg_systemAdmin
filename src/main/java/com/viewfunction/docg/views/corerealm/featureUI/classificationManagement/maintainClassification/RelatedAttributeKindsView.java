package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.text.NumberFormat;

public class RelatedAttributeKindsView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem attributeKindCountDisplayItem;
    public RelatedAttributeKindsView(String classificationName){
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"相关属性类型运行时信息");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();
        this.attributeKindCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"相关属性类型数量:",this.numberFormat.format(123456789));
    }
}
