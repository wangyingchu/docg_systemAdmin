package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

/**
 * Vaadin 25.2 标准的 React 组件封装 —— 将 nvl-graphExplore 的 React 图可视化
 * 组件以 {@link ReactAdapterComponent} 方式注入 Flow 布局。
 *
 * <h3>架构链路</h3>
 * <pre>
 *   Java (VerticalLayout)
 *     └── NvlGraphComponent  (@Tag + @JsModule)
 *           └── nvl-graph-adapter.tsx  (ReactAdapterElement)
 *                 └── NvlGraphView.tsx  (React: InteractiveNvlWrapper)
 * </pre>
 *
 * <p>该组件可直接作为普通 Vaadin Component 使用：</p>
 * <pre>{@code
 *   VerticalLayout layout = new VerticalLayout();
 *   layout.add(new NvlGraphComponent());
 * }</pre>
 */
@NpmPackage(value = "@neo4j-nvl/base", version = "1.2.1")
@NpmPackage(value = "@neo4j-nvl/react", version = "1.2.1")
@Tag("nvl-graph-react")
@JsModule("./externalTech/flow/integration/react/nvlGraphExplore/nvl-graph-adapter.tsx")
public class ExplorationResultGraphExploreChart extends ReactAdapterComponent {

    /**
     * 创建一个全尺寸的 NVL 图可视化组件。
     * 默认高度 100%，宽度自动撑满父容器。
     */
    public ExplorationResultGraphExploreChart() {
        // ReactAdapterComponent 内置 setState/getState 双向同步能力，
        // 此处暂不需要与 React 侧状态同步，故构造函数为空。
        getElement().getStyle()
                .set("display", "block")
                .set("width", "100%")
                .set("height", "100%");
    }
}
