package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

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

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;

import java.util.ArrayList;
import java.util.List;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.AttributesViewKind;

@Route("attributesViewKindDetailInfo/:attributesViewKindUID")
public class AttributesViewKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver {
    private String attributesViewKindUID;
    private int conceptionKindDetailViewHeightOffset = 110;
    private int currentBrowserHeight = 0;
    private Registration listener;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;

    public AttributesViewKindDetailUI(){}

    public AttributesViewKindDetailUI(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;
        this.conceptionKindDetailViewHeightOffset = 45;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.attributesViewKindUID = beforeEnterEvent.getRouteParameters().get("attributesViewKindUID").get();
        this.conceptionKindDetailViewHeightOffset = 45;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderAttributesViewKindData();


        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int chartHeight = currentBrowserHeight - conceptionKindDetailViewHeightOffset - 340;
            //conceptionKindCorrelationInfoChart.setHeight(chartHeight, Unit.PIXELS);
            //this.conceptionRelationRealtimeInfoGrid.setHeight(chartHeight,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            //kindCorrelationInfoTabSheet.setHeight(currentBrowserHeight-conceptionKindDetailViewHeightOffset-290,Unit.PIXELS);
        }));



        //ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderAttributesViewKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();
        String attributesViewKindDisplayInfo = this.attributesViewKindUID;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        if(targetAttributesViewKind != null){
            attributesViewKindDisplayInfo = targetAttributesViewKind.getAttributesViewKindName() +" ( "+this.attributesViewKindUID+" )";
        }
        NativeLabel attributesViewKindNameLabel = new NativeLabel(attributesViewKindDisplayInfo);
        attributesViewKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(attributesViewKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.attributesViewKindUID,AttributesViewKind);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

        Button refreshConceptionKindConfigInfoButton= new Button("刷新属性视图类型配置信息");
        refreshConceptionKindConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshConceptionKindConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshConceptionKindConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //ConceptionKindConfigurationInfoRefreshEvent conceptionKindConfigurationInfoRefreshEvent = new ConceptionKindConfigurationInfoRefreshEvent();
                //conceptionKindConfigurationInfoRefreshEvent.setConceptionKindName(conceptionKind);
                //ResourceHolder.getApplicationBlackboard().fire(conceptionKindConfigurationInfoRefreshEvent);
            }
        });
        buttonList.add(refreshConceptionKindConfigInfoButton);

        Button queryConceptionKindButton= new Button("概念类型实体数据查询");
        queryConceptionKindButton.setIcon(VaadinIcon.RECORDS.create());
        queryConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        queryConceptionKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderConceptionKindQueryUI();
            }
        });
        buttonList.add(queryConceptionKindButton);

        Button conceptionKindMetaInfoButton= new Button("属性视图类型元数据");
        conceptionKindMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        conceptionKindMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        conceptionKindMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderShowMetaInfoUI();
            }
        });
        buttonList.add(conceptionKindMetaInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TASKS),"Attributes View Kind 属性视图类型  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);
    }

    private void renderShowMetaInfoUI(){
        AttributesViewKindMetaInfoView attributesViewKindMetaInfoView = new AttributesViewKindMetaInfoView(this.attributesViewKindUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"属性视图类型元数据信息",null,true,500,340,false);
        fixSizeWindow.setWindowContent(attributesViewKindMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }
}
