package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis.ConceptionEntityPathTravelableView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology.ConceptionEntityRelationsChart;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology.EntitySyntheticAbstractInfoView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology.RelatedConceptionEntitiesDandelionGraphInfoView;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.topology.RelatedConceptionEntitiesKindStaticInfoView;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntityRelationTopologyView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private SecondaryKeyValueDisplayItem relationEntityCountDisplayItem;
    private SecondaryKeyValueDisplayItem conceptionEntityCountDisplayItem;
    private NativeLabel currentPageIndexValue;
    private int conceptionEntityRelationInfoViewHeightOffset;
    private ConceptionEntityRelationsChart conceptionEntityRelationsChart;
    private Registration listener;
    private Button deleteSingleEntityButton;
    private Button deleteEntitiesButton;
    private Button expendEntityRelationButton;
    private Button compressEntityRelationButton;
    private Button resetPageIndexButton;
    private Button goBackOnePageIndexButton;
    private Button goForwardOnePageIndexButton;
    private EntitySyntheticAbstractInfoView entitySyntheticAbstractInfoView;
    private int currentSelectedConceptionEntityRelationQueryIndex = 1;

    public ConceptionEntityRelationTopologyView(String conceptionKind,String conceptionEntityUID,int conceptionEntityIntegratedInfoViewHeightOffset) {
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityRelationInfoViewHeightOffset = conceptionEntityIntegratedInfoViewHeightOffset+100;
        List<Component> secondaryTitleComponentsList = new ArrayList<>();
        List<Component> actionComponentsList = new ArrayList<>();

        HorizontalLayout graphExploreActionButtonContainer = new HorizontalLayout();
        graphExploreActionButtonContainer.setPadding(false);
        graphExploreActionButtonContainer.setSpacing(false);
        graphExploreActionButtonContainer.setMargin(false);

        Button reloadConceptionEntitiesInfoButton = new Button();
        reloadConceptionEntitiesInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadConceptionEntitiesInfoButton.setTooltipText("重新加载关系图");
        reloadConceptionEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.reload();
                disableControlActionButtons();
                entitySyntheticAbstractInfoView.cleanAbstractInfo();
            }
        });
        reloadConceptionEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(reloadConceptionEntitiesInfoButton);

        deleteSingleEntityButton = new Button();
        deleteSingleEntityButton.setIcon(VaadinIcon.DEL_A.create());
        deleteSingleEntityButton.setTooltipText("隐藏选中的概念实体");
        deleteSingleEntityButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.deleteSelectedConceptionEntity();
                disableControlActionButtons();
                entitySyntheticAbstractInfoView.cleanAbstractInfo();
            }
        });
        deleteSingleEntityButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(deleteSingleEntityButton);
        deleteSingleEntityButton.setEnabled(false);

        deleteEntitiesButton = new Button();
        deleteEntitiesButton.setIcon(VaadinIcon.DEL.create());
        deleteEntitiesButton.setTooltipText("隐藏选中的以及与其一度关联的所有概念实体");
        deleteEntitiesButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.deleteSelectedAndDirectlyRelatedConceptionEntities();
                disableControlActionButtons();
                entitySyntheticAbstractInfoView.cleanAbstractInfo();
            }
        });
        deleteEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(deleteEntitiesButton);
        deleteEntitiesButton.setEnabled(false);

        expendEntityRelationButton = new Button();
        expendEntityRelationButton.setIcon(VaadinIcon.EXPAND_SQUARE.create());
        expendEntityRelationButton.setTooltipText("一度展开与选中概念实体关联的部分概念实体");
        expendEntityRelationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.expandSelectedEntityOneDegreeRelations();
            }
        });
        expendEntityRelationButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(expendEntityRelationButton);
        expendEntityRelationButton.setEnabled(false);

        compressEntityRelationButton = new Button();
        compressEntityRelationButton.setIcon(VaadinIcon.COMPRESS_SQUARE.create());
        compressEntityRelationButton.setTooltipText("隐藏与选中概念实体一度关联的所有其他概念实体");
        compressEntityRelationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.deleteOneDegreeRelatedConceptionEntitiesOfSelectedConceptionEntity();
                currentPageIndexValue.setText("1");
            }
        });
        compressEntityRelationButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(compressEntityRelationButton);
        compressEntityRelationButton.setEnabled(false);

        Icon divIcon = VaadinIcon.LINE_V.create();
        divIcon.setSize("12px");
        graphExploreActionButtonContainer.add(divIcon);
        graphExploreActionButtonContainer.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        resetPageIndexButton = new Button();
        resetPageIndexButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());
        resetPageIndexButton.setTooltipText("将选中概念实体的关联查询分页重置为第一页");
        resetPageIndexButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.resetConceptionEntityRelationQueryPageIndex();
                currentPageIndexValue.setText("1");
            }
        });
        resetPageIndexButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(resetPageIndexButton);
        resetPageIndexButton.setEnabled(false);

        goBackOnePageIndexButton = new Button();
        goBackOnePageIndexButton.setIcon(VaadinIcon.ARROW_CIRCLE_LEFT_O.create());
        goBackOnePageIndexButton.setTooltipText("将选中概念实体的关联查询分页减小一页");
        goBackOnePageIndexButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.minusConceptionEntityRelationQueryPageIndex();
                if(currentSelectedConceptionEntityRelationQueryIndex-1>0){
                    currentSelectedConceptionEntityRelationQueryIndex =currentSelectedConceptionEntityRelationQueryIndex -1;
                    currentPageIndexValue.setText(""+currentSelectedConceptionEntityRelationQueryIndex);
                }else{
                    currentPageIndexValue.setText("1");
                }
            }
        });
        goBackOnePageIndexButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(goBackOnePageIndexButton);
        goBackOnePageIndexButton.setEnabled(false);

        goForwardOnePageIndexButton = new Button();
        goForwardOnePageIndexButton.setIcon(VaadinIcon.ARROW_CIRCLE_RIGHT_O.create());
        goForwardOnePageIndexButton.setTooltipText("将选中概念实体的关联查询分页增加一页");
        goForwardOnePageIndexButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.addConceptionEntityRelationQueryPageIndex();
                currentSelectedConceptionEntityRelationQueryIndex = currentSelectedConceptionEntityRelationQueryIndex+1;
                currentPageIndexValue.setText(""+(currentSelectedConceptionEntityRelationQueryIndex));
            }
        });
        goForwardOnePageIndexButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(goForwardOnePageIndexButton);
        goForwardOnePageIndexButton.setEnabled(false);

        secondaryTitleComponentsList.add(graphExploreActionButtonContainer);

        HorizontalLayout titleLayout = new HorizontalLayout();
        secondaryTitleComponentsList.add(titleLayout);

        Icon currentPageIndexIcon = VaadinIcon.ABACUS.create();
        currentPageIndexIcon.setSize("8px");
        currentPageIndexIcon.addClassNames("text-tertiary");
        titleLayout.add(currentPageIndexIcon);
        titleLayout.setVerticalComponentAlignment(Alignment.CENTER,currentPageIndexIcon);
        currentPageIndexIcon.setTooltipText("选中概念实体的当前关联查询分页");

        currentPageIndexValue = new NativeLabel("-");
        currentPageIndexValue.getStyle().set("font-size","10px").set("padding-right","30px");
        currentPageIndexValue.addClassNames("text-tertiary");
        titleLayout.add(currentPageIndexValue);
        titleLayout.setVerticalComponentAlignment(Alignment.CENTER,currentPageIndexValue);

        conceptionEntityCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.CIRCLE_THIN.create(), "当前显示概念实体总量", "-");
        relationEntityCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.EXPAND.create(), "当前显示关系实体总量", "-");

        NativeLabel selectMethodMessage = new NativeLabel("单击选中实体，双击概念实体获取其一度关联实体信息展开");
        selectMethodMessage.getStyle().set("font-size","10px").set("padding-left","30px");
        selectMethodMessage.addClassNames("text-tertiary");
        titleLayout.add(selectMethodMessage);
        titleLayout.setVerticalComponentAlignment(Alignment.CENTER,selectMethodMessage);

        Button conceptionEntitiesStaticInfoButton = new Button("关联概念实体类型分布");
        conceptionEntitiesStaticInfoButton.setIcon(VaadinIcon.PIE_CHART.create());
        conceptionEntitiesStaticInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        conceptionEntitiesStaticInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedConceptionEntitiesKindStaticInfo();
            }
        });
        actionComponentsList.add(conceptionEntitiesStaticInfoButton);

        Button fullRelationsInfoButton = new Button("关联实体全量蒲公英图");
        fullRelationsInfoButton.setIcon(VaadinIcon.ASTERISK.create());
        fullRelationsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        fullRelationsInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelatedConceptionEntitiesDandelionGraphInfo();
            }
        });
        actionComponentsList.add(fullRelationsInfoButton);

        MenuBar starterPathAnalyOperationMenuBar = new MenuBar();
        starterPathAnalyOperationMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY,MenuBarVariant.LUMO_ICON,MenuBarVariant.LUMO_SMALL);
        starterPathAnalyOperationMenuBar.getStyle().set("top","0px").set("position","relative");

        Icon starterPathAnalysisMenuIcon = LineAwesomeIconsSvg.DRAW_POLYGON_SOLID.create();
        starterPathAnalysisMenuIcon.setSize("16px");
        MenuItem starterPathAnalysisMenuItem = starterPathAnalyOperationMenuBar.addItem(starterPathAnalysisMenuIcon);
        starterPathAnalysisMenuItem.add("起始点路径分析");
        Icon downArrowIcon = new Icon(VaadinIcon.CHEVRON_DOWN);
        downArrowIcon.setSize("12px");
        starterPathAnalysisMenuItem.add(downArrowIcon);
        starterPathAnalysisMenuItem.getStyle().set("font-size","1em");
        actionComponentsList.add(starterPathAnalyOperationMenuBar);

        SubMenu starterPathAnalysisSubMenu = starterPathAnalysisMenuItem.getSubMenu();
        MenuItem expandSubGraphDataSubMenu = starterPathAnalysisSubMenu.addItem(LineAwesomeIconsSvg.HUBSPOT.create());
        expandSubGraphDataSubMenu.add(" 扩展子图");
        expandSubGraphDataSubMenu.addClickListener(new ComponentEventListener<ClickEvent<MenuItem>>() {
            @Override
            public void onComponentEvent(ClickEvent<MenuItem> menuItemClickEvent) {
                renderConceptionEntityExpandGraphInfo();
            }
        });

        Icon relationsIcon = VaadinIcon.AIRPLANE.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon, "关联关系探索: ", secondaryTitleComponentsList, actionComponentsList);
        add(secondaryTitleActionBar);

        HorizontalLayout relationEntitiesDetailLayout = new HorizontalLayout();
        relationEntitiesDetailLayout.setWidthFull();
        add(relationEntitiesDetailLayout);

        this.conceptionEntityRelationsChart = new ConceptionEntityRelationsChart(this.conceptionKind,this.conceptionEntityUID);
        this.conceptionEntityRelationsChart.setContainerConceptionEntityRelationTopologyView(this);
        relationEntitiesDetailLayout.add(this.conceptionEntityRelationsChart);

        VerticalLayout selectedEntityInfoContainerLayout = new VerticalLayout();
        selectedEntityInfoContainerLayout.setSpacing(false);
        selectedEntityInfoContainerLayout.setMargin(false);
        selectedEntityInfoContainerLayout.getStyle().set("padding-right","0px");
        selectedEntityInfoContainerLayout.setWidth(330,Unit.PIXELS);
        selectedEntityInfoContainerLayout.getStyle()
                .set("border-left", "1px solid var(--lumo-contrast-20pct)");
        relationEntitiesDetailLayout.add(selectedEntityInfoContainerLayout);
        relationEntitiesDetailLayout.setFlexGrow(1,this.conceptionEntityRelationsChart);

        this.entitySyntheticAbstractInfoView = new EntitySyntheticAbstractInfoView(330);
        selectedEntityInfoContainerLayout.add(this.entitySyntheticAbstractInfoView);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            conceptionEntityRelationsChart.setHeight((event.getHeight()-this.conceptionEntityRelationInfoViewHeightOffset)+55, Unit.PIXELS);
            entitySyntheticAbstractInfoView.setEntityAttributesInfoGridHeight(event.getHeight()-this.conceptionEntityRelationInfoViewHeightOffset-160);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            conceptionEntityRelationsChart.setHeight((browserHeight-this.conceptionEntityRelationInfoViewHeightOffset+55),Unit.PIXELS);
            entitySyntheticAbstractInfoView.setEntityAttributesInfoGridHeight(browserHeight-this.conceptionEntityRelationInfoViewHeightOffset-160);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void loadEntityRelationNetworks(){
        this.conceptionEntityRelationsChart.initLoadTargetConceptionEntityRelationData();
    }

    public void disableControlActionButtons() {
        deleteSingleEntityButton.setEnabled(false);
        deleteEntitiesButton.setEnabled(false);
        compressEntityRelationButton.setEnabled(false);
        expendEntityRelationButton.setEnabled(false);
        resetPageIndexButton.setEnabled(false);
        goBackOnePageIndexButton.setEnabled(false);
        goForwardOnePageIndexButton.setEnabled(false);
        currentPageIndexValue.setText("-");
    }

    public void enableControlActionButtons(int pageIndex) {
        deleteSingleEntityButton.setEnabled(true);
        deleteEntitiesButton.setEnabled(true);
        compressEntityRelationButton.setEnabled(true);
        expendEntityRelationButton.setEnabled(true);
        resetPageIndexButton.setEnabled(true);
        goBackOnePageIndexButton.setEnabled(true);
        goForwardOnePageIndexButton.setEnabled(true);
        currentPageIndexValue.setText(""+pageIndex);
        currentSelectedConceptionEntityRelationQueryIndex = pageIndex;
    }

    public void updateEntitiesMetaStaticInfo(int totalConceptionEntityNumber,int totalRelationEntityNumber){
        relationEntityCountDisplayItem.updateDisplayValue(""+totalRelationEntityNumber);
        conceptionEntityCountDisplayItem.updateDisplayValue(""+totalConceptionEntityNumber);
    }

    public void renderSelectedConceptionEntityAbstractInfo(String entityType,String entityUID){
        entitySyntheticAbstractInfoView.renderConceptionEntitySyntheticAbstractInfo(entityType,entityUID);
    }

    public void clearConceptionEntityAbstractInfo(){
        entitySyntheticAbstractInfoView.cleanAbstractInfo();
    }

    public void renderSelectedRelationEntityAbstractInfo(String entityType,String entityUID){
        entitySyntheticAbstractInfoView.renderRelationEntitySyntheticAbstractInfo(entityType,entityUID);
    }

    public void clearRelationEntityAbstractInfo(){
        entitySyntheticAbstractInfoView.cleanAbstractInfo();
    }

    private void renderRelatedConceptionEntitiesKindStaticInfo(){
        String targetConceptionKind = this.conceptionKind;
        String targetConceptionEntityUID = this.conceptionEntityUID;
        RelatedConceptionEntitiesKindStaticInfoView relatedConceptionEntitiesKindStaticInfoView = new RelatedConceptionEntitiesKindStaticInfoView(targetConceptionKind,targetConceptionEntityUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PIE_CHART),"关联概念实体类型分布",null,true,1300,480,false);
        fixSizeWindow.setWindowContent(relatedConceptionEntitiesKindStaticInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderRelatedConceptionEntitiesDandelionGraphInfo(){
        RelatedConceptionEntitiesDandelionGraphInfoView relatedConceptionEntitiesDandelionGraphInfoView =
                new RelatedConceptionEntitiesDandelionGraphInfoView(this.conceptionKind,this.conceptionEntityUID);
        FullScreenWindow.CloseFullScreenWindowListener closeFullScreenWindowListener = new FullScreenWindow.CloseFullScreenWindowListener() {
            @Override
            public void beforeCloseWindow() {
                relatedConceptionEntitiesDandelionGraphInfoView.cleanGraphResource();
            }
        };
        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.ASTERISK),"关联实体全量蒲公英图",null,null,closeFullScreenWindowListener);
        fullScreenWindow.setWindowContent(relatedConceptionEntitiesDandelionGraphInfoView);
        fullScreenWindow.show();
    }

    private void renderConceptionEntityExpandGraphInfo(){
        ConceptionEntityPathTravelableView conceptionEntityPathTravelableView = new ConceptionEntityPathTravelableView(this.conceptionKind,this.conceptionEntityUID);
        FullScreenWindow.CloseFullScreenWindowListener closeFullScreenWindowListener = new FullScreenWindow.CloseFullScreenWindowListener() {
            @Override
            public void beforeCloseWindow() {
                //conceptionEntityExpandGraphView.cleanGraphResource();
            }
        };
        FullScreenWindow fullScreenWindow = new FullScreenWindow(LineAwesomeIconsSvg.HUBSPOT.create(),"由概念实体出发扩展子图",null,null,closeFullScreenWindowListener);
        fullScreenWindow.setWindowContent(conceptionEntityPathTravelableView);
        fullScreenWindow.show();
    }
}
