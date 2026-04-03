package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.util.List;

public class EchartsTreeChartPayload implements JsonSerializable {

    private String name;
    private Integer value;
    private List<EchartsTreeChartPayload> children;
    private static final ObjectMapper mapper = new ObjectMapper();

    public EchartsTreeChartPayload(String name){
        this.name = name;
    }

    @Override
    public ObjectNode toJson() {
        ObjectNode obj = mapper.createObjectNode();
        if (getName() != null) {
            obj.put("name", getName());
        }
        if (getValue() != null) {
            obj.put("value", getValue());
        }
        if(getChildren() != null && getChildren().size() > 0){
            ArrayNode arrayNode = mapper.createArrayNode();
            obj.set("children",arrayNode);
            for(int i=0;i<getChildren().size();i++){
                arrayNode.insert(i,getChildren().get(i).toJson());
            }
        }
        return obj;
    }

    @Override
    public JsonSerializable readJson(JsonNode jsonNode) {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<EchartsTreeChartPayload> getChildren() {
        return children;
    }

    public void setChildren(List<EchartsTreeChartPayload> children) {
        this.children = children;
    }
}
