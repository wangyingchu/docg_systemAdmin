package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;

public class ConceptionEntitySyntheticAbstractInfoView extends VerticalLayout{

    private VerticalLayout infoContentContainer;
    private Label conceptionKindLabel;
    private Label conceptionEntityUIDLabel;
    private SecondaryKeyValueDisplayItem relationCountDisplayItem;
    private SecondaryKeyValueDisplayItem inDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem outDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem isDenseDisplayItem;

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
        conceptionKindIcon.getStyle().set("padding-right","3px");
        conceptionKindNameInfoContainer.add(conceptionKindIcon);
        conceptionKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindIcon);
        conceptionKindLabel = new Label();
        conceptionKindLabel.addClassNames("text-s");
        conceptionKindLabel.getStyle().set("font-weight","bold");
        conceptionKindNameInfoContainer.add(conceptionKindLabel);
        conceptionKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindLabel);

        HorizontalLayout conceptionEntityUIDInfoContainer = new HorizontalLayout();
        infoContentContainer.add(conceptionEntityUIDInfoContainer);
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px");
        conceptionEntityUIDInfoContainer.add(conceptionEntityIcon);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityIcon);
        conceptionEntityUIDLabel = new Label();
        conceptionEntityUIDLabel.addClassNames("text-s");
        conceptionEntityUIDLabel.getStyle().set("font-weight","bold");
        conceptionEntityUIDInfoContainer.add(conceptionEntityUIDLabel);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityUIDLabel);

        HorizontalLayout titleLayout1 = new HorizontalLayout();
        infoContentContainer.add(titleLayout1);
        relationCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout1, VaadinIcon.LIST_OL.create(), "关联关系总量", "-");
        HorizontalLayout titleLayout2 = new HorizontalLayout();
        infoContentContainer.add(titleLayout2);
        inDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout2, VaadinIcon.ANGLE_DOUBLE_LEFT.create(), "关系入度", "-");
        HorizontalLayout titleLayout3 = new HorizontalLayout();
        infoContentContainer.add(titleLayout3);
        outDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout3, VaadinIcon.ANGLE_DOUBLE_RIGHT.create(), "关系出度", "-");
        HorizontalLayout titleLayout4 = new HorizontalLayout();
        infoContentContainer.add(titleLayout4);
        isDenseDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout4, VaadinIcon.BULLSEYE.create(), "是否稠密实体", "-");
    }

    public void renderConceptionEntitySyntheticAbstractInfo(String conceptionKindName,String conceptionEntityUID){
        conceptionKindLabel.setText(conceptionKindName);
        conceptionEntityUIDLabel.setText(conceptionEntityUID);
    }
}
