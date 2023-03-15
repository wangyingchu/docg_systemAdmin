package com.viewfunction.docg.util;

public class RelationEntityResourceHolderVO {

    private String relationKind;
    private String relationEntityUID;
    private String comment;

    public RelationEntityResourceHolderVO(String relationKind,String relationEntityUID){
        this.setRelationKind(relationKind);
        this.setRelationEntityUID(relationEntityUID);
    }

    public String getRelationKind() {
        return relationKind;
    }

    public void setRelationKind(String relationKind) {
        this.relationKind = relationKind;
    }

    public String getRelationEntityUID() {
        return relationEntityUID;
    }

    public void setRelationEntityUID(String relationEntityUID) {
        this.relationEntityUID = relationEntityUID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
