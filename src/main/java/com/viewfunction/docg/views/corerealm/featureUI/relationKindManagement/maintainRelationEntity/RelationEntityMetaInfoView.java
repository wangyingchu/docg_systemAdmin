package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RelationEntityMetaInfoView extends VerticalLayout {
    private String relationKind;
    private String relationEntityUID;
    private DateTimePicker createDate;
    private DateTimePicker lastUpdateDate;
    private TextField dataSource;
    private TextField creator;
    public RelationEntityMetaInfoView(String relationKind, String relationEntityUID){
        this.setMargin(false);
        this.setSpacing(false);
        this.relationKind = relationKind;
        this.relationEntityUID = relationEntityUID;
        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("12px");
        relationKindIcon.getStyle().set("padding-right","3px");
        Icon relationEntityIcon = VaadinIcon.KEY_O.create();
        relationEntityIcon.setSize("18px");
        relationEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(relationKindIcon, relationKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(relationEntityIcon, relationEntityUID));
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
        RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        if(targetRelationKind != null){
            RelationEntity targetEntity = targetRelationKind.getEntityByUID(this.relationEntityUID);
            if(targetEntity != null){
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
                CommonUIOperationUtil.showPopupNotification("关系类型 "+ relationKind +" 中不存在 UID 为"+ relationEntityUID +" 的关系实体", NotificationVariant.LUMO_ERROR);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("关系类型 "+ relationKind +" 不存在", NotificationVariant.LUMO_ERROR);
        }
        coreRealm.closeGlobalSession();
    }
}
