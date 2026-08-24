package com.viewfunction.docg.element.visualizationComponent.payload.common;

import java.util.List;

public class NVLNodePayload {

    private String id;
    private String caption;
    private List<String> labels;
    private boolean initialNode;

    public NVLNodePayload(String id,String caption,List<String> labels,boolean initialNode) {
        this.id = id;
        this.caption = caption;
        this.labels = labels;
        this.initialNode = initialNode;
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

    public boolean isInitialNode() {
        return initialNode;
    }

    public void setInitialNode(boolean initialNode) {
        this.initialNode = initialNode;
    }
}
