package com.viewfunction.docg.element.visualizationComponent.payload.common;

import java.util.HashMap;

public class EdgePayload {
    String group = "edges";
    HashMap<String, String> data = new HashMap();

    public EdgePayload() {
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
}
