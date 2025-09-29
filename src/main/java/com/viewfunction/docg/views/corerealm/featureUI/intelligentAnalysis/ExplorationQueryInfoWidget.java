package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.ThirdLevelTitleActionBar;

import java.util.ArrayList;
import java.util.List;

public class ExplorationQueryInfoWidget extends VerticalLayout {

    private Markdown markdown;
    private Scroller insightContentScroller;

    private Button editAndReQueryButton;
    private Button cancelEditAndReQueryButton;
    private Button confirmEditAndReQueryButton;

    private TextArea explorationQueryTextArea;
    private String explorationQuery;
    private InformationExplorationWidget parentInformationExplorationWidget;

    public ExplorationQueryInfoWidget(String explorationQuery){
        this.explorationQuery = explorationQuery;
        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"探索查询语句");
        infoTitle.setWidthFull();
        infoTitle.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        add(infoTitle);

        List<Component> actionComponentsList = new ArrayList<>();
        editAndReQueryButton = new Button("编辑探查查询语句并重新探索",new Icon(VaadinIcon.EDIT));
        editAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        editAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        editAndReQueryButton.addClickListener((ClickEvent<Button> click) ->{
            doEdit();
        });

        cancelEditAndReQueryButton = new Button("取消编辑",new Icon(VaadinIcon.ARROW_BACKWARD));
        cancelEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        cancelEditAndReQueryButton.addClickListener((ClickEvent<Button> click) ->{
            cancelEditAndQuery();
        });
        cancelEditAndReQueryButton.setVisible(false);

        confirmEditAndReQueryButton = new Button("确认编辑并重新探索",new Icon(VaadinIcon.CHECK));
        confirmEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        confirmEditAndReQueryButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        confirmEditAndReQueryButton.addClickListener((ClickEvent<Button> click) ->{
            confirmEditAndQuery();
        });
        confirmEditAndReQueryButton.setVisible(false);

        actionComponentsList.add(editAndReQueryButton);
        actionComponentsList.add(cancelEditAndReQueryButton);
        actionComponentsList.add(confirmEditAndReQueryButton);

        ThirdLevelTitleActionBar sectionActionBar = new ThirdLevelTitleActionBar(new Icon(VaadinIcon.TOOLS),"操作",null,actionComponentsList);
        add(sectionActionBar);

        insightContentScroller = new Scroller();
        insightContentScroller.setWidthFull();
        insightContentScroller.setHeight(220, Unit.PIXELS);
        add(insightContentScroller);

        markdown = new Markdown(explorationQuery);
        markdown.getStyle().set("background-color", "var(--lumo-contrast-5pct)").set("padding", "var(--lumo-space-s) var(--lumo-space-s) var(--lumo-space-s) var(--lumo-space-s)");

        insightContentScroller.setContent(markdown);

        explorationQueryTextArea = new TextArea();
        explorationQueryTextArea.setWidthFull();
        explorationQueryTextArea.setHeight(220,Unit.PIXELS);
        explorationQueryTextArea.setValue(this.explorationQuery);
        add(explorationQueryTextArea);
        explorationQueryTextArea.setVisible(false);
    }

    public void setExplorationQuery(String explorationQuery){
        this.markdown.setContent(explorationQuery);
        this.explorationQuery = explorationQuery;
        this.explorationQueryTextArea.setValue(this.explorationQuery);
    }

    private void doEdit(){
        editAndReQueryButton.setVisible(false);
        confirmEditAndReQueryButton.setVisible(true);
        cancelEditAndReQueryButton.setVisible(true);

        explorationQueryTextArea.setVisible(true);
        insightContentScroller.setVisible(false);
    }

    private void confirmEditAndQuery(){
        editAndReQueryButton.setVisible(true);
        confirmEditAndReQueryButton.setVisible(false);
        cancelEditAndReQueryButton.setVisible(false);

        explorationQueryTextArea.setVisible(false);
        insightContentScroller.setVisible(true);

        if(this.parentInformationExplorationWidget != null){
            this.explorationQuery = explorationQueryTextArea.getValue();
            this.parentInformationExplorationWidget.doExplorationQuery(explorationQueryTextArea.getValue());
        }
    }

    private void cancelEditAndQuery(){
        editAndReQueryButton.setVisible(true);
        confirmEditAndReQueryButton.setVisible(false);
        cancelEditAndReQueryButton.setVisible(false);

        explorationQueryTextArea.setVisible(false);
        insightContentScroller.setVisible(true);
        explorationQueryTextArea.setValue(this.explorationQuery);
    }

    public void setParentInformationExplorationWidget(InformationExplorationWidget parentInformationExplorationWidget) {
        this.parentInformationExplorationWidget = parentInformationExplorationWidget;
    }
}
