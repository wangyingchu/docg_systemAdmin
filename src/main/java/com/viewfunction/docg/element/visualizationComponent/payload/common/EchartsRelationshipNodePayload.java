package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class EchartsRelationshipNodePayload implements JsonSerializable {

    private long weight;
    private String name;
    private String desc;
    private String category;
    private String id;
    private HashMap<String, Object> data = new HashMap();

    public EchartsRelationshipNodePayload(){}

    public EchartsRelationshipNodePayload(String name, String desc, String id, String category, long weight){
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

    public HashMap<String, Object> getData() {
        return this.data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
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

        JsonObject dataObject = Json.createObject();
        if(getData().size() >0) {
            for(Map.Entry<String,Object> entry:getData().entrySet()){
                String propKey = entry.getKey();
                Object propValueObj = entry.getValue();
                if(propValueObj instanceof Boolean){
                    dataObject.put(propKey,(Boolean)propValueObj);
                }
                else if(propValueObj instanceof Long){
                    dataObject.put(propKey,(Long)propValueObj);
                }
                else if(propValueObj instanceof Double){
                    dataObject.put(propKey,(Double)propValueObj);
                }
                else if(propValueObj instanceof Integer){
                    dataObject.put(propKey,(Integer)propValueObj);
                }else{
                    dataObject.put(propKey,propValueObj.toString());
                }
            }
        }
        obj.put("data",dataObject);
        return obj;
    }

    @Override
    public JsonSerializable readJson(JsonObject jsonObject) {
        return null;
    }
}
