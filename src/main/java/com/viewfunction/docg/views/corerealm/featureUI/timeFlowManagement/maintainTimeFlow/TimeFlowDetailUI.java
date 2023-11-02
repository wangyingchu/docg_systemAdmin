package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeFlowRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleMoment;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Route("timeFlowDetailInfo/:timeFlow")
public class TimeFlowDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String timeFlowName;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout middleContainerLayout;
    private Registration listener;
    private Grid<TimeScaleEntity> timeScaleEntitiesGrid;
    private NativeLabel startYearValue;
    private NativeLabel toYearValue;
    private RadioButtonGroup<String> timeScaleGradeLevelRadioGroup;
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
    private TimeFlow.TimeScaleGrade queryTimeScaleGrade;
    private NumberFormat numberFormat;
    private SecondaryKeyValueDisplayItem totalTimeScaleEntityCountDisplayItem;
    private SecondaryKeyValueDisplayItem totalTimeScaleEventCountDisplayItem;
    private SecondaryKeyValueDisplayItem yearEntityCountItem;
    private SecondaryKeyValueDisplayItem yearEventCountItem;
    private SecondaryKeyValueDisplayItem monthEntityCountItem;
    private SecondaryKeyValueDisplayItem monthEventCountItem;
    private SecondaryKeyValueDisplayItem dayEntityCountItem;
    private SecondaryKeyValueDisplayItem dayEventCountItem;
    private SecondaryKeyValueDisplayItem hourEntityCountItem;
    private SecondaryKeyValueDisplayItem hourEventCountItem;
    private SecondaryKeyValueDisplayItem minuteEntityCountItem;
    private SecondaryKeyValueDisplayItem minuteEventCountItem;
    private Binder<String> binder;

    public TimeFlowDetailUI(){}

    public TimeFlowDetailUI(String timeFlowName){
        this.timeFlowName = timeFlowName;
        this.binder = new Binder<>();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.timeFlowName = beforeEnterEvent.getRouteParameters().get("timeFlow").get();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderTimeFlowData();
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            leftSideContainerLayout.setHeight(event.getHeight()-265,Unit.PIXELS);
            middleContainerLayout.setHeight(event.getHeight()-265,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            leftSideContainerLayout.setHeight(browserHeight-265,Unit.PIXELS);
            middleContainerLayout.setHeight(browserHeight-265,Unit.PIXELS);
        }));
        renderTimeFlowBasicInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void renderTimeFlowData(){
        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setSpacing(false);
        mainContainerLayout.setPadding(false);
        mainContainerLayout.setMargin(false);
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);

        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(270, Unit.PIXELS);
        leftSideContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"时间流概览");
        leftSideContainerLayout.add(filterTitle1);

        HorizontalLayout heightSpaceDiv0 = new HorizontalLayout();
        heightSpaceDiv0.setWidth(95,Unit.PERCENTAGE);
        leftSideContainerLayout.add(heightSpaceDiv0);
        heightSpaceDiv0.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");

        VerticalLayout timeFlowInformationLayout = new VerticalLayout();
        timeFlowInformationLayout.setMargin(false);

        leftSideContainerLayout.add(timeFlowInformationLayout);
        VerticalScrollLayout leftSideSectionContainerScrollLayout = new VerticalScrollLayout();
        leftSideContainerLayout.add(leftSideSectionContainerScrollLayout);

        HorizontalLayout timeHorizontalLayout = new HorizontalLayout();
        timeHorizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        MenuBar timeflowOperationMenuBar = new MenuBar();
        timeflowOperationMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        MenuItem timeFlowOperationMenu = createIconItem(timeflowOperationMenuBar, VaadinIcon.COG, "配置", null);
        timeFlowOperationMenu.getStyle().set("font-size","0.7rem");
        Icon downArrowIcon = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon.setSize("10px");
        timeFlowOperationMenu.add(downArrowIcon);

        SubMenu operationSubItems = timeFlowOperationMenu.getSubMenu();
        MenuItem expandFlowActionItem = operationSubItems.addItem("扩展时间跨度");
        expandFlowActionItem.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                //renderLoadCSVFormatConceptionEntitiesView();
            }
        });

        List<Component> actionComponentsList = new ArrayList<>();
        actionComponentsList.add(timeflowOperationMenuBar);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TIMER),this.timeFlowName,null,actionComponentsList);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        timeFlowInformationLayout.add(secondaryTitleActionBar);

        NativeLabel yearSpanText = new NativeLabel("时间跨度:");
        yearSpanText.addClassNames("text-xs","font-semibold","text-secondary");
        timeHorizontalLayout.add(yearSpanText);

        Span fromYear = new Span();
        startYearValue = new NativeLabel("");
        startYearValue.addClassNames("text-l","font-extrabold");
        fromYear.add(startYearValue);
        fromYear.getElement().getThemeList().add("badge pill");
        fromYear.addClassNames("text-xl","text-primary","font-extrabold");
        fromYear.getStyle().set("color","#2e4e7e");

        Icon yearDivIcon = VaadinIcon.ARROWS_LONG_RIGHT.create();
        yearDivIcon.setSize("12px");

        Span toYear = new Span();
        toYearValue = new NativeLabel("");
        toYearValue.addClassNames("text-l","font-extrabold");
        toYear.add(toYearValue);
        toYear.getElement().getThemeList().add("badge pill");
        toYear.addClassNames("text-xl","font-bold");
        toYear.getStyle().set("color","#2e4e7e");

        timeHorizontalLayout.add(fromYear);
        timeHorizontalLayout.add(yearDivIcon);
        timeHorizontalLayout.add(toYear);
        timeHorizontalLayout.setVerticalComponentAlignment(Alignment.CENTER,yearDivIcon);
        timeFlowInformationLayout.add(timeHorizontalLayout);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        totalTimeScaleEntityCountDisplayItem = new SecondaryKeyValueDisplayItem(horizontalLayout, FontAwesome.Solid.CLOCK.create(),"TimeScaleEntity 数量:","-");
        timeFlowInformationLayout.add(horizontalLayout);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        totalTimeScaleEventCountDisplayItem = new SecondaryKeyValueDisplayItem(horizontalLayout2,FontAwesome.Solid.BEZIER_CURVE.create(),"TimeScaleEvent 数量:","-");
        timeFlowInformationLayout.add(horizontalLayout2);

        VerticalLayout timeFlowInfoWallContainerLayout = new VerticalLayout();
        timeFlowInfoWallContainerLayout.setMargin(false);
        timeFlowInfoWallContainerLayout.setPadding(false);
        timeFlowInfoWallContainerLayout.setSpacing(false);
        timeFlowInfoWallContainerLayout.setWidth(95,Unit.PERCENTAGE);
        timeFlowInformationLayout.add(timeFlowInfoWallContainerLayout);

        HorizontalLayout yearEntitiesInfoLayout = new HorizontalLayout();
        yearEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel yearEntitiesLabel = new NativeLabel("Year Entities:");
        yearEntityCountItem = new SecondaryKeyValueDisplayItem(yearEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        yearEventCountItem = new SecondaryKeyValueDisplayItem(yearEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon yearInfoTitleIcon = new Icon(VaadinIcon.CALENDAR);
        yearInfoTitleIcon.setSize("10px");
        SectionWallTitle yearInfoSectionWallTitle = new SectionWallTitle(yearInfoTitleIcon,yearEntitiesLabel);
        SectionWallContainer yearInfoSectionWallContainer = new SectionWallContainer(yearInfoSectionWallTitle,yearEntitiesInfoLayout);
        yearInfoSectionWallContainer.setOpened(false);
        timeFlowInfoWallContainerLayout.add(yearInfoSectionWallContainer);

        HorizontalLayout monthEntitiesInfoLayout = new HorizontalLayout();
        monthEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel monthEntitiesLabel = new NativeLabel("Month Entities:");
        monthEntityCountItem = new SecondaryKeyValueDisplayItem(monthEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        monthEventCountItem = new SecondaryKeyValueDisplayItem(monthEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon monthInfoTitleIcon = new Icon(VaadinIcon.CALENDAR);
        monthInfoTitleIcon.setSize("10px");
        SectionWallTitle monthInfoSectionWallTitle = new SectionWallTitle(monthInfoTitleIcon,monthEntitiesLabel);
        SectionWallContainer monthInfoSectionWallContainer = new SectionWallContainer(monthInfoSectionWallTitle,monthEntitiesInfoLayout);
        monthInfoSectionWallContainer.setOpened(false);
        timeFlowInfoWallContainerLayout.add(monthInfoSectionWallContainer);

        HorizontalLayout dayEntitiesInfoLayout = new HorizontalLayout();
        dayEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel dayEntitiesLabel = new NativeLabel("Day Entities:");
        dayEntityCountItem = new SecondaryKeyValueDisplayItem(dayEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        dayEventCountItem = new SecondaryKeyValueDisplayItem(dayEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon dayInfoTitleIcon = new Icon(VaadinIcon.CALENDAR);
        dayInfoTitleIcon.setSize("10px");
        SectionWallTitle dayInfoSectionWallTitle = new SectionWallTitle(dayInfoTitleIcon,dayEntitiesLabel);
        SectionWallContainer dayInfoSectionWallContainer = new SectionWallContainer(dayInfoSectionWallTitle,dayEntitiesInfoLayout);
        dayInfoSectionWallContainer.setOpened(false);
        timeFlowInfoWallContainerLayout.add(dayInfoSectionWallContainer);

        HorizontalLayout hourEntitiesInfoLayout = new HorizontalLayout();
        hourEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel hourEntitiesLabel = new NativeLabel("Hour Entities:");
        hourEntityCountItem = new SecondaryKeyValueDisplayItem(hourEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        hourEventCountItem = new SecondaryKeyValueDisplayItem(hourEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon hourInfoTitleIcon = new Icon(VaadinIcon.CLOCK);
        hourInfoTitleIcon.setSize("10px");
        SectionWallTitle hourInfoSectionWallTitle = new SectionWallTitle(hourInfoTitleIcon,hourEntitiesLabel);
        SectionWallContainer hourInfoSectionWallContainer = new SectionWallContainer(hourInfoSectionWallTitle,hourEntitiesInfoLayout);
        hourInfoSectionWallContainer.setOpened(false);
        timeFlowInfoWallContainerLayout.add(hourInfoSectionWallContainer);

        HorizontalLayout minuteEntitiesInfoLayout = new HorizontalLayout();
        minuteEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel minuteEntitiesLabel = new NativeLabel("Minute Entities:");
        minuteEntityCountItem = new SecondaryKeyValueDisplayItem(minuteEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","-");
        minuteEventCountItem = new SecondaryKeyValueDisplayItem(minuteEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","-");
        Icon minuteInfoTitleIcon = new Icon(VaadinIcon.CLOCK);
        minuteInfoTitleIcon.setSize("10px");
        SectionWallTitle minuteInfoSectionWallTitle = new SectionWallTitle(minuteInfoTitleIcon,minuteEntitiesLabel);
        SectionWallContainer minuteInfoSectionWallContainer = new SectionWallContainer(minuteInfoSectionWallTitle,minuteEntitiesInfoLayout);
        minuteInfoSectionWallContainer.setOpened(false);
        timeFlowInfoWallContainerLayout.add(minuteInfoSectionWallContainer);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"时间尺度实体检索");
        leftSideSectionContainerScrollLayout.add(filterTitle2);

        HorizontalLayout heightSpaceDiv06 = new HorizontalLayout();
        heightSpaceDiv06.setWidth(95,Unit.PERCENTAGE);
        leftSideSectionContainerScrollLayout.add(heightSpaceDiv06);
        heightSpaceDiv06.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        HorizontalLayout checkBoxesContainer1 = new HorizontalLayout();
        checkBoxesContainer1.getStyle().set("padding-top", "var(--lumo-space-m)");
        leftSideSectionContainerScrollLayout.add(checkBoxesContainer1);

        NativeLabel timeGuLevelFilterText = new NativeLabel("时间粒度 :");
        timeGuLevelFilterText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(timeGuLevelFilterText);

        timeScaleGradeLevelRadioGroup = new RadioButtonGroup<>();
        timeScaleGradeLevelRadioGroup.setItems("年", "月", "日","小时","分钟");
        leftSideSectionContainerScrollLayout.add(timeScaleGradeLevelRadioGroup);
        timeScaleGradeLevelRadioGroup.setValue("年");
        timeScaleGradeLevelRadioGroup.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<RadioButtonGroup<String>, String> radioButtonGroupStringComponentValueChangeEvent) {
                String newValue = radioButtonGroupStringComponentValueChangeEvent.getValue();
                setupTimeScaleGradeSearchElements(newValue);
            }
        });

        HorizontalLayout yearValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(yearValueContainer);
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
        leftSideSectionContainerScrollLayout.add(monthValueContainer);
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
        leftSideSectionContainerScrollLayout.add(dayValueContainer);
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
        leftSideSectionContainerScrollLayout.add(hourValueContainer);
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
        leftSideSectionContainerScrollLayout.add(minuteValueContainer);
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
        heightSpaceDiv1.setWidth(90,Unit.PERCENTAGE);
        leftSideSectionContainerScrollLayout.add(heightSpaceDiv1);
        heightSpaceDiv1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("padding-bottom","2px");

        HorizontalLayout heightSpaceDiv2 = new HorizontalLayout();
        heightSpaceDiv2.setWidth(10,Unit.PIXELS);
        heightSpaceDiv2.setHeight(15,Unit.PIXELS);
        leftSideSectionContainerScrollLayout.add(heightSpaceDiv2);

        Button executeQueryButton = new Button("检索时间尺度实体");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                queryTimeScaleEntities();
            }
        });
        leftSideSectionContainerScrollLayout.add(executeQueryButton);

        middleContainerLayout = new VerticalLayout();
        middleContainerLayout.setSpacing(false);
        middleContainerLayout.setPadding(false);
        middleContainerLayout.setMargin(false);

        mainContainerLayout.add(middleContainerLayout);
        middleContainerLayout.setWidth(300, Unit.PIXELS);
        middleContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");

        SecondaryIconTitle filterTitle3 = new SecondaryIconTitle(FontAwesome.Solid.CLOCK.create(),"时间尺度实体检索结果");
        filterTitle3.getStyle().set("padding-left","10px");
        middleContainerLayout.add(filterTitle3);

        HorizontalLayout heightSpaceDivM0 = new HorizontalLayout();
        heightSpaceDivM0.setWidth(100,Unit.PERCENTAGE);
        middleContainerLayout.add(heightSpaceDivM0);
        heightSpaceDivM0.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-left", "var(--lumo-space-l)")
                .set("padding-right", "var(--lumo-space-l)")
                .set("padding-bottom", "var(--lumo-space-s)");

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
        timeScaleEntitiesGrid.setWidth(295,Unit.PIXELS);
        timeScaleEntitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER);
        timeScaleEntitiesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
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
    }

    private void renderTimeFlowBasicInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        TimeFlow targetTimeFlow = coreRealm.getOrCreateTimeFlow(this.timeFlowName);
        List<Integer> availableTimeSpanYearsList = targetTimeFlow.getAvailableTimeSpanYears();
        if(availableTimeSpanYearsList != null && availableTimeSpanYearsList.size() >0){
            if(availableTimeSpanYearsList.size() ==1){
                Integer onlyYear = availableTimeSpanYearsList.get(0);
                startYearValue.setText(""+onlyYear);
                toYearValue.setText(""+onlyYear);
                timeScaleEntitiesGrid.setItems(targetTimeFlow.getYearEntity(onlyYear));
                startYearTextField.setValue(""+onlyYear);
            }else{
                Integer firstYear = availableTimeSpanYearsList.get(0);
                Integer lastYear = availableTimeSpanYearsList.get(availableTimeSpanYearsList.size() -1);
                startYearValue.setText(""+firstYear);
                toYearValue.setText(""+lastYear);
                startYearTextField.setValue(""+firstYear);
                toYearTextField.setValue(""+lastYear);
                try {
                    List<TimeScaleEntity> timeScaleEntityList = targetTimeFlow.getYearEntities(firstYear,lastYear);
                    timeScaleEntitiesGrid.setItems(timeScaleEntityList);
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        setupTimeScaleGradeSearchElements("年");
        queryTimeScaleGrade = TimeFlow.TimeScaleGrade.YEAR;

        this.numberFormat = NumberFormat.getInstance();
        TimeFlowRuntimeStatistics timeFlowRuntimeStatistics = targetTimeFlow.getTimeFlowRuntimeStatistics();
        totalTimeScaleEntityCountDisplayItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getContainsTotalTimeScaleEntityCount()));
        totalTimeScaleEventCountDisplayItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getRefersTotalTimeScaleEventCount()));
        yearEntityCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getContainsYearScaleTimeScaleEntityCount()));
        yearEventCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getRefersYearScaleTimeScaleEventCount()));
        monthEntityCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getContainsMonthScaleTimeScaleEntityCount()));
        monthEventCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getRefersMonthScaleTimeScaleEventCount()));
        dayEntityCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getContainsDayScaleTimeScaleEntityCount()));
        dayEventCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getRefersDayScaleTimeScaleEventCount()));
        hourEntityCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getContainsHourScaleTimeScaleEntityCount()));
        hourEventCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getRefersHourScaleTimeScaleEventCount()));
        minuteEntityCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getContainsMinuteScaleTimeScaleEntityCount()));
        minuteEventCountItem.updateDisplayValue(this.numberFormat.format(timeFlowRuntimeStatistics.getRefersMinuteScaleTimeScaleEventCount()));

        binder.forField((TextField)startYearTextField).withConverter(new StringToIntegerConverter("该项属性值必须为INT类型"))
                .withValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null, null))
                .bind(new ValueProvider<String, Integer>() {
                    @Override
                    public Integer apply(String s) {
                        return Integer.valueOf(s);
                    }
                }, new Setter<String, Integer>() {
                    @Override
                    public void accept(String s, Integer intValue) {}
                });

        binder.forField((TextField)toYearTextField).withConverter(new StringToIntegerConverter("该项属性值必须为INT类型"))
                .withValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null, null))
                .bind(new ValueProvider<String, Integer>() {
                    @Override
                    public Integer apply(String s) {
                        return Integer.valueOf(s);
                    }
                }, new Setter<String, Integer>() {
                    @Override
                    public void accept(String s, Integer intValue) {}
                });
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
        if(startYearTextField.isInvalid() || toYearTextField.isInvalid()){
            CommonUIOperationUtil.showPopupNotification("请输入正确的年时间数值 ",
                    NotificationVariant.LUMO_WARNING,3000, Notification.Position.BOTTOM_START);
            return;
        }
        timeScaleEntitiesGrid.setItems(new ArrayList<>());
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        TimeFlow targetTimeFlow = coreRealm.getOrCreateTimeFlow(this.timeFlowName);
        List<TimeScaleEntity> resultTimeScaleEntityList = new ArrayList<>();
        boolean executedQuery = false;
        DateTimeCheckResult yearDateTimeCheckResult;
        DateTimeCheckResult monthDateTimeCheckResult;
        DateTimeCheckResult dayDateTimeCheckResult;
        try {
            switch(queryTimeScaleGrade){
                case YEAR :
                    yearDateTimeCheckResult = checkYearInputsData(false);
                    switch(yearDateTimeCheckResult){
                        case correct :
                            resultTimeScaleEntityList.addAll(
                                    targetTimeFlow.getYearEntities(Integer.parseInt(startYearTextField.getValue()),
                                            Integer.parseInt(toYearTextField.getValue())));
                            executedQuery = true;
                            break;
                        case singleEntity:
                            int startYear0 = Integer.parseInt(startYearTextField.getValue());
                            TimeScaleEntity resultTimeScaleEntity = targetTimeFlow.getYearEntity(startYear0);
                            resultTimeScaleEntityList.add(resultTimeScaleEntity);
                            executedQuery = true;
                            break;
                    }
                    break;
                case MONTH:
                    yearDateTimeCheckResult = checkYearInputsData(true);
                    monthDateTimeCheckResult = checkMonthInputsData(startMonthComboBox,toMonthComboBox,"请输入起始月时间值");
                    if(!DateTimeCheckResult.inCorrect.equals(yearDateTimeCheckResult) &&
                            !DateTimeCheckResult.inCorrect.equals(monthDateTimeCheckResult)){
                        if(DateTimeCheckResult.correct.equals(yearDateTimeCheckResult) &&
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
                            }
                        }else{
                            TimeScaleEntity resultTimeScaleEntity = targetTimeFlow.getMonthEntity(Integer.parseInt(startYearTextField.getValue()),startMonthComboBox.getValue());
                            resultTimeScaleEntityList.add(resultTimeScaleEntity);
                            executedQuery = true;
                            break;
                        }
                    }
                    break;
                case DAY:
                    yearDateTimeCheckResult = checkYearInputsData(true);
                    monthDateTimeCheckResult = checkMonthInputsData(startMonthComboBox,toMonthComboBox,"请输入起始月时间值");
                    dayDateTimeCheckResult = checkMonthInputsData(startDayComboBox,toDayComboBox,"请输入起始日时间值");

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
                    break;
                case HOUR:
                    int startYear3 = Integer.parseInt(startYearTextField.getValue());
                    int toYear3 = Integer.parseInt(toYearTextField.getValue());
                    int startMonth3 = startMonthComboBox.getValue();
                    int toMonth3 = toMonthComboBox.getValue();
                    int startDay3 = startDayComboBox.getValue();
                    int toDay3 = toDayComboBox.getValue();
                    int startHour3 = startHourComboBox.getValue();
                    int toHour3 = toHourComboBox.getValue();
                    TimeScaleMoment startTimeScaleMoment3 = new TimeScaleMoment(startYear3,startMonth3,startDay3,startHour3);
                    TimeScaleMoment toTimeScaleMoment3 = new TimeScaleMoment(toYear3,toMonth3,toDay3,toHour3);
                    resultTimeScaleEntityList.addAll(targetTimeFlow.getHourEntities(startTimeScaleMoment3,toTimeScaleMoment3));
                    executedQuery = true;
                    break;
                case MINUTE:
                    int startYear4 = Integer.parseInt(startYearTextField.getValue());
                    int toYear4 = Integer.parseInt(toYearTextField.getValue());
                    int startMonth4 = startMonthComboBox.getValue();
                    int toMonth4 = toMonthComboBox.getValue();
                    int startDay4 = startDayComboBox.getValue();
                    int toDay4 = toDayComboBox.getValue();
                    int startHour4 = startHourComboBox.getValue();
                    int toHour4 = toHourComboBox.getValue();
                    int startMinute4 = startMinuteComboBox.getValue();
                    int toMinute4 = toMinuteComboBox.getValue();
                    TimeScaleMoment startTimeScaleMoment4 = new TimeScaleMoment(startYear4,startMonth4,startDay4,startHour4,startMinute4);
                    TimeScaleMoment toTimeScaleMoment4 = new TimeScaleMoment(toYear4,toMonth4,toDay4,toHour4,toMinute4);
                    resultTimeScaleEntityList.addAll(targetTimeFlow.getMinuteEntities(startTimeScaleMoment4,toTimeScaleMoment4));
                    executedQuery = true;
                    break;
            }
            if(executedQuery){
                CommonUIOperationUtil.showPopupNotification("时间粒度实体查询操作成功，查询返回实体数: "+resultTimeScaleEntityList.size(),
                        NotificationVariant.LUMO_SUCCESS,3000, Notification.Position.BOTTOM_START);
                timeScaleEntitiesGrid.setItems(resultTimeScaleEntityList);
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

    private DateTimeCheckResult checkMonthInputsData(ComboBox<Integer> startDate,ComboBox<Integer> toDate,String errorMessage){
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

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel) {
        return createIconItem(menu, iconName, label, ariaLabel, false);
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,String label, String ariaLabel, boolean isChild) {
        Icon icon = new Icon(iconName);
        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }
        MenuItem item = menu.addItem(icon, e -> {});
        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }
        if (label != null) {
            item.add(new Text(label));
        }
        return item;
    }
}
