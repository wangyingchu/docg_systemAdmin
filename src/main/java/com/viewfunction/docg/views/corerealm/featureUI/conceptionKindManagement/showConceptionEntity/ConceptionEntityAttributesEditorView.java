package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.showConceptionEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
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
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.List;

public class ConceptionEntityAttributesEditorView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private VerticalLayout attributeEditorsContainer;
    private Registration listener;

    public ConceptionEntityAttributesEditorView(String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);

        Button addAttributeButton= new Button();
        addAttributeButton.setIcon(VaadinIcon.PLUS.create());
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        Tooltips.getCurrent().setTooltip(addAttributeButton, "添加新的实体属性");
        SecondaryIconTitle viewTitle = new SecondaryIconTitle(new Icon(VaadinIcon.COMBOBOX),"实体属性",addAttributeButton);
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
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.conceptionEntityUID);
            if(targetEntity != null){
                List<AttributeValue> allAttributesList = targetEntity.getAttributes();
                if(allAttributesList != null){
                    for(AttributeValue currentAttributeValue:allAttributesList){
                        AttributeEditorItemWidget attributeEditorItemWidget = new AttributeEditorItemWidget(currentAttributeValue);
                        attributeEditorsContainer.add(attributeEditorItemWidget);
                    }
                }
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            attributeEditorsContainer.setHeight(event.getHeight()-135,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            attributeEditorsContainer.setHeight(browserHeight-135,Unit.PIXELS);
        }));
        renderAttributesEditorItems();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
}