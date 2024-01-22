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

public class AttachConceptionKindEntitiesToTimeFlowView extends VerticalLayout {
    private String conceptionKindName;
    private Dialog containerDialog;
    private ComboBox<KindEntityAttributeRuntimeStatistics> timeEventAttributeSelect;
    private ComboBox<String> dateTimeFormatterSelect;
    private TextField eventCommentField;
    private ComboBox<TimeFlow.TimeScaleGrade> timeScaleGradeSelect;



    private VerticalLayout relationEntityAttributesContainer;
    private Map<String,AttributeEditorItemWidget> relationAttributeEditorsMap;
    private Button clearAttributeButton;

    public AttachConceptionKindEntitiesToTimeFlowView(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;
        this.setWidthFull();

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,this.conceptionKindName));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        timeEventAttributeSelect = new ComboBox<>("时间事件属性");
        timeEventAttributeSelect.setAllowCustomValue(true);
        timeEventAttributeSelect.setPageSize(30);
        timeEventAttributeSelect.setPlaceholder("选择时间事件属性");
        timeEventAttributeSelect.setWidthFull();
        timeEventAttributeSelect.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });
        timeEventAttributeSelect.setRenderer(createRenderer());
        add(timeEventAttributeSelect);

        dateTimeFormatterSelect = new ComboBox<>("时间日期定义格式");
        dateTimeFormatterSelect.setAllowCustomValue(true);
        dateTimeFormatterSelect.setItems(
                "yyyy-MM-dd HH:mm:ss",
                "yyyy/mm/dd hh:mm:ss",
                "MM/dd/yyyy HH:mm:ss a",
                "MM/dd/yyyy hh:mm:ss a",
                "yyyy/m/d h:mm:ss",
                "yyyy/m/dd h:mm:ss",
                "yyyyMMdd",
                "yyyy-MM-dd",
                "yyyymmdd",
                "yyyy/mm/dd",
                "yyyy/m/d"
        );
        dateTimeFormatterSelect.setPageSize(30);
        dateTimeFormatterSelect.setPlaceholder("选择或输入时间日期定义格式");
        dateTimeFormatterSelect.setWidthFull();
        add(dateTimeFormatterSelect);

        timeScaleGradeSelect = new ComboBox<>("事件时间刻度");
        timeScaleGradeSelect.setItems(TimeFlow.TimeScaleGrade.YEAR,TimeFlow.TimeScaleGrade.MONTH,
                TimeFlow.TimeScaleGrade.DAY,TimeFlow.TimeScaleGrade.HOUR,TimeFlow.TimeScaleGrade.MINUTE);
        timeScaleGradeSelect.setAllowCustomValue(false);
        timeScaleGradeSelect.setPageSize(30);
        timeScaleGradeSelect.setPlaceholder("选择事件时间刻度");
        timeScaleGradeSelect.setWidthFull();
        add(timeScaleGradeSelect);

        eventCommentField = new TextField("时间事件备注");
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

        relationEntityAttributesContainer = new VerticalLayout();
        relationEntityAttributesContainer.setMargin(false);
        relationEntityAttributesContainer.setSpacing(false);
        relationEntityAttributesContainer.setPadding(false);
        relationEntityAttributesContainer.setWidth(100, Unit.PERCENTAGE);

        Scroller relationEntityAttributesScroller = new Scroller(relationEntityAttributesContainer);
        relationEntityAttributesScroller.setHeight(150,Unit.PIXELS);
        relationEntityAttributesScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        add(relationEntityAttributesScroller);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认链接概念类型实体至时间流",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //doCreateNewConceptionKind();
            }
        });




        this.relationAttributeEditorsMap = new HashMap<>();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadAttributeNamesComboBox();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
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
                case STRING, DATE, TIME, DATETIME, TIMESTAMP -> dataTypeMatchedAttributeList.add(currentKindEntityAttributeRuntimeStatistics);
            }
        }
        timeEventAttributeSelect.setItems(dataTypeMatchedAttributeList);
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
                    attributeEditorItemWidget.setWidth(445,Unit.PIXELS);
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

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"添加时间事件属性",null,true,480,190,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void cleanRelationAttributes(){
        relationEntityAttributesContainer.removeAll();
        relationAttributeEditorsMap.clear();
        clearAttributeButton.setEnabled(false);
    }
}
