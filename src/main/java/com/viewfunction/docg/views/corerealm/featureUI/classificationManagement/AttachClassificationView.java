package com.viewfunction.docg.views.corerealm.featureUI.classificationManagement;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.*;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AttachClassificationView extends VerticalLayout {
    private ClassificationMetaInfo classificationMetaInfo;
    private Dialog containerDialog;
    private Grid<ClassificationMetaInfo> classificationsMetaInfoFilterGrid;
    private GridListDataView<ClassificationMetaInfo> classificationMetaInfosMetaInfoFilterView;
    private TextField classificationDescFilterField;
    private TextField classificationNameFilterField;
    private Button executeAttachRelationButton;
    private SecondaryTitleActionBar selectedParentTitleActionBar;
    private ClassificationMetaInfo selectedParentClassificationMetaInfo;

    public AttachClassificationView(ClassificationMetaInfo classificationMetaInfo){
        this.classificationMetaInfo = classificationMetaInfo;

        Icon kindIcon = VaadinIcon.TAGS.create();
        String viewTitleText = this.classificationMetaInfo.getClassificationName() +" ("+this.classificationMetaInfo.getClassificationDesc()+")";
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");

        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, viewTitleText));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);

        ThirdLevelIconTitle infoTitle4 = new ThirdLevelIconTitle(new Icon(VaadinIcon.TAGS),"选择目标父分类");
        add(infoTitle4);

        VerticalLayout classificationMetaInfoGridContainerLayout = new VerticalLayout();
        classificationMetaInfoGridContainerLayout.setSpacing(true);
        classificationMetaInfoGridContainerLayout.setMargin(false);
        classificationMetaInfoGridContainerLayout.setPadding(false);

        HorizontalLayout classificationsSearchElementsContainerLayout = new HorizontalLayout();
        classificationsSearchElementsContainerLayout.setSpacing(false);
        classificationsSearchElementsContainerLayout.setMargin(false);
        classificationMetaInfoGridContainerLayout.add(classificationsSearchElementsContainerLayout);

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(new Icon(VaadinIcon.FILTER),"过滤条件");
        classificationsSearchElementsContainerLayout.add(filterTitle);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,filterTitle);
        filterTitle.setWidth(80, Unit.PIXELS);

        classificationNameFilterField = new TextField();
        classificationNameFilterField.setPlaceholder("分类名称");
        classificationNameFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationNameFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationNameFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationNameFilterField);

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("12px");
        classificationsSearchElementsContainerLayout.add(plusIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,plusIcon);

        classificationDescFilterField = new TextField();
        classificationDescFilterField.setPlaceholder("分类描述");
        classificationDescFilterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        classificationDescFilterField.setWidth(170,Unit.PIXELS);
        classificationsSearchElementsContainerLayout.add(classificationDescFilterField);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER, classificationDescFilterField);

        Button searchClassificationsButton = new Button("查找分类",new Icon(VaadinIcon.SEARCH));
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        searchClassificationsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(searchClassificationsButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,searchClassificationsButton);
        searchClassificationsButton.setWidth(90,Unit.PIXELS);
        searchClassificationsButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                filterClassifications();
            }
        });

        Icon divIcon = new Icon(VaadinIcon.LINE_V);
        divIcon.setSize("8px");
        classificationsSearchElementsContainerLayout.add(divIcon);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,divIcon);

        Button clearSearchCriteriaButton = new Button("清除查询条件",new Icon(VaadinIcon.ERASER));
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearSearchCriteriaButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        classificationsSearchElementsContainerLayout.add(clearSearchCriteriaButton);
        classificationsSearchElementsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,clearSearchCriteriaButton);
        clearSearchCriteriaButton.setWidth(120,Unit.PIXELS);
        clearSearchCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                cancelFilterClassifications();
            }
        });
        add(classificationsSearchElementsContainerLayout);

        classificationsMetaInfoFilterGrid = new Grid<>();
        classificationsMetaInfoFilterGrid.setWidth(720,Unit.PIXELS);
        classificationsMetaInfoFilterGrid.setHeight(350,Unit.PIXELS);
        classificationsMetaInfoFilterGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        classificationsMetaInfoFilterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        classificationsMetaInfoFilterGrid.addColumn(ClassificationMetaInfo::getClassificationName).setKey("idx_0").setHeader("分类名称");
        classificationsMetaInfoFilterGrid.addColumn(ClassificationMetaInfo::getClassificationDesc).setKey("idx_1").setHeader("分类描述");
        classificationsMetaInfoFilterGrid.addColumn(ClassificationMetaInfo::getChildClassificationCount).setKey("idx_2").setHeader("子分类数量")
                .setFlexGrow(0).setWidth("110px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0A = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"分类名称");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0A).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1A = new GridColumnHeader(VaadinIcon.DESKTOP,"分类描述");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1A).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2A = new GridColumnHeader(VaadinIcon.ROAD_BRANCHES,"子分类数量");
        classificationsMetaInfoFilterGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2A).setSortable(true);

        classificationsMetaInfoFilterGrid.addSelectionListener(new SelectionListener<Grid<ClassificationMetaInfo>, ClassificationMetaInfo>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<ClassificationMetaInfo>, ClassificationMetaInfo> selectionEvent) {
                Set<ClassificationMetaInfo> selectedItemSet = selectionEvent.getAllSelectedItems();
                if(selectedItemSet.size() == 0){
                    //unselected item, reset link detail info
                    clearSelectedParentClassification();
                }else{
                    ClassificationMetaInfo selectedClassificationMetaInfo = selectedItemSet.iterator().next();
                    showSelectedParentClassification(selectedClassificationMetaInfo);
                }
            }
        });

        add(classificationsMetaInfoFilterGrid);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<ClassificationMetaInfo> classificationsMetaInfoList = coreRealm.getClassificationsMetaInfo();
            this.classificationMetaInfosMetaInfoFilterView = classificationsMetaInfoFilterGrid.setItems(classificationsMetaInfoList);
            this.classificationMetaInfosMetaInfoFilterView.addFilter(item->{
                String entityKindName = item.getClassificationName();
                String entityKindDesc = item.getClassificationDesc();
                boolean attributeKindNameFilterResult = true;
                if(!classificationNameFilterField.getValue().trim().equals("")){
                    if(entityKindName.contains(classificationNameFilterField.getValue().trim())){
                        attributeKindNameFilterResult = true;
                    }else{
                        attributeKindNameFilterResult = false;
                    }
                }
                boolean attributeKindDescFilterResult = true;
                if(!classificationDescFilterField.getValue().trim().equals("")){
                    if(entityKindDesc.contains(classificationDescFilterField.getValue().trim())){
                        attributeKindDescFilterResult = true;
                    }else{
                        attributeKindDescFilterResult = false;
                    }
                }
                return attributeKindNameFilterResult & attributeKindDescFilterResult;
            });
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        add(buttonsContainerLayout);

        executeAttachRelationButton = new Button("设定父分类");
        executeAttachRelationButton.setIcon(new Icon(VaadinIcon.LINK));
        executeAttachRelationButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeAttachRelationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                attachClassification();
            }
        });
        buttonsContainerLayout.add(executeAttachRelationButton);
        executeAttachRelationButton.setEnabled(false);
        selectedParentTitleActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAG),"-",null,null,false);
        buttonsContainerLayout.add(selectedParentTitleActionBar);
        buttonsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER);
        buttonsContainerLayout.setVerticalComponentAlignment(Alignment.CENTER,selectedParentTitleActionBar);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void filterClassifications(){
        String classificationFilterValue = classificationNameFilterField.getValue().trim();
        String classificationDescFilterValue = classificationDescFilterField.getValue().trim();
        if(classificationFilterValue.equals("")&classificationDescFilterValue.equals("")){
            CommonUIOperationUtil.showPopupNotification("请输入分类名称 和/或 分类描述", NotificationVariant.LUMO_ERROR);
        }else{
            this.classificationMetaInfosMetaInfoFilterView.refreshAll();
        }
    }

    private void cancelFilterClassifications(){
        classificationNameFilterField.setValue("");
        classificationDescFilterField.setValue("");
        this.classificationMetaInfosMetaInfoFilterView.refreshAll();
    }

    private void clearSelectedParentClassification(){
        this.selectedParentClassificationMetaInfo = null;
        this.executeAttachRelationButton.setEnabled(false);
        this.selectedParentTitleActionBar.updateTitleContent("-");
    }

    private void showSelectedParentClassification(ClassificationMetaInfo selectedClassificationMetaInfo){
        this.selectedParentClassificationMetaInfo = selectedClassificationMetaInfo;
        this.executeAttachRelationButton.setEnabled(true);
        this.selectedParentTitleActionBar.updateTitleContent(this.selectedParentClassificationMetaInfo.getClassificationName());
    }

    private void attachClassification(){}
}
