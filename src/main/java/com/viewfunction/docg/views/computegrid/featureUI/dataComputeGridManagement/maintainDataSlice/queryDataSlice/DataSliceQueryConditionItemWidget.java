package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice.queryDataSlice;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.DataSlicePropertyType;

public class DataSliceQueryConditionItemWidget extends VerticalLayout {

    private DataSliceQueryCriteriaView containerDataSliceQueryCriteriaView;
    private String propertyName;
    private DataSlicePropertyType propertyDataType;
    private boolean isFirstQueryCondition = false;

    public DataSliceQueryConditionItemWidget(String propertyName, DataSlicePropertyType propertyDataType, Binder<String> binder){




    }



    public void setContainerDataSliceQueryCriteriaView(DataSliceQueryCriteriaView containerDataSliceQueryCriteriaView) {
        this.containerDataSliceQueryCriteriaView = containerDataSliceQueryCriteriaView;
    }

    public void setAsDefaultQueryConditionItem(){
        this.isFirstQueryCondition = true;
        /*
        this.plusIcon.setSize("20px");
        this.multiIcon.setSize("16px");
        this.filteringLogicOrButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        this.filteringLogicAndButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        this.filteringLogicOrButton.removeThemeVariants(ButtonVariant.LUMO_CONTRAST);
        this.filteringLogicAndButton.removeThemeVariants(ButtonVariant.LUMO_CONTRAST);
        this.filteringLogicOrButton.setEnabled(false);
        this.filteringLogicAndButton.setEnabled(false);

        this.isDefaultLabel.setVisible(true);
        this.joinTypeLabel.setVisible(false);
        */

    }

    public DataSlicePropertyType getPropertyDataType() {
        return propertyDataType;
    }

    public String getPropertyName(){
        return this.propertyName;
    }

    public boolean isDefaultQueryConditionItem(){
        return this.isFirstQueryCondition;
    }
}
