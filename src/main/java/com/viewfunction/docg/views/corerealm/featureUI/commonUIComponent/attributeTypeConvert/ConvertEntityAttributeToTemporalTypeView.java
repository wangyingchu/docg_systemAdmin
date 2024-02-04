package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeTypeConvert;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.TemporalScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConvertEntityAttributeToTemporalTypeView extends VerticalLayout {

    public enum KindType {ConceptionKind,RelationKind}
    private String kindName;
    private String attributeName;
    private KindType kindType;
    private Dialog containerDialog;
    private ComboBox<String> dateTimeFormatterSelect;
    private TemporalScaleCalculable.TemporalScaleLevel temporalScaleLevel;
    private ConvertEntityAttributeToTemporalTypeCallback convertEntityAttributeToTemporalTypeCallback;

    public interface ConvertEntityAttributeToTemporalTypeCallback {
        public void onSuccess(EntitiesOperationStatistics entitiesOperationStatistics);
    }

    public void setConvertEntityAttributeToTemporalTypeCallback(ConvertEntityAttributeToTemporalTypeCallback convertEntityAttributeToTemporalTypeCallback) {
        this.convertEntityAttributeToTemporalTypeCallback = convertEntityAttributeToTemporalTypeCallback;
    }

    public ConvertEntityAttributeToTemporalTypeView(KindType kindType,String kindName,String attributeName,TemporalScaleCalculable.TemporalScaleLevel temporalScaleLevel){
        this.kindName = kindName;
        this.attributeName = attributeName;
        this.kindType = kindType;
        this.temporalScaleLevel = temporalScaleLevel;
        this.setWidthFull();

        Icon kindIcon = VaadinIcon.CUBE.create();
        switch (kindType){
            case ConceptionKind -> kindIcon = VaadinIcon.CUBE.create();
            case RelationKind ->  kindIcon = VaadinIcon.CONNECT_O.create();
        }
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");

        Icon attributeIcon = VaadinIcon.BULLETS.create();
        attributeIcon.setSize("12px");
        attributeIcon.getStyle().set("padding-left","3px").set("padding-right","2px");

        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon,this.kindName));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributeIcon,this.attributeName));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout infoLayout = new HorizontalLayout();
        infoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(infoLayout);

        ThirdLevelIconTitle noticeTitle = new ThirdLevelIconTitle(VaadinIcon.INFO_CIRCLE_O.create(),"类型无法转换的属性将被删除");
        infoLayout.add(noticeTitle);

        Button formatDescIntroButtonLaunchButton = new Button();
        formatDescIntroButtonLaunchButton.setTooltipText("时间日期定义格式说明");
        Icon infoIcon = VaadinIcon.QUESTION_CIRCLE.create();
        infoIcon.setSize("14px");
        formatDescIntroButtonLaunchButton.setIcon(infoIcon);
        formatDescIntroButtonLaunchButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_CONTRAST);
        infoLayout.add(formatDescIntroButtonLaunchButton);
        formatDescIntroButtonLaunchButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                showFormatDescIntroView();
            }
        });

        dateTimeFormatterSelect = new ComboBox<>("时间日期定义格式");
        dateTimeFormatterSelect.setAllowCustomValue(true);

        dateTimeFormatterSelect.setItems(
                "yyyy-MM-dd hh:mm:ss",
                "yyyy-MM-dd hh:mm:ss a",

                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss SSS",

                "yyyy-MM-dd HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:ssZ",
               "yyyy-MM-dd'T'HH:mm:ssXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",

                "yyyy/MM/dd hh:mm:ss",
                "yyyy/MM/dd hh:mm:ss a",
                "yyyy/M/d hh:mm:ss",
                "yyyy/M/d hh:mm:ss a",
                "yyyy/MM/dd HH:mm:ss",
                "yyyy/MM/dd HH:mm:ss SSS",

                "MM/dd/yyyy hh:mm:ss",
                "MM/dd/yyyy hh:mm:ss a",
                "M/d/yyyy hh:mm:ss",
                "M/d/yyyy hh:mm:ss a",
                "MM/dd/yyyy HH:mm:ss",
                "MM/dd/yyyy HH:mm:ss SSS",

                "yyyyMMdd",
                "yyyy-MM-dd",
                "yyyy-M-d",
                "yyyy/MM/dd",
                "yyyy/M/d"
        );

        dateTimeFormatterSelect.setPageSize(30);
        dateTimeFormatterSelect.setPlaceholder("选择或输入时间日期定义格式");
        dateTimeFormatterSelect.setWidthFull();
        add(dateTimeFormatterSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确认转换数据类型",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                convertEntityAttributeToTemporalTypeView();
            }
        });
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void convertEntityAttributeToTemporalTypeView(){
        String temporalFormat = dateTimeFormatterSelect.getValue();
        if(temporalFormat == null || temporalFormat.isEmpty()){
            CommonUIOperationUtil.showPopupNotification("请确定时间日期定义格式", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
        }else{
            DateTimeFormatter dtf = null;
            switch(temporalScaleLevel){
                case Date,Datetime,Time -> dtf = DateTimeFormatter.ofPattern(temporalFormat);
                case Timestamp -> dtf = DateTimeFormatter.ofPattern(temporalFormat).withZone(ZoneId.systemDefault());
            }
            EntitiesOperationStatistics entitiesOperationStatistics = null;
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            switch (kindType){
                case ConceptionKind :
                    com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.kindName);
                    try {
                        entitiesOperationStatistics = targetConceptionKind.convertEntityAttributeToTemporalType(attributeName,dtf, temporalScaleLevel);
                        if(this.containerDialog != null){
                            this.containerDialog.close();
                        }
                        if(this.convertEntityAttributeToTemporalTypeCallback != null){
                            this.convertEntityAttributeToTemporalTypeCallback.onSuccess(entitiesOperationStatistics);
                        }
                    } catch (CoreRealmServiceRuntimeException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case RelationKind :
                    /*
                    com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind targetRelationKind = coreRealm.getRelationKind(this.kindName);
                    try {
                        entitiesOperationStatistics = targetRelationKind.convertEntityAttributeToTemporalType(attributeName,dtf, TemporalScaleCalculable.TemporalScaleLevel.Date);
                        String notificationMessage = "将概念类型 "+this.conceptionKind+" 的实体属性 "+attributeName+" 转换为 DATE 类型操作成功";
                        showPopupNotification(notificationMessage,entitiesOperationStatistics,NotificationVariant.LUMO_SUCCESS);
                        refreshConceptionKindAttributesInfoGrid();
                    } catch (CoreRealmServiceRuntimeException e) {
                        throw new RuntimeException(e);
                    }
                    */
                    ;
            }
        }
    }

    private void showFormatDescIntroView(){
        DateTimeFormatDescIntroView dateTimeFormatDescIntroView = new DateTimeFormatDescIntroView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.QUESTION_CIRCLE),"时间日期定义格式说明",null,true,700,490,false);
        fixSizeWindow.setWindowContent(dateTimeFormatDescIntroView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
