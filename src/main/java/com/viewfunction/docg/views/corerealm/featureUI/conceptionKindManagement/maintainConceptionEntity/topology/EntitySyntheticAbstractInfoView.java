package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;

import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity.RelationEntityDetailUI;

import java.util.ArrayList;
import java.util.List;

public class EntitySyntheticAbstractInfoView extends VerticalLayout{

    private VerticalLayout infoContentContainer;
    private NativeLabel conceptionKindLabel;
    private NativeLabel conceptionEntityUIDLabel;
    private NativeLabel relationKindLabel;
    private NativeLabel relationEntityUIDLabel;
    private SecondaryKeyValueDisplayItem relationCountDisplayItem;
    private SecondaryKeyValueDisplayItem inDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem outDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem isDenseDisplayItem;
    private String currentConceptionKindName;
    private String currentConceptionEntityUID;
    private Grid<AttributeValue> entityAttributesInfoGrid;
    private String currentRelationKindName;
    private String currentRelationEntityUID;
    private HorizontalLayout conceptionKindNameInfoContainer;
    private HorizontalLayout conceptionEntityUIDInfoContainer;
    private HorizontalLayout relationKindNameInfoContainer;
    private HorizontalLayout relationEntityUIDInfoContainer;
    private HorizontalLayout titleLayout1;
    private HorizontalLayout titleLayout2;
    private HorizontalLayout titleLayout3;
    private HorizontalLayout titleLayout4;
    private VerticalLayout relationConceptionEntityInfoLayout;

    public EntitySyntheticAbstractInfoView(int viewWidth){
        setSpacing(false);
        setMargin(false);
        setPadding(false);
        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.POINTER)," 已选中实体综合信息");
        add(infoTitle);

        HorizontalLayout horizontalDiv01 = new HorizontalLayout();
        horizontalDiv01.setWidthFull();
        horizontalDiv01.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(horizontalDiv01);

        infoContentContainer = new VerticalLayout();
        infoContentContainer.setSpacing(false);
        infoContentContainer.setMargin(false);
        infoContentContainer.setPadding(false);

        Scroller scroller = new Scroller(infoContentContainer);
        scroller.setWidth(viewWidth-5,Unit.PIXELS);
        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        add(scroller);

        conceptionKindNameInfoContainer = new HorizontalLayout();
        infoContentContainer.add(conceptionKindNameInfoContainer);
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("16px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        conceptionKindNameInfoContainer.add(conceptionKindIcon);
        conceptionKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindIcon);
        conceptionKindLabel = new NativeLabel();
        conceptionKindLabel.addClassNames("text-s","font-extrabold","border-b","border-contrast-10");
        conceptionKindLabel.getStyle().set("font-weight","bold").set("color","#2e4e7e");
        conceptionKindNameInfoContainer.getStyle().set("padding-top","15px");
        conceptionKindNameInfoContainer.add(conceptionKindLabel);
        conceptionKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindLabel);

        conceptionEntityUIDInfoContainer = new HorizontalLayout();
        infoContentContainer.add(conceptionEntityUIDInfoContainer);
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px");
        conceptionEntityUIDInfoContainer.add(conceptionEntityIcon);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityIcon);
        conceptionEntityUIDLabel = new NativeLabel();
        conceptionEntityUIDLabel.addClassNames("text-s","font-extrabold","border-b","border-contrast-10");
        conceptionEntityUIDLabel.getStyle().set("font-weight","bold").set("color","#2e4e7e");
        conceptionEntityUIDInfoContainer.getStyle().set("padding-top","5px");
        conceptionEntityUIDInfoContainer.add(conceptionEntityUIDLabel);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityUIDLabel);

        Button showDetailButton = new Button("显示概念实体详情");
        showDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showDetailButton.getStyle().set("font-size","12px");
        showDetailButton.setIcon(VaadinIcon.EYE.create());
        showDetailButton.setTooltipText("显示概念实体详情");
        showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedConceptionEntityUI();
            }
        });
        conceptionEntityUIDInfoContainer.add(showDetailButton);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,showDetailButton);
        conceptionEntityUIDInfoContainer.getStyle().set("padding-bottom", "10px");

        relationKindNameInfoContainer = new HorizontalLayout();
        infoContentContainer.add(relationKindNameInfoContainer);
        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("16px");
        relationKindIcon.getStyle().set("padding-right","3px");
        relationKindNameInfoContainer.add(relationKindIcon);
        relationKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,relationKindIcon);
        relationKindLabel = new NativeLabel();
        relationKindLabel.addClassNames("text-s","font-extrabold","border-b","border-contrast-10");
        relationKindLabel.getStyle().set("font-weight","bold").set("color","#2e4e7e");
        relationKindNameInfoContainer.getStyle().set("padding-top","15px");
        relationKindNameInfoContainer.add(relationKindLabel);
        relationKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,relationKindLabel);

        relationEntityUIDInfoContainer = new HorizontalLayout();
        infoContentContainer.add(relationEntityUIDInfoContainer);
        Icon relationEntityIcon = VaadinIcon.KEY_O.create();
        relationEntityIcon.setSize("18px");
        relationEntityIcon.getStyle().set("padding-right","3px");
        relationEntityUIDInfoContainer.add(relationEntityIcon);
        relationEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,relationEntityIcon);
        relationEntityUIDLabel = new NativeLabel();
        relationEntityUIDLabel.addClassNames("text-s","font-extrabold","border-b","border-contrast-10");
        relationEntityUIDLabel.getStyle().set("font-weight","bold").set("color","#2e4e7e");
        relationEntityUIDInfoContainer.getStyle().set("padding-top","12px");
        relationEntityUIDInfoContainer.add(relationEntityUIDLabel);
        relationEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,relationEntityUIDLabel);
        relationEntityUIDInfoContainer.getStyle().set("padding-bottom", "10px");

        Button showDetailButton2 = new Button("显示关系实体详情");
        showDetailButton2.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showDetailButton2.getStyle().set("font-size","12px");
        showDetailButton2.setIcon(VaadinIcon.EYE.create());
        showDetailButton2.setTooltipText("显示关系实体详情");
        showDetailButton2.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedRelationEntityUI();
            }
        });
        relationEntityUIDInfoContainer.add(showDetailButton2);
        relationEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,showDetailButton2);
        relationEntityUIDInfoContainer.getStyle().set("padding-bottom", "3px");

        titleLayout1 = new HorizontalLayout();
        infoContentContainer.add(titleLayout1);
        relationCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout1, VaadinIcon.LIST_OL.create(), "关联关系总量", "-");
        titleLayout2 = new HorizontalLayout();
        infoContentContainer.add(titleLayout2);
        inDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout2, VaadinIcon.ANGLE_DOUBLE_LEFT.create(), "关系入度", "-");
        titleLayout3 = new HorizontalLayout();
        infoContentContainer.add(titleLayout3);
        outDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout3, VaadinIcon.ANGLE_DOUBLE_RIGHT.create(), "关系出度", "-");
        titleLayout4 = new HorizontalLayout();
        infoContentContainer.add(titleLayout4);
        isDenseDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout4, VaadinIcon.BULLSEYE.create(), "是否稠密实体", "-");

        relationConceptionEntityInfoLayout = new VerticalLayout();
        infoContentContainer.add(relationConceptionEntityInfoLayout);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"实体属性");
        infoTitle1.getStyle().set("padding-top","15px");
        infoContentContainer.add(infoTitle1);
        HorizontalLayout horizontalDiv02 = new HorizontalLayout();
        horizontalDiv02.setWidthFull();
        horizontalDiv02.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        infoContentContainer.add(horizontalDiv02);

        entityAttributesInfoGrid = new Grid<>();
        entityAttributesInfoGrid.setWidth(100, Unit.PERCENTAGE);
        entityAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        entityAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        entityAttributesInfoGrid.addColumn(AttributeValue::getAttributeName).setHeader("属性名称").setKey("idx_0");
        entityAttributesInfoGrid.addColumn(AttributeValue::getAttributeValue).setHeader("属性值").setKey("idx_1").setFlexGrow(1).setResizable(true);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        entityAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.INPUT,"属性值");
        entityAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        entityAttributesInfoGrid.setHeight(400,Unit.PIXELS);
        infoContentContainer.add(entityAttributesInfoGrid);
        infoContentContainer.setVisible(false);
    }

    public void renderConceptionEntitySyntheticAbstractInfo(String conceptionKindName,String conceptionEntityUID){
        this.conceptionKindNameInfoContainer.setVisible(true);
        this.conceptionEntityUIDInfoContainer.setVisible(true);
        this.titleLayout1.setVisible(true);
        this.titleLayout2.setVisible(true);
        this.titleLayout3.setVisible(true);
        this.titleLayout4.setVisible(true);
        this.relationKindNameInfoContainer.setVisible(false);
        this.relationEntityUIDInfoContainer.setVisible(false);
        this.relationConceptionEntityInfoLayout.setVisible(false);

        this.infoContentContainer.setVisible(true);
        this.currentConceptionKindName = conceptionKindName;
        this.currentConceptionEntityUID = conceptionEntityUID;
        this.conceptionKindLabel.setText(conceptionKindName);
        this.conceptionEntityUIDLabel.setText(conceptionEntityUID);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.currentConceptionKindName);
        if(targetConceptionKind != null){
            ConceptionEntity targetEntity = targetConceptionKind.getEntityByUID(this.currentConceptionEntityUID);
            if(targetEntity != null){
                List<AttributeValue> allAttributesList = targetEntity.getAttributes();
                entityAttributesInfoGrid.setItems(allAttributesList);
                try {
                    long allRelationsCount = targetEntity.countAllRelations();
                    long inDegree = targetEntity.countAllSpecifiedRelations(null, RelationDirection.TO);
                    long outDegree = targetEntity.countAllSpecifiedRelations(null, RelationDirection.FROM);
                    inDegreeDisplayItem.updateDisplayValue(""+inDegree);
                    outDegreeDisplayItem.updateDisplayValue(""+outDegree);
                    relationCountDisplayItem.updateDisplayValue(""+allRelationsCount);
                    isDenseDisplayItem.updateDisplayValue(""+targetEntity.isDense());
                } catch (CoreRealmServiceRuntimeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        coreRealm.closeGlobalSession();
    }

    private void renderRelatedConceptionEntityUI(){
        String targetConceptionKind = this.currentConceptionKindName;
        String targetConceptionEntityUID = this.currentConceptionEntityUID;

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(targetConceptionKind);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(targetConceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(targetConceptionKind,targetConceptionEntityUID);
        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void renderRelatedRelationEntityUI(){
        String relationKindName = this.currentRelationKindName;
        String relationEntityUID = this.currentRelationEntityUID;

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("10px");
        titleDetailLayout.add(relationKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(relationKindName);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon relationEntityIcon = VaadinIcon.KEY_O.create();
        relationEntityIcon.setSize("10px");
        titleDetailLayout.add(relationEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel relationEntityUIDLabel = new NativeLabel(relationEntityUID);
        titleDetailLayout.add(relationEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        RelationEntityDetailUI relationEntityDetailUI = new RelationEntityDetailUI(relationKindName,relationEntityUID);
        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"关系实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationEntityDetailUI);
        relationEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    public void cleanAbstractInfo(){
        this.infoContentContainer.setVisible(false);
    }

    public void setEntityAttributesInfoGridHeight(int heightValue){
        entityAttributesInfoGrid.setHeight(heightValue,Unit.PIXELS);
    }

    public void renderRelationEntitySyntheticAbstractInfo(String relationKindName,String relationEntityUID){
        this.conceptionKindNameInfoContainer.setVisible(false);
        this.conceptionEntityUIDInfoContainer.setVisible(false);
        this.titleLayout1.setVisible(false);
        this.titleLayout2.setVisible(false);
        this.titleLayout3.setVisible(false);
        this.titleLayout4.setVisible(false);
        this.relationKindNameInfoContainer.setVisible(true);
        this.relationEntityUIDInfoContainer.setVisible(true);
        this.relationConceptionEntityInfoLayout.setVisible(true);

        this.infoContentContainer.setVisible(true);
        this.currentRelationKindName = relationKindName;
        this.currentRelationEntityUID = relationEntityUID;
        this.relationKindLabel.setText(this.currentRelationKindName);
        this.relationEntityUIDLabel.setText(this.currentRelationEntityUID);
        this.entityAttributesInfoGrid.setItems(new ArrayList<>());
        this.relationConceptionEntityInfoLayout.removeAll();
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();

        RelationKind targetRelationKind = coreRealm.getRelationKind(this.currentRelationKindName);
        if(targetRelationKind != null){
            RelationEntity targetEntity = targetRelationKind.getEntityByUID(this.currentRelationEntityUID);
            if(targetEntity != null){
                List<AttributeValue> allAttributesList = targetEntity.getAttributes();
                entityAttributesInfoGrid.setItems(allAttributesList);

                String fromConceptionEntityUID = targetEntity.getFromConceptionEntityUID();
                List<String> fromConceptionKinds = targetEntity.getFromConceptionEntityKinds();
                String toConceptionEntityUID = targetEntity.getToConceptionEntityUID();
                List<String> toConceptionKinds = targetEntity.getToConceptionEntityKinds();

                HorizontalLayout fromConceptionEntityInfo = new HorizontalLayout();
                Icon conceptionKindIcon0 = VaadinIcon.CUBE.create();
                conceptionKindIcon0.setSize("8px");
                fromConceptionEntityInfo.add(conceptionKindIcon0);
                fromConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindIcon0);
                NativeLabel fromConceptionKind = new NativeLabel(fromConceptionKinds.get(0));
                fromConceptionKind.addClassNames("border-b","border-contrast-10");
                fromConceptionKind.getStyle().set("font-size","12px");
                fromConceptionEntityInfo.add(fromConceptionKind);
                fromConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,fromConceptionKind);
                Icon divIcon = VaadinIcon.ITALIC.create();
                divIcon.setSize("8px");
                fromConceptionEntityInfo.add(divIcon);
                fromConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,divIcon);
                Icon conceptionEntityIcon0 = VaadinIcon.KEY_O.create();
                conceptionEntityIcon0.setSize("8px");
                fromConceptionEntityInfo.add(conceptionEntityIcon0);
                fromConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityIcon0);
                NativeLabel fromConceptionEntityUIDLabel = new NativeLabel(fromConceptionEntityUID);
                fromConceptionEntityUIDLabel.addClassNames("border-b","border-contrast-10");
                fromConceptionEntityUIDLabel.getStyle().set("font-size","12px");
                fromConceptionEntityInfo.add(fromConceptionEntityUIDLabel);
                fromConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,fromConceptionEntityUIDLabel);
                relationConceptionEntityInfoLayout.add(fromConceptionEntityInfo);

                Icon relationIcon = VaadinIcon.ANGLE_DOUBLE_DOWN.create();
                relationIcon.setSize("14px");
                relationIcon.getStyle().set("padding-left","5px");
                relationConceptionEntityInfoLayout.add(relationIcon);
                relationConceptionEntityInfoLayout.setHorizontalComponentAlignment(Alignment.START,relationIcon);

                HorizontalLayout toConceptionEntityInfo = new HorizontalLayout();
                Icon conceptionKindIcon1 = VaadinIcon.CUBE.create();
                conceptionKindIcon1.setSize("8px");
                toConceptionEntityInfo.add(conceptionKindIcon1);
                toConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindIcon1);
                NativeLabel toConceptionKind = new NativeLabel(toConceptionKinds.get(0));
                toConceptionKind.addClassNames("border-b","border-contrast-10");
                toConceptionKind.getStyle().set("font-size","12px");
                toConceptionEntityInfo.add(toConceptionKind);
                toConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,toConceptionKind);
                Icon divIcon1 = VaadinIcon.ITALIC.create();
                divIcon1.setSize("8px");
                toConceptionEntityInfo.add(divIcon1);
                toConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,divIcon1);
                Icon conceptionEntityIcon1 = VaadinIcon.KEY_O.create();
                conceptionEntityIcon1.setSize("8px");
                toConceptionEntityInfo.add(conceptionEntityIcon1);
                toConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityIcon1);
                NativeLabel toConceptionEntityUIDLabel = new NativeLabel(toConceptionEntityUID);
                toConceptionEntityUIDLabel.addClassNames("border-b","border-contrast-10");
                toConceptionEntityUIDLabel.getStyle().set("font-size","12px");
                toConceptionEntityInfo.add(toConceptionEntityUIDLabel);
                toConceptionEntityInfo.setVerticalComponentAlignment(Alignment.CENTER,toConceptionEntityUIDLabel);
                relationConceptionEntityInfoLayout.add(toConceptionEntityInfo);
            }
        }
        coreRealm.closeGlobalSession();
    }
}
