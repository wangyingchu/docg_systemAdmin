package com.viewfunction.docg.util.helper;

import org.apache.arrow.vector.*;
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

    public static FieldVector getArrowVector(VectorSchemaRoot root,String fieldName, String fieldDataType){
        FieldVector resultVector = null;
        switch(fieldDataType){
            case "BOOLEAN":
                resultVector = (BitVector) root.getVector(fieldName);
                break;
            case "INT":
                resultVector = (IntVector) root.getVector(fieldName);
                break;
            case "SHORT":
                resultVector = (SmallIntVector) root.getVector(fieldName);
                break;
            case "LONG":
                resultVector = (BigIntVector) root.getVector(fieldName);
                break;
            case "FLOAT":
                resultVector = (Float4Vector) root.getVector(fieldName);
                break;
            case "DOUBLE":
                resultVector = (Float8Vector) root.getVector(fieldName);
                break;
            case "TIMESTAMP":
                resultVector = (TimeStampVector) root.getVector(fieldName);
                break;
            case "DATE":
                resultVector = (DateDayVector) root.getVector(fieldName);
                break;
            case "DATETIME":
                resultVector = (DateMilliVector) root.getVector(fieldName);
                break;
            case "TIME":
                resultVector = (TimeSecVector) root.getVector(fieldName);
                break;
            case "STRING":
                resultVector = (VarCharVector) root.getVector(fieldName);
                break;
            case "BYTE":
                resultVector = (TinyIntVector) root.getVector(fieldName);
                break;
            case "DECIMAL":
                resultVector = (DecimalVector) root.getVector(fieldName);
                break;
        }
        return resultVector;
    }

    public static void allocateNewArrowVector(FieldVector fieldVector, String fieldDataType,int allocateNewCount){
        switch (fieldDataType) {
            case "BOOLEAN":
                BitVector bitVector = (BitVector) fieldVector;
                bitVector.allocateNew(allocateNewCount);
                break;
            case "INT":
                IntVector intVector = (IntVector) fieldVector;
                intVector.allocateNew(allocateNewCount);
                break;
            case "SHORT":
                SmallIntVector smallIntVector = (SmallIntVector) fieldVector;
                smallIntVector.allocateNew(allocateNewCount);
                break;
            case "LONG":
                BigIntVector bigIntVector = (BigIntVector) fieldVector;
                bigIntVector.allocateNew(allocateNewCount);
                break;
            case "FLOAT":
                Float4Vector float4Vector = (Float4Vector) fieldVector;
                float4Vector.allocateNew(allocateNewCount);
                break;
            case "DOUBLE":
                Float8Vector float8Vector = (Float8Vector) fieldVector;
                float8Vector.allocateNew(allocateNewCount);
                break;
            case "TIMESTAMP":
                TimeStampVector timeStampVector = (TimeStampVector) fieldVector;
                timeStampVector.allocateNew(allocateNewCount);
                break;
            case "DATE":
                DateDayVector dateDayVector = (DateDayVector) fieldVector;
                dateDayVector.allocateNew(allocateNewCount);
                break;
            case "DATETIME":
                DateMilliVector dateMilliVector = (DateMilliVector) fieldVector;
                dateMilliVector.allocateNew(allocateNewCount);
                break;
            case "TIME":
                TimeSecVector timeSecVector = (TimeSecVector) fieldVector;
                timeSecVector.allocateNew(allocateNewCount);
                break;
            case "STRING":
                VarCharVector varCharVector = (VarCharVector) fieldVector;
                varCharVector.allocateNew(allocateNewCount);
                break;
            case "BYTE":
                TinyIntVector tinyIntVector = (TinyIntVector) fieldVector;
                tinyIntVector.allocateNew(allocateNewCount);
                break;
            case "DECIMAL":
                DecimalVector decimalVector = (DecimalVector) fieldVector;
                decimalVector.allocateNew(allocateNewCount);
                break;
        }
    }
}
