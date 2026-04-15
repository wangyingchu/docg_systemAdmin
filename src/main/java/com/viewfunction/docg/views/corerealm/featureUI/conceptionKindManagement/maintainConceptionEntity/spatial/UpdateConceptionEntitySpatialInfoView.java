package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitySpatialInfoUpdatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleFeatureSupportable.WKTGeometryType;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;


public class UpdateConceptionEntitySpatialInfoView extends VerticalLayout {

    private ConceptionEntity conceptionEntity;
    private GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel;
    private boolean isInited = false;
    private TextField  _CRSAIDField;
    private ComboBox<WKTGeometryType> _WKTGeometryTypeComboBox;
    private TextArea _WKTGeometryTextArea;

    public UpdateConceptionEntitySpatialInfoView(){}

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(!isInited){
            Icon conceptionKindIcon = VaadinIcon.CUBE.create();
            conceptionKindIcon.setSize("12px");
            conceptionKindIcon.getStyle().set("padding-right","3px");
            Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
            conceptionEntityIcon.setSize("18px");
            conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
            List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon, this.getConceptionEntity().getConceptionKindName()));
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon, this.getConceptionEntity().getConceptionEntityUID()));
            FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
            add(entityInfoFootprintMessageBar);
            HorizontalLayout spaceDivLayout = new HorizontalLayout();
            spaceDivLayout.setWidthFull();
            spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
            add(spaceDivLayout);

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
            spaceDivLayout1.setWidth(10,Unit.PIXELS);
            horizontalLayout.add(spaceDivLayout1);
            horizontalLayout.setSpacing(false);
            horizontalLayout.setMargin(false);
            Icon crsaidIcon = VaadinIcon.CROSSHAIRS.create();
            crsaidIcon.setSize("8px");
            horizontalLayout.add(crsaidIcon);
            crsaidIcon.getStyle().set("top","3px").set("position","relative");
            HorizontalLayout spaceDivHorizontalLayout = new HorizontalLayout();
            spaceDivHorizontalLayout.setWidth(3, Unit.PIXELS);
            horizontalLayout.add(spaceDivHorizontalLayout);
            add(horizontalLayout);

            _CRSAIDField = new TextField();
            _CRSAIDField.setWidth(160, Unit.PIXELS);
            _CRSAIDField.addThemeVariants(TextFieldVariant.SMALL,TextFieldVariant.LUMO_SMALL);
            _CRSAIDField.getStyle().set("top","-6px").set("position","relative");
            _CRSAIDField.setPlaceholder("CRSA ID");

            _WKTGeometryTypeComboBox = new ComboBox<>();
            _WKTGeometryTypeComboBox.setRequiredIndicatorVisible(true);
            _WKTGeometryTypeComboBox.setRequired(true);
            _WKTGeometryTypeComboBox.setPlaceholder("WKT Geometry Type");
            _WKTGeometryTypeComboBox.setWidth(190, Unit.PIXELS);
            _WKTGeometryTypeComboBox.setItems(
                    WKTGeometryType.POINT,
                    WKTGeometryType.LINESTRING,
                    WKTGeometryType.POLYGON,
                    WKTGeometryType.MULTIPOINT,
                    WKTGeometryType.MULTILINESTRING,
                    WKTGeometryType.MULTIPOLYGON,
                    WKTGeometryType.GEOMETRYCOLLECTION);
            _WKTGeometryTypeComboBox.addThemeVariants(ComboBoxVariant.SMALL,ComboBoxVariant.LUMO_SMALL);
            _WKTGeometryTypeComboBox.getStyle().set("top","-6px").set("position","relative");

            switch (this.spatialScaleLevel){
                case GeospatialScaleCalculable.SpatialScaleLevel.Global:
                    HorizontalLayout globalInfoSpan =new HorizontalLayout();
                    Icon globalInfoIcon = new Icon(VaadinIcon.GLOBE_WIRE);
                    globalInfoIcon.setSize("14px");
                    NativeLabel globalInfoLabel = new NativeLabel(" 全球坐标系地理空间信息");
                    globalInfoSpan.add(globalInfoIcon,globalInfoLabel,horizontalLayout,_CRSAIDField,_WKTGeometryTypeComboBox);
                    add(globalInfoSpan);
                    _CRSAIDField.setValue("EPSG:4326 - (WGS 84)");
                    _CRSAIDField.setReadOnly(true);
                    break;
                case GeospatialScaleCalculable.SpatialScaleLevel.Country:
                    HorizontalLayout countryInfoSpan =new HorizontalLayout();
                    Icon countryInfoIcon = new Icon(VaadinIcon.LOCATION_ARROW_CIRCLE);
                    countryInfoIcon.setSize("14px");
                    NativeLabel countryInfoLabel = new NativeLabel(" 国家坐标系地理空间信息");
                    countryInfoSpan.add(countryInfoIcon,countryInfoLabel,horizontalLayout,_CRSAIDField,_WKTGeometryTypeComboBox);
                    add(countryInfoSpan);
                    _CRSAIDField.setValue("EPSG:4490 - (CGCS2000)");
                    _CRSAIDField.setReadOnly(true);
                    break;
                case GeospatialScaleCalculable.SpatialScaleLevel.Local:
                    HorizontalLayout localInfoSpan =new HorizontalLayout();
                    Icon localInfoIcon = new Icon(VaadinIcon.HOME);
                    localInfoIcon.setSize("14px");
                    NativeLabel localInfoLabel = new NativeLabel(" 本地坐标系地理空间信息");
                    localInfoSpan.add(localInfoIcon,localInfoLabel,horizontalLayout,_CRSAIDField,_WKTGeometryTypeComboBox);
                    add(localInfoSpan);
                    break;
            }

            _WKTGeometryTextArea = new TextArea("WKT Geometry Text");
            _WKTGeometryTextArea.setRequiredIndicatorVisible(true);
            _WKTGeometryTextArea.setRequired(true);
            _WKTGeometryTextArea.setHeight(400,Unit.PIXELS);
            _WKTGeometryTextArea.setWidth(565,Unit.PIXELS);
            add(_WKTGeometryTextArea);

            Button confirmButton = new Button("确认添加地理空间信息",new Icon(VaadinIcon.CHECK_CIRCLE));
            confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            add(confirmButton);
            setHorizontalComponentAlignment(Alignment.END,confirmButton);
            confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    updateEntitySpatialInfo();
                }
            });
            add(confirmButton);

            isInited = true;
        }
    }

    private void updateEntitySpatialInfo(){
        String _CRSAID = null;
        switch (this.spatialScaleLevel) {
            case GeospatialScaleCalculable.SpatialScaleLevel.Global -> _CRSAID = "EPSG:4326";
            case GeospatialScaleCalculable.SpatialScaleLevel.Country -> _CRSAID = "EPSG:4490";
            case GeospatialScaleCalculable.SpatialScaleLevel.Local -> _CRSAID = _CRSAIDField.getValue();
        }
        if(_CRSAID.equals("")){
            CommonUIOperationUtil.showPopupNotification("CRSA ID 不能为空", NotificationVariant.LUMO_ERROR,0, Notification.Position.MIDDLE);
            return;
        }
        if(_WKTGeometryTypeComboBox.getValue() == null){
            CommonUIOperationUtil.showPopupNotification("WKT Geometry Type 必须选择", NotificationVariant.LUMO_ERROR,0, Notification.Position.MIDDLE);
            return;
        }
        if(_WKTGeometryTextArea.getValue().equals("")){
            CommonUIOperationUtil.showPopupNotification("WKT Geometry Text 不能为空", NotificationVariant.LUMO_ERROR,0, Notification.Position.MIDDLE);
            return;
        }

        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认更新概念实体地理空间信息",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","本操作将更新概念实体 "+this.conceptionEntity.getConceptionKindName()+" - "+ this.conceptionEntity.getConceptionEntityUID()+" 的地理空间信息 ,请确认执行操作",actionButtonList,650,190);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doUpdateEntitySpatialInfo();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doUpdateEntitySpatialInfo(){
        String _CRSAID = null;
        switch (this.spatialScaleLevel) {
            case GeospatialScaleCalculable.SpatialScaleLevel.Global -> _CRSAID = "EPSG:4326";
            case GeospatialScaleCalculable.SpatialScaleLevel.Country -> _CRSAID = "EPSG:4490";
            case GeospatialScaleCalculable.SpatialScaleLevel.Local -> _CRSAID = _CRSAIDField.getValue();
        }
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionEntity.getConceptionKindName());
        if(targetConceptionKind != null){
            ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(conceptionEntity.getConceptionEntityUID());
            if(targetConceptionEntity != null){
                targetConceptionEntity.addOrUpdateGeometryType(_WKTGeometryTypeComboBox.getValue());
                switch (this.spatialScaleLevel) {
                    case Global :
                        targetConceptionEntity.addOrUpdateGlobalCRSAID(_CRSAID);
                        targetConceptionEntity.addOrUpdateGLGeometryContent(_WKTGeometryTextArea.getValue());
                        break;
                    case Country:
                        targetConceptionEntity.addOrUpdateCountryCRSAID(_CRSAID);
                        targetConceptionEntity.addOrUpdateCLGeometryContent(_WKTGeometryTextArea.getValue());
                        break;
                    case Local:
                        targetConceptionEntity.addOrUpdateLocalCRSAID(_CRSAID);
                        targetConceptionEntity.addOrUpdateLLGeometryContent(_WKTGeometryTextArea.getValue());
                        break;
                }

                ConceptionEntitySpatialInfoUpdatedEvent conceptionEntitySpatialInfoUpdatedEvent = new ConceptionEntitySpatialInfoUpdatedEvent();
                conceptionEntitySpatialInfoUpdatedEvent.setConceptionEntityUID(this.conceptionEntity.getConceptionEntityUID());
                conceptionEntitySpatialInfoUpdatedEvent.setConceptionKindName(this.conceptionEntity.getConceptionKindName());
                conceptionEntitySpatialInfoUpdatedEvent.setSpatialScaleLevel(this.spatialScaleLevel);
                ResourceHolder.getApplicationBlackboard().fire(conceptionEntitySpatialInfoUpdatedEvent);

                if(containerDialog != null){
                    containerDialog.close();
                }
                CommonUIOperationUtil.showPopupNotification("地理空间信息更新成功", NotificationVariant.LUMO_SUCCESS,5000, Notification.Position.BOTTOM_START);
            }else{
                CommonUIOperationUtil.showPopupNotification("概念实体 "+conceptionEntity.getConceptionEntityUID()+" 不存在", NotificationVariant.LUMO_ERROR,0, Notification.Position.MIDDLE);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionEntity.getConceptionKindName()+" 不存在", NotificationVariant.LUMO_ERROR,0, Notification.Position.MIDDLE);
        }
    }

    private Dialog containerDialog;

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public ConceptionEntity getConceptionEntity() {
        return conceptionEntity;
    }

    public void setConceptionEntity(ConceptionEntity conceptionEntity) {
        this.conceptionEntity = conceptionEntity;
    }

    public GeospatialScaleCalculable.SpatialScaleLevel getSpatialScaleLevel() {
        return spatialScaleLevel;
    }

    public void setSpatialScaleLevel(GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel) {
        this.spatialScaleLevel = spatialScaleLevel;
    }
}
