package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;

import com.vaadin.flow.function.ValueProvider;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentQueryResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class InformationExplorationWidget extends VerticalLayout {

    private String explorationQuery;
    private Grid<DynamicContentValue> queryResultGrid;
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

        Icon explorationQueryIcon = LineAwesomeIconsSvg.DIGITAL_TACHOGRAPH_SOLID.create();
        explorationQueryIcon.getStyle().set("padding-left","5px");
        explorationQueryControlLayout.add(explorationQueryIcon,reRunButton);

        informationExplorationResultDetails.add(explorationQueryControlLayout);

        Popover popover = new Popover();
        popover.setTarget(explorationQueryIcon);
        popover.setWidth("500px");
        popover.setHeight("280px");
        popover.addThemeVariants(PopoverVariant.ARROW,PopoverVariant.LUMO_NO_PADDING);
        popover.setPosition(PopoverPosition.BOTTOM_START);
        popover.setModal(true);
        popover.add(new ExplorationQueryInfoWidget(explorationQuery));
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
                informationExplorationResultDetails.add(queryResultGrid);
                List<DynamicContentValue> dynamicContentValueList = dynamicContentQueryResult.getDynamicContentValueList();

                long resultContentValue = dynamicContentQueryResult.getDynamicContentValuesCount();

                contentValueMap.forEach((key, value) -> {
                    queryResultGrid.addColumn(new ValueProvider<DynamicContentValue, Object>() {

                        @Override
                        public Object apply(DynamicContentValue dynamicContentValue) {
                            return dynamicContentValue.getValueObject();
                        }
                    }).setHeader(" " + key).setKey(key + "_KEY");
                    queryResultGrid.getColumnByKey(key + "_KEY").setSortable(true).setResizable(true);;
                });
                queryResultGrid.setItems(dynamicContentValueList);
            }
        } catch(CoreRealmServiceEntityExploreException e){
            throw new RuntimeException(e);
        }
    }
}
