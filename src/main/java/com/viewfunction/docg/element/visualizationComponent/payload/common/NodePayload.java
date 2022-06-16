package com.viewfunction.docg.element.visualizationComponent.payload.common;

import java.util.HashMap;

public class NodePayload {
    String group = "nodes";
    HashMap<String, String> data = new HashMap();
    HashMap<String, Integer> position = new HashMap();

    public NodePayload() {
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public HashMap<String, Integer> getPosition() {
        return this.position;
    }

    public void setPosition(HashMap<String, Integer> position) {
        this.position = position;
    }
}
