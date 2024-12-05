package com.viewfunction.docg.util.helper;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.filteringItem.EqualFilteringItem;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.spi.neo4j.termImpl.Neo4JAttributeKindImpl;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.DataService;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.term.DataSlicePropertyType;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.util.common.CoreRealmOperationUtil;
import com.viewfunction.docg.dataCompute.dataComputeServiceCore.util.factory.ComputeGridTermFactory;

import java.util.*;

public class DataSliceOperationHelper {

    public final static String GeospatialScaleDataSliceSystemGroup = "SYS_GS_GROUP";
    public final static String GeospatialScaleContinentDataSlice = "GS_ContinentDS";
    public final static String GeospatialScaleCountryRegionDataSlice = "GS_CountryRegionDS";
    public final static String GeospatialScaleProvinceDataSlice = "GS_ProvinceDS";
    public final static String GeospatialScalePrefectureDataSlice = "GS_PrefectureDS";
    public final static String GeospatialScaleCountyDataSlice = "GS_CountyDS";
    public final static String GeospatialScaleTownshipDataSlice  = "GS_TownshipDS";
    public final static String GeospatialScaleVillageDataSlice = "GS_VillageDS";

    public static class DataSliceOperationResult {

        private boolean operationResult;
        private String operationMessage;
        private Date operationStartTime;
        private Date operationEndTime;

        public DataSliceOperationResult() {}

        public boolean getOperationResult() {
            return operationResult;
        }

        public void setOperationResult(boolean operationResult) {
            this.operationResult = operationResult;
        }

        public String getOperationMessage() {
            return operationMessage;
        }

        public void setOperationMessage(String operationMessage) {
            this.operationMessage = operationMessage;
        }

        public Date getOperationStartTime() {
            return operationStartTime;
        }

        public void setOperationStartTime(Date operationStartTime) {
            this.operationStartTime = operationStartTime;
        }

        public Date getOperationEndTime() {
            return operationEndTime;
        }

        public void setOperationEndTime(Date operationEndTime) {
            this.operationEndTime = operationEndTime;
        }
    }

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

