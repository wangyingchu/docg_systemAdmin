/**
 * Vaadin 25.2 ReactAdapterElement 适配器 ——
 * 将 NvlGraphView React 组件包裹为 Web Component，
 * 使得 Java 侧可通过 @Tag("nvl-graph-react") 直接使用。
 *
 * 架构链路：
 *   MainView.java (VerticalLayout)
 *     └── NvlGraphComponent (@Tag + @JsModule)
 *           └── nvl-graph-adapter.tsx (本文件: ReactAdapterElement)
 *                 └── NvlGraphView.tsx (React 组件: InteractiveNvlWrapper)
 */
import { ReactAdapterElement, type RenderHooks } from 'Frontend/generated/flow/ReactAdapter';
import { NvlGraphView } from './nvl/NvlGraphView';
import type { ReactElement } from 'react';

class NvlGraphReactElement extends ReactAdapterElement {

  protected override render(_hooks: RenderHooks): ReactElement | null {
    return <NvlGraphView />;
  }
}

customElements.define('nvl-graph-react', NvlGraphReactElement);
