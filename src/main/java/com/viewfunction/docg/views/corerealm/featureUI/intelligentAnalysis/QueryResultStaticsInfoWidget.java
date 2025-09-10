package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class QueryResultStaticsInfoWidget extends VerticalLayout {

    private DateTimePicker startDate;
    private DateTimePicker finishDate;
    private TextField dataCount;

    public QueryResultStaticsInfoWidget(Date startTime,Date finishTime,long countNumber){
        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(LineAwesomeIconsSvg.LIST_SOLID.create(),"探索查询统计信息");
        infoTitle.setWidthFull();
        infoTitle.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        add(infoTitle);

        startDate = new DateTimePicker("查询开始时间");
        if(startTime != null){
            startDate.setValue(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()));
        }
        startDate.setReadOnly(true);

        finishDate = new DateTimePicker("查询结束时间");
        if(finishDate != null){
            finishDate.setValue(LocalDateTime.ofInstant(finishTime.toInstant(), ZoneId.systemDefault()));
        }
        finishDate.setReadOnly(true);

        dataCount = new TextField("查询结果数量");
        dataCount.setReadOnly(true);
        dataCount.setValue(""+countNumber);

        FormLayout formLayout = new FormLayout();
        formLayout.add(startDate, finishDate, dataCount);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2)
        );
        add(formLayout);
    }
}
