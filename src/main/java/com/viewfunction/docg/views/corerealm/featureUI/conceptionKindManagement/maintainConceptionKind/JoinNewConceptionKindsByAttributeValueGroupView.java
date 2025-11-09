package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.Map;

public class JoinNewConceptionKindsByAttributeValueGroupView extends VerticalLayout {

    private Dialog containerDialog;
    private String conceptionKind;
    private String attributeName;

    public JoinNewConceptionKindsByAttributeValueGroupView(String conceptionKind, String attributeName){
        this.conceptionKind = conceptionKind;
        this.attributeName = attributeName;
        staticAttributeValueGroupInfo();
    }

    private void staticAttributeValueGroupInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        try {
            QueryParameters queryParameters = new QueryParameters();
            queryParameters.setResultNumber(100000000);

            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
            Map<Object, Number> resultMap = targetConceptionKind.statisticEntityGroupByAttributeValue(queryParameters,this.attributeName);
            System.out.println(resultMap);
            System.out.println(resultMap);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        coreRealm.closeGlobalSession();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
