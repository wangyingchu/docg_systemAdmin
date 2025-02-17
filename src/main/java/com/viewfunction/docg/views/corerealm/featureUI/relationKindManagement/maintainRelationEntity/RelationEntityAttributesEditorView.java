package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity;

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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.eventHandling.RelationEntityAttributeAddedEvent;
import com.viewfunction.docg.element.eventHandling.RelationEntityAttributeDeletedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddEntityAttributeView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;

import java.util.List;

public class RelationEntityAttributesEditorView extends VerticalLayout implements
        RelationEntityAttributeAddedEvent.RelationEntityAttributeAddedListener,
        RelationEntityAttributeDeletedEvent.RelationEntityAttributeDeletedListener {
    private String relationKind;
    private String relationEntityUID;
    private VerticalLayout attributeEditorsContainer;
    private Registration listener;
    private int relationEntityAttributesEditorHeightOffset;
    public RelationEntityAttributesEditorView(String relationKind, String relationEntityUID, int relationEntityAttributesEditorHeightOffset){
        this.relationKind = relationKind;
        this.relationEntityUID = relationEntityUID;
        this.relationEntityAttributesEditorHeightOffset = relationEntityAttributesEditorHeightOffset;
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);

        HorizontalLayout actionButtonBarContainer = new HorizontalLayout();
        actionButtonBarContainer.setSpacing(false);

        Button metaInfoButton= new Button("实体元数据");
        metaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        metaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        actionButtonBarContainer.add(metaInfoButton);
        metaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderShowMetaInfoUI();
            }
        });

        Button addAttributeButton= new Button("添加实体属性");
        addAttributeButton.setIcon(VaadinIcon.PLUS.create());
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        actionButtonBarContainer.add(addAttributeButton);
        SecondaryIconTitle viewTitle = new SecondaryIconTitle(new Icon(VaadinIcon.COMBOBOX),"实体属性",actionButtonBarContainer);
        //set height to 39 in order to make ConceptionEntityAttributesEditorView and ConceptionEntityIntegratedInfoView have the same tab bottom line align
        viewTitle.setHeight(39, Unit.PIXELS);
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
        RelationKind targetRelationKind = coreRealm.getRelationKind(this.relationKind);
        if(targetRelationKind != null){
            RelationEntity targetEntity = targetRelationKind.getEntityByUID(this.relationEntityUID);
            if(targetEntity != null){
                List<AttributeValue> allAttributesList = targetEntity.getAttributes();
                if(allAttributesList != null){
                    for(AttributeValue currentAttributeValue:allAttributesList){
                        AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(this.relationKind,this.relationEntityUID,currentAttributeValue, AttributeEditorItemWidget.KindType.RelationKind);
                        attributeEditorsContainer.add(attributeEditorItemWidget);
                    }
                }
            }
        }
    }

    private void renderAddNewAttributeUI(){
        AddEntityAttributeView addEntityAttributeView = new AddEntityAttributeView(this.relationKind,this.relationEntityUID, AddEntityAttributeView.KindType.RelationKind);

        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS),"添加实体属性",null,true,480,200,false);
        fixSizeWindow.setWindowContent(addEntityAttributeView);
        fixSizeWindow.setModel(true);
        addEntityAttributeView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderShowMetaInfoUI(){
        RelationEntityMetaInfoView relationEntityMetaInfoView = new RelationEntityMetaInfoView(this.relationKind,this.relationEntityUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"关系实体元数据信息",null,true,500,340,false);
        fixSizeWindow.setWindowContent(relationEntityMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            attributeEditorsContainer.setHeight(event.getHeight()- relationEntityAttributesEditorHeightOffset,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            attributeEditorsContainer.setHeight(browserHeight- relationEntityAttributesEditorHeightOffset,Unit.PIXELS);
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
    public void receivedRelationEntityAttributeAddedEvent(RelationEntityAttributeAddedEvent event) {
        String entityUID = event.getRelationEntityUID();
        AttributeValue attributeValue = event.getAttributeValue();
        if(entityUID != null && attributeValue != null){
            if(this.relationEntityUID.equals(entityUID)){
                AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(this.relationKind,this.relationEntityUID,attributeValue, AttributeEditorItemWidget.KindType.RelationKind);
                attributeEditorsContainer.add(attributeEditorItemWidget);
            }
        }
    }

    @Override
    public void receivedRelationEntityAttributeDeletedEvent(RelationEntityAttributeDeletedEvent event) {
        String entityUID = event.getRelationEntityUID();
        if(entityUID != null){
            if(this.relationEntityUID.equals(entityUID)){
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
