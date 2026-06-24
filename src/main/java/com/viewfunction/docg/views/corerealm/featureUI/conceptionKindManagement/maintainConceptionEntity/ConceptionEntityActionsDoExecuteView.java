package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;

import java.util.List;

public class ConceptionEntityActionsDoExecuteView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private ConceptionEntityActionsExecutionView containerConceptionEntityActionsExecutionView;
    private TabSheet externalDataAccessViewTabSheet;
    private int conceptionEntityExternalDataViewHeightOffset;

    public ConceptionEntityActionsDoExecuteView(String conceptionKind,String conceptionEntityUID,int conceptionEntityExternalDataViewHeightOffset){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityExternalDataViewHeightOffset = conceptionEntityExternalDataViewHeightOffset;
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
    }

    public ConceptionEntityActionsExecutionView getContainerExternalAttributesAccessView() {
        return containerConceptionEntityActionsExecutionView;
    }

    public void setContainerExternalAttributesAccessView(ConceptionEntityActionsExecutionView containerConceptionEntityExternalAttributesAccessView) {
        this.containerConceptionEntityActionsExecutionView = containerConceptionEntityExternalAttributesAccessView;
    }

    public void renderConceptionEntityActionsUI(List<AttributesViewKind> conceptionKindExternalAttributeViewList){
        /*
        externalDataAccessViewTabSheet = new TabSheet();
        externalDataAccessViewTabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_SMALL);
        externalDataAccessViewTabSheet.setWidthFull();
        add(externalDataAccessViewTabSheet);

        for(AttributesViewKind attributesViewKind:conceptionKindExternalAttributeViewList){
            Object processorID = attributesViewKind.getMetaConfigItem(RealmConstant.ExternalAttributesValueAccessProcessorID);
            String externalProcessorIDStr = processorID != null ? processorID.toString() : null;
            String externalDataType = processorID != null ? getExternalDataType(processorID.toString()):null;
            ExternalValueAttributeDataAccessView currentExternalValueAttributeDataAccessView =
                    new ExternalValueAttributeDataAccessView(this.conceptionKind,this.conceptionEntityUID,attributesViewKind.getAttributesViewKindUID(),externalProcessorIDStr,this.conceptionEntityExternalDataViewHeightOffset);
            externalDataAccessViewTabSheet.
                    add(generateTabTitle(VaadinIcon.TASKS,attributesViewKind.getAttributesViewKindName(),attributesViewKind.getAttributesViewKindDesc(),externalDataType),currentExternalValueAttributeDataAccessView);
        }
        */
    }
}
