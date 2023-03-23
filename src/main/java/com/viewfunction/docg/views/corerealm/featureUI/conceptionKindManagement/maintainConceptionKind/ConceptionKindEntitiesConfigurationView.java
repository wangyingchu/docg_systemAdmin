package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;

import java.text.NumberFormat;

public class ConceptionKindEntitiesConfigurationView extends VerticalLayout {

    private String conceptionKindName;

    public ConceptionKindEntitiesConfigurationView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(infoContainer);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        long conceptionEntitiesCount = 0;
        try {
            conceptionEntitiesCount = coreRealm.getConceptionKind(this.conceptionKindName).countConceptionEntities();
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"概念实体数量:",numberFormat.format(conceptionEntitiesCount));

    }
}
