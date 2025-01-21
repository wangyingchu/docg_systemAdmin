package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class EchartsTimeSequenceBarChartPayload implements JsonSerializable {

    private Map<LocalDateTime,Long> timeAndValueMapping;

    public EchartsTimeSequenceBarChartPayload(Map<LocalDateTime,Long> timeAndValueMapping) {
        this.timeAndValueMapping = timeAndValueMapping;
    }

    public EchartsTimeSequenceBarChartPayload() {}

    @Override
    public JsonObject toJson() {
        JsonObject obj = Json.createObject();
        JsonArray valueArray = Json.createArray();
        obj.put("data", valueArray);

        if(timeAndValueMapping != null && !timeAndValueMapping.isEmpty()){
            Set<LocalDateTime> dateTimeSet = timeAndValueMapping.keySet();
            int index = 0;
            for(LocalDateTime currentDateTime : dateTimeSet){
                Long currentValue = timeAndValueMapping.get(currentDateTime);

                JsonObject currentValueObj = Json.createObject();
                currentValueObj.put("year",currentDateTime.getYear());
                currentValueObj.put("month",currentDateTime.getMonthValue());
                currentValueObj.put("day",currentDateTime.getDayOfMonth());
                currentValueObj.put("hour",currentDateTime.getHour());
                currentValueObj.put("minute",currentDateTime.getMinute());
                currentValueObj.put("second",currentDateTime.getSecond());
                currentValueObj.put("value",currentValue);
                valueArray.set(index,currentValueObj);
                index++;
            }
        }
        return obj;
    }

    @Override
    public JsonSerializable readJson(JsonObject jsonObject) {
        return null;
    }

    public Map<LocalDateTime, Long> getTimeAndValueMapping() {
        return timeAndValueMapping;
    }

    public void setTimeAndValueMapping(Map<LocalDateTime, Long> timeAndValueMapping) {
        this.timeAndValueMapping = timeAndValueMapping;
    }
}
