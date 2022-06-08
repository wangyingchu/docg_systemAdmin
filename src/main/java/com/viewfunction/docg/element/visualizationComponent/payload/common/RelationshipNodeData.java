package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonObject;

public class RelationshipNodeData  implements JsonSerializable {

    private long weight;
    private String name;
    private String desc;
    private String category;
    private String id;

    public RelationshipNodeData(){}

    public RelationshipNodeData(String name,String desc,String id,String category,long weight){
        setName(name);
        setDesc(desc);
        setId(id);
        setCategory(category);
        setWeight(weight);
    }

    public long increaseWeight(){
        weight++;
        return getWeight();
    }

    public long decreaseWeight(){
        weight--;
        return getWeight();
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = Json.createObject();
        if (getName() != null) {
            obj.put("name", getName());
        }
        if (getDesc() != null) {
            obj.put("desc", getDesc());
        }
        if (getCategory() != null) {
            obj.put("category", getCategory());
        }
        if (getId() != null) {
            obj.put("id", getId());
        }
        obj.put("weight", getWeight());
        return obj;
    }

    @Override
    public JsonSerializable readJson(JsonObject jsonObject) {
        return null;
    }
}
