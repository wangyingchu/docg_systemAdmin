package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityAttributeAddedEvent;
import com.viewfunction.docg.element.eventHandling.ConceptionEntityAttributeDeletedEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.List;

public class ConceptionEntityAttributesEditorView extends VerticalLayout implements
        ConceptionEntityAttributeAddedEvent.ConceptionEntityAttributeAddedListener,
        ConceptionEntityAttributeDeletedEvent.ConceptionEntityAttributeDeletedListener {
    private String conceptionKind;
    private String conceptionEntityUID;
    private VerticalLayout attributeEditorsContainer;
    private Registration listener;
    private int conceptionEntityAttributesEditorHeightOffset;
    public ConceptionEntityAttributesEditorView(String conceptionKind,String conceptionEntityUID,int conceptionEntityAttributesEditorHeightOffset){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityAttributesEditorHeightOffset = conceptionEntityAttributesEditorHeightOffset;
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);

        HorizontalLayout actionButtonBarContainer = new HorizontalLayout();
        actionButtonBarContainer.setSpacing(false);

        Button metaInfoButton= new Button("???????????????");
        metaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        metaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        actionButtonBarContainer.add(metaInfoButton);
        metaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderShowMetaInfoUI();
            }
        });

        Button addAttributeButton= new Button("??????????????????");
        addAttributeButton.setIcon(VaadinIcon.PLUS.create());
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        actionButtonBarContainer.add(addAttributeButton);
        SecondaryIconTitle viewTitle = new SecondaryIconTitle(new Icon(VaadinIcon.COMBOBOX),"????????????",actionButtonBarContainer);
        //set height to 39 in order to make ConceptionEntityAttributesEditorView and ConceptionEntityIntegratedInfoView have the same tab bottom line align
        viewTitle.setHeight(39,Unit.PIXELS);
        add(viewTitle);
        addAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddNewAttributeUI();
            }
        });

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
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            if(targetEntity != null){
                List<AttributeValue> allAttributesList = targetEntity.getAttributes();
                if(allAttributesList != null){
                    for(AttributeValue currentAttributeValue:allAttributesList){
                        AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(this.conceptionKind,this.conceptionEntityUID,currentAttributeValue);
                        attributeEditorsContainer.add(attributeEditorItemWidget);
                    }
                }
            }
        }
    }

    private void renderAddNewAttributeUI(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(this.conceptionKind,this.conceptionEntityUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"??????????????????",null,true,480,230,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderShowMetaInfoUI(){
        ConceptionEntityMetaInfoView conceptionEntityMetaInfoView = new ConceptionEntityMetaInfoView(this.conceptionKind,this.conceptionEntityUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"???????????????????????????",null,true,500,380,false);
        fixSizeWindow.setWindowContent(conceptionEntityMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            attributeEditorsContainer.setHeight(event.getHeight()-conceptionEntityAttributesEditorHeightOffset,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            attributeEditorsContainer.setHeight(browserHeight-conceptionEntityAttributesEditorHeightOffset,Unit.PIXELS);
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
    public void receivedConceptionEntityAttributeAddedEvent(ConceptionEntityAttributeAddedEvent event) {
        String entityUID = event.getConceptionEntityUID();
        AttributeValue attributeValue = event.getAttributeValue();
        if(entityUID != null && attributeValue != null){
            if(this.conceptionEntityUID.equals(entityUID)){
                AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(this.conceptionKind,this.conceptionEntityUID,attributeValue);
                attributeEditorsContainer.add(attributeEditorItemWidget);
            }
        }
    }

    @Override
    public void receivedConceptionEntityAttributeDeletedEvent(ConceptionEntityAttributeDeletedEvent event) {
        String entityUID = event.getConceptionEntityUID();
        if(entityUID != null){
            if(this.conceptionEntityUID.equals(entityUID)){
                String attributeName = event.getAttributeName();

                int attributeEditorItemWidgetCount = attributeEditorsContainer.getComponentCount();
                for(int i=0;i<attributeEditorItemWidgetCount;i++){
                    AttributeEditorItemWidget currentAttributeEditorItemWidget = (AttributeEditorItemWidget)attributeEditorsContainer.getComponentAt(i);
                    String currentAttributeName = currentAttributeEditorItemWidget.getAttributeName();
                    if(attributeName.equals(currentAttributeName)){
                        attributeEditorsContainer.remove(currentAttributeEditorItemWidget);
                        return;
                    }
                }
            }
        }
    }
}
