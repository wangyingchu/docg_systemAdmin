package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classification;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import java.util.ArrayList;
import java.util.List;

public class ClassificationConfigView extends VerticalLayout {

    public ClassificationConfigView(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);

        this.setWidth(100, Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();
        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAGS),"分类配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);
    }
}
