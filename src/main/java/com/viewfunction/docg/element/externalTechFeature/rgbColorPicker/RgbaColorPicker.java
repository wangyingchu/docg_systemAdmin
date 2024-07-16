package com.viewfunction.docg.element.externalTechFeature.rgbColorPicker;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;
import com.vaadin.flow.function.SerializableConsumer;


@NpmPackage(value = "react-colorful", version = "5.6.1")
@JsModule("./externalTech/flow/integration/react/rgba-color-picker.tsx")
@Tag("rgba-color-picker")
public class RgbaColorPicker extends ReactAdapterComponent {

    public RgbaColorPicker() {
        setColor(new RgbaColor(44, 124, 120, 0.5));
    }


    public RgbaColor getColor() {
        return getState("color", RgbaColor.class);
    }

    public void setColor(RgbaColor color) {
        setState("color", color);
    }

    public void addColorChangeListener(SerializableConsumer<RgbaColor> listener) {
        addStateChangeListener("color", RgbaColor.class, listener);
    }

}
