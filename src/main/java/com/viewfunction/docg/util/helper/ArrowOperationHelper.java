package com.viewfunction.docg.util.helper;

import org.apache.arrow.vector.types.DateUnit;
import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.TimeUnit;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;

import java.util.TimeZone;

public class ArrowOperationHelper {

    public static Field getArrowField(String fieldName, String fieldDataType){
        //BOOLEAN,INT,SHORT,LONG,FLOAT,DOUBLE,TIMESTAMP,DATE,DATETIME,TIME,STRING,BYTE,DECIMAL
        Field resultField = null;
        switch(fieldDataType){
            case "BOOLEAN":
                resultField = new Field(fieldName, FieldType.nullable(new ArrowType.Bool()),/*children*/ null);
                break;
            case "INT":
                resultField = new Field(fieldName, FieldType.nullable(new ArrowType.Int(32, true)),/*children*/ null);
                break;
            case "SHORT":
                resultField = new Field(fieldName,FieldType.nullable(new ArrowType.Int(16, true)),/*children*/ null);
                break;
            case "LONG":
                resultField = new Field(fieldName,FieldType.nullable(new ArrowType.Int(64, true)),/*children*/ null);
                break;
            case "FLOAT":
                resultField = new Field(fieldName,FieldType.nullable(new ArrowType.FloatingPoint(FloatingPointPrecision.SINGLE)),/*children*/ null);
                break;
            case "DOUBLE":
                resultField = new Field(fieldName,FieldType.nullable(new ArrowType.FloatingPoint(FloatingPointPrecision.DOUBLE)),/*children*/ null);
                break;
            case "TIMESTAMP":
                resultField = new Field(fieldName, FieldType.nullable(new ArrowType.Timestamp(TimeUnit.SECOND, TimeZone.getDefault().toString())),/*children*/ null);
                break;
            case "DATE":
                resultField = new Field(fieldName, FieldType.nullable(new ArrowType.Date(DateUnit.DAY)),/*children*/ null);
                break;
            case "DATETIME":
                resultField = new Field(fieldName, FieldType.nullable(new ArrowType.Date(DateUnit.MILLISECOND)),/*children*/ null);
                break;
            case "TIME":
                resultField = new Field(fieldName, FieldType.nullable(new ArrowType.Time(TimeUnit.SECOND,32)),/*children*/ null);
                break;
            case "STRING":
                resultField = new Field(fieldName, FieldType.nullable(new ArrowType.Utf8()),/*children*/ null);
                break;
            case "BYTE":
                resultField = new Field(fieldName, FieldType.nullable(new ArrowType.Int(8, true)),/*children*/ null);
                break;
            case "DECIMAL":
                resultField = new Field(fieldName, FieldType.nullable(new ArrowType.Decimal(1,1,1)),/*children*/ null);
                break;
        }
        return resultField;
    }
}
