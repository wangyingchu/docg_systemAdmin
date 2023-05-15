package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeEditorItemWidget;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.processingDataList.ProcessingConceptionEntityListView;
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelateClassificationView extends VerticalLayout {

    private ClassificationConfigView.ClassificationRelatedObjectType classificationRelatedObjectType;
    private String relatedObjectID;
    private Dialog containerDialog;
    private ClassificationConfigView containerClassificationConfigView;



    private ComboBox<KindMetaInfo> relationKindSelect;
    private RadioButtonGroup<String> relationDirectionRadioGroup;
    private ProcessingConceptionEntityListView processingConceptionEntityListView;
    private Checkbox allowDupTypeRelationCheckbox;
    private String conceptionKind;
    private String conceptionEntityUID;
    private VerticalLayout relationEntityAttributesContainer;
    private Map<String, AttributeEditorItemWidget> relationAttributeEditorsMap;
    private Button clearAttributeButton;

    public RelateClassificationView(ClassificationConfigView.ClassificationRelatedObjectType
                                            classificationRelatedObjectType,String relatedObjectID){
        this.classificationRelatedObjectType = classificationRelatedObjectType;
        this.relatedObjectID = relatedObjectID;
        this.setMargin(false);
        this.setWidthFull();

        Icon kindIcon = VaadinIcon.CUBE.create();
        String viewTitleText = "概念类型索引信息";
        switch (this.classificationRelatedObjectType){
            case ConceptionKind :
                kindIcon = VaadinIcon.CUBE.create();
                viewTitleText = "概念类型索引信息";
                break;
            case RelationKind :
                kindIcon = VaadinIcon.CONNECT_O.create();
                viewTitleText = "关系类型索引信息";
                break;
            case AttributeKind:
                kindIcon = VaadinIcon.INPUT.create();
                viewTitleText = "关系类型索引信息";
                break;
            case AttributesViewKind:
                kindIcon = VaadinIcon.TASKS.create();
                viewTitleText = "关系类型索引信息";
                break;
            case ConceptionEntity:
                kindIcon = LineAwesomeIconsSvg.KEYCDN.create();
                viewTitleText = "关系类型索引信息";
                break;
        }
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, relatedObjectID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);








        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        add(mainContainerLayout);

        VerticalLayout basicInfoContainerLayout = new VerticalLayout();
        basicInfoContainerLayout.setWidth(350,Unit.PIXELS);
        mainContainerLayout.add(basicInfoContainerLayout);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT_O),"选择关系类型");
        basicInfoContainerLayout.add(infoTitle1);

        relationKindSelect = new ComboBox();
        relationKindSelect.setPageSize(30);
        relationKindSelect.setPlaceholder("选择关系类型定义");
        relationKindSelect.setWidth(340,Unit.PIXELS);

        relationKindSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo kindMetaInfo) {
                String itemLabelValue = kindMetaInfo.getKindName()+ " ("+
                        kindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        basicInfoContainerLayout.add(relationKindSelect);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.EXCHANGE),"选择关系方向");
        basicInfoContainerLayout.add(infoTitle2);

        relationDirectionRadioGroup = new RadioButtonGroup<>();
        relationDirectionRadioGroup.setItems("FROM", "TO");
        relationDirectionRadioGroup.setValue("FROM");
        basicInfoContainerLayout.add(relationDirectionRadioGroup);

        HorizontalLayout addRelationAttributesUIContainerLayout = new HorizontalLayout();
        addRelationAttributesUIContainerLayout.setSpacing(false);
        addRelationAttributesUIContainerLayout.setMargin(false);
        addRelationAttributesUIContainerLayout.setPadding(false);
        basicInfoContainerLayout.add(addRelationAttributesUIContainerLayout);

        addRelationAttributesUIContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定关系属性");
        addRelationAttributesUIContainerLayout.add(infoTitle3);

        Button addAttributeButton = new Button();
        Tooltips.getCurrent().setTooltip(addAttributeButton, "添加关系实体属性");
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        addAttributeButton.setIcon(VaadinIcon.KEYBOARD_O.create());
        addAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //renderAddNewAttributeUI();
            }
        });
        addRelationAttributesUIContainerLayout.add(addAttributeButton);

        clearAttributeButton = new Button();
        clearAttributeButton.setEnabled(false);
        Tooltips.getCurrent().setTooltip(clearAttributeButton, "清除已设置关系实体属性");
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearAttributeButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        clearAttributeButton.setIcon(VaadinIcon.RECYCLE.create());
        clearAttributeButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //cleanRelationAttributes();
            }
        });
        addRelationAttributesUIContainerLayout.add(clearAttributeButton);

        relationEntityAttributesContainer = new VerticalLayout();
        relationEntityAttributesContainer.setMargin(false);
        relationEntityAttributesContainer.setSpacing(false);
        relationEntityAttributesContainer.setPadding(false);
        relationEntityAttributesContainer.setWidth(100,Unit.PERCENTAGE);

        Scroller relationEntityAttributesScroller = new Scroller(relationEntityAttributesContainer);
        relationEntityAttributesScroller.setHeight(300,Unit.PIXELS);
        relationEntityAttributesScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        basicInfoContainerLayout.add(relationEntityAttributesScroller);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        basicInfoContainerLayout.add(spaceDivLayout2);

        HorizontalLayout buttonsContainerLayout = new HorizontalLayout();
        buttonsContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        basicInfoContainerLayout.add(buttonsContainerLayout);

        Button executeCreateRelationButton = new Button("创建实体关联");
        executeCreateRelationButton.setIcon(new Icon(VaadinIcon.LINK));
        executeCreateRelationButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        executeCreateRelationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                //creatConceptionEntityRelations();
            }
        });
        buttonsContainerLayout.add(executeCreateRelationButton);

        allowDupTypeRelationCheckbox = new Checkbox();
        buttonsContainerLayout.add(allowDupTypeRelationCheckbox);
        Label chechboxDescLabel = new Label("允许重复创建同类型关系");
        chechboxDescLabel.addClassNames("text-xs","text-tertiary");
        chechboxDescLabel.setWidth(80,Unit.PIXELS);
        buttonsContainerLayout.add(chechboxDescLabel);

        VerticalLayout targetConceptionEntitiesInfoContainerLayout = new VerticalLayout();
        targetConceptionEntitiesInfoContainerLayout.setSpacing(false);
        mainContainerLayout.add(targetConceptionEntitiesInfoContainerLayout);

        ThirdLevelIconTitle infoTitle4 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CUBES),"选择目标概念实体");
        targetConceptionEntitiesInfoContainerLayout.add(infoTitle4);

        processingConceptionEntityListView = new ProcessingConceptionEntityListView(500);
        targetConceptionEntitiesInfoContainerLayout.add(processingConceptionEntityListView);

        this.relationAttributeEditorsMap = new HashMap<>();
















