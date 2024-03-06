package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.attachToTimeFlowAndGeospatialRegion;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialRegion;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;

import java.util.*;

public class AttachConceptionKindEntitiesToGeospatialRegionByGeoComputeView extends VerticalLayout {
    private String conceptionKindName;
    private Dialog containerDialog;
    private TextField eventCommentField;
    private ComboBox<GeospatialRegion.GeospatialScaleGrade> geospatialScaleGradeSelect;
    private VerticalLayout eventEntityAttributesContainer;
    private Map<String, AttributeEditorItemWidget> eventAttributeEditorsMap;
    private Button clearAttributeButton;
    private ComboBox<GeospatialScaleCalculable.SpatialScaleLevel> spatialScaleLevelSelect;
    private ComboBox<GeospatialScaleCalculable.SpatialPredicateType> spatialPredicateTypeSelect;
    private ComboBox<String> geospatialRegionNameSelect;

    public AttachConceptionKindEntitiesToGeospatialRegionByGeoComputeView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
        this.setWidthFull();

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,this.conceptionKindName));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        geospatialRegionNameSelect = new ComboBox<>("地理空间区域名称");
        geospatialRegionNameSelect.setRequired(true);
        geospatialRegionNameSelect.setRequiredIndicatorVisible(true);
        geospatialRegionNameSelect.setAllowCustomValue(false);
        geospatialRegionNameSelect.setPageSize(5);
        geospatialRegionNameSelect.setPlaceholder("选择地理空间区域名称");
        geospatialRegionNameSelect.setWidthFull();
        add(geospatialRegionNameSelect);

        spatialScaleLevelSelect = new ComboBox<>("地理空间尺度");
        spatialScaleLevelSelect.setRequired(true);
        spatialScaleLevelSelect.setRequiredIndicatorVisible(true);
        spatialScaleLevelSelect.setItems(
                GeospatialScaleCalculable.SpatialScaleLevel.Global,
                GeospatialScaleCalculable.SpatialScaleLevel.Country,
                GeospatialScaleCalculable.SpatialScaleLevel.Local);
        spatialScaleLevelSelect.setAllowCustomValue(false);
        spatialScaleLevelSelect.setPageSize(3);
        spatialScaleLevelSelect.setPlaceholder("选择空间计算使用的地理空间尺度参考坐标系");
        spatialScaleLevelSelect.setWidthFull();
        add(spatialScaleLevelSelect);

        spatialPredicateTypeSelect = new ComboBox<>("地理空间计算谓词类型");
        spatialPredicateTypeSelect.setRequired(true);
        spatialPredicateTypeSelect.setRequiredIndicatorVisible(true);
        spatialPredicateTypeSelect.setItems(
                GeospatialScaleCalculable.SpatialPredicateType.Contains,
                GeospatialScaleCalculable.SpatialPredicateType.Intersects,
                GeospatialScaleCalculable.SpatialPredicateType.Within,
                GeospatialScaleCalculable.SpatialPredicateType.Equals,
                GeospatialScaleCalculable.SpatialPredicateType.Crosses,
                GeospatialScaleCalculable.SpatialPredicateType.Touches,
                GeospatialScaleCalculable.SpatialPredicateType.Overlaps,
                GeospatialScaleCalculable.SpatialPredicateType.Disjoint,
                GeospatialScaleCalculable.SpatialPredicateType.Cover,
                GeospatialScaleCalculable.SpatialPredicateType.CoveredBy);
        spatialPredicateTypeSelect.setAllowCustomValue(false);
        spatialPredicateTypeSelect.setPageSize(10);
        spatialPredicateTypeSelect.setPlaceholder("选择空间计算使用的空间拓扑关系定义");
        spatialPredicateTypeSelect.setWidthFull();
        add(spatialPredicateTypeSelect);

        geospatialScaleGradeSelect = new ComboBox<>("事件地理空间刻度");
        geospatialScaleGradeSelect.setRequired(true);
        geospatialScaleGradeSelect.setRequiredIndicatorVisible(true);
        geospatialScaleGradeSelect.setItems(
                GeospatialRegion.GeospatialScaleGrade.CONTINENT,
                GeospatialRegion.GeospatialScaleGrade.COUNTRY_REGION,
                GeospatialRegion.GeospatialScaleGrade.PROVINCE,
                GeospatialRegion.GeospatialScaleGrade.PREFECTURE,
                GeospatialRegion.GeospatialScaleGrade.COUNTY,
                GeospatialRegion.GeospatialScaleGrade.TOWNSHIP,
                GeospatialRegion.GeospatialScaleGrade.VILLAGE);
        geospatialScaleGradeSelect.setAllowCustomValue(false);
        geospatialScaleGradeSelect.setPageSize(7);
        geospatialScaleGradeSelect.setPlaceholder("选择事件地理空间刻度");
        geospatialScaleGradeSelect.setWidthFull();
        add(geospatialScaleGradeSelect);

        eventCommentField = new TextField("地理空间事件备注");
        eventCommentField.setRequired(true);
        eventCommentField.setRequiredIndicatorVisible(true);
        eventCommentField.setWidthFull();
        add(eventCommentField);

        HorizontalLayout addEventAttributesUIContainerLayout = new HorizontalLayout();
        addEventAttributesUIContainerLayout.setSpacing(false);
        addEventAttributesUIContainerLayout.setMargin(false);
        addEventAttributesUIContainerLayout.setPadding(false);
        add(addEventAttributesUIContainerLayout);

        addEventAttributesUIContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定地理空间事件属性");
        addEventAttributesUIContainerLayout.add(infoTitle3);

        Button addAttributeButton = new Button();
        addAttributeButton.setTooltipText("添加地理空间事件属性");
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        addAttributeButton.setIcon(VaadinIcon.KEYBOARD_O.create());
        addAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddNewAttributeUI();
            }
        });
        addEventAttributesUIContainerLayout.add(addAttributeButton);

        clearAttributeButton = new Button();
        clearAttributeButton.setTooltipText("清除已设置地理空间事件属性");
        clearAttributeButton.setEnabled(false);
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        clearAttributeButton.setIcon(VaadinIcon.RECYCLE.create());
        clearAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cleanRelationAttributes();
            }
        });
        addEventAttributesUIContainerLayout.add(clearAttributeButton);

        eventEntityAttributesContainer = new VerticalLayout();
        eventEntityAttributesContainer.setMargin(false);
        eventEntityAttributesContainer.setSpacing(false);
        eventEntityAttributesContainer.setPadding(false);
        eventEntityAttributesContainer.setWidth(100, Unit.PERCENTAGE);

        Scroller relationEntityAttributesScroller = new Scroller(eventEntityAttributesContainer);
        relationEntityAttributesScroller.setHeight(150,Unit.PIXELS);
        relationEntityAttributesScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        add(relationEntityAttributesScroller);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("链接概念类型实体至地理空间区域",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                attachConceptionKindEntitiesToGeospatialRegion();
            }
        });
        this.eventAttributeEditorsMap = new HashMap<>();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadGeospatialRegionNameSelect();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void renderAddNewAttributeUI(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(null,null, AddEntityAttributeView.KindType.ConceptionKind);
        AttributeValueOperateHandler attributeValueOperateHandlerForDelete = new AttributeValueOperateHandler(){
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                String attributeName = attributeValue.getAttributeName();
                if(eventAttributeEditorsMap.containsKey(attributeName)){
                    eventEntityAttributesContainer.remove(eventAttributeEditorsMap.get(attributeName));
                }
                eventAttributeEditorsMap.remove(attributeName);
                if(eventAttributeEditorsMap.size()==0) {
                    clearAttributeButton.setEnabled(false);
                }
            }
        };
        AttributeValueOperateHandler attributeValueOperateHandlerForAdd = new AttributeValueOperateHandler() {
            @Override
            public void handleAttributeValue(AttributeValue attributeValue) {
                String attributeName = attributeValue.getAttributeName();
                if(eventAttributeEditorsMap.containsKey(attributeName)){
                    CommonUIOperationUtil.showPopupNotification("已经设置了名称为 "+attributeName+" 的事件属性", NotificationVariant.LUMO_ERROR);
                }else{
                    AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(null,null,attributeValue, AttributeEditorItemWidget.KindType.RelationKind);
                    attributeEditorItemWidget.setWidth(445,Unit.PIXELS);
                    eventEntityAttributesContainer.add(attributeEditorItemWidget);
                    attributeEditorItemWidget.setAttributeValueOperateHandler(attributeValueOperateHandlerForDelete);
                    eventAttributeEditorsMap.put(attributeName,attributeEditorItemWidget);
                }
                if(eventAttributeEditorsMap.size()>0){
                    clearAttributeButton.setEnabled(true);
                }
            }
        };
        addEntityAttributeView.setAttributeValueOperateHandler(attributeValueOperateHandlerForAdd);

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"添加地理空间事件属性",null,true,480,190,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void cleanRelationAttributes(){
        eventEntityAttributesContainer.removeAll();
        eventAttributeEditorsMap.clear();
        clearAttributeButton.setEnabled(false);
    }

    private void loadGeospatialRegionNameSelect(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<GeospatialRegion> geospatialRegionList = coreRealm.getGeospatialRegions();
        if(geospatialRegionList != null){
            List<String> geospatialRegionNameList = new ArrayList<>();
            for(GeospatialRegion currentGeospatialRegion : geospatialRegionList){
                geospatialRegionNameList.add(currentGeospatialRegion.getGeospatialRegionName());
            }
            geospatialRegionNameSelect.setItems(geospatialRegionNameList);
            if(!geospatialRegionNameList.isEmpty()){
                geospatialRegionNameSelect.setValue(geospatialRegionNameList.get(0));
            }
        }
    }

    private void attachConceptionKindEntitiesToGeospatialRegion(){
        String geospatialRegionName = geospatialRegionNameSelect.getValue();
        GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel = spatialScaleLevelSelect.getValue();
        GeospatialScaleCalculable.SpatialPredicateType spatialPredicateType = spatialPredicateTypeSelect.getValue();
        GeospatialRegion.GeospatialScaleGrade geospatialScaleGrade = geospatialScaleGradeSelect.getValue();
        String eventComment = eventCommentField.getValue();
        if(geospatialRegionName == null){
            CommonUIOperationUtil.showPopupNotification("请选择地理空间区域名称", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }
        if(spatialScaleLevel == null){
            CommonUIOperationUtil.showPopupNotification("请选择地理空间尺度", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }
        if(spatialPredicateType == null){
            CommonUIOperationUtil.showPopupNotification("请选择地理空间计算谓词类型", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }
        if(geospatialScaleGrade == null){
            CommonUIOperationUtil.showPopupNotification("请选择事件地理空间刻度", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }
        if(eventComment.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入地理空间事件备注", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }

        List<Button> actionButtonList = new ArrayList<>();

        Button confirmButton = new Button("确认链接概念类型实体至地理空间区域",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行链接概念类型实体至地理空间区域操作",actionButtonList,400,180);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doAttachConceptionKindEntitiesToGeospatialRegion();
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

    private void doAttachConceptionKindEntitiesToGeospatialRegion(){
        Map<String, Object> eventData = eventAttributeEditorsMap.isEmpty() ? null : new HashMap<>();
        if(!eventAttributeEditorsMap.isEmpty()){
            Set<String> commentPropertiesNameSet = eventAttributeEditorsMap.keySet();
            for(String currentPropertyName:commentPropertiesNameSet){
                AttributeEditorItemWidget attributeEditorItemWidget = eventAttributeEditorsMap.get(currentPropertyName);
                eventData.put(attributeEditorItemWidget.getAttributeName(),attributeEditorItemWidget.getAttributeValue().getAttributeValue());
            }
        }

        String geospatialRegionName = geospatialRegionNameSelect.getValue();
        GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel = spatialScaleLevelSelect.getValue();
        GeospatialScaleCalculable.SpatialPredicateType spatialPredicateType = spatialPredicateTypeSelect.getValue();
        GeospatialRegion.GeospatialScaleGrade geospatialScaleGrade = geospatialScaleGradeSelect.getValue();
        String eventComment = eventCommentField.getValue();

        try {
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
            QueryParameters queryParameters = new QueryParameters();
            queryParameters.setResultNumber(100000000);
            EntitiesOperationStatistics attachResult = targetConceptionKind.attachGeospatialScaleEventsByEntityGeometryContent(
                    queryParameters,spatialScaleLevel,spatialPredicateType,geospatialScaleGrade,geospatialRegionName,eventComment,eventData);
            showPopupNotification(attachResult,NotificationVariant.LUMO_SUCCESS);
            if(this.containerDialog != null){
                this.containerDialog.close();
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    private void showPopupNotification(EntitiesOperationStatistics conceptionEntitiesAttributesRetrieveResult, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("概念类型 "+conceptionKindName+" 链接概念类型实体至地理空间区域操作成功"));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.addClickListener(event -> {
            notification.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setFlexGrow(1,text);
        notification.add(layout);

        VerticalLayout notificationMessageContainer = new VerticalLayout();
        notificationMessageContainer.add(new Div(new Text("链接实体数: "+conceptionEntitiesAttributesRetrieveResult.getSuccessItemsCount())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+conceptionEntitiesAttributesRetrieveResult.getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+conceptionEntitiesAttributesRetrieveResult.getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.setDuration(3000);
        notification.open();
    }
}
