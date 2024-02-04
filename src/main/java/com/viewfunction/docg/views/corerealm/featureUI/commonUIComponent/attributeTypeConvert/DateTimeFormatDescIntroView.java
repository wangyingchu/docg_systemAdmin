package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.attributeTypeConvert;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;

public class DateTimeFormatDescIntroView extends VerticalLayout {

    private Grid<DateTimeFormatterInfo> dateTimeFormatterIntroInfoGrid;

    private class DateTimeFormatterInfo {
        private String dateTimeFormatter;
        private String dateTimeValueExample;

        private DateTimeFormatterInfo(String dateTimeFormatter,String dateTimeValueExample){
            this.setDateTimeFormatter(dateTimeFormatter);
            this.setDateTimeValueExample(dateTimeValueExample);
        }

        public String getDateTimeFormatter() {
            return dateTimeFormatter;
        }

        public void setDateTimeFormatter(String dateTimeFormatter) {
            this.dateTimeFormatter = dateTimeFormatter;
        }

        public String getDateTimeValueExample() {
            return dateTimeValueExample;
        }

        public void setDateTimeValueExample(String dateTimeValueExample) {
            this.dateTimeValueExample = dateTimeValueExample;
        }
    }

    public DateTimeFormatDescIntroView(){
        dateTimeFormatterIntroInfoGrid = new Grid<>();
        dateTimeFormatterIntroInfoGrid.setWidth(100, Unit.PERCENTAGE);
        dateTimeFormatterIntroInfoGrid.setSelectionMode(Grid.SelectionMode.NONE);
        dateTimeFormatterIntroInfoGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        dateTimeFormatterIntroInfoGrid.addColumn(DateTimeFormatterInfo::getDateTimeFormatter).setHeader("格式内容").setKey("idx_0");
        dateTimeFormatterIntroInfoGrid.addColumn(DateTimeFormatterInfo::getDateTimeValueExample).setHeader("数据样例").setKey("idx_1");
       
        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.BULLETS,"属性名称");
        dateTimeFormatterIntroInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.INPUT,"属性数据类型");
        dateTimeFormatterIntroInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);

        dateTimeFormatterIntroInfoGrid.setItems(
                new DateTimeFormatterInfo("yyyy-MM-dd hh:mm:ss","12小时制 示例: 2015-07-03 2:57:41"),
                new DateTimeFormatterInfo("yyyy-MM-dd hh:mm:ss a","12小时制 示例: 2015-07-03 2:57:41 AM"),

                new DateTimeFormatterInfo("yyyy-MM-dd HH:mm:ss","24小时制 示例: 2015-07-03 14:57:41"),
                new DateTimeFormatterInfo("yyyy-MM-dd HH:mm:ss SSS","24小时制 示例: 2015-07-03 14:57:41 000"),

                new DateTimeFormatterInfo("yyyy-MM-dd HH:mm:ssZ","24小时制 示例: 2014-11-11 12:00:00Z"),
                new DateTimeFormatterInfo("yyyy-MM-dd'T'HH:mm:ssZ","24小时制 示例: 2014-11-11T12:00:00Z"),
                new DateTimeFormatterInfo("yyyy-MM-dd'T'HH:mm:ssXXX","24小时制 示例: 2014-11-11T12:00:00+08:00"),
                new DateTimeFormatterInfo("yyyy-MM-dd'T'HH:mm:ss.SSSZ","24小时制 示例: 2018-05-14T03:51:50.153Z"),
                new DateTimeFormatterInfo("yyyy-MM-dd'T'HH:mm:ss.SSSXXX","24小时制 示例: 2021-06-01T12:23:00.235+08:00"),

                new DateTimeFormatterInfo("yyyy/MM/dd hh:mm:ss","12小时制 示例: 2015/07/03 2:57:41"),
                new DateTimeFormatterInfo("yyyy/MM/dd hh:mm:ss a","12小时制 示例: 2015/07/03 2:57:41 PM"),
                new DateTimeFormatterInfo("yyyy/M/d hh:mm:ss","12小时制 示例: 2015/7/3 2:57:41"),
                new DateTimeFormatterInfo("yyyy/M/d hh:mm:ss a","12小时制 示例: 2015/7/3 2:57:41 PM"),
                new DateTimeFormatterInfo("yyyy/MM/dd HH:mm:ss","24小时制 示例: 2015/07/03 14:57:41"),
                new DateTimeFormatterInfo("yyyy/MM/dd HH:mm:ss SSS","24小时制 示例: 2015/07/03 14:57:41 000"),

                new DateTimeFormatterInfo("MM/dd/yyyy hh:mm:ss","12小时制 示例: 07/03/2015 2:57:41"),
                new DateTimeFormatterInfo("MM/dd/yyyy hh:mm:ss a","12小时制 示例: 07/03/2015 2:57:41 AM"),
                new DateTimeFormatterInfo("M/d/yyyy hh:mm:ss","12小时制 示例: 7/3/2015 2:57:41"),
                new DateTimeFormatterInfo("M/d/yyyy hh:mm:ss a","12小时制 示例: 7/3/2015 2:57:41 AM"),
                new DateTimeFormatterInfo("MM/dd/yyyy HH:mm:ss","24小时制 示例: 07/03/2015 14:57:41"),
                new DateTimeFormatterInfo("MM/dd/yyyy HH:mm:ss SSS","24小时制 示例: 07/03/2015 14:57:41 000"),

                new DateTimeFormatterInfo("yyyyMMdd","示例: 20150703"),
                new DateTimeFormatterInfo("yyyy-MM-dd","示例: 2015-07-03"),
                new DateTimeFormatterInfo("yyyy-M-d","示例: 2015-7-3"),
                new DateTimeFormatterInfo("yyyy/MM/dd","示例: 2015/07/03"),
                new DateTimeFormatterInfo("yyyy/M/d","示例: 2015/7/3")
        );
        add(dateTimeFormatterIntroInfoGrid);
    }
}
