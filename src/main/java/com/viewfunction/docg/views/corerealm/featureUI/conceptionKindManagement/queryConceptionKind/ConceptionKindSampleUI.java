package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConceptionKindSampleUI extends VerticalLayout {

    private String conceptionKind;
    private int sampleCount;
    private IntegerField entitiesSampleCountField;
    private HorizontalLayout mainLayout;
    private NativeLabel resultNumberValue;
    private ConceptionEntitiesListView conceptionEntitiesListView;
    private ConceptionEntitiesRelationsChart conceptionEntitiesRelationsChart;

    public ConceptionKindSampleUI(String conceptionKind, int sampleCount) {
        this.conceptionKind = conceptionKind;
        this.sampleCount = sampleCount;

        List<Component> actionElementsList = new ArrayList<>();

        NativeLabel currentDisplayCountInfoMessage = new NativeLabel("当前采样数量:");
        currentDisplayCountInfoMessage.getStyle().set("font-size","10px").set("padding-left","5px");
        currentDisplayCountInfoMessage.addClassNames("text-tertiary");
        actionElementsList.add(currentDisplayCountInfoMessage);

        this.entitiesSampleCountField = new IntegerField();
        this.entitiesSampleCountField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        this.entitiesSampleCountField.setMin(1);
        this.entitiesSampleCountField.setStep(1);
        this.entitiesSampleCountField.setValue(this.sampleCount);
        actionElementsList.add(this.entitiesSampleCountField);

        Button resampleButton = new Button("重新采样");
        resampleButton.setIcon(VaadinIcon.REFRESH.create());
        resampleButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        actionElementsList.add(resampleButton);
        resampleButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doConceptionEntitiesSample();
            }
        });

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.CONTROLLER.create(), "数据采样设置",actionElementsList,null);
        add(secondaryTitleActionBar);

        mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(false);
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setWidthFull();
        add(mainLayout);

        VerticalLayout leftSideContainer = new VerticalLayout();
        leftSideContainer.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        leftSideContainer.setWidth(300,Unit.PIXELS);
        leftSideContainer.setSpacing(true);
        leftSideContainer.setMargin(false);
        leftSideContainer.setPadding(false);
        mainLayout.add(leftSideContainer);

        VerticalLayout rightSideContainer = new VerticalLayout();
        rightSideContainer.setSpacing(false);
        rightSideContainer.setMargin(false);
        rightSideContainer.setPadding(false);
        mainLayout.add(rightSideContainer);

        this.resultNumberValue = new NativeLabel("-");
        this.resultNumberValue.addClassNames("text-xs","font-bold");
        this.resultNumberValue.getStyle().set("padding-right","10px");

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(LineAwesomeIconsSvg.CUBES_SOLID.create(),"概念类型实体采样结果",this.resultNumberValue);
        filterTitle.getStyle().set("padding-left","10px");
        leftSideContainer.add(filterTitle);

        this.conceptionEntitiesListView = new ConceptionEntitiesListView(false);
        leftSideContainer.add(this.conceptionEntitiesListView);
        this.conceptionEntitiesRelationsChart = new ConceptionEntitiesRelationsChart();
        rightSideContainer.add(this.conceptionEntitiesRelationsChart);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            this.conceptionEntitiesListView.setHeight(receiver.getBodyClientHeight()-145, Unit.PIXELS);
        }));
        doConceptionEntitiesSample();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void doConceptionEntitiesSample(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            try {
                int realSampleCount = entitiesSampleCountField.getValue();
                int executeSampleCount = realSampleCount == this.sampleCount ? this.sampleCount : realSampleCount;
                Set<ConceptionEntity> conceptionEntitySet = targetConceptionKind.getRandomEntities(executeSampleCount);
                int resultConceptionEntitiesCount = conceptionEntitySet.size();
                this.resultNumberValue.setText(""+resultConceptionEntitiesCount);
                this.conceptionEntitiesListView.renderConceptionEntitiesList(conceptionEntitySet);
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
