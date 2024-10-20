package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConceptionEntityMetaInfoView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private DateTimePicker createDate;
    private DateTimePicker lastUpdateDate;
    private TextField dataSource;
    private TextField creator;
    private TextField entityBelongedConceptionKinds;
    private Button retreatFromConceptionKindButton;
    private Button displayEntityDetailButton;

    public ConceptionEntityMetaInfoView(String conceptionKind,String conceptionEntityUID){
        this.setMargin(false);
        this.setSpacing(false);
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
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

        Icon eyeIcon = new Icon(VaadinIcon.EYE);
        eyeIcon.setSize("20px");
        displayEntityDetailButton = new Button(eyeIcon, event -> {
            renderConceptionEntityUI(conceptionKind,conceptionEntityUID);
        });
        displayEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        displayEntityDetailButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        displayEntityDetailButton.setTooltipText("显示概念实体详情");
        entityInfoFootprintMessageBar.addAdditionalComponent(displayEntityDetailButton);
        displayEntityDetailButton.setVisible(false);

        HorizontalLayout actionContainerLayout = new HorizontalLayout();
        actionContainerLayout.setSpacing(false);
        actionContainerLayout.setMargin(false);
        actionContainerLayout.setPadding(false);

        Button joinConceptionKindButton = new Button();
        joinConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_TERTIARY);
        joinConceptionKindButton.setTooltipText("加入新的概念类型");
        joinConceptionKindButton.setIcon(VaadinIcon.PLUS.create());
        joinConceptionKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderJoinConceptionKindUI();
            }
        });

        retreatFromConceptionKindButton = new Button();
        retreatFromConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
        retreatFromConceptionKindButton.setTooltipText("退出所属概念类型");
        retreatFromConceptionKindButton.setIcon(VaadinIcon.MINUS.create());
        retreatFromConceptionKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRetreatFromConceptionKindUI();
            }
        });

        actionContainerLayout.add(joinConceptionKindButton, retreatFromConceptionKindButton);

        entityBelongedConceptionKinds = new TextField("实体所属概念类型");
        entityBelongedConceptionKinds.setPrefixComponent(actionContainerLayout);
        entityBelongedConceptionKinds.setWidthFull();
        entityBelongedConceptionKinds.setReadOnly(true);
        add(entityBelongedConceptionKinds);

        createDate = new DateTimePicker("创建时间");
        createDate.setReadOnly(true);
        lastUpdateDate = new DateTimePicker("最后更新时间");
        lastUpdateDate.setReadOnly(true);
        dataSource = new TextField("数据原始来源");
        dataSource.setReadOnly(true);
        creator = new TextField("创建人ID");
        creator.setReadOnly(true);

        FormLayout formLayout = new FormLayout();
        formLayout.add(createDate, lastUpdateDate,dataSource,creator);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2)
        );
        add(formLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        refreshEntityMetaInfo();
    }

    private void renderJoinConceptionKindUI(){
        JoinNewConceptionKindsView joinNewConceptionKindsView = new JoinNewConceptionKindsView(this.conceptionKind,this.conceptionEntityUID);
        joinNewConceptionKindsView.setCallerConceptionEntityMetaInfoView(this);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"加入概念类型",null,true,490,200,false);
        fixSizeWindow.setWindowContent(joinNewConceptionKindsView);
        fixSizeWindow.setModel(true);
        joinNewConceptionKindsView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderRetreatFromConceptionKindUI(){
        RetreatFromConceptionKindsView retreatFromConceptionKindsView = new RetreatFromConceptionKindsView(this.conceptionKind,this.conceptionEntityUID);
        retreatFromConceptionKindsView.setCallerConceptionEntityMetaInfoView(this);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.MINUS),"退出概念类型",null,true,490,200,false);
        fixSizeWindow.setWindowContent(retreatFromConceptionKindsView);
        fixSizeWindow.setModel(true);
        retreatFromConceptionKindsView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    public void refreshEntityMetaInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            if(targetEntity != null){
                List<String> belongedKinds = targetEntity.getAllConceptionKindNames();
                entityBelongedConceptionKinds.setValue(belongedKinds.toString());
                int canRetreatCount = 0;
                boolean isInternalKind = false;
                for(String belongedKind : belongedKinds){
                    if(!belongedKind.startsWith("DOCG_")){
                        canRetreatCount++;
                    }else{
                        isInternalKind = true;
                    }
                }
                if(isInternalKind && canRetreatCount == 1){
                    retreatFromConceptionKindButton.setEnabled(true);
                }else if(canRetreatCount > 1){
                    retreatFromConceptionKindButton.setEnabled(true);
                }else{
                    retreatFromConceptionKindButton.setEnabled(false);
                }
                ZoneId zoneId = ZoneId.systemDefault();
                if(targetEntity.hasAttribute(RealmConstant._createDateProperty)){
                    Date createdDateValue = (Date)targetEntity.getAttribute(RealmConstant._createDateProperty).getAttributeValue();
                    Instant instant = createdDateValue.toInstant();
                    createDate.setValue(instant.atZone(zoneId).toLocalDateTime());
                }
                if(targetEntity.hasAttribute(RealmConstant._lastModifyDateProperty)){
                    Date lastModifyDateValue = (Date)targetEntity.getAttribute(RealmConstant._lastModifyDateProperty).getAttributeValue();
                    Instant instant = lastModifyDateValue.toInstant();
                    lastUpdateDate.setValue(instant.atZone(zoneId).toLocalDateTime());
                }
                if(targetEntity.hasAttribute(RealmConstant._creatorIdProperty)){
                    String creatorIdValue = targetEntity.getAttribute(RealmConstant._creatorIdProperty).getAttributeValue().toString();
                    creator.setValue(creatorIdValue);
                }
                if(targetEntity.hasAttribute(RealmConstant._dataOriginProperty)){
                    String dataOriginValue = targetEntity.getAttribute(RealmConstant._dataOriginProperty).getAttributeValue().toString();
                    dataSource.setValue(dataOriginValue);
                }
            }else{
                CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 中不存在 UID 为"+conceptionEntityUID+" 的概念实体", NotificationVariant.LUMO_ERROR);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("概念类型 "+conceptionKind+" 不存在", NotificationVariant.LUMO_ERROR);
        }
        coreRealm.closeGlobalSession();
    }

    private void renderConceptionEntityUI(String conceptionKindName,String conceptionEntityUID){
        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(conceptionKindName,conceptionEntityUID);

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8, Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(conceptionKindName);
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
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(conceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    public void showDisplayEntityDetailButton(boolean showButtonFlag){
        this.displayEntityDetailButton.setVisible(showButtonFlag);
    }
}
