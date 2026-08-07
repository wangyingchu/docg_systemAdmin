import { createRoot } from "react-dom/client";
import { createElement, useRef, useEffect } from "react";
import { MapInfoChart, type MapInfoChartRef } from "./components/MapInfoChart";
import type { Map } from "maplibre-gl";
import {
  renderEntityContent,
  renderEnvelope,
  renderInteriorPoint,
  renderCentroidPoint,
  clearMap as clearAllFeatures,
} from "./components/Controls";
// @ts-ignore
import "./components/map-styles.css";

const MAP_ID = "map-main";

interface MapAPI {
  renderEntityContent(wkt: string, label: string): void;
  renderEnvelope(wkt: string, label: string): void;
  renderInteriorPoint(wkt: string, label: string): void;
  renderCentroidPoint(wkt: string, label: string): void;
  clearMap(): void;
}
declare global { interface Window { __mapInstances: Record<string, MapAPI> } }
window.__mapInstances ??= {};

function MapContent() {
  const chartRef = useRef<MapInfoChartRef>(null);

  useEffect(() => {
    window.__mapInstances[MAP_ID] = {
      renderEntityContent(w, l) { const m = chartRef.current?.getMap(); if (m) renderEntityContent(w, m, l || undefined); },
      renderEnvelope(w, l) { const m = chartRef.current?.getMap(); if (m) renderEnvelope(w, m, l || undefined); },
      renderInteriorPoint(w, l) { const m = chartRef.current?.getMap(); if (m) renderInteriorPoint(w, m, l || undefined); },
      renderCentroidPoint(w, l) { const m = chartRef.current?.getMap(); if (m) renderCentroidPoint(w, m, l || undefined); },
      clearMap() { const m = chartRef.current?.getMap(); if (m) clearAllFeatures(m); },
    };
    return () => { delete window.__mapInstances[MAP_ID]; };
  }, []);

  return createElement(MapInfoChart, {
    ref: chartRef,
    containerStyle: { width: "100%", height: "100%" },
  });
}

class MapHost extends HTMLElement {
  #root: any = null;
  connectedCallback() {
    this.style.display = "block"; this.style.width = "100%"; this.style.height = "100%";
    const m = document.createElement("div"); m.style.width = "100%"; m.style.height = "100%";
    this.appendChild(m);
    this.#root = createRoot(m);
    this.#root.render(createElement(MapContent));
  }
  disconnectedCallback() { this.#root?.unmount(); }
}
customElements.get("map-info-chart") || customElements.define("map-info-chart", MapHost);
export default MapHost;
