package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class EchartsTimeSequenceBarChartPayload implements JsonSerializable {

    private Map<LocalDateTime,Long> timeAndValueMapping;
    private static final ObjectMapper mapper = new ObjectMapper();

    public EchartsTimeSequenceBarChartPayload(Map<LocalDateTime,Long> timeAndValueMapping) {
        this.timeAndValueMapping = timeAndValueMapping;
    }

    public EchartsTimeSequenceBarChartPayload() {}

    @Override
    public ObjectNode toJson() {
        ObjectNode obj = mapper.createObjectNode();
        ArrayNode valueArray = mapper.createArrayNode();
        obj.set("data", valueArray);

        if(timeAndValueMapping != null && !timeAndValueMapping.isEmpty()){
            Set<LocalDateTime> dateTimeSet = timeAndValueMapping.keySet();
            int index = 0;
            for(LocalDateTime currentDateTime : dateTimeSet){
                Long currentValue = timeAndValueMapping.get(currentDateTime);
                ObjectNode currentValueObj = mapper.createObjectNode();
                currentValueObj.put("year",currentDateTime.getYear());
                currentValueObj.put("month",currentDateTime.getMonthValue());
                currentValueObj.put("day",currentDateTime.getDayOfMonth());
                currentValueObj.put("hour",currentDateTime.getHour());
                currentValueObj.put("minute",currentDateTime.getMinute());
                currentValueObj.put("second",currentDateTime.getSecond());
                currentValueObj.put("value",currentValue);
                valueArray.insert(index,currentValueObj);
                index++;
            }
        }
        return obj;
    }

    @Override
    public JsonSerializable readJson(JsonNode jsonNode) {
        return null;
    }

    public Map<LocalDateTime, Long> getTimeAndValueMapping() {
        return timeAndValueMapping;
    }

    public void setTimeAndValueMapping(Map<LocalDateTime, Long> timeAndValueMapping) {
        this.timeAndValueMapping = timeAndValueMapping;
    }
}
