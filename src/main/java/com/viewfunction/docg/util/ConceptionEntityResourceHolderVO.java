package com.viewfunction.docg.util;

public class ConceptionEntityResourceHolderVO {

    private String conceptionKind;
    private String conceptionEntityUID;
    private String comment;

    public ConceptionEntityResourceHolderVO(String conceptionKind,String conceptionEntityUID){
        this.setConceptionKind(conceptionKind);
        this.setConceptionEntityUID(conceptionEntityUID);
    }

    public ConceptionEntityResourceHolderVO(){}

    public String getConceptionKind() {
        return conceptionKind;
    }

    public void setConceptionKind(String conceptionKind) {
        this.conceptionKind = conceptionKind;
    }

    public String getConceptionEntityUID() {
        return conceptionEntityUID;
    }

    public void setConceptionEntityUID(String conceptionEntityUID) {
        this.conceptionEntityUID = conceptionEntityUID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
