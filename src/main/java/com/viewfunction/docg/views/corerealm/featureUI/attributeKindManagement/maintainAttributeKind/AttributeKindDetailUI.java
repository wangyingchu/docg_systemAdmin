package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;

import java.util.ArrayList;
import java.util.List;

@Route("attributeKindDetailInfo/:attributeKindUID")
public class AttributeKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String attributeKindUID;
    private int conceptionKindDetailViewHeightOffset = 110;
    private int currentBrowserHeight = 0;
    private Registration listener;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;

    public AttributeKindDetailUI(){}

    public AttributeKindDetailUI(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.attributeKindUID = beforeEnterEvent.getRouteParameters().get("attributeKindUID").get();
        this.conceptionKindDetailViewHeightOffset = 45;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderAttributeKindData();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int containerHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset;
            //this.containerConceptionKindsConfigView.setHeight(containerHeight);
            //this.containsAttributeKindsConfigView.setHeight(containerHeight);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            int containerHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset;
            //this.containerConceptionKindsConfigView.setHeight(containerHeight);
            //this.containsAttributeKindsConfigView.setHeight(containerHeight);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void renderAttributeKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();
        String attributesViewKindDisplayInfo = this.attributeKindUID;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
        if(targetAttributeKind != null){
            attributesViewKindDisplayInfo = targetAttributeKind.getAttributeKindName() +" ( "+this.attributeKindUID+" )";
        }
        NativeLabel attributesViewKindNameLabel = new NativeLabel(attributesViewKindDisplayInfo);
        attributesViewKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(attributesViewKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.attributeKindUID, KindDescriptionEditorItemWidget.KindType.AttributeKind);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

        Button metaConfigItemConfigInfoButton= new Button("元属性配置管理");
        metaConfigItemConfigInfoButton.setIcon(VaadinIcon.BOOKMARK.create());
        metaConfigItemConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        metaConfigItemConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderMetaConfigItemConfigInfoUI();
            }
        });
        buttonList.add(metaConfigItemConfigInfoButton);

        Button classificationConfigInfoButton= new Button("分类配置管理");
        classificationConfigInfoButton.setIcon(VaadinIcon.TAGS.create());
        classificationConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        classificationConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderClassificationConfigInfoUI();
            }
        });
        buttonList.add(classificationConfigInfoButton);

        Button refreshConceptionKindConfigInfoButton= new Button("刷新属性视图类型配置信息");
        refreshConceptionKindConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshConceptionKindConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshConceptionKindConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //containerConceptionKindsConfigView.refreshConceptionKindsInfo();
                //containsAttributeKindsConfigView.refreshAttributeTypesInfo();
            }
        });
        buttonList.add(refreshConceptionKindConfigInfoButton);

        Button conceptionKindMetaInfoButton= new Button("属性视图类型元数据");
        conceptionKindMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        conceptionKindMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        conceptionKindMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderShowMetaInfoUI();
            }
        });
        buttonList.add(conceptionKindMetaInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TASKS),"Attributes View Kind 属性视图类型  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);







    }
}
