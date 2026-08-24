package com.viewfunction.docg.element.visualizationComponent.payload.common;

public class NVLEdgePayload {

    private String id;
    private String from;
    private String to;
    private String caption;
    private boolean initialRel;

    public NVLEdgePayload(String id,String caption,String from,String to,boolean initialRel) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.caption = caption;
        this.initialRel = initialRel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isInitialRel() {
        return initialRel;
    }

    public void setInitialRel(boolean initialRel) {
        this.initialRel = initialRel;
    }
}
