package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItem;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MetaConfigItemsConfigView extends VerticalLayout {

    public enum MetaConfigItemType {AttributeKind,AttributesViewKind,ConceptionKind,RelationAttachKind,RelationKind}

    private String metaConfigItemUID;

    public MetaConfigItemsConfigView(String metaConfigItemUID){
        this.setMetaConfigItemUID(metaConfigItemUID);
    }

    public MetaConfigItemsConfigView(MetaConfigItemType metaConfigItemType,String metaConfigItemName){

    }

    public String getMetaConfigItemUID() {
        return metaConfigItemUID;
    }

    public void setMetaConfigItemUID(String metaConfigItemUID) {
        this.metaConfigItemUID = metaConfigItemUID;
    }

    public void renderMetaConfigItemsConfigUI(){

    }
}
