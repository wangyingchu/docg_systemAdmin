package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

public class ClassificationAttributesEditorView extends VerticalLayout {

    public ClassificationAttributesEditorView(String classificationName,int classificationAttributesEditorViewHeightOffset){




        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);

        HorizontalLayout actionButtonBarContainer = new HorizontalLayout();
        actionButtonBarContainer.setSpacing(false);

        Button metaInfoButton= new Button("实体元数据");
        metaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        metaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        //actionButtonBarContainer.add(metaInfoButton);
        metaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderShowMetaInfoUI();
            }
        });

        Button classificationsManageButton= new Button("分类关联");
        classificationsManageButton.setIcon(VaadinIcon.TAGS.create());
        classificationsManageButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        //actionButtonBarContainer.add(classificationsManageButton);
        classificationsManageButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderClassificationManageUI();
            }
        });

        Button addAttributeButton= new Button("添加分类属性");
        addAttributeButton.setIcon(VaadinIcon.PLUS.create());
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        actionButtonBarContainer.add(addAttributeButton);
        addAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAddNewAttributeUI();
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





    }
}
