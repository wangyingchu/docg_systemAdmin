package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionKindCorrelationInfo;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InformationInsightWidget extends VerticalLayout {

    private Details informationExplorationResultDetails;
    private String inputInitMessage;
    private Span questionSpan;
    private Button showInsightConfigButton;
    private int widgetContentHeight;
    private int insightContentHeight = 100;
    private TextArea questionTextArea;
    private VerticalLayout insightContentDisplayContainerLayout;
    private List<String> insightScopeConceptionKindList;
    private List<String> insightScopeRelationKindList;
    private List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList;

    private List<String> insightInputeMessageList;
    private Scroller scroller;
    private int browserWidth;

    public InformationInsightWidget(String inputInitMessage,
                                    List<String> insightScopeConceptionKindList,
                                    List<String> insightScopeRelationKindList,
                                    List<ConceptionKindCorrelationInfo> insightScopeConceptionKindCorrelationList,
                                    int widgetContentHeight,int browserWidth){
        this.setWidthFull();
        this.widgetContentHeight = widgetContentHeight;
        this.browserWidth = browserWidth;
        this.insightContentHeight = widgetContentHeight -30;
        this.inputInitMessage = inputInitMessage;
        this.insightScopeConceptionKindList = insightScopeConceptionKindList;
        this.insightScopeRelationKindList = insightScopeRelationKindList;
        this.insightScopeConceptionKindCorrelationList = insightScopeConceptionKindCorrelationList;

        Icon operationIcon = LineAwesomeIconsSvg.BINOCULARS_SOLID.create();
        operationIcon.setSize("16px");
        operationIcon.getStyle().set("padding-right","1px");

        NativeLabel operationLabel = new NativeLabel("洞察");
        Icon editIcon = LineAwesomeIconsSvg.BUROMOBELEXPERTE.create();
        editIcon.setSize("14px");
        this.showInsightConfigButton = new Button(editIcon);
        this.showInsightConfigButton.setTooltipText("显示洞察范围");
        this.showInsightConfigButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        this.showInsightConfigButton.addClickListener((ClickEvent<Button> click) ->{
            //doEdit();
        });

        String questionDisplayContent = this.inputInitMessage.length() > 50 ? this.inputInitMessage.substring(0,50)+"..." : this.inputInitMessage;

        this.questionSpan = new Span(questionDisplayContent);
        this.questionSpan.getStyle()
                .set("font-size","var(--lumo-font-size-m)")
                .set("font-weight","bolder")
                .set("font-style","oblique")
                .set("padding-right","5px");
        Span explorationQuestionSpan = new Span(this.questionSpan);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now =LocalDateTime.now();
        Span timeSpan = new Span("["+now.format(formatter)+ "] ");

        Icon closeIcon = new Icon(VaadinIcon.CLOSE_BIG);
        closeIcon.setSize("14px");
        Button closeButton = new Button(closeIcon, event -> {
            this.informationExplorationResultDetails.setOpened(false);
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
        horizontalLayout.add(spaceDivLayout1,operationIcon,operationLabel,timeSpan,explorationQuestionSpan, showInsightConfigButton,closeButton,spaceDivLayout2);
        this.setFlexGrow(1,explorationQuestionSpan);

        this.informationExplorationResultDetails = new Details(horizontalLayout);
        this.informationExplorationResultDetails.addThemeVariants(DetailsVariant.REVERSE);
        this.informationExplorationResultDetails.setWidthFull();
        this.informationExplorationResultDetails.setOpened(true);
        this.informationExplorationResultDetails.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        this.informationExplorationResultDetails.getStyle().set("border-top", "1px solid var(--lumo-contrast-20pct)");
        this.informationExplorationResultDetails.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");
        this.informationExplorationResultDetails.getStyle().set("border-right", "1px solid var(--lumo-contrast-20pct)");
        add(this.informationExplorationResultDetails);

        this.insightContentDisplayContainerLayout = new VerticalLayout();

        this.scroller = new Scroller(this.insightContentDisplayContainerLayout);
        this.scroller.addThemeName("overflow-indicators");
        this.informationExplorationResultDetails.add(this.scroller);

        HorizontalLayout inputElementContainerLayout = new HorizontalLayout();
        inputElementContainerLayout.setPadding(true);
        inputElementContainerLayout.setWidthFull();
        inputElementContainerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.informationExplorationResultDetails.add(inputElementContainerLayout);

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

        this.insightInputeMessageList = new ArrayList<>();
        if(!this.inputInitMessage.isEmpty()){
            this.insightInputeMessageList.add(this.inputInitMessage);
            displayInsightInputMessage(this.inputInitMessage);
        }
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
        this.scroller.setMaxHeight(this.insightContentHeight - 115, Unit.PIXELS);
        scroller.setHeight(this.insightContentHeight - 115, Unit.PIXELS);
    }

    private void executeInsightLogic(){
        String inputMessage = this.questionTextArea.getValue();
        if(inputMessage.isBlank()){
            CommonUIOperationUtil.showPopupNotification("请输入问题", NotificationVariant.LUMO_ERROR,1500, Notification.Position.MIDDLE);
        }else{
            displayInsightInputMessage(inputMessage);
        }
        this.questionTextArea.clear();
    }

    private void displayInsightInputMessage(String messageTxt){
        Span spaceHolderSpan = new Span();
        NativeLabel messageLabel = new NativeLabel(messageTxt);
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xxs)");
        messageLabel.getElement().getThemeList().add("badge contrast");
        messageLabel.setMaxWidth(browserWidth -1000,Unit.PIXELS);
        HorizontalLayout contentContainerLayout = new HorizontalLayout();
        contentContainerLayout.setWidthFull();
        contentContainerLayout.add(spaceHolderSpan,messageLabel);
        contentContainerLayout.setFlexGrow(1,spaceHolderSpan);
        this.insightContentDisplayContainerLayout.add(contentContainerLayout);
    }
}
