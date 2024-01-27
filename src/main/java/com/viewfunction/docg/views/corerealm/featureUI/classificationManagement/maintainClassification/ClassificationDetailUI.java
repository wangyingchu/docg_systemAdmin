package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ClassificationRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.InheritanceTree;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.eventHandling.ClassificationDetachedEvent;
import com.viewfunction.docg.element.eventHandling.ClassificationRemovedEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.CreateClassificationView;
import com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.DetachClassificationView;
import com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.RemoveClassificationView;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;

import java.util.*;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.Classification;

@Route("classificationDetailInfo/:classificationName")
public class ClassificationDetailUI extends VerticalLayout implements
        BeforeEnterObserver,
        ClassificationRemovedEvent.ClassificationRemovedListener,
        ClassificationDetachedEvent.ClassificationDetachedListener{
    private String classificationName;
    private int attributesViewKindDetailViewHeightOffset = 170;
    private int rightSideLayoutWidthOffset = 20;
    private int currentBrowserHeight = 0;
    private Registration listener;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private VerticalLayout middleContainerLayout;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private String parentClassificationName;
    private TreeGrid<ClassificationMetaInfo> classificationsMetaInfoTreeGrid;
    private Map<String,ClassificationMetaInfo> classificationMetaInfoMap;
    private List<ClassificationMetaInfo> allClassificationsMetaInfoList;
    private ClassificationRuntimeStatistics classificationRuntimeStatistics;
    private SecondaryKeyValueDisplayItem childClassificationCountDisplayItem;
    private SecondaryKeyValueDisplayItem offendClassificationCountDisplayItem;
    private RelatedConceptionKindsView relatedConceptionKindsView;
    private RelatedRelationKindsView relatedRelationKindsView;
    private RelatedAttributeKindsView relatedAttributeKindsView;
    private RelatedAttributesViewKindsView relatedAttributesViewKindsView;
    private RelatedConceptionEntitiesView relatedConceptionEntitiesView;
    private boolean relationKindClassificationInfoTabRendered = false;
    private boolean attributeKindClassificationInfoTabRendered = false;
    private boolean attributesViewKindClassificationInfoTabRendered = false;

    public ClassificationDetailUI(){}

    public ClassificationDetailUI(String classificationName){
        this.classificationName = classificationName;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.classificationName = beforeEnterEvent.getRouteParameters().get("classificationName").get();
        this.attributesViewKindDetailViewHeightOffset = 110;
        this.rightSideLayoutWidthOffset = 30;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        renderClassificationData();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int containerHeight = currentBrowserHeight - attributesViewKindDetailViewHeightOffset;
            int rightSideContainerLayoutWidth = event.getWidth() - 350-450 - rightSideLayoutWidthOffset;
            this.rightSideContainerLayout.setWidth(rightSideContainerLayoutWidth,Unit.PIXELS);
            this.relatedConceptionKindsView.setHeight(containerHeight-30);
            this.relatedRelationKindsView.setHeight(containerHeight-30);
            this.relatedAttributeKindsView.setHeight(containerHeight-30);
            this.relatedAttributesViewKindsView.setHeight(containerHeight-30);
            this.relatedConceptionEntitiesView.setHeight(containerHeight-30);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            int containerHeight = currentBrowserHeight - attributesViewKindDetailViewHeightOffset;
            int rightSideContainerLayoutWidth = receiver.getBodyClientWidth() - 350-450 - rightSideLayoutWidthOffset;
            this.rightSideContainerLayout.setWidth(rightSideContainerLayoutWidth,Unit.PIXELS);
            this.relatedConceptionKindsView.setHeight(containerHeight-30);
            this.relatedRelationKindsView.setHeight(containerHeight-30);
            this.relatedAttributeKindsView.setHeight(containerHeight-30);
            this.relatedAttributesViewKindsView.setHeight(containerHeight-30);
            this.relatedConceptionEntitiesView.setHeight(containerHeight-30);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
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

        List<Component> buttonList = new ArrayList<>();
        Button classificationMetaInfoButton= new Button("分类元数据");
        classificationMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        classificationMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        classificationMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderShowMetaInfoUI();
            }
        });
        buttonList.add(classificationMetaInfoButton);

        Icon divIcon1 = VaadinIcon.LINE_V.create();
        divIcon1.setSize("8px");
        buttonList.add(divIcon1);

        Button refreshClassificationConfigInfoButton= new Button("刷新分类配置信息");
        refreshClassificationConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshClassificationConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshClassificationConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //containerConceptionKindsConfigView.refreshConceptionKindsInfo();
                //containsAttributeKindsConfigView.refreshAttributeTypesInfo();
                //refreshAttributesViewKindCorrelationInfoChart();
            }
        });
        buttonList.add(refreshClassificationConfigInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAGS),"Classification 分类  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);
        leftSideContainerLayout.setWidth(450, Unit.PIXELS);
        leftSideContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        mainContainerLayout.add(leftSideContainerLayout);

        HorizontalLayout actionButtonBarContainer = new HorizontalLayout();
        actionButtonBarContainer.setSpacing(false);

        Button createNewChildClassificationButton= new Button("创建子分类");
        createNewChildClassificationButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        createNewChildClassificationButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        createNewChildClassificationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCreateClassificationUI();
            }
        });
        actionButtonBarContainer.add(createNewChildClassificationButton);

        SecondaryIconTitle viewTitle = new SecondaryIconTitle(VaadinIcon.SITEMAP.create(),"分类继承信息",actionButtonBarContainer);
        viewTitle.setHeight(39, Unit.PIXELS);
        leftSideContainerLayout.add(viewTitle);
        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        leftSideContainerLayout.add(spaceDivLayout);

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setHeight(10,Unit.PIXELS);
        leftSideContainerLayout.add(spaceDivLayout1);

        //ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(LineAwesomeIconsSvg.MALE_SOLID.create(),"父分类信息");
        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(VaadinIcon.MALE.create(),"父分类信息");
        leftSideContainerLayout.add(infoTitle);

        List<Component> actionComponentsList1 = new ArrayList<>();
        Button showParentClassificationButton= new Button();
        showParentClassificationButton.setTooltipText("显示父分类配置定义");
        showParentClassificationButton.setIcon(VaadinIcon.COG.create());
        showParentClassificationButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        showParentClassificationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderParentClassificationConfigurationUI();
            }
        });
        actionComponentsList1.add(showParentClassificationButton);

        String parentClassificationName = "-";
        String parentClassificationDesc = "-";
        String childClassificationCount = "-";
        String offspringClassificationsCount = "-";
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        if(targetClassification != null){
            this.classificationRuntimeStatistics = targetClassification.getClassificationRuntimeStatistics();
            childClassificationCount = ""+this.classificationRuntimeStatistics.getChildClassificationsCount();
            offspringClassificationsCount = ""+this.classificationRuntimeStatistics.getOffspringClassificationsCount();
            if(!targetClassification.isRootClassification()){
                Classification parentClassification = targetClassification.getParentClassification();
                this.parentClassificationName = parentClassification.getClassificationName();
                parentClassificationName = parentClassification.getClassificationName();
                parentClassificationDesc = parentClassification.getClassificationDesc();
            }else{
                showParentClassificationButton.setEnabled(false);
            }
        }

        SecondaryTitleActionBar secondaryTitleActionBar2 = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAG),parentClassificationName,null,actionComponentsList1,false);
        secondaryTitleActionBar2.setWidth(100,Unit.PERCENTAGE);
        leftSideContainerLayout.add(secondaryTitleActionBar2);
        SecondaryTitleActionBar secondaryTitleActionBar3 = new SecondaryTitleActionBar(new Icon(VaadinIcon.DESKTOP),parentClassificationDesc,null,null);
        leftSideContainerLayout.add(secondaryTitleActionBar3);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(10,Unit.PIXELS);
        leftSideContainerLayout.add(spaceDivLayout2);

        //ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(LineAwesomeIconsSvg.CHILD_SOLID.create(),"分类及三代内后代分类分布");
        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(VaadinIcon.CHILD.create(),"后代分类信息");
        leftSideContainerLayout.add(infoTitle2);
        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setHeight(10,Unit.PIXELS);
        leftSideContainerLayout.add(spaceDivLayout3);

        HorizontalLayout displayItemContainer5 = new HorizontalLayout();
        displayItemContainer5.getStyle().set("padding-left","10px");
        leftSideContainerLayout.add(displayItemContainer5);
        childClassificationCountDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer5, VaadinIcon.TAG.create(),"Child Classification-子分类数量:",childClassificationCount);

        HorizontalLayout spaceDivLayout_ = new HorizontalLayout();
        spaceDivLayout_.setWidthFull();
        spaceDivLayout_.setHeight(5,Unit.PIXELS);
        leftSideContainerLayout.add(spaceDivLayout_);

        HorizontalLayout displayItemContainer6 = new HorizontalLayout();
        displayItemContainer6.getStyle().set("padding-left","10px");
        leftSideContainerLayout.add(displayItemContainer6);
        offendClassificationCountDisplayItem = new SecondaryKeyValueDisplayItem(displayItemContainer6, FontAwesome.Solid.TAGS.create(),"Offspring Classification-后代分类数量:",offspringClassificationsCount);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidthFull();
        spaceDivLayout4.setHeight(10,Unit.PIXELS);
        spaceDivLayout4.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
        leftSideContainerLayout.add(spaceDivLayout4);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidthFull();
        spaceDivLayout5.setHeight(10,Unit.PIXELS);
        leftSideContainerLayout.add(spaceDivLayout5);

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(VaadinIcon.SPLIT.create(),"后代分类分布");
        leftSideContainerLayout.add(infoTitle3);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(classificationMetaInfo -> {
            Icon configIcon = new Icon(VaadinIcon.COG);
            configIcon.setSize("18px");
            Button configClassification = new Button(configIcon, event -> {
                if(classificationMetaInfo instanceof ClassificationMetaInfo){
                    renderClassificationConfigurationUI((ClassificationMetaInfo)classificationMetaInfo);
                }
            });
            configClassification.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            configClassification.addThemeVariants(ButtonVariant.LUMO_SMALL);
            configClassification.setTooltipText("显示子分类配置定义");

            Icon detachClassificationIcon = new Icon(VaadinIcon.UNLINK);
            detachClassificationIcon.setSize("18px");
            Button detachClassification = new Button(detachClassificationIcon, event -> {});
            detachClassification.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            detachClassification.addThemeVariants(ButtonVariant.LUMO_SMALL);
            detachClassification.setTooltipText("移除子分类");
            detachClassification.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(classificationMetaInfo instanceof ClassificationMetaInfo){
                        renderDetachChildClassificationUI((ClassificationMetaInfo)classificationMetaInfo);
                    }
                }
            });

            Icon deleteClassificationIcon = new Icon(VaadinIcon.TRASH);
            deleteClassificationIcon.setSize("18px");
            Button removeClassification = new Button(deleteClassificationIcon, event -> {});
            removeClassification.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeClassification.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeClassification.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeClassification.setTooltipText("删除分类");
            removeClassification.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(classificationMetaInfo instanceof ClassificationMetaInfo){
                        renderRemoveChildClassificationUI((ClassificationMetaInfo)classificationMetaInfo);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(configClassification,detachClassification,removeClassification);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15, Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        classificationsMetaInfoTreeGrid = new TreeGrid<>();
        classificationsMetaInfoTreeGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_ROW_BORDERS,GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER);
        classificationsMetaInfoTreeGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        classificationsMetaInfoTreeGrid.addHierarchyColumn(ClassificationMetaInfo::getClassificationName).setKey("idx_0").setHeader("分类名称");
        classificationsMetaInfoTreeGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("110px").setResizable(false);
        LightGridColumnHeader gridColumnHeader_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"分类名称");
        classificationsMetaInfoTreeGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx3 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        classificationsMetaInfoTreeGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3);

        leftSideContainerLayout.add(classificationsMetaInfoTreeGrid);

        middleContainerLayout = new VerticalLayout();
        middleContainerLayout.setSpacing(false);
        middleContainerLayout.setPadding(false);
        middleContainerLayout.setMargin(false);
        middleContainerLayout.setWidth(350, Unit.PIXELS);
        middleContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        mainContainerLayout.add(middleContainerLayout);

        ClassificationAttributesEditorView classificationAttributesEditorView= new ClassificationAttributesEditorView(this.classificationName,this.attributesViewKindDetailViewHeightOffset);
        middleContainerLayout.add(classificationAttributesEditorView);

        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setSpacing(false);
        rightSideContainerLayout.setPadding(false);
        rightSideContainerLayout.setMargin(false);
        mainContainerLayout.add(rightSideContainerLayout);

        TabSheet classificationRuntimeInfoTabSheet = new TabSheet();
        classificationRuntimeInfoTabSheet.setWidthFull();
        rightSideContainerLayout.add(classificationRuntimeInfoTabSheet);
        rightSideContainerLayout.setFlexGrow(1,classificationRuntimeInfoTabSheet);

        this.relatedConceptionKindsView = new RelatedConceptionKindsView(this.classificationName);
        this.relatedRelationKindsView = new RelatedRelationKindsView(this.classificationName);
        this.relatedAttributeKindsView = new RelatedAttributeKindsView(this.classificationName);
        this.relatedAttributesViewKindsView = new RelatedAttributesViewKindsView(this.classificationName);
        this.relatedConceptionEntitiesView = new RelatedConceptionEntitiesView(this.classificationName);

        classificationRuntimeInfoTabSheet.add(generateTabTitle(VaadinIcon.CUBE,"相关概念类型信息"),this.relatedConceptionKindsView);
        Tab relationKindClassificationInfoTab = classificationRuntimeInfoTabSheet.add(generateTabTitle(VaadinIcon.CONNECT_O,"相关关系类型信息"),this.relatedRelationKindsView);
        Tab attributeKindClassificationInfoTab = classificationRuntimeInfoTabSheet.add(generateTabTitle(VaadinIcon.INPUT,"相关属性类型信息"),this.relatedAttributeKindsView);
        Tab attributesViewKindClassificationInfoTab = classificationRuntimeInfoTabSheet.add(generateTabTitle(VaadinIcon.TASKS,"相关属性视图类型信息"),this.relatedAttributesViewKindsView);
        classificationRuntimeInfoTabSheet.add(generateTabTitle(VaadinIcon.STOCK,"相关概念实体信息"),this.relatedConceptionEntitiesView);

        classificationRuntimeInfoTabSheet.addSelectedChangeListener(new ComponentEventListener<TabSheet.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(TabSheet.SelectedChangeEvent selectedChangeEvent) {
                Tab selectedTab = selectedChangeEvent.getSelectedTab();
                if(selectedTab == relationKindClassificationInfoTab){
                    renderRelationKindClassificationInfoTab();
                }
                if(selectedTab == attributeKindClassificationInfoTab){
                    renderAttributeKindClassificationInfoTab();
                }
                if(selectedTab == attributesViewKindClassificationInfoTab){
                    renderAttributesViewKindClassificationInfoTab();
                }
            }
        });

        initLoadClassificationData();
    }

    private void initLoadClassificationData(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        try {
            allClassificationsMetaInfoList = coreRealm.getClassificationsMetaInfo();
            TreeDataProvider<ClassificationMetaInfo> dataProvider = (TreeDataProvider<ClassificationMetaInfo>)classificationsMetaInfoTreeGrid.getDataProvider();
            TreeData<ClassificationMetaInfo> gridTreeData = dataProvider.getTreeData();
            gridTreeData.clear();

            classificationMetaInfoMap = new HashMap<>();
            for(ClassificationMetaInfo currentClassificationMetaInfo:allClassificationsMetaInfoList){
                classificationMetaInfoMap.put(currentClassificationMetaInfo.getClassificationName(),currentClassificationMetaInfo);
            }

            Classification targetClassification = coreRealm.getClassification(this.classificationName);
            ClassificationRuntimeStatistics classificationRuntimeStatistics = targetClassification.getClassificationRuntimeStatistics();
            this.relatedConceptionKindsView.setTotalCount(classificationRuntimeStatistics.getRelatedConceptionKindCount());
            this.relatedRelationKindsView.setTotalCount(classificationRuntimeStatistics.getRelatedRelationKindCount());
            this.relatedAttributeKindsView.setTotalCount(classificationRuntimeStatistics.getRelatedAttributeKindCount());
            this.relatedAttributesViewKindsView.setTotalCount(classificationRuntimeStatistics.getRelatedAttributesViewKindCount());
            this.relatedConceptionEntitiesView.setTotalCount(classificationRuntimeStatistics.getRelatedConceptionEntityCount());

            InheritanceTree<Classification> classificationInheritanceTree =  targetClassification.getOffspringClassifications();
            Map<String,Classification> flattedClassificationMap = new HashMap<>();
            Classification startClassification = classificationInheritanceTree.getRoot();
            flatClassificationInheritanceTreeToList(classificationInheritanceTree,startClassification,flattedClassificationMap);

            Collection<Classification> classificationsSet = flattedClassificationMap.values();

            for(Classification currentClassification : classificationsSet){
                String classificationName = currentClassification.getClassificationName();
                if(!classificationName.equals(this.classificationName)){
                    ClassificationMetaInfo currentClassificationMetaInfo = classificationMetaInfoMap.get(classificationName);
                    gridTreeData.addItem(null,currentClassificationMetaInfo);
                }
            }
            for(Classification currentClassification : classificationsSet){
                String classificationName = currentClassification.getClassificationName();
                if(!classificationName.equals(this.classificationName)){
                    Classification parentClassification = currentClassification.getParentClassification();
                    if(parentClassification != null && ! parentClassification.getClassificationName().equals(this.classificationName)){
                        gridTreeData.setParent(classificationMetaInfoMap.get(currentClassification.getClassificationName()),classificationMetaInfoMap.get(parentClassification.getClassificationName()));
                    }
                }
            }
            dataProvider.refreshAll();
            this.classificationsMetaInfoTreeGrid.expand(allClassificationsMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
        coreRealm.closeGlobalSession();
    }

    private void flatClassificationInheritanceTreeToList(InheritanceTree<Classification> classificationInheritanceTree,Classification startClassification,Map<String,Classification> flattedClassificationMap){
        if(classificationInheritanceTree != null && flattedClassificationMap != null && startClassification != null){
            Classification currentClassification = classificationInheritanceTree.getNode(startClassification.getClassificationName());
            flattedClassificationMap.put(currentClassification.getClassificationName(),currentClassification);
            Collection<Classification> childOfCurrentClassification = classificationInheritanceTree.getChildren(currentClassification.getClassificationName());
            for(Classification currentChildClassification :childOfCurrentClassification){
                flattedClassificationMap.put(currentChildClassification.getClassificationName(),currentChildClassification);
                flatClassificationInheritanceTreeToList(classificationInheritanceTree,currentChildClassification,flattedClassificationMap);
            }
        }
    }

    private void renderShowMetaInfoUI(){
        ClassificationMetaInfoView classificationMetaInfoView = new ClassificationMetaInfoView(this.classificationName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.INFO_CIRCLE_O),"分类元数据信息",null,true,500,340,false);
        fixSizeWindow.setWindowContent(classificationMetaInfoView);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    private void renderParentClassificationConfigurationUI(){
        ClassificationDetailUI classificationDetailUI = new ClassificationDetailUI(this.parentClassificationName);
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

        Icon attributesViewKindIcon = VaadinIcon.TAGS.create();
        attributesViewKindIcon.setSize("10px");
        titleDetailLayout.add(attributesViewKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel attributesViewKindName = new NativeLabel(this.parentClassificationName);
        titleDetailLayout.add(attributesViewKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"分类配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(classificationDetailUI);
        fullScreenWindow.show();
    }

    private void renderClassificationConfigurationUI(ClassificationMetaInfo classificationMetaInfo){
        ClassificationDetailUI classificationDetailUI = new ClassificationDetailUI(classificationMetaInfo.getClassificationName());
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

        Icon attributesViewKindIcon = VaadinIcon.TAGS.create();
        attributesViewKindIcon.setSize("10px");
        titleDetailLayout.add(attributesViewKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel attributesViewKindName = new NativeLabel(classificationMetaInfo.getClassificationName());
        titleDetailLayout.add(attributesViewKindName);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"分类配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(classificationDetailUI);
        fullScreenWindow.show();
    }

    private void renderDetachChildClassificationUI(ClassificationMetaInfo classificationMetaInfo){
        DetachClassificationView detachClassificationView = new DetachClassificationView(classificationMetaInfo.getParentClassificationName(),classificationMetaInfo.getClassificationName());
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"移除子分类",null,true,600,200,false);
        fixSizeWindow.setWindowContent(detachClassificationView);
        fixSizeWindow.setModel(true);
        detachClassificationView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderRemoveChildClassificationUI(ClassificationMetaInfo classificationMetaInfo){
        RemoveClassificationView removeClassificationView = new RemoveClassificationView(classificationMetaInfo);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"删除分类",null,true,600,240,false);
        fixSizeWindow.setWindowContent(removeClassificationView);
        fixSizeWindow.setModel(true);
        removeClassificationView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderCreateClassificationUI(){
        CreateClassificationView createClassificationView = new CreateClassificationView();
        createClassificationView.setParentClassification(this.classificationName);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.PLUS_SQUARE_O.create(),"创建分类",null,true,500,350,false);
        fixSizeWindow.setWindowContent(createClassificationView);
        createClassificationView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    @Override
    public void receivedClassificationRemovedEvent(ClassificationRemovedEvent event) {
        if(event.getClassificationName() != null){
            initLoadClassificationData();
        }
    }

    @Override
    public void receivedClassificationDetachedEvent(ClassificationDetachedEvent event) {
        if(event.getParentClassificationName() != null){
            initLoadClassificationData();
        }
    }

    private HorizontalLayout generateTabTitle(VaadinIcon tabIcon, String tabTitleTxt){
        HorizontalLayout  kindConfigTabLayout = new HorizontalLayout();
        kindConfigTabLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        kindConfigTabLayout.setHeight(26,Unit.PIXELS);
        Icon configTabIcon = new Icon(tabIcon);
        configTabIcon.setSize("12px");
        NativeLabel configTabLabel = new NativeLabel(" "+tabTitleTxt);
        configTabLabel.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");
        kindConfigTabLayout.add(configTabIcon,configTabLabel);
        return kindConfigTabLayout;
    }

    private void renderRelationKindClassificationInfoTab(){
        if(!relationKindClassificationInfoTabRendered){
            relationKindClassificationInfoTabRendered = true;
            relatedRelationKindsView.loadDirectRelatedRelationKindsInfo();
        }
    }

    private void renderAttributeKindClassificationInfoTab(){
        if(!attributeKindClassificationInfoTabRendered){
            attributeKindClassificationInfoTabRendered = true;
            relatedAttributeKindsView.loadDirectRelatedAttributeKindsInfo();
        }
    }

    private void renderAttributesViewKindClassificationInfoTab(){
        if(!attributesViewKindClassificationInfoTabRendered){
            attributesViewKindClassificationInfoTabRendered = true;
            relatedAttributesViewKindsView.loadDirectRelatedAttributesViewKindsInfo();
        }
    }
}
