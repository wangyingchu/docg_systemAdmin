package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.text.NumberFormat;

public class RelatedConceptionKindsView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem conceptionKindCountDisplayItem;
    public RelatedConceptionKindsView(String classificationName){
        this.classificationName = classificationName;
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"相关概念类型运行时信息");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();
        this.conceptionKindCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"相关概念类型数量:",this.numberFormat.format(123456789));




        HorizontalLayout classificationsSearchElementsContainerLayout = new HorizontalLayout();
        classificationsSearchElementsContainerLayout.setSpacing(false);
        classificationsSearchElementsContainerLayout.setMargin(false);
        add(classificationsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        classificationsSearchElementsContainerLayout.add(filterTitle);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80, Unit.PIXELS);

        TextField classificationNameFilterField = new TextField();
        classificationNameFilterField.setPlaceholder("分类名称");
        classificationNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationNameFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationNameFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        classificationsSearchElementsContainerLayout.add(plusIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        TextField classificationDescFilterField = new TextField();
        classificationDescFilterField.setPlaceholder("分类描述");
        classificationDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationDescFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationDescFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationDescFilterField);

        Button searchClassificationsButton = new Button("查找分类",new Icon(VaadinIcon.SEARCH));
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(searchClassificationsButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchClassificationsButton);
        searchClassificationsButton.setWidth(90,Unit.PIXELS);
        searchClassificationsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //filterClassifications();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        classificationsSearchElementsContainerLayout.add(divIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //cancelFilterClassifications();
            }
        });



    }

    public void renderRelatedConceptionKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();

        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        //targetClassification

    }
}
