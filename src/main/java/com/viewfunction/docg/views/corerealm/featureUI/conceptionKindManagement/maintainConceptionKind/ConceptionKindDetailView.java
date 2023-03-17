package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;

import java.util.ArrayList;
import java.util.List;

@Route("conceptionKindDetailInfo/:conceptionKind")
public class ConceptionKindDetailView extends VerticalLayout implements BeforeEnterObserver {

    private String conceptionKind;
    private int conceptionKindDetailViewHeightOffset = 135;

    public ConceptionKindDetailView(){}

    public ConceptionKindDetailView(String conceptionKind){
        this.conceptionKind = conceptionKind;
        renderConceptionKindData();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.conceptionKind = beforeEnterEvent.getRouteParameters().get("conceptionKind").get();
        this.conceptionKindDetailViewHeightOffset = 70;
    }

    private void renderConceptionKindData(){
        List<Component> conceptionKindManagementOperationButtonList = new ArrayList<>();
        Icon icon = new Icon(VaadinIcon.LIST);
       // SectionActionBar sectionActionBar = new SectionActionBar(icon,"概念类型定义:",conceptionKindManagementOperationButtonList);
        //add(sectionActionBar);

        List<Component> secTitleElementsList = new ArrayList<>();

        Label conceptionKindNameLabel = new Label(this.conceptionKind);

        conceptionKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-l)")
                //.set("color","#2e4e7e")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");




        secTitleElementsList.add(conceptionKindNameLabel);

        TextField textField = new TextField();

        secTitleElementsList.add(textField);


        Icon realmIcon = VaadinIcon.ARCHIVE.create();
        realmIcon.getStyle().set("padding", "var(--lumo-space-xs");
        Span realmNameSpan = new Span( realmIcon,new Span("Default CoreRealm"));
        realmNameSpan.addClassName("text-2xs");
        realmNameSpan.getElement().getThemeList().add("badge contrast");
        secTitleElementsList.add(realmNameSpan);

        List<Component> buttonList = new ArrayList<>();

        //TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Conception Kind 概念类型数据管理",secTitleElementsList,buttonList);
        //add(titleActionBar);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"Conception Kind 概念类型 - ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);
    }
}
