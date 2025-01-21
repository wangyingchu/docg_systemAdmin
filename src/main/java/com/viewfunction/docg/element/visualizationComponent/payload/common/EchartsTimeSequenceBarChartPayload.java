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
        //System.out.println(timeAndValueMapping);
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

            /*
            while(dateTimeSet.iterator().hasNext()){
                LocalDateTime currentDateTime = dateTimeSet.iterator().next();
                Long currentValue = timeAndValueMapping.get(currentDateTime);

                JsonObject currentValueObj = Json.createObject();
                currentValueObj.put("time",currentDateTime.toString());
                currentValueObj.put("value",currentValue);
                valueArray.set(index,currentValueObj);
                index++;
            }
            */

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
