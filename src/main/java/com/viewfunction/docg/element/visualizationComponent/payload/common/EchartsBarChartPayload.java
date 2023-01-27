package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

public class EchartsBarChartPayload implements JsonSerializable {

    private String[] category;
    private Double[] value;

    public EchartsBarChartPayload(){}

    public EchartsBarChartPayload(String[] category,Double[] value){
        setCategory(category);
        setValue(value);
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = Json.createObject();
        if (getCategory() != null) {
            JsonArray categoryArray = Json.createArray();
            obj.put("category", categoryArray);
            for(int i = 0; i < getCategory().length; i++) {
                categoryArray.set(i,getCategory()[i]);
            }
        }
        if (getValue() != null) {
            JsonArray valueArray = Json.createArray();
            obj.put("value", valueArray);
            for(int i = 0; i < getValue().length; i++) {
                valueArray.set(i,getValue()[i]);
            }
        }
        return obj;
    }

    @Override
    public JsonSerializable readJson(JsonObject jsonObject) {
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
