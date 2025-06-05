package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.externalData;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.shared.Registration;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FullScreenWindow;
import com.viewfunction.docg.element.commonComponent.ThirdLevelTitleActionBar;
import com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind.AttributesViewKindDetailUI;

import java.util.ArrayList;
import java.util.List;

public class ExternalValueAttributeDataAccessView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private AttributesViewKind attributesViewKind;
    private String attributesViewKindUID;
    private VerticalLayout queryFieldsContainer;
    private VerticalLayout queryResultContainer;
    private Registration listener;
    private int conceptionEntityExternalDataViewHeightOffset;
    private ConceptionEntityExternalDataQueryResultsView conceptionEntityExternalDataQueryResultsView;
    private String externalAttributesValueAccessProcessorID;

    public ExternalValueAttributeDataAccessView(String conceptionKind,String conceptionEntityUID,String attributesViewKindUID,String externalAttributesValueAccessProcessorID,int conceptionEntityExternalDataViewHeightOffset){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.attributesViewKindUID = attributesViewKindUID;
        this.conceptionEntityExternalDataViewHeightOffset = conceptionEntityExternalDataViewHeightOffset;
        this.externalAttributesValueAccessProcessorID = externalAttributesValueAccessProcessorID;

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        this.attributesViewKind = coreRealm.getAttributesViewKind(this.attributesViewKindUID);

        setPadding(false);
        setSpacing(false);
        setMargin(false);

        queryFieldsContainer = new VerticalLayout();
        queryFieldsContainer.setPadding(false);
        queryFieldsContainer.setSpacing(false);
        queryFieldsContainer.setMargin(false);

        VerticalLayout attributesViewKindInfoContainer = new VerticalLayout();
        attributesViewKindInfoContainer.setSpacing(false);
        attributesViewKindInfoContainer.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

        queryFieldsContainer.add(attributesViewKindInfoContainer);

        String attributeNameText = attributesViewKind.getAttributesViewKindName() +" ( "+attributesViewKind.getAttributesViewKindDesc()+" )";
        String attributeKindIdText = attributesViewKind.getAttributesViewKindUID()+ " - "+attributesViewKind.getAttributesViewKindDataForm();

        ThirdLevelTitleActionBar secondaryTitleActionBar = new ThirdLevelTitleActionBar(new Icon(VaadinIcon.TASKS),attributeNameText,null,null);
        secondaryTitleActionBar.setWidth(100,Unit.PERCENTAGE);
        attributesViewKindInfoContainer.add(secondaryTitleActionBar);

        Icon configIcon = new Icon(VaadinIcon.COG);
        configIcon.setSize("16px");
        Button configAttributeKind = new Button(configIcon, event -> {
            renderAttributesViewKindConfigurationUI(attributesViewKind);
        });
        configAttributeKind.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        configAttributeKind.addThemeVariants(ButtonVariant.LUMO_SMALL);
        configAttributeKind.setTooltipText("配置属性视图类型定义");
        List<Component> actionComponentList = new ArrayList<>();

        actionComponentList.add(configAttributeKind);

        ThirdLevelTitleActionBar secondaryTitleActionBar2 = new ThirdLevelTitleActionBar(new Icon(VaadinIcon.KEY_O),attributeKindIdText,null,actionComponentList);
        secondaryTitleActionBar2.setWidth(100,Unit.PERCENTAGE);
        attributesViewKindInfoContainer.add(secondaryTitleActionBar2);

        ConceptionEntityExternalDataQueryCriteriaView conceptionEntityExternalDataQueryCriteriaView = new ConceptionEntityExternalDataQueryCriteriaView(this.conceptionKind,this.attributesViewKind,this.externalAttributesValueAccessProcessorID,this.conceptionEntityExternalDataViewHeightOffset);
        queryFieldsContainer.add(conceptionEntityExternalDataQueryCriteriaView);
        conceptionEntityExternalDataQueryCriteriaView.setContainerExternalValueAttributeDataAccessView(this);

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

        conceptionEntityExternalDataQueryResultsView = new ConceptionEntityExternalDataQueryResultsView(this.conceptionKind,this.conceptionEntityUID,this.attributesViewKind,this.externalAttributesValueAccessProcessorID,this.conceptionEntityExternalDataViewHeightOffset);
        queryResultContainer.add(conceptionEntityExternalDataQueryResultsView);

        SplitLayout splitLayout = new SplitLayout(queryFieldsContainer, queryResultContainer);
        splitLayout.setSplitterPosition(0);
        splitLayout.setSizeFull();
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        add(splitLayout);
    }

    public void queryExternalValueAttributesViewData(QueryParameters queryParameters){
        this.conceptionEntityExternalDataQueryResultsView.queryExternalValueAttributesViewData(queryParameters);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            queryFieldsContainer.setHeight(event.getHeight() - conceptionEntityExternalDataViewHeightOffset + 50,Unit.PIXELS);
            queryResultContainer.setHeight(event.getHeight() - conceptionEntityExternalDataViewHeightOffset + 60,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            queryFieldsContainer.setHeight(browserHeight - conceptionEntityExternalDataViewHeightOffset + 50,Unit.PIXELS);
            queryResultContainer.setHeight(browserHeight - conceptionEntityExternalDataViewHeightOffset + 60,Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void renderAttributesViewKindConfigurationUI(AttributesViewKind attributesViewKind){
        AttributesViewKindDetailUI attributesViewKindDetailUI = new AttributesViewKindDetailUI(attributesViewKind.getAttributesViewKindUID());
        List<Component> actionComponentList = new ArrayList<>();

        HorizontalLayout titleDetailLayout = new HorizontalLayout();
        titleDetailLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        titleDetailLayout.setSpacing(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("14px");
        footPrintStartIcon.getStyle().set("color","var(--lumo-contrast-50pct)");
        titleDetailLayout.add(footPrintStartIcon);
        HorizontalLayout spaceDivLayout1 = new HorizontalLayout();
        spaceDivLayout1.setWidth(8,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout1);

        Icon attributesViewKindIcon = VaadinIcon.TASKS.create();
        attributesViewKindIcon.setSize("10px");
        titleDetailLayout.add(attributesViewKindIcon);
        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidth(5,Unit.PIXELS);
        titleDetailLayout.add(spaceDivLayout2);
        NativeLabel attributesViewKindName = new NativeLabel(attributesViewKind.getAttributesViewKindName()+" ( ");
        titleDetailLayout.add(attributesViewKindName);

        Icon _UIDIcon = VaadinIcon.KEY_O.create();
        _UIDIcon.setSize("10px");
        titleDetailLayout.add(_UIDIcon);
        NativeLabel attributesViewKindUID = new NativeLabel(" "+attributesViewKind.getAttributesViewKindUID()+")");
        titleDetailLayout.add(attributesViewKindUID);
        actionComponentList.add(titleDetailLayout);

        FullScreenWindow fullScreenWindow = new FullScreenWindow(new Icon(VaadinIcon.COG),"属性视图类型配置",actionComponentList,null,true);
        fullScreenWindow.setWindowContent(attributesViewKindDetailUI);
        fullScreenWindow.show();
    }
}
