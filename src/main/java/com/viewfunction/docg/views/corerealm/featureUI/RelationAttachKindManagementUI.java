package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationAttachKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.ConceptionKindCorrelationInfoChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RelationAttachKindManagementUI extends VerticalLayout {

    private Grid<RelationAttachKind> relationAttachKindGrid;
    private RelationAttachKind lastSelectedRelationAttachKind;
    private VerticalLayout singleConceptionKindSummaryInfoContainerLayout;
    private VerticalLayout selectKindPromptInfoContainerLayout;
    private Registration listener;



    private int screenAreaWidth = 1200;
    private int screenAreaHeight = 900;

    public RelationAttachKindManagementUI() {
        Button refreshDataButton = new Button("刷新关系附着规则类型数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            //loadConceptionKindsInfo();
            //resetSingleConceptionKindSummaryInfoArea();
            //selectKindPromptInfoContainerLayout.setVisible(true);
            //singleConceptionKindSummaryInfoContainerLayout.setVisible(false);
        });

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        String coreRealmName = coreRealm.getCoreRealmName();

        Icon realmIcon = VaadinIcon.ARCHIVE.create();
        realmIcon.getStyle().set("padding", "var(--lumo-space-xs");
        Span realmNameSpan = new Span( realmIcon,new Span(coreRealmName));
        realmNameSpan.addClassName("text-2xs");
        realmNameSpan.getElement().getThemeList().add("badge contrast");
        secTitleElementsList.add(realmNameSpan);

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Relation Attach Kind 关系附着规则类型数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> relationAttachKindManagementOperationButtonList = new ArrayList<>();

        Button createRelationAttachKindButton = new Button("创建关系附着规则",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        relationAttachKindManagementOperationButtonList.add(createRelationAttachKindButton);
        createRelationAttachKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderCreateConceptionKindUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"关系附着规则类型:",relationAttachKindManagementOperationButtonList);
        add(sectionActionBar);








        HorizontalLayout conceptionKindsInfoContainerLayout = new HorizontalLayout();
        conceptionKindsInfoContainerLayout.setSpacing(false);
        conceptionKindsInfoContainerLayout.setMargin(false);
        conceptionKindsInfoContainerLayout.setWidth(100, Unit.PERCENTAGE);
        add(conceptionKindsInfoContainerLayout);

        VerticalLayout conceptionKindMetaInfoGridContainerLayout = new VerticalLayout();
        conceptionKindMetaInfoGridContainerLayout.setSpacing(true);
        conceptionKindMetaInfoGridContainerLayout.setMargin(false);
        conceptionKindMetaInfoGridContainerLayout.setPadding(false);

        HorizontalLayout conceptionKindsSearchElementsContainerLayout = new HorizontalLayout();
        conceptionKindsSearchElementsContainerLayout.setSpacing(false);
        conceptionKindsSearchElementsContainerLayout.setMargin(false);
        conceptionKindMetaInfoGridContainerLayout.add(conceptionKindsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        conceptionKindsSearchElementsContainerLayout.add(filterTitle);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80,Unit.PIXELS);

        TextField conceptionKindNameFilterField = new TextField();
        conceptionKindNameFilterField.setPlaceholder("概念类型名称");
        conceptionKindNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindNameFilterField.setWidth(250,Unit.PIXELS);
        conceptionKindsSearchElementsContainerLayout.add(conceptionKindNameFilterField);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        conceptionKindsSearchElementsContainerLayout.add(plusIcon);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        TextField conceptionKindDescFilterField = new TextField();
        conceptionKindDescFilterField.setPlaceholder("概念类型显示名称");
        conceptionKindDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        conceptionKindDescFilterField.setWidth(250,Unit.PIXELS);
        conceptionKindsSearchElementsContainerLayout.add(conceptionKindDescFilterField);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,conceptionKindDescFilterField);

        Button searchConceptionKindsButton = new Button("查找概念类型",new Icon(VaadinIcon.SEARCH));
        searchConceptionKindsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchConceptionKindsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindsSearchElementsContainerLayout.add(searchConceptionKindsButton);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchConceptionKindsButton);
        searchConceptionKindsButton.setWidth(115,Unit.PIXELS);
        searchConceptionKindsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //filterConceptionKinds();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        conceptionKindsSearchElementsContainerLayout.add(divIcon);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        conceptionKindsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        conceptionKindsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //cancelFilterConceptionKinds();
            }
        });

        //conceptionKindMetaInfoGridContainerLayout.add(conceptionKindMetaInfoGrid);

        conceptionKindsInfoContainerLayout.add(conceptionKindMetaInfoGridContainerLayout);





        ComponentRenderer _toolBarComponentRenderer1 = new ComponentRenderer<>(relationAttachKind -> {
            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeRelationAttachKindButton = new Button(deleteKindIcon, event -> {});
            removeRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeRelationAttachKindButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeRelationAttachKindButton.setTooltipText("删除关系附着规则类型");
            removeRelationAttachKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    //renderDeleteRelationAttachKindUI((RelationAttachKind)relationAttachKind);
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(removeRelationAttachKindButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        relationAttachKindGrid = new Grid<>();

        relationAttachKindGrid.setWidth(1300,Unit.PIXELS);
        relationAttachKindGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        relationAttachKindGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        relationAttachKindGrid.addColumn(RelationAttachKind::getRelationAttachKindName).setHeader("规则类型名称").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getRelationAttachKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::getSourceConceptionKindName).setHeader("源概念类型名称").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getSourceConceptionKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::getRelationKindName).setHeader("关系类型名称").setKey("idx_2").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getRelationKindName());
        relationAttachKindGrid.addColumn(RelationAttachKind::getTargetConceptionKindName).setHeader("目标概念类型名称").setKey("idx_3").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getTargetConceptionKindName());
        relationAttachKindGrid.addColumn(_toolBarComponentRenderer1).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("70px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"规则类型名称");
        relationAttachKindGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.LEVEL_LEFT,"源概念类型名称");
        relationAttachKindGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.CONNECT_O.create(),"关系类型名称");
        relationAttachKindGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.LEVEL_RIGHT,"目标概念类型名称");
        relationAttachKindGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        relationAttachKindGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);



        relationAttachKindGrid.appendFooterRow();
        conceptionKindMetaInfoGridContainerLayout.add(relationAttachKindGrid);


        relationAttachKindGrid.addSelectionListener(new SelectionListener<Grid<RelationAttachKind>, RelationAttachKind>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<RelationAttachKind>, RelationAttachKind> selectionEvent) {
                Set<RelationAttachKind> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    relationAttachKindGrid.select(lastSelectedRelationAttachKind);
                }else{
                    RelationAttachKind selectedRelationAttachKind = selectedItemSet.iterator().next();
                    lastSelectedRelationAttachKind = selectedRelationAttachKind;
                    //renderRelationAttachKindDetailInfo(selectedRelationAttachKind);
                }
            }
        });


        singleConceptionKindSummaryInfoContainerLayout = new VerticalLayout();
        singleConceptionKindSummaryInfoContainerLayout.setSpacing(true);
        singleConceptionKindSummaryInfoContainerLayout.setMargin(true);
        singleConceptionKindSummaryInfoContainerLayout.setPadding(false);
        conceptionKindsInfoContainerLayout.add(singleConceptionKindSummaryInfoContainerLayout);
        conceptionKindsInfoContainerLayout.setFlexGrow(1,singleConceptionKindSummaryInfoContainerLayout);



        selectKindPromptInfoContainerLayout = new VerticalLayout();
        selectKindPromptInfoContainerLayout.getStyle().set("padding-top", "50px");
        conceptionKindsInfoContainerLayout.add(selectKindPromptInfoContainerLayout);

        HorizontalLayout selectKindInfoMessage = new HorizontalLayout();
        selectKindInfoMessage.setSpacing(true);
        selectKindInfoMessage.setPadding(true);
        selectKindInfoMessage.setMargin(true);
        selectKindInfoMessage.setWidth(100,Unit.PERCENTAGE);
        selectKindInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 请从左侧列表选择关系附着规则类型显示详情");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        selectKindInfoMessage.add(messageLogo,messageLabel);
        selectKindPromptInfoContainerLayout.add(selectKindInfoMessage);

        HorizontalLayout singleConceptionKindInfoElementsContainerLayout = new HorizontalLayout();
        singleConceptionKindInfoElementsContainerLayout.setSpacing(false);
        singleConceptionKindInfoElementsContainerLayout.setMargin(false);
        singleConceptionKindInfoElementsContainerLayout.setHeight("30px");
        singleConceptionKindSummaryInfoContainerLayout.add(singleConceptionKindInfoElementsContainerLayout);



    }










    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
       // ResourceHolder.getApplicationBlackboard().addListener(this);
        //loadConceptionKindsInfo();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            relationAttachKindGrid.setHeight(event.getHeight()-250,Unit.PIXELS);
            screenAreaWidth = event.getWidth();
            screenAreaHeight = event.getHeight();
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            relationAttachKindGrid.setHeight(browserHeight-250,Unit.PIXELS);
            //conceptionKindCorrelationInfoChart = new ConceptionKindCorrelationInfoChart(browserHeight-600);
            //singleConceptionKindSummaryInfoContainerLayout.add(conceptionKindCorrelationInfoChart);
            screenAreaWidth = browserWidth;
            screenAreaHeight = browserHeight;
        }));
        renderRelationAttachKindsInfo();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }







    private void renderRelationAttachKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<RelationAttachKind>  relationAttachKindList = coreRealm.getRelationAttachKinds(null,null,null,null,null,null);
        List<RelationAttachKind> filteredRelationAttachKindList = new ArrayList<>();
        for(RelationAttachKind currentRelationAttachKind:relationAttachKindList){
            String sourceConceptionKindName = currentRelationAttachKind.getSourceConceptionKindName();
            String targetConceptionKindName = currentRelationAttachKind.getTargetConceptionKindName();
            String relationKindName = currentRelationAttachKind.getRelationKindName();
            filteredRelationAttachKindList.add(currentRelationAttachKind);

        }
        relationAttachKindGrid.setItems(filteredRelationAttachKindList);
    }

    public void refreshRelationAttachKindsInfo(){
        renderRelationAttachKindsInfo();
        //clearRelationAttachKindDetailInfo();
    }









}
