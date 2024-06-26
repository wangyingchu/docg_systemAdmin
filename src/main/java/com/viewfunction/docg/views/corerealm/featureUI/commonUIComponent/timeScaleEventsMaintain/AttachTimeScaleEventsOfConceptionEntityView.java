package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.timeScaleEventsMaintain;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
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
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleMoment;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.text.NumberFormat;
import java.util.*;

public class AttachTimeScaleEventsOfConceptionEntityView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private RadioButtonGroup<String> timeScaleGradeLevelRadioGroup;
    private TimeFlow.TimeScaleGrade queryTimeScaleGrade = TimeFlow.TimeScaleGrade.YEAR;
    private TextField startYearTextField;
    private TextField toYearTextField;
    private ComboBox<Integer> startMonthComboBox;
    private ComboBox<Integer> toMonthComboBox;
    private ComboBox<Integer> startDayComboBox;
    private ComboBox<Integer> toDayComboBox;
    private ComboBox<Integer> startHourComboBox;
    private ComboBox<Integer> toHourComboBox;
    private ComboBox<Integer> startMinuteComboBox;
    private ComboBox<Integer> toMinuteComboBox;
    private ComboBox<String> timeFlowNameSelect;
    private NativeLabel resultNumberValue;
    private Grid<TimeScaleEntity> timeScaleEntitiesGrid;
    private TextField eventCommentField;
    private VerticalLayout eventEntityAttributesContainer;
    private Map<String, AttributeEditorItemWidget> eventAttributeEditorsMap;
    private Button clearAttributeButton;
    private NumberFormat numberFormat;
    private Dialog containerDialog;
    private AttachTimeScaleEventsOfConceptionEntityCallback attachTimeScaleEventsOfConceptionEntityCallback;

    public void setAttachTimeScaleEventsOfConceptionEntityCallback(AttachTimeScaleEventsOfConceptionEntityCallback attachTimeScaleEventsOfConceptionEntityCallback) {
        this.attachTimeScaleEventsOfConceptionEntityCallback = attachTimeScaleEventsOfConceptionEntityCallback;
    }

    public interface AttachTimeScaleEventsOfConceptionEntityCallback {
        public void onSuccess(List<TimeScaleEvent> resultEventList);
    }

    public AttachTimeScaleEventsOfConceptionEntityView (String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;

        this.setSpacing(false);

        this.numberFormat = NumberFormat.getInstance();

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,conceptionKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,conceptionEntityUID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout titleDivLayout = new HorizontalLayout();
        titleDivLayout.setWidthFull();
        titleDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        add(titleDivLayout);

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setWidthFull();
        add(mainLayout);

        VerticalScrollLayout leftSideSectionContainerLayout = new VerticalScrollLayout();
        leftSideSectionContainerLayout.setWidth(245,Unit.PIXELS);
        mainLayout.add(leftSideSectionContainerLayout);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"时间尺度实体检索");
        leftSideSectionContainerLayout.add(filterTitle2);

        HorizontalLayout heightSpaceDiv01 = new HorizontalLayout();
        heightSpaceDiv01.setWidthFull();
        leftSideSectionContainerLayout.add(heightSpaceDiv01);
        heightSpaceDiv01.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        timeFlowNameSelect = new ComboBox<>("时间流名称");
        timeFlowNameSelect.setWidth(225,Unit.PIXELS);
        timeFlowNameSelect.setRequired(true);
        timeFlowNameSelect.setRequiredIndicatorVisible(true);
        timeFlowNameSelect.setAllowCustomValue(false);
        timeFlowNameSelect.setPageSize(5);
        timeFlowNameSelect.setPlaceholder("选择时间流名称");
        leftSideSectionContainerLayout.add(timeFlowNameSelect);

        HorizontalLayout checkBoxesContainer1 = new HorizontalLayout();
        checkBoxesContainer1.getStyle().set("padding-top", "var(--lumo-space-m)");
        leftSideSectionContainerLayout.add(checkBoxesContainer1);

        NativeLabel timeGuLevelFilterText = new NativeLabel("时间粒度 :");
        timeGuLevelFilterText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(timeGuLevelFilterText);

        timeScaleGradeLevelRadioGroup = new RadioButtonGroup<>();
        timeScaleGradeLevelRadioGroup.setItems("年", "月", "日","小时","分钟");
        leftSideSectionContainerLayout.add(timeScaleGradeLevelRadioGroup);
        timeScaleGradeLevelRadioGroup.setValue("年");

        timeScaleGradeLevelRadioGroup.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String> radioButtonGroupStringComponentValueChangeEvent) {
                String newValue = radioButtonGroupStringComponentValueChangeEvent.getValue();
                setupTimeScaleGradeSearchElements(newValue);
            }
        });

        HorizontalLayout yearValueContainer = new HorizontalLayout();
        leftSideSectionContainerLayout.add(yearValueContainer);
        NativeLabel yearFilterText = new NativeLabel("年 :");
        yearFilterText.setWidth(30,Unit.PIXELS);
        yearFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        yearValueContainer.add(yearFilterText);
        yearValueContainer.setVerticalComponentAlignment(Alignment.CENTER,yearFilterText);

        startYearTextField = new TextField();
        startYearTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        startYearTextField.setWidth(80,Unit.PIXELS);
        Button syncStartToEndYear = new Button();
        syncStartToEndYear.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon rightDirIcon0 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_RIGHT_SOLID.create();
        rightDirIcon0.setSize("14px");
        syncStartToEndYear.setIcon(rightDirIcon0);
        yearValueContainer.add(startYearTextField);
        startYearTextField.setPrefixComponent(syncStartToEndYear);
        syncStartToEndYear.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(!startYearTextField.isInvalid() && startYearTextField.getValue() != ""){
                    toYearTextField.setValue(startYearTextField.getValue());
                }
            }
        });

        Icon inputDivIcon0 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon0.setSize("10px");
        yearValueContainer.add(inputDivIcon0);
        yearValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon0);

        toYearTextField = new TextField();
        toYearTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        toYearTextField.setWidth(80,Unit.PIXELS);
        yearValueContainer.add(toYearTextField);
        Button syncEndToStartYear = new Button();
        syncEndToStartYear.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon leftDirIcon0 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_LEFT_SOLID.create();
        leftDirIcon0.setSize("14px");
        syncEndToStartYear.setIcon(leftDirIcon0);
        toYearTextField.setPrefixComponent(syncEndToStartYear);
        syncEndToStartYear.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(!toYearTextField.isInvalid() && toYearTextField.getValue() != ""){
                    startYearTextField.setValue(toYearTextField.getValue());
                }
            }
        });

        HorizontalLayout monthValueContainer = new HorizontalLayout();
        leftSideSectionContainerLayout.add(monthValueContainer);
        NativeLabel monthFilterText = new NativeLabel("月 :");
        monthFilterText.setWidth(30,Unit.PIXELS);
        monthFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        monthValueContainer.add(monthFilterText);
        monthValueContainer.setVerticalComponentAlignment(Alignment.CENTER,monthFilterText);

        startMonthComboBox = new ComboBox<>();
        startMonthComboBox.setItems(1,2,3,4,5,6,7,8,9,10,11,12);
        startMonthComboBox.setWidth(80,Unit.PIXELS);
        startMonthComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        startMonthComboBox.setAllowCustomValue(false);
        monthValueContainer.add(startMonthComboBox);
        Button syncStartToEndMonth = new Button();
        syncStartToEndMonth.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon rightDirIcon1 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_RIGHT_SOLID.create();
        rightDirIcon1.setSize("14px");
        syncStartToEndMonth.setIcon(rightDirIcon1);
        startMonthComboBox.setPrefixComponent(syncStartToEndMonth);
        syncStartToEndMonth.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(startMonthComboBox.getValue() != null){
                    toMonthComboBox.setValue(startMonthComboBox.getValue());
                }
            }
        });

        Icon inputDivIcon1 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon1.setSize("10px");
        monthValueContainer.add(inputDivIcon1);
        monthValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon1);

        toMonthComboBox = new ComboBox<>();
        toMonthComboBox.setItems(1,2,3,4,5,6,7,8,9,10,11,12);
        toMonthComboBox.setWidth(80,Unit.PIXELS);
        toMonthComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        toMonthComboBox.setAllowCustomValue(false);
        monthValueContainer.add(toMonthComboBox);
        Button syncEndToStartMonth = new Button();
        syncEndToStartMonth.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon leftDirIcon1 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_LEFT_SOLID.create();
        leftDirIcon1.setSize("14px");
        syncEndToStartMonth.setIcon(leftDirIcon1);
        toMonthComboBox.setPrefixComponent(syncEndToStartMonth);
        syncEndToStartMonth.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(toMonthComboBox.getValue() != null){
                    startMonthComboBox.setValue(toMonthComboBox.getValue());
                }
            }
        });

        HorizontalLayout dayValueContainer = new HorizontalLayout();
        leftSideSectionContainerLayout.add(dayValueContainer);
        NativeLabel dayFilterText = new NativeLabel("日 :");
        dayFilterText.setWidth(30,Unit.PIXELS);
        dayFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        dayValueContainer.add(dayFilterText);
        dayValueContainer.setVerticalComponentAlignment(Alignment.CENTER,dayFilterText);

        startDayComboBox = new ComboBox<>();
        startDayComboBox.setItems(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31);
        startDayComboBox.setWidth(80,Unit.PIXELS);
        startDayComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        startDayComboBox.setAllowCustomValue(false);
        dayValueContainer.add(startDayComboBox);
        Button syncStartToEndDay = new Button();
        syncStartToEndDay.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon rightDirIcon2 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_RIGHT_SOLID.create();
        rightDirIcon2.setSize("14px");
        syncStartToEndDay.setIcon(rightDirIcon2);
        startDayComboBox.setPrefixComponent(syncStartToEndDay);
        syncStartToEndDay.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(startDayComboBox.getValue() != null){
                    toDayComboBox.setValue(startDayComboBox.getValue());
                }
            }
        });

        Icon inputDivIcon2 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon2.setSize("10px");
        dayValueContainer.add(inputDivIcon2);
        dayValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon2);

        toDayComboBox = new ComboBox<>();
        toDayComboBox.setItems(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31);
        toDayComboBox.setWidth(80,Unit.PIXELS);
        toDayComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        toDayComboBox.setAllowCustomValue(false);
        dayValueContainer.add(toDayComboBox);
        Button syncEndToStartDay = new Button();
        syncEndToStartDay.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon leftDirIcon2 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_LEFT_SOLID.create();
        leftDirIcon2.setSize("14px");
        syncEndToStartDay.setIcon(leftDirIcon2);
        toDayComboBox.setPrefixComponent(syncEndToStartDay);
        syncEndToStartDay.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(toDayComboBox.getValue() != null){
                    startDayComboBox.setValue(toDayComboBox.getValue());
                }
            }
        });

        HorizontalLayout hourValueContainer = new HorizontalLayout();
        leftSideSectionContainerLayout.add(hourValueContainer);
        NativeLabel hourFilterText = new NativeLabel("小时 :");
        hourFilterText.setWidth(30,Unit.PIXELS);
        hourFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        hourValueContainer.add(hourFilterText);
        hourValueContainer.setVerticalComponentAlignment(Alignment.CENTER,hourFilterText);

        startHourComboBox = new ComboBox<>();
        startHourComboBox.setItems(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23);
        startHourComboBox.setWidth(80,Unit.PIXELS);
        startHourComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        startHourComboBox.setAllowCustomValue(false);
        hourValueContainer.add(startHourComboBox);
        Button syncStartToEndHour = new Button();
        syncStartToEndHour.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon rightDirIcon3 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_RIGHT_SOLID.create();
        rightDirIcon3.setSize("14px");
        syncStartToEndHour.setIcon(rightDirIcon3);
        startHourComboBox.setPrefixComponent(syncStartToEndHour);
        syncStartToEndHour.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(startHourComboBox.getValue() != null){
                    toHourComboBox.setValue(startHourComboBox.getValue());
                }
            }
        });

        Icon inputDivIcon3 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon3.setSize("10px");
        hourValueContainer.add(inputDivIcon3);
        hourValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon3);

        toHourComboBox = new ComboBox<>();
        toHourComboBox.setItems(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23);
        toHourComboBox.setWidth(80,Unit.PIXELS);
        toHourComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        toHourComboBox.setAllowCustomValue(false);
        hourValueContainer.add(toHourComboBox);
        Button syncEndToStartHour = new Button();
        syncEndToStartHour.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon leftDirIcon3 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_LEFT_SOLID.create();
        leftDirIcon3.setSize("14px");
        syncEndToStartHour.setIcon(leftDirIcon3);
        toHourComboBox.setPrefixComponent(syncEndToStartHour);
        syncEndToStartHour.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(toHourComboBox.getValue() != null){
                    startHourComboBox.setValue(toHourComboBox.getValue());
                }
            }
        });

        HorizontalLayout minuteValueContainer = new HorizontalLayout();
        leftSideSectionContainerLayout.add(minuteValueContainer);
        NativeLabel minuteFilterText = new NativeLabel("分钟 :");
        minuteFilterText.setWidth(30,Unit.PIXELS);
        minuteFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        minuteValueContainer.add(minuteFilterText);
        minuteValueContainer.setVerticalComponentAlignment(Alignment.CENTER,minuteFilterText);

        startMinuteComboBox = new ComboBox<>();
        startMinuteComboBox.setItems(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59);
        startMinuteComboBox.setWidth(80,Unit.PIXELS);
        startMinuteComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        startMinuteComboBox.setAllowCustomValue(false);
        minuteValueContainer.add(startMinuteComboBox);
        Button syncStartToEndMinute = new Button();
        syncStartToEndMinute.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon rightDirIcon4 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_RIGHT_SOLID.create();
        rightDirIcon4.setSize("14px");
        syncStartToEndMinute.setIcon(rightDirIcon4);
        startMinuteComboBox.setPrefixComponent(syncStartToEndMinute);
        syncStartToEndMinute.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(startMinuteComboBox.getValue() != null){
                    toMinuteComboBox.setValue(startMinuteComboBox.getValue());
                }
            }
        });

        Icon inputDivIcon4 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon4.setSize("10px");
        minuteValueContainer.add(inputDivIcon4);
        minuteValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon4);

        toMinuteComboBox = new ComboBox<>();
        toMinuteComboBox.setItems(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59);
        toMinuteComboBox.setWidth(80,Unit.PIXELS);
        toMinuteComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        toMinuteComboBox.setAllowCustomValue(false);
        minuteValueContainer.add(toMinuteComboBox);
        Button syncEndToStartMinute = new Button();
        syncEndToStartMinute.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon leftDirIcon4 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_LEFT_SOLID.create();
        leftDirIcon4.setSize("14px");
        syncEndToStartMinute.setIcon(leftDirIcon4);
        toMinuteComboBox.setPrefixComponent(syncEndToStartMinute);
        syncEndToStartMinute.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(toMinuteComboBox.getValue() != null){
                    startMinuteComboBox.setValue(toMinuteComboBox.getValue());
                }
            }
        });

        HorizontalLayout heightSpaceDiv1 = new HorizontalLayout();
        heightSpaceDiv1.setWidthFull();
        leftSideSectionContainerLayout.add(heightSpaceDiv1);
        heightSpaceDiv1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("padding-bottom","2px");

        HorizontalLayout heightSpaceDiv2 = new HorizontalLayout();
        heightSpaceDiv2.setWidth(10,Unit.PIXELS);
        heightSpaceDiv2.setHeight(15,Unit.PIXELS);
        leftSideSectionContainerLayout.add(heightSpaceDiv2);

        Button executeQueryButton = new Button("检索时间尺度实体");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                queryTimeScaleEntities();
            }
        });
        leftSideSectionContainerLayout.add(executeQueryButton);

        VerticalLayout middleContainerLayout = new VerticalLayout();
        middleContainerLayout.setSpacing(false);
        middleContainerLayout.setPadding(false);
        middleContainerLayout.setMargin(false);
        mainLayout.add(middleContainerLayout);

        middleContainerLayout.setWidth(315, Unit.PIXELS);
        middleContainerLayout.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");

        resultNumberValue = new NativeLabel("");
        resultNumberValue.addClassNames("text-xs","font-bold");
        resultNumberValue.getStyle().set("padding-right","10px");

        SecondaryIconTitle filterResultTitle = new SecondaryIconTitle(FontAwesome.Solid.CLOCK.create(),"时间尺度实体检索结果",resultNumberValue);
        filterResultTitle.getStyle().set("padding-left","10px").set("padding-top","9px");
        middleContainerLayout.add(filterResultTitle);

        HorizontalLayout heightSpaceDiv02 = new HorizontalLayout();
        heightSpaceDiv02.setWidthFull();
        middleContainerLayout.add(heightSpaceDiv02);
        heightSpaceDiv02.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(timeScaleEntity -> {
            Icon eyeIcon = new Icon(VaadinIcon.EYE);
            eyeIcon.setSize("20px");
            Button timeScaleEntityDetailButton = new Button(eyeIcon, event -> {
                if(timeScaleEntity instanceof TimeScaleEntity){
                    renderTimeScaleEntityDetailUI((TimeScaleEntity)timeScaleEntity);
                }
            });
            timeScaleEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            timeScaleEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            timeScaleEntityDetailButton.setTooltipText("显示时间粒度实体详情");

            HorizontalLayout buttons = new HorizontalLayout(timeScaleEntityDetailButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        timeScaleEntitiesGrid = new Grid<>();
        timeScaleEntitiesGrid.setWidth(310,Unit.PIXELS);
        timeScaleEntitiesGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        timeScaleEntitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER);
        timeScaleEntitiesGrid.addColumn(TimeScaleEntity::getTimeScaleGrade).setHeader("粒度").setKey("idx_0").setWidth("70px");
        timeScaleEntitiesGrid.addColumn(TimeScaleEntity::getTimeScaleEntityDesc).setHeader("时间描述").setKey("idx_1");
        timeScaleEntitiesGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_2").setWidth("60px");

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"粒度");
        timeScaleEntitiesGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"时间描述");
        timeScaleEntitiesGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        timeScaleEntitiesGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        middleContainerLayout.add(timeScaleEntitiesGrid);

        VerticalLayout rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setSpacing(false);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);
        mainLayout.add(rightSideContainerLayout);

        rightSideContainerLayout.setWidth(470,Unit.PIXELS);
        rightSideContainerLayout.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");

        SecondaryIconTitle eventCommentInfoTitle = new SecondaryIconTitle(VaadinIcon.CALENDAR_CLOCK.create(), "事件信息");
        eventCommentInfoTitle.getStyle().set("padding-left","10px").set("padding-top","9px");
        rightSideContainerLayout.add(eventCommentInfoTitle);

        HorizontalLayout heightSpaceDiv03 = new HorizontalLayout();
        heightSpaceDiv03.setWidthFull();
        rightSideContainerLayout.add(heightSpaceDiv03);
        heightSpaceDiv03.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        VerticalLayout rightSideContentLayout = new VerticalLayout();
        rightSideContentLayout.setSpacing(true);
        rightSideContainerLayout.add(rightSideContentLayout);

        eventCommentField = new TextField("时间事件备注");
        eventCommentField.setRequired(true);
        eventCommentField.setRequiredIndicatorVisible(true);
        eventCommentField.setWidthFull();
        rightSideContentLayout.add(eventCommentField);

        HorizontalLayout addEventAttributesUIContainerLayout = new HorizontalLayout();
        addEventAttributesUIContainerLayout.setSpacing(false);
        addEventAttributesUIContainerLayout.setMargin(false);
        addEventAttributesUIContainerLayout.setPadding(false);
        rightSideContentLayout.add(addEventAttributesUIContainerLayout);

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
        relationEntityAttributesScroller.setHeight(250,Unit.PIXELS);
        relationEntityAttributesScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        rightSideContentLayout.add(relationEntityAttributesScroller);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        rightSideContentLayout.add(spaceDivLayout);

        Button confirmButton = new Button("链接概念实体至时间流",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        rightSideContentLayout.add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                attachConceptionEntityToTimeFlow();
            }
        });
        this.eventAttributeEditorsMap = new HashMap<>();
        setupTimeScaleGradeSearchElements("年");
        this.queryTimeScaleGrade = TimeFlow.TimeScaleGrade.YEAR;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadAttributesInfoComboBox();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void loadAttributesInfoComboBox(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<TimeFlow> timeFlowsList = coreRealm.getTimeFlows();
        if(timeFlowsList != null){
            List<String> timeFlowNames = new ArrayList<>();
            for(TimeFlow currentTimeFlow : timeFlowsList){
                timeFlowNames.add(currentTimeFlow.getTimeFlowName());
            }
            timeFlowNameSelect.setItems(timeFlowNames);
            if(!timeFlowNames.isEmpty()){
                timeFlowNameSelect.setValue(timeFlowNames.get(0));
            }
        }
    }

    private void setupTimeScaleGradeSearchElements(String gradeValue){
        if(gradeValue.equals("年")){
            queryTimeScaleGrade = TimeFlow.TimeScaleGrade.YEAR;
            startMonthComboBox.setEnabled(false);
            toMonthComboBox.setEnabled(false);
            startDayComboBox.setEnabled(false);
            toDayComboBox.setEnabled(false);
            startHourComboBox.setEnabled(false);
            toHourComboBox.setEnabled(false);
            startMinuteComboBox.setEnabled(false);
            toMinuteComboBox.setEnabled(false);
        }else if(gradeValue.equals("月")){
            queryTimeScaleGrade = TimeFlow.TimeScaleGrade.MONTH;
            startMonthComboBox.setEnabled(true);
            toMonthComboBox.setEnabled(true);
            startDayComboBox.setEnabled(false);
            toDayComboBox.setEnabled(false);
            startHourComboBox.setEnabled(false);
            toHourComboBox.setEnabled(false);
            startMinuteComboBox.setEnabled(false);
            toMinuteComboBox.setEnabled(false);
        }else if(gradeValue.equals("日")){
            queryTimeScaleGrade = TimeFlow.TimeScaleGrade.DAY;
            startMonthComboBox.setEnabled(true);
            toMonthComboBox.setEnabled(true);
            startDayComboBox.setEnabled(true);
            toDayComboBox.setEnabled(true);
            startHourComboBox.setEnabled(false);
            toHourComboBox.setEnabled(false);
            startMinuteComboBox.setEnabled(false);
            toMinuteComboBox.setEnabled(false);
        }else if(gradeValue.equals("小时")){
            queryTimeScaleGrade = TimeFlow.TimeScaleGrade.HOUR;
            startMonthComboBox.setEnabled(true);
            toMonthComboBox.setEnabled(true);
            startDayComboBox.setEnabled(true);
            toDayComboBox.setEnabled(true);
            startHourComboBox.setEnabled(true);
            toHourComboBox.setEnabled(true);
            startMinuteComboBox.setEnabled(false);
            toMinuteComboBox.setEnabled(false);
        }else if(gradeValue.equals("分钟")){
            queryTimeScaleGrade = TimeFlow.TimeScaleGrade.MINUTE;
            startMonthComboBox.setEnabled(true);
            toMonthComboBox.setEnabled(true);
            startDayComboBox.setEnabled(true);
            toDayComboBox.setEnabled(true);
            startHourComboBox.setEnabled(true);
            toHourComboBox.setEnabled(true);
            startMinuteComboBox.setEnabled(true);
            toMinuteComboBox.setEnabled(true);
        }
    }

    private void queryTimeScaleEntities(){
        if(timeFlowNameSelect.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("请选择时间流名称 ",
                    NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
            return;
        }

        if(startYearTextField.isInvalid() || toYearTextField.isInvalid()){
            CommonUIOperationUtil.showPopupNotification("请输入正确的年时间数值 ",
                    NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
            return;
        }
        timeScaleEntitiesGrid.setItems(new ArrayList<>());
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();

        TimeFlow targetTimeFlow = coreRealm.getOrCreateTimeFlow(timeFlowNameSelect.getValue());

        List<TimeScaleEntity> resultTimeScaleEntityList = new ArrayList<>();
        boolean executedQuery = false;
        DateTimeCheckResult yearDateTimeCheckResult;
        DateTimeCheckResult monthDateTimeCheckResult;
        DateTimeCheckResult dayDateTimeCheckResult;
        DateTimeCheckResult hourDateTimeCheckResult;
        DateTimeCheckResult minuteDateTimeCheckResult;
        String queryTimeArea = "";
        try {
            switch(queryTimeScaleGrade){
                case YEAR:
                    yearDateTimeCheckResult = checkYearInputsData(false);
                    switch(yearDateTimeCheckResult){
                        case correct :
                            resultTimeScaleEntityList.addAll(
                                    targetTimeFlow.getYearEntities(Integer.parseInt(startYearTextField.getValue()),
                                            Integer.parseInt(toYearTextField.getValue())));
                            executedQuery = true;
                            queryTimeArea = ""+startYearTextField.getValue() + " - "+toYearTextField.getValue();
                            break;
                        case singleEntity:
                            int startYear0 = Integer.parseInt(startYearTextField.getValue());
                            TimeScaleEntity resultTimeScaleEntity = targetTimeFlow.getYearEntity(startYear0);
                            if(resultTimeScaleEntity != null){
                                resultTimeScaleEntityList.add(resultTimeScaleEntity);
                            }
                            executedQuery = true;
                            queryTimeArea = ""+startYearTextField.getValue();
                            break;
                    }
                    break;
                case MONTH:
                    yearDateTimeCheckResult = checkYearInputsData(true);
                    monthDateTimeCheckResult = checkDateInputsData(startMonthComboBox,toMonthComboBox,"请输入起始月时间值");
                    if(!DateTimeCheckResult.inCorrect.equals(yearDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(monthDateTimeCheckResult)){
                        if(DateTimeCheckResult.correct.equals(yearDateTimeCheckResult) &
                                DateTimeCheckResult.correct.equals(monthDateTimeCheckResult)){
                            if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                    toMonthComboBox.getValue() <= startMonthComboBox.getValue()){
                                CommonUIOperationUtil.showPopupNotification("结束月时间值必须大于起始月时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else{
                                TimeScaleMoment startTimeScaleMoment1 = new TimeScaleMoment(Integer.parseInt(startYearTextField.getValue()),startMonthComboBox.getValue());
                                TimeScaleMoment toTimeScaleMoment1 = new TimeScaleMoment(Integer.parseInt(toYearTextField.getValue()),toMonthComboBox.getValue());
                                resultTimeScaleEntityList.addAll(targetTimeFlow.getMonthEntities(startTimeScaleMoment1,toTimeScaleMoment1));
                                executedQuery = true;
                                queryTimeArea = ""+startYearTextField.getValue() +"/"+startMonthComboBox.getValue()+ " - "+toYearTextField.getValue()+"/"+toMonthComboBox.getValue();
                            }
                        }else{
                            TimeScaleEntity resultTimeScaleEntity = targetTimeFlow.getMonthEntity(Integer.parseInt(startYearTextField.getValue()),startMonthComboBox.getValue());
                            if(resultTimeScaleEntity != null){
                                resultTimeScaleEntityList.add(resultTimeScaleEntity);
                            }
                            executedQuery = true;
                            queryTimeArea = ""+startYearTextField.getValue() +"/"+startMonthComboBox.getValue();
                            break;
                        }
                    }
                    break;
                case DAY:
                    yearDateTimeCheckResult = checkYearInputsData(true);
                    monthDateTimeCheckResult = checkDateInputsData(startMonthComboBox,toMonthComboBox,"请输入起始月时间值");
                    dayDateTimeCheckResult = checkDateInputsData(startDayComboBox,toDayComboBox,"请输入起始日时间值");
                    if(!DateTimeCheckResult.inCorrect.equals(yearDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(monthDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(dayDateTimeCheckResult)){
                    }{
                    if(DateTimeCheckResult.correct.equals(yearDateTimeCheckResult) &
                            DateTimeCheckResult.correct.equals(monthDateTimeCheckResult) &
                            DateTimeCheckResult.correct.equals(dayDateTimeCheckResult)){
                        if(Integer.parseInt(toYearTextField.getValue()) < Integer.parseInt(startYearTextField.getValue())){
                            CommonUIOperationUtil.showPopupNotification("结束年时间值必须大于等于起始年时间值",
                                    NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                        }else if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                toMonthComboBox.getValue() < startMonthComboBox.getValue()){
                            CommonUIOperationUtil.showPopupNotification("结束月时间值必须大于等于起始月时间值",
                                    NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                        }else if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                toMonthComboBox.getValue() == startMonthComboBox.getValue() & toDayComboBox.getValue() <= startDayComboBox.getValue()){
                            CommonUIOperationUtil.showPopupNotification("结束日时间值必须大于起始日时间值",
                                    NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                        }else{
                            int startYear2 = Integer.parseInt(startYearTextField.getValue());
                            int toYear2 = Integer.parseInt(toYearTextField.getValue());
                            int startMonth2 = startMonthComboBox.getValue();
                            int toMonth2 = toMonthComboBox.getValue();
                            int startDay2 = startDayComboBox.getValue();
                            int toDay2 = toDayComboBox.getValue();
                            TimeScaleMoment startTimeScaleMoment2 = new TimeScaleMoment(startYear2,startMonth2,startDay2);
                            TimeScaleMoment toTimeScaleMoment2 = new TimeScaleMoment(toYear2,toMonth2,toDay2);
                            resultTimeScaleEntityList.addAll(targetTimeFlow.getDayEntities(startTimeScaleMoment2,toTimeScaleMoment2));
                            executedQuery = true;
                            queryTimeArea = ""+startYearTextField.getValue() +"/"+startMonthComboBox.getValue()+"/"+startDayComboBox.getValue()+ " - "+toYearTextField.getValue()+"/"+toMonthComboBox.getValue()+"/"+toDayComboBox.getValue();
                        }
                    }else{
                        TimeScaleEntity resultTimeScaleEntity = targetTimeFlow.getDayEntity(Integer.parseInt(startYearTextField.getValue()),startMonthComboBox.getValue(),startDayComboBox.getValue());
                        if(resultTimeScaleEntity != null){
                            resultTimeScaleEntityList.add(resultTimeScaleEntity);
                        }
                        executedQuery = true;
                        queryTimeArea = ""+startYearTextField.getValue() +"/"+startMonthComboBox.getValue()+"/"+startDayComboBox.getValue();
                    }
                }
                break;
                case HOUR:
                    yearDateTimeCheckResult = checkYearInputsData(true);
                    monthDateTimeCheckResult = checkDateInputsData(startMonthComboBox,toMonthComboBox,"请输入起始月时间值");
                    dayDateTimeCheckResult = checkDateInputsData(startDayComboBox,toDayComboBox,"请输入起始日时间值");
                    hourDateTimeCheckResult = checkDateInputsData(startHourComboBox,toHourComboBox,"请输入起始小时时间值");
                    if(!DateTimeCheckResult.inCorrect.equals(yearDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(monthDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(dayDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(hourDateTimeCheckResult)){
                        if(DateTimeCheckResult.correct.equals(yearDateTimeCheckResult) &
                                DateTimeCheckResult.correct.equals(monthDateTimeCheckResult) &
                                DateTimeCheckResult.correct.equals(dayDateTimeCheckResult) &
                                DateTimeCheckResult.correct.equals(hourDateTimeCheckResult)){
                            if(Integer.parseInt(toYearTextField.getValue()) < Integer.parseInt(startYearTextField.getValue())){
                                CommonUIOperationUtil.showPopupNotification("结束年时间值必须大于等于起始年时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                    toMonthComboBox.getValue() < startMonthComboBox.getValue()){
                                CommonUIOperationUtil.showPopupNotification("结束月时间值必须大于等于起始月时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                    toMonthComboBox.getValue() == startMonthComboBox.getValue() & toDayComboBox.getValue() < startDayComboBox.getValue()){
                                CommonUIOperationUtil.showPopupNotification("结束日时间值必须大于等于起始日时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                    toMonthComboBox.getValue() == startMonthComboBox.getValue() & toDayComboBox.getValue() == startDayComboBox.getValue() &
                                    toHourComboBox.getValue() <= startHourComboBox.getValue()){
                                CommonUIOperationUtil.showPopupNotification("结束小时时间值必须大于起始小时时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else{
                                int startYear = Integer.parseInt(startYearTextField.getValue());
                                int toYear = Integer.parseInt(toYearTextField.getValue());
                                int startMonth = startMonthComboBox.getValue();
                                int toMonth = toMonthComboBox.getValue();
                                int startDay = startDayComboBox.getValue();
                                int toDay = toDayComboBox.getValue();
                                int startHour = startHourComboBox.getValue();
                                int toHour = toHourComboBox.getValue();
                                TimeScaleMoment startTimeScaleMoment2 = new TimeScaleMoment(startYear,startMonth,startDay,startHour);
                                TimeScaleMoment toTimeScaleMoment2 = new TimeScaleMoment(toYear,toMonth,toDay,toHour);
                                resultTimeScaleEntityList.addAll(targetTimeFlow.getHourEntities(startTimeScaleMoment2,toTimeScaleMoment2));
                                executedQuery = true;
                                queryTimeArea = ""+startYearTextField.getValue() +"/"+startMonthComboBox.getValue()+"/"+startDayComboBox.getValue()+" "+startHourComboBox.getValue()+ " - "+toYearTextField.getValue()+"/"+toMonthComboBox.getValue()+"/"+toDayComboBox.getValue()+" "+toHourComboBox.getValue();
                            }
                        }else{
                            TimeScaleEntity resultTimeScaleEntity = targetTimeFlow.getHourEntity(Integer.parseInt(startYearTextField.getValue()),
                                    startMonthComboBox.getValue(),startDayComboBox.getValue(),startHourComboBox.getValue());
                            if(resultTimeScaleEntity != null){
                                resultTimeScaleEntityList.add(resultTimeScaleEntity);
                            }
                            executedQuery = true;
                            queryTimeArea = ""+startYearTextField.getValue() +"/"+startMonthComboBox.getValue()+"/"+startDayComboBox.getValue()+" "+startHourComboBox.getValue();
                        }
                    }
                    break;
                case MINUTE:
                    yearDateTimeCheckResult = checkYearInputsData(true);
                    monthDateTimeCheckResult = checkDateInputsData(startMonthComboBox,toMonthComboBox,"请输入起始月时间值");
                    dayDateTimeCheckResult = checkDateInputsData(startDayComboBox,toDayComboBox,"请输入起始日时间值");
                    hourDateTimeCheckResult = checkDateInputsData(startHourComboBox,toHourComboBox,"请输入起始小时时间值");
                    minuteDateTimeCheckResult = checkDateInputsData(startMinuteComboBox,toMinuteComboBox,"请输入起始分钟时间值");
                    if(!DateTimeCheckResult.inCorrect.equals(yearDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(monthDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(dayDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(hourDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(minuteDateTimeCheckResult)){
                        if(DateTimeCheckResult.correct.equals(yearDateTimeCheckResult) &
                                DateTimeCheckResult.correct.equals(monthDateTimeCheckResult) &
                                DateTimeCheckResult.correct.equals(dayDateTimeCheckResult) &
                                DateTimeCheckResult.correct.equals(hourDateTimeCheckResult) &
                                DateTimeCheckResult.correct.equals(minuteDateTimeCheckResult)){
                            if(Integer.parseInt(toYearTextField.getValue()) < Integer.parseInt(startYearTextField.getValue())){
                                CommonUIOperationUtil.showPopupNotification("结束年时间值必须大于等于起始年时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                    toMonthComboBox.getValue() < startMonthComboBox.getValue()){
                                CommonUIOperationUtil.showPopupNotification("结束月时间值必须大于等于起始月时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                    toMonthComboBox.getValue() == startMonthComboBox.getValue() & toDayComboBox.getValue() < startDayComboBox.getValue()){
                                CommonUIOperationUtil.showPopupNotification("结束日时间值必须大于等于起始日时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                    toMonthComboBox.getValue() == startMonthComboBox.getValue() & toDayComboBox.getValue() == startDayComboBox.getValue() &
                                    toHourComboBox.getValue() < startHourComboBox.getValue()){
                                CommonUIOperationUtil.showPopupNotification("结束小时时间值必须大于等于起始小时时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else if(Integer.parseInt(startYearTextField.getValue()) == Integer.parseInt(toYearTextField.getValue()) &
                                    toMonthComboBox.getValue() == startMonthComboBox.getValue() & toDayComboBox.getValue() == startDayComboBox.getValue() &
                                    toHourComboBox.getValue() == startHourComboBox.getValue() & toMinuteComboBox.getValue() <= startMinuteComboBox.getValue()){
                                CommonUIOperationUtil.showPopupNotification("结束分钟时间值必须大于起始分钟时间值",
                                        NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                            }else{
                                int startYear = Integer.parseInt(startYearTextField.getValue());
                                int toYear = Integer.parseInt(toYearTextField.getValue());
                                int startMonth = startMonthComboBox.getValue();
                                int toMonth = toMonthComboBox.getValue();
                                int startDay = startDayComboBox.getValue();
                                int toDay = toDayComboBox.getValue();
                                int startHour = startHourComboBox.getValue();
                                int toHour = toHourComboBox.getValue();
                                int startMinute = startMinuteComboBox.getValue();
                                int toMinute = toMinuteComboBox.getValue();
                                TimeScaleMoment startTimeScaleMoment2 = new TimeScaleMoment(startYear,startMonth,startDay,startHour,startMinute);
                                TimeScaleMoment toTimeScaleMoment2 = new TimeScaleMoment(toYear,toMonth,toDay,toHour,toMinute);
                                resultTimeScaleEntityList.addAll(targetTimeFlow.getMinuteEntities(startTimeScaleMoment2,toTimeScaleMoment2));
                                executedQuery = true;
                                queryTimeArea = ""+startYearTextField.getValue() +"/"+startMonthComboBox.getValue()+"/"+startDayComboBox.getValue()+" "+startHourComboBox.getValue()+":"+startMinuteComboBox.getValue()+ " - "+toYearTextField.getValue()+"/"+toMonthComboBox.getValue()+"/"+toDayComboBox.getValue()+" "+toHourComboBox.getValue()+":"+toMinuteComboBox.getValue();
                            }
                        }else{
                            TimeScaleEntity resultTimeScaleEntity = targetTimeFlow.getMinuteEntity(Integer.parseInt(startYearTextField.getValue()),
                                    startMonthComboBox.getValue(),startDayComboBox.getValue(),startHourComboBox.getValue(),startMinuteComboBox.getValue());
                            if(resultTimeScaleEntity != null){
                                resultTimeScaleEntityList.add(resultTimeScaleEntity);
                            }
                            executedQuery = true;
                            queryTimeArea = ""+startYearTextField.getValue() +"/"+startMonthComboBox.getValue()+"/"+startDayComboBox.getValue()+" "+startHourComboBox.getValue()+":"+startMinuteComboBox.getValue();
                        }
                    }
                    break;
            }
            if(executedQuery){
                CommonUIOperationUtil.showPopupNotification("时间粒度实体查询操作成功，查询返回实体数: "+resultTimeScaleEntityList.size(),
                        NotificationVariant.LUMO_SUCCESS,3000, Notification.Position.BOTTOM_START);
                timeScaleEntitiesGrid.setItems(resultTimeScaleEntityList);
                resultNumberValue.setText("实体总量："+this.numberFormat.format(resultTimeScaleEntityList.size()));
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private enum DateTimeCheckResult { correct,singleEntity,inCorrect}

    private DateTimeCheckResult checkYearInputsData(boolean allowEqual){
        if(startYearTextField.getValue().equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入起始年时间值",
                    NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
            return DateTimeCheckResult.inCorrect;
        }else{
            int startYear = Integer.parseInt(startYearTextField.getValue());
            if(toYearTextField.getValue().equals("")){
                return DateTimeCheckResult.singleEntity;
            }else {
                int toYear = Integer.parseInt(toYearTextField.getValue());
                if(allowEqual){
                    if(toYear < startYear){
                        CommonUIOperationUtil.showPopupNotification("结束年时间值必须大于等于起始年时间值",
                                NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                        return DateTimeCheckResult.inCorrect;
                    }
                }else{
                    if(toYear <= startYear){
                        CommonUIOperationUtil.showPopupNotification("结束年时间值必须大于起始年时间值",
                                NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
                        return DateTimeCheckResult.inCorrect;
                    }
                }
            }
        }
        return DateTimeCheckResult.correct;
    }

    private DateTimeCheckResult checkDateInputsData(ComboBox<Integer> startDate, ComboBox<Integer> toDate, String errorMessage){
        if(startDate.getValue() == null){
            CommonUIOperationUtil.showPopupNotification(errorMessage,
                    NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
            return DateTimeCheckResult.inCorrect;
        }else{
            if(toDate.getValue() == null){
                return DateTimeCheckResult.singleEntity;
            }
        }
        return DateTimeCheckResult.correct;
    }

    private void renderTimeScaleEntityDetailUI(TimeScaleEntity timeScaleEntity){
        String timeScaleEntityClassName = RealmConstant.TimeScaleEntityClass;
        TimeFlow.TimeScaleGrade currentTimeScaleGrade = timeScaleEntity.getTimeScaleGrade();
        switch(currentTimeScaleGrade){
            case YEAR -> timeScaleEntityClassName = RealmConstant.TimeScaleYearEntityClass;
            case MONTH -> timeScaleEntityClassName = RealmConstant.TimeScaleMonthEntityClass;
            case DAY -> timeScaleEntityClassName = RealmConstant.TimeScaleDayEntityClass;
            case HOUR -> timeScaleEntityClassName = RealmConstant.TimeScaleHourEntityClass;
            case MINUTE -> timeScaleEntityClassName = RealmConstant.TimeScaleMinuteEntityClass;
        }
        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(timeScaleEntityClassName,timeScaleEntity.getTimeScaleEntityUID());

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
        NativeLabel conceptionKindNameLabel = new NativeLabel(timeScaleEntityClassName);
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
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(timeScaleEntity.getTimeScaleEntityUID());
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
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

    private void cleanRelationAttributes(){
        eventEntityAttributesContainer.removeAll();
        eventAttributeEditorsMap.clear();
        clearAttributeButton.setEnabled(false);
    }

    private void attachConceptionEntityToTimeFlow(){
        Set<TimeScaleEntity> selectedTimeScaleEntitySet = timeScaleEntitiesGrid.getSelectedItems();
        if(selectedTimeScaleEntitySet == null || selectedTimeScaleEntitySet.isEmpty()){
            CommonUIOperationUtil.showPopupNotification("请选择至少一项时间尺度实体", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }
        String eventComment = eventCommentField.getValue();
        if(eventComment.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入时间事件备注", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
            return;
        }

        List<Button> actionButtonList = new ArrayList<>();

        Button confirmButton = new Button("确认链接概念实体至时间流",new Icon(VaadinIcon.CHECK_CIRCLE));
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行链接概念实体至时间流操作",actionButtonList,400,180);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doAttachConceptionEntityToTimeFlow();
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

    private void doAttachConceptionEntityToTimeFlow(){
        Set<TimeScaleEntity> selectedTimeScaleEntitySet = timeScaleEntitiesGrid.getSelectedItems();
        if(selectedTimeScaleEntitySet != null & !selectedTimeScaleEntitySet.isEmpty()){
            List<TimeScaleEvent> resultEventList = new ArrayList<>();
            Map<String, Object> eventData = eventAttributeEditorsMap.isEmpty() ? null : new HashMap<>();
            if(!eventAttributeEditorsMap.isEmpty()){
                Set<String> commentPropertiesNameSet = eventAttributeEditorsMap.keySet();
                for(String currentPropertyName:commentPropertiesNameSet){
                    AttributeEditorItemWidget attributeEditorItemWidget = eventAttributeEditorsMap.get(currentPropertyName);
                    eventData.put(attributeEditorItemWidget.getAttributeName(),attributeEditorItemWidget.getAttributeValue().getAttributeValue());
                }
            }

            String eventComment = eventCommentField.getValue();

            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
            ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);

            for(TimeScaleEntity currentTimeScaleEntity : selectedTimeScaleEntitySet){
                try {
                    TimeScaleEvent resultEvent = targetConceptionEntity.attachTimeScaleEventByTimeScaleEntityUID(currentTimeScaleEntity.getTimeScaleEntityUID(),eventComment,eventData);
                    if(resultEvent != null){
                        resultEventList.add(resultEvent);
                    }
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
            }

            if(this.attachTimeScaleEventsOfConceptionEntityCallback != null){
                this.attachTimeScaleEventsOfConceptionEntityCallback.onSuccess(resultEventList);
            }

            if(this.containerDialog != null){
                this.containerDialog.close();
            }
            coreRealm.closeGlobalSession();
            showPopupNotification(resultEventList.size(),NotificationVariant.LUMO_SUCCESS);
        }
    }

    private void showPopupNotification(int resultNumber, NotificationVariant notificationVariant){
        Notification notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        Div text = new Div(new Text("链接概念实体 "+this.conceptionKind+"/"+this.conceptionEntityUID+ " 至时间流操作成功"));
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
        notificationMessageContainer.add(new Div(new Text("链接实体数: "+resultNumber)));
        notification.setDuration(3000);
        notification.open();
    }
}