    public static DataSliceOperationResult syncGeospatialRegionToDataSlice(String geospatialRegionName){
        DataSliceOperationResult dataSliceOperationResult = new DataSliceOperationResult();
        dataSliceOperationResult.setOperationStartTime(new Date());
        int dataSyncPerLoadResultNum = 100000000;
        var queryParameters = new QueryParameters();
        queryParameters.setDefaultFilteringItem(new EqualFilteringItem(RealmConstant.GeospatialRegionProperty,geospatialRegionName));
        queryParameters.setResultNumber(dataSyncPerLoadResultNum);
        ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
        DataService dataService = null;
        try {
            dataService = targetComputeGrid.getDataService();
            List<String> existDataSlices = dataService.listDataSliceNames();
            if(existDataSlices.contains(DataSliceOperationHelper.GeospatialScaleContinentDataSlice)){
                dataSliceOperationResult.setOperationResult(false);
                dataSliceOperationResult.setOperationEndTime(new Date());
                dataSliceOperationResult.setOperationMessage("DataSlice with name "+DataSliceOperationHelper.GeospatialScaleContinentDataSlice +" already exist.");
                return dataSliceOperationResult;
            }
            if(existDataSlices.contains(DataSliceOperationHelper.GeospatialScaleCountryRegionDataSlice)){
                dataSliceOperationResult.setOperationResult(false);
                dataSliceOperationResult.setOperationEndTime(new Date());
                dataSliceOperationResult.setOperationMessage("DataSlice with name "+DataSliceOperationHelper.GeospatialScaleCountryRegionDataSlice +" already exist.");
                return dataSliceOperationResult;
            }
            if(existDataSlices.contains(DataSliceOperationHelper.GeospatialScaleProvinceDataSlice)){
                dataSliceOperationResult.setOperationResult(false);
                dataSliceOperationResult.setOperationEndTime(new Date());
                dataSliceOperationResult.setOperationMessage("DataSlice with name "+DataSliceOperationHelper.GeospatialScaleProvinceDataSlice +" already exist.");
                return dataSliceOperationResult;
            }
            if(existDataSlices.contains(DataSliceOperationHelper.GeospatialScalePrefectureDataSlice)){
                dataSliceOperationResult.setOperationResult(false);
                dataSliceOperationResult.setOperationEndTime(new Date());
                dataSliceOperationResult.setOperationMessage("DataSlice with name "+DataSliceOperationHelper.GeospatialScalePrefectureDataSlice +" already exist.");
                return dataSliceOperationResult;
            }
            if(existDataSlices.contains(DataSliceOperationHelper.GeospatialScaleCountyDataSlice)){
                dataSliceOperationResult.setOperationResult(false);
                dataSliceOperationResult.setOperationEndTime(new Date());
                dataSliceOperationResult.setOperationMessage("DataSlice with name "+DataSliceOperationHelper.GeospatialScaleCountyDataSlice +" already exist.");
                return dataSliceOperationResult;
            }
            if(existDataSlices.contains(DataSliceOperationHelper.GeospatialScaleTownshipDataSlice)){
                dataSliceOperationResult.setOperationResult(false);
                dataSliceOperationResult.setOperationEndTime(new Date());
                dataSliceOperationResult.setOperationMessage("DataSlice with name "+DataSliceOperationHelper.GeospatialScaleTownshipDataSlice +" already exist.");
                return dataSliceOperationResult;
            }
            if(existDataSlices.contains(DataSliceOperationHelper.GeospatialScaleVillageDataSlice)){
                dataSliceOperationResult.setOperationResult(false);
                dataSliceOperationResult.setOperationEndTime(new Date());
                dataSliceOperationResult.setOperationMessage("DataSlice with name "+DataSliceOperationHelper.GeospatialScaleVillageDataSlice +" already exist.");
                return dataSliceOperationResult;
            }

            // For Continent
            HashMap<String, DataSlicePropertyType> dataSlicePropertyMap = new HashMap<>();
            dataSlicePropertyMap.put("ISO_Code", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialChineseNameProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialEnglishNameProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("ChineseFullName", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialCodeProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialRegionProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialScaleGradeProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(CoreRealmOperationUtil.RealmGlobalUID, DataSlicePropertyType.STRING);
            List<AttributeKind> containsAttributesKinds = DataSliceOperationHelper.buildAttributeKindList(dataSlicePropertyMap);

            CoreRealmOperationUtil.syncInnerDataKindEntitiesToDataSlice(dataService,
                    RealmConstant.GeospatialScaleContinentEntityClass,
                    DataSliceOperationHelper.GeospatialScaleDataSliceSystemGroup,
                    containsAttributesKinds,
                    queryParameters,
                    DataSliceOperationHelper.GeospatialScaleContinentDataSlice,
                    true, 1) ;

            // For CountryRegion
            dataSlicePropertyMap = new HashMap<>();
            dataSlicePropertyMap.put("Alpha_2Code", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("Alpha_3Code", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("NumericCode", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("ISO3166_2Code", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialEnglishNameProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialChineseNameProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("belongedContinent", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("capitalChineseName", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("capitalEnglishName", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialCodeProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialRegionProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialScaleGradeProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGeometryType, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGlobalCRSAID, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGLGeometryContent, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(CoreRealmOperationUtil.RealmGlobalUID, DataSlicePropertyType.STRING);
            containsAttributesKinds = buildAttributeKindList(dataSlicePropertyMap);
            CoreRealmOperationUtil.syncInnerDataKindEntitiesToDataSlice(dataService,
                    RealmConstant.GeospatialScaleCountryRegionEntityClass,
                    DataSliceOperationHelper.GeospatialScaleDataSliceSystemGroup,
                    containsAttributesKinds,
                    queryParameters,
                    DataSliceOperationHelper.GeospatialScaleCountryRegionDataSlice,
                    true, 1);

            // For Province
            dataSlicePropertyMap = new HashMap<>();
            dataSlicePropertyMap.put("ISO3166_1Alpha_2Code", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("ISO3166_2SubDivisionCode", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("ISO3166_2SubdivisionName", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("ISO3166_2SubdivisionCategory", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialCodeProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialRegionProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialScaleGradeProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("DivisionCategory_EN", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("DivisionCategory_CH", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialEnglishNameProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialChineseNameProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGLGeometryPOI, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGlobalCRSAID, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGeometryType, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGLGeometryContent, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("ChinaDivisionCode", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialCLGeometryPOI, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialCountryCRSAID, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialCLGeometryContent, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(CoreRealmOperationUtil.RealmGlobalUID, DataSlicePropertyType.STRING);
            containsAttributesKinds = buildAttributeKindList(dataSlicePropertyMap);
            CoreRealmOperationUtil.syncInnerDataKindEntitiesToDataSlice(dataService,
                    RealmConstant.GeospatialScaleProvinceEntityClass,
                    DataSliceOperationHelper.GeospatialScaleDataSliceSystemGroup,
                    containsAttributesKinds,
                    queryParameters,
                    DataSliceOperationHelper.GeospatialScaleProvinceDataSlice,
                    true, 1);

            // For Prefecture
            dataSlicePropertyMap = new HashMap<>();
            dataSlicePropertyMap.put("ChinaParentDivisionCode", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("ChinaDivisionCode", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put("ChinaProvinceName", DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialCodeProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialRegionProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGeometryType, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGlobalCRSAID, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGLGeometryContent, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialCountryCRSAID, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialCLGeometryContent, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGLGeometryPOI, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialCLGeometryPOI, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialGLGeometryBorder, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant._GeospatialCLGeometryBorder, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialScaleGradeProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(RealmConstant.GeospatialChineseNameProperty, DataSlicePropertyType.STRING);
            dataSlicePropertyMap.put(CoreRealmOperationUtil.RealmGlobalUID, DataSlicePropertyType.STRING);
            containsAttributesKinds = buildAttributeKindList(dataSlicePropertyMap);
            CoreRealmOperationUtil.syncInnerDataKindEntitiesToDataSlice(dataService,
                    RealmConstant.GeospatialScalePrefectureEntityClass,
                    DataSliceOperationHelper.GeospatialScaleDataSliceSystemGroup,
                    containsAttributesKinds,
                    queryParameters,
                    DataSliceOperationHelper.GeospatialScalePrefectureDataSlice,
                    true, 1);

            // For County
            dataSlicePropertyMap.put("ChinaPrefectureName", DataSlicePropertyType.STRING);
            containsAttributesKinds = buildAttributeKindList(dataSlicePropertyMap);
            CoreRealmOperationUtil.syncInnerDataKindEntitiesToDataSlice(dataService,
                    RealmConstant.GeospatialScaleCountyEntityClass,
                    DataSliceOperationHelper.GeospatialScaleDataSliceSystemGroup,
                    containsAttributesKinds,
                    queryParameters,
                    DataSliceOperationHelper.GeospatialScaleCountyDataSlice,
                    true, 1);

            // For Township
            dataSlicePropertyMap.put("ChinaCountyName", DataSlicePropertyType.STRING);
            containsAttributesKinds = buildAttributeKindList(dataSlicePropertyMap);
            CoreRealmOperationUtil.syncInnerDataKindEntitiesToDataSlice(dataService,
                    RealmConstant.GeospatialScaleTownshipEntityClass,
                    DataSliceOperationHelper.GeospatialScaleDataSliceSystemGroup,
                    containsAttributesKinds,
                    queryParameters,
                    DataSliceOperationHelper.GeospatialScaleTownshipDataSlice,
                    true, 2);

            // For Village
            dataSlicePropertyMap.put("ChinaTownshipName", DataSlicePropertyType.STRING);
            containsAttributesKinds = buildAttributeKindList(dataSlicePropertyMap);
            CoreRealmOperationUtil.syncInnerDataKindEntitiesToDataSlice(dataService,
                    RealmConstant.GeospatialScaleVillageEntityClass,
                    DataSliceOperationHelper.GeospatialScaleDataSliceSystemGroup,
                    containsAttributesKinds,
                    queryParameters,
                    DataSliceOperationHelper.GeospatialScaleVillageDataSlice,
                    true,
                    3);
        } catch (ComputeGridException e0) {
            ComputeGridException e = e0;
            throw new RuntimeException(e);
        }finally {
            if (dataService != null) {
                try {
                    dataService.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        dataSliceOperationResult.setOperationEndTime(new Date());
        dataSliceOperationResult.setOperationResult(true);
        dataSliceOperationResult.setOperationMessage("syncGeospatialRegionToDataSlice operation success finished");
        return dataSliceOperationResult;
    }

    private static List<AttributeKind> buildAttributeKindList(Map<String, DataSlicePropertyType> dataSlicePropertyMap)  {
        ArrayList<AttributeKind> attributeKindList = new ArrayList<>();
        Set<String> dataSlicePropertyNameSet = dataSlicePropertyMap.keySet();
        for (String attributeKindName :dataSlicePropertyNameSet) {

            if (!(attributeKindName.equals(CoreRealmOperationUtil.RealmGlobalUID))) {
                DataSlicePropertyType mapValue = dataSlicePropertyMap.get(attributeKindName);
                AttributeDataType attributeDataType = null;
                switch(mapValue)  {
                    case BOOLEAN -> attributeDataType = AttributeDataType.BOOLEAN;
                    case INT ->  attributeDataType = AttributeDataType.INT;
                    case SHORT -> attributeDataType = AttributeDataType.SHORT;
                    case LONG -> attributeDataType = AttributeDataType.LONG;
                    case FLOAT -> attributeDataType = AttributeDataType.FLOAT;
                    case DOUBLE -> attributeDataType = AttributeDataType.DOUBLE;
                    case DATE -> attributeDataType = AttributeDataType.TIMESTAMP;
                    case STRING -> attributeDataType = AttributeDataType.STRING;
                    case BYTE -> attributeDataType = AttributeDataType.BYTE;
                    case DECIMAL -> attributeDataType = AttributeDataType.DECIMAL;
                    case BINARY -> attributeDataType = AttributeDataType.BINARY;
                    case GEOMETRY -> attributeDataType = AttributeDataType.STRING;
                    case UUID -> attributeDataType = AttributeDataType.STRING;
                }
                AttributeKind currentAttributeKind = new Neo4JAttributeKindImpl(null, attributeKindName, "", attributeDataType, null);
                attributeKindList.add(currentAttributeKind);
            }
        }
        return attributeKindList;
    }
}
