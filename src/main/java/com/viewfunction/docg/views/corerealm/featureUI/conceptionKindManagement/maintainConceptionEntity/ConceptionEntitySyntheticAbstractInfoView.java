package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;

public class ConceptionEntitySyntheticAbstractInfoView extends VerticalLayout{

    public ConceptionEntitySyntheticAbstractInfoView(){
        setSpacing(false);
        setMargin(false);
        setPadding(false);
        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.POINTER)," 已选中实体综合信息");
        add(infoTitle);
    }

}
