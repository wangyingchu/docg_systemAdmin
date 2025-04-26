package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.eventHandling.AttributesViewKindDescriptionUpdatedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain.ClassificationConfigView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.metaConfigItemMaintain.MetaConfigItemsConfigView;

public class AttributesViewKindRuntimeConfigurationView extends VerticalLayout implements
        AttributesViewKindDescriptionUpdatedEvent.AttributesViewKindDescriptionUpdatedListener{
    private String attributesViewKindUID;
    private MetaConfigItemsConfigView metaConfigItemsConfigView;
    private ClassificationConfigView classificationConfigView;
    private PrimaryKeyValueDisplayItem attributesViewKindDescTxt;

    public AttributesViewKindRuntimeConfigurationView(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;

        SecondaryIconTitle filterTitle1 = new SecondaryIconTitle(new Icon(VaadinIcon.COG_O),"属性视图类型定义配置");
        add(filterTitle1);
        HorizontalLayout infoContainer0 = new HorizontalLayout();
        infoContainer0.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        infoContainer0.setWidthFull();
        add(infoContainer0);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);

        new PrimaryKeyValueDisplayItem(infoContainer0, VaadinIcon.INFO_CIRCLE_O.create(),"属性视图类型名称:",targetAttributesViewKind.getAttributesViewKindName());
        HorizontalLayout horSpaceDiv0 = new HorizontalLayout();
        horSpaceDiv0.setWidth(20, Unit.PIXELS);
        infoContainer0.add(horSpaceDiv0);
        attributesViewKindDescTxt = new PrimaryKeyValueDisplayItem(infoContainer0, VaadinIcon.DESKTOP.create(),"属性视图类型描述:",targetAttributesViewKind.getAttributesViewKindDesc());

        HorizontalLayout infoContainer1 = new HorizontalLayout();
        infoContainer1.setDefaultVerticalComponentAlignment(Alignment.END);
        infoContainer1.setWidthFull();
        infoContainer1.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-l)");
        add(infoContainer1);
        new PrimaryKeyValueDisplayItem(infoContainer1, VaadinIcon.ELLIPSIS_H.create(),"属性视图存储结构:",targetAttributesViewKind.getAttributesViewKindDataForm().toString());
        HorizontalLayout horSpaceDiv1 = new HorizontalLayout();
        horSpaceDiv1.setWidth(20,Unit.PIXELS);
        infoContainer1.add(horSpaceDiv1);

        new PrimaryKeyValueDisplayItem(infoContainer1, VaadinIcon.KEY_O.create(),"属性视图类型 UID:",targetAttributesViewKind.getAttributesViewKindUID());

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.CONTROLLER),"属性视图类型组件运行时配置");
        filterTitle2.getStyle().set("padding-top", "var(--lumo-space-s)");
        add(filterTitle2);

        metaConfigItemsConfigView = new MetaConfigItemsConfigView(MetaConfigItemsConfigView.MetaConfigItemType.AttributesViewKind,this.attributesViewKindUID);
        metaConfigItemsConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(metaConfigItemsConfigView);

        classificationConfigView = new ClassificationConfigView(ClassificationConfigView.ClassificationRelatedObjectType.AttributesViewKind,this.attributesViewKindUID);
        classificationConfigView.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(classificationConfigView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        metaConfigItemsConfigView.setViewHeight(350);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedAttributesViewKindDescriptionUpdatedEvent(AttributesViewKindDescriptionUpdatedEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getAttributesViewKindDesc() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                attributesViewKindDescTxt.updateDisplayValue(event.getAttributesViewKindDesc());
            }
        }
    }

    public void setViewHeight(int viewHeight){
        this.classificationConfigView.setHeight(viewHeight - 575,Unit.PIXELS);
    }
}
