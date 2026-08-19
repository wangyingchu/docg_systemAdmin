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
import { useState, useCallback, useRef, useEffect } from 'react';

class NvlGraphReactElement extends ReactAdapterElement {

  protected override render(hooks: RenderHooks): ReactElement | null {

    const [chartData, setChartData] = hooks.useState<any>("chartData");
    //const chartDataStateRef = useRef(chartData);

/*
    useEffect(() => {
      chartDataStateRef.current = chartData;

      console.log("###################");
      console.log("##########SSSS#########");
      console.log("############SSSS#######");
      console.log("###################");

    }, [chartData]);
*/


    const getChartGraphData = () =>{

      console.log("###################");
      console.log("##########SSSS#########");
      console.log(chartData);
      console.log(chartData);
      console.log(chartData);
      console.log("############SSSS#######");
      console.log("###################");

      return {
        nodes:chartData.nodes,
        links:chartData.rels
      }
    }



/*

    // @ts-ignore
    const customSetState = useCallback((updater, callback) => {
      // 前置处理
      console.log('Before update:', chartDataStateRef.current);

      // 支持函数式更新
      const newState = typeof updater === 'function'
          ? updater(chartDataStateRef.current)
          : updater;

      // 验证逻辑
      if (newState && typeof newState === 'object') {
        // 添加时间戳
        const stateWithMeta = {
          ...newState,
          _meta: {
            updatedAt: new Date().toISOString(),
            version: (chartDataStateRef.current._meta?.version || 0) + 1
          }
        };

        // 执行更新
        setChartData(stateWithMeta);

        // 后置处理
        console.log('After update:', stateWithMeta);

        // 触发自定义回调
        if (callback) callback(stateWithMeta);


      }
    }, []);
*/




    return <NvlGraphView
        // @ts-ignore
        graphData={getChartGraphData()}

    />;
  }
}

customElements.define('nvl-graph-react', NvlGraphReactElement);