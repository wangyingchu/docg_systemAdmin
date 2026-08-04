package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentQueryResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.element.visualizationComponent.payload.common.NVLEdgePayload;
import com.viewfunction.docg.element.visualizationComponent.payload.common.NVLNodePayload;

import java.util.*;

/**
 * Vaadin 25.2 标准的 React 组件封装 —— 将 nvl-graphExplore 的 React 图可视化
 * 组件以 {@link ReactAdapterComponent} 方式注入 Flow 布局。
 *
 * <h3>架构链路</h3>
 * <pre>
 *   Java (VerticalLayout)
 *     └── ExplorationResultGraphExploreChart  (@Tag + @JsModule)
 *           └── nvl-graph-adapter.tsx  (ReactAdapterElement)
 *                 └── NvlGraphView.tsx  (React: InteractiveNvlWrapper)
 * </pre>
 *
 * <p>该组件可直接作为普通 Vaadin Component 使用：</p>
 * <pre>{@code
 *   VerticalLayout layout = new VerticalLayout();
 *   layout.add(new NvlGraphComponent());
 * }</pre>
 */
@NpmPackage(value = "@neo4j-nvl/base", version = "1.2.1")
@NpmPackage(value = "@neo4j-nvl/react", version = "1.2.1")
@Tag("nvl-graph-react")
@JsModule("./externalTech/flow/integration/react/nvlGraphExplore/nvl-graph-adapter.tsx")
public class ExplorationResultGraphExploreChart extends ReactAdapterComponent {

    /**
     * 创建一个全尺寸的 NVL 图可视化组件。
     * 默认高度 100%，宽度自动撑满父容器。
     */
    public ExplorationResultGraphExploreChart() {
        // ReactAdapterComponent 内置 setState/getState 双向同步能力，
        // 此处暂不需要与 React 侧状态同步，故构造函数为空。
        getElement().getStyle()
                .set("display", "block")
                .set("width", "100%")
                .set("height", "100%");
    }

    public void setGraphExploreData(DynamicContentQueryResult dynamicContentQueryResult){
        if(dynamicContentQueryResult != null){
            Map<String, DynamicContentValue.ContentValueType>  attributesValueTypeMap = dynamicContentQueryResult.getDynamicContentAttributesValueTypeMap();
            boolean containsGraphExploreData = false;
            Set<String> graphExploreDataAttributeNames = new HashSet<>();
            Set<String> keySet = attributesValueTypeMap.keySet();
            for(String currentAttribute:keySet){
                DynamicContentValue.ContentValueType attributeValueType = attributesValueTypeMap.get(currentAttribute);
                if(DynamicContentValue.ContentValueType.CONCEPTION_ENTITY.equals(attributeValueType) ||
                        DynamicContentValue.ContentValueType.RELATION_ENTITY.equals(attributeValueType) ||
                        DynamicContentValue.ContentValueType.ENTITIES_PATH.equals(attributeValueType)
                ){
                    containsGraphExploreData = true;
                    graphExploreDataAttributeNames.add(currentAttribute);
                }
            }
            if(containsGraphExploreData){
                List<Map<String, DynamicContentValue>>  dynamicContentResultValueList = dynamicContentQueryResult.getDynamicContentResultValueList();
                if(dynamicContentResultValueList != null && !dynamicContentResultValueList.isEmpty()){
                    processGraphExploreData(graphExploreDataAttributeNames,attributesValueTypeMap,dynamicContentResultValueList);
                }
            }
        }
    }

    private void processGraphExploreData(Set<String> attributeNames,
                                         Map<String, DynamicContentValue.ContentValueType> attributesValueTypeMap,
                                         List<Map<String, DynamicContentValue>>  dynamicContentResultValueList){
        System.out.println("=======================");
        System.out.println("=======================");

        Map<String,String> conceptionEntitiesInfoMap = new HashMap<>();
        List<String> conceptionEntitiesUIDList = new ArrayList<>();
        List<NVLNodePayload> _NVLNodePayloadList = new ArrayList<>();
        List<NVLEdgePayload> _NVLEdgePayloadList = new ArrayList<>();

        for(Map<String, DynamicContentValue> currentContentValue:dynamicContentResultValueList){
            for(String currentAttributeName:attributeNames){
                Object valueObject = currentContentValue.get(currentAttributeName).getValueObject();
                System.out.println(valueObject);
                if(DynamicContentValue.ContentValueType.CONCEPTION_ENTITY.equals(attributesValueTypeMap.get(currentAttributeName))){
                    ConceptionEntity conceptionEntity = (ConceptionEntity) valueObject;
                    conceptionEntitiesInfoMap.put(conceptionEntity.getConceptionEntityUID(),conceptionEntity.getConceptionKindName());
                    conceptionEntitiesUIDList.add(conceptionEntity.getConceptionEntityUID());
                    List<String> entityLabels = new ArrayList<>();
                    entityLabels.add(conceptionEntity.getConceptionKindName());
                    _NVLNodePayloadList.add(new NVLNodePayload(conceptionEntity.getConceptionEntityUID(),conceptionEntity.getConceptionKindName(),entityLabels));
                }
                if(DynamicContentValue.ContentValueType.RELATION_ENTITY.equals(attributesValueTypeMap.get(currentAttributeName))){
                    RelationEntity relationEntity = (RelationEntity) valueObject;
                }
                if(DynamicContentValue.ContentValueType.ENTITIES_PATH.equals(attributesValueTypeMap.get(currentAttributeName))){
                    EntitiesPath entitiesPath = (EntitiesPath) valueObject;
                }

            }
        }

        System.out.println(conceptionEntitiesInfoMap);

        CoreRealm targetCoreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<RelationEntity> additionalConceptionEntitiesRelations = null;
        CrossKindDataOperator crossKindDataOperator = targetCoreRealm.getCrossKindDataOperator();
        try {
            additionalConceptionEntitiesRelations = crossKindDataOperator.getRelationsOfConceptionEntityPair(conceptionEntitiesUIDList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        if(additionalConceptionEntitiesRelations != null){
            for(RelationEntity relationEntity:additionalConceptionEntitiesRelations){
                _NVLEdgePayloadList.add(new NVLEdgePayload(
                        relationEntity.getRelationEntityUID(),relationEntity.getRelationKindName(),
                        relationEntity.getFromConceptionEntityUID(),relationEntity.getToConceptionEntityUID()));

            }
        }

        System.out.println(additionalConceptionEntitiesRelations);

        //System.out.println("GraphExploreDataAttributeNames:"+graphExploreDataAttributeNames);
        //System.out.println("GraphExploreDataAttributeNames:"+graphExploreDataAttributeNames);
        //System.out.println("GraphExploreDataAttributeNames:"+graphExploreDataAttributeNames);
        System.out.println("=======================");
        System.out.println("=======================");

    }

}
