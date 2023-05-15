package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.List;

public class ClassificationConfigView extends VerticalLayout {

    public ClassificationConfigView(){
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderClassificationConfigUI();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void renderClassificationConfigUI(){

        this.setWidth(100, Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button relateToClassificationButton= new Button("关联分类");
        relateToClassificationButton.setIcon(LineAwesomeIconsSvg.BOOKMARK.create());
        relateToClassificationButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        buttonList.add(relateToClassificationButton);
        relateToClassificationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAddNewKindIndexUI();
            }
        });

        Button refreshRelatedClassificationsInfoButton = new Button("刷新分类信息",new Icon(VaadinIcon.REFRESH));
        refreshRelatedClassificationsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshRelatedClassificationsInfoButton.addClickListener((ClickEvent<Button> click) ->{
            //refreshKindIndex();
        });
        buttonList.add(refreshRelatedClassificationsInfoButton);

        SecondaryTitleActionBar relatedClassificationConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAGS),"分类配置管理 ",secTitleElementsList,buttonList);
        add(relatedClassificationConfigActionBar);
    }
}
