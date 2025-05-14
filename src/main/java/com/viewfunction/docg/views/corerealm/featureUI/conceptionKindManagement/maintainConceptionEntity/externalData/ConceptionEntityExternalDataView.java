package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.externalData;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributesViewKind;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityExternalAttributesAccessView;

import java.util.List;

public class ConceptionEntityExternalDataView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private ConceptionEntityExternalAttributesAccessView containerConceptionEntityExternalAttributesAccessView;
    private TabSheet externalDataAccessViewTabSheet;
    private int conceptionEntityExternalDataViewHeightOffset;

    public ConceptionEntityExternalDataView(String conceptionKind,String conceptionEntityUID,int conceptionEntityExternalDataViewHeightOffset){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityExternalDataViewHeightOffset = conceptionEntityExternalDataViewHeightOffset;
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
    }

    public ConceptionEntityExternalAttributesAccessView getContainerExternalAttributesAccessView() {
        return containerConceptionEntityExternalAttributesAccessView;
    }

    public void setContainerExternalAttributesAccessView(ConceptionEntityExternalAttributesAccessView containerConceptionEntityExternalAttributesAccessView) {
        this.containerConceptionEntityExternalAttributesAccessView = containerConceptionEntityExternalAttributesAccessView;
    }

    public void renderExternalAttributesAccessDataUI(List<AttributesViewKind> conceptionKindExternalAttributeViewList){
        externalDataAccessViewTabSheet = new TabSheet();
        externalDataAccessViewTabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_SMALL);
        externalDataAccessViewTabSheet.setWidthFull();
        add(externalDataAccessViewTabSheet);

        for(AttributesViewKind attributesViewKind:conceptionKindExternalAttributeViewList){
            ExternalValueAttributeDataAccessView currentExternalValueAttributeDataAccessView =
                    new ExternalValueAttributeDataAccessView(this.conceptionKind,this.conceptionEntityUID,attributesViewKind,this.conceptionEntityExternalDataViewHeightOffset);
            Object processorID = attributesViewKind.getMetaConfigItem("DOCG_ExternalAttributesValueAccessProcessorID");
            String externalDataType = processorID != null ? getExternalDataType(processorID.toString()):null;
            externalDataAccessViewTabSheet.
                    add(generateTabTitle(VaadinIcon.TASKS,attributesViewKind.getAttributesViewKindName(),attributesViewKind.getAttributesViewKindDesc(),externalDataType),currentExternalValueAttributeDataAccessView);
        }
    }

    private HorizontalLayout generateTabTitle(VaadinIcon tabIcon, String tabTitleTxt,String tabTooltipTxt,String externalDataType){
        HorizontalLayout  tabTitleLayout = new HorizontalLayout();
        tabTitleLayout.setDefaultVerticalComponentAlignment(Alignment.START);
        Icon tabTitleIcon = new Icon(tabIcon);
        if(externalDataType != null){
            tabTitleTxt = tabTitleTxt+" ("+externalDataType+")";
        }
        tabTitleIcon.setTooltipText(tabTooltipTxt);
        tabTitleIcon.setSize("8px");
        NativeLabel tabTitleLabel = new NativeLabel(" "+tabTitleTxt);
        tabTitleLabel.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");
        tabTitleLayout.add(tabTitleIcon,tabTitleLabel);
        return tabTitleLayout;
    }

    private String getExternalDataType(String processorID){
        if(processorID.equals("com.viewfunction.docg.coreRealm.realmServiceCore.external.dataExchange.relationDB.DefaultRelationDBExternalAttributesValueAccessProcessor")){
            return "Relation Database";
        }else if(processorID.equals("com.viewfunction.docg.coreRealm.realmServiceCore.external.dataExchange.objectStore.DefaultObjectStoreExternalAttributesValueAccessProcessor")){
            return "Object Store";
        }else{
            return null;
        }
    }
}
