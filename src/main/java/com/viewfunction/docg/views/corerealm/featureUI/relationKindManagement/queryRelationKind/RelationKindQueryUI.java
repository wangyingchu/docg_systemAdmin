package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement.queryRelationKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.shared.Registration;

public class RelationKindQueryUI extends VerticalLayout {
    private String relationKindName;
    private Registration listener;
    private VerticalLayout queryFieldsContainer;
    private VerticalLayout queryResultContainer;

    public RelationKindQueryUI(String relationKindName){
        this.relationKindName = relationKindName;

        queryFieldsContainer = new VerticalLayout();
        queryFieldsContainer.setPadding(false);
        queryFieldsContainer.setSpacing(false);
        queryFieldsContainer.setMargin(false);
        RelationKindQueryCriteriaView relationKindQueryCriteriaView = new RelationKindQueryCriteriaView(this.relationKindName);
        queryFieldsContainer.add(relationKindQueryCriteriaView);

        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        if(webBrowser.isChrome()){
            queryFieldsContainer.setMinWidth(360, Unit.PIXELS);
            queryFieldsContainer.setMaxWidth(360,Unit.PIXELS);
        }else{
            queryFieldsContainer.setMinWidth(350,Unit.PIXELS);
            queryFieldsContainer.setMaxWidth(350,Unit.PIXELS);
        }

        queryResultContainer= new VerticalLayout();
        queryResultContainer.setPadding(false);
        queryResultContainer.setSpacing(false);
        queryResultContainer.setMargin(false);
        RelationKindQueryResultsView relationKindQueryResultsView = new RelationKindQueryResultsView(this.relationKindName);
        queryResultContainer.add(relationKindQueryResultsView);

        SplitLayout splitLayout = new SplitLayout(queryFieldsContainer, queryResultContainer);
        splitLayout.setSplitterPosition(0);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            queryFieldsContainer.setHeight(event.getHeight()-80,Unit.PIXELS);
            queryResultContainer.setHeight(event.getHeight()-80,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryFieldsContainer.setHeight(browserHeight-80,Unit.PIXELS);
            queryResultContainer.setHeight(browserHeight-80,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
}
