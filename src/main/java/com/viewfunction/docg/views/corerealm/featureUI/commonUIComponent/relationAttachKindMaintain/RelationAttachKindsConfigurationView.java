package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.relationAttachKindMaintain;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import java.util.ArrayList;
import java.util.List;

public class RelationAttachKindsConfigurationView extends VerticalLayout {

    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;

    public RelationAttachKindsConfigurationView(){
        this.setWidth(100, Unit.PERCENTAGE);

        setSpacing(false);
        setMargin(false);
        setPadding(false);
        this.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setSpacing(false);
        mainContainerLayout.setMargin(false);
        mainContainerLayout.setPadding(false);
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setWidth(800,Unit.PIXELS);

        mainContainerLayout.add(leftSideContainerLayout);










        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button attachAttributesViewKindButton= new Button("创建新的关联关系附着规则类型");
        attachAttributesViewKindButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        attachAttributesViewKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        attachAttributesViewKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAttachNewAttributesViewKindUI();
            }
        });
        buttonList.add(attachAttributesViewKindButton);

        Button refreshAttributesViewKindsButton = new Button("刷新关联的关系附着规则类型信息",new Icon(VaadinIcon.REFRESH));
        refreshAttributesViewKindsButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshAttributesViewKindsButton.addClickListener((ClickEvent<Button> click) ->{
            //refreshAttributesViewKindsInfo();
        });
        buttonList.add(refreshAttributesViewKindsButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TREE_TABLE),"关系附着规则类型配置管理 ",secTitleElementsList,buttonList);
        leftSideContainerLayout.add(metaConfigItemConfigActionBar);








        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setWidth(400, Unit.PIXELS);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);
        mainContainerLayout.add(rightSideContainerLayout);

        HorizontalLayout spaceDiv01Layout1 = new HorizontalLayout();
        spaceDiv01Layout1.setHeight(10,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout1);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"选中关系附着规则类型概览");
        rightSideContainerLayout.add(filterTitle);

        HorizontalLayout spaceDiv01Layout2 = new HorizontalLayout();
        spaceDiv01Layout2.setHeight(2,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout2);

        SecondaryTitleActionBar selectedAttributesViewKindTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TREE_TABLE),"-",null,null,false);
        selectedAttributesViewKindTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.add(selectedAttributesViewKindTitleActionBar);

    }
}
