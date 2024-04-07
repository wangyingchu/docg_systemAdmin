package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.dataCompute.computeServiceCore.exception.ComputeGridException;
import com.viewfunction.docg.dataCompute.computeServiceCore.payload.DataSliceMetaInfo;
import com.viewfunction.docg.dataCompute.computeServiceCore.term.ComputeGrid;
import com.viewfunction.docg.dataCompute.computeServiceCore.util.factory.ComputeGridTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SyncConceptionEntitiesToNewDataSliceView extends VerticalLayout {
    private String conceptionKindName;
    private Dialog containerDialog;
    private HorizontalLayout doesNotDetectDataGridInfoMessage;
    private VerticalLayout contentContainer;

    public SyncConceptionEntitiesToNewDataSliceView(String conceptionKindName) {
        this.setWidthFull();
        this.conceptionKindName = conceptionKindName;

        doesNotDetectDataGridInfoMessage = new HorizontalLayout();
        doesNotDetectDataGridInfoMessage.setSpacing(true);
        doesNotDetectDataGridInfoMessage.setPadding(true);
        doesNotDetectDataGridInfoMessage.setMargin(true);
        doesNotDetectDataGridInfoMessage.setWidth(100, Unit.PERCENTAGE);
        Icon messageLogo = new Icon(VaadinIcon.EXCLAMATION_CIRCLE);
        messageLogo.getStyle()
                .set("color", "#ce0000").set("padding-right", "5px");
        messageLogo.setSize("30px");
        NativeLabel messageLabel = new NativeLabel(" 未检测到运行中的数据计算网格");
        messageLabel.getStyle().set("font-size", "var(--lumo-font-size-xl)").set("color", "#ce0000");
        doesNotDetectDataGridInfoMessage.add(messageLogo, messageLabel);
        add(doesNotDetectDataGridInfoMessage);
        doesNotDetectDataGridInfoMessage.setVisible(false);

        contentContainer = new VerticalLayout();
        contentContainer.setWidthFull();
        contentContainer.setSpacing(false);
        contentContainer.setPadding(false);
        contentContainer.setMargin(false);
        add(contentContainer);

        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right", "3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.conceptionKindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        contentContainer.add(entityInfoFootprintMessageBar);
        contentContainer.setVisible(true);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        checkComputeGridStatusInfo();
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void checkComputeGridStatusInfo(){
        ComputeGrid targetComputeGrid = ComputeGridTermFactory.getComputeGrid();
        try {
            //Set<DataComputeUnitMetaInfo> dataComputeUnitMetaInfoSet =
            targetComputeGrid.listDataComputeUnit();
            Set<DataSliceMetaInfo> dataSliceMetaInfoSet = targetComputeGrid.listDataSlice();
            doesNotDetectDataGridInfoMessage.setVisible(false);
            contentContainer.setVisible(true);
        } catch (ComputeGridException e) {
            doesNotDetectDataGridInfoMessage.setVisible(true);
            contentContainer.setVisible(false);
        }
    }

}
