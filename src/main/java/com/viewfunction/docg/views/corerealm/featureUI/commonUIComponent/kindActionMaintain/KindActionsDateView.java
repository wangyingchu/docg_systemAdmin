package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindActionMaintain;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionAction;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KindActionsDateView extends VerticalLayout {

    public enum KindType {ConceptionKind,RelationKind}

    private String kindName;
    private int actionsDataViewHeightOffset;
    private KindType kindType;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;

    public KindActionsDateView(KindType kindType,String kindName,int actionsDataViewHeightOffset){
        this.kindName = kindName;
        this.kindType = kindType;
        this.actionsDataViewHeightOffset = actionsDataViewHeightOffset;

        this.setWidth(100, Unit.PERCENTAGE);








        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button attachAttributesViewKindButton= new Button("注册新的自定义动作");
        attachAttributesViewKindButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        attachAttributesViewKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        attachAttributesViewKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAttachNewAttributesViewKindUI();
            }
        });
        buttonList.add(attachAttributesViewKindButton);

        Button refreshAttributesViewKindsButton = new Button("刷新自定义动作信息",new Icon(VaadinIcon.REFRESH));
        refreshAttributesViewKindsButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshAttributesViewKindsButton.addClickListener((ClickEvent<Button> click) ->{
            //refreshAttributesViewKindsInfo();
        });
        buttonList.add(refreshAttributesViewKindsButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONTROLLER),"自定义动作配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);








    }

    public void renderActionDataUI(Set<ConceptionAction> actionSet){}
}
