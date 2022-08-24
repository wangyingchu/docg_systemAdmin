package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;

public class ConceptionEntitySyntheticAbstractInfoView extends VerticalLayout{

    private VerticalLayout infoContentContainer;
    Label conceptionKindLabel;
    Label conceptionEntityUIDLabel;

    public ConceptionEntitySyntheticAbstractInfoView(){
        setSpacing(false);
        setMargin(false);
        setPadding(false);
        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.POINTER)," 已选中实体综合信息");
        add(infoTitle);

        HorizontalLayout horizontalDiv01 = new HorizontalLayout();
        horizontalDiv01.setWidthFull();
        horizontalDiv01.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(horizontalDiv01);

        infoContentContainer = new VerticalLayout();
        infoContentContainer.setSpacing(false);
        infoContentContainer.setMargin(false);
        add(infoContentContainer);

        HorizontalLayout conceptionKindNameInfoContainer = new HorizontalLayout();
        infoContentContainer.add(conceptionKindNameInfoContainer);
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("16px");
        conceptionKindIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        conceptionKindNameInfoContainer.add(conceptionKindIcon);
        conceptionKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindIcon);
        conceptionKindLabel = new Label();
        conceptionKindLabel.addClassNames("text-xs");
        conceptionKindLabel.getStyle().set("font-weight","bold");
        conceptionKindNameInfoContainer.add(conceptionKindLabel);
        conceptionKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindLabel);

        HorizontalLayout conceptionEntityUIDInfoContainer = new HorizontalLayout();
        infoContentContainer.add(conceptionEntityUIDInfoContainer);
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        conceptionEntityUIDInfoContainer.add(conceptionEntityIcon);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityIcon);
        conceptionEntityUIDLabel = new Label();
        conceptionEntityUIDLabel.addClassNames("text-xs");
        conceptionEntityUIDLabel.getStyle().set("font-weight","bold");
        conceptionEntityUIDInfoContainer.add(conceptionEntityUIDLabel);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityUIDLabel);
    }

    public void renderConceptionEntitySyntheticAbstractInfo(String conceptionKindName,String conceptionEntityUID){
        conceptionKindLabel.setText(conceptionKindName);
        conceptionEntityUIDLabel.setText(conceptionEntityUID);
    }
}
