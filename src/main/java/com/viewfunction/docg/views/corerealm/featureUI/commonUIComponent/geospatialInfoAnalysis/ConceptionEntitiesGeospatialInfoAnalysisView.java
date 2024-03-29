package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.geospatialInfoAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntitiesGeospatialInfoAnalysisView extends VerticalLayout {
    private Registration listener;
    private ConceptionEntitiesGeospatialScaleMapInfoChart conceptionEntitiesGeospatialScaleMapInfoChart;
    public ConceptionEntitiesGeospatialInfoAnalysisView(String kindName, GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel,
                                                        ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult) {
        List<Component> actionElementsList = new ArrayList<>();

        IntegerField entitiesSampleCountField = new IntegerField();
        entitiesSampleCountField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        entitiesSampleCountField.setMin(1);
        entitiesSampleCountField.setStep(50);
        entitiesSampleCountField.setValue(100);
        actionElementsList.add(entitiesSampleCountField);

        Button resampleButton = new Button("重新采样");
        resampleButton.setIcon(VaadinIcon.REFRESH.create());
        resampleButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        actionElementsList.add(resampleButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.CONTROLLER.create(), "数据采样设置:",actionElementsList,null);
        add(secondaryTitleActionBar);

        this.conceptionEntitiesGeospatialScaleMapInfoChart = new ConceptionEntitiesGeospatialScaleMapInfoChart(kindName,spatialScaleLevel,conceptionEntitiesAttributesRetrieveResult);
        add(this.conceptionEntitiesGeospatialScaleMapInfoChart);

        this.conceptionEntitiesGeospatialScaleMapInfoChart.renderMapAndSpatialInfo();
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
}
