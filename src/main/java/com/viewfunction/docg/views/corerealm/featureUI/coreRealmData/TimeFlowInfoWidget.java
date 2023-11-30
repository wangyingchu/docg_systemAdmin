package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeFlowRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.TimeFlow;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;

import java.text.NumberFormat;
import java.util.List;

public class TimeFlowInfoWidget extends VerticalLayout {

    private boolean contentAlreadyLoaded = false;
    private NumberFormat numberFormat;
    private Accordion accordion;

    public TimeFlowInfoWidget(){
        this.setSpacing(false);
        this.setMargin(false);
        this.addClassNames("bg-base");
        this.setWidth(100, Unit.PERCENTAGE);
        this.numberFormat = NumberFormat.getInstance();

        this.accordion = new Accordion();
        this.accordion.setWidth(100, Unit.PERCENTAGE);
        add(this.accordion);
    }

    public void loadWidgetContent(){
        if(!this.contentAlreadyLoaded){
            this.contentAlreadyLoaded = true;
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            coreRealm.openGlobalSession();
            List<TimeFlow> existingTimeFlowList = coreRealm.getTimeFlows();
            if(existingTimeFlowList != null){
                for(TimeFlow currentTimeFlow : existingTimeFlowList){
                    String timeFlowName = currentTimeFlow.getTimeFlowName();

                    VerticalLayout timeFlowInformationLayout = new VerticalLayout();
                    timeFlowInformationLayout.setPadding(false);

                    HorizontalLayout timeHorizontalLayout = new HorizontalLayout();
                    timeHorizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

                    Span fromYear = new Span("");
                    fromYear.getElement().getThemeList().add("badge pill");
                    fromYear.addClassNames("text-xl","font-bold");
                    fromYear.getStyle().set("color","#2e4e7e");
                    Span toYear = new Span("");
                    toYear.getElement().getThemeList().add("badge pill");
                    toYear.addClassNames("text-xl","font-bold");
                    toYear.getStyle().set("color","#2e4e7e");
                    Span yearDiv = new Span(" - ");
                    timeHorizontalLayout.add(fromYear);
                    timeHorizontalLayout.add(yearDiv);
                    timeHorizontalLayout.add(toYear);
                    timeFlowInformationLayout.add(timeHorizontalLayout);

                    List<Integer> availableTimeSpanYearsList = currentTimeFlow.getAvailableTimeSpanYears();
                    if(availableTimeSpanYearsList != null && availableTimeSpanYearsList.size() >0){
                        if(availableTimeSpanYearsList.size() ==1){
                            Integer onlyYear = availableTimeSpanYearsList.get(0);
                            fromYear.setText(""+onlyYear);
                            toYear.setText(""+onlyYear);
                        }else{
                            Integer firstYear = availableTimeSpanYearsList.get(0);
                            Integer lastYear = availableTimeSpanYearsList.get(availableTimeSpanYearsList.size() -1);
                            fromYear.setText(""+firstYear);
                            toYear.setText(""+lastYear);
                        }
                    }

                    TimeFlowRuntimeStatistics timeFlowRuntimeStatistics = currentTimeFlow.getTimeFlowRuntimeStatistics();

                    HorizontalLayout horizontalLayout = new HorizontalLayout();
                    horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    new PrimaryKeyValueDisplayItem(horizontalLayout, FontAwesome.Solid.CLOCK.create(),"TimeScaleEntity 数量:",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getContainsTotalTimeScaleEntityCount()));
                    timeFlowInformationLayout.add(horizontalLayout);

                    HorizontalLayout horizontalLayout2 = new HorizontalLayout();
                    horizontalLayout2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    new PrimaryKeyValueDisplayItem(horizontalLayout2,FontAwesome.Solid.BEZIER_CURVE.create(),"TimeScaleEvent 数量:",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getRefersTotalTimeScaleEventCount()));
                    timeFlowInformationLayout.add(horizontalLayout2);

                    HorizontalLayout horizontalLayout3 = new HorizontalLayout();
                    horizontalLayout3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label3 = new NativeLabel("Year Entities:");
                    label3.getElement().getThemeList().add("badge success small");
                    horizontalLayout3.add(label3);
                    new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.CLOCK.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getContainsYearScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout3,FontAwesome.Solid.BEZIER_CURVE.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getRefersYearScaleTimeScaleEventCount()));
                    timeFlowInformationLayout.add(horizontalLayout3);

                    HorizontalLayout horizontalLayout4 = new HorizontalLayout();
                    horizontalLayout4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label4 = new NativeLabel("Month Entities:");
                    label4.getElement().getThemeList().add("badge success small");
                    horizontalLayout4.add(label4);
                    new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.CLOCK.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getContainsMonthScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout4,FontAwesome.Solid.BEZIER_CURVE.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getRefersMonthScaleTimeScaleEventCount()));
                    timeFlowInformationLayout.add(horizontalLayout4);

                    HorizontalLayout horizontalLayout5 = new HorizontalLayout();
                    horizontalLayout5.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label5 = new NativeLabel("Day Entities:");
                    label5.getElement().getThemeList().add("badge success small");
                    horizontalLayout5.add(label5);
                    new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.CLOCK.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getContainsDayScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout5,FontAwesome.Solid.BEZIER_CURVE.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getRefersDayScaleTimeScaleEventCount()));
                    timeFlowInformationLayout.add(horizontalLayout5);

                    HorizontalLayout horizontalLayout6 = new HorizontalLayout();
                    horizontalLayout6.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label6 = new NativeLabel("Hour Entities:");
                    label6.getElement().getThemeList().add("badge success small");
                    horizontalLayout6.add(label6);
                    new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.CLOCK.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getContainsHourScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout6,FontAwesome.Solid.BEZIER_CURVE.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getRefersHourScaleTimeScaleEventCount()));
                    timeFlowInformationLayout.add(horizontalLayout6);

                    HorizontalLayout horizontalLayout7 = new HorizontalLayout();
                    horizontalLayout7.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    NativeLabel label7 = new NativeLabel("Minute Entities:");
                    label7.getElement().getThemeList().add("badge success small");
                    horizontalLayout7.add(label7);
                    new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.CLOCK.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getContainsMinuteScaleTimeScaleEntityCount()));
                    new SecondaryKeyValueDisplayItem(horizontalLayout7,FontAwesome.Solid.BEZIER_CURVE.create(),"",
                            this.numberFormat.format(timeFlowRuntimeStatistics.getRefersMinuteScaleTimeScaleEventCount()));
                    timeFlowInformationLayout.add(horizontalLayout7);

                    AccordionPanel timeFlowInfoPanel1 = accordion.add(timeFlowName, timeFlowInformationLayout);
                    timeFlowInfoPanel1.addThemeVariants(DetailsVariant.SMALL,DetailsVariant.REVERSE);
                }
            }
            coreRealm.closeGlobalSession();
        }
    }
}
