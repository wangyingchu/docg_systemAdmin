package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AttributesViewKindsCorrelationInfoSummaryView extends VerticalLayout {
    private AttributesViewKindsCorrelationInfoSummaryChart attributesViewKindsCorrelationInfoSummaryChart;
    private AttributesViewKindsCorrelationInfoSummaryChartInitThread attributesViewKindsCorrelationInfoSummaryChartInitThread;
    private int windowWidth;
    private int windowHeight;
    public AttributesViewKindsCorrelationInfoSummaryView(int windowWidth, int windowHeight){
        this.setWidthFull();
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
        this.setHeight(windowHeight, Unit.PIXELS);
        this.setWidth(windowWidth, Unit.PIXELS);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        this.attributesViewKindsCorrelationInfoSummaryChartInitThread = new AttributesViewKindsCorrelationInfoSummaryChartInitThread(attachEvent.getUI(), this);
        this.attributesViewKindsCorrelationInfoSummaryChartInitThread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        this.attributesViewKindsCorrelationInfoSummaryChartInitThread.interrupt();
        this.attributesViewKindsCorrelationInfoSummaryChartInitThread = null;
    }

    private static class AttributesViewKindsCorrelationInfoSummaryChartInitThread extends Thread {
        private final UI ui;
        private AttributesViewKindsCorrelationInfoSummaryView attributesViewKindsCorrelationInfoSummaryView;

        public AttributesViewKindsCorrelationInfoSummaryChartInitThread(UI ui, AttributesViewKindsCorrelationInfoSummaryView attributesViewKindsCorrelationInfoSummaryView) {
            this.ui = ui;
            this.attributesViewKindsCorrelationInfoSummaryView = attributesViewKindsCorrelationInfoSummaryView;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(50);
                ui.access(() -> attributesViewKindsCorrelationInfoSummaryView.initAttributesViewKindsCorrelationInfoSummaryChart());
                this.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initAttributesViewKindsCorrelationInfoSummaryChart(){
        attributesViewKindsCorrelationInfoSummaryChart = new AttributesViewKindsCorrelationInfoSummaryChart(windowWidth,windowHeight);
        add(attributesViewKindsCorrelationInfoSummaryChart);
    }
}
