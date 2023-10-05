package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement.maintainClassification;

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
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.Classification;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget;

import java.util.ArrayList;
import java.util.List;

import static com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindMaintain.KindDescriptionEditorItemWidget.KindType.Classification;

@Route("classificationDetailInfo/:classificationName")
public class ClassificationDetailUI extends VerticalLayout implements
        BeforeEnterObserver{
    private String classificationName;
    private int attributesViewKindDetailViewHeightOffset = 170;
    private int rightSideLayoutWidthOffset = 20;

    private int currentBrowserHeight = 0;
    private Registration listener;
    private KindDescriptionEditorItemWidget kindDescriptionEditorItemWidget;
    private VerticalLayout leftSideContainerLayout;
    private VerticalLayout middleContainerLayout;
    private VerticalLayout rightSideContainerLayout;
    private String parentClassificationName;
    private TreeGrid<ClassificationMetaInfo> classificationsMetaInfoTreeGrid;
    private List<ClassificationMetaInfo> classificationsMetaInfoList;

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
        //ResourceHolder.getApplicationBlackboard().addListener(this);
        renderClassificationData();
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int containerHeight = currentBrowserHeight - attributesViewKindDetailViewHeightOffset;
            int rightSideContainerLayoutWidth = event.getWidth() - 350-450 - rightSideLayoutWidthOffset;
            this.rightSideContainerLayout.setWidth(rightSideContainerLayoutWidth,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            int containerHeight = currentBrowserHeight - attributesViewKindDetailViewHeightOffset;
            int rightSideContainerLayoutWidth = receiver.getBodyClientWidth() - 350-450 - rightSideLayoutWidthOffset;
            this.rightSideContainerLayout.setWidth(rightSideContainerLayoutWidth,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        //listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
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

        Button attributesViewKindMetaInfoButton= new Button("分类元数据");
        attributesViewKindMetaInfoButton.setIcon(VaadinIcon.INFO_CIRCLE_O.create());
        attributesViewKindMetaInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        attributesViewKindMetaInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderShowMetaInfoUI();
            }
        });
        buttonList.add(attributesViewKindMetaInfoButton);

        Icon divIcon = VaadinIcon.LINE_V.create();
        divIcon.setSize("8px");
        buttonList.add(divIcon);

        Button refreshAttributesViewKindConfigInfoButton= new Button("刷新分类配置信息");
        refreshAttributesViewKindConfigInfoButton.setIcon(VaadinIcon.REFRESH.create());
        refreshAttributesViewKindConfigInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshAttributesViewKindConfigInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //containerConceptionKindsConfigView.refreshConceptionKindsInfo();
                //containsAttributeKindsConfigView.refreshAttributeTypesInfo();
                //refreshAttributesViewKindCorrelationInfoChart();
            }
        });
        buttonList.add(refreshAttributesViewKindConfigInfoButton);

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAGS),"Classification 分类  ",secTitleElementsList,buttonList);
        add(secondaryTitleActionBar);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        add(mainContainerLayout);

        leftSideContainerLayout = new VerticalLayout();
        leftSideContainerLayout.setSpacing(false);
        leftSideContainerLayout.setPadding(false);
        leftSideContainerLayout.setMargin(false);
        leftSideContainerLayout.setWidth(350, Unit.PIXELS);
        leftSideContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        mainContainerLayout.add(leftSideContainerLayout);

        ClassificationAttributesEditorView classificationAttributesEditorView= new ClassificationAttributesEditorView(this.classificationName,this.attributesViewKindDetailViewHeightOffset);
        leftSideContainerLayout.add(classificationAttributesEditorView);

        middleContainerLayout = new VerticalLayout();
        middleContainerLayout.setSpacing(false);
        middleContainerLayout.setPadding(false);
        middleContainerLayout.setMargin(false);
        middleContainerLayout.setWidth(450, Unit.PIXELS);
        middleContainerLayout.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        mainContainerLayout.add(middleContainerLayout);

        HorizontalLayout actionButtonBarContainer = new HorizontalLayout();
        actionButtonBarContainer.setSpacing(false);

        SecondaryIconTitle viewTitle = new SecondaryIconTitle(LineAwesomeIconsSvg.CODE_BRANCH_SOLID.create(),"分类继承信息",actionButtonBarContainer);
        viewTitle.setHeight(39, Unit.PIXELS);
        middleContainerLayout.add(viewTitle);
        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        middleContainerLayout.add(spaceDivLayout);

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setHeight(10,Unit.PIXELS);
        middleContainerLayout.add(spaceDivLayout1);

        //ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(LineAwesomeIconsSvg.MALE_SOLID.create(),"父分类信息");
        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(VaadinIcon.MALE.create(),"父分类信息");
        middleContainerLayout.add(infoTitle);

        List<Component> actionComponentsList1 = new ArrayList<>();
        Button showParentClassificationButton= new Button();
        showParentClassificationButton.setTooltipText("父分类配置定义");
        showParentClassificationButton.setIcon(VaadinIcon.EYE.create());
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
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        Classification targetClassification = coreRealm.getClassification(this.classificationName);
        if(targetClassification != null && !targetClassification.isRootClassification()){
            Classification parentClassification = targetClassification.getParentClassification();
            this.parentClassificationName = parentClassification.getClassificationName();
            parentClassificationName = parentClassification.getClassificationName();
            parentClassificationDesc = parentClassification.getClassificationDesc();
        }else{
            showParentClassificationButton.setEnabled(false);
        }

        SecondaryTitleActionBar secondaryTitleActionBar2 = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAG),parentClassificationName,null,actionComponentsList1,false);
        secondaryTitleActionBar2.setWidth(100,Unit.PERCENTAGE);
        middleContainerLayout.add(secondaryTitleActionBar2);
        SecondaryTitleActionBar secondaryTitleActionBar3 = new SecondaryTitleActionBar(new Icon(VaadinIcon.DESKTOP),parentClassificationDesc,null,null);
        middleContainerLayout.add(secondaryTitleActionBar3);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setHeight(10,Unit.PIXELS);
        middleContainerLayout.add(spaceDivLayout2);

        //ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(LineAwesomeIconsSvg.CHILD_SOLID.create(),"分类及三代内后代分类分布");
        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(VaadinIcon.CHILD.create(),"后代分类分布");
        middleContainerLayout.add(infoTitle2);
        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setHeight(10,Unit.PIXELS);
        middleContainerLayout.add(spaceDivLayout3);

        classificationsMetaInfoTreeGrid = new TreeGrid<>();
        //classificationsMetaInfoTreeGrid.setWidth(400,Unit.PIXELS);
        classificationsMetaInfoTreeGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_ROW_BORDERS);
        classificationsMetaInfoTreeGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        classificationsMetaInfoTreeGrid.addColumn(ClassificationMetaInfo::getClassificationName).setKey("idx_0").setHeader("分类信息");
        classificationsMetaInfoTreeGrid.addColumn(ClassificationMetaInfo::getClassificationDesc).setKey("idx_1").setHeader("操作").setFlexGrow(0).setWidth("80px").setResizable(false);;
        LightGridColumnHeader gridColumnHeader_idx0 = new LightGridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"分类信息");
        classificationsMetaInfoTreeGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx1 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        classificationsMetaInfoTreeGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        middleContainerLayout.add(classificationsMetaInfoTreeGrid);

        rightSideContainerLayout = new VerticalLayout();
        rightSideContainerLayout.setSpacing(false);
        rightSideContainerLayout.setPadding(false);
        rightSideContainerLayout.setMargin(false);
        rightSideContainerLayout.setWidth(500,Unit.PIXELS);
        //rightSideContainerLayout.setWidth(100, Unit.PERCENTAGE);
        mainContainerLayout.add(rightSideContainerLayout);
        //mainContainerLayout.setFlexGrow(1,rightSideContainerLayout);

        TabSheet classificationRuntimeInfoTabSheet = new TabSheet();
        classificationRuntimeInfoTabSheet.setWidthFull();
        rightSideContainerLayout.add(classificationRuntimeInfoTabSheet);
        rightSideContainerLayout.setFlexGrow(1,classificationRuntimeInfoTabSheet);
        classificationRuntimeInfoTabSheet.add(generateTabTitle(VaadinIcon.SPARK_LINE,"属性视图类型运行时配置"),new NativeLabel("222"));
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

    private HorizontalLayout generateTabTitle(VaadinIcon tabIcon, String tabTitleTxt){
        HorizontalLayout  kindConfigTabLayout = new HorizontalLayout();
        kindConfigTabLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        kindConfigTabLayout.setHeight(26,Unit.PIXELS);
        Icon configTabIcon = new Icon(tabIcon);
        configTabIcon.setSize("12px");
        NativeLabel configTabLabel = new NativeLabel(" "+tabTitleTxt);
        configTabLabel.getStyle()
                . set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");
        kindConfigTabLayout.add(configTabIcon,configTabLabel);
        return kindConfigTabLayout;
    }
}
