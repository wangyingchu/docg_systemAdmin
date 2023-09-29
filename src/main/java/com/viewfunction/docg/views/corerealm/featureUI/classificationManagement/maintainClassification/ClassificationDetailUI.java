package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        BeforeEnterObserver {
    private String classificationName;
    private int attributesViewKindDetailViewHeightOffset = 40;
    private int currentBrowserHeight = 0;
    private Registration listener;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
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

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAGS),"Classification 分类  ",secTitleElementsList,null);
        add(secondaryTitleActionBar);
    }
}
