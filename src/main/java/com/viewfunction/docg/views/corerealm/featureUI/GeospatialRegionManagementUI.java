package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.GeospatialRegion;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;
import com.viewfunction.docg.element.eventHandling.GeospatialRegionCreatedEvent;
import com.viewfunction.docg.element.eventHandling.GeospatialRegionRefreshEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.CreateGeospatialRegionView;
import com.viewfunction.docg.views.corerealm.featureUI.geospatialRegionManagement.maintainGeospatialRegion.GeospatialRegionDetailUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeospatialRegionManagementUI extends VerticalLayout implements
        GeospatialRegionCreatedEvent.GeospatialRegionCreatedListener {

    private Map<String, GeospatialRegionDetailUI> geospatialRegionDetailUIMap;
    private TabSheet geospatialRegionInfoTabSheet;

    public GeospatialRegionManagementUI(){
        this.geospatialRegionDetailUIMap = new HashMap<>();
        Button refreshDataButton = new Button("刷新地理空间区域数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            GeospatialRegionRefreshEvent geospatialRegionRefreshEvent = new GeospatialRegionRefreshEvent();
            ResourceHolder.getApplicationBlackboard().fire(geospatialRegionRefreshEvent);
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

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Geospatial Region 地理空间区域数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> timeFlowManagementOperationButtonList = new ArrayList<>();

        Button createTimeFlowButton = new Button("创建地理空间区域",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createTimeFlowButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createTimeFlowButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        timeFlowManagementOperationButtonList.add(createTimeFlowButton);
        createTimeFlowButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderCreateGeospatialRegionUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"地理空间区域数据:",timeFlowManagementOperationButtonList);
        add(sectionActionBar);

        geospatialRegionInfoTabSheet = new TabSheet();
        geospatialRegionInfoTabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_SMALL);
        geospatialRegionInfoTabSheet.setWidthFull();
        add(geospatialRegionInfoTabSheet);

        List<GeospatialRegion> geospatialRegionList = coreRealm.getGeospatialRegions();
        for(GeospatialRegion currentGeospatialRegion : geospatialRegionList){
            String currentTimeFlowName = currentGeospatialRegion.getGeospatialRegionName();
            GeospatialRegionDetailUI geospatialRegionDetailUI = new GeospatialRegionDetailUI(currentTimeFlowName);
            this.geospatialRegionDetailUIMap.put(currentTimeFlowName,geospatialRegionDetailUI);
            geospatialRegionInfoTabSheet.add(generateTabTitle(VaadinIcon.GLOBE,currentTimeFlowName),geospatialRegionDetailUI);
        }
    }

    private HorizontalLayout generateTabTitle(VaadinIcon tabIcon, String tabTitleTxt){
        HorizontalLayout  tabTitleLayout = new HorizontalLayout();
        tabTitleLayout.setDefaultVerticalComponentAlignment(Alignment.START);
        Icon tabTitleIcon = new Icon(tabIcon);
        tabTitleIcon.setSize("8px");
        NativeLabel tabTitleLabel = new NativeLabel(" "+tabTitleTxt);
        tabTitleLabel.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");
        tabTitleLayout.add(tabTitleIcon,tabTitleLabel);
        return tabTitleLayout;
    }

    private void renderCreateGeospatialRegionUI(){
        CreateGeospatialRegionView createGeospatialRegionView = new CreateGeospatialRegionView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"创建地理空间区域",null,true,530,220,false);
        fixSizeWindow.setWindowContent(createGeospatialRegionView);
        fixSizeWindow.setModel(true);
        createGeospatialRegionView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
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

    @Override
    public void receivedGeospatialRegionCreatedEvent(GeospatialRegionCreatedEvent event) {
        String geospatialRegionName = event.getGeospatialRegionName();
        GeospatialRegionDetailUI geospatialRegionDetailUI = new GeospatialRegionDetailUI(geospatialRegionName);
        this.geospatialRegionDetailUIMap.put(geospatialRegionName,geospatialRegionDetailUI);
        geospatialRegionInfoTabSheet.add(generateTabTitle(VaadinIcon.GLOBE,geospatialRegionName),geospatialRegionDetailUI);
    }
}
