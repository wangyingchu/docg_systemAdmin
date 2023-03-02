package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.ProcessingListView;

import java.util.ArrayList;
import java.util.List;

public class CreateRelationEntityView extends VerticalLayout {

    private ComboBox relationKindSelect;
    private RadioButtonGroup<String> relationDirectionRadioGroup;

    public CreateRelationEntityView(String conceptionKind,String conceptionEntityUID){
        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("12px");
        conceptionKindIcon.getStyle().set("padding-right","3px");
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("18px");
        conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon,conceptionKind));
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon,conceptionEntityUID));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout);

        HorizontalLayout mainContainerLayout = new HorizontalLayout();
        add(mainContainerLayout);

        VerticalLayout basicInfoContainerLayout = new VerticalLayout();
        mainContainerLayout.add(basicInfoContainerLayout);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CONNECT_O),"选择关系类型");
        basicInfoContainerLayout.add(infoTitle1);

        relationKindSelect = new ComboBox();
        relationKindSelect.setPageSize(30);
        relationKindSelect.setPlaceholder("选择关系类型定义");
        relationKindSelect.setWidth(250,Unit.PIXELS);
        basicInfoContainerLayout.add(relationKindSelect);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.EXCHANGE),"选择关系方向");
        basicInfoContainerLayout.add(infoTitle2);

        relationDirectionRadioGroup = new RadioButtonGroup<>();
        relationDirectionRadioGroup.setItems("FROM", "TO", "BOTH");
        relationDirectionRadioGroup.setValue("FROM");
        basicInfoContainerLayout.add(relationDirectionRadioGroup);

        ThirdLevelIconTitle infoTitle3 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定关系属性");
        basicInfoContainerLayout.add(infoTitle3);

        VerticalLayout relationEntityAttributesContainer = new VerticalLayout();
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

        Button executeQueryButton = new Button("查询概念实体");
        executeQueryButton.setIcon(new Icon(VaadinIcon.SEARCH));
        executeQueryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        basicInfoContainerLayout.add(executeQueryButton);

        VerticalLayout targetConceptionEntitiesInfoContainerLayout = new VerticalLayout();
        targetConceptionEntitiesInfoContainerLayout.setSpacing(false);
        mainContainerLayout.add(targetConceptionEntitiesInfoContainerLayout);

        ThirdLevelIconTitle infoTitle4 = new ThirdLevelIconTitle(new Icon(VaadinIcon.CUBES),"选择目标概念实体");
        targetConceptionEntitiesInfoContainerLayout.add(infoTitle4);

        ProcessingListView processingListView = new ProcessingListView(500);
        targetConceptionEntitiesInfoContainerLayout.add(processingListView);
    }

    private Dialog containerDialog;

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
