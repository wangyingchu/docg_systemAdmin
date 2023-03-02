package com.viewfunction.docg.views.corerealm.featureUI.relationKindManagement;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.ProcessingListView;

public class CreateRelationEntityView extends HorizontalLayout {


    public CreateRelationEntityView(){




        ProcessingListView processingListView = new ProcessingListView(500);

        add(processingListView);


    }



    private Dialog containerDialog;

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
}
