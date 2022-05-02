package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConceptionKindManagementUI extends VerticalLayout {

    public ConceptionKindManagementUI(){
        /*
        add(new Label("ConceptionKindManagementUI"));
        H3 personalTitle = new H3("Personal information");
        personalTitle.setId("PERSONAL_TITLE_ID");

        TextField firstName = new TextField("First name");
        firstName.setWidthFull();

        TextField lastName = new TextField("Last name");
        lastName.setWidthFull();

        DatePicker birthDate = new DatePicker("Birthdate");
        birthDate.setInitialPosition(LocalDate.of(1990, 1, 1));
        birthDate.setWidthFull();

        Section personalInformation = new Section(personalTitle, firstName, lastName, birthDate);
        personalInformation.getElement().setAttribute("aria-labelledby", "PERSONAL_TITLE_ID");

        // Employment information
        H3 employementTitle = new H3("Employment information");
        employementTitle.setId("EMPLOYMENT_TITLE_ID");

        TextField position = new TextField("Position");
        position.setWidthFull();

        TextArea additionalInformation = new TextArea("Additional Information");
        additionalInformation.setWidthFull();

        Section employmentInformation = new Section(employementTitle, position, additionalInformation);
        employmentInformation.getElement().setAttribute("aria-labelledby", "EMPLOYMENT_TITLE_ID");

        // NOTE
        // We are using inline styles here to keep the example simple.
        // We recommend placing CSS in a separate style sheet and to
        // encapsulating the styling in a new component.
        Scroller scroller = new Scroller(new Div(personalInformation, employmentInformation));
        scroller.setHeight(150, Unit.PIXELS);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        scroller.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding", "var(--lumo-space-m)");
        add(scroller);
        */


        Button refreshDataButton = new Button("刷新概念类型数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();

        Label coreRealmNameLabel = new Label(" [ Default CoreRealm ]");
        coreRealmNameLabel.getStyle().set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-secondary-text-color)");
        secTitleElementsList.add(coreRealmNameLabel);

        //Label coreRealmTechLabel = new Label(" NEO4J 实现");
        //coreRealmTechLabel.addClassName("text-2xs");
        //secTitleElementsList.add(coreRealmTechLabel);
        //coreRealmTechLabel.getElement().getThemeList().add("badge success");

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Conception Kind 概念类型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> conceptionKindManagementOperationButtonList = new ArrayList<>();

        Button conceptionKindRelationGuideButton = new Button("概念类型定义导览",new Icon(VaadinIcon.SITEMAP));
        conceptionKindRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        conceptionKindRelationGuideButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindManagementOperationButtonList.add(conceptionKindRelationGuideButton);


        Button createConceptionKindButton = new Button("创建概念类型定义",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindManagementOperationButtonList.add(createConceptionKindButton);



        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"概念类型定义:",conceptionKindManagementOperationButtonList);
        add(sectionActionBar);


        loadConceptionKindsInfo();
    }

    private void loadConceptionKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<EntityStatisticsInfo>  entityStatisticsInfoList = coreRealm.getConceptionEntitiesStatistics();
        System.out.println(entityStatisticsInfoList);

        for(EntityStatisticsInfo currentEntityStatisticsInfo:entityStatisticsInfoList){
            System.out.println(currentEntityStatisticsInfo.getEntityKindName()+"-"+currentEntityStatisticsInfo.getEntitiesCount());
            System.out.println("-----------------------------");
        }
    }
}
