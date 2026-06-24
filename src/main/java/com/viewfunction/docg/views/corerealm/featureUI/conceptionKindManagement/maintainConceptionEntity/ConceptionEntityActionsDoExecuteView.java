package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionAction;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.util.List;

public class ConceptionEntityActionsDoExecuteView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private ConceptionEntityActionsExecutionView containerConceptionEntityActionsExecutionView;
    private TabSheet conceptionActionExecuteViewTabSheet;
    private int conceptionEntityExternalDataViewHeightOffset;

    public ConceptionEntityActionsDoExecuteView(String conceptionKind,String conceptionEntityUID,int conceptionEntityExternalDataViewHeightOffset){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityExternalDataViewHeightOffset = conceptionEntityExternalDataViewHeightOffset;
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
    }

    public ConceptionEntityActionsExecutionView getContainerExternalAttributesAccessView() {
        return containerConceptionEntityActionsExecutionView;
    }

    public void setContainerExternalAttributesAccessView(ConceptionEntityActionsExecutionView containerConceptionEntityExternalAttributesAccessView) {
        this.containerConceptionEntityActionsExecutionView = containerConceptionEntityExternalAttributesAccessView;
    }

    public void renderConceptionEntityActionsUI(List<ConceptionAction> conceptionActionsList) {
        conceptionActionExecuteViewTabSheet = new TabSheet();
        conceptionActionExecuteViewTabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_SMALL);
        conceptionActionExecuteViewTabSheet.setWidthFull();
        add(conceptionActionExecuteViewTabSheet);

        for (ConceptionAction conceptionAction : conceptionActionsList) {
            //Object processorID = attributesViewKind.getMetaConfigItem(RealmConstant.ExternalAttributesValueAccessProcessorID);
            //String externalProcessorIDStr = processorID != null ? processorID.toString() : null;
            //String externalDataType = processorID != null ? getExternalDataType(processorID.toString()):null;
            //ExternalValueAttributeDataAccessView currentExternalValueAttributeDataAccessView =
            //      new ExternalValueAttributeDataAccessView(this.conceptionKind,this.conceptionEntityUID,attributesViewKind.getAttributesViewKindUID(),externalProcessorIDStr,this.conceptionEntityExternalDataViewHeightOffset);
            NativeLabel nativeLabel = new NativeLabel(conceptionAction.getActionName());
            conceptionActionExecuteViewTabSheet.
                    add(generateTabTitle(LineAwesomeIconsSvg.FIRE_SOLID.create(),conceptionAction.getActionName(), conceptionAction.getActionDesc()), nativeLabel);
        }
    }

    private HorizontalLayout generateTabTitle(Icon tabIcon, String tabTitleTxt,String tabTooltipTxt){
        HorizontalLayout  tabTitleLayout = new HorizontalLayout();
        tabTitleLayout.setDefaultVerticalComponentAlignment(Alignment.START);
        tabIcon.setTooltipText(tabTooltipTxt);
        tabIcon.setSize("12px");
        NativeLabel tabTitleLabel = new NativeLabel(" "+tabTitleTxt);
        tabTitleLabel.getStyle()
                .set("font-size","var(--lumo-font-size-s)")
                .set("font-weight", "bold");
        tabTitleLayout.add(tabIcon,tabTitleLabel);
        return tabTitleLayout;
    }
}
