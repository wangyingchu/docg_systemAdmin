package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.actionExecute;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

public class ConceptionEntityActionExecuteResultsView extends VerticalLayout {

    private String conceptionKindName;
    private String conceptionEntityUID;
    private NativeLabel resultLabel;
    private NativeLabel errorMessageLabel;

    public ConceptionEntityActionExecuteResultsView(String conceptionKindName,String conceptionEntityUID){
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntityUID = conceptionEntityUID;

        this.setPadding(true);
        this.setSpacing(true);

        HorizontalLayout toolbarLayout = new HorizontalLayout();
        add(toolbarLayout);
        HorizontalLayout titleLayout = new HorizontalLayout();
        toolbarLayout.add(titleLayout);
        SecondaryIconTitle filterTitle2 = new SecondaryIconTitle(new Icon(VaadinIcon.HARDDRIVE_O),"执行结果");
        titleLayout.add(filterTitle2);

        resultLabel = new NativeLabel();
        add(resultLabel);

        errorMessageLabel = new NativeLabel();
        add(errorMessageLabel);
    }

    public void renderActionExecutionResult(Object actionResult){
        if(actionResult != null){
            resultLabel.setText(actionResult.toString());
            resultLabel.setVisible(true);
            errorMessageLabel.setVisible(false);
        }
    }

    public void renderActionExecutionErrorMessage(String errorMessage){
        if(errorMessage != null){
            errorMessageLabel.setText(errorMessage);
            resultLabel.setVisible(false);
            errorMessageLabel.setVisible(true);
        }
    }
}
