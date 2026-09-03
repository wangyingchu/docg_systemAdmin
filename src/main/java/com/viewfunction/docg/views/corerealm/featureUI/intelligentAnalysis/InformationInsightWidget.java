package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.AttachEvent;
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
import com.vaadin.flow.component.textfield.TextArea;

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
    //private TextField questionEditField;
    private DynamicContentQueryResult dynamicContentQueryResult;
    private QueryResultInsightWidget queryResultInsightWidget;
    private QueryResultGraphWidget queryResultGraphWidget;
    private HorizontalLayout doesNotContainsDataInfoMessage;
    private Button fullScreenDisplayButton;
    private Button resetScreenDisplayButton;
    private int widgetContentHeight;
    private int insightContentHeight = 100;

    private TextArea questionTextArea;
    private VerticalLayout insightContentDisplayContainerLayout;

    private List<String> insightScopeConceptionKindList;
    private List<String> insightScopeRelationKindList;
    private List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList;

    public InformationInsightWidget(String question,
                                    List<String> insightScopeConceptionKindList,
                                    List<String> insightScopeRelationKindList,
                                    List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList,
                                    int widgetContentHeight){
        this.setWidthFull();
        this.widgetContentHeight = widgetContentHeight;
        this.insightContentHeight = widgetContentHeight -30;
        this.question = question;
        this.insightScopeConceptionKindList = insightScopeConceptionKindList;
        this.insightScopeRelationKindList = insightScopeRelationKindList;
        this.insightScopeConceptionKindCorrelationList = insightScopeConceptionKindCorrelationList;

        Icon operationIcon = LineAwesomeIconsSvg.BINOCULARS_SOLID.create();
        operationIcon.setSize("16px");
        operationIcon.getStyle().set("padding-right","1px");

        NativeLabel operationLabel = new NativeLabel("洞察");
        Icon editIcon = LineAwesomeIconsSvg.BUROMOBELEXPERTE.create();
        editIcon.setSize("14px");
        editAndReQueryButton = new Button(editIcon);
        editAndReQueryButton.setTooltipText("编辑探查问题并重新探索");
        editAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        editAndReQueryButton.addClickListener((ClickEvent<Button> click) ->{
            //doEdit();
        });

        //question = "这是一段很长的标签文本，它会在超出宽度时被截断并显示省略号。这是一段很长的标签文本，它会在超出宽度时被截断并显示省略号。这是一段很长的标签文本，它会在超出宽度时被截断并显示省略号。这是一段很长的标签文本，它会在超出宽度时被截断并显示省略号。这是一段很长的标签文本，它会在超出宽度时被截断并显示省略号。";
        String questionDisplayContent = question.length() > 50 ? question.substring(0,50)+"..." : question;

        questionSpan = new Span(questionDisplayContent);
        questionSpan.getStyle()
                .set("font-size","var(--lumo-font-size-m)")
                .set("font-weight","bolder")
                .set("font-style","oblique")
                .set("padding-right","5px");
        Span explorationQuestionSpan = new Span(questionSpan);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now =LocalDateTime.now();
        Span timeSpan = new Span("["+now.format(formatter)+ "] ");

        Icon closeIcon = new Icon(VaadinIcon.CLOSE_BIG);
        closeIcon.setSize("14px");
        Button closeButton = new Button(closeIcon, event -> {
            informationExplorationResultDetails.setOpened(false);
            removeSelf();
        });
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_ERROR);
        closeButton.setTooltipText("关闭解读");

        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(1,Unit.PIXELS);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(1,Unit.PIXELS);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.add(spaceDivLayout1,operationIcon,operationLabel,timeSpan,explorationQuestionSpan,editAndReQueryButton,closeButton,spaceDivLayout2);
        this.setFlexGrow(1,explorationQuestionSpan);

        informationExplorationResultDetails = new Details(horizontalLayout);
        informationExplorationResultDetails.addThemeVariants(DetailsVariant.REVERSE);
        informationExplorationResultDetails.setWidthFull();
        informationExplorationResultDetails.setOpened(true);
        informationExplorationResultDetails.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        informationExplorationResultDetails.getStyle().set("border-top", "1px solid var(--lumo-contrast-20pct)");
        informationExplorationResultDetails.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");
        informationExplorationResultDetails.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        add(informationExplorationResultDetails);

        insightContentDisplayContainerLayout = new VerticalLayout();
        informationExplorationResultDetails.add(insightContentDisplayContainerLayout);

        HorizontalLayout inputElementContainerLayout = new HorizontalLayout();
        inputElementContainerLayout.setPadding(true);
        inputElementContainerLayout.setWidthFull();
        inputElementContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        informationExplorationResultDetails.add(inputElementContainerLayout);

        this.questionTextArea = new TextArea();
        this.questionTextArea.setWidthFull();
        this.questionTextArea.setHeight(50, Unit.PIXELS);
        this.questionTextArea.addValueChangeListener(e -> {
            //e.getSource().setHelperText(e.getValue().length() + "/" + charLimit);
        });

        inputElementContainerLayout.add(this.questionTextArea);
        inputElementContainerLayout.setFlexGrow(1, this.questionTextArea);
        HorizontalLayout buttonsControllerLayout = new HorizontalLayout();
        inputElementContainerLayout.add(buttonsControllerLayout);

        Button askButton = new Button(" 输入");
        askButton.setIcon(LineAwesomeIconsSvg.PAPER_PLANE.create());
        askButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SMALL);
        askButton.setWidth(30,Unit.PIXELS);
        askButton.setHeight(50,Unit.PIXELS);
        askButton.addClickListener(e -> {
            executeInsightLogic();
        });
        buttonsControllerLayout.add(askButton);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderInsightResult();
    }

    private void removeSelf(){
        this.removeFromParent();
    }

    private void renderInsightResult(){
        insightContentDisplayContainerLayout.setHeight(this.insightContentHeight - 115,Unit.PIXELS);
    }

    private void executeInsightLogic(){
        this.questionTextArea.clear();
    }
}
