package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.AddConceptionEntityToProcessingListView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity.RelationEntityDetailUI;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RelatedConceptionEntitiesView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem conceptionEntitiesCountDisplayItem;
    private Grid<ConceptionEntityValue> queryResultGrid;
    private final String _rowIndexPropertyName = "ROW_INDEX";
    private List<String> currentRowKeyList;
    private List<String> lastQueryAttributesList;
    private String currentWorkingRelationKindName;
    private RelationDirection currentWorkingRelationDirection;
    private SecondaryKeyValueDisplayItem startTimeDisplayItem;
    private SecondaryKeyValueDisplayItem finishTimeDisplayItem;
    private SecondaryKeyValueDisplayItem dataCountDisplayItem;
    private final ZoneId id = ZoneId.systemDefault();

    public RelatedConceptionEntitiesView(String classificationName){
        this.setPadding(false);
        this.classificationName = classificationName;
        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.SPARK_LINE),"相关概念实体运行时信息");
        filterTitle1.getStyle().set("padding-top","10px");
        add(filterTitle1);

        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer.setWidthFull();
        infoContainer.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        add(infoContainer);

        this.numberFormat = NumberFormat.getInstance();
        this.conceptionEntitiesCountDisplayItem =
                new PrimaryKeyValueDisplayItem(infoContainer, FontAwesome.Solid.CIRCLE.create(),"相关概念实体数量:","-");

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.CONTROLLER),"关联概念实体自定义条件查询");
        add(filterTitle);
        ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper classificationRelatedDataQueryHelper =
                new ClassificationRelatedDataQueryCriteriaView.ClassificationRelatedDataQueryHelper() {
                    @Override
                    public void executeQuery(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel) {
                        queryRelatedConceptionEntities(relationKindName,relationDirection,includeOffspringClassifications,offspringLevel);
                    }
                };

        ClassificationRelatedDataQueryCriteriaView classificationRelatedDataQueryCriteriaView = new ClassificationRelatedDataQueryCriteriaView();
        classificationRelatedDataQueryCriteriaView.setClassificationRelatedDataQueryHelper(classificationRelatedDataQueryHelper);
        add(classificationRelatedDataQueryCriteriaView);

        //Button testButton = new Button("我是一个测试Button");
        //classificationRelatedDataQueryCriteriaView.getCustomQueryCriteriaElementsContainer().add(testButton);

        HorizontalLayout sectionDiv01 = new HorizontalLayout();
        sectionDiv01.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        sectionDiv01.setWidthFull();
        sectionDiv01.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        add(sectionDiv01);

        HorizontalLayout toolbarLayout = new HorizontalLayout();
        add(toolbarLayout);
        HorizontalLayout titleLayout = new HorizontalLayout();
        toolbarLayout.add(titleLayout);
        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.HARDDRIVE_O),"查询结果");
        titleLayout.add(filterTitle2);
        startTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询开始时间","-");
        finishTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询结束时间","-");
        dataCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(),"结果集数据量","-");

        queryResultGrid = new Grid<>();
        queryResultGrid.setWidth(100, Unit.PERCENTAGE);
        queryResultGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        queryResultGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        queryResultGrid.addColumn(new ValueProvider<ConceptionEntityValue, Object>() {
            @Override
            public Object apply(ConceptionEntityValue conceptionEntityValue) {
                return conceptionEntityValue.getEntityAttributesValue().get(_rowIndexPropertyName);
            }
        }).setHeader("").setHeader("IDX").setKey("idx").setFlexGrow(0).setWidth("75px").setResizable(false);
        queryResultGrid.addComponentColumn(new ConceptionEntityActionButtonsValueProvider()).setHeader("操作").setKey("idx_0").setFlexGrow(0).setWidth("150px").setResizable(false);
        queryResultGrid.addColumn(ConceptionEntityValue::getAllConceptionKindNames).setHeader(" ConceptionKinds").setKey("idx_1").setFlexGrow(1).setWidth("150px").setResizable(true)
                .setTooltipGenerator(new ValueProvider<ConceptionEntityValue, String>() {
            @Override
            public String apply(ConceptionEntityValue conceptionEntityValue) {
                return conceptionEntityValue.getAllConceptionKindNames().toString();
            }
        });
        queryResultGrid.addColumn(ConceptionEntityValue::getConceptionEntityUID).setHeader(" EntityUID").setKey("idx_2").setFlexGrow(1).setWidth("70px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_idx = new LightGridColumnHeader(VaadinIcon.LIST_OL,"");
        queryResultGrid.getColumnByKey("idx").setHeader(gridColumnHeader_idx).setSortable(false);
        LightGridColumnHeader gridColumnHeader_idx1 = new LightGridColumnHeader(VaadinIcon.WRENCH,"操作");
        queryResultGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx1).setSortable(false);
        LightGridColumnHeader gridColumnHeader_idx0 = new LightGridColumnHeader(VaadinIcon.CUBES,"实体概念类型");
        queryResultGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx0).setSortable(false).setResizable(true);
        LightGridColumnHeader gridColumnHeader_idx2 = new LightGridColumnHeader(VaadinIcon.KEY_O,"概念实体UID");
        queryResultGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(false).setResizable(true);
        add(queryResultGrid);

        queryResultGrid.addItemDoubleClickListener(new ComponentEventListener<ItemDoubleClickEvent<ConceptionEntityValue>>() {
            @Override
            public void onComponentEvent(ItemDoubleClickEvent<ConceptionEntityValue> conceptionEntityValueItemDoubleClickEvent) {
                ConceptionEntityValue targetConceptionEntityValue = conceptionEntityValueItemDoubleClickEvent.getItem();
                if(targetConceptionEntityValue!= null){
                    renderConceptionEntityUI(targetConceptionEntityValue);
                }
            }
        });

        this.currentRowKeyList = new ArrayList<>();
    }

    public void setHeight(int viewHeight){
        this.queryResultGrid.setHeight(viewHeight-145, Unit.PIXELS);
    }

    public void setTotalCount(int totalCount){
        this.conceptionEntitiesCountDisplayItem.updateDisplayValue(this.numberFormat.format(totalCount));
    }

    private void queryRelatedConceptionEntities(String relationKindName, RelationDirection relationDirection, boolean includeOffspringClassifications, int offspringLevel){
        currentWorkingRelationKindName = relationKindName;
        currentWorkingRelationDirection = relationDirection;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);

        for(String currentExistingRowKey:this.currentRowKeyList){
            queryResultGrid.removeColumnByKey(currentExistingRowKey);
        }
        queryResultGrid.setItems(new ArrayList<>());
        this.currentRowKeyList.clear();
        this.lastQueryAttributesList = null;
        try {
            QueryParameters queryParameters = new QueryParameters();
            List<String> attributesList = new ArrayList<>();
            /*
            if(resultAttributesList != null && resultAttributesList.size() > 0){
                attributesList.addAll(resultAttributesList);
            }else{
                attributesList.add(RealmConstant._createDateProperty);
                attributesList.add(RealmConstant._lastModifyDateProperty);
                attributesList.add(RealmConstant._creatorIdProperty);
                attributesList.add(RealmConstant._dataOriginProperty);
            }
            */

            attributesList.add(RealmConstant._createDateProperty);
            attributesList.add(RealmConstant._lastModifyDateProperty);
            attributesList.add(RealmConstant._creatorIdProperty);
            attributesList.add(RealmConstant._dataOriginProperty);

            this.lastQueryAttributesList = attributesList;

            ConceptionEntitiesAttributesRetrieveResult conceptionEntitiesAttributesRetrieveResult =
                    targetClassification.getRelatedConceptionEntityAttributes(currentWorkingRelationKindName, currentWorkingRelationDirection,queryParameters,
                            attributesList,includeOffspringClassifications,offspringLevel);
            List<ConceptionEntityValue> conceptionEntityValueList = conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues();

            Date startDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getStartTime();
            ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(startDateTime.toInstant(), id);
            String startTimeStr = startZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
            startTimeDisplayItem.updateDisplayValue(startTimeStr);
            Date finishDateTime = conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getFinishTime();
            ZonedDateTime finishZonedDateTime = ZonedDateTime.ofInstant(finishDateTime.toInstant(), id);
            String finishTimeStr = finishZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM)));
            startTimeDisplayItem.updateDisplayValue(startTimeStr);
            finishTimeDisplayItem.updateDisplayValue(finishTimeStr);
            dataCountDisplayItem.updateDisplayValue(""+   numberFormat.format(conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount()));

            for(int i=0 ; i<conceptionEntityValueList.size();i++){
                ConceptionEntityValue currentConceptionEntityValue = conceptionEntityValueList.get(i);
                currentConceptionEntityValue.getEntityAttributesValue().put(_rowIndexPropertyName,i+1);
            }
            if (attributesList != null && attributesList.size() > 0) {
                for (String currentProperty : attributesList) {
                    if (!currentProperty.equals(_rowIndexPropertyName)) {
                        queryResultGrid.addColumn(new ValueProvider<ConceptionEntityValue, Object>() {
                            @Override
                            public Object apply(ConceptionEntityValue conceptionEntityValue) {
                                return conceptionEntityValue.getEntityAttributesValue().get(currentProperty);
                            }
                        }).setHeader(" " + currentProperty).setKey(currentProperty + "_KEY").
                                setTooltipGenerator(new ValueProvider<ConceptionEntityValue, String>() {
                                    @Override
                                    public String apply(ConceptionEntityValue conceptionEntityValue) {
                                        return conceptionEntityValue.getEntityAttributesValue().get(currentProperty) != null ?
                                                conceptionEntityValue.getEntityAttributesValue().get(currentProperty).toString():
                                                "";
                                    }
                                });
                        queryResultGrid.getColumnByKey(currentProperty + "_KEY").setSortable(true).setResizable(true);
                    }

                    this.currentRowKeyList.add(currentProperty + "_KEY");
                }
            }
            queryResultGrid.setItems(conceptionEntityValueList);

            CommonUIOperationUtil.showPopupNotification("查询关联概念实体成功,查询返回 "+
                    conceptionEntitiesAttributesRetrieveResult.getOperationStatistics().getResultEntitiesCount()+" 项关联概念实体", NotificationVariant.LUMO_SUCCESS);
        } catch (CoreRealmServiceRuntimeException | CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderConceptionEntityUI(ConceptionEntityValue conceptionEntityValue){
        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(conceptionEntityValue.getAllConceptionKindNames().get(0),conceptionEntityValue.getConceptionEntityUID());

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(conceptionEntityValue.getAllConceptionKindNames().get(0));
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(conceptionEntityValue.getConceptionEntityUID());
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void addConceptionEntityToProcessingList(ConceptionEntityValue conceptionEntityValue){
        AddConceptionEntityToProcessingListView addConceptionEntityToProcessingListView = new AddConceptionEntityToProcessingListView(conceptionEntityValue.getAllConceptionKindNames().get(0),conceptionEntityValue);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INBOX),"待处理数据列表添加概念实例",null,true,600,300,false);
        fixSizeWindow.setWindowContent(addConceptionEntityToProcessingListView);
        fixSizeWindow.setModel(true);
        addConceptionEntityToProcessingListView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderRelationEntityDetailUI(ConceptionEntityValue conceptionEntityValue){
        String conceptionEntityUID = conceptionEntityValue.getConceptionEntityUID();
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            coreRealm.openGlobalSession();
            Classification targetClassification = coreRealm.getClassification(this.classificationName);
            String classificationUID = targetClassification.getClassificationUID();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            List<String> conceptionPairUIDList = new ArrayList<>();
            conceptionPairUIDList.add(conceptionEntityUID);
            conceptionPairUIDList.add(classificationUID);
            List<RelationEntity> relationEntityList = crossKindDataOperator.getRelationsOfConceptionEntityPair(conceptionPairUIDList);
            RelationEntity targetRelationEntity = null;
            if(relationEntityList != null && relationEntityList.size() >0){
                if(relationEntityList.size() == 1){
                    targetRelationEntity = relationEntityList.get(0);
                }else{
                    for(RelationEntity currentRelationEntity:relationEntityList){
                        if(currentRelationEntity.getRelationKindName().equals(currentWorkingRelationKindName)){
                            switch (currentWorkingRelationDirection){
                                case FROM :
                                    if(currentRelationEntity.getFromConceptionEntityUID().equals(classificationUID)){
                                        targetRelationEntity = currentRelationEntity;
                                    }
                                    break;
                                case TO:
                                    if(currentRelationEntity.getToConceptionEntityUID().equals(classificationUID)){
                                        targetRelationEntity = currentRelationEntity;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
            if(targetRelationEntity != null){
                RelationEntityDetailUI relationEntityDetailUI = new RelationEntityDetailUI(targetRelationEntity.getRelationKindName(),targetRelationEntity.getRelationEntityUID());
                List<Component> actionComponentList = new ArrayList<>();

                HorizontalLayout titleDetailLayout = new HorizontalLayout();
                titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                titleDetailLayout.setSpacing(false);

                Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
                footPrintStartIcon.setSize("14px");
                footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
                titleDetailLayout.add(footPrintStartIcon);
                HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
                spaceDivLayout1.setWidth(8,Unit.PIXELS);
                titleDetailLayout.add(spaceDivLayout1);

                Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
                relationKindIcon.setSize("10px");
                titleDetailLayout.add(relationKindIcon);
                HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
                spaceDivLayout2.setWidth(5,Unit.PIXELS);
                titleDetailLayout.add(spaceDivLayout2);
                NativeLabel relationKindNameLabel = new NativeLabel(targetRelationEntity.getRelationKindName());
                titleDetailLayout.add(relationKindNameLabel);

                HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
                spaceDivLayout3.setWidth(5,Unit.PIXELS);
                titleDetailLayout.add(spaceDivLayout3);

                Icon divIcon = VaadinIcon.ITALIC.create();
                divIcon.setSize("8px");
                titleDetailLayout.add(divIcon);

                HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
                spaceDivLayout4.setWidth(5,Unit.PIXELS);
                titleDetailLayout.add(spaceDivLayout4);

                Icon relationEntityIcon = VaadinIcon.KEY_O.create();
                relationEntityIcon.setSize("10px");
                titleDetailLayout.add(relationEntityIcon);

                HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
                spaceDivLayout5.setWidth(5,Unit.PIXELS);
                titleDetailLayout.add(spaceDivLayout5);
                NativeLabel relationEntityUIDLabel = new NativeLabel(targetRelationEntity.getRelationEntityUID());
                titleDetailLayout.add(relationEntityUIDLabel);

                actionComponentList.add(titleDetailLayout);

                FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"关系实体详情",actionComponentList,null,true);
                fullScreenWindow.setWindowContent(relationEntityDetailUI);
                relationEntityDetailUI.setContainerDialog(fullScreenWindow);
                fullScreenWindow.show();
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }finally {
            coreRealm.closeGlobalSession();
        }
    }

    private void renderDetachClassificationLinkUI(ConceptionEntityValue conceptionEntityValue){
        String conceptionEntityUID = conceptionEntityValue.getConceptionEntityUID();
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            Classification targetClassification = coreRealm.getClassification(this.classificationName);
            String classificationUID = targetClassification.getClassificationUID();
            CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
            List<String> conceptionPairUIDList = new ArrayList<>();
            conceptionPairUIDList.add(conceptionEntityUID);
            conceptionPairUIDList.add(classificationUID);
            List<RelationEntity> relationEntityList = crossKindDataOperator.getRelationsOfConceptionEntityPair(conceptionPairUIDList);
            RelationEntity targetRelationEntity = null;
            if(relationEntityList != null && relationEntityList.size() >0){
                if(relationEntityList.size() == 1){
                    targetRelationEntity = relationEntityList.get(0);
                }else{
                    for(RelationEntity currentRelationEntity:relationEntityList){
                        if(currentRelationEntity.getRelationKindName().equals(currentWorkingRelationKindName)){
                            switch (currentWorkingRelationDirection){
                                case FROM :
                                    if(currentRelationEntity.getFromConceptionEntityUID().equals(classificationUID)){
                                        targetRelationEntity = currentRelationEntity;
                                    }
                                    break;
                                case TO:
                                    if(currentRelationEntity.getToConceptionEntityUID().equals(classificationUID)){
                                        targetRelationEntity = currentRelationEntity;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
            if(targetRelationEntity != null){
                List<Button> actionButtonList = new ArrayList<>();
                Button confirmButton = new Button("确认删除分类关联",new Icon(VaadinIcon.CHECK_CIRCLE));
                confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                Button cancelButton = new Button("取消操作");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
                actionButtonList.add(confirmButton);
                actionButtonList.add(cancelButton);

                ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作",
                        "请确认执行删除分类关联 "+ this.classificationName+" - ["+targetRelationEntity.getRelationKindName()+"] -"+
                                conceptionEntityValue.getAllConceptionKindNames().get(0)+"("+conceptionEntityValue.getConceptionEntityUID()+") 的操作",actionButtonList,400,180);
                confirmWindow.open();
                final RelationEntity finalDetachRelationEntity = targetRelationEntity;
                confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        doDetachClassificationLink(conceptionEntityValue,finalDetachRelationEntity,confirmWindow);
                    }
                });
                cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        confirmWindow.closeConfirmWindow();
                    }
                });
            }
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void doDetachClassificationLink(ConceptionEntityValue conceptionEntityValue,RelationEntity targetRelationEntity, ConfirmWindow confirmWindow){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        RelationKind targetRelationKind = coreRealm.getRelationKind(targetRelationEntity.getRelationKindName());
        try {
            boolean detachResult = targetRelationKind.deleteEntity(targetRelationEntity.getRelationEntityUID());
            if(detachResult){
                CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ this.classificationName+" - ["+targetRelationEntity.getRelationKindName()+"] -"+
                        conceptionEntityValue.getAllConceptionKindNames().get(0)+"("+conceptionEntityValue.getConceptionEntityUID() +") 成功", NotificationVariant.LUMO_SUCCESS);
                confirmWindow.closeConfirmWindow();
                ListDataProvider dataProvider=(ListDataProvider)queryResultGrid.getDataProvider();
                dataProvider.getItems().remove(conceptionEntityValue);
                dataProvider.refreshAll();
            }else{
                CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ this.classificationName+" - ["+targetRelationEntity.getRelationKindName()+"] -"+
                        conceptionEntityValue.getAllConceptionKindNames().get(0)+"("+conceptionEntityValue.getConceptionEntityUID() +") 失败", NotificationVariant.LUMO_ERROR);
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private class ConceptionEntityActionButtonsValueProvider implements ValueProvider<ConceptionEntityValue,HorizontalLayout>{
        @Override
        public HorizontalLayout apply(ConceptionEntityValue conceptionEntityValue) {
            HorizontalLayout actionButtonContainerLayout = new HorizontalLayout();
            actionButtonContainerLayout.setMargin(false);
            actionButtonContainerLayout.setSpacing(false);
            Button showDetailButton = new Button();
            showDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            showDetailButton.setIcon(VaadinIcon.EYE.create());
            Tooltips.getCurrent().setTooltip(showDetailButton, "显示概念实体详情");
            actionButtonContainerLayout.add(showDetailButton);
            showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionEntityValue != null){
                        renderConceptionEntityUI(conceptionEntityValue);
                    }
                }
            });

            Button addToProcessListButton = new Button();
            addToProcessListButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addToProcessListButton.setIcon(VaadinIcon.INBOX.create());
            Tooltips.getCurrent().setTooltip(addToProcessListButton, "加入待处理数据列表");
            actionButtonContainerLayout.add(addToProcessListButton);
            addToProcessListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionEntityValue != null){
                        addConceptionEntityToProcessingList(conceptionEntityValue);
                    }
                }
            });

            Icon linkIcon = LineAwesomeIconsSvg.LINK_SOLID.create();
            linkIcon.setSize("17px");
            Button editLinkProperties = new Button(linkIcon, event -> {});
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editLinkProperties.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editLinkProperties.setTooltipText("关联链接信息");
            actionButtonContainerLayout.add(editLinkProperties);
            editLinkProperties.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    renderRelationEntityDetailUI(conceptionEntityValue);
                }
            });

            Icon deleteLinkIcon = new Icon(VaadinIcon.UNLINK);
            deleteLinkIcon.setSize("21px");
            Button removeClassificationLink = new Button(deleteLinkIcon, event -> {});
            removeClassificationLink.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeClassificationLink.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeClassificationLink.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeClassificationLink.setTooltipText("删除分类关联");
            actionButtonContainerLayout.add(removeClassificationLink);
            removeClassificationLink.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    renderDetachClassificationLinkUI(conceptionEntityValue);
                }
            });
            return actionButtonContainerLayout;
        }
    }
}
