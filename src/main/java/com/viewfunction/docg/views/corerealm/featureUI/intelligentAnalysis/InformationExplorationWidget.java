package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;

import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.*;
import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityDetailUI;
import com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.maintainRelationEntity.RelationEntityDetailUI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InformationExplorationWidget extends VerticalLayout {

    private String explorationQuery;
    private Grid<Map<String,DynamicContentValue>> queryResultGrid;
    private Details informationExplorationResultDetails;

    public InformationExplorationWidget(String question,String explorationQuery){
        this.setWidthFull();
        this.explorationQuery = explorationQuery;

        Icon operationIcon = LineAwesomeIconsSvg.SQUARE.create();
        operationIcon.setSize("12px");
        operationIcon.getStyle().set("padding-right","1px");

        Span explorationQuestionSpan = new Span(question);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now =LocalDateTime.now();
        Span timeSpan = new Span("["+now.format(formatter)+ "] ");

        Icon reRunIcon = new Icon(VaadinIcon.REFRESH);
        reRunIcon.setSize("16px");
        Button reRunButton = new Button(reRunIcon, event -> {
            //renderConceptionKindConfigurationUI(conceptionKindName);
        });
        reRunButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        reRunButton.setTooltipText("重新执行探索");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.add(operationIcon,timeSpan,explorationQuestionSpan);

        informationExplorationResultDetails = new Details(horizontalLayout);
        informationExplorationResultDetails.addThemeVariants(DetailsVariant.REVERSE);
        informationExplorationResultDetails.setWidthFull();

        informationExplorationResultDetails.setOpened(true);
        add(informationExplorationResultDetails);
        informationExplorationResultDetails.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");

        HorizontalLayout explorationQueryControlLayout = new HorizontalLayout();
        explorationQueryControlLayout.setHeight(20, Unit.PIXELS);
        explorationQueryControlLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        explorationQueryControlLayout.getStyle().set("background-color", "var(--lumo-contrast-5pct)");

        informationExplorationResultDetails.add(explorationQueryControlLayout);

        Icon explorationQueryIcon = LineAwesomeIconsSvg.DIGITAL_TACHOGRAPH_SOLID.create();
        explorationQueryIcon.getStyle().set("padding-left","5px");
        explorationQueryIcon.setTooltipText("探索查询语句");
        Popover popover1 = new Popover();
        popover1.setTarget(explorationQueryIcon);
        popover1.setWidth("500px");
        popover1.setHeight("280px");
        popover1.addThemeVariants(PopoverVariant.ARROW,PopoverVariant.LUMO_NO_PADDING);
        popover1.setPosition(PopoverPosition.BOTTOM_START);
        popover1.setModal(true);
        popover1.add(new ExplorationQueryInfoWidget(explorationQuery));

        Icon attributrValuesMapIcon = LineAwesomeIconsSvg.BARS_SOLID.create();
        attributrValuesMapIcon.getStyle().set("padding-left","5px");
        attributrValuesMapIcon.setTooltipText("探索结果属性值信息");
        Popover popover2 = new Popover();
        popover2.setTarget(attributrValuesMapIcon);
        popover2.setWidth("500px");
        popover2.setHeight("280px");
        popover2.addThemeVariants(PopoverVariant.ARROW,PopoverVariant.LUMO_NO_PADDING);
        popover2.setPosition(PopoverPosition.BOTTOM_START);
        popover2.setModal(true);
        popover2.add(new ExplorationQueryInfoWidget(explorationQuery));

        Icon queryResultStaticIcon = LineAwesomeIconsSvg.CHALKBOARD_SOLID.create();
        queryResultStaticIcon.getStyle().set("padding-left","5px");
        queryResultStaticIcon.setTooltipText("探索结果内容解读");
        Popover popover3 = new Popover();
        popover3.setTarget(queryResultStaticIcon);
        popover3.setWidth("500px");
        popover3.setHeight("280px");
        popover3.addThemeVariants(PopoverVariant.ARROW,PopoverVariant.LUMO_NO_PADDING);
        popover3.setPosition(PopoverPosition.BOTTOM_START);
        popover3.setModal(true);
        popover3.add(new ExplorationQueryInfoWidget(explorationQuery));

        explorationQueryControlLayout.add(explorationQueryIcon,attributrValuesMapIcon,queryResultStaticIcon,reRunButton);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderExplorationQueryResult();
    }

    private void renderExplorationQueryResult(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
        try {
            DynamicContentQueryResult dynamicContentQueryResult = crossKindDataOperator.executeAdhocQuery(explorationQuery);
            if (dynamicContentQueryResult != null) {
                Map<String, DynamicContentValue.ContentValueType> contentValueMap =
                        dynamicContentQueryResult.getDynamicContentAttributesValueTypeMap();

                queryResultGrid = new Grid<>();
                queryResultGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_COLUMN_BORDERS,GridVariant.LUMO_COMPACT,GridVariant.LUMO_WRAP_CELL_CONTENT);
                TabSheet tabSheet = new TabSheet();
                tabSheet.add("数据", queryResultGrid);
                tabSheet.add("图谱", new Div(new Text("This is the Graph tab content")));
                tabSheet.add("解读", new Div(new Text("This is the Explanation tab content")));
                add(tabSheet);
                informationExplorationResultDetails.add(tabSheet);

                List<Map<String,DynamicContentValue>> dynamicContentValueResultList = dynamicContentQueryResult.getDynamicContentResultValueList();
                //long resultContentValue = dynamicContentQueryResult.getDynamicContentValuesCount();
                contentValueMap.forEach((key, value) -> {
                    ComponentRenderer dynamicContentValueComponentRenderer = new ComponentRenderer(dynamicContentValueObj -> {
                        if(dynamicContentValueObj != null && dynamicContentValueObj instanceof Map){
                            Map<String,DynamicContentValue> dynamicContentValueMap = (Map<String,DynamicContentValue>)dynamicContentValueObj;
                            DynamicContentValue dynamicContentValue = dynamicContentValueMap.get(key);
                            DynamicContentValue.ContentValueType contentValueType = dynamicContentValue.getValueType();
                            Object contentObject = dynamicContentValue.getValueObject();

                            if(DynamicContentValue.ContentValueType.CONCEPTION_ENTITY.equals(contentValueType)){
                                ConceptionEntity conceptionEntity = (ConceptionEntity)contentObject;
                                Icon showConceptionEntityIcon = new Icon(VaadinIcon.CUBE);
                                showConceptionEntityIcon.setSize("15px");
                                Button showConceptionEntityButton = new Button(showConceptionEntityIcon, event -> {
                                    renderConceptionEntityUI(conceptionEntity);
                                });
                                showConceptionEntityButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
                                showConceptionEntityButton.setTooltipText("显示概念实体详情");
                                return showConceptionEntityButton;
                            }else if(DynamicContentValue.ContentValueType.RELATION_ENTITY.equals(contentValueType)){
                                RelationEntity relationEntity = (RelationEntity)contentObject;
                                Icon showRelationEntityIcon = new Icon(VaadinIcon.CONNECT_O);
                                showRelationEntityIcon.setSize("18px");
                                Button showRelationEntityButton = new Button(showRelationEntityIcon, event -> {
                                    renderRelationEntityUI(relationEntity);
                                });
                                showRelationEntityButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
                                showRelationEntityButton.setTooltipText("显示关系实体详情");
                                return showRelationEntityButton;
                            }else if(DynamicContentValue.ContentValueType.ENTITIES_PATH.equals(contentValueType)){
                                EntitiesPath entitiesPath = (EntitiesPath)contentObject;
                                return new NativeLabel("CC");
                            }else{
                                return new NativeLabel(dynamicContentValue.getValueObject().toString());
                            }
                        }else{
                            return new NativeLabel("");
                        }
                    });
                    queryResultGrid.addColumn(dynamicContentValueComponentRenderer).setHeader(" " + key).setKey(key + "_KEY");
                    queryResultGrid.getColumnByKey(key + "_KEY").setSortable(true).setResizable(true);
                });
                queryResultGrid.setItems(dynamicContentValueResultList);
            }
        } catch(CoreRealmServiceEntityExploreException e){
            throw new RuntimeException(e);
        }
    }

    private void renderConceptionEntityUI(ConceptionEntity conceptionEntity){
        String conceptionKindName =conceptionEntity.getConceptionKindName();
        String conceptionEntityUID = conceptionEntity.getConceptionEntityUID();

        ConceptionEntityDetailUI conceptionEntityDetailUI = new ConceptionEntityDetailUI(conceptionKindName,conceptionEntityUID);

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        titleDetailLayout.add(conceptionKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(conceptionKindName);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
        conceptionEntityIcon.setSize("10px");
        titleDetailLayout.add(conceptionEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel conceptionEntityUIDLabel = new NativeLabel(conceptionEntityUID);
        titleDetailLayout.add(conceptionEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(conceptionEntityDetailUI);
        conceptionEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }

    private void renderRelationEntityUI(RelationEntity relationEntity){
        String relationKindName =relationEntity.getRelationKindName();
        String relationEntityUID = relationEntity.getRelationEntityUID();
        RelationEntityDetailUI relationEntityDetailUI = new RelationEntityDetailUI(relationKindName,relationEntityUID);

        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon relationKindIcon = VaadinIcon.CONNECT_O.create();
        relationKindIcon.setSize("10px");
        titleDetailLayout.add(relationKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel conceptionKindNameLabel = new NativeLabel(relationKindName);
        titleDetailLayout.add(conceptionKindNameLabel);

        HorizontalLayout spaceDivLayout3 = new HorizontalLayout();
        spaceDivLayout3.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout3);

        Icon divIcon = VaadinIcon.ITALIC.create();
        divIcon.setSize("8px");
        titleDetailLayout.add(divIcon);

        HorizontalLayout spaceDivLayout4 = new HorizontalLayout();
        spaceDivLayout4.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout4);

        Icon relationEntityIcon = VaadinIcon.KEY_O.create();
        relationEntityIcon.setSize("10px");
        titleDetailLayout.add(relationEntityIcon);

        HorizontalLayout spaceDivLayout5 = new HorizontalLayout();
        spaceDivLayout5.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout5);
        NativeLabel relationEntityUIDLabel = new NativeLabel(relationEntityUID);
        titleDetailLayout.add(relationEntityUIDLabel);

        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"关系实体详情",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(relationEntityDetailUI);
        relationEntityDetailUI.setContainerDialog(fullScreenWindow);
        fullScreenWindow.show();
    }
}
