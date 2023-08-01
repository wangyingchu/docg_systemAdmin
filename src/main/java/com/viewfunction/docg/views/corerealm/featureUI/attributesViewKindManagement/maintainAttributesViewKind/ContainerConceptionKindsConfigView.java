package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import com.viewfunction.docg.element.eventHandling.AttributesViewKindAttachedToConceptionKindEvent;
import com.viewfunction.docg.element.eventHandling.AttributesViewKindDetachedFromConceptionKindEvent;
import com.viewfunction.docg.util.ResourceHolder;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContainerConceptionKindsConfigView extends VerticalLayout implements
        AttributesViewKindAttachedToConceptionKindEvent.AttributesViewKindAttachedToConceptionKindListener,
        AttributesViewKindDetachedFromConceptionKindEvent.AttributesViewKindDetachedFromConceptionKindListener{
    private String attributesViewKindUID;
    private Grid<ConceptionKind> conceptionKindGrid;
    private int containerHeight;
    public ContainerConceptionKindsConfigView(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;
        this.setWidth(100, Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button createMetaConfigItemButton= new Button("附加新的概念类型");
        createMetaConfigItemButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        createMetaConfigItemButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        createMetaConfigItemButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAttachNewConceptionKindUI();
            }
        });
        buttonList.add(createMetaConfigItemButton);

        Button refreshMetaConfigItemsInfoButton = new Button("刷新应用的概念类型信息",new Icon(VaadinIcon.REFRESH));
        refreshMetaConfigItemsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshMetaConfigItemsInfoButton.addClickListener((ClickEvent<Button> click) ->{
            refreshConceptionKindsInfo();
        });
        buttonList.add(refreshMetaConfigItemsInfoButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.CUBE),"概念类型配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(conceptionKind -> {
            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeAttributeKind = new Button(deleteKindIcon, event -> {});
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeAttributeKind.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeAttributeKind, "移除与概念类型的关联");
            removeAttributeKind.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(conceptionKind instanceof ConceptionKind){
                        renderDetachConceptionKindUI((ConceptionKind)conceptionKind);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(removeAttributeKind);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        conceptionKindGrid = new Grid<>();
        conceptionKindGrid.setWidth(100,Unit.PERCENTAGE);
        conceptionKindGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        conceptionKindGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conceptionKindGrid.addColumn(ConceptionKind::getConceptionKindName).setHeader("概念类型名称").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getConceptionKindName());
        conceptionKindGrid.addColumn(ConceptionKind::getConceptionKindDesc).setHeader("概念类型描述").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getConceptionKindDesc());
        conceptionKindGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_2").setFlexGrow(0).setWidth("90px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"概念类型名称");
        conceptionKindGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"概念类型描述");
        conceptionKindGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        conceptionKindGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2);

        conceptionKindGrid.appendFooterRow();
        add(conceptionKindGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ResourceHolder.getApplicationBlackboard().addListener(this);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        List<ConceptionKind> conceptionKindsList = targetAttributesViewKind.getContainerConceptionKinds();
        conceptionKindGrid.setItems(conceptionKindsList);
        coreRealm.closeGlobalSession();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    private void renderAttachNewConceptionKindUI(){
        AttachNewConceptionKindView attachNewConceptionKindView = new AttachNewConceptionKindView(this.attributesViewKindUID);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.PLUS_SQUARE_O),"附加概念类型",null,true,490,200,false);
        fixSizeWindow.setWindowContent(attachNewConceptionKindView);
        fixSizeWindow.setModel(true);
        attachNewConceptionKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    private void renderDetachConceptionKindUI(ConceptionKind attributeKind){
        DetachConceptionKindView detachConceptionKindView = new DetachConceptionKindView(this.attributesViewKindUID,attributeKind);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.TRASH),"移除概念类型",null,true,600,210,false);
        fixSizeWindow.setWindowContent(detachConceptionKindView);
        fixSizeWindow.setModel(true);
        detachConceptionKindView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    public void setHeight(int heightValue){
        containerHeight = heightValue;
        this.conceptionKindGrid.setHeight(containerHeight-190,Unit.PIXELS);
    }

    public void refreshConceptionKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributesViewKind targetAttributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);
        List<ConceptionKind> conceptionKindsList = targetAttributesViewKind.getContainerConceptionKinds();
        conceptionKindGrid.setItems(conceptionKindsList);
    }

    @Override
    public void receivedAttributesViewKindAttachedToConceptionKindEvent(AttributesViewKindAttachedToConceptionKindEvent event) {
        if(event.getAttributesViewKindUID() != null &&event.getConceptionKind() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                ListDataProvider dtaProvider=(ListDataProvider)conceptionKindGrid.getDataProvider();
                dtaProvider.getItems().add(event.getConceptionKind());
                dtaProvider.refreshAll();
            }
        }
    }

    @Override
    public void receivedAttributesViewKindDetachedFromConceptionKindEvent(AttributesViewKindDetachedFromConceptionKindEvent event) {
        if(event.getAttributesViewKindUID() != null && event.getConceptionKindName() != null){
            if(this.attributesViewKindUID.equals(event.getAttributesViewKindUID())){
                ListDataProvider dataProvider=(ListDataProvider)conceptionKindGrid.getDataProvider();
                Collection<ConceptionKind> itemsCollection = dataProvider.getItems();
                if(itemsCollection != null){
                    for(ConceptionKind currentConceptionKind:itemsCollection){
                        if(event.getConceptionKindName().equals(currentConceptionKind.getConceptionKindName())){
                            itemsCollection.remove(currentConceptionKind);
                            break;
                        }
                    }
                    dataProvider.refreshAll();
                }
            }
        }
    }
}
