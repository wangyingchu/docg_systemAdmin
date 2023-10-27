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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeScaleEntity;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.List;

@Route("timeFlowDetailInfo/:timeFlow")
public class TimeFlowDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String timeFlowName;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout middleContainerLayout;
    private Registration listener;
    private Grid<TimeScaleEntity> timeScaleEntitiesGrid;

    public TimeFlowDetailUI(){}

    public TimeFlowDetailUI(String timeFlowName){
        this.timeFlowName = timeFlowName;
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
        NativeLabel startYearValue = new NativeLabel("1990");
        startYearValue.addClassNames("text-l","font-extrabold");
        fromYear.add(startYearValue);
        fromYear.getElement().getThemeList().add("badge pill");
        fromYear.addClassNames("text-xl","text-primary","font-extrabold");
        fromYear.getStyle().set("color","#2e4e7e");

        Icon yearDivIcon = VaadinIcon.ARROWS_LONG_RIGHT.create();
        yearDivIcon.setSize("12px");

        Span toYear = new Span();
        NativeLabel toYearValue = new NativeLabel("2050");
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
        new SecondaryKeyValueDisplayItem(horizontalLayout, FontAwesome.Solid.CLOCK.create(),"TimeScaleEntity 数量:","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        new SecondaryKeyValueDisplayItem(horizontalLayout2,FontAwesome.Solid.BEZIER_CURVE.create(),"TimeScaleEvent 数量:","1,000,000,000");
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
        new SecondaryKeyValueDisplayItem(yearEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(yearEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        Icon yearInfoTitleIcon = new Icon(VaadinIcon.CALENDAR);
        yearInfoTitleIcon.setSize("10px");
        SectionWallTitle yearInfoSectionWallTitle = new SectionWallTitle(yearInfoTitleIcon,yearEntitiesLabel);
        SectionWallContainer yearInfoSectionWallContainer = new SectionWallContainer(yearInfoSectionWallTitle,yearEntitiesInfoLayout);
        yearInfoSectionWallContainer.setOpened(false);
        timeFlowInfoWallContainerLayout.add(yearInfoSectionWallContainer);

        HorizontalLayout monthEntitiesInfoLayout = new HorizontalLayout();
        monthEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel monthEntitiesLabel = new NativeLabel("Month Entities:");
        new SecondaryKeyValueDisplayItem(monthEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(monthEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        Icon monthInfoTitleIcon = new Icon(VaadinIcon.CALENDAR);
        monthInfoTitleIcon.setSize("10px");
        SectionWallTitle monthInfoSectionWallTitle = new SectionWallTitle(monthInfoTitleIcon,monthEntitiesLabel);
        SectionWallContainer monthInfoSectionWallContainer = new SectionWallContainer(monthInfoSectionWallTitle,monthEntitiesInfoLayout);
        monthInfoSectionWallContainer.setOpened(false);
        timeFlowInfoWallContainerLayout.add(monthInfoSectionWallContainer);

        HorizontalLayout dayEntitiesInfoLayout = new HorizontalLayout();
        dayEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel dayEntitiesLabel = new NativeLabel("Day Entities:");
        new SecondaryKeyValueDisplayItem(dayEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(dayEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        Icon dayInfoTitleIcon = new Icon(VaadinIcon.CALENDAR);
        dayInfoTitleIcon.setSize("10px");
        SectionWallTitle dayInfoSectionWallTitle = new SectionWallTitle(dayInfoTitleIcon,dayEntitiesLabel);
        SectionWallContainer dayInfoSectionWallContainer = new SectionWallContainer(dayInfoSectionWallTitle,dayEntitiesInfoLayout);
        dayInfoSectionWallContainer.setOpened(false);
        timeFlowInfoWallContainerLayout.add(dayInfoSectionWallContainer);

        HorizontalLayout hourEntitiesInfoLayout = new HorizontalLayout();
        hourEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel hourEntitiesLabel = new NativeLabel("Hour Entities:");
        new SecondaryKeyValueDisplayItem(hourEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(hourEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        Icon hourInfoTitleIcon = new Icon(VaadinIcon.CLOCK);
        hourInfoTitleIcon.setSize("10px");
        SectionWallTitle hourInfoSectionWallTitle = new SectionWallTitle(hourInfoTitleIcon,hourEntitiesLabel);
        SectionWallContainer hourInfoSectionWallContainer = new SectionWallContainer(hourInfoSectionWallTitle,hourEntitiesInfoLayout);
        hourInfoSectionWallContainer.setOpened(false);
        timeFlowInfoWallContainerLayout.add(hourInfoSectionWallContainer);

        HorizontalLayout minuteEntitiesInfoLayout = new HorizontalLayout();
        minuteEntitiesInfoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel minuteEntitiesLabel = new NativeLabel("Minute Entities:");
        new SecondaryKeyValueDisplayItem(minuteEntitiesInfoLayout,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(minuteEntitiesInfoLayout,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
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

        RadioButtonGroup<String> timeScaleGradeLevelRadioGroup = new RadioButtonGroup<>();
        timeScaleGradeLevelRadioGroup.setItems("年", "月", "日","小时","分钟");
        leftSideSectionContainerScrollLayout.add(timeScaleGradeLevelRadioGroup);

        HorizontalLayout yearValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(yearValueContainer);
        NativeLabel yearFilterText = new NativeLabel("年 :");
        yearFilterText.setWidth(30,Unit.PIXELS);
        yearFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        yearValueContainer.add(yearFilterText);
        yearValueContainer.setVerticalComponentAlignment(Alignment.CENTER,yearFilterText);

        TextField startYearTextField = new TextField();
        startYearTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        startYearTextField.setWidth(80,Unit.PIXELS);
        Button syncStartToEndYear = new Button();
        syncStartToEndYear.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon rightDirIcon0 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_RIGHT_SOLID.create();
        rightDirIcon0.setSize("14px");
        syncStartToEndYear.setIcon(rightDirIcon0);
        yearValueContainer.add(startYearTextField);
        startYearTextField.setPrefixComponent(syncStartToEndYear);

        Icon inputDivIcon0 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon0.setSize("10px");
        yearValueContainer.add(inputDivIcon0);
        yearValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon0);

        TextField toYearTextField = new TextField();
        toYearTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        toYearTextField.setWidth(80,Unit.PIXELS);
        yearValueContainer.add(toYearTextField);
        Button syncEndToStartYear = new Button();
        syncEndToStartYear.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE);
        Icon leftDirIcon0 = LineAwesomeIconsSvg.CHEVRON_CIRCLE_LEFT_SOLID.create();
        leftDirIcon0.setSize("14px");
        syncEndToStartYear.setIcon(leftDirIcon0);
        toYearTextField.setPrefixComponent(syncEndToStartYear);

        HorizontalLayout monthValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(monthValueContainer);
        NativeLabel monthFilterText = new NativeLabel("月 :");
        monthFilterText.setWidth(30,Unit.PIXELS);
        monthFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        monthValueContainer.add(monthFilterText);
        monthValueContainer.setVerticalComponentAlignment(Alignment.CENTER,monthFilterText);

        ComboBox<Integer> startMonthComboBox = new ComboBox<>();
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

        Icon inputDivIcon1 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon1.setSize("10px");
        monthValueContainer.add(inputDivIcon1);
        monthValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon1);

        ComboBox<Integer> toMonthComboBox = new ComboBox<>();
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

        HorizontalLayout dayValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(dayValueContainer);
        NativeLabel dayFilterText = new NativeLabel("日 :");
        dayFilterText.setWidth(30,Unit.PIXELS);
        dayFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        dayValueContainer.add(dayFilterText);
        dayValueContainer.setVerticalComponentAlignment(Alignment.CENTER,dayFilterText);

        ComboBox<Integer> startDayComboBox = new ComboBox<>();
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

        Icon inputDivIcon2 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon2.setSize("10px");
        dayValueContainer.add(inputDivIcon2);
        dayValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon2);

        ComboBox<Integer> toDayComboBox = new ComboBox<>();
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

        HorizontalLayout hourValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(hourValueContainer);
        NativeLabel hourFilterText = new NativeLabel("小时 :");
        hourFilterText.setWidth(30,Unit.PIXELS);
        hourFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        hourValueContainer.add(hourFilterText);
        hourValueContainer.setVerticalComponentAlignment(Alignment.CENTER,hourFilterText);

        ComboBox<Integer> startHourComboBox = new ComboBox<>();
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

        Icon inputDivIcon3 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon3.setSize("10px");
        hourValueContainer.add(inputDivIcon3);
        hourValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon3);

        ComboBox<Integer> toHourComboBox = new ComboBox<>();
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

        HorizontalLayout minuteValueContainer = new HorizontalLayout();
        leftSideSectionContainerScrollLayout.add(minuteValueContainer);
        NativeLabel minuteFilterText = new NativeLabel("分钟 :");
        minuteFilterText.setWidth(30,Unit.PIXELS);
        minuteFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        minuteValueContainer.add(minuteFilterText);
        minuteValueContainer.setVerticalComponentAlignment(Alignment.CENTER,minuteFilterText);

        ComboBox<Integer> startMinuteComboBox = new ComboBox<>();
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

        Icon inputDivIcon4 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon4.setSize("10px");
        minuteValueContainer.add(inputDivIcon4);
        minuteValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon4);

        ComboBox<Integer> toMinuteComboBox = new ComboBox<>();
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

        HorizontalLayout heightSpaceDiv1 = new HorizontalLayout();
        heightSpaceDiv1.setWidth(90,Unit.PERCENTAGE);
        leftSideSectionContainerScrollLayout.add(heightSpaceDiv1);
        heightSpaceDiv1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("padding-bottom","2px");

        Span spaceDivSpan = new Span(" ");
        spaceDivSpan.setHeight(10,Unit.PIXELS);
        leftSideSectionContainerScrollLayout.add(spaceDivSpan);

        Button executeQueryButton = new Button("检索时间尺度实体");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //queryConceptionEntities();
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

        timeScaleEntitiesGrid = new Grid<>();
        timeScaleEntitiesGrid.setWidth(295,Unit.PIXELS);
        timeScaleEntitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER);
        timeScaleEntitiesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        timeScaleEntitiesGrid.addColumn(TimeScaleEntity::getTimeScaleGrade).setHeader("实体时间尺度").setKey("idx_0");
        timeScaleEntitiesGrid.addColumn(TimeScaleEntity::getEntityValue).setHeader("实体值").setKey("idx_1");
        middleContainerLayout.add(timeScaleEntitiesGrid);

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
