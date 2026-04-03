package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class EchartsPieChartPayload implements JsonSerializable {

    private String name;
    private Double value;
    private static final ObjectMapper mapper = new ObjectMapper();

    public EchartsPieChartPayload(){}

    public EchartsPieChartPayload(String name,Double value){
        setName(name);
        setValue(value);
    }

    @Override
    public ObjectNode toJson() {
        ObjectNode objectNode = mapper.createObjectNode();
        if (getName() != null) {
            objectNode.put("name", getName());
        }
        if (getValue() != null) {
            objectNode.put("value", getValue());
        }
        return objectNode;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
