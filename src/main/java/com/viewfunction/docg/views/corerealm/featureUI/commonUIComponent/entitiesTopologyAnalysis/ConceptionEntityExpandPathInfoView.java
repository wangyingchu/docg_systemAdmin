package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class ConceptionEntityExpandPathInfoView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private List<EntitiesPath> entitiesPathList;

    public ConceptionEntityExpandPathInfoView(String conceptionKind,String conceptionEntityUID,List<EntitiesPath> entitiesPathList) {
        setSizeFull();
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.entitiesPathList = entitiesPathList;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //ResourceHolder.getApplicationBlackboard().addListener(this);
        /*
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            queryResultGrid.setHeight(event.getHeight()-140, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryResultGrid.setHeight(browserHeight-140,Unit.PIXELS);
        }));
        */



        add(new NativeLabel("Conception Entity Expand Path Info"));
        System.out.println(this.conceptionKind);
        System.out.println(this.conceptionEntityUID);
        System.out.println(this.entitiesPathList);
        System.out.println(this.entitiesPathList.size());



        if(!this.entitiesPathList.isEmpty()){
            for(EntitiesPath currentPath : this.entitiesPathList){

                LinkedList<ConceptionEntity> pathConceptionEntitiesList =  currentPath.getPathConceptionEntities();
                LinkedList<RelationEntity> pathRelationEntitiesList = currentPath.getPathRelationEntities();
                pathConceptionEntitiesList.forEach(new Consumer<ConceptionEntity>() {
                    @Override
                    public void accept(ConceptionEntity conceptionEntity) {

                    }
                });

                pathRelationEntitiesList.forEach(new Consumer<RelationEntity>() {
                    @Override
                    public void accept(RelationEntity relationEntity) {

                    }
                });

            }



        }


    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        //listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }
}
