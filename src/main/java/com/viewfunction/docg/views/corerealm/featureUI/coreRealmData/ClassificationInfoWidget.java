package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;

public class ClassificationInfoWidget extends HorizontalLayout {

    public ClassificationInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");

        VerticalLayout leftComponentContainer = new VerticalLayout();
        leftComponentContainer.setSpacing(false);
        leftComponentContainer.setMargin(false);
        add(leftComponentContainer);

        new PrimaryKeyValueDisplayItem(leftComponentContainer, FontAwesome.Solid.CIRCLE.create(),"分类数量:","300");

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关概念类型:","1,000,000,000");

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout2);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关关系类型:","1,000,000,000");

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout3);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Solid.CIRCLE.create(),"相关概念实体:","1,000,000,000");

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout4);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关属性视图类型:","1,000,000,000");

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout5);

        new SecondaryKeyValueDisplayItem(leftComponentContainer,FontAwesome.Regular.CIRCLE.create(),"相关属性类型:","1,000,000,000");

        HorizontalLayout spaceDivLayout6 = new HorizontalLayout();
        spaceDivLayout6.setHeight(15,Unit.PIXELS);
        leftComponentContainer.add(spaceDivLayout6);

        Label messageText = new Label("Top 3 Levels Classifications ->");
        leftComponentContainer.add(messageText);
        messageText.addClassNames("text-xs","text-tertiary");

        VerticalLayout rightComponentContainer = new VerticalLayout();
        rightComponentContainer.setSpacing(false);
        rightComponentContainer.setMargin(false);
        add(rightComponentContainer);
        this.setFlexGrow(1,rightComponentContainer);

        ClassificationsTreeChart classificationsTreeChart = new ClassificationsTreeChart();
        rightComponentContainer.add(classificationsTreeChart);
        rightComponentContainer.setHorizontalComponentAlignment(Alignment.START,classificationsTreeChart);
    }
}
