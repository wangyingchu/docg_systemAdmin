package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ConceptionKindQueryResultsView extends VerticalLayout implements
        ConceptionKindQueriedEvent.ConceptionKindQueriedListener {
    private String conceptionKindName;
    private Registration listener;
    private Grid<ConceptionEntityValue> queryResultGrid;
    private SecondaryKeyValueDisplayItem startTimeDisplayItem;
    private SecondaryKeyValueDisplayItem finishTimeDisplayItem;
    private SecondaryKeyValueDisplayItem dataCountDisplayItem;
    private final ZoneId id = ZoneId.systemDefault();
    public ConceptionKindQueryResultsView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
        this.setPadding(true);
        this.setSpacing(true);

        HorizontalLayout titleLayout = new HorizontalLayout();
        add(titleLayout);
        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.HARDDRIVE_O),"查询结果");
        titleLayout.add(filterTitle2);
        startTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询开始时间","-");
        finishTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询结束时间","-");
        dataCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(),"结果集数据量","-");

        queryResultGrid = new Grid<>();
        queryResultGrid.setWidth(100,Unit.PERCENTAGE);

        queryResultGrid.addColumn(ConceptionEntityValue::getConceptionEntityUID).setHeader(" EntityUID").setId("IDX");
                //.setWidth(90).setResizable(true);
        queryResultGrid.addComponentColumn(new ConceptionEntityActionButtonsValueProvider()).setHeader("操作").setId("ACTIONS");
            //.setWidth(150).setResizable(false);
           // queryResultGrid.addColumn(InfoObjectValueVO::getObjectInstanceRID).setCaption(" ObjectInstanceRID").setId("0").setWidth(140).setResizable(false);
        queryResultGrid.addColumn(new ValueProvider<ConceptionEntityValue, Object>() {
            @Override
            public Object apply(ConceptionEntityValue conceptionEntityValue) {
                return null;
            }
        }).setHeader(" ID").setId("1");
        //queryResultGrid.addColumn("EntityStatisticsInfo::getEntityKindName").setHeader("概念类型名称").setKey("idx_0");
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
                if(conceptionEntitiesAttributesRetrieveResult != null && conceptionEntitiesAttributesRetrieveResult.getOperationStatistics() != null){
                    Date startDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
                    ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(startDateTime.toInstant(), id);
                    String startTimeStr = startZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    startTimeDisplayItem.updateDisplayValue(startTimeStr);
                    Date finishDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
                    ZonedDateTime finishZonedDateTime = ZonedDateTime.ofInstant(finishDateTime.toInstant(), id);
                    String finishTimeStr = finishZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
                    finishTimeDisplayItem.updateDisplayValue(finishTimeStr);
                    dataCountDisplayItem.updateDisplayValue(""+conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount());

                    List<ConceptionEntityValue> conceptionEntityValueList = conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues();
                    List<String> entityAttributeNamesList = new ArrayList<>();
                    entityAttributeNamesList.add("Prop001");
                    entityAttributeNamesList.add("Prop002");
                    entityAttributeNamesList.add("Prop003");

                    for(ConceptionEntityValue currentConceptionEntityValue:conceptionEntityValueList){
                        currentConceptionEntityValue.getEntityAttributesValue().put("Prop001",1);
                        currentConceptionEntityValue.getEntityAttributesValue().put("Prop002","sssssssss");
                                currentConceptionEntityValue.getEntityAttributesValue().put("Prop003",new Date());
                    }

                    if (entityAttributeNamesList != null && entityAttributeNamesList.size() > 0) {
                        for (String currentProperty : entityAttributeNamesList) {
                            //if (!currentProperty.equals(BusinessLogicConstant.ID_PROPERTY_TYPE_NAME)) {
                            queryResultGrid.addColumn(new ValueProvider<ConceptionEntityValue, Object>() {
                                    @Override
                                    public Object apply(ConceptionEntityValue conceptionEntityValue) {
                                        return conceptionEntityValue.getEntityAttributesValue().get(currentProperty);
                                    }
                                }).setHeader(" " + currentProperty).setId(currentProperty + "_ID");
                               // this.currentDisplayPropertiesList.add(currentProperty + "_ID");
                            //}
                        }
                    }
                    queryResultGrid.setItems(conceptionEntityValueList);
                }
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

    private class ConceptionEntityActionButtonsValueProvider implements ValueProvider<ConceptionEntityValue,HorizontalLayout>{

        @Override
        public HorizontalLayout apply(ConceptionEntityValue o) {
            HorizontalLayout actionButtonContainerLayout = new HorizontalLayout();
            actionButtonContainerLayout.setMargin(false);
            actionButtonContainerLayout.setSpacing(false);
            Button linkButton = new Button();
            linkButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            linkButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            linkButton.setIcon(VaadinIcon.EYE.create());
            //linkButton.setDescription("显示对象实例数据详情");
            actionButtonContainerLayout.add(linkButton);
            return actionButtonContainerLayout;
        }
    }
}