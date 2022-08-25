package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;

import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntitySyntheticAbstractInfoView extends VerticalLayout{

    private VerticalLayout infoContentContainer;
    private Label conceptionKindLabel;
    private Label conceptionEntityUIDLabel;
    private SecondaryKeyValueDisplayItem relationCountDisplayItem;
    private SecondaryKeyValueDisplayItem inDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem outDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem isDenseDisplayItem;
    private SecondaryKeyValueDisplayItem currentExplorePageNumberDisplayItem;
    private String currentConceptionKindName;
    private String currentConceptionEntityUID;
    private Grid<AttributeValue> entityAttributesInfoGrid;

    public ConceptionEntitySyntheticAbstractInfoView(){
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
        add(infoContentContainer);

        HorizontalLayout conceptionKindNameInfoContainer = new HorizontalLayout();
        infoContentContainer.add(conceptionKindNameInfoContainer);
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("16px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        conceptionKindNameInfoContainer.add(conceptionKindIcon);
        conceptionKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindIcon);
        conceptionKindLabel = new Label();
        conceptionKindLabel.addClassNames("text-s","font-extrabold","border-b","border-contrast-10");
        conceptionKindLabel.getStyle().set("font-weight","bold").set("color","#2e4e7e");
        conceptionKindNameInfoContainer.getStyle().set("padding-top","15px");
        conceptionKindNameInfoContainer.add(conceptionKindLabel);
        conceptionKindNameInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindLabel);

        HorizontalLayout conceptionEntityUIDInfoContainer = new HorizontalLayout();
        infoContentContainer.add(conceptionEntityUIDInfoContainer);
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px");
        conceptionEntityUIDInfoContainer.add(conceptionEntityIcon);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityIcon);
        conceptionEntityUIDLabel = new Label();
        conceptionEntityUIDLabel.addClassNames("text-s","font-extrabold","border-b","border-contrast-10");
        conceptionEntityUIDLabel.getStyle().set("font-weight","bold").set("color","#2e4e7e");
        conceptionEntityUIDInfoContainer.getStyle().set("padding-top","5px");
        conceptionEntityUIDInfoContainer.add(conceptionEntityUIDLabel);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,conceptionEntityUIDLabel);

        Button showDetailButton = new Button("显示概念实体详情");
        showDetailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        showDetailButton.getStyle().set("font-size","12px");
        showDetailButton.setIcon(VaadinIcon.EYE.create());
        Tooltips.getCurrent().setTooltip(showDetailButton, "显示概念实体详情");
        showDetailButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedConceptionEntityUI();
            }
        });
        conceptionEntityUIDInfoContainer.add(showDetailButton);
        conceptionEntityUIDInfoContainer.setVerticalComponentAlignment(Alignment.CENTER,showDetailButton);
        conceptionEntityUIDInfoContainer.getStyle().set("padding-bottom", "10px");

        HorizontalLayout titleLayout0 = new HorizontalLayout();
        titleLayout0.getStyle().set("padding-bottom","8px");
        infoContentContainer.add(titleLayout0);
        currentExplorePageNumberDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout0, VaadinIcon.ABACUS.create(), "当前关联查询分页", "-");

        Button backPageButton = new Button();
        backPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        backPageButton.getStyle().set("font-size","8px");
        backPageButton.setIcon(VaadinIcon.ANGLE_LEFT.create());
        Tooltips.getCurrent().setTooltip(backPageButton, "显示概念实体详情");
        titleLayout0.add(backPageButton);
        titleLayout0.setVerticalComponentAlignment(Alignment.AUTO,backPageButton);


        HorizontalLayout titleLayout1 = new HorizontalLayout();
        infoContentContainer.add(titleLayout1);
        relationCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout1, VaadinIcon.LIST_OL.create(), "关联关系总量", "-");
        HorizontalLayout titleLayout2 = new HorizontalLayout();
        infoContentContainer.add(titleLayout2);
        inDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout2, VaadinIcon.ANGLE_DOUBLE_LEFT.create(), "关系入度", "-");
        HorizontalLayout titleLayout3 = new HorizontalLayout();
        infoContentContainer.add(titleLayout3);
        outDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout3, VaadinIcon.ANGLE_DOUBLE_RIGHT.create(), "关系出度", "-");
        HorizontalLayout titleLayout4 = new HorizontalLayout();
        infoContentContainer.add(titleLayout4);
        isDenseDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout4, VaadinIcon.BULLSEYE.create(), "是否稠密实体", "-");

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"实体属性");
        infoTitle1.getStyle().set("padding-top","25px");
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
        entityAttributesInfoGrid.setHeight(500,Unit.PIXELS);
        infoContentContainer.add(entityAttributesInfoGrid);
    }

    public void renderConceptionEntitySyntheticAbstractInfo(String conceptionKindName,String conceptionEntityUID,int currentRelationQueryPage){
        this.currentConceptionKindName = conceptionKindName;
        this.currentConceptionEntityUID = conceptionEntityUID;
        this.conceptionKindLabel.setText(conceptionKindName);
        this.conceptionEntityUIDLabel.setText(conceptionEntityUID);
        this.currentExplorePageNumberDisplayItem.updateDisplayValue(""+currentRelationQueryPage);

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
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,targetConceptionKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,targetConceptionEntityUID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        actionComponentList.add(entityInfoFootprintMessageBar);

        ConceptionEntityDetailView conceptionEntityDetailView = new ConceptionEntityDetailView(targetConceptionKind,targetConceptionEntityUID);
        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailView);
        conceptionEntityDetailView.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }
}
