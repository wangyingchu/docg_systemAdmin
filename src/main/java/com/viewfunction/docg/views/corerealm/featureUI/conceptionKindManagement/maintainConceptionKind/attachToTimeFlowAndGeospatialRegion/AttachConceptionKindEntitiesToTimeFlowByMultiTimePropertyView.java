package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.attachToTimeFlowAndGeospatialRegion;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttachConceptionKindEntitiesToTimeFlowByMultiTimePropertyView extends VerticalLayout {
    private String conceptionKindName;
    private Dialog containerDialog;
    private TextField eventCommentField;
    private ComboBox<TimeFlow.TimeScaleGrade> timeScaleGradeSelect;
    private VerticalLayout eventEntityAttributesContainer;
    private Map<String, AttributeEditorItemWidget> eventAttributeEditorsMap;
    private Button clearAttributeButton;
    private ComboBox<KindEntityAttributeRuntimeStatistics> timeEventAttribute_year_Select;
    private ComboBox<KindEntityAttributeRuntimeStatistics> timeEventAttribute_month_Select;
    private ComboBox<KindEntityAttributeRuntimeStatistics> timeEventAttribute_day_Select;
    private ComboBox<KindEntityAttributeRuntimeStatistics> timeEventAttribute_hour_Select;
    private ComboBox<KindEntityAttributeRuntimeStatistics> timeEventAttribute_minute_Select;

    public AttachConceptionKindEntitiesToTimeFlowByMultiTimePropertyView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
        this.setWidthFull();

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,this.conceptionKindName));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        timeEventAttribute_year_Select = new ComboBox<>("时间事件年份属性");
        timeEventAttribute_year_Select.setRequired(true);
        timeEventAttribute_year_Select.setRequiredIndicatorVisible(true);
        timeEventAttribute_year_Select.setAllowCustomValue(false);
        timeEventAttribute_year_Select.setPageSize(30);
        timeEventAttribute_year_Select.setPlaceholder("选择时间事件年份属性");
        timeEventAttribute_year_Select.setWidthFull();
        timeEventAttribute_year_Select.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });
        timeEventAttribute_year_Select.setRenderer(createRenderer());
        add(timeEventAttribute_year_Select);

        timeEventAttribute_month_Select = new ComboBox<>("时间事件月份属性");
        timeEventAttribute_month_Select.setRequired(false);
        timeEventAttribute_month_Select.setRequiredIndicatorVisible(true);
        timeEventAttribute_month_Select.setAllowCustomValue(false);
        timeEventAttribute_month_Select.setPageSize(30);
        timeEventAttribute_month_Select.setPlaceholder("选择时间事件月份属性");
        timeEventAttribute_month_Select.setWidthFull();
        timeEventAttribute_month_Select.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });
        timeEventAttribute_month_Select.setRenderer(createRenderer());
        add(timeEventAttribute_month_Select);

        timeEventAttribute_day_Select = new ComboBox<>("时间事件日属性");
        timeEventAttribute_day_Select.setRequired(false);
        timeEventAttribute_day_Select.setRequiredIndicatorVisible(true);
        timeEventAttribute_day_Select.setAllowCustomValue(false);
        timeEventAttribute_day_Select.setPageSize(30);
        timeEventAttribute_day_Select.setPlaceholder("选择时间事件日属性");
        timeEventAttribute_day_Select.setWidthFull();
        timeEventAttribute_day_Select.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });
        timeEventAttribute_day_Select.setRenderer(createRenderer());
        add(timeEventAttribute_day_Select);

        timeEventAttribute_hour_Select = new ComboBox<>("时间事件小时属性");
        timeEventAttribute_hour_Select.setRequired(false);
        timeEventAttribute_hour_Select.setRequiredIndicatorVisible(true);
        timeEventAttribute_hour_Select.setAllowCustomValue(false);
        timeEventAttribute_hour_Select.setPageSize(30);
        timeEventAttribute_hour_Select.setPlaceholder("选择时间事件小时属性");
        timeEventAttribute_hour_Select.setWidthFull();
        timeEventAttribute_hour_Select.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });
        timeEventAttribute_hour_Select.setRenderer(createRenderer());
        add(timeEventAttribute_hour_Select);

        timeEventAttribute_minute_Select = new ComboBox<>("时间事件分钟属性");
        timeEventAttribute_minute_Select.setRequired(false);
        timeEventAttribute_minute_Select.setRequiredIndicatorVisible(true);
        timeEventAttribute_minute_Select.setAllowCustomValue(false);
        timeEventAttribute_minute_Select.setPageSize(30);
        timeEventAttribute_minute_Select.setPlaceholder("选择时间事件分钟属性");
        timeEventAttribute_minute_Select.setWidthFull();
        timeEventAttribute_minute_Select.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });
        timeEventAttribute_minute_Select.setRenderer(createRenderer());
        add(timeEventAttribute_minute_Select);

        timeScaleGradeSelect = new ComboBox<>("事件时间刻度");
        timeScaleGradeSelect.setRequired(true);
        timeScaleGradeSelect.setRequiredIndicatorVisible(true);
        timeScaleGradeSelect.setItems(TimeFlow.TimeScaleGrade.YEAR,TimeFlow.TimeScaleGrade.MONTH,
                TimeFlow.TimeScaleGrade.DAY,TimeFlow.TimeScaleGrade.HOUR,TimeFlow.TimeScaleGrade.MINUTE);
        timeScaleGradeSelect.setAllowCustomValue(false);
        timeScaleGradeSelect.setPageSize(30);
        timeScaleGradeSelect.setPlaceholder("选择事件时间刻度");
        timeScaleGradeSelect.setWidthFull();
        add(timeScaleGradeSelect);

        eventCommentField = new TextField("时间事件备注");
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

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定时间事件属性");
        addEventAttributesUIContainerLayout.add(infoTitle3);

        Button addAttributeButton = new Button();
        addAttributeButton.setTooltipText("添加时间事件属性");
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
        clearAttributeButton.setTooltipText("清除已设置时间事件属性");
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

        Button confirmButton = new Button("链接概念类型实体至时间流",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //attachConceptionKindEntitiesToTimeFlow();
            }
        });
        this.eventAttributeEditorsMap = new HashMap<>();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadAttributeNamesComboBox();
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

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"添加时间事件属性",null,true,480,190,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private Renderer<KindEntityAttributeRuntimeStatistics> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    ${item.attributeName}");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeDataType}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<KindEntityAttributeRuntimeStatistics>of(tpl.toString())
                .withProperty("attributeName", KindEntityAttributeRuntimeStatistics::getAttributeName)
                .withProperty("attributeDataType", KindEntityAttributeRuntimeStatistics::getAttributeDataType);
    }

    private void cleanRelationAttributes(){
        eventEntityAttributesContainer.removeAll();
        eventAttributeEditorsMap.clear();
        clearAttributeButton.setEnabled(false);
    }

    private void loadAttributeNamesComboBox(){
        int entityAttributesDistributionStatisticSampleRatio = 20000;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKindName);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList =
                targetConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
        coreRealm.closeGlobalSession();

        List<KindEntityAttributeRuntimeStatistics> dataTypeMatchedAttributeList = new ArrayList<>();
        for(KindEntityAttributeRuntimeStatistics currentKindEntityAttributeRuntimeStatistics : kindEntityAttributeRuntimeStatisticsList){
            AttributeDataType attributeDataType = currentKindEntityAttributeRuntimeStatistics.getAttributeDataType();
            switch(attributeDataType){
                case STRING, INT, LONG, FLOAT -> dataTypeMatchedAttributeList.add(currentKindEntityAttributeRuntimeStatistics);
            }
        }
        timeEventAttribute_year_Select.setItems(dataTypeMatchedAttributeList);
        timeEventAttribute_month_Select.setItems(dataTypeMatchedAttributeList);
        timeEventAttribute_day_Select.setItems(dataTypeMatchedAttributeList);
        timeEventAttribute_hour_Select.setItems(dataTypeMatchedAttributeList);
        timeEventAttribute_minute_Select.setItems(dataTypeMatchedAttributeList);
    }
}
