package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonObject;

public class RelationshipNodeData  implements JsonSerializable {

    private int weight;
    private String name;
    private String desc;
    private String category;
    private String id;

    public RelationshipNodeData(){}

    public RelationshipNodeData(String name,String desc,String id,String category,int weight){
        setName(name);
        setDesc(desc);
        setId(id);
        setCategory(category);
        setWeight(weight);
    }

    public int increaseWeight(){
        weight++;
        return getWeight();
    }

    public int decreaseWeight(){
        weight--;
        return getWeight();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
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
