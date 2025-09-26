package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.maintainEntitiesPath;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.structure.EntitiesPath;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis.EntitiesPathInfoView;

import java.util.*;

public class EntitiesPathDetailUI extends VerticalLayout {

    private Dialog containerDialog;
    private EntitiesPath entitiesPath;
    private VerticalLayout entitiesFieldsContainer;
    private VerticalLayout entitiesPathInfoContainer;
    private Registration listener;
    private EntitiesPathElementsInfoView entitiesPathElementsInfoView;

    public EntitiesPathDetailUI(EntitiesPath entitiesPath){
        this.entitiesPath = entitiesPath;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            renderView(event.getHeight()-75);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            renderView(browserHeight-75);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void renderView(int viewHeight){
        this.entitiesFieldsContainer = new VerticalLayout();
        this.entitiesFieldsContainer.setPadding(false);
        this.entitiesFieldsContainer.setSpacing(false);
        this.entitiesFieldsContainer.setMargin(false);
        this.entitiesFieldsContainer.setMinWidth(450, Unit.PIXELS);
        this.entitiesFieldsContainer.setMaxWidth(450, Unit.PIXELS);
        EntitiesPathInfoView entitiesPathInfoView = new EntitiesPathInfoView(this.entitiesPath,viewHeight);
        this.entitiesFieldsContainer.add(entitiesPathInfoView);

        this.entitiesPathInfoContainer = new VerticalLayout();
        HorizontalLayout titleLayout = new HorizontalLayout();
        this.entitiesPathInfoContainer.add(titleLayout);

        Icon pathIcon = LineAwesomeIconsSvg.PROJECT_DIAGRAM_SOLID.create();
        pathIcon.setSize("16px");
        pathIcon.getStyle().set("padding-right","3px");
        titleLayout.add(pathIcon);
        new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.CUBE.create(),"概念实体数量",""+this.entitiesPath.getPathConceptionEntities().size());
        new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.CONNECT_O.create(),"关系实体数量",""+this.entitiesPath.getPathRelationEntities().size());

        Icon reRunIcon = new Icon(VaadinIcon.REFRESH);
        reRunIcon.setSize("16px");
        Button reRunButton = new Button(reRunIcon, event -> {
            refreshPathTopologyInfo();
        });
        reRunButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ICON);
        reRunButton.setTooltipText("刷新路径拓扑图");
        titleLayout.add(reRunButton);

        titleLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");

        entitiesPathElementsInfoView = new EntitiesPathElementsInfoView(this.entitiesPath,viewHeight);
        entitiesPathElementsInfoView.setWidthFull();
        this.entitiesPathInfoContainer.add(entitiesPathElementsInfoView);

        SplitLayout splitLayout = new SplitLayout(this.entitiesFieldsContainer, this.entitiesPathInfoContainer);
        splitLayout.setSplitterPosition(0);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);
        splitLayout.getSecondaryComponent().getElement().getStyle().set("padding-top","0px").set("padding-right","0px");
    }

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void refreshPathTopologyInfo(){
        entitiesPathElementsInfoView.refreshPathTopologyInfo();
    }
}
