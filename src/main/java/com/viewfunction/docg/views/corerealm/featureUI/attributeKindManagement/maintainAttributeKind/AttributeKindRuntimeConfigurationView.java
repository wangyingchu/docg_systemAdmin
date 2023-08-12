package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.AttributeKindDescriptionUpdatedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain.ClassificationConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain.MetaConfigItemsConfigView;

public class AttributeKindRuntimeConfigurationView extends VerticalLayout implements
        AttributeKindDescriptionUpdatedEvent.AttributeKindDescriptionUpdatedListener{
    private String attributeKindUID;
    private MetaConfigItemsConfigView metaConfigItemsConfigView;
    private ClassificationConfigView classificationConfigView;
    private PrimaryKeyValueDisplayItem attributeKindDescTxt;

    public AttributeKindRuntimeConfigurationView(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.COG_O),"属性类型定义配置");
        add(filterTitle1);
        HorizontalLayout infoContainer0 = new HorizontalLayout();
        infoContainer0.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer0.setWidthFull();
        add(infoContainer0);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);

        new PrimaryKeyValueDisplayItem(infoContainer0, VaadinIcon.INFO_CIRCLE_O.create(),"属性类型名称:",targetAttributeKind.getAttributeKindName());
        HorizontalLayout horSpaceDiv0 = new HorizontalLayout();
        horSpaceDiv0.setWidth(20,Unit.PIXELS);
        infoContainer0.add(horSpaceDiv0);
        attributeKindDescTxt = new PrimaryKeyValueDisplayItem(infoContainer0, VaadinIcon.DESKTOP.create(),"属性类型描述:",targetAttributeKind.getAttributeKindDesc());

        HorizontalLayout infoContainer1 = new HorizontalLayout();
        infoContainer1.setDefaultVerticalComponentAlignment(Alignment.END);
        infoContainer1.setWidthFull();
        infoContainer1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer1);
        new PrimaryKeyValueDisplayItem(infoContainer1, LineAwesomeIconsSvg.FIRSTDRAFT.create(),"属性数据类型:",targetAttributeKind.getAttributeDataType().toString());
        HorizontalLayout horSpaceDiv1 = new HorizontalLayout();
        horSpaceDiv1.setWidth(20,Unit.PIXELS);
        infoContainer1.add(horSpaceDiv1);
        new PrimaryKeyValueDisplayItem(infoContainer1, VaadinIcon.KEY_O.create(),"属性类型 UID:",targetAttributeKind.getAttributeKindUID());

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.CONTROLLER),"属性类型组件运行时配置");
        filterTitle2.getStyle().set("padding-top", "var(--lumo-space-s)");
        add(filterTitle2);

        metaConfigItemsConfigView = new MetaConfigItemsConfigView(MetaConfigItemsConfigView.MetaConfigItemType.AttributeKind,this.attributeKindUID);
        metaConfigItemsConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(metaConfigItemsConfigView);

        classificationConfigView = new ClassificationConfigView(ClassificationConfigView.ClassificationRelatedObjectType.AttributeKind,this.attributeKindUID);
        classificationConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(classificationConfigView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        metaConfigItemsConfigView.setViewHeight(280);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedAttributeKindDescriptionUpdatedEvent(AttributeKindDescriptionUpdatedEvent event) {
        if(event.getAttributeKindUID() != null && event.getAttributeKindDesc() != null){
            attributeKindDescTxt.updateDisplayValue(event.getAttributeKindDesc());
        }
    }

    public void setViewHeight(int viewHeight){
        this.classificationConfigView.setHeight(viewHeight - 425,Unit.PIXELS);
    }

    public void refreshAttributeKindRuntimeConfigurationInfo(){
        metaConfigItemsConfigView.refreshMetaConfigItemsInfo();
        classificationConfigView.refreshClassificationConfigInfo();
    }
}
