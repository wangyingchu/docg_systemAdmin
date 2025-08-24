package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindActionMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KindActionsDateView extends VerticalLayout {

    public enum KindType {ConceptionKind,RelationKind}

    private String kindName;
    private int actionsDataViewHeightOffset;
    private KindType kindType;
    private Grid<ConceptionAction> conceptionActionsGrid;
    private ConceptionAction lastSelectedConceptionAction;
    private Grid<RelationAction> relationActionsGrid;
    private RelationAction lastSelectedRelationAction;
    private Registration listener;
    private int currentBrowserHeight = 0;
    private Popover registerActionViewPopover;
    private Button registerKindActionButton;

    public KindActionsDateView(KindType kindType,String kindName,int actionsDataViewHeightOffset){
        this.kindName = kindName;
        this.kindType = kindType;
        this.actionsDataViewHeightOffset = actionsDataViewHeightOffset;

        this.setWidth(100, Unit.PERCENTAGE);

        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        registerKindActionButton = new Button("注册新的自定义动作");
        registerKindActionButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        registerKindActionButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        registerKindActionButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRegisterActionUI();
            }
        });
        buttonList.add(registerKindActionButton);

        Button refreshKindActionsButton = new Button("刷新自定义动作信息",new Icon(VaadinIcon.REFRESH));
        refreshKindActionsButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshKindActionsButton.addClickListener((ClickEvent<Button> click) ->{
            refreshKindActionsInfo();
        });
        buttonList.add(refreshKindActionsButton);

        SecondaryTitleActionBar kindActionConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONTROLLER),"自定义动作配置管理 ",secTitleElementsList,buttonList);
        add(kindActionConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(kindAction -> {
            Icon editIcon = new Icon(VaadinIcon.EDIT);
            editIcon.setSize("19px");
            Button editButton = new Button(editIcon, event -> {});
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.setTooltipText("更新自定义活动信息");
            editButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(kindAction instanceof ConceptionAction){
                        renderEditActionUI(((ConceptionAction)kindAction).getActionName());
                    }
                    if(kindAction instanceof RelationAction){
                        renderEditActionUI(((RelationAction)kindAction).getActionName());
                    }
                }
            });

            Icon deleteIcon = new Icon(VaadinIcon.TRASH);
            deleteIcon.setSize("20px");
            Button deleteButton = new Button(deleteIcon, event -> {});
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.setTooltipText("注销自定义活动");
            deleteButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(kindAction instanceof ConceptionAction){
                        renderUnregisterActionUI(((ConceptionAction)kindAction).getActionName());
                    }
                    if(kindAction instanceof RelationAction){
                        renderUnregisterActionUI(((RelationAction)kindAction).getActionName());
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(editButton,deleteButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        switch(kindType){
            case ConceptionKind :
                conceptionActionsGrid = new Grid<>();
                conceptionActionsGrid.setWidth(100,Unit.PERCENTAGE);
                conceptionActionsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
                conceptionActionsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
                conceptionActionsGrid.addColumn(ConceptionAction::getActionName).setHeader("自定义动作名称").setKey("idx_0").setFlexGrow(0).setWidth("200px").setResizable(true)
                        .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getActionName());
                conceptionActionsGrid.addColumn(ConceptionAction::getActionDesc).setHeader("自定义动作描述").setKey("idx_1").setFlexGrow(0).setWidth("300px").setResizable(true)
                        .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getActionDesc());
                conceptionActionsGrid.addColumn(ConceptionAction::getActionImplementationClass).setHeader("自定义动作类全名").setKey("idx_2").setFlexGrow(1).setResizable(true);
                conceptionActionsGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("90px").setResizable(false);
                GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"自定义动作名称");
                conceptionActionsGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
                GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"自定义动作描述");
                conceptionActionsGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
                GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(LineAwesomeIconsSvg.JAVA.create(),"自定义动作类全名");
                conceptionActionsGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
                GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
                conceptionActionsGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3);

                conceptionActionsGrid.addSelectionListener(new SelectionListener<Grid<ConceptionAction>, ConceptionAction>() {
                    @Override
                    public void selectionChange(SelectionEvent<Grid<ConceptionAction>, ConceptionAction> selectionEvent) {
                        Set<ConceptionAction> selectedItemSet = selectionEvent.getAllSelectedItems();
                        if(selectedItemSet.size() == 0){
                            // don't allow to unselect item, just reselect last selected item
                            conceptionActionsGrid.select(lastSelectedConceptionAction);
                        }else{
                            ConceptionAction selectedAttributesViewKind = selectedItemSet.iterator().next();
                            lastSelectedConceptionAction = selectedAttributesViewKind;
                        }
                    }
                });

                conceptionActionsGrid.appendFooterRow();
                add(conceptionActionsGrid);

                break;
            case RelationKind :
                relationActionsGrid = new Grid<>();
                relationActionsGrid.setWidth(100,Unit.PERCENTAGE);
                relationActionsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
                relationActionsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
                relationActionsGrid.addColumn(RelationAction::getActionName).setHeader("自定义动作名称").setKey("idx_0").setFlexGrow(0).setWidth("200px").setResizable(true)
                        .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getActionName());
                relationActionsGrid.addColumn(RelationAction::getActionDesc).setHeader("自定义动作描述").setKey("idx_1").setFlexGrow(0).setWidth("300px").setResizable(true)
                        .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getActionDesc());
                relationActionsGrid.addColumn(RelationAction::getActionImplementationClass).setHeader("自定义动作类全名").setKey("idx_2").setFlexGrow(1).setResizable(true);
                relationActionsGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("90px").setResizable(false);
                GridColumnHeader gridColumnHeader_idx0_r = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"自定义动作名称");
                relationActionsGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0_r).setSortable(true);
                GridColumnHeader gridColumnHeader_idx1_r = new GridColumnHeader(VaadinIcon.DESKTOP,"自定义动作描述");
                relationActionsGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1_r).setSortable(true);
                GridColumnHeader gridColumnHeader_idx2_r = new GridColumnHeader(LineAwesomeIconsSvg.JAVA.create(),"自定义动作类全名");
                relationActionsGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2_r).setSortable(true);
                GridColumnHeader gridColumnHeader_idx3_r = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
                relationActionsGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3_r);

                relationActionsGrid.addSelectionListener(new SelectionListener<Grid<RelationAction>, RelationAction>() {
                    @Override
                    public void selectionChange(SelectionEvent<Grid<RelationAction>, RelationAction> selectionEvent) {
                        Set<RelationAction> selectedItemSet = selectionEvent.getAllSelectedItems();
                        if(selectedItemSet.size() == 0){
                            // don't allow to unselect item, just reselect last selected item
                            relationActionsGrid.select(lastSelectedRelationAction);
                        }else{
                            RelationAction selectedAttributesViewKind = selectedItemSet.iterator().next();
                            lastSelectedRelationAction = selectedAttributesViewKind;
                        }
                    }
                });

                relationActionsGrid.appendFooterRow();
                add(relationActionsGrid);
                break;
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int chartHeight = currentBrowserHeight - actionsDataViewHeightOffset + 31;
            if(this.conceptionActionsGrid != null){
                this.conceptionActionsGrid.setHeight(chartHeight,Unit.PIXELS);
            }
            if(this.relationActionsGrid != null){
                this.relationActionsGrid.setHeight(chartHeight,Unit.PIXELS);
            }
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            int chartHeight = currentBrowserHeight - actionsDataViewHeightOffset + 31;
            if(this.conceptionActionsGrid != null){
                this.conceptionActionsGrid.setHeight(chartHeight,Unit.PIXELS);
            }
            if(this.relationActionsGrid != null){
                this.relationActionsGrid.setHeight(chartHeight,Unit.PIXELS);
            }
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void renderConceptionActionDataUI(Set<ConceptionAction> actionSet){
        this.conceptionActionsGrid.setItems(actionSet);
    }

    public void renderRelationActionDataUI(Set<RelationAction> actionSet){
        this.relationActionsGrid.setItems(actionSet);
    }

    private void renderRegisterActionUI(){
        if(registerActionViewPopover == null){
            registerActionViewPopover = new Popover();
            RegisterKindActionView registerKindActionView = new RegisterKindActionView(this.kindType,this.kindName);
            registerKindActionView.setParentKindActionsDateView(this);
            registerKindActionView.setContainerPopover(registerActionViewPopover);
            registerActionViewPopover.setTarget(registerKindActionButton);
            registerActionViewPopover.setWidth("700px");
            registerActionViewPopover.setHeight("325px");
            registerActionViewPopover.addThemeVariants(PopoverVariant.ARROW);
            registerActionViewPopover.setPosition(PopoverPosition.TOP);
            registerActionViewPopover.add(registerKindActionView);
            registerActionViewPopover.setAutofocus(true);
            registerActionViewPopover.setModal(true,true);
        }
        registerActionViewPopover.open();
    }

    private void renderUnregisterActionUI(String actionName){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认注销自定义活动",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行注销自定义活动 "+ actionName +" 的操作",actionButtonList,500,175);
        confirmWindow.open();
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doUnregisterAction(actionName,confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doUnregisterAction(String actionName,ConfirmWindow confirmWindow){
        try {
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            switch (this.kindType){
                case ConceptionKind :
                    ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(kindName);
                    boolean unregisterResult = targetConceptionKind.unregisterAction(actionName);
                    if(unregisterResult){
                        CommonUIOperationUtil.showPopupNotification("注销自定义活动 "+ actionName +" 成功", NotificationVariant.LUMO_SUCCESS);
                        confirmWindow.closeConfirmWindow();
                        refreshKindActionsInfo();
                    }else{
                        CommonUIOperationUtil.showPopupNotification("注销自定义活动 "+ actionName +" 失败", NotificationVariant.LUMO_ERROR);
                    }
                    break;
                case RelationKind :
                    RelationKind targetRelationKind = coreRealm.getRelationKind(kindName);
                    boolean unregisterResult2 = targetRelationKind.unregisterAction(actionName);
                    if(unregisterResult2){
                        CommonUIOperationUtil.showPopupNotification("注销自定义活动 "+ actionName +" 成功", NotificationVariant.LUMO_SUCCESS);
                        confirmWindow.closeConfirmWindow();
                        refreshKindActionsInfo();
                    }else{
                        CommonUIOperationUtil.showPopupNotification("注销自定义活动 "+ actionName +" 失败", NotificationVariant.LUMO_ERROR);
                    }
                    break;
            }
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderEditActionUI(String actionName){
        EditKindActionView editKindActionView = new EditKindActionView(actionName,this.kindType,kindName);
        editKindActionView.setParentKindActionsDateView(this);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(VaadinIcon.EDIT.create(),"更新自定义活动信息",null,true,700,380,false);
        fixSizeWindow.setWindowContent(editKindActionView);
        editKindActionView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.setModel(true);
        fixSizeWindow.show();
    }

    public void refreshKindActionsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        switch (this.kindType){
            case ConceptionKind :
                ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(kindName);
                Set<ConceptionAction> conceptionActionsSet = targetConceptionKind.getActions();
                this.renderConceptionActionDataUI(conceptionActionsSet);
                break;
            case RelationKind :
                RelationKind targetRelationKind = coreRealm.getRelationKind(kindName);
                Set<RelationAction> relationActionsSet = targetRelationKind.getActions();
                this.renderRelationActionDataUI(relationActionsSet);
                break;
        }
    }
}
