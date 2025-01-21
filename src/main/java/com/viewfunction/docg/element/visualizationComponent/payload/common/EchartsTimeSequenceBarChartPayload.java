package com.viewfunction.docg.element.visualizationComponent.payload.common;

import com.vaadin.flow.component.JsonSerializable;
import elemental.json.JsonObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class EchartsTimeSequenceBarChartPayload implements JsonSerializable {
    private String name;
    private Integer value;

    public EchartsTimeSequenceBarChartPayload(ArrayList<Map<LocalDateTime,Long>> timeAndValueMappingList) {}

    @Override
    public JsonObject toJson() {
        return null;
    }

    @Override
    public JsonSerializable readJson(JsonObject jsonObject) {
        return null;
    }
}
