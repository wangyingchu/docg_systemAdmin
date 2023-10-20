package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesAttributesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RelatedConceptionEntitiesView extends VerticalLayout {
    private String classificationName;
    private NumberFormat numberFormat;
    private PrimaryKeyValueDisplayItem conceptionEntitiesCountDisplayItem;
    private Grid<ConceptionEntityValue> queryResultGrid;
    private final String _rowIndexPropertyName = "ROW_INDEX";
    private List<String> currentRowKeyList;
    private  List<String> lastQueryAttributesList;

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
                        //currentAdvanceQueryRelationKindName = relationKindName;
                        //currentAdvanceQueryRelationDirection = relationDirection;
                        //currentAdvanceQueryIncludeOffspringClassifications = includeOffspringClassifications;
                        //currentAdvanceQueryOffspringLevel = offspringLevel;
                        //clearClassificationLinkRelationInfo();
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
        SecondaryKeyValueDisplayItem startTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询开始时间","-");
        SecondaryKeyValueDisplayItem finishTimeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, FontAwesome.Regular.CLOCK.create(),"查询结束时间","-");
        SecondaryKeyValueDisplayItem dataCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(),"结果集数据量","-");

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
                    //renderConceptionEntityUI(targetConceptionEntityValue);
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
                    targetClassification.getRelatedConceptionEntityAttributes(relationKindName,relationDirection,queryParameters,
                            attributesList,includeOffspringClassifications,offspringLevel);
            List<ConceptionEntityValue> conceptionEntityValueList = conceptionEntitiesAttributesRetrieveResult.getConceptionEntityValues();

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
                        //renderConceptionEntityUI(conceptionEntityValue);
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
                        //addConceptionEntityToProcessingList(conceptionEntityValue);
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
                    //renderRelationEntityDetailUI(((RelatedConceptionKindsView.ConceptionKindAttachInfoVO) conceptionKindAttachInfoVO).relationKindName,
                    //        ((RelatedConceptionKindsView.ConceptionKindAttachInfoVO) conceptionKindAttachInfoVO).getRelationEntityUID());
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
                    //renderDetachConceptionKindUI((RelatedConceptionKindsView.ConceptionKindAttachInfoVO)conceptionKindAttachInfoVO);
                }
            });
            /*
            Button deleteButton = new Button();
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR );
            deleteButton.setIcon(VaadinIcon.TRASH.create());
            Tooltips.getCurrent().setTooltip(deleteButton, "删除概念实体");
            actionButtonContainerLayout.add(deleteButton);
            deleteButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionEntityValue != null){
                        //deleteConceptionEntity(conceptionEntityValue);
                    }
                }
            });
            */
            return actionButtonContainerLayout;
        }
    }
}
