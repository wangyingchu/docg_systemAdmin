package com.viewfunction.docg.views.corerealm.featureUI.attributeKindManagement.maintainAttributeKind;

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

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RelatedAttributesViewKindsConfigView extends VerticalLayout {
    private String attributeKindUID;
    private Grid<AttributesViewKind> attributeKindGrid;
    private AttributesViewKind lastSelectedAttributesViewKind;

    public interface AttributesViewKindSelectedListener {
        public void attributesViewKindSelectedAction(AttributesViewKind selectedAttributesViewKind);
    }
    private AttributesViewKindSelectedListener attributesViewKindSelectedListener;

    public interface AttributesViewKindsRefreshedListener {
        public void attributesViewKindsRefreshedAction();
    }
    private AttributesViewKindsRefreshedListener attributesViewKindsRefreshedListener;

    public RelatedAttributesViewKindsConfigView(String attributeKindUID){
        this.attributeKindUID = attributeKindUID;

        this.setWidth(100, Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button attachAttributesViewKindButton= new Button("附加新的属性视图类型");
        attachAttributesViewKindButton.setIcon(VaadinIcon.PLUS_SQUARE_O.create());
        attachAttributesViewKindButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        attachAttributesViewKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAttachNewAttributeKindUI();
            }
        });
        buttonList.add(attachAttributesViewKindButton);

        Button refreshAttributesViewKindsButton = new Button("刷新关联的属性视图类型信息",new Icon(VaadinIcon.REFRESH));
        refreshAttributesViewKindsButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshAttributesViewKindsButton.addClickListener((ClickEvent<Button> click) ->{
            refreshAttributesViewKindsInfo();
        });
        buttonList.add(refreshAttributesViewKindsButton);

        SecondaryTitleActionBar metaConfigItemConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TASKS),"属性视图类型配置管理 ",secTitleElementsList,buttonList);
        add(metaConfigItemConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(attributeKind -> {
            Icon deleteKindIcon = new Icon(VaadinIcon.TRASH);
            deleteKindIcon.setSize("21px");
            Button removeAttributeKindButton = new Button(deleteKindIcon, event -> {});
            removeAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeAttributeKindButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeAttributeKindButton, "移除属性类型");
            removeAttributeKindButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(attributeKind instanceof AttributeKind){
                        //renderDetachAttributeKindUI((AttributeKind)attributeKind);
                    }
                }
            });

            HorizontalLayout buttons = new HorizontalLayout(removeAttributeKindButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(15,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        attributeKindGrid = new Grid<>();
        attributeKindGrid.setWidth(100,Unit.PERCENTAGE);
        attributeKindGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        attributeKindGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributeKindGrid.addColumn(AttributesViewKind::getAttributesViewKindName).setHeader("属性视图类型名称").setKey("idx_0").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getAttributesViewKindName());
        attributeKindGrid.addColumn(AttributesViewKind::getAttributesViewKindDesc).setHeader("属性图类型描述").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getAttributesViewKindDesc());
        attributeKindGrid.addColumn(AttributesViewKind::getAttributesViewKindDataForm).setHeader("视图存储结构").setKey("idx_2")
                .setFlexGrow(0).setWidth("130px").setResizable(false);
        attributeKindGrid.addColumn(AttributesViewKind::isCollectionAttributesViewKind).setHeader("集合视图").setKey("idx_3")
                .setFlexGrow(0).setWidth("100px").setResizable(false);
        attributeKindGrid.addColumn(AttributesViewKind::getAttributesViewKindUID).setHeader("视图类型 UID").setKey("idx_4")
                .setFlexGrow(0).setWidth("130px").setResizable(false);
        attributeKindGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_5").setFlexGrow(0).setWidth("70px").setResizable(false);
        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性视图类型名称");
        attributeKindGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"属性视图类型描述");
        attributeKindGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(VaadinIcon.ELLIPSIS_H.create(),"视图存储结构");
        attributeKindGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.COINS,"集合视图");
        attributeKindGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.KEY_O,"视图类型 UID");
        attributeKindGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.TOOLS,"操作");
        attributeKindGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5);

        attributeKindGrid.addSelectionListener(new SelectionListener<Grid<AttributesViewKind>, AttributesViewKind>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<AttributesViewKind>, AttributesViewKind> selectionEvent) {
                Set<AttributesViewKind> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    // don't allow to unselect item, just reselect last selected item
                    attributeKindGrid.select(lastSelectedAttributesViewKind);
                }else{
                    AttributesViewKind selectedAttributesViewKind = selectedItemSet.iterator().next();
                    lastSelectedAttributesViewKind = selectedAttributesViewKind;
                    if(getAttributesViewKindSelectedListener() != null){
                        getAttributesViewKindSelectedListener().attributesViewKindSelectedAction(selectedAttributesViewKind);
                    }
                }
            }
        });

        attributeKindGrid.appendFooterRow();
        add(attributeKindGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //ResourceHolder.getApplicationBlackboard().addListener(this);
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
        List<AttributesViewKind> containerAttributesViewKindList = targetAttributeKind.getContainerAttributesViewKinds();
        coreRealm.closeGlobalSession();
        attributeKindGrid.setItems(containerAttributesViewKindList);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        //ResourceHolder.getApplicationBlackboard().removeListener(this);
    }

    public void setViewHeight(int viewHeight){
        this.attributeKindGrid.setHeight(viewHeight - 60,Unit.PIXELS);
    }

    public AttributesViewKindSelectedListener getAttributesViewKindSelectedListener() {
        return attributesViewKindSelectedListener;
    }

    public void setAttributesViewKindSelectedListener(AttributesViewKindSelectedListener attributesViewKindSelectedListener) {
        this.attributesViewKindSelectedListener = attributesViewKindSelectedListener;
    }

    public AttributesViewKindsRefreshedListener getAttributesViewKindsRefreshedListener() {
        return attributesViewKindsRefreshedListener;
    }

    public void setAttributesViewKindsRefreshedListener(AttributesViewKindsRefreshedListener attributesViewKindsRefreshedListener) {
        this.attributesViewKindsRefreshedListener = attributesViewKindsRefreshedListener;
    }

    private void refreshAttributesViewKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        AttributeKind targetAttributeKind = coreRealm.getAttributeKind(this.attributeKindUID);
        List<AttributesViewKind> containerAttributesViewKindList = targetAttributeKind.getContainerAttributesViewKinds();
        coreRealm.closeGlobalSession();
        attributeKindGrid.setItems(containerAttributesViewKindList);
        if(getAttributesViewKindsRefreshedListener() != null){
            getAttributesViewKindsRefreshedListener().attributesViewKindsRefreshedAction();
        }
    }
}
