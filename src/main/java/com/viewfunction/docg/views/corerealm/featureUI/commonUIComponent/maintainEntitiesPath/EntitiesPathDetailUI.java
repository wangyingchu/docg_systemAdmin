package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.maintainEntitiesPath;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis.ConceptionEntityExpandTopologyChart;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis.EntitiesPathInfoView;

import java.util.*;

public class EntitiesPathDetailUI extends VerticalLayout {

    private Dialog containerDialog;
    private EntitiesPath entitiesPath;
    private VerticalLayout entitiesFieldsContainer;
    private VerticalLayout entitiesPathInfoContainer;
    private Registration listener;

    public EntitiesPathDetailUI(EntitiesPath entitiesPath){
        this.entitiesPath = entitiesPath;


        LinkedList<ConceptionEntity> list1 =  this.entitiesPath.getPathConceptionEntities();
        for(ConceptionEntity cConceptionEntity:list1){
            System.out.println(cConceptionEntity.getAllConceptionKindNames());
        }

        Map<String,RelationEntity> relationEntityMap = new HashMap<>();

        LinkedList<RelationEntity> list2 = this.entitiesPath.getPathRelationEntities();
        for(RelationEntity cRelationEntity:list2){
            System.out.println(cRelationEntity.getFromConceptionEntityKinds());
            System.out.println(cRelationEntity.getToConceptionEntityKinds());
        }






    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            renderView(event.getHeight()-75);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            renderView(browserHeight-75);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void renderView(int viewHeight){
        this.entitiesFieldsContainer = new VerticalLayout();
        this.entitiesFieldsContainer.setPadding(false);
        this.entitiesFieldsContainer.setSpacing(false);
        this.entitiesFieldsContainer.setMargin(false);
        this.entitiesFieldsContainer.setMinWidth(450, Unit.PIXELS);
        this.entitiesFieldsContainer.setMaxWidth(450, Unit.PIXELS);

        EntitiesPathInfoView entitiesPathInfoView = new EntitiesPathInfoView(this.entitiesPath,viewHeight);
        this.entitiesFieldsContainer.add(entitiesPathInfoView);
        this.entitiesPathInfoContainer = new VerticalLayout();

        List<EntitiesPath> entitiesPathList =new ArrayList<>();
        entitiesPathList.add(this.entitiesPath);

        //ConceptionEntityExpandPathInfoView conceptionEntityExpandPathInfoView =
        //        new ConceptionEntityExpandPathInfoView(this.entitiesPath.getStartConceptionEntityType(),this.entitiesPath.getStartConceptionEntityUID(),entitiesPathList);




        ConceptionEntityExpandTopologyChart conceptionEntityExpandTopologyChart =
                new ConceptionEntityExpandTopologyChart(this.entitiesPath.getStartConceptionEntityType(),this.entitiesPath.getEndConceptionEntityUID());

        conceptionEntityExpandTopologyChart.setHeight(600,Unit.PIXELS);
        conceptionEntityExpandTopologyChart.setHeight(800,Unit.PIXELS);

        conceptionEntityExpandTopologyChart.setData(this.entitiesPath.getPathRelationEntities(),this.entitiesPath.getPathConceptionEntities());

        this.entitiesPathInfoContainer.add(conceptionEntityExpandTopologyChart);

        SplitLayout splitLayout = new SplitLayout(this.entitiesFieldsContainer, this.entitiesPathInfoContainer);
        splitLayout.setSplitterPosition(0);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);
        splitLayout.getSecondaryComponent().getElement().getStyle().set("padding-top","0px").set("padding-right","0px");
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
