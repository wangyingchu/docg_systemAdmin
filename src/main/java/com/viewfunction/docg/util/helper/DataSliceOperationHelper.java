package com.viewfunction.docg.util.helper;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.DataSlicePropertyType;

public class DataSliceOperationHelper {

    public static DataSlicePropertyType getCorrespondingDataSlicePropertyType(AttributeDataType attributeDataType) {
        if(attributeDataType == null){
            return null;
        }else{
            DataSlicePropertyType correspondingDataSlicePropertyType = null;
            switch (attributeDataType){
                case STRING -> correspondingDataSlicePropertyType = DataSlicePropertyType.STRING;
                case BOOLEAN -> correspondingDataSlicePropertyType = DataSlicePropertyType.BOOLEAN;
                case INT -> correspondingDataSlicePropertyType = DataSlicePropertyType.INT;
                case SHORT -> correspondingDataSlicePropertyType = DataSlicePropertyType.SHORT;
                case LONG -> correspondingDataSlicePropertyType = DataSlicePropertyType.LONG;
                case FLOAT -> correspondingDataSlicePropertyType = DataSlicePropertyType.FLOAT;
                case DOUBLE -> correspondingDataSlicePropertyType = DataSlicePropertyType.DOUBLE;
                case TIMESTAMP -> correspondingDataSlicePropertyType = DataSlicePropertyType.TIMESTAMP;
                case DATE -> correspondingDataSlicePropertyType = DataSlicePropertyType.DATE;
                case DATETIME -> correspondingDataSlicePropertyType = DataSlicePropertyType.DATE;
                case TIME -> correspondingDataSlicePropertyType = DataSlicePropertyType.TIME;
                case BYTE -> correspondingDataSlicePropertyType = DataSlicePropertyType.BYTE;
                case DECIMAL -> correspondingDataSlicePropertyType = DataSlicePropertyType.DECIMAL;
            }
            return correspondingDataSlicePropertyType;
        }
    }
}