/*
        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT_O),"选择关系类型");
        add(infoTitle1);

        ComboBox relationKindSelect = new ComboBox();
        relationKindSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        relationKindSelect.setPageSize(30);
        relationKindSelect.setPlaceholder("选择关系类型定义");
        relationKindSelect.setWidth(100, Unit.PERCENTAGE);

        relationKindSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo kindMetaInfo) {
                String itemLabelValue = kindMetaInfo.getKindName()+ " ("+
                        kindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        add(relationKindSelect);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.EXCHANGE),"选择关系方向");
        add(infoTitle2);

        RadioButtonGroup<String> relationDirectionRadioGroup = new RadioButtonGroup<>();
        relationDirectionRadioGroup.setItems("FROM", "TO");
        relationDirectionRadioGroup.setValue("FROM");
        add(relationDirectionRadioGroup);

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT_O),"选择分类");
        add(infoTitle3);

        ComboBox classificationSelect = new ComboBox();
        classificationSelect.setPageSize(30);
        classificationSelect.setPlaceholder("选择分类");
        classificationSelect.setWidth(100, Unit.PERCENTAGE);
        add(classificationSelect);
*/








    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setContainerClassificationConfigView(ClassificationConfigView containerClassificationConfigView) {
        this.containerClassificationConfigView = containerClassificationConfigView;
    }
}
