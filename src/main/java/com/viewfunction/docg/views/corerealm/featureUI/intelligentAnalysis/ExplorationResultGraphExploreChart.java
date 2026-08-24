package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentQueryResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
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

    private Map<String,Integer> targetConceptionEntityRelationCurrentQueryPageMap;
    private int currentQueryPageSize = 5;
    private Map<String,List<String>> expandedConceptionEntityUIDsMap;

    /**
     * 创建一个全尺寸的 NVL 图可视化组件。
     * 默认高度 100%，宽度自动撑满父容器。
     */
    public ExplorationResultGraphExploreChart() {
        getElement().getStyle()
                .set("display", "block")
                .set("width", "100%")
                .set("height", "100%");
        // 步骤 1/2：React 双击节点 -> expandRequest 状态变化 -> generateExpandData 生成数据并回传
        addStateChangeListener("expandRequest", ExpandRequest.class, this::handleExpandRequest);
        // ReactAdapterComponent 内置 setState/getState 双向同步能力
        // 步骤 3/4：React 单击选中节点 -> selectedNodeId 状态变化 -> onNodeSelected 打印日志
        addStateChangeListener("selectedNodeId", String.class, this::onNodeSelected);

        this.targetConceptionEntityRelationCurrentQueryPageMap = new HashMap<>();
        this.expandedConceptionEntityUIDsMap = new HashMap<>();
    }

    /*
    private static final String[] EXPAND_LABELS = {
            "Person_A", "Company_A", "Project_A", "Skill_A", "City_A"
    };
    private static final String[] EXPAND_REL_TYPES = {
            "KNOWS", "WORKS_AT", "LIVES_IN", "HAS_SKILL", "MANAGES", "OWNS"
    };
    */
    /**
     * 步骤 1：模拟数据生成方法。
     * <p>
     * 随机创建小批量（3~6 个）新节点，并从被双击的 {@code nodeId} 节点连出
     * 对应数量的关系，供 React NVL 组件双击展开时使用。
     *
     * @param nodeId 被双击的节点 id，新关系的 from 端
     * @return 新生成的节点和边数据
     */
    public ExpandData generateExpandData(String nodeId) {
        if (nodeId == null || nodeId.isBlank()) {
            throw new IllegalArgumentException("onceptionEntity UID 不能为空");
        }

        List<GraphNode> nodes = new ArrayList<>();
        List<GraphRel> rels = new ArrayList<>();

        /*
        // generate mock data
        Random random = new Random();
        int count = 3 + random.nextInt(4);
        String batchId = "java-expand-" + UUID.randomUUID().toString().substring(0, 8);
        for (int i = 0; i < count; i++) {
            String id = batchId + "-" + i;
            String label = EXPAND_LABELS[random.nextInt(EXPAND_LABELS.length)];
            nodes.add(new GraphNode(id, label + "_" + i, randomHslColor(random)));
            rels.add(new GraphRel(batchId + "-rel-" + i, nodeId, id,
                    EXPAND_REL_TYPES[random.nextInt(EXPAND_REL_TYPES.length)]));
        }
        */

        List<RelationEntity> resultRelations = loadAdditionalTargetConceptionEntityRelationData(nodeId);

        if(!expandedConceptionEntityUIDsMap.containsKey(nodeId)){
            expandedConceptionEntityUIDsMap.put(nodeId,new ArrayList<>());
        }
        List<String> expandedConceptionEntityUIDs = expandedConceptionEntityUIDsMap.get(nodeId);

        if(resultRelations != null){
            Random random = new Random();
            String currentColor = randomHslColor(random);
            resultRelations.forEach(currentRelation ->{
                String relationName = currentRelation.getRelationKindName();
                String relationUID = currentRelation.getRelationEntityUID();
                String fromConceptionEntityUID = currentRelation.getFromConceptionEntityUID();
                String fromConceptionEntityKind = currentRelation.getFromConceptionEntityKinds().get(0);
                String toConceptionEntityUID = currentRelation.getToConceptionEntityUID();
                String toConceptionEntityKind = currentRelation.getToConceptionEntityKinds().get(0);
                nodes.add(new GraphNode(fromConceptionEntityUID, fromConceptionEntityKind+":"+fromConceptionEntityUID, currentColor,true));
                nodes.add(new GraphNode(toConceptionEntityUID, toConceptionEntityKind+":"+toConceptionEntityUID, currentColor,true));
                rels.add(new GraphRel(relationUID, fromConceptionEntityUID,toConceptionEntityUID,relationName+":"+relationUID,true));
                if(nodeId.equals(fromConceptionEntityUID)){
                    expandedConceptionEntityUIDs.add(toConceptionEntityUID);
                }else{
                    expandedConceptionEntityUIDs.add(fromConceptionEntityUID);
                }
            });
        }

        if(!expandedConceptionEntityUIDs.isEmpty()){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            try {
                List<RelationEntity> resultRelationsOfExpandedEntities =crossKindDataOperator.getRelationsOfConceptionEntityPair(expandedConceptionEntityUIDs);
                if(resultRelationsOfExpandedEntities != null){
                    resultRelationsOfExpandedEntities.forEach(currentRelation ->{
                        String relationName = currentRelation.getRelationKindName();
                        String relationUID = currentRelation.getRelationEntityUID();
                        String fromConceptionEntityUID = currentRelation.getFromConceptionEntityUID();
                        String toConceptionEntityUID = currentRelation.getToConceptionEntityUID();
                        rels.add(new GraphRel(relationUID, fromConceptionEntityUID,toConceptionEntityUID,relationName+":"+relationUID,false));
                    });
                }
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
        return new ExpandData(nodes, rels);
    }

    /**
     * React 双击事件触发的状态监听回调：生成数据并通过 {@code expandResult}
     * 状态回传给 React。
     */
    private void handleExpandRequest(ExpandRequest request) {
        if (request == null || request.clickedNodeId() == null
                || request.clickedNodeId().isBlank()) {
            return;
        }
        ExpandData data = generateExpandData(request.clickedNodeId());
        setState("expandResult",
                new ExpandResult(request.requestId(), data.nodes(), data.rels()));
    }

    /**
     * 步骤 3：节点单击事件监听方法。
     * <p>
     * React 侧单击选中节点时，会通过 {@code selectedNodeId} 状态通道
     * 触发本方法，这里用 {@code System.out.println} 输出节点选中日志。
     *
     * @param nodeId 被选中的节点 id；再次单击同一节点取消选中时为 {@code null}
     */
    public void onNodeSelected(String nodeId) {
        System.out.println("[NvlGraphComponent] 节点选中事件: "
                + (nodeId == null || nodeId.isBlank() ? "(取消选中)" : nodeId));
    }

    // ============================================================
    // 通信数据结构（Jackson 序列化/反序列化）
    // ============================================================

    /** Java 生成并返回给 React 的节点 */
    public record GraphNode(String id, String caption, String color,boolean initialNode) {}

    /** Java 生成并返回给 React 的边 */
    public record GraphRel(String id, String from, String to, String caption,boolean initialRel) {}

    /** {@link #generateExpandData(String)} 的返回结果 */
    public record ExpandData(List<GraphNode> nodes, List<GraphRel> rels) {}

    /**
     * React → Java：双击节点扩展请求。
     * 字段名使用 clickedNodeId 而不是 nodeId，避免 Flow 的
     * MapSyncRpcHandler 把 nodeId 当作 StateNode 的数字引用。
     */
    public record ExpandRequest(String clickedNodeId, String requestId) {}

    /** Java → React：双击节点扩展结果（带 requestId 便于 React 关联请求） */
    public record ExpandResult(String requestId, List<GraphNode> nodes, List<GraphRel> rels) {}

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
        Map<String,String> conceptionEntitiesInfoMap = new HashMap<>();
        List<String> conceptionEntitiesUIDList = new ArrayList<>();
        List<NVLNodePayload> _NVLNodePayloadList = new ArrayList<>();
        List<NVLEdgePayload> _NVLEdgePayloadList = new ArrayList<>();

        for(Map<String, DynamicContentValue> currentContentValue:dynamicContentResultValueList){
            for(String currentAttributeName:attributeNames){
                Object valueObject = currentContentValue.get(currentAttributeName).getValueObject();
                if(DynamicContentValue.ContentValueType.CONCEPTION_ENTITY.equals(attributesValueTypeMap.get(currentAttributeName))){
                    ConceptionEntity conceptionEntity = (ConceptionEntity) valueObject;
                    conceptionEntitiesInfoMap.put(conceptionEntity.getConceptionEntityUID(),conceptionEntity.getConceptionKindName());
                    conceptionEntitiesUIDList.add(conceptionEntity.getConceptionEntityUID());
                    List<String> entityLabels = new ArrayList<>();
                    entityLabels.add(conceptionEntity.getConceptionKindName());
                    _NVLNodePayloadList.add(new NVLNodePayload(conceptionEntity.getConceptionEntityUID(),conceptionEntity.getConceptionKindName(),entityLabels,true));
                }
                if(DynamicContentValue.ContentValueType.RELATION_ENTITY.equals(attributesValueTypeMap.get(currentAttributeName))){
                    RelationEntity relationEntity = (RelationEntity) valueObject;
                }
                if(DynamicContentValue.ContentValueType.ENTITIES_PATH.equals(attributesValueTypeMap.get(currentAttributeName))){
                    EntitiesPath entitiesPath = (EntitiesPath) valueObject;
                }
            }
        }

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
                        relationEntity.getFromConceptionEntityUID(),relationEntity.getToConceptionEntityUID(),true));
            }
        }

        Map<String,Object>  graphExploreData= new HashMap<>();
        graphExploreData.put("nodes",_NVLNodePayloadList);
        graphExploreData.put("rels",_NVLEdgePayloadList);
        setState("chartData",graphExploreData);
    }

    private List<RelationEntity> loadAdditionalTargetConceptionEntityRelationData(String conceptionEntityUID){
        List<RelationEntity> totalKindsRelationEntitiesList = new ArrayList<>();
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        try{
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            List<String> conceptionEntityUIDsList = new ArrayList<>();
            conceptionEntityUIDsList.add(conceptionEntityUID);
            List<ConceptionEntity> resltEntitiesList = crossKindDataOperator.getConceptionEntitiesByUIDs(conceptionEntityUIDsList);
            if(resltEntitiesList != null && !resltEntitiesList.isEmpty()){
                ConceptionEntity targetEntity = resltEntitiesList.get(0);
                int currentEntityQueryPage = 1;
                if(targetConceptionEntityRelationCurrentQueryPageMap.containsKey(conceptionEntityUID)){
                    currentEntityQueryPage = targetConceptionEntityRelationCurrentQueryPageMap.get(conceptionEntityUID);
                }
                totalKindsRelationEntitiesList = new ArrayList<>();
                List<String> attachedRelationKinds = targetEntity.listAttachedRelationKinds();
                //List<String> attachedConceptionKinds = targetEntity.listAttachedConceptionKinds();
                QueryParameters relationshipQueryParameters = new QueryParameters();
                relationshipQueryParameters.setStartPage(currentEntityQueryPage);
                relationshipQueryParameters.setEndPage(currentEntityQueryPage+1);
                relationshipQueryParameters.setPageSize(currentQueryPageSize);
                for (String currentRelationKind : attachedRelationKinds) {
                    relationshipQueryParameters.setEntityKind(currentRelationKind);
                    List<RelationEntity> currentKindTargetRelationEntityList = targetEntity.getSpecifiedRelations(relationshipQueryParameters, RelationDirection.TWO_WAY);
                    totalKindsRelationEntitiesList.addAll(currentKindTargetRelationEntityList);
                }
                currentEntityQueryPage++;
                targetConceptionEntityRelationCurrentQueryPageMap.put(conceptionEntityUID,currentEntityQueryPage);
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        } finally{
            coreRealm.closeGlobalSession();
        }
        return totalKindsRelationEntitiesList;
    }

    private static String randomHslColor(Random random) {
        double h = random.nextInt(360) / 360.0;
        double s = (55 + random.nextInt(25)) / 100.0;
        double l = (42 + random.nextInt(16)) / 100.0;
        return hslToHex(h, s, l);
    }

    private static String hslToHex(double h, double s, double l) {
        double r, g, b;
        if (s == 0) {
            r = g = b = l;
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = hue2rgb(p, q, h + 1.0 / 3.0);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1.0 / 3.0);
        }
        return String.format("#%02x%02x%02x",
                Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }

    private static double hue2rgb(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1.0 / 6.0) return p + (q - p) * 6 * t;
        if (t < 1.0 / 2.0) return q;
        if (t < 2.0 / 3.0) return p + (q - p) * (2.0 / 3.0 - t) * 6;
        return p;
    }
}