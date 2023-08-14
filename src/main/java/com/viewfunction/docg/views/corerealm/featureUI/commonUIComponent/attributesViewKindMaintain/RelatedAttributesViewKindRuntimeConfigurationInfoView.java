package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributesViewKindMaintain;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.AttributeKindDetachedFromAttributesViewKindEvent;
import com.viewfunction.docg.element.eventHandling.AttributesViewKindDetachedFromConceptionKindEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;

public class RelatedAttributesViewKindRuntimeConfigurationInfoView extends VerticalLayout implements
        AttributeKindDetachedFromAttributesViewKindEvent.AttributeKindDetachedFromAttributesViewKindListener,
        AttributesViewKindDetachedFromConceptionKindEvent.AttributesViewKindDetachedFromConceptionKindListener{
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private RelatedAttributesViewKindsConfigView relatedAttributesViewKindsConfigView;
    private SecondaryTitleActionBar selectedAttributesViewKindTitleActionBar;
    private SecondaryTitleActionBar selectedAttributesViewKindUIDActionBar;
    private String pairKindIdentify;
    private Grid<AttributeKind> attributeKindAttributesInfoGrid;
    private Grid<ConceptionKind> conceptionKindAttributesInfoGrid;
    private AttributesViewKind selectedAttributesViewKind;

    public enum KindTypeOfRelatedPair {ConceptionKind,AttributeKind}
    private KindTypeOfRelatedPair kindTypeOfRelatedPair;

    public RelatedAttributesViewKindRuntimeConfigurationInfoView(KindTypeOfRelatedPair kindTypeOfRelatedPair,String pairKindIdentify){
        this.kindTypeOfRelatedPair = kindTypeOfRelatedPair;
        this.pairKindIdentify = pairKindIdentify;

        setSpacing(false);
        setMargin(false);
        setPadding(false);
        this.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setSpacing(false);
        mainContainerLayout.setMargin(false);
        mainContainerLayout.setPadding(false);
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setWidth(800,Unit.PIXELS);
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setMargin(false);
        leftSideContainerLayout.setPadding(false);
        mainContainerLayout.add(leftSideContainerLayout);

        RelatedAttributesViewKindsConfigView.AttributesViewKindSelectedListener
                attributesViewKindSelectedListener = new RelatedAttributesViewKindsConfigView.AttributesViewKindSelectedListener() {
            @Override
            public void attributesViewKindSelectedAction(AttributesViewKind selectedAttributesViewKind) {
                renderAttributesViewKindOverview(selectedAttributesViewKind);
            }
        };
        RelatedAttributesViewKindsConfigView.AttributesViewKindsRefreshedListener attributesViewKindsRefreshedListener = new RelatedAttributesViewKindsConfigView.AttributesViewKindsRefreshedListener() {
            @Override
            public void attributesViewKindsRefreshedAction() {
                resetAttributesViewKindsInfo();
            }
        };

        RelatedAttributesViewKindsConfigView.KindTypeOfRelatedPair configViewKindTypeOfRelatedPair = null;
        switch (this.kindTypeOfRelatedPair){
            case ConceptionKind -> configViewKindTypeOfRelatedPair = RelatedAttributesViewKindsConfigView.KindTypeOfRelatedPair.ConceptionKind;
            case AttributeKind -> configViewKindTypeOfRelatedPair = RelatedAttributesViewKindsConfigView.KindTypeOfRelatedPair.AttributeKind;
        }
        relatedAttributesViewKindsConfigView = new RelatedAttributesViewKindsConfigView(configViewKindTypeOfRelatedPair,this.pairKindIdentify);
        relatedAttributesViewKindsConfigView.setAttributesViewKindSelectedListener(attributesViewKindSelectedListener);
        relatedAttributesViewKindsConfigView.setAttributesViewKindsRefreshedListener(attributesViewKindsRefreshedListener);
        leftSideContainerLayout.add(relatedAttributesViewKindsConfigView);

        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setWidth(400, Unit.PIXELS);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setPadding(false);
        mainContainerLayout.add(rightSideContainerLayout);

        HorizontalLayout spaceDiv01Layout1 = new HorizontalLayout();
        spaceDiv01Layout1.setHeight(10,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout1);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.LAPTOP),"被选择属性视图类型概览");
        rightSideContainerLayout.add(filterTitle);

        HorizontalLayout spaceDiv01Layout2 = new HorizontalLayout();
        spaceDiv01Layout2.setHeight(2,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout2);

        selectedAttributesViewKindTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TASKS),"-",null,null,false);
        selectedAttributesViewKindTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.add(selectedAttributesViewKindTitleActionBar);

        selectedAttributesViewKindUIDActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.KEY_O),"-",null,null);
        selectedAttributesViewKindUIDActionBar.setWidth(100,Unit.PERCENTAGE);
        rightSideContainerLayout.add(selectedAttributesViewKindUIDActionBar);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CUBE),"包含被选择属性视图类型的概念类型");
        rightSideContainerLayout.add(infoTitle2);

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.setHeight(200,Unit.PIXELS);
        conceptionKindAttributesInfoGrid.addColumn(ConceptionKind::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(ConceptionKind::getConceptionKindDesc).setHeader("概念类型显示名称").setKey("idx_1");
        LightGridColumnHeader gridColumnHeader0_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"概念类型名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader0_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader1_idx1 = new LightGridColumnHeader(VaadinIcon.DESKTOP,"概念类型显示名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader1_idx1).setSortable(true);
        rightSideContainerLayout.add(conceptionKindAttributesInfoGrid);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.INPUT),"被选择属性视图类型包含的属性类型");
        rightSideContainerLayout.add(infoTitle1);

        attributeKindAttributesInfoGrid = new Grid<>();
        attributeKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        attributeKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        attributeKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        attributeKindAttributesInfoGrid.addColumn(AttributeKind::getAttributeKindName).setHeader("属性类型名称").setKey("idx_0");
        attributeKindAttributesInfoGrid.addColumn(AttributeKind::getAttributeKindUID).setHeader("属性类型 UID").setKey("idx_1").setFlexGrow(0).setWidth("150px").setResizable(false);
        attributeKindAttributesInfoGrid.addColumn(AttributeKind::getAttributeDataType).setHeader("数据数据类型").setKey("idx_2").setFlexGrow(0).setWidth("150px").setResizable(false);
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性类型名称");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.KEY_O,"属性类型 UID");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"数据数据类型");
        attributeKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        rightSideContainerLayout.add(attributeKindAttributesInfoGrid);
        //need use this layout to keep attributeKindAttributesInfoGrid not extends too long
        HorizontalLayout spaceDiv01Layout3 = new HorizontalLayout();
        spaceDiv01Layout3.setHeight(5,Unit.PIXELS);
        rightSideContainerLayout.add(spaceDiv01Layout3);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    public void setViewHeight(int viewHeight){
        relatedAttributesViewKindsConfigView.setViewHeight(viewHeight);
        attributeKindAttributesInfoGrid.setHeight(viewHeight-500,Unit.PIXELS);
    }

    public void setViewWidth(int viewWidth){
        rightSideContainerLayout.setWidth(viewWidth-600,Unit.PIXELS);
    }

    private void renderAttributesViewKindOverview(AttributesViewKind attributesViewKind){
        this.selectedAttributesViewKind = attributesViewKind;
        String attributesViewKindName = attributesViewKind.getAttributesViewKindName();
        String attributesViewKindDesc = attributesViewKind.getAttributesViewKindDesc() != null ?
                attributesViewKind.getAttributesViewKindDesc():"未设置描述信息";
        String attributesViewKindUID = attributesViewKind.getAttributesViewKindUID();
        String attributeNameText = attributesViewKindName +" ( "+attributesViewKindDesc+" )";
        selectedAttributesViewKindTitleActionBar.updateTitleContent(attributeNameText);
        selectedAttributesViewKindUIDActionBar.updateTitleContent(attributesViewKindUID);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind selectedAttributesViewKind = coreRealm.getAttributesViewKind(attributesViewKindUID);
        List<ConceptionKind> conceptionKindList = selectedAttributesViewKind.getContainerConceptionKinds();
        if(conceptionKindList != null){
            conceptionKindAttributesInfoGrid.setItems(conceptionKindList);
        }
        List<AttributeKind> containsAttributeKindList = selectedAttributesViewKind.getContainsAttributeKinds();
        if(containsAttributeKindList != null){
            attributeKindAttributesInfoGrid.setItems(containsAttributeKindList);
        }
        coreRealm.closeGlobalSession();
    }

    private void resetAttributesViewKindsInfo(){
        this.conceptionKindAttributesInfoGrid.setItems(new ArrayList<>());
        this.attributeKindAttributesInfoGrid.setItems(new ArrayList<>());
        this.selectedAttributesViewKindTitleActionBar.updateTitleContent("-");
        this.selectedAttributesViewKindUIDActionBar.updateTitleContent("-");
        this.selectedAttributesViewKind = null;
    }

    @Override
    public void receivedAttributeKindDetachedFromAttributesViewKindEvent(AttributeKindDetachedFromAttributesViewKindEvent event) {
        if(this.kindTypeOfRelatedPair.equals(KindTypeOfRelatedPair.AttributeKind)){
            if(event.getAttributesViewKindUID() != null && event.getAttributeKindUID() != null){
                if(this.selectedAttributesViewKind != null &&
                        this.selectedAttributesViewKind.getAttributesViewKindUID().equals(event.getAttributesViewKindUID()) &&
                        this.pairKindIdentify.equals(event.getAttributeKindUID())){
                    resetAttributesViewKindsInfo();
                }
            }
        }
    }

    @Override
    public void receivedAttributesViewKindDetachedFromConceptionKindEvent(AttributesViewKindDetachedFromConceptionKindEvent event) {
        if(this.kindTypeOfRelatedPair.equals(KindTypeOfRelatedPair.ConceptionKind)){
            if(event.getAttributesViewKindUID() != null && event.getConceptionKindName() != null){
                if(this.selectedAttributesViewKind != null &&
                        this.selectedAttributesViewKind.getAttributesViewKindUID().equals(event.getAttributesViewKindUID()) &&
                        this.pairKindIdentify.equals(event.getConceptionKindName())){
                    resetAttributesViewKindsInfo();
                }
            }
        }
    }

    public void refreshRelatedAttributesViewKindRuntimeConfigurationInfo(){
        resetAttributesViewKindsInfo();
        relatedAttributesViewKindsConfigView.refreshAttributesViewKindsInfo();
    }
}
