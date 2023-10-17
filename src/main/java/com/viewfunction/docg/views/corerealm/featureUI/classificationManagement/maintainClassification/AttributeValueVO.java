package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

public class AttributeValueVO {
    private String attributeName;
    private Object attributeValue;

    public AttributeValueVO(String attributeName,Object attributeValue){
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Object getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(Object attributeValue) {
        this.attributeValue = attributeValue;
    }
}
