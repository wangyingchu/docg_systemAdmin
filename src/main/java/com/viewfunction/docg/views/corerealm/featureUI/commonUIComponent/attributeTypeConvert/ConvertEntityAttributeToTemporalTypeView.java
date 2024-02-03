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
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.TemporalScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
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
    private ComboBox<DateTimeFormatterInfo> dateTimeFormatterSelect;
    private TemporalScaleCalculable.TemporalScaleLevel temporalScaleLevel;
    private ConvertEntityAttributeToTemporalTypeCallback convertEntityAttributeToTemporalTypeCallback;

    public interface ConvertEntityAttributeToTemporalTypeCallback {
        public void onSuccess(EntitiesOperationStatistics entitiesOperationStatistics);
    }

    public void setConvertEntityAttributeToTemporalTypeCallback(ConvertEntityAttributeToTemporalTypeCallback convertEntityAttributeToTemporalTypeCallback) {
        this.convertEntityAttributeToTemporalTypeCallback = convertEntityAttributeToTemporalTypeCallback;
    }

    private class DateTimeFormatterInfo {
        private String dateTimeFormatter;
        private String dateTimeValueExample;

        private DateTimeFormatterInfo(){}

        private DateTimeFormatterInfo(String dateTimeFormatter,String dateTimeValueExample){
            this.setDateTimeFormatter(dateTimeFormatter);
            this.setDateTimeValueExample(dateTimeValueExample);
        }

        public String getDateTimeFormatter() {
            return dateTimeFormatter;
        }

        public void setDateTimeFormatter(String dateTimeFormatter) {
            this.dateTimeFormatter = dateTimeFormatter;
        }

        public String getDateTimeValueExample() {
            return dateTimeValueExample;
        }

        public void setDateTimeValueExample(String dateTimeValueExample) {
            this.dateTimeValueExample = dateTimeValueExample;
        }
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

        ThirdLevelIconTitle noticeTitle = new ThirdLevelIconTitle(VaadinIcon.INFO_CIRCLE_O.create(),"类型无法转换的属性将被删除");
        add(noticeTitle);

        dateTimeFormatterSelect = new ComboBox<>("时间日期定义格式");
        dateTimeFormatterSelect.setAllowCustomValue(true);
        dateTimeFormatterSelect.setItems(
                new DateTimeFormatterInfo("yyyy-MM-dd HH:mm:ss","24小时制 示例: 2015-07-03 14:57:41"),
                new DateTimeFormatterInfo("yyyy-MM-dd hh:mm:ss","12小时制 示例: 2015-07-03 2:57:41"),
                new DateTimeFormatterInfo("yyyy-MM-dd HH:mm:ss SSS","24小时制 示例: 2015-07-03 14:57:41 000"),
                new DateTimeFormatterInfo("yyyy-MM-dd hh:mm:ss SSS","12小时制 示例: 2015-07-03 2:57:41 000"),
                new DateTimeFormatterInfo("yyyy-MM-dd hh:mm:ss a","12小时制 示例: 2015-07-03 2:57:41 AM"),
                new DateTimeFormatterInfo("yyyy-MM-dd hh:mm:ss SSS a","12小时制 示例: 2015-07-03 2:57:41 000 AM"),

                new DateTimeFormatterInfo("yyyy-MM-ddTHH:mm:ssZ","24小时制 示例: 2014-11-11T12:00:00Z"),
                new DateTimeFormatterInfo("yyyy-MM-dd'T'HH:mm:ss.SSSZ","24小时制 示例: 2018-05-14T03:51:50.153Z"),
            


                //ISO 8601
                //RFC 3339
                //ANSI
                //ISO 8601 简化


                new DateTimeFormatterInfo("yyyy/MM/dd hh:mm:ss","-"),


                new DateTimeFormatterInfo("MM/dd/yyyy HH:mm:ss a","-"),
                new DateTimeFormatterInfo("MM/dd/yyyy hh:mm:ss a","-"),
                new DateTimeFormatterInfo("yyyy/m/d h:mm:ss","-"),
                new DateTimeFormatterInfo("yyyy/m/dd h:mm:ss","-"),


                new DateTimeFormatterInfo("yyyyMMdd","-"),
                new DateTimeFormatterInfo("yyyy-MM-dd","-"),
                new DateTimeFormatterInfo("yyyymmdd","-"),
                new DateTimeFormatterInfo("yyyy/mm/dd","-"),
                new DateTimeFormatterInfo("yyyy/m/d","-")
        );
        dateTimeFormatterSelect.setRenderer(createRenderer());
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
        DateTimeFormatterInfo dateTimeFormatterInfo = dateTimeFormatterSelect.getValue();
        if(dateTimeFormatterInfo == null){
            CommonUIOperationUtil.showPopupNotification("请确定时间日期定义格式", NotificationVariant.LUMO_ERROR,10000, Notification.Position.MIDDLE);
        }else{
            String temporalFormat = dateTimeFormatterSelect.getValue().getDateTimeFormatter();
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

    private Renderer<DateTimeFormatterInfo> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-l); color: var(--lumo-primary-text-color);\">${item.dateTimeFormatter}</span>");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-m); color: var(--lumo-secondary-text-color);\">${item.dateTimeValueExample}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<DateTimeFormatterInfo>of(tpl.toString())
                .withProperty("dateTimeFormatter", DateTimeFormatterInfo::getDateTimeFormatter)
                .withProperty("dateTimeValueExample", DateTimeFormatterInfo::getDateTimeValueExample);
    }
}
