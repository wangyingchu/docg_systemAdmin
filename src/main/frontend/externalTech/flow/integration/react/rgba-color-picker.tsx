import {RgbaColor, RgbaColorPicker} from "react-colorful";
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import {ReactElement} from "react";

class RgbaColorPickerElement extends ReactAdapterElement {
    protected override render(hooks: RenderHooks): ReactElement | null {
        const [color, setColor] =
            hooks.useState<RgbaColor>("color"); // (2)

        return <RgbaColorPicker
            color={color}
            onChange={setColor}
        />; // (3)
    }
}

customElements.define(
    "rgba-color-picker",
    RgbaColorPickerElement
); // (4)