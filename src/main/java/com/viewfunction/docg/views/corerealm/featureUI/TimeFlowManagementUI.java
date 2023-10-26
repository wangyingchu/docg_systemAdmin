package com.viewfunction.docg.views.corerealm.featureUI;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;
import com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow.TimeFlowDetailUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeFlowManagementUI extends VerticalLayout {

    private Map<String, TimeFlowDetailUI> timeFlowDetailUIMap;

    public TimeFlowManagementUI(){
        this.timeFlowDetailUIMap = new HashMap<>();
        Button refreshDataButton = new Button("刷新时间流数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            //loadConceptionKindsInfo();
            //resetSingleConceptionKindSummaryInfoArea();
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

        TitleActionBar titleActionBar = new TitleActionBar(new Icon(VaadinIcon.COG_O),"Time Flow 时间流数据管理",secTitleElementsList,buttonList);
        add(titleActionBar);

        List<Component> timeFlowManagementOperationButtonList = new ArrayList<>();

        Button createTimeFlowButton = new Button("创建时间流",new Icon(VaadinIcon.PLUS_SQUARE_O));
        createTimeFlowButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createTimeFlowButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        timeFlowManagementOperationButtonList.add(createTimeFlowButton);
        createTimeFlowButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderCreateClassificationUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"时间流数据:",timeFlowManagementOperationButtonList);
        add(sectionActionBar);

        TabSheet timeFlowInfoTabSheet = new TabSheet();
        timeFlowInfoTabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_SMALL);
        timeFlowInfoTabSheet.setWidthFull();
        add(timeFlowInfoTabSheet);

        List<TimeFlow> timeFlowList = coreRealm.getTimeFlows();
        for(TimeFlow currentTimeFlow : timeFlowList){
            String currentTimeFlowName = currentTimeFlow.getTimeFlowName();
            TimeFlowDetailUI currentTimeFlowDetailUI = new TimeFlowDetailUI(currentTimeFlowName);
            this.timeFlowDetailUIMap.put(currentTimeFlowName,currentTimeFlowDetailUI);
            timeFlowInfoTabSheet.add(generateTabTitle(VaadinIcon.CLOCK,currentTimeFlowName),currentTimeFlowDetailUI);
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
}
