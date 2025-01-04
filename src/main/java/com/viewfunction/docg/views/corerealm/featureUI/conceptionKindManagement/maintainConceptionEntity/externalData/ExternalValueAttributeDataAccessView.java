package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.externalData;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.ConceptionKindQueryResultsView;

public class ExternalValueAttributeDataAccessView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private AttributesViewKind attributesViewKind;
    private VerticalLayout queryFieldsContainer;
    private VerticalLayout queryResultContainer;
    private Registration listener;
    private int conceptionEntityExternalDataViewHeightOffset;

    public ExternalValueAttributeDataAccessView(String conceptionKind,String conceptionEntityUID,AttributesViewKind attributesViewKind,int conceptionEntityExternalDataViewHeightOffset){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.attributesViewKind = attributesViewKind;
        this.conceptionEntityExternalDataViewHeightOffset = conceptionEntityExternalDataViewHeightOffset;

        setPadding(false);
        setSpacing(false);
        setMargin(false);

        queryFieldsContainer = new VerticalLayout();
        queryFieldsContainer.setPadding(false);
        queryFieldsContainer.setSpacing(false);
        queryFieldsContainer.setMargin(false);
        ConceptionEntityExternalDataQueryCriteriaView conceptionEntityExternalDataQueryCriteriaView = new ConceptionEntityExternalDataQueryCriteriaView(this.conceptionKind,this.attributesViewKind,this.conceptionEntityExternalDataViewHeightOffset);
        queryFieldsContainer.add(conceptionEntityExternalDataQueryCriteriaView);

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

        //ConceptionKindQueryResultsView conceptionKindQueryResultsView = new ConceptionKindQueryResultsView(this.conceptionKind);
       // queryResultContainer.add(conceptionKindQueryResultsView);

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
            queryFieldsContainer.setHeight(event.getHeight() - conceptionEntityExternalDataViewHeightOffset + 60,Unit.PIXELS);
           // queryResultContainer.setHeight(event.getHeight()-110,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryFieldsContainer.setHeight(browserHeight - conceptionEntityExternalDataViewHeightOffset + 60,Unit.PIXELS);
          //  queryResultContainer.setHeight(browserHeight-110,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }
}
