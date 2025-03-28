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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.SectionActionBar;
import com.viewfunction.docg.element.commonComponent.TitleActionBar;
import com.viewfunction.docg.element.eventHandling.TimeFlowCreatedEvent;
import com.viewfunction.docg.element.eventHandling.TimeFlowRefreshEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.CreateTimeFlowView;
import com.viewfunction.docg.views.corerealm.featureUI.timeFlowManagement.maintainTimeFlow.TimeFlowDetailUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeFlowManagementUI extends VerticalLayout implements TimeFlowCreatedEvent.TimeFlowCreatedListener {

    private Map<String, TimeFlowDetailUI> timeFlowDetailUIMap;
    private TabSheet timeFlowInfoTabSheet;

    public TimeFlowManagementUI(){
        this.timeFlowDetailUIMap = new HashMap<>();
        Button refreshDataButton = new Button("刷新时间流数据统计信息",new Icon(VaadinIcon.REFRESH));
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        refreshDataButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        refreshDataButton.addClickListener((ClickEvent<Button> click) ->{
            TimeFlowRefreshEvent timeFlowRefreshEvent = new TimeFlowRefreshEvent();
            ResourceHolder.getApplicationBlackboard().fire(timeFlowRefreshEvent);
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
                renderCreateTimeFlowUI();
            }
        });

        Icon icon = new Icon(VaadinIcon.LIST);
        SectionActionBar sectionActionBar = new SectionActionBar(icon,"时间流数据:",timeFlowManagementOperationButtonList);
        add(sectionActionBar);

        timeFlowInfoTabSheet = new TabSheet();
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

    private void renderCreateTimeFlowUI(){
        CreateTimeFlowView createTimeFlowView = new CreateTimeFlowView();
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"创建时间流",null,true,530,220,false);
        fixSizeWindow.setWindowContent(createTimeFlowView);
        fixSizeWindow.setModel(true);
        createTimeFlowView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    @Override
    public void receivedTimeFlowCreatedEvent(TimeFlowCreatedEvent event) {
        String timeFlowName = event.getTimeFlowName();
        TimeFlowDetailUI currentTimeFlowDetailUI = new TimeFlowDetailUI(timeFlowName);
        this.timeFlowDetailUIMap.put(timeFlowName,currentTimeFlowDetailUI);
        timeFlowInfoTabSheet.add(generateTabTitle(VaadinIcon.CLOCK,timeFlowName),currentTimeFlowDetailUI);
    }
}
