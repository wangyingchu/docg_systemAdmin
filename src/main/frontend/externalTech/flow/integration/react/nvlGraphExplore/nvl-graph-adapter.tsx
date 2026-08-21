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

  protected override render(hooks: RenderHooks): ReactElement | null {
    const [chartData, setChartData] = hooks.useState<any>("chartData");

    const getChartGraphData = () =>{
      return {
        nodes:chartData.nodes,
        rels:chartData.rels
      }
    }

    // @ts-ignore
    const handleNodeSelectAction = (data) => {
      console.log('handleNodeSelectAction 子组件调用了这个方法，数据：', data);
      // 执行父组件逻辑
    };

    // @ts-ignore
    const handleNodeDoubleClickedAction = (data) => {
      console.log('handleNodeDoubleClickedAction 子组件调用了这个方法，数据：', data);
      // 执行父组件逻辑
    };

    return <NvlGraphView
        // @ts-ignore
        graphData = {getChartGraphData()}
        nodeSelectAction = {handleNodeSelectAction}
        nodeDoubleClickAction = {handleNodeDoubleClickedAction}
    />;
  }
}

customElements.define('nvl-graph-react', NvlGraphReactElement);