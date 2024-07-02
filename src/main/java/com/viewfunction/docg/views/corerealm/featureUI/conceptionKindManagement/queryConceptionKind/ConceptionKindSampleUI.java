package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConceptionKindSampleUI extends VerticalLayout {

    private String conceptionKind;
    private int sampleCount;
    private IntegerField entitiesSampleCountField;
    private HorizontalLayout mainLayout;
    private NativeLabel resultNumberValue;
    private ConceptionEntitiesListView conceptionEntitiesListView;
    private ConceptionEntitiesRelationsChart conceptionEntitiesRelationsChart;
    private Grid<AttributeValue> entityAttributesInfoGrid;
    private NativeLabel selectedConceptionEntityUIDLabel;

    public ConceptionKindSampleUI(String conceptionKind, int sampleCount) {
        this.conceptionKind = conceptionKind;
        this.sampleCount = sampleCount;

        List<Component> actionElementsList = new ArrayList<>();

        NativeLabel currentDisplayCountInfoMessage = new NativeLabel("当前采样数量:");
        currentDisplayCountInfoMessage.getStyle().set("font-size","10px").set("padding-left","5px");
        currentDisplayCountInfoMessage.addClassNames("text-tertiary");
        actionElementsList.add(currentDisplayCountInfoMessage);

        this.entitiesSampleCountField = new IntegerField();
        this.entitiesSampleCountField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        this.entitiesSampleCountField.setMin(1);
        this.entitiesSampleCountField.setStep(1);
        this.entitiesSampleCountField.setValue(this.sampleCount);
        actionElementsList.add(this.entitiesSampleCountField);

        Button resampleButton = new Button("重新采样");
        resampleButton.setIcon(VaadinIcon.REFRESH.create());
        resampleButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL);
        actionElementsList.add(resampleButton);
        resampleButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntitiesRelationsChart.clearData();
                doConceptionEntitiesSample();
            }
        });

        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(VaadinIcon.CONTROLLER.create(), "数据采样设置",actionElementsList,null);
        add(secondaryTitleActionBar);

        mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(false);
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setWidthFull();
        add(mainLayout);

        VerticalLayout leftSideContainer = new VerticalLayout();
        leftSideContainer.setWidth(260,Unit.PIXELS);
        leftSideContainer.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        leftSideContainer.setSpacing(true);
        leftSideContainer.setMargin(false);
        leftSideContainer.setPadding(false);
        mainLayout.add(leftSideContainer);

        HorizontalLayout rightSideContainer = new HorizontalLayout();
        rightSideContainer.setWidthFull();
        rightSideContainer.setSpacing(false);
        rightSideContainer.setMargin(false);
        rightSideContainer.setPadding(false);
        mainLayout.add(rightSideContainer);

        this.resultNumberValue = new NativeLabel("-");
        this.resultNumberValue.addClassNames("text-xs","font-bold");
        this.resultNumberValue.getStyle().set("padding-right","10px");

        SecondaryIconTitle filterTitle = new SecondaryIconTitle(LineAwesomeIconsSvg.CUBES_SOLID.create(),"概念类型实体采样结果",this.resultNumberValue);
        filterTitle.getStyle().set("padding-left","10px");
        leftSideContainer.add(filterTitle);
        this.conceptionEntitiesListView = new ConceptionEntitiesListView(false);
        this.conceptionEntitiesListView.setWidth(250,Unit.PIXELS);
        leftSideContainer.add(this.conceptionEntitiesListView);

        ConceptionEntitiesListView.SelectConceptionEntityListener selectConceptionEntityListener = new ConceptionEntitiesListView.SelectConceptionEntityListener() {
            @Override
            public void onSelectConceptionEntity(ConceptionEntity conceptionEntity) {
                List<AttributeValue> allAttributesList = conceptionEntity.getAttributes();
                entityAttributesInfoGrid.setItems(allAttributesList);
                selectedConceptionEntityUIDLabel.setText(conceptionEntity.getConceptionEntityUID());
            }

            @Override
            public void onUnSelectConceptionEntity() {
                entityAttributesInfoGrid.setItems(new ArrayList<>());
                selectedConceptionEntityUIDLabel.setText("-");
            }
        };
        this.conceptionEntitiesListView.setSelectConceptionEntityListener(selectConceptionEntityListener);

        this.conceptionEntitiesRelationsChart = new ConceptionEntitiesRelationsChart();
        rightSideContainer.add(this.conceptionEntitiesRelationsChart);

        VerticalLayout attributesInfoSideContainer = new VerticalLayout();
        attributesInfoSideContainer.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");
        attributesInfoSideContainer.setSpacing(true);
        attributesInfoSideContainer.setMargin(false);
        attributesInfoSideContainer.setPadding(false);
        rightSideContainer.add(attributesInfoSideContainer);

        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(VaadinIcon.CUBE.create(),"选中概念类型实体信息");
        filterTitle2.getStyle().set("padding-left","5px");
        attributesInfoSideContainer.add(filterTitle2);

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);
        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);
        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);
        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        selectedConceptionEntityUIDLabel = new NativeLabel("-");
        titleDetailLayout.add(selectedConceptionEntityUIDLabel);

        attributesInfoSideContainer.add(titleDetailLayout);

        entityAttributesInfoGrid = new Grid<>();
        entityAttributesInfoGrid.setWidthFull();
        entityAttributesInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        entityAttributesInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        entityAttributesInfoGrid.addColumn(AttributeValue::getAttributeName).setHeader("属性名称").setKey("idx_0");
        entityAttributesInfoGrid.addColumn(AttributeValue::getAttributeValue).setHeader("属性值").setKey("idx_1").setFlexGrow(1).setResizable(true);
        entityAttributesInfoGrid.getStyle().set("padding-top", "100px");

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        entityAttributesInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.INPUT,"属性值");
        entityAttributesInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);

        attributesInfoSideContainer.add(entityAttributesInfoGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            this.conceptionEntitiesListView.setHeight(receiver.getBodyClientHeight()-145, Unit.PIXELS);
            this.entityAttributesInfoGrid.setHeight(receiver.getBodyClientHeight()-175, Unit.PIXELS);
        }));
        doConceptionEntitiesSample();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        this.conceptionEntitiesRelationsChart.clearData();
        super.onDetach(detachEvent);
    }

    private void doConceptionEntitiesSample(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
        if(targetConceptionKind != null){
            try {
                int realSampleCount = entitiesSampleCountField.getValue();
                int executeSampleCount = realSampleCount == this.sampleCount ? this.sampleCount : realSampleCount;
                Set<ConceptionEntity> conceptionEntitySet = targetConceptionKind.getRandomEntities(executeSampleCount);
                int resultConceptionEntitiesCount = conceptionEntitySet.size();
                this.resultNumberValue.setText(""+resultConceptionEntitiesCount);
                this.conceptionEntitiesListView.renderConceptionEntitiesList(conceptionEntitySet);
                this.conceptionEntitiesRelationsChart.renderConceptionEntitiesList(conceptionEntitySet);
            } catch (CoreRealmServiceEntityExploreException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
