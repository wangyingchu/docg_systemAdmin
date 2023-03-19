package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.eventHandling.ConceptionKindDescriptionUpdatedEvent;
import com.viewfunction.docg.element.eventHandling.RelationKindDescriptionUpdatedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import dev.mett.vaadin.tooltip.Tooltips;

public class KindDescriptionEditorItemWidget extends HorizontalLayout {

    public enum KindType {ConceptionKind,RelationKind}
    private KindType currentKindType;
    private String currentKindName;
    private String currentKindDescription;
    private Label kindDescriptionLabel;
    private TextField kindDescriptionEditor;
    private Button updateAttributeValueButton;
    private Button confirmUpdateAttributeValueButton;
    private Button cancelUpdateValueButton;
    public KindDescriptionEditorItemWidget(String kindName,KindType kindType){
        this.currentKindName = kindName;
        this.currentKindType = kindType;
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.kindDescriptionLabel = new Label("");
        this.kindDescriptionLabel.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("color","var(--lumo-body-text-color)")
                .set("fount-weight","bold");
        add(this.kindDescriptionLabel);

        this.kindDescriptionEditor = new TextField();
        this.kindDescriptionEditor.setWidth(200,Unit.PIXELS);
        this.kindDescriptionEditor.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        add(this.kindDescriptionEditor);
        this.kindDescriptionEditor.setVisible(false);

        updateAttributeValueButton = new Button();
        updateAttributeValueButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        Icon plusIcon = VaadinIcon.EDIT.create();
        plusIcon.setSize("18px");
        updateAttributeValueButton.setIcon(plusIcon);
        Tooltips.getCurrent().setTooltip(updateAttributeValueButton, "更新属性值");
        updateAttributeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                enableEditAttributeValue();
            }
        });
        add(updateAttributeValueButton);

        confirmUpdateAttributeValueButton = new Button();
        confirmUpdateAttributeValueButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL);
        Icon multiIcon = VaadinIcon.CHECK_CIRCLE_O.create();
        multiIcon.setSize("20px");
        confirmUpdateAttributeValueButton.setIcon(multiIcon);
        Tooltips.getCurrent().setTooltip(confirmUpdateAttributeValueButton, "确认更新");
        confirmUpdateAttributeValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                updateAttributeValue();
            }
        });
        add(confirmUpdateAttributeValueButton);
        confirmUpdateAttributeValueButton.setVisible(false);

        cancelUpdateValueButton = new Button();
        cancelUpdateValueButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL);
        Icon notIcon = VaadinIcon.ARROW_BACKWARD.create();
        notIcon.setSize("20px");
        cancelUpdateValueButton.setIcon(notIcon);
        Tooltips.getCurrent().setTooltip(cancelUpdateValueButton, "取消更新");
        cancelUpdateValueButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelEditAttributeValue();
            }
        });
        add(cancelUpdateValueButton);
        cancelUpdateValueButton.setVisible(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        switch (this.currentKindType){
            case ConceptionKind :
                ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.currentKindName);
                if(targetConceptionKind != null){
                    this.currentKindDescription = targetConceptionKind.getConceptionKindDesc();
                }
                break;
            case RelationKind:
                RelationKind targetRelationKind = coreRealm.getRelationKind(this.currentKindName);
                if(targetRelationKind != null){
                    this.currentKindDescription = targetRelationKind.getRelationKindDesc();
                }
        }
        if(this.currentKindDescription != null){
            this.kindDescriptionLabel.setText("("+this.currentKindDescription+")");
        }

    }

    private void enableEditAttributeValue(){
        this.kindDescriptionEditor.setValue(this.currentKindDescription);
        this.kindDescriptionEditor.setVisible(true);
        this.kindDescriptionLabel.setVisible(false);
        this.updateAttributeValueButton.setVisible(false);
        this.confirmUpdateAttributeValueButton.setVisible(true);
        this.cancelUpdateValueButton.setVisible(true);
    }

    private void cancelEditAttributeValue(){
        this.kindDescriptionEditor.setValue(this.currentKindDescription);
        this.kindDescriptionEditor.setVisible(false);
        this.kindDescriptionLabel.setVisible(true);
        this.updateAttributeValueButton.setVisible(true);
        this.confirmUpdateAttributeValueButton.setVisible(false);
        this.cancelUpdateValueButton.setVisible(false);
    }

    private void updateAttributeValue(){
        boolean updateResult = false;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        switch (this.currentKindType){
            case ConceptionKind :
                ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.currentKindName);
                if(targetConceptionKind != null){
                    updateResult = targetConceptionKind.updateConceptionKindDesc(this.kindDescriptionEditor.getValue());
                }
                break;
            case RelationKind:
                RelationKind targetRelationKind = coreRealm.getRelationKind(this.currentKindName);
                if(targetRelationKind != null){
                    updateResult = targetRelationKind.updateRelationKindDesc(this.kindDescriptionEditor.getValue());
                }
        }
        if(updateResult){
            this.currentKindDescription = this.kindDescriptionEditor.getValue();
            this.kindDescriptionLabel.setText("("+this.currentKindDescription+")");
            this.kindDescriptionEditor.setVisible(false);
            this.kindDescriptionLabel.setVisible(true);
            this.updateAttributeValueButton.setVisible(true);
            this.confirmUpdateAttributeValueButton.setVisible(false);
            this.cancelUpdateValueButton.setVisible(false);
            switch (this.currentKindType){
                case ConceptionKind :
                        ConceptionKindDescriptionUpdatedEvent conceptionKindDescriptionUpdatedEvent = new ConceptionKindDescriptionUpdatedEvent();
                    conceptionKindDescriptionUpdatedEvent.setConceptionKindDesc(this.currentKindDescription);
                    conceptionKindDescriptionUpdatedEvent.setConceptionKindName(this.currentKindName);
                    ResourceHolder.getApplicationBlackboard().fire(conceptionKindDescriptionUpdatedEvent);
                    break;
                case RelationKind:
                    RelationKindDescriptionUpdatedEvent relationKindDescriptionUpdatedEvent = new RelationKindDescriptionUpdatedEvent();
                    relationKindDescriptionUpdatedEvent.setRelationKindName(this.currentKindName);
                    relationKindDescriptionUpdatedEvent.setRelationKindDesc(this.currentKindDescription);
                    ResourceHolder.getApplicationBlackboard().fire(relationKindDescriptionUpdatedEvent);
            }
        }
    }
}
