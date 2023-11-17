package com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;

public class ExpendTimeFlowYearsView extends VerticalLayout {

    private String timeFlowName;
    private Dialog containerDialog;
    private NativeLabel startYearValue;
    private NativeLabel toYearValue;
    private IntegerField startYearTextField;
    private IntegerField endYearTextField;
    private Checkbox createMinuteScaleEntity;
    private int currentStartYear;
    private int currentEndYear;

    public ExpendTimeFlowYearsView(String timeFlowName){
        this.timeFlowName = timeFlowName;

        Icon timeFlowIcon = VaadinIcon.CLOCK.create();
        timeFlowIcon.setSize("12px");
        timeFlowIcon.getStyle().set("padding-right","3px");

        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(timeFlowIcon,timeFlowName));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout infoDivLayout = new HorizontalLayout();
        infoDivLayout.setWidthFull();
        infoDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(infoDivLayout);

        HorizontalLayout timeHorizontalLayout = new HorizontalLayout();
        timeHorizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        NativeLabel yearSpanText = new NativeLabel("时间跨度:");
        yearSpanText.addClassNames("text-xs","font-semibold","text-secondary");
        timeHorizontalLayout.add(yearSpanText);

        startYearTextField = new IntegerField();
        startYearTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        startYearTextField.setWidth(85,Unit.PIXELS);
        startYearTextField.setPlaceholder("起始年度时间");
        startYearTextField.setStep(1);
        timeHorizontalLayout.add(startYearTextField);

        Icon yearDivIcon0 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        yearDivIcon0.setSize("12px");
        timeHorizontalLayout.add(yearDivIcon0);
        timeHorizontalLayout.setVerticalComponentAlignment(Alignment.CENTER,yearDivIcon0);

        Span fromYear = new Span();
        startYearValue = new NativeLabel("");
        startYearValue.addClassNames("text-l","font-extrabold");
        fromYear.add(startYearValue);
        fromYear.getElement().getThemeList().add("badge pill");
        fromYear.addClassNames("text-xl","text-primary","font-extrabold");
        fromYear.getStyle().set("color","#2e4e7e");

        Icon yearDivIcon1 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        yearDivIcon1.setSize("12px");

        Span toYear = new Span();
        toYearValue = new NativeLabel("");
        toYearValue.addClassNames("text-l","font-extrabold");
        toYear.add(toYearValue);
        toYear.getElement().getThemeList().add("badge pill");
        toYear.addClassNames("text-xl","font-bold");
        toYear.getStyle().set("color","#2e4e7e");

        timeHorizontalLayout.add(fromYear);
        timeHorizontalLayout.add(yearDivIcon1);
        timeHorizontalLayout.add(toYear);
        timeHorizontalLayout.setVerticalComponentAlignment(Alignment.CENTER,yearDivIcon1);
        add(timeHorizontalLayout);

        Icon yearDivIcon2 = VaadinIcon.ARROWS_LONG_RIGHT.create();
        yearDivIcon2.setSize("12px");
        timeHorizontalLayout.add(yearDivIcon2);
        timeHorizontalLayout.setVerticalComponentAlignment(Alignment.CENTER,yearDivIcon2);

        endYearTextField = new IntegerField();
        endYearTextField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        endYearTextField.setWidth(85,Unit.PIXELS);
        endYearTextField.setPlaceholder("结束年度时间");
        endYearTextField.setStep(1);
        timeHorizontalLayout.add(endYearTextField);

        createMinuteScaleEntity = new Checkbox("创建分钟粒度实体");
        createMinuteScaleEntity.addClassNames("text-xs");
        createMinuteScaleEntity.getStyle().set("color","#2e4e7e");
        createMinuteScaleEntity.setValue(true);
        timeHorizontalLayout.add(createMinuteScaleEntity);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认扩展时间流年跨度",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doExpandTimeFlow();
            }
        });

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        TimeFlow targetTimeFlow = coreRealm.getOrCreateTimeFlow(this.timeFlowName);
        List<Integer> availableTimeSpanYearsList = targetTimeFlow.getAvailableTimeSpanYears();
        if(availableTimeSpanYearsList != null && availableTimeSpanYearsList.size() >0){
            if(availableTimeSpanYearsList.size() ==1){
                Integer onlyYear = availableTimeSpanYearsList.get(0);
                currentStartYear = onlyYear;
                currentEndYear = onlyYear;
                startYearValue.setText(""+onlyYear);
                toYearValue.setText(""+onlyYear);
                startYearTextField.setMin(0);
                startYearTextField.setMax(onlyYear-1);
                endYearTextField.setMin(onlyYear+1);
            }else{
                Integer firstYear = availableTimeSpanYearsList.get(0);
                Integer lastYear = availableTimeSpanYearsList.get(availableTimeSpanYearsList.size() -1);
                currentStartYear = firstYear;
                currentEndYear = lastYear;
                startYearValue.setText(""+firstYear);
                toYearValue.setText(""+lastYear);
                startYearTextField.setMin(0);
                startYearTextField.setMax(firstYear-1);
                endYearTextField.setMin(lastYear+1);
            }
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void doExpandTimeFlow(){
        if(startYearTextField.isInvalid()){
            CommonUIOperationUtil.showPopupNotification("请输入合法的起始年度时间", NotificationVariant.LUMO_ERROR,0, Notification.Position.MIDDLE);
            return;
        }
        if(endYearTextField.isInvalid()){
            CommonUIOperationUtil.showPopupNotification("请输入合法的结束年度时间", NotificationVariant.LUMO_ERROR,0, Notification.Position.MIDDLE);
            return;
        }
        if(startYearTextField.getValue() == null && endYearTextField.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("起始年度时间与结束年度时间必须输入至少一项", NotificationVariant.LUMO_ERROR,0, Notification.Position.MIDDLE);
            return;
        }
        boolean createMinuteEntities = createMinuteScaleEntity.getValue();
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            TimeFlow targetTimeFlow = coreRealm.getOrCreateTimeFlow(this.timeFlowName);
            boolean createResult = true;
            if(startYearTextField.getValue() != null){
                if(startYearTextField.getValue() == currentStartYear -1){
                    createResult = targetTimeFlow.createTimeSpanEntities(startYearTextField.getValue(),createMinuteEntities);
                }else{
                    createResult = targetTimeFlow.createTimeSpanEntities(startYearTextField.getValue(),currentStartYear -1,createMinuteEntities);
                }
            }
            if(endYearTextField.getValue() != null){
                if(endYearTextField.getValue() == currentEndYear +1){
                    createResult = createResult & targetTimeFlow.createTimeSpanEntities(endYearTextField.getValue(),createMinuteEntities);
                }else{
                    createResult = createResult & targetTimeFlow.createTimeSpanEntities(currentEndYear +1,endYearTextField.getValue(),createMinuteEntities);
                }
            }
            if(createResult){
                if(this.containerDialog != null){
                    this.containerDialog.close();
                }
                CommonUIOperationUtil.showPopupNotification("扩展时间流年跨度操作成功", NotificationVariant.LUMO_SUCCESS);
            }else{
                CommonUIOperationUtil.showPopupNotification("扩展时间流年跨度操作错误", NotificationVariant.LUMO_SUCCESS);
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
