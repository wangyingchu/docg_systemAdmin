package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindActionMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KindActionsDateView extends VerticalLayout {

    public enum KindType {ConceptionKind,RelationKind}

    private String kindName;
    private int actionsDataViewHeightOffset;
    private KindType kindType;
    private Grid<ConceptionAction> attributesViewKindGrid;
    private ConceptionAction lastSelectedAttributesViewKind;
    private Registration listener;
    private int currentBrowserHeight = 0;

    public KindActionsDateView(KindType kindType,String kindName,int actionsDataViewHeightOffset){
        this.kindName = kindName;
        this.kindType = kindType;
        this.actionsDataViewHeightOffset = actionsDataViewHeightOffset;

        this.setWidth(100, Unit.PERCENTAGE);

        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button attachAttributesViewKindButton= new Button("注册新的自定义动作");
        attachAttributesViewKindButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        attachAttributesViewKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        attachAttributesViewKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAttachNewAttributesViewKindUI();
            }
        });
        buttonList.add(attachAttributesViewKindButton);

        Button refreshAttributesViewKindsButton = new Button("刷新自定义动作信息",new Icon(VaadinIcon.REFRESH));
        refreshAttributesViewKindsButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshAttributesViewKindsButton.addClickListener((ClickEvent<Button> click) ->{
            //refreshAttributesViewKindsInfo();
        });
        buttonList.add(refreshAttributesViewKindsButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CONTROLLER),"自定义动作配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(attributesViewKind -> {

            Icon editIcon = new Icon(VaadinIcon.EDIT);
            editIcon.setSize("19px");
            Button editButton = new Button(editIcon, event -> {});
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.setTooltipText("移除属性视图类型");
            editButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(attributesViewKind instanceof ConceptionAction){
                        //renderDetachAttributesViewKindUI((AttributesViewKind)attributesViewKind);
                    }
                }
            });

            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("20px");
            Button removeAttributeKindButton = new Button(deleteKindIcon, event -> {});
            removeAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            removeAttributeKindButton.setTooltipText("移除属性视图类型");
            removeAttributeKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(attributesViewKind instanceof AttributesViewKind){
                        //renderDetachAttributesViewKindUI((AttributesViewKind)attributesViewKind);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(editButton,removeAttributeKindButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        attributesViewKindGrid = new Grid<>();
        attributesViewKindGrid.setWidth(100,Unit.PERCENTAGE);
        attributesViewKindGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        attributesViewKindGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributesViewKindGrid.addColumn(ConceptionAction::getActionName).setHeader("自定义动作名称").setKey("idx_0").setFlexGrow(0).setWidth("200px").setResizable(true)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getActionName());
        attributesViewKindGrid.addColumn(ConceptionAction::getActionDesc).setHeader("自定义动作描述").setKey("idx_1").setFlexGrow(0).setWidth("300px").setResizable(true)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getActionDesc());
        attributesViewKindGrid.addColumn(ConceptionAction::getActionImplementationClass).setHeader("自定义动作类全名").setKey("idx_2").setFlexGrow(1).setResizable(true);
        attributesViewKindGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_3").setFlexGrow(0).setWidth("90px").setResizable(false);
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"自定义动作名称");
        attributesViewKindGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"自定义动作描述");
        attributesViewKindGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(LineAwesomeIconsSvg.JAVA.create(),"自定义动作类全名");
        attributesViewKindGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        attributesViewKindGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3);

        attributesViewKindGrid.addSelectionListener(new SelectionListener<Grid<ConceptionAction>, ConceptionAction>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ConceptionAction>, ConceptionAction> selectionEvent) {
                Set<ConceptionAction> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    attributesViewKindGrid.select(lastSelectedAttributesViewKind);
                }else{
                    ConceptionAction selectedAttributesViewKind = selectedItemSet.iterator().next();
                    lastSelectedAttributesViewKind = selectedAttributesViewKind;


                    /*

                    if(getAttributesViewKindSelectedListener() != null){
                        getAttributesViewKindSelectedListener().attributesViewKindSelectedAction(selectedAttributesViewKind);
                    }
                    */


                }
            }
        });

        attributesViewKindGrid.appendFooterRow();
        add(attributesViewKindGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            currentBrowserHeight = event.getHeight();
            int chartHeight = currentBrowserHeight - actionsDataViewHeightOffset + 31;
            this.attributesViewKindGrid.setHeight(chartHeight,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            currentBrowserHeight = receiver.getBodyClientHeight();
            int chartHeight = currentBrowserHeight - actionsDataViewHeightOffset + 31;
            this.attributesViewKindGrid.setHeight(chartHeight,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void renderActionDataUI(Set<ConceptionAction> actionSet){
        this.attributesViewKindGrid.setItems(actionSet);
    }
}
