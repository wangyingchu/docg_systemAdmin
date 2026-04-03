package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

public class EchartsBarChartPayload implements JsonSerializable {

    private String[] category;
    private Double[] value;
    private static final ObjectMapper mapper = new ObjectMapper();

    public EchartsBarChartPayload(){}

    public EchartsBarChartPayload(String[] category,Double[] value){
        setCategory(category);
        setValue(value);
    }

    @Override
    public ObjectNode toJson() {
        ObjectNode obj = mapper.createObjectNode();
        if (getCategory() != null) {
            ArrayNode categoryArray = mapper.createArrayNode();
            obj.set("category", categoryArray);
            for(int i = 0; i < getCategory().length; i++) {
                categoryArray.insert(i,getCategory()[i]);
            }
        }
        if (getValue() != null) {
            ArrayNode valueArray = mapper.createArrayNode();
            obj.set("value", valueArray);
            for(int i = 0; i < getValue().length; i++) {
                valueArray.insert(i,getValue()[i]);
            }
        }
        return obj;
    }

    @Override
    public JsonSerializable readJson(JsonNode jsonNode) {
        return null;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public Double[] getValue() {
        return value;
    }

    public void setValue(Double[] value) {
        this.value = value;
    }
}
