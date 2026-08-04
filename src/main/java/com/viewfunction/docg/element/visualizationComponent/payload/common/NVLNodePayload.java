package com.viewfunction.docg.element.visualizationComponent.payload.common;

import java.util.List;

public class NVLNodePayload {

    private String id;
    private String caption;
    private List<String> labels;

    public NVLNodePayload(String id,String caption,List<String> labels) {
        this.id = id;
        this.caption = caption;
        this.labels = labels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
