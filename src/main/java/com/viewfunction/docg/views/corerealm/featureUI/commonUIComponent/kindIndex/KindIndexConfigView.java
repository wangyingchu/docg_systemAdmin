package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndex;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.SearchIndexInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.Set;

public class KindIndexConfigView extends VerticalLayout {

    public enum KindIndexType {ConceptionKind,RelationKind}

    private KindIndexType kindIndexType;
    private String kindName;

    public KindIndexConfigView(KindIndexType kindIndexType,String kindName){
        this.kindIndexType = kindIndexType;
        this.kindName = kindName;
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderKindIndexConfigUI();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void renderKindIndexConfigUI(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        SystemMaintenanceOperator systemMaintenanceOperator = coreRealm.getSystemMaintenanceOperator();
        Set<SearchIndexInfo> searchIndexInfoSet = systemMaintenanceOperator.listConceptionKindSearchIndex();
        for(SearchIndexInfo currentSearchIndexInfo:searchIndexInfoSet){
            System.out.println(currentSearchIndexInfo.getIndexName());
            System.out.println(currentSearchIndexInfo.getSearchIndexType());
            System.out.println(currentSearchIndexInfo.getSearchKindName());
            System.out.println(currentSearchIndexInfo.getIndexedAttributeNames());
            System.out.println(currentSearchIndexInfo.getPopulationPercent());
        }
    }
}
