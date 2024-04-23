package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement.maintainDataSlice.queryDataSlice;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;

public class DataSliceQueryUI extends VerticalLayout {

    private Registration listener;
    private DataSliceMetaInfo dataSliceMetaInfo;
    private VerticalLayout queryFieldsContainer;
    private VerticalLayout queryResultContainer;

    public DataSliceQueryUI(DataSliceMetaInfo dataSliceMetaInfo) {
        this.dataSliceMetaInfo = dataSliceMetaInfo;

        this.queryFieldsContainer = new VerticalLayout();
        this.queryFieldsContainer.setPadding(false);
        this.queryFieldsContainer.setSpacing(false);
        this.queryFieldsContainer.setMargin(false);
        DataSliceQueryCriteriaView dataSliceQueryCriteriaView = new DataSliceQueryCriteriaView(this.dataSliceMetaInfo);
        this.queryFieldsContainer.add(dataSliceQueryCriteriaView);

        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        if(webBrowser.isChrome()){
            this.queryFieldsContainer.setMinWidth(350, Unit.PIXELS);
            this.queryFieldsContainer.setMaxWidth(350,Unit.PIXELS);
        }else{
            this.queryFieldsContainer.setMinWidth(350,Unit.PIXELS);
            this.queryFieldsContainer.setMaxWidth(350,Unit.PIXELS);
        }

        this.queryResultContainer= new VerticalLayout();
        this.queryResultContainer.setPadding(false);
        this.queryResultContainer.setSpacing(false);
        this.queryResultContainer.setMargin(false);
        DataSliceQueryResultsView dataSliceQueryResultsView = new DataSliceQueryResultsView(this.dataSliceMetaInfo);
        this.queryResultContainer.add(dataSliceQueryResultsView);

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
