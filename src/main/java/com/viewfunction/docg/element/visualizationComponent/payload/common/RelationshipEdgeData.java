package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonObject;

public class RelationshipEdgeData implements JsonSerializable {

    private long weight;
    private String name;
    private String desc;
    private String id;
    private String source;
    private String target;

    public RelationshipEdgeData(){}

    public RelationshipEdgeData(String name,String desc,String id,String source,String target,long weight){
        setName(name);
        setDesc(desc);
        setId(id);
        setSource(source);
        setTarget(target);
        setWeight(weight);
    }

    public long increaseWeight(){
        setWeight(getWeight() + 1);
        return getWeight();
    }

    public long decreaseWeight(){
        setWeight(getWeight() - 1);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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
        if (getSource() != null) {
            obj.put("source", getSource());
        }
        if (getTarget() != null) {
            obj.put("target", getTarget());
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
