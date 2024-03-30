package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialInfoAnalysis;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConceptionEntitiesGeospatialInfoAnalysisView extends VerticalLayout {
    private Registration listener;
    private IntegerField entitiesSampleCountField;
    private ConceptionEntitiesGeospatialScaleMapInfoChart conceptionEntitiesGeospatialScaleMapInfoChart;
    private int entitiesSampleCount = 100;

    private ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult;

    public ConceptionEntitiesGeospatialInfoAnalysisView(String kindName, GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel,
                                                        ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult) {
        this.conceptionEntitiesAttributesRetrieveResult = conceptionEntitiesAttributesRetrieveResult;
        List<Component> actionElementsList = new ArrayList<>();

        NativeLabel currentDisplayCountInfoMessage = new NativeLabel("当前采样数量:");
        currentDisplayCountInfoMessage.getStyle().set("font-size","10px").set("padding-left","5px");
        currentDisplayCountInfoMessage.addClassNames("text-tertiary");
        actionElementsList.add(currentDisplayCountInfoMessage);

        this.entitiesSampleCountField = new IntegerField();
        this.entitiesSampleCountField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        this.entitiesSampleCountField.setMin(1);
        this.entitiesSampleCountField.setStep(1);
        this.entitiesSampleCountField.setValue(this.entitiesSampleCount);
        actionElementsList.add(this.entitiesSampleCountField);

        Button resampleButton = new Button("重新采样");
        resampleButton.setIcon(VaadinIcon.REFRESH.create());
        resampleButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        actionElementsList.add(resampleButton);
        resampleButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                refreshMapSpatialInfo();
            }
        });

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.CONTROLLER.create(), "数据采样设置",actionElementsList,null);
        add(secondaryTitleActionBar);

        this.conceptionEntitiesGeospatialScaleMapInfoChart = new ConceptionEntitiesGeospatialScaleMapInfoChart(kindName,spatialScaleLevel,conceptionEntitiesAttributesRetrieveResult);
        add(this.conceptionEntitiesGeospatialScaleMapInfoChart);
        this.conceptionEntitiesGeospatialScaleMapInfoChart.renderMapAndSpatialInfo(getRandomEntitiesUID(entitiesSampleCount,this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues()));
    }

    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            conceptionEntitiesGeospatialScaleMapInfoChart.setHeight(receiver.getBodyClientHeight()-120, Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void refreshMapSpatialInfo(){
        if(entitiesSampleCountField.getValue() == null || entitiesSampleCountField.isInvalid()){
            entitiesSampleCountField.setValue(entitiesSampleCount);
        }
        int currentSampleCount = entitiesSampleCountField.getValue();
        List<String> conceptionEntitiesUIDList = getRandomEntitiesUID(currentSampleCount,this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues());
        this.conceptionEntitiesGeospatialScaleMapInfoChart.clearMap();
        if(conceptionEntitiesUIDList != null){
            this.conceptionEntitiesGeospatialScaleMapInfoChart.renderMapAndSpatialInfo(getRandomEntitiesUID(currentSampleCount,this.conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues()));
        }
    }

    private List<String> getRandomEntitiesUID(int targetEntitiesCount,List<ConceptionEntityValue> conceptionEntityValueList){
        List<String> targetUIDList = new ArrayList<>();
        while(targetUIDList.size() < targetEntitiesCount){
            int currentIdx = new Random().nextInt(conceptionEntityValueList.size());
            targetUIDList.add(conceptionEntityValueList.get(currentIdx).getConceptionEntityUID());
        }
        return targetUIDList;
    }
}
