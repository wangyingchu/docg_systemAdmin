package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentQueryResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.DynamicContentValue;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class InformationInsightWidget extends VerticalLayout {

    private String explorationQuery;
    private Grid<Map<String, DynamicContentValue>> queryResultGrid;
    private Details informationExplorationResultDetails;
    private Popover popover2;
    private Popover popover3;
    private TabSheet contentTabSheet;
    private String question;
    private ExplorationQueryInfoWidget explorationQueryInfoWidget;
    private Span questionSpan;
    private Button editAndReQueryButton;
    private Button cancelEditAndReQueryButton;
    private Button confirmEditAndReQueryButton;
    private TextField questionEditField;
    private DynamicContentQueryResult dynamicContentQueryResult;
    private QueryResultInsightWidget queryResultInsightWidget;
    private QueryResultGraphWidget queryResultGraphWidget;
    private HorizontalLayout doesNotContainsDataInfoMessage;
    private Button fullScreenDisplayButton;
    private Button resetScreenDisplayButton;
    private int widgetContentHeight;
    private int explorationContentHeight = 100;

    public InformationInsightWidget(String question,
                                    List<String> insightScopeConceptionKindList,
                                    List<String> insightScopeRelationKindList,
                                    List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList,
                                    int widgetContentHeight){
        this.setWidthFull();
        this.widgetContentHeight = widgetContentHeight;
        this.explorationContentHeight = this.widgetContentHeight -30;
        this.question = question;


        this.setWidthFull();
        this.widgetContentHeight = widgetContentHeight;
        this.explorationContentHeight = this.widgetContentHeight -30;
        this.question = question;
        this.explorationQuery = explorationQuery;
        Icon operationIcon = LineAwesomeIconsSvg.BINOCULARS_SOLID.create();
        operationIcon.setSize("16px");
        operationIcon.getStyle().set("padding-right","1px");

        NativeLabel operationLabel = new NativeLabel("洞察");

        questionEditField = new TextField();
        questionEditField.setWidth(80, Unit.PERCENTAGE);
        questionEditField.setValue(question);
        questionEditField.setPrefixComponent(VaadinIcon.QUESTION.create());
        questionEditField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        questionEditField.setVisible(false);

        Icon editIcon = VaadinIcon.EDIT.create();
        editIcon.setSize("14px");
        editAndReQueryButton = new Button(editIcon);
        editAndReQueryButton.setTooltipText("编辑探查问题并重新探索");
        editAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        editAndReQueryButton.addClickListener((ClickEvent<Button> click) ->{
            //doEdit();
        });

        Icon arrowBackwardIcon = VaadinIcon.ARROW_BACKWARD.create();
        arrowBackwardIcon.setSize("14px");
        cancelEditAndReQueryButton = new Button(arrowBackwardIcon);
        cancelEditAndReQueryButton.setTooltipText("取消编辑");
        cancelEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        cancelEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        cancelEditAndReQueryButton.addClickListener((ClickEvent<Button> click) ->{
            //cancelEdit();
        });
        cancelEditAndReQueryButton.setVisible(false);

        Icon checkIcon = VaadinIcon.CHECK.create();
        checkIcon.setSize("14px");
        confirmEditAndReQueryButton = new Button(checkIcon);
        confirmEditAndReQueryButton.setTooltipText("确认编辑并重新探索");
        confirmEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        confirmEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        confirmEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        confirmEditAndReQueryButton.addClickListener((ClickEvent<Button> click) ->{
            //confirmEditAndQuery();
        });
        confirmEditAndReQueryButton.setVisible(false);

        questionSpan = new Span(question);
        questionSpan.getStyle()
                .set("font-size","var(--lumo-font-size-m)")
                .set("font-weight","bolder")
                .set("font-style","oblique").set("padding-right","5px");
        Span explorationQuestionSpan = new Span(questionSpan,questionEditField,editAndReQueryButton,cancelEditAndReQueryButton,confirmEditAndReQueryButton);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now =LocalDateTime.now();
        Span timeSpan = new Span("["+now.format(formatter)+ "] ");

        Icon reRunIcon = new Icon(VaadinIcon.REFRESH);
        reRunIcon.setSize("16px");
        Button reRunButton = new Button(reRunIcon, event -> {
            //reCalculateExplorationQuery();
        });
        reRunButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        reRunButton.setTooltipText("重新执行探索");

        /*
        Icon fullScreenDisplayIcon = new Icon(VaadinIcon.EXPAND_FULL);
        fullScreenDisplayIcon.setSize("14px");
        fullScreenDisplayButton = new Button(fullScreenDisplayIcon);
        fullScreenDisplayButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        fullScreenDisplayButton.setTooltipText("全屏显示");
        fullScreenDisplayButton.addClickListener((event -> {
            informationExplorationResultDetails.setOpened(true);
            queryResultInsightWidget.setHeight(browserPageHeight-150,Unit.PIXELS);
            resetScreenDisplayButton.setVisible(true);
            fullScreenDisplayButton.setVisible(false);
            this.getElement().removeFromParent();
            FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.RECORDS),"概念实体详情",null,null,true);
            fullScreenWindow.setWindowContent(this);
            fullScreenWindow.show();
        }));
       // Fullscreen.onClick(fullScreenDisplayButton).enter(this);

        Icon resetScreenDisplayIcon = new Icon(VaadinIcon.COMPRESS_SQUARE);
        resetScreenDisplayIcon.setSize("14px");
        resetScreenDisplayButton = new Button(resetScreenDisplayIcon);
        resetScreenDisplayButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        resetScreenDisplayButton.setTooltipText("退出全屏显示");
        resetScreenDisplayButton.addClickListener((event -> {
            queryResultInsightWidget.setHeight(400,Unit.PIXELS);
            Fullscreen.exit();
            resetScreenDisplayButton.setVisible(false);
            fullScreenDisplayButton.setVisible(true);
        }));
        resetScreenDisplayButton.setVisible(false);
        */

        Icon closeIcon = new Icon(VaadinIcon.CLOSE_BIG);
        closeIcon.setSize("14px");
        Button closeButton = new Button(closeIcon, event -> {
            informationExplorationResultDetails.setOpened(false);
            //removeSelf();
        });
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_ERROR);
        closeButton.setTooltipText("关闭探索");

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(1,Unit.PIXELS);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(1,Unit.PIXELS);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        //horizontalLayout.add(spaceDivLayout1,operationIcon,timeSpan,explorationQuestionSpan,fullScreenDisplayButton,resetScreenDisplayButton,closeButton,spaceDivLayout2);
        horizontalLayout.add(spaceDivLayout1,operationIcon,operationLabel,timeSpan,explorationQuestionSpan,closeButton,spaceDivLayout2);
        this.setFlexGrow(1,explorationQuestionSpan);

        informationExplorationResultDetails = new Details(horizontalLayout);
        informationExplorationResultDetails.addThemeVariants(DetailsVariant.REVERSE);
        informationExplorationResultDetails.setWidthFull();
        informationExplorationResultDetails.setOpened(true);
        add(informationExplorationResultDetails);
        informationExplorationResultDetails.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        informationExplorationResultDetails.getStyle().set("border-top", "1px solid var(--lumo-contrast-20pct)");
        informationExplorationResultDetails.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");
        informationExplorationResultDetails.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
    }
}
