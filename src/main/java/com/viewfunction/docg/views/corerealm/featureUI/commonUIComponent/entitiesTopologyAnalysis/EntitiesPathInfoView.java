package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;

public class EntitiesPathInfoView extends VerticalLayout {

    public EntitiesPathInfoView(EntitiesPath entitiesPath) {
        this.setWidth(400, Unit.PIXELS);
        this.setHeight(500, Unit.PIXELS);
        this.add(new NativeLabel(entitiesPath.getEndConceptionEntityType()+" "+entitiesPath.getPathJumps()));
    }
}
