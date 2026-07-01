package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.actionExecute;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
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
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionAction;
import com.viewfunction.docg.element.commonComponent.ThirdLevelTitleActionBar;
import com.viewfunction.docg.element.userInterfaceUtil.AttributeValueOperateHandler;

import java.util.*;

public class ActionExecutionControlView extends VerticalLayout implements AttributeValueOperateHandler {
    private String conceptionKind;
    private String conceptionEntityUID;
    private ConceptionAction conceptionAction;

    private VerticalLayout queryFieldsContainer;
    private VerticalLayout queryResultContainer;
    private Registration listener;
    private int conceptionEntityActionsExecutionViewHeightOffset;
    private Popover addActionExecuteAttributePopover;
    private Button addActionExecuteAttributeButton;
    private Set<String> existingAttributeNames;
    private VerticalLayout actionAttributesValueContainer;

    public ActionExecutionControlView(String conceptionKind,String conceptionEntityUID,ConceptionAction conceptionAction,int conceptionEntityActionsExecutionViewHeightOffset){
        this.conceptionEntityActionsExecutionViewHeightOffset = conceptionEntityActionsExecutionViewHeightOffset;
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionAction = conceptionAction;
        this.existingAttributeNames = new HashSet<>();

        setPadding(false);
        setSpacing(false);
        setMargin(false);

        queryFieldsContainer = new VerticalLayout();
        queryFieldsContainer.setPadding(false);
        queryFieldsContainer.setSpacing(false);
        queryFieldsContainer.setMargin(false);

        VerticalLayout attributesViewKindInfoContainer = new VerticalLayout();
        attributesViewKindInfoContainer.setSpacing(false);
        attributesViewKindInfoContainer.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

        queryFieldsContainer.add(attributesViewKindInfoContainer);

        String attributeNameText = conceptionAction.getActionName() +" ( "+conceptionAction.getActionUID()+" )";
        String attributeKindIdText = conceptionAction.getActionDesc();

        ThirdLevelTitleActionBar secondaryTitleActionBar = new ThirdLevelTitleActionBar(new Icon(VaadinIcon.TASKS),attributeNameText,null,null);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        attributesViewKindInfoContainer.add(secondaryTitleActionBar);

        List<Component> actionComponentList = new ArrayList<>();

        Icon addPropertyIcon = new Icon(VaadinIcon.PLUS);
        addPropertyIcon.setSize("16px");
        addActionExecuteAttributeButton = new Button(addPropertyIcon, event -> {
            //renderAddActionExecuteAttributeUI();
            executeAction();
        });
        addActionExecuteAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addActionExecuteAttributeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        addActionExecuteAttributeButton.setTooltipText("添加自定义动作执行属性");


/*
        Button launchExecutionButton = new Button("执行动作");
        launchExecutionButton = new Button(addPropertyIcon, event -> {
            executeAction();
        });
        launchExecutionButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        launchExecutionButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        launchExecutionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        launchExecutionButton.setTooltipText("执行自定义动作");
*/

        actionComponentList.add(addActionExecuteAttributeButton);
        //actionComponentList.add(launchExecutionButton);


        Icon configIcon = new Icon(VaadinIcon.INFO_CIRCLE_O);
        configIcon.setTooltipText(conceptionAction.getActionImplementationClass());

        ThirdLevelTitleActionBar secondaryTitleActionBar2 = new ThirdLevelTitleActionBar(configIcon,attributeKindIdText,null,actionComponentList);
        secondaryTitleActionBar2.setWidth(100,Unit.PERCENTAGE);
        attributesViewKindInfoContainer.add(secondaryTitleActionBar2);

        //ConceptionEntityExternalDataQueryCriteriaView conceptionEntityExternalDataQueryCriteriaView = new ConceptionEntityExternalDataQueryCriteriaView(this.conceptionKind,this.attributesViewKind,this.externalAttributesValueAccessProcessorID,this.conceptionEntityExternalDataViewHeightOffset);
       // queryFieldsContainer.add(conceptionEntityExternalDataQueryCriteriaView);
       // conceptionEntityExternalDataQueryCriteriaView.setContainerExternalValueAttributeDataAccessView(this);

        actionAttributesValueContainer = new VerticalLayout();
        queryFieldsContainer.add(actionAttributesValueContainer);

        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        if(webBrowser.isChrome()){
            queryFieldsContainer.setMinWidth(360, Unit.PIXELS);
            queryFieldsContainer.setMaxWidth(360,Unit.PIXELS);
        }else{
            queryFieldsContainer.setMinWidth(350,Unit.PIXELS);
            queryFieldsContainer.setMaxWidth(350,Unit.PIXELS);
        }

        queryResultContainer= new VerticalLayout();
        queryResultContainer.setPadding(false);
        queryResultContainer.setSpacing(false);
        queryResultContainer.setMargin(false);

        //conceptionEntityExternalDataQueryResultsView = new ConceptionEntityExternalDataQueryResultsView(this.conceptionKind,this.conceptionEntityUID,this.attributesViewKind,this.externalAttributesValueAccessProcessorID,this.conceptionEntityExternalDataViewHeightOffset);
        //queryResultContainer.add(conceptionEntityExternalDataQueryResultsView);

        SplitLayout splitLayout = new SplitLayout(queryFieldsContainer, queryResultContainer);
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
            queryFieldsContainer.setHeight(event.getHeight() - conceptionEntityActionsExecutionViewHeightOffset + 50, Unit.PIXELS);
            //queryResultContainer.setHeight(event.getHeight() - conceptionEntityActionsExecutionViewHeightOffset + 60,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryFieldsContainer.setHeight(browserHeight - conceptionEntityActionsExecutionViewHeightOffset + 50,Unit.PIXELS);
            //queryResultContainer.setHeight(browserHeight - conceptionEntityActionsExecutionViewHeightOffset + 60,Unit.PIXELS);
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
        //CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        //ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        //ConceptionAction conceptionAction = targetConceptionKind.getAction(this.conceptionAction.getActionName());

        if(this.conceptionAction != null){
            try {
                Map <String,Object> executionAttributesMap = new HashMap<>();
                this.actionAttributesValueContainer.getChildren().forEach(actionExecutionAttributeEditorItemWidget -> {
                    ActionExecutionAttributeEditorItemWidget targetActionExecutionAttributeEditorItemWidget =
                            (ActionExecutionAttributeEditorItemWidget)actionExecutionAttributeEditorItemWidget;
                    AttributeValue targetAttributeValue = targetActionExecutionAttributeEditorItemWidget.getAttributeValue();
                    executionAttributesMap.put(targetAttributeValue.getAttributeName(),targetAttributeValue.getAttributeValue());
                });
                this.conceptionAction.executeActionSync(executionAttributesMap);
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
