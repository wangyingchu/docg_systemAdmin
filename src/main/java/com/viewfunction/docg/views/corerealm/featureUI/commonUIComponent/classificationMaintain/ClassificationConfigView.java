package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import java.util.ArrayList;
import java.util.List;

public class ClassificationConfigView extends VerticalLayout {

    public enum ClassificationRelatedObjectType {
        ConceptionKind,RelationKind,AttributeKind,AttributesViewKind,ConceptionEntity
    }

    private ClassificationConfigView.ClassificationRelatedObjectType classificationRelatedObjectType;
    private String relatedObjectID;

    public ClassificationConfigView(ClassificationConfigView.ClassificationRelatedObjectType
                                            classificationRelatedObjectType,String relatedObjectID){
        this.classificationRelatedObjectType = classificationRelatedObjectType;
        this.relatedObjectID = relatedObjectID;
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
        relateToClassificationButton.setIcon(VaadinIcon.BOOKMARK.create());
        relateToClassificationButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        buttonList.add(relateToClassificationButton);
        relateToClassificationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelateClassificationViewUI();
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

    private void renderRelateClassificationViewUI(){
        RelateClassificationView relateClassificationView = new RelateClassificationView(this.classificationRelatedObjectType,this.relatedObjectID);
        relateClassificationView.setContainerClassificationConfigView(this);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.BOOKMARK),"关联分类",null,true,1140,710,false);
        fixSizeWindow.setWindowContent(relateClassificationView);
        fixSizeWindow.setModel(true);
        relateClassificationView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    public void setViewHeight(int viewHeight){
        this.setHeight(viewHeight,Unit.PIXELS);
    }
}
