package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.eventHandling.ClassificationAttributeAddedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;

import java.util.List;

public class ClassificationAttributesEditorView extends VerticalLayout implements
        ClassificationAttributeAddedEvent.ClassificationAttributeAddedListener {

    private String classificationName;
    private int classificationAttributesEditorViewHeightOffset;
    private VerticalLayout attributeEditorsContainer;
    private Registration listener;
    public ClassificationAttributesEditorView(String classificationName,int classificationAttributesEditorViewHeightOffset){
        this.classificationName = classificationName;
        this.classificationAttributesEditorViewHeightOffset = classificationAttributesEditorViewHeightOffset;
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);

        HorizontalLayout actionButtonBarContainer = new HorizontalLayout();
        actionButtonBarContainer.setSpacing(false);

        Button addAttributeButton= new Button("添加分类属性");
        addAttributeButton.setIcon(VaadinIcon.PLUS.create());
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        actionButtonBarContainer.add(addAttributeButton);
        addAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddNewAttributeUI();
            }
        });

        SecondaryIconTitle viewTitle = new SecondaryIconTitle(new Icon(VaadinIcon.COMBOBOX),"分类属性",actionButtonBarContainer);
        //set height to 39 in order to make ConceptionEntityAttributesEditorView and ConceptionEntityIntegratedInfoView have the same tab bottom line align
        viewTitle.setHeight(39, Unit.PIXELS);
        add(viewTitle);
        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);

        attributeEditorsContainer = new VerticalLayout();
        attributeEditorsContainer.setMargin(false);
        attributeEditorsContainer.setSpacing(false);
        attributeEditorsContainer.setPadding(false);
        attributeEditorsContainer.setWidthFull();

        Scroller attributeEditorItemsScroller = new Scroller(attributeEditorsContainer);
        attributeEditorItemsScroller.setWidth(100,Unit.PERCENTAGE);
        attributeEditorItemsScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        add(attributeEditorItemsScroller);
    }

    private void renderAttributesEditorItems(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        if(targetClassification != null){
            List<AttributeValue> allAttributesList = targetClassification.getAttributes();
            if(allAttributesList != null){
                for(AttributeValue currentAttributeValue:allAttributesList){
                    String currentAttributeName = currentAttributeValue.getAttributeName();
                    if(currentAttributeName.equals(RealmConstant._createDateProperty) ||
                            currentAttributeName.equals(RealmConstant._lastModifyDateProperty) ||
                            currentAttributeName.equals(RealmConstant._creatorIdProperty)||
                            currentAttributeName.equals(RealmConstant._dataOriginProperty)||
                            currentAttributeName.equals(RealmConstant._NameProperty)||
                            currentAttributeName.equals(RealmConstant._DescProperty))
                    {}else{
                        AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(this.classificationName,null,currentAttributeValue, AttributeEditorItemWidget.KindType.Classification);
                        attributeEditorsContainer.add(attributeEditorItemWidget);
                    }
                }
            }
        }
    }

    private void renderAddNewAttributeUI(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(this.classificationName,"MOCK_UID", AddEntityAttributeView.KindType.Classification);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"添加实体属性",null,true,480,200,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            attributeEditorsContainer.setHeight(event.getHeight()-classificationAttributesEditorViewHeightOffset,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            attributeEditorsContainer.setHeight(browserHeight-classificationAttributesEditorViewHeightOffset,Unit.PIXELS);
        }));
        renderAttributesEditorItems();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    @Override
    public void receivedClassificationAttributeAddedEvent(ClassificationAttributeAddedEvent event) {
        String classificationName = event.getClassificationName();
        AttributeValue attributeValue = event.getAttributeValue();
        if(classificationName != null && attributeValue != null){
            if(this.classificationName.equals(classificationName)){
                AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(this.classificationName,null,attributeValue, AttributeEditorItemWidget.KindType.Classification);
                attributeEditorsContainer.add(attributeEditorItemWidget);
            }
        }
    }
}
