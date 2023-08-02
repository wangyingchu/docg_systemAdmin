package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttributeKindMetaInfoView extends VerticalLayout {
    private String attributeKindUID;
    private DateTimePicker createDate;
    private DateTimePicker lastUpdateDate;
    private TextField dataSource;
    private TextField creator;

    public AttributeKindMetaInfoView(String attributeKindUID){
        this.setMargin(false);
        this.setSpacing(false);
        this.attributeKindUID = attributeKindUID;

        Icon attributesViewKindIcon = VaadinIcon.INPUT.create();
        attributesViewKindIcon.setSize("12px");
        attributesViewKindIcon.getStyle().set("padding-right","3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(attributesViewKindIcon, attributeKindUID));

        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

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
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
        if(targetAttributeKind != null){
            ZoneId zoneId = ZoneId.systemDefault();
            Date createdDateValue = targetAttributeKind.getCreateDateTime();
            Instant createInstant = createdDateValue.toInstant();
            createDate.setValue(createInstant.atZone(zoneId).toLocalDateTime());
            Date lastModifyDateValue = targetAttributeKind.getLastModifyDateTime();
            Instant lastUpdateInstant = lastModifyDateValue.toInstant();
            lastUpdateDate.setValue(lastUpdateInstant.atZone(zoneId).toLocalDateTime());
            String creatorIdValue = targetAttributeKind.getCreatorId();
            if(creatorIdValue != null){
                creator.setValue(creatorIdValue);
            }
            String dataOriginValue = targetAttributeKind.getDataOrigin();
            if(dataOriginValue != null){
                dataSource.setValue(dataOriginValue);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification(" UID 为："+ attributeKindUID +" 的属性类型不存在", NotificationVariant.LUMO_ERROR);
        }
        coreRealm.closeGlobalSession();
    }
}
