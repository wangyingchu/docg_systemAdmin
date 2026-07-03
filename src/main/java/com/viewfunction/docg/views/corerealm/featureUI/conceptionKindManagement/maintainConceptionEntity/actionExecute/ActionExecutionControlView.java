package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.actionExecute;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.ThirdLevelTitleActionBar;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;

import java.util.*;

public class ActionExecutionControlView extends VerticalLayout implements AttributeValueOperateHandler {
    private String conceptionKind;
    private String conceptionEntityUID;
    private ConceptionAction conceptionAction;
    private VerticalLayout exectionAttributeFieldsContainer;
    private VerticalLayout exectionResultContainer;
    private Registration listener;
    private int conceptionEntityActionsExecutionViewHeightOffset;
    private Popover addActionExecuteAttributePopover;
    private Button addActionExecuteAttributeButton;
    private Set<String> existingAttributeNames;
    private VerticalLayout actionAttributesValueContainer;
    private ConceptionEntityActionExecuteResultsView conceptionEntityActionExecuteResultsView;

    public ActionExecutionControlView(String conceptionKind,String conceptionEntityUID,ConceptionAction conceptionAction,int conceptionEntityActionsExecutionViewHeightOffset){
        this.conceptionEntityActionsExecutionViewHeightOffset = conceptionEntityActionsExecutionViewHeightOffset;
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionAction = conceptionAction;
        this.existingAttributeNames = new HashSet<>();

        setPadding(false);
        setSpacing(false);
        setMargin(false);

        exectionAttributeFieldsContainer = new VerticalLayout();
        exectionAttributeFieldsContainer.setPadding(false);
        exectionAttributeFieldsContainer.setSpacing(false);
        exectionAttributeFieldsContainer.setMargin(false);

        VerticalLayout attributesViewKindInfoContainer = new VerticalLayout();
        attributesViewKindInfoContainer.setSpacing(false);
        attributesViewKindInfoContainer.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

        exectionAttributeFieldsContainer.add(attributesViewKindInfoContainer);

        String attributeNameText = conceptionAction.getActionName() +" ( "+conceptionAction.getActionUID()+" )";
        String attributeKindIdText = conceptionAction.getActionDesc();

        ThirdLevelTitleActionBar secondaryTitleActionBar = new ThirdLevelTitleActionBar(new Icon(VaadinIcon.TASKS),attributeNameText,null,null);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        attributesViewKindInfoContainer.add(secondaryTitleActionBar);

        List<Component> actionComponentList = new ArrayList<>();

        Icon addPropertyIcon = new Icon(VaadinIcon.PLUS);
        addPropertyIcon.setSize("18px");
        addActionExecuteAttributeButton = new Button(addPropertyIcon, event -> {
            renderAddActionExecuteAttributeUI();
        });
        addActionExecuteAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addActionExecuteAttributeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        addActionExecuteAttributeButton.setTooltipText("添加自定义动作执行属性");

        Icon executeActionIcon = new Icon(VaadinIcon.CARET_RIGHT);
        executeActionIcon.setSize("26px");
        Button executeActionButton = new Button(executeActionIcon, event -> {
            executeAction();
        });
        executeActionButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        executeActionButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        executeActionButton.setTooltipText("执行自定义动作");

        actionComponentList.add(addActionExecuteAttributeButton);
        actionComponentList.add(executeActionButton);

        Icon configIcon = new Icon(VaadinIcon.INFO_CIRCLE_O);
        configIcon.setTooltipText(conceptionAction.getActionImplementationClass());

        ThirdLevelTitleActionBar secondaryTitleActionBar2 = new ThirdLevelTitleActionBar(configIcon,attributeKindIdText,null,actionComponentList);
        secondaryTitleActionBar2.setWidth(100,Unit.PERCENTAGE);
        attributesViewKindInfoContainer.add(secondaryTitleActionBar2);

        actionAttributesValueContainer = new VerticalLayout();
        exectionAttributeFieldsContainer.add(actionAttributesValueContainer);

        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        if(webBrowser.isChrome()){
            exectionAttributeFieldsContainer.setMinWidth(360, Unit.PIXELS);
            exectionAttributeFieldsContainer.setMaxWidth(360,Unit.PIXELS);
        }else{
            exectionAttributeFieldsContainer.setMinWidth(350,Unit.PIXELS);
            exectionAttributeFieldsContainer.setMaxWidth(350,Unit.PIXELS);
        }

        exectionResultContainer = new VerticalLayout();
        exectionResultContainer.setPadding(false);
        exectionResultContainer.setSpacing(false);
        exectionResultContainer.setMargin(false);

        conceptionEntityActionExecuteResultsView = new ConceptionEntityActionExecuteResultsView(this.conceptionKind,this.conceptionEntityUID);
        exectionResultContainer.add(conceptionEntityActionExecuteResultsView);

        SplitLayout splitLayout = new SplitLayout(exectionAttributeFieldsContainer, exectionResultContainer);
        splitLayout.setSplitterPosition(0);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            exectionAttributeFieldsContainer.setHeight(event.getHeight() - conceptionEntityActionsExecutionViewHeightOffset + 50, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            exectionAttributeFieldsContainer.setHeight(browserHeight - conceptionEntityActionsExecutionViewHeightOffset + 50,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void renderAddActionExecuteAttributeUI(){
        if(addActionExecuteAttributePopover == null){
            addActionExecuteAttributePopover = new Popover();
            addActionExecuteAttributePopover.setTarget(addActionExecuteAttributeButton);
            addActionExecuteAttributePopover.setWidth("460px");
            addActionExecuteAttributePopover.setHeight("120px");
            addActionExecuteAttributePopover.addThemeVariants(PopoverVariant.ARROW);
            addActionExecuteAttributePopover.setPosition(PopoverPosition.BOTTOM);
            addActionExecuteAttributePopover.setAutofocus(true);
            addActionExecuteAttributePopover.setModal(true,true);
        }
        addActionExecuteAttributePopover.removeAll();
        AddEntityActionExecuteAttributeView addEntityActionExecuteAttributeView = new AddEntityActionExecuteAttributeView(this.existingAttributeNames);
        addEntityActionExecuteAttributeView.setAttributeValueOperateHandler(this);
        addActionExecuteAttributePopover.add(addEntityActionExecuteAttributeView);
        addEntityActionExecuteAttributeView.setContainerPopover(addActionExecuteAttributePopover);
        addEntityActionExecuteAttributeView.setParentActionExecutionControlView(this);
        addActionExecuteAttributePopover.open();
    }

    @Override
    public void handleAttributeValue(AttributeValue attributeValue) {
        this.existingAttributeNames.add(attributeValue.getAttributeName());
        this.addActionExecuteAttributePopover.close();
        ActionExecutionAttributeEditorItemWidget actionExecutionAttributeEditorItemWidget = new ActionExecutionAttributeEditorItemWidget(attributeValue);
        actionExecutionAttributeEditorItemWidget.setContainerActionExecutionControlView(this);
        this.actionAttributesValueContainer.add(actionExecutionAttributeEditorItemWidget);
    }

    public void deleteActionAttribute(String attributeName,ActionExecutionAttributeEditorItemWidget actionExecutionAttributeEditorItemWidget){
        this.existingAttributeNames.remove(attributeName);
        this.actionAttributesValueContainer.remove(actionExecutionAttributeEditorItemWidget);
    }

    public void executeAction() {
        if(this.conceptionAction != null){
            List<Button> actionButtonList = new ArrayList<>();
            Button confirmButton = new Button("确认执行自定义动作",new Icon(VaadinIcon.CHECK_CIRCLE));
            confirmButton.addThemeVariants(ButtonVariant.PRIMARY);
            Button cancelButton = new Button("取消操作");
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
            actionButtonList.add(confirmButton);
            actionButtonList.add(cancelButton);

            ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"执行自定义动作",
                    "请确认执行自定义动作 "+ conceptionAction.getActionName(),actionButtonList,500,185);
            confirmWindow.open();

            confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    try {
                        Map <String,Object> executionAttributesMap = new HashMap<>();
                        actionAttributesValueContainer.getChildren().forEach(actionExecutionAttributeEditorItemWidget -> {
                            ActionExecutionAttributeEditorItemWidget targetActionExecutionAttributeEditorItemWidget =
                                    (ActionExecutionAttributeEditorItemWidget)actionExecutionAttributeEditorItemWidget;
                            AttributeValue targetAttributeValue = targetActionExecutionAttributeEditorItemWidget.getAttributeValue();
                            executionAttributesMap.put(targetAttributeValue.getAttributeName(),targetAttributeValue.getAttributeValue());
                        });

                        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
                        try{
                            coreRealm.openGlobalSession();
                            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKind);
                            ConceptionEntity targetConceptionEntity = targetConceptionKind.getEntityByUID(conceptionEntityUID);
                            ConceptionAction targetConceptionAction = targetConceptionKind.getAction(conceptionAction.getActionName());
                            Object actionResult = targetConceptionAction.executeActionSync(executionAttributesMap,targetConceptionEntity);
                            if(actionResult != null ){
                                conceptionEntityActionExecuteResultsView.renderActionExecutionResult(actionResult);
                            }
                        }finally{
                            coreRealm.closeGlobalSession();
                        }
                    } catch (CoreRealmServiceRuntimeException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        e.printStackTrace();
                        conceptionEntityActionExecuteResultsView.renderActionExecutionErrorMessage(e.getMessage());
                    }
                    confirmWindow.closeConfirmWindow();
                }
            });
            cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    confirmWindow.closeConfirmWindow();
                }
            });
        }
    }
}
