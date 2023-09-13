package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.List;

public class EchartsRadialTreeChartPayload implements JsonSerializable {
    private String name;
    private Integer value;
    private List<EchartsRadialTreeChartPayload> children;

    public EchartsRadialTreeChartPayload(String name){
        this.name = name;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = Json.createObject();
        if (getName() != null) {
            obj.put("name", getName());
        }
        if (getValue() != null) {
            obj.put("value", getValue());
        }
        if(getChildren() != null && getChildren().size() > 0){
            JsonArray childrenArray = Json.createArray();
            obj.put("children",childrenArray);
            for(int i=0;i<getChildren().size();i++){
                childrenArray.set(i,getChildren().get(i).toJson());
            }
        }
        return obj;
    }

    @Override
    public JsonSerializable readJson(JsonObject jsonObject) {
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

    public List<EchartsRadialTreeChartPayload> getChildren() {
        return children;
    }

    public void setChildren(List<EchartsRadialTreeChartPayload> children) {
        this.children = children;
    }
}
