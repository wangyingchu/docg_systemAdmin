package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;

@Route("timeFlowDetailInfo/:timeFlow")
public class TimeFlowDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String timeFlowName;
    private VerticalLayout leftSideContainerLayout;
    private Registration listener;

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
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            leftSideContainerLayout.setHeight(browserHeight-265,Unit.PIXELS);
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
        leftSideContainerLayout.setWidth(290, Unit.PIXELS);
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
        leftSideContainerLayout.add(timeFlowInformationLayout);

        HorizontalLayout timeHorizontalLayout = new HorizontalLayout();
        timeHorizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

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

        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label3 = new NativeLabel("Year Entities:");
        label3.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label3);
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout3);

        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label4 = new NativeLabel("Month Entities:");
        label4.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label4);
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout4);

        HorizontalLayout horizontalLayout5 = new HorizontalLayout();
        horizontalLayout5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label5 = new NativeLabel("Day Entities:");
        label5.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label5);
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout5);

        HorizontalLayout horizontalLayout6 = new HorizontalLayout();
        horizontalLayout6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label6 = new NativeLabel("Hour Entities:");
        label6.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label6);
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout6);

        HorizontalLayout horizontalLayout7 = new HorizontalLayout();
        horizontalLayout7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        NativeLabel label7 = new NativeLabel("Minute Entities:");
        label7.getElement().getThemeList().add("badge success small");
        timeFlowInformationLayout.add(label7);
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.CLOCK.create(),"","1,000,000,000");
        new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.BEZIER_CURVE.create(),"","1,000,000,000");
        timeFlowInformationLayout.add(horizontalLayout7);

        HorizontalLayout heightSpaceDiv05 = new HorizontalLayout();
        timeFlowInformationLayout.add(heightSpaceDiv05);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"时间尺度实体检索");
        leftSideContainerLayout.add(filterTitle2);

        HorizontalLayout heightSpaceDiv06 = new HorizontalLayout();
        heightSpaceDiv06.setWidth(95,Unit.PERCENTAGE);
        leftSideContainerLayout.add(heightSpaceDiv06);
        heightSpaceDiv06.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");

        HorizontalLayout checkBoxesContainer1 = new HorizontalLayout();
        checkBoxesContainer1.getStyle().set("padding-top", "var(--lumo-space-m)");
        leftSideContainerLayout.add(checkBoxesContainer1);

        NativeLabel timeGuLevelFilterText = new NativeLabel("时间粒度 :");
        timeGuLevelFilterText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(timeGuLevelFilterText);

        Checkbox yearCheckbox = new Checkbox("年");
        yearCheckbox.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(yearCheckbox);
        Checkbox monthCheckbox = new Checkbox("月");
        monthCheckbox.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(monthCheckbox);
        Checkbox dayCheckbox = new Checkbox("日");
        dayCheckbox.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(dayCheckbox);
        Checkbox hourCheckbox = new Checkbox("小时");
        hourCheckbox.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(hourCheckbox);
        Checkbox minuteCheckbox = new Checkbox("分钟");
        minuteCheckbox.getStyle().set("font-size","0.6rem").set("color","var(--lumo-contrast-80pct)");
        checkBoxesContainer1.add(minuteCheckbox);

        HorizontalLayout yearValueContainer = new HorizontalLayout();
        leftSideContainerLayout.add(yearValueContainer);
        NativeLabel yearFilterText = new NativeLabel("年 :");
        yearFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        yearValueContainer.add(yearFilterText);
        yearValueContainer.setVerticalComponentAlignment(Alignment.CENTER,yearFilterText);

        TextField startYearTextField = new TextField();
        startYearTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        startYearTextField.setWidth(50,Unit.PIXELS);
        yearValueContainer.add(startYearTextField);

        Icon inputDivIcon0 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon0.setSize("10px");
        yearValueContainer.add(inputDivIcon0);
        yearValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon0);

        TextField toYearTextField = new TextField();
        toYearTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        toYearTextField.setWidth(50,Unit.PIXELS);
        yearValueContainer.add(toYearTextField);

        HorizontalLayout monthValueContainer = new HorizontalLayout();
        leftSideContainerLayout.add(monthValueContainer);
        NativeLabel monthFilterText = new NativeLabel("月 :");
        monthFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        monthValueContainer.add(monthFilterText);
        monthValueContainer.setVerticalComponentAlignment(Alignment.CENTER,monthFilterText);

        ComboBox<Integer> startMonthComboBox = new ComboBox<>();
        startMonthComboBox.setItems(1,2,3,4,5,6,7,8,9,10,11,12);
        startMonthComboBox.setWidth(50,Unit.PIXELS);
        startMonthComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        startMonthComboBox.setAllowCustomValue(false);
        monthValueContainer.add(startMonthComboBox);

        Icon inputDivIcon1 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon1.setSize("10px");
        monthValueContainer.add(inputDivIcon1);
        monthValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon1);

        ComboBox<Integer> toMonthComboBox = new ComboBox<>();
        toMonthComboBox.setItems(1,2,3,4,5,6,7,8,9,10,11,12);
        toMonthComboBox.setWidth(50,Unit.PIXELS);
        toMonthComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        toMonthComboBox.setAllowCustomValue(false);
        monthValueContainer.add(toMonthComboBox);

        HorizontalLayout dayValueContainer = new HorizontalLayout();
        leftSideContainerLayout.add(dayValueContainer);
        NativeLabel dayFilterText = new NativeLabel("日 :");
        dayFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        dayValueContainer.add(dayFilterText);
        dayValueContainer.setVerticalComponentAlignment(Alignment.CENTER,dayFilterText);

        ComboBox<Integer> startDayComboBox = new ComboBox<>();
        startDayComboBox.setItems(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31);
        startDayComboBox.setWidth(50,Unit.PIXELS);
        startDayComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        startDayComboBox.setAllowCustomValue(false);
        dayValueContainer.add(startDayComboBox);

        Icon inputDivIcon2 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon2.setSize("10px");
        dayValueContainer.add(inputDivIcon2);
        dayValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon2);

        ComboBox<Integer> toDayComboBox = new ComboBox<>();
        toDayComboBox.setItems(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31);
        toDayComboBox.setWidth(50,Unit.PIXELS);
        toDayComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        toDayComboBox.setAllowCustomValue(false);
        dayValueContainer.add(toDayComboBox);

        HorizontalLayout hourValueContainer = new HorizontalLayout();
        leftSideContainerLayout.add(hourValueContainer);
        NativeLabel hourFilterText = new NativeLabel("小时 :");
        hourFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        hourValueContainer.add(hourFilterText);
        hourValueContainer.setVerticalComponentAlignment(Alignment.CENTER,hourFilterText);

        ComboBox<Integer> startHourComboBox = new ComboBox<>();
        startHourComboBox.setItems(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23);
        startHourComboBox.setWidth(50,Unit.PIXELS);
        startHourComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        startHourComboBox.setAllowCustomValue(false);
        hourValueContainer.add(startHourComboBox);

        Icon inputDivIcon3 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon3.setSize("10px");
        hourValueContainer.add(inputDivIcon3);
        hourValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon3);

        ComboBox<Integer> toHourComboBox = new ComboBox<>();
        toHourComboBox.setItems(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23);
        toHourComboBox.setWidth(50,Unit.PIXELS);
        toHourComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        toHourComboBox.setAllowCustomValue(false);
        hourValueContainer.add(toHourComboBox);

        HorizontalLayout minuteValueContainer = new HorizontalLayout();
        leftSideContainerLayout.add(minuteValueContainer);
        NativeLabel minuteFilterText = new NativeLabel("分钟 :");
        minuteFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        minuteValueContainer.add(minuteFilterText);
        minuteValueContainer.setVerticalComponentAlignment(Alignment.CENTER,minuteFilterText);

        ComboBox<Integer> startMinuteComboBox = new ComboBox<>();
        startMinuteComboBox.setItems(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59);
        startMinuteComboBox.setWidth(50,Unit.PIXELS);
        startMinuteComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        startMinuteComboBox.setAllowCustomValue(false);
        minuteValueContainer.add(startMinuteComboBox);

        Icon inputDivIcon4 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        inputDivIcon4.setSize("10px");
        minuteValueContainer.add(inputDivIcon4);
        minuteValueContainer.setVerticalComponentAlignment(Alignment.CENTER,inputDivIcon4);

        ComboBox<Integer> toMinuteComboBox = new ComboBox<>();
        toMinuteComboBox.setItems(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59);
        toMinuteComboBox.setWidth(50,Unit.PIXELS);
        toMinuteComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        toMinuteComboBox.setAllowCustomValue(false);
        minuteValueContainer.add(toMinuteComboBox);

        HorizontalLayout heightSpaceDiv1 = new HorizontalLayout();
        heightSpaceDiv1.setWidth(90,Unit.PERCENTAGE);
        leftSideContainerLayout.add(heightSpaceDiv1);
        heightSpaceDiv1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("padding-bottom","2px");

        Span spaceDivSpan = new Span(" ");
        spaceDivSpan.setHeight(10,Unit.PIXELS);
        leftSideContainerLayout.add(spaceDivSpan);

        Button executeQueryButton = new Button("查询时间尺度实体");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeQueryButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //queryConceptionEntities();
            }
        });
        leftSideContainerLayout.add(executeQueryButton);
    }
}
