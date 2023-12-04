package com.viewfunction.docg.views.computegrid.featureUI;

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
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.GridComputeUnitVO;
import com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.GridRuntimeInfoWidget;

import java.util.ArrayList;
import java.util.List;

public class DataComputeGridManagementUI extends VerticalLayout {

    private Registration listener;
    private VerticalLayout leftSideContentContainerLayout;
    private VerticalLayout rightSideContentContainerLayout;
    private GridRuntimeInfoWidget gridRuntimeInfoWidget;
    private Grid<GridComputeUnitVO> computeUnitGrid;
    public DataComputeGridManagementUI(){

        Button refreshDataButton = new Button("刷新计算网格统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
        });

        List<Component> buttonList = new ArrayList<>();
        buttonList.add(refreshDataButton);

        List<Component> secTitleElementsList = new ArrayList<>();
        NativeLabel coreRealmTechLabel = new NativeLabel(" Apache Ignite 实现");
        coreRealmTechLabel.getStyle().set("font-size","var(--lumo-font-size-xxs)");
        secTitleElementsList.add(coreRealmTechLabel);
        coreRealmTechLabel.getElement().getThemeList().add("badge success");

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Compute Grid 计算网格管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        HorizontalLayout contentContainerLayout = new HorizontalLayout();
        contentContainerLayout.setWidthFull();
        add(contentContainerLayout);

        leftSideContentContainerLayout = new VerticalLayout();
        leftSideContentContainerLayout.setSpacing(false);
        leftSideContentContainerLayout.setWidth(550, Unit.PIXELS);
        leftSideContentContainerLayout.getStyle()
                .set("border-right", "1px solid var(--lumo-contrast-20pct)");
        contentContainerLayout.add(leftSideContentContainerLayout);

        rightSideContentContainerLayout = new VerticalLayout();
        rightSideContentContainerLayout.setSpacing(false);
        rightSideContentContainerLayout.setPadding(false);
        rightSideContentContainerLayout.setMargin(false);

        contentContainerLayout.add(rightSideContentContainerLayout);

        Icon gridUnitsIcon = LineAwesomeIconsSvg.SERVER_SOLID.create();
        SecondaryTitleActionBar gridUnitsActionBar = new SecondaryTitleActionBar(gridUnitsIcon,"网格计算单元",null,null);
        leftSideContentContainerLayout.add(gridUnitsActionBar);

        computeUnitGrid = new Grid<>();
        computeUnitGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        computeUnitGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        computeUnitGrid.addColumn(GridComputeUnitVO::getUnitID).setHeader("单元ID").setId("idx_0");
        computeUnitGrid.addColumn(GridComputeUnitVO::getHostName).setHeader("主机地址").setId("idx_1");
        computeUnitGrid.addColumn(GridComputeUnitVO::getIP).setHeader("主机端口").setId("idx_2");
        computeUnitGrid.addColumn(GridComputeUnitVO::getUnitType).setHeader("单元类型").setId("idx_3");
        computeUnitGrid.setHeight(250,Unit.PIXELS);


        leftSideContentContainerLayout.add(computeUnitGrid);

        VerticalLayout spaceDivLayout1 = new VerticalLayout();
        leftSideContentContainerLayout.add(spaceDivLayout1);

        Icon gridRuntimeStatusIcon = new Icon(VaadinIcon.SPARK_LINE);
        SecondaryTitleActionBar gridRuntimeStatusActionBar = new SecondaryTitleActionBar(gridRuntimeStatusIcon,"网格运行信息",null,null);
        leftSideContentContainerLayout.add(gridRuntimeStatusActionBar);

        gridRuntimeInfoWidget = new GridRuntimeInfoWidget();
        leftSideContentContainerLayout.add(gridRuntimeInfoWidget);

        TabSheet gridConfigurationTabSheet = new TabSheet();
        gridConfigurationTabSheet.setWidthFull();
        rightSideContentContainerLayout.add(gridConfigurationTabSheet);
        rightSideContentContainerLayout.setFlexGrow(1,gridConfigurationTabSheet);

        gridConfigurationTabSheet.add(generateKindConfigurationTabTitle(LineAwesomeIconsSvg.BUFFER.create(),"网格数据切片配置"),new HorizontalLayout());
        gridConfigurationTabSheet.add(generateKindConfigurationTabTitle(LineAwesomeIconsSvg.BOLT_SOLID.create(),"网格计算服务配置"),new HorizontalLayout());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.leftSideContentContainerLayout.setHeight(event.getHeight()-170,Unit.PIXELS);
            this.rightSideContentContainerLayout.setWidth(event.getWidth()-580,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            int browserHeight = receiver.getBodyClientHeight();
            this.leftSideContentContainerLayout.setHeight(browserHeight-170,Unit.PIXELS);
            this.rightSideContentContainerLayout.setWidth(browserWidth-580,Unit.PIXELS);
        }));
        //ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private HorizontalLayout generateKindConfigurationTabTitle(Icon tabIcon, String tabTitleTxt){
        HorizontalLayout  kindConfigTabLayout = new HorizontalLayout();
        kindConfigTabLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        kindConfigTabLayout.setHeight(26,Unit.PIXELS);
        //Icon configTabIcon = new Icon(tabIcon);
        tabIcon.setSize("12px");
        NativeLabel configTabLabel = new NativeLabel(" "+tabTitleTxt);
        configTabLabel.getStyle()
                . set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");
        kindConfigTabLayout.add(tabIcon,configTabLabel);
        return kindConfigTabLayout;
    }
}
