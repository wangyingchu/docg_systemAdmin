package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

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
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;

import java.util.ArrayList;
import java.util.List;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.Classification;

@Route("classificationDetailInfo/:classificationName")
public class ClassificationDetailUI extends VerticalLayout implements
        BeforeEnterObserver{
    private String classificationName;
    private int attributesViewKindDetailViewHeightOffset = 40;
    private int currentBrowserHeight = 0;
    private Registration listener;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;

    public ClassificationDetailUI(){}

    public ClassificationDetailUI(String classificationName){
        this.classificationName = classificationName;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.classificationName = beforeEnterEvent.getRouteParameters().get("classificationName").get();
        this.attributesViewKindDetailViewHeightOffset = -10;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //ResourceHolder.getApplicationBlackboard().addListener(this);
        renderClassificationData();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderClassificationData(){
        List<Component> secTitleElementsList = new ArrayList<>();
        String attributesViewKindDisplayInfo = this.classificationName;

        NativeLabel attributesViewKindNameLabel = new NativeLabel(attributesViewKindDisplayInfo);
        attributesViewKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(attributesViewKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.classificationName,Classification);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

        Button attributesViewKindMetaInfoButton= new Button("分类元数据");
        attributesViewKindMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        attributesViewKindMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        attributesViewKindMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderShowMetaInfoUI();
            }
        });
        buttonList.add(attributesViewKindMetaInfoButton);

        Icon divIcon = VaadinIcon.LINE_V.create();
        divIcon.setSize("8px");
        buttonList.add(divIcon);

        Button refreshAttributesViewKindConfigInfoButton= new Button("刷新分类配置信息");
        refreshAttributesViewKindConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshAttributesViewKindConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshAttributesViewKindConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //containerConceptionKindsConfigView.refreshConceptionKindsInfo();
                //containsAttributeKindsConfigView.refreshAttributeTypesInfo();
                //refreshAttributesViewKindCorrelationInfoChart();
            }
        });
        buttonList.add(refreshAttributesViewKindConfigInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAGS),"Classification 分类  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);


        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);

        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(350, Unit.PIXELS);
        leftSideContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");

        ClassificationAttributesEditorView classificationAttributesEditorView= new ClassificationAttributesEditorView(this.classificationName,this.attributesViewKindDetailViewHeightOffset);
        leftSideContainerLayout.add(classificationAttributesEditorView);

    }
}
