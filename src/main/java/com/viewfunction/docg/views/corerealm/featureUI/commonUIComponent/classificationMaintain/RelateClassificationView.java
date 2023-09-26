package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.ClassificationAttachable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;

import dev.mett.vaadin.tooltip.Tooltips;

import java.util.*;

public class RelateClassificationView extends VerticalLayout {

    private ClassificationConfigView.ClassificationRelatedObjectType classificationRelatedObjectType;
    private String relatedObjectID;
    private Dialog containerDialog;
    private ClassificationConfigView containerClassificationConfigView;
    private ComboBox<KindMetaInfo> relationKindSelect;
    private RadioButtonGroup<String> relationDirectionRadioGroup;
    private VerticalLayout relationEntityAttributesContainer;
    private Map<String, AttributeEditorItemWidget> relationAttributeEditorsMap;
    private Button clearAttributeButton;
    private Grid<ClassificationMetaInfo> classificationsMetaInfoFilterGrid;
    private GridListDataView<ClassificationMetaInfo> classificationMetaInfosMetaInfoFilterView;
    private TextField classificationDescFilterField;
    private TextField classificationNameFilterField;

    public RelateClassificationView(ClassificationConfigView.ClassificationRelatedObjectType
                                            classificationRelatedObjectType,String relatedObjectID){
        this.classificationRelatedObjectType = classificationRelatedObjectType;
        this.relatedObjectID = relatedObjectID;
        this.setMargin(false);
        this.setWidthFull();
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Icon kindIcon = VaadinIcon.CUBE.create();
        String viewTitleText = "";
        switch (this.classificationRelatedObjectType){
            case ConceptionKind :
                kindIcon = VaadinIcon.CUBE.create();
                viewTitleText = relatedObjectID;
                break;
            case RelationKind :
                kindIcon = VaadinIcon.CONNECT_O.create();
                viewTitleText = relatedObjectID;
                break;
            case AttributeKind:
                kindIcon = VaadinIcon.INPUT.create();
                AttributeKind targetAttributeKind = coreRealm.getAttributeKind(relatedObjectID);
                if(targetAttributeKind != null){
                    viewTitleText = targetAttributeKind.getAttributeKindName()+"("+relatedObjectID+")";
                }else{
                    viewTitleText = relatedObjectID;
                }
                break;
            case AttributesViewKind:
                kindIcon = VaadinIcon.TASKS.create();
                AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(relatedObjectID);
                if(targetAttributesViewKind != null){
                    viewTitleText = targetAttributesViewKind.getAttributesViewKindName()+"("+relatedObjectID+")";
                }else{
                    viewTitleText = relatedObjectID;
                }
                break;
            case ConceptionEntity:
                kindIcon = VaadinIcon.CUBES.create();
                viewTitleText = relatedObjectID;
                break;
        }
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, viewTitleText));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        add(mainContainerLayout);

        VerticalLayout basicInfoContainerLayout = new VerticalLayout();
        basicInfoContainerLayout.setWidth(350,Unit.PIXELS);
        mainContainerLayout.add(basicInfoContainerLayout);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT_O),"选择关系类型");
        basicInfoContainerLayout.add(infoTitle1);

        relationKindSelect = new ComboBox();
        relationKindSelect.setPageSize(30);
        relationKindSelect.setPlaceholder("选择关系类型定义");
        relationKindSelect.setWidth(340,Unit.PIXELS);

        relationKindSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo kindMetaInfo) {
                String itemLabelValue = kindMetaInfo.getKindName()+ " ("+
                        kindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        basicInfoContainerLayout.add(relationKindSelect);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.EXCHANGE),"选择关系方向");
        basicInfoContainerLayout.add(infoTitle2);

        relationDirectionRadioGroup = new RadioButtonGroup<>();
        relationDirectionRadioGroup.setItems("FROM", "TO");
        relationDirectionRadioGroup.setValue("FROM");
        basicInfoContainerLayout.add(relationDirectionRadioGroup);

        HorizontalLayout addRelationAttributesUIContainerLayout = new HorizontalLayout();
        addRelationAttributesUIContainerLayout.setSpacing(false);
        addRelationAttributesUIContainerLayout.setMargin(false);
        addRelationAttributesUIContainerLayout.setPadding(false);
        basicInfoContainerLayout.add(addRelationAttributesUIContainerLayout);

        addRelationAttributesUIContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定关系属性");
        addRelationAttributesUIContainerLayout.add(infoTitle3);

        Button addAttributeButton = new Button();
        Tooltips.getCurrent().setTooltip(addAttributeButton, "添加关系实体属性");
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        addAttributeButton.setIcon(VaadinIcon.KEYBOARD_O.create());
        addAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddNewAttributeUI();
            }
        });
        addRelationAttributesUIContainerLayout.add(addAttributeButton);

        clearAttributeButton = new Button();
        clearAttributeButton.setEnabled(false);
        Tooltips.getCurrent().setTooltip(clearAttributeButton, "清除已设置关系实体属性");
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        clearAttributeButton.setIcon(VaadinIcon.RECYCLE.create());
        clearAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cleanRelationAttributes();
            }
        });
        addRelationAttributesUIContainerLayout.add(clearAttributeButton);

        relationEntityAttributesContainer = new VerticalLayout();
        relationEntityAttributesContainer.setMargin(false);
        relationEntityAttributesContainer.setSpacing(false);
        relationEntityAttributesContainer.setPadding(false);
        relationEntityAttributesContainer.setWidth(100,Unit.PERCENTAGE);

        Scroller relationEntityAttributesScroller = new Scroller(relationEntityAttributesContainer);
        relationEntityAttributesScroller.setHeight(300,Unit.PIXELS);
        relationEntityAttributesScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        basicInfoContainerLayout.add(relationEntityAttributesScroller);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        basicInfoContainerLayout.add(spaceDivLayout2);

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        buttonsContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        basicInfoContainerLayout.add(buttonsContainerLayout);

        Button executeCreateRelationButton = new Button("创建分类关联");
        executeCreateRelationButton.setIcon(new Icon(VaadinIcon.LINK));
        executeCreateRelationButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeCreateRelationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                attachClassifications();
            }
        });
        buttonsContainerLayout.add(executeCreateRelationButton);

        VerticalLayout targetConceptionEntitiesInfoContainerLayout = new VerticalLayout();
        targetConceptionEntitiesInfoContainerLayout.setSpacing(false);
        mainContainerLayout.add(targetConceptionEntitiesInfoContainerLayout);

        ThirdLevelIconTitle infoTitle4 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CUBES),"选择目标分类");
        targetConceptionEntitiesInfoContainerLayout.add(infoTitle4);

        VerticalLayout classificationMetaInfoGridContainerLayout = new VerticalLayout();
        classificationMetaInfoGridContainerLayout.setSpacing(true);
        classificationMetaInfoGridContainerLayout.setMargin(false);
        classificationMetaInfoGridContainerLayout.setPadding(false);

        HorizontalLayout classificationsSearchElementsContainerLayout = new HorizontalLayout();
        classificationsSearchElementsContainerLayout.setSpacing(false);
        classificationsSearchElementsContainerLayout.setMargin(false);
        classificationMetaInfoGridContainerLayout.add(classificationsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        classificationsSearchElementsContainerLayout.add(filterTitle);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        classificationNameFilterField = new TextField();
        classificationNameFilterField.setPlaceholder("分类名称");
        classificationNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationNameFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationNameFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        classificationsSearchElementsContainerLayout.add(plusIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        classificationDescFilterField = new TextField();
        classificationDescFilterField.setPlaceholder("分类描述");
        classificationDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationDescFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationDescFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationDescFilterField);

        Button searchClassificationsButton = new Button("查找分类",new Icon(VaadinIcon.SEARCH));
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(searchClassificationsButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchClassificationsButton);
        searchClassificationsButton.setWidth(90,Unit.PIXELS);
        searchClassificationsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterClassifications();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        classificationsSearchElementsContainerLayout.add(divIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterClassifications();
            }
        });
        targetConceptionEntitiesInfoContainerLayout.add(classificationsSearchElementsContainerLayout);

        HorizontalLayout spaceLayout01 = new HorizontalLayout();
        spaceLayout01.setHeight(10,Unit.PIXELS);
        targetConceptionEntitiesInfoContainerLayout.add(spaceLayout01);

        classificationsMetaInfoFilterGrid = new Grid<>();
        classificationsMetaInfoFilterGrid.setWidth(720,Unit.PIXELS);
        classificationsMetaInfoFilterGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        classificationsMetaInfoFilterGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        classificationsMetaInfoFilterGrid.addColumn(ClassificationMetaInfo::getClassificationName).setKey("idx_0").setHeader("分类名称");
        classificationsMetaInfoFilterGrid.addColumn(ClassificationMetaInfo::getClassificationDesc).setKey("idx_1").setHeader("分类描述");
        classificationsMetaInfoFilterGrid.addColumn(ClassificationMetaInfo::getChildClassificationCount).setKey("idx_2").setHeader("子分类数量")
                .setFlexGrow(0).setWidth("110px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0A = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"分类名称");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0A).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1A = new GridColumnHeader(VaadinIcon.DESKTOP,"分类描述");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1A).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2A = new GridColumnHeader(VaadinIcon.ROAD_BRANCHES,"子分类数量");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2A).setSortable(true);

        targetConceptionEntitiesInfoContainerLayout.add(classificationsMetaInfoFilterGrid);

        try {
            List<ClassificationMetaInfo> classificationsMetaInfoList = coreRealm.getClassificationsMetaInfo();
            this.classificationMetaInfosMetaInfoFilterView = classificationsMetaInfoFilterGrid.setItems(classificationsMetaInfoList);
            //logic to filter AttributeKinds already loaded from server
            this.classificationMetaInfosMetaInfoFilterView.addFilter(item->{
                String entityKindName = item.getClassificationName();
                String entityKindDesc = item.getClassificationDesc();
                boolean attributeKindNameFilterResult = true;
                if(!classificationNameFilterField.getValue().trim().equals("")){
                    if(entityKindName.contains(classificationNameFilterField.getValue().trim())){
                        attributeKindNameFilterResult = true;
                    }else{
                        attributeKindNameFilterResult = false;
                    }
                }
                boolean attributeKindDescFilterResult = true;
                if(!classificationDescFilterField.getValue().trim().equals("")){
                    if(entityKindDesc.contains(classificationDescFilterField.getValue().trim())){
                        attributeKindDescFilterResult = true;
                    }else{
                        attributeKindDescFilterResult = false;
                    }
                }
                return attributeKindNameFilterResult & attributeKindDescFilterResult;
            });
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }

        this.relationAttributeEditorsMap = new HashMap<>();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<KindMetaInfo> reltionKindsList = coreRealm.getRelationKindsMetaInfo();
            relationKindSelect.setItems(reltionKindsList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setContainerClassificationConfigView(ClassificationConfigView containerClassificationConfigView) {
        this.containerClassificationConfigView = containerClassificationConfigView;
    }

    private void renderAddNewAttributeUI(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(null,null, AddEntityAttributeView.KindType.RelationKind);
        AttributeValueOperateHandler attributeValueOperateHandlerForDelete = new AttributeValueOperateHandler(){
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                String attributeName = attributeValue.getAttributeName();
                if(relationAttributeEditorsMap.containsKey(attributeName)){
                    relationEntityAttributesContainer.remove(relationAttributeEditorsMap.get(attributeName));
                }
                relationAttributeEditorsMap.remove(attributeName);
                if(relationAttributeEditorsMap.size()==0) {
                    clearAttributeButton.setEnabled(false);
                }
            }
        };
        AttributeValueOperateHandler attributeValueOperateHandlerForAdd = new AttributeValueOperateHandler() {
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                String attributeName = attributeValue.getAttributeName();
                if(relationAttributeEditorsMap.containsKey(attributeName)){
                    CommonUIOperationUtil.showPopupNotification("已经设置了名称为 "+attributeName+" 的关系属性", NotificationVariant.LUMO_ERROR);
                }else{
                    AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(null,null,attributeValue, AttributeEditorItemWidget.KindType.RelationKind);
                    attributeEditorItemWidget.setWidth(350,Unit.PIXELS);
                    relationEntityAttributesContainer.add(attributeEditorItemWidget);
                    attributeEditorItemWidget.setAttributeValueOperateHandler(attributeValueOperateHandlerForDelete);
                    relationAttributeEditorsMap.put(attributeName,attributeEditorItemWidget);
                }
                if(relationAttributeEditorsMap.size()>0){
                    clearAttributeButton.setEnabled(true);
                }
            }
        };
        addEntityAttributeView.setAttributeValueOperateHandler(attributeValueOperateHandlerForAdd);

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"添加关系实体属性",null,true,480,210,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void cleanRelationAttributes(){
        relationEntityAttributesContainer.removeAll();
        relationAttributeEditorsMap.clear();
        clearAttributeButton.setEnabled(false);
        relationKindSelect.clear();
        relationDirectionRadioGroup.setValue("FROM");
    }

    private void filterClassifications(){
        String classificationFilterValue = classificationNameFilterField.getValue().trim();
        String classificationDescFilterValue = classificationDescFilterField.getValue().trim();
        if(classificationFilterValue.equals("")&classificationDescFilterValue.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入分类名称 和/或 分类描述", NotificationVariant.LUMO_ERROR);
        }else{
            this.classificationMetaInfosMetaInfoFilterView.refreshAll();
        }
    }

    private void cancelFilterClassifications(){
        classificationNameFilterField.setValue("");
        classificationDescFilterField.setValue("");
        this.classificationMetaInfosMetaInfoFilterView.refreshAll();
    }

    private void attachClassifications(){
        KindMetaInfo selectedRelationKind = relationKindSelect.getValue();
        if(selectedRelationKind == null){
            CommonUIOperationUtil.showPopupNotification("请选择关系类型定义", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }

        Set<ClassificationMetaInfo> classificationMetaInfosSet = classificationsMetaInfoFilterGrid.getSelectedItems();
        if(classificationMetaInfosSet == null || classificationMetaInfosSet.size() == 0){
            CommonUIOperationUtil.showPopupNotification("请选择至少一个分类定义", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }

        List<Button> actionButtonList = new ArrayList<>();

        Button confirmButton = new Button("确认关联分类",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行关联分类操作",actionButtonList,400,180);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doAttachClassifications();
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doAttachClassifications(){
        String relationKind = relationKindSelect.getValue().getKindName();
        String relationDirection = relationDirectionRadioGroup.getValue();

        Map<String, Object> relationAttributesMap = null;
        if(relationAttributeEditorsMap.size() > 0){
            relationAttributesMap = new HashMap<>();
            for(String currentAttributeName:relationAttributeEditorsMap.keySet()){
                AttributeValue currentAttributeValue = relationAttributeEditorsMap.get(currentAttributeName).getAttributeValue();
                relationAttributesMap.put(currentAttributeValue.getAttributeName(),currentAttributeValue.getAttributeValue());
            }
        }
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();

        ClassificationAttachable targetClassificationAttachable = null;
        switch (this.classificationRelatedObjectType){
            case ConceptionKind :
                targetClassificationAttachable = coreRealm.getConceptionKind(relatedObjectID);
                break;
            case RelationKind :
                targetClassificationAttachable = coreRealm.getRelationKind(relatedObjectID);
                break;
            case AttributeKind:
                targetClassificationAttachable = coreRealm.getAttributeKind(relatedObjectID);
                break;
            case AttributesViewKind:
                targetClassificationAttachable = coreRealm.getAttributesViewKind(relatedObjectID);
                break;
            case ConceptionEntity:

                break;
        }
        int totalAttachedClassificationCount = 0;
        int alreadyAttachedClassificationCount = 0;
        RelationAttachInfo relationAttachInfo = new RelationAttachInfo();
        relationAttachInfo.setRelationKind(relationKind);
        relationAttachInfo.setRelationData(relationAttributesMap);
        if(relationDirection.equals("FROM")){
            relationAttachInfo.setRelationDirection(RelationDirection.FROM);
        }else if(relationDirection.equals("TO")){
            relationAttachInfo.setRelationDirection(RelationDirection.TO);
        }

        Set<String> successAttachedClassifications = new HashSet<>();
        for(ClassificationMetaInfo currentClassificationMetaInfo:classificationsMetaInfoFilterGrid.getSelectedItems()){
            try {
                boolean isAlreadyAttached = targetClassificationAttachable.isClassificationAttached(currentClassificationMetaInfo.getClassificationName(),relationAttachInfo.getRelationKind(),relationAttachInfo.getRelationDirection());
                if(!isAlreadyAttached){
                    RelationEntity attachResult = targetClassificationAttachable.attachClassification(relationAttachInfo,currentClassificationMetaInfo.getClassificationName());
                    if(attachResult != null){
                        successAttachedClassifications.add(currentClassificationMetaInfo.getClassificationName());
                        totalAttachedClassificationCount++;
                    }
                }else{
                    alreadyAttachedClassificationCount++;
                }
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
        }

        if(totalAttachedClassificationCount >0){
            String successMessage = "";
            if(alreadyAttachedClassificationCount == 0 ){
                successMessage = "与 "+totalAttachedClassificationCount+" 项分类关联成功";
            }else{
                successMessage = "原有 "+alreadyAttachedClassificationCount+" 项分类已经关联,"+
                "与 "+totalAttachedClassificationCount+" 项分类新建关联成功";
            }
            CommonUIOperationUtil.showPopupNotification(successMessage, NotificationVariant.LUMO_SUCCESS,10000, Notification.Position.BOTTOM_START);
            classificationsMetaInfoFilterGrid.deselectAll();
            cleanRelationAttributes();
            if(containerClassificationConfigView != null){
                containerClassificationConfigView.attachClassificationSuccessCallback(successAttachedClassifications,relationAttachInfo);
            }
        }else{
            if(alreadyAttachedClassificationCount > 0 ){
                String successMessage = "选择的 "+alreadyAttachedClassificationCount+" 项分类已经存在关联";
                CommonUIOperationUtil.showPopupNotification(successMessage, NotificationVariant.LUMO_WARNING,10000, Notification.Position.BOTTOM_START);
            }
            classificationsMetaInfoFilterGrid.deselectAll();
            cleanRelationAttributes();
        }
        coreRealm.closeGlobalSession();
    }
}
