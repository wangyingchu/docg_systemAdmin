package com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialRegion;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.GeospatialRegionCreatedEvent;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.List;

public class CreateGeospatialRegionView extends VerticalLayout {

    private TextField geospatialRegionNameField;
    private H6 errorMessage;
    private Dialog containerDialog;
    private Button confirmButton;
    private boolean createDefaultGeospatialRegionMode = false;

    public CreateGeospatialRegionView() {
        this.setWidthFull();

        HorizontalLayout messageContainerLayout = new HorizontalLayout();
        add(messageContainerLayout);

        H6 viewTitle = new H6("地理空间区域信息");
        messageContainerLayout.add(viewTitle);

        errorMessage = new H6("-");
        errorMessage.getStyle().set("color","#CE0000");
        messageContainerLayout.add(errorMessage);
        errorMessage.setVisible(false);

        this.geospatialRegionNameField = new TextField("地理空间区域名称 - Geospatial Region Name");
        this.geospatialRegionNameField.setWidthFull();
        this.geospatialRegionNameField.setRequired(true);
        this.geospatialRegionNameField.setRequiredIndicatorVisible(true);
        this.geospatialRegionNameField.setTitle("请输入地理空间区域名称");
        add(geospatialRegionNameField);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        confirmButton = new Button("确认创建地理空间区域",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doCreateGeospatialRegion();
            }
        });
    }

    private void showErrorMessage(String errorMessageTxt){
        this.errorMessage.setText(errorMessageTxt);
        this.errorMessage.setVisible(true);
    }

    private void hideErrorMessage(){
        this.errorMessage.setVisible(false);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void doCreateGeospatialRegion(){
        if(createDefaultGeospatialRegionMode){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            GeospatialRegion defaultGeospatialRegion = coreRealm.getOrCreateGeospatialRegion();
            if(defaultGeospatialRegion != null){
                GeospatialRegionCreatedEvent geospatialRegionCreatedEvent = new GeospatialRegionCreatedEvent();
                geospatialRegionCreatedEvent.setGeospatialRegionName(defaultGeospatialRegion.getGeospatialRegionName());
                ResourceHolder.getApplicationBlackboard().fire(geospatialRegionCreatedEvent);
                if(this.containerDialog != null){
                    this.containerDialog.close();
                }
                CommonUIOperationUtil.showPopupNotification("默认地理空间区域创建成功", NotificationVariant.LUMO_SUCCESS);
            }else{
                CommonUIOperationUtil.showPopupNotification("默认地理空间区域创建失败", NotificationVariant.LUMO_ERROR);
            }
        }else{


        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<GeospatialRegion> existingGeospatialRegionList = coreRealm.getGeospatialRegions();
        if(existingGeospatialRegionList.size() == 0){
            this.geospatialRegionNameField.setEnabled(false);
            this.confirmButton.setText("确认创建默认地理空间区域");
            this.createDefaultGeospatialRegionMode = true;
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        super.onDetach(detachEvent);
    }
}
