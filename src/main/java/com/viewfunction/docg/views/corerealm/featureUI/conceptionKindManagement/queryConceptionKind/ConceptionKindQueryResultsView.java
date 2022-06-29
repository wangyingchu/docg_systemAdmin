package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.eventHandling.ConceptionKindQueriedEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;

public class ConceptionKindQueryResultsView extends VerticalLayout implements
        ConceptionKindQueriedEvent.ConceptionKindQueriedListener {
    private String conceptionKindName;
    private Registration listener;
    private Grid<ConceptionEntityValue> queryResultGrid;
    private SecondaryKeyValueDisplayItem startTimeDisplayItem;
    private SecondaryKeyValueDisplayItem finishTimeDisplayItem;
    private SecondaryKeyValueDisplayItem dataCountDisplayItem;
    public ConceptionKindQueryResultsView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
        this.setPadding(true);
        this.setSpacing(true);

        HorizontalLayout titleLayout = new HorizontalLayout();
        add(titleLayout);
        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.HARDDRIVE_O),"查询结果");
        titleLayout.add(filterTitle2);
        startTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询开始时间","1,000,000,000");
        finishTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询结束时间","1,000,000,000");
        dataCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(),"结果集数据量","1,000,000,000");

        queryResultGrid = new Grid<>();
        queryResultGrid.setWidth(100,Unit.PERCENTAGE);
        add(queryResultGrid);
    }

    @Override
    public void receivedConceptionKindQueriedEvent(ConceptionKindQueriedEvent event) {
        String conceptionKindName = event.getConceptionKindName();
        if(conceptionKindName.equals(this.conceptionKindName)){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            ConceptionKind targetConception = coreRealm.getConceptionKind(conceptionKindName);
            QueryParameters queryParameters = new QueryParameters();
            try {
                List<String> attributesList = new ArrayList<>();
                attributesList.add(RealmConstant._createDateProperty);
                attributesList.add(RealmConstant._lastModifyDateProperty);
                attributesList.add(RealmConstant._creatorIdProperty);
                attributesList.add(RealmConstant._dataOriginProperty);

                ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult =
                        targetConception.getSingleValueEntityAttributesByAttributeNames(attributesList,queryParameters);
                System.out.println(conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount());

                startTimeDisplayItem.updateDisplayValue(conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime().toString());
                finishTimeDisplayItem.updateDisplayValue(conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getFinishTime().toString());
                dataCountDisplayItem.updateDisplayValue(""+conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount());

            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            queryResultGrid.setHeight(event.getHeight()-140,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryResultGrid.setHeight(browserHeight-140,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }
}
