package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.actionExecute;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionAction;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.ConceptionEntityActionsExecutionView;

import java.util.List;

public class ConceptionEntityActionsDoExecuteView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private ConceptionEntityActionsExecutionView containerConceptionEntityActionsExecutionView;
    private TabSheet conceptionActionExecuteViewTabSheet;
    private int conceptionEntityActionsExecutionViewHeightOffset;

    public ConceptionEntityActionsDoExecuteView(String conceptionKind,String conceptionEntityUID,int conceptionEntityActionsExecutionViewHeightOffset){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityActionsExecutionViewHeightOffset = conceptionEntityActionsExecutionViewHeightOffset;
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
            ActionExecutionControlView actionExecutionControlView = new ActionExecutionControlView(this.conceptionKind,this.conceptionEntityUID,conceptionAction,this.conceptionEntityActionsExecutionViewHeightOffset);
            conceptionActionExecuteViewTabSheet.
                    add(generateTabTitle(LineAwesomeIconsSvg.FIRE_SOLID.create(),conceptionAction.getActionName(), conceptionAction.getActionDesc()), actionExecutionControlView);
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
