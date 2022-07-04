package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.shared.Registration;

public class ConceptionKindQueryUI extends VerticalLayout {
    private Registration listener;
    private String conceptionKindName;
    private VerticalLayout queryFieldsContainer;
    private VerticalLayout queryResultContainer;

    public ConceptionKindQueryUI(String conceptionKindName){
        this.conceptionKindName = conceptionKindName;

        queryFieldsContainer = new VerticalLayout();
        queryFieldsContainer.setPadding(false);
        queryFieldsContainer.setSpacing(false);
        queryFieldsContainer.setMargin(false);
        ConceptionKindQueryCriteriaView conceptionKindQueryCriteriaView = new ConceptionKindQueryCriteriaView(this.conceptionKindName);
        queryFieldsContainer.add(conceptionKindQueryCriteriaView);
        queryFieldsContainer.setMinWidth(400,Unit.PIXELS);
        queryFieldsContainer.setMaxWidth(400,Unit.PIXELS);

        queryResultContainer= new VerticalLayout();
        queryResultContainer.setPadding(false);
        queryResultContainer.setSpacing(false);
        queryResultContainer.setMargin(false);
        ConceptionKindQueryResultsView conceptionKindQueryResultsView = new ConceptionKindQueryResultsView(this.conceptionKindName);
        queryResultContainer.add(conceptionKindQueryResultsView);

        SplitLayout splitLayout = new SplitLayout(queryFieldsContainer, queryResultContainer);
        //splitLayout.setSplitterPosition(15);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            queryFieldsContainer.setHeight(event.getHeight()-90,Unit.PIXELS);
            queryResultContainer.setHeight(event.getHeight()-90,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryFieldsContainer.setHeight(browserHeight-90,Unit.PIXELS);
            queryResultContainer.setHeight(browserHeight-90,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
}
