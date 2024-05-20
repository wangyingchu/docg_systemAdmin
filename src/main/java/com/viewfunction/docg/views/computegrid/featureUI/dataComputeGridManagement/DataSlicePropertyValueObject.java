package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement;

import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.DataSlicePropertyType;

public class DataSlicePropertyValueObject {
    private String propertyName;
    private DataSlicePropertyType dataSlicePropertyType;
    private DataSlicePropertyOperateHandler dataSlicePropertyOperateHandler;
    private boolean isPrimaryKey;

    public String getPropertyName() {
            return propertyName;
        }
    public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

    public DataSlicePropertyType getDataSlicePropertyType() {
            return dataSlicePropertyType;
        }

    public void setDataSlicePropertyType(DataSlicePropertyType dataSlicePropertyType) {
        this.dataSlicePropertyType = dataSlicePropertyType;
    }

    public boolean isPrimaryKey() {
            return isPrimaryKey;
        }
    public void setPrimaryKey(boolean primaryKey) {
            isPrimaryKey = primaryKey;
        }
}
