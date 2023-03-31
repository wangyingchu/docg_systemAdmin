package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindIndex;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.SystemMaintenanceOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.SearchIndexInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import java.util.ArrayList;
import java.util.List;
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
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);

        this.setWidth(100, Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button createKindIndexButton= new Button("创建类型索引");
        createKindIndexButton.setIcon(VaadinIcon.PLUS.create());
        createKindIndexButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        buttonList.add(createKindIndexButton);

        SecondaryTitleActionBar indexConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONTROLLER),"类型索引配置管理 ",secTitleElementsList,buttonList);
        add(indexConfigActionBar);











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
