package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntityStatisticsInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.eventHandling.ConceptionKindConfigurationInfoRefreshEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;
import dev.mett.vaadin.tooltip.Tooltips;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.RelationKind;

public class RelationKindDetailUI extends VerticalLayout implements
        BeforeEnterObserver {

    private String relationKind;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private int relationKindDetailViewHeightOffset = 110;
    private Registration listener;
    private int currentBrowserHeight = 0;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private Grid<KindEntityAttributeRuntimeStatistics> conceptionKindAttributesInfoGrid;

    public RelationKindDetailUI(){}

    public RelationKindDetailUI(String relationKind){
        this.relationKind = relationKind;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.relationKind = beforeEnterEvent.getRouteParameters().get("relationKind").get();
        this.relationKindDetailViewHeightOffset = 45;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderConceptionKindData();
        //loadConceptionKindInfoData();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int chartHeight = currentBrowserHeight - relationKindDetailViewHeightOffset - 340;
            //conceptionKindCorrelationInfoChart.setHeight(chartHeight, Unit.PIXELS);
            //this.conceptionRelationRealtimeInfoGrid.setHeight(chartHeight,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            //kindCorrelationInfoTabSheet.setHeight(currentBrowserHeight-relationKindDetailViewHeightOffset-290,Unit.PIXELS);
        }));
       // renderKindCorrelationInfoTabContent();
        //ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderConceptionKindData(){
        List<Component> secTitleElementsList = new ArrayList<>();

        Label conceptionKindNameLabel = new Label(this.relationKind);
        conceptionKindNameLabel.getStyle()
                .set("font-size","var(--lumo-font-size-xl)")
                .set("color","var(--lumo-primary-text-color)")
                .set("fount-weight","bold");
        secTitleElementsList.add(conceptionKindNameLabel);

        this.kindDescriptionEditorItemWidget = new KindDescriptionEditorItemWidget(this.relationKind,RelationKind);
        secTitleElementsList.add(this.kindDescriptionEditorItemWidget);

        List<Component> buttonList = new ArrayList<>();

        Button refreshConceptionKindConfigInfoButton= new Button("刷新关系类型配置信息");
        refreshConceptionKindConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshConceptionKindConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshConceptionKindConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                ConceptionKindConfigurationInfoRefreshEvent conceptionKindConfigurationInfoRefreshEvent = new ConceptionKindConfigurationInfoRefreshEvent();
                conceptionKindConfigurationInfoRefreshEvent.setConceptionKindName(relationKind);
                ResourceHolder.getApplicationBlackboard().fire(conceptionKindConfigurationInfoRefreshEvent);
            }
        });
        buttonList.add(refreshConceptionKindConfigInfoButton);

        Button queryConceptionKindButton= new Button("关系类型实体数据查询");
        queryConceptionKindButton.setIcon(VaadinIcon.RECORDS.create());
        queryConceptionKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        queryConceptionKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderConceptionKindQueryUI();
            }
        });
        buttonList.add(queryConceptionKindButton);

        Button conceptionKindMetaInfoButton= new Button("关系类型元数据");
        conceptionKindMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        conceptionKindMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        conceptionKindMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderShowMetaInfoUI();
            }
        });
        buttonList.add(conceptionKindMetaInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"Relation Kind 关系类型  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);

        mainContainerLayout.add(leftSideContainerLayout);
        leftSideContainerLayout.setWidth(800, Unit.PIXELS);
        leftSideContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"关系类型属性分布 (实体概略采样数 "+10000+")");
        infoTitle1.getStyle().set("padding-bottom","5px");

        leftSideContainerLayout.add(infoTitle1);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            Icon queryIcon = new Icon(VaadinIcon.INPUT);
            queryIcon.setSize("20px");
            Button addAsAttributeKind = new Button(queryIcon, event -> {
                if(entityStatisticsInfo instanceof EntityStatisticsInfo){
                    //renderConceptionKindQueryUI((EntityStatisticsInfo)entityStatisticsInfo);
                }
            });
            addAsAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addAsAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Tooltips.getCurrent().setTooltip(addAsAttributeKind, "添加为属性类型");

            HorizontalLayout buttons = new HorizontalLayout(addAsAttributeKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(10,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        conceptionKindAttributesInfoGrid = new Grid<>();
        conceptionKindAttributesInfoGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeName).setHeader("属性名称").setKey("idx_0");
        conceptionKindAttributesInfoGrid.addColumn(KindEntityAttributeRuntimeStatistics::getAttributeDataType).setHeader("属性数据类型").setKey("idx_1").setFlexGrow(0).setWidth("130px").setResizable(false);
        conceptionKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getSampleCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
                .setHeader("属性采样数").setKey("idx_2")
                .setFlexGrow(0).setWidth("100px").setResizable(false);
        conceptionKindAttributesInfoGrid.addColumn(new NumberRenderer<>(KindEntityAttributeRuntimeStatistics::getAttributeHitCount, NumberFormat.getIntegerInstance()))
                .setComparator((entityStatisticsInfo1, entityStatisticsInfo2) ->
                        (int)(entityStatisticsInfo1.getAttributeHitCount() - entityStatisticsInfo2.getAttributeHitCount()))
                .setHeader("属性命中数").setKey("idx_3")
                .setFlexGrow(0).setWidth("100px").setResizable(false);
        conceptionKindAttributesInfoGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("60px").setResizable(false);
        /*
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true)
                .setTooltipGenerator(kindEntityAttributeRuntimeStatistics -> getAttributeName(kindEntityAttributeRuntimeStatistics));
        */

        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.PASSWORD,"属性数据类型");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.EYEDROPPER,"属性采样数");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.CROSSHAIRS,"属性命中数");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindAttributesInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);

        conceptionKindAttributesInfoGrid.setHeight(218,Unit.PIXELS);
        leftSideContainerLayout.add(conceptionKindAttributesInfoGrid);


    }



}
