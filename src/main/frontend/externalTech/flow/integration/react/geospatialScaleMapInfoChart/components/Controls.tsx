import { useEffect, useRef, useState, useCallback, type ReactNode } from "react";
import { jsPDF } from "jspdf";
import { bbox as turfBbox } from "@turf/bbox";
import type { Feature, Position } from "geojson";
import {
  NavigationControl as MaplibreNavigationControl,
  ScaleControl as MaplibreScaleControl,
  FullscreenControl as MaplibreFullscreenControl,
  GeolocateControl as MaplibreGeolocateControl,
  AttributionControl as MaplibreAttributionControl,
  Popup as MaplibrePopup,
  type NavigationControlOptions,
  type ScaleControlOptions,
  type FullscreenControlOptions,
  type GeolocateControlOptions,
  type AttributionControlOptions,
  type ControlPosition,
  type StyleSpecification,
} from "maplibre-gl";
import { useMap } from "./MapContext";

/* ------------------------------------------------------------------ */
/*  NavigationControl                                                  */
/* ------------------------------------------------------------------ */

export interface NavigationControlProps extends NavigationControlOptions {
  /** MapLibre control position (default: 'top-left') */
  position?: ControlPosition;
}

export function NavigationControl({
  position = "top-left",
  ...options
}: NavigationControlProps) {
  const map = useMap();
  const ctrlRef = useRef<MaplibreNavigationControl | null>(null);

  useEffect(() => {
    if (!map) return;
    const ctrl = new MaplibreNavigationControl(options);
    map.addControl(ctrl, position);
    ctrlRef.current = ctrl;
    return () => {
      map.removeControl(ctrl);
      ctrlRef.current = null;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [map]);

  return null;
}

/* ------------------------------------------------------------------ */
/*  ScaleControl                                                       */
/* ------------------------------------------------------------------ */

export interface ScaleControlProps extends ScaleControlOptions {
  position?: ControlPosition;
}

export function ScaleControl({
  position = "bottom-left",
  ...options
}: ScaleControlProps) {
  const map = useMap();
  const ctrlRef = useRef<MaplibreScaleControl | null>(null);

  useEffect(() => {
    if (!map) return;
    const ctrl = new MaplibreScaleControl(options);
    map.addControl(ctrl, position);
    ctrlRef.current = ctrl;
    return () => {
      map.removeControl(ctrl);
      ctrlRef.current = null;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [map]);

  return null;
}

/* ------------------------------------------------------------------ */
/*  FullscreenControl                                                  */
/* ------------------------------------------------------------------ */

export interface FullscreenControlProps extends FullscreenControlOptions {
  position?: ControlPosition;
}

export function FullscreenControl({
  position = "top-right",
  ...options
}: FullscreenControlProps) {
  const map = useMap();
  const ctrlRef = useRef<MaplibreFullscreenControl | null>(null);

  useEffect(() => {
    if (!map) return;
    const ctrl = new MaplibreFullscreenControl(options);
    map.addControl(ctrl, position);
    ctrlRef.current = ctrl;
    return () => {
      map.removeControl(ctrl);
      ctrlRef.current = null;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [map]);

  return null;
}

/* ------------------------------------------------------------------ */
/*  GeolocateControl                                                   */
/* ------------------------------------------------------------------ */

export interface GeolocateControlProps extends GeolocateControlOptions {
  position?: ControlPosition;
}

export function GeolocateControl({
  position = "top-right",
  ...options
}: GeolocateControlProps) {
  const map = useMap();
  const ctrlRef = useRef<MaplibreGeolocateControl | null>(null);

  useEffect(() => {
    if (!map) return;
    const ctrl = new MaplibreGeolocateControl(options);
    map.addControl(ctrl, position);
    ctrlRef.current = ctrl;
    return () => {
      map.removeControl(ctrl);
      ctrlRef.current = null;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [map]);

  return null;
}

/* ------------------------------------------------------------------ */
/*  AttributionControl                                                 */
/* ------------------------------------------------------------------ */

export interface AttributionControlProps extends AttributionControlOptions {
  position?: ControlPosition;
}

export function AttributionControl({
  position = "bottom-right",
  ...options
}: AttributionControlProps) {
  const map = useMap();
  const ctrlRef = useRef<MaplibreAttributionControl | null>(null);

  useEffect(() => {
    if (!map) return;
    const ctrl = new MaplibreAttributionControl(options);
    map.addControl(ctrl, position);
    ctrlRef.current = ctrl;
    return () => {
      map.removeControl(ctrl);
      ctrlRef.current = null;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [map]);

  return null;
}

/* ------------------------------------------------------------------ */
/*  StyleSwitcher — React-rendered pill control                        */
/* ------------------------------------------------------------------ */

export interface StyleOption {
  /** Display label */
  label: string;
  /** maplibre style URL or StyleSpecification */
  style: string | StyleSpecification;
}

export interface StyleSwitcherProps {
  /** Available style options */
  options: StyleOption[];
  /** Currently active style (compared by reference or URL string) */
  activeStyle: string | StyleSpecification;
  /** Called when user picks a new style */
  onChange: (style: string | StyleSpecification) => void;
}

/**
 * A compact floating pill that lets users switch between map styles.
 * Rendered as a React element — position it with CSS (e.g. absolute).
 */
export function StyleSwitcher({
  options,
  activeStyle,
  onChange,
}: StyleSwitcherProps) {
  const map = useMap();
  const [open, setOpen] = useState(false);
  const panelRef = useRef<HTMLDivElement>(null);

  // Close on outside click
  const handleClickOutside = useCallback(
    (e: MouseEvent) => {
      if (panelRef.current && !panelRef.current.contains(e.target as Node)) {
        setOpen(false);
      }
    },
    []
  );

  useEffect(() => {
    if (!open) return;
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [open, handleClickOutside]);

  const activeLabel =
    options.find((o) => o.style === activeStyle)?.label ?? "样式";

  if (!map) return null;

  return (
    <div ref={panelRef} className="maplibre-style-switcher">
      <button
        className="maplibre-style-switcher__btn"
        onClick={() => setOpen((v) => !v)}
        title="切换地图样式"
      >
        <span className="maplibre-style-switcher__icon">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none"
            stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <polygon points="12 2 2 7 12 12 22 7 12 2" />
            <polyline points="2 17 12 22 22 17" />
            <polyline points="2 12 12 17 22 12" />
          </svg>
        </span>
        <span className="maplibre-style-switcher__label">{activeLabel}</span>
      </button>

      {open && (
        <ul className="maplibre-style-switcher__menu">
          {options.map((opt) => (
            <li
              key={opt.label}
              className={
                "maplibre-style-switcher__item" +
                (opt.style === activeStyle
                  ? " maplibre-style-switcher__item--active"
                  : "")
              }
              onClick={() => {
                onChange(opt.style);
                setOpen(false);
              }}
            >
              {opt.label}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

/* ------------------------------------------------------------------ */
/*  ResetViewButton — custom home button                               */
/* ------------------------------------------------------------------ */

export interface ResetViewButtonProps {
  /** Target center [lng, lat] */
  center: [number, number];
  /** Target zoom level */
  zoom?: number;
}

/**
 * A simple "home" button that flies back to the configured center/zoom.
 * Renders as a React element — position with CSS.
 */
export function ResetViewButton({ center, zoom }: ResetViewButtonProps) {
  const map = useMap();

  if (!map) return null;

  return (
    <div className="maplibre-reset-view">
      <button
        className="maplibre-ctrl-btn"
        title="回到初始视图"
        onClick={() => map.flyTo({ center, zoom: zoom ?? map.getZoom() })}
      >
        <svg
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
          <polyline points="9 22 9 12 15 12 15 22" />
        </svg>
      </button>
    </div>
  );
}

/* ------------------------------------------------------------------ */
/*  TerrainControl — 3D terrain toggle                                 */
/* ------------------------------------------------------------------ */

export interface TerrainControlProps {
  /** Exaggeration factor (default: 2.5) */
  exaggeration?: number;
  /** Pitch angle when 3D is enabled (default: 65) */
  pitch?: number;
}

/** Terrain RGB tile source — AWS terranium (verified 200 OK, global coverage) */
const TERRAIN_SOURCE_ID = "maplibre-terrain";
const TERRAIN_SOURCE = {
  type: "raster-dem" as const,
  tiles: [
    "https://s3.amazonaws.com/elevation-tiles-prod/terrarium/{z}/{x}/{y}.png",
  ],
  tileSize: 256,
  encoding: "terrarium" as const,
  maxzoom: 15,
  attribution: "&copy; Tilezen Joerd",
};

/**
 * Toggle button that enables/disables 3D terrain.
 * When enabled it adds a raster-dem source and pitches the camera.
 */
export function TerrainControl({
  exaggeration = 2.5,
  pitch = 65,
}: TerrainControlProps) {
  const map = useMap();
  const [enabled, setEnabled] = useState(false);

  useEffect(() => {
    if (!map) return;

    // Re-apply terrain when style data loads (e.g., after style switch)
    const reapply = () => {
      if (!enabled) return;
      if (!map.getSource(TERRAIN_SOURCE_ID)) {
        map.addSource(TERRAIN_SOURCE_ID, TERRAIN_SOURCE);
      }
      map.setTerrain({ source: TERRAIN_SOURCE_ID, exaggeration });
    };

    if (enabled) {
      reapply();
      map.flyTo({ pitch, duration: 1200 });
    } else {
      map.setTerrain(null);
      map.flyTo({ pitch: 0, duration: 800 });
    }

    map.on("styledata", reapply);
    return () => {
      map.off("styledata", reapply);
    };
  }, [map, enabled, exaggeration, pitch]);

  if (!map) return null;

  return (
    <div className="maplibre-terrain-control">
      <button
        className={
          "maplibre-ctrl-btn" +
          (enabled ? " maplibre-ctrl-btn--active" : "")
        }
        title={enabled ? "关闭 3D 地形" : "开启 3D 地形"}
        onClick={() => setEnabled((v) => !v)}
      >
        <svg
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <polygon points="12 2 22 8.5 22 15.5 12 22 2 15.5 2 8.5" />
          <line x1="12" y1="22" x2="12" y2="15.5" />
          <polyline points="22 8.5 12 15.5 2 8.5" />
        </svg>
      </button>
    </div>
  );
}

/* ------------------------------------------------------------------ */
/*  ExportControl — 导出地图为 PNG / JPEG / PDF                        */
/* ------------------------------------------------------------------ */

export type ExportFormat = "png" | "jpeg" | "pdf";

export interface ExportControlProps {
  /** File name prefix (default: 'map') */
  fileName?: string;
  /** JPEG quality 0–1 (default: 0.95) */
  jpegQuality?: number;
}

/**
 * Draw the map canvas onto a new canvas with a solid white background.
 * Necessary for JPEG export because maplibre's canvas is transparent
 * and JPEG doesn't support alpha.
 */
function canvasWithWhiteBg(source: HTMLCanvasElement): HTMLCanvasElement {
  const w = source.width;
  const h = source.height;
  const canvas = document.createElement("canvas");
  canvas.width = w;
  canvas.height = h;
  const ctx = canvas.getContext("2d")!;
  ctx.fillStyle = "#ffffff";
  ctx.fillRect(0, 0, w, h);
  ctx.drawImage(source, 0, 0);
  return canvas;
}

/** Trigger a browser download for a data URL */
function downloadDataUrl(dataUrl: string, fileName: string) {
  const a = document.createElement("a");
  a.href = dataUrl;
  a.download = fileName;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
}

/**
 * Floating pill button that lets users export the current map view
 * as PNG, JPEG, or PDF. Uses maplibre's built-in canvas and jsPDF.
 */
export function ExportControl({
  fileName = "map",
  jpegQuality = 0.95,
}: ExportControlProps) {
  const map = useMap();
  const [open, setOpen] = useState(false);
  const panelRef = useRef<HTMLDivElement>(null);

  // Close on outside click
  const handleClickOutside = useCallback((e: MouseEvent) => {
    if (panelRef.current && !panelRef.current.contains(e.target as Node)) {
      setOpen(false);
    }
  }, []);

  useEffect(() => {
    if (!open) return;
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [open, handleClickOutside]);

  if (!map) return null;

  const doExport = (format: ExportFormat) => {
    setOpen(false);
    const canvas = map.getCanvas();
    if (!canvas) return;

    // Wait one animation frame so the WebGL drawing buffer is flushed.
    // Required even with preserveDrawingBuffer because the user may have
    // clicked mid-frame before the GPU has finished compositing.
    requestAnimationFrame(() => {
      const ext = format === "jpeg" ? "jpg" : format;
      const fname = `${fileName}.${ext}`;

      if (format === "png") {
        downloadDataUrl(canvas.toDataURL("image/png"), fname);
      } else if (format === "jpeg") {
        const bgCanvas = canvasWithWhiteBg(canvas);
        downloadDataUrl(bgCanvas.toDataURL("image/jpeg", jpegQuality), fname);
      } else if (format === "pdf") {
        const dataUrl = canvas.toDataURL("image/png");
        const w = canvas.width;
        const h = canvas.height;
        // Scale to fit an A4 page with margins
        const pdf = new jsPDF({ orientation: w > h ? "landscape" : "portrait", unit: "px" });
        const pw = pdf.internal.pageSize.getWidth();
        const ph = pdf.internal.pageSize.getHeight();
        const margin = 20;
        const availW = pw - margin * 2;
        const availH = ph - margin * 2;
        const scale = Math.min(availW / w, availH / h);
        const imgW = w * scale;
        const imgH = h * scale;
        const x = (pw - imgW) / 2;
        const y = (ph - imgH) / 2;
        pdf.addImage(dataUrl, "PNG", x, y, imgW, imgH);
        pdf.save(fname);
      }
    });
  };

  const formats: { label: string; format: ExportFormat; icon: ReactNode }[] = [
    {
      label: "导出 PNG",
      format: "png",
      icon: (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
          <circle cx="8.5" cy="8.5" r="1.5" />
          <polyline points="21 15 16 10 5 21" />
        </svg>
      ),
    },
    {
      label: "导出 JPEG",
      format: "jpeg",
      icon: (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
          <circle cx="8.5" cy="8.5" r="1.5" />
          <polyline points="21 15 16 10 5 21" />
        </svg>
      ),
    },
    {
      label: "导出 PDF",
      format: "pdf",
      icon: (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
          <polyline points="14 2 14 8 20 8" />
          <line x1="16" y1="13" x2="8" y2="13" />
          <line x1="16" y1="17" x2="8" y2="17" />
          <polyline points="10 9 9 9 8 9" />
        </svg>
      ),
    },
  ];

  return (
    <div ref={panelRef} className="maplibre-export-control">
      <button
        className="maplibre-export-control__btn"
        onClick={() => setOpen((v) => !v)}
        title="导出地图图片"
      >
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
          <polyline points="7 10 12 15 17 10" />
          <line x1="12" y1="15" x2="12" y2="3" />
        </svg>
      </button>

      {open && (
        <ul className="maplibre-export-control__menu">
          {formats.map((f) => (
            <li
              key={f.format}
              className="maplibre-export-control__item"
              onClick={() => doExport(f.format)}
            >
              <span className="maplibre-export-control__icon">{f.icon}</span>
              {f.label}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

/* ------------------------------------------------------------------ */
/*  ToggleUIButton — 显示/隐藏所有 overlay 元素                         */
/* ------------------------------------------------------------------ */

export interface ToggleUIButtonProps {
  /** Whether UI is currently visible */
  visible: boolean;
  /** Called when user toggles */
  onToggle: () => void;
}

export function ToggleUIButton({ visible, onToggle }: ToggleUIButtonProps) {
  const map = useMap();

  // Toggle GIS layer visibility when button state changes
  useEffect(() => {
    if (!map) return;

    const applyVisibility = () => {
      const v = visible ? "visible" : "none";
      const layers = [
        "wkt-fill", "wkt-line", "wkt-point",
        "envelope-fill", "envelope-line", "envelope-point",
        "interior-fill", "interior-line", "interior-point",
        "centroid-fill", "centroid-line", "centroid-point",
      ];
      for (const id of layers) {
        if (map.getLayer(id)) {
          map.setLayoutProperty(id, "visibility", v);
        }
      }
    };

    applyVisibility();
    // Re-apply after style switches (layers are recreated)
    map.on("styledata", applyVisibility);
    return () => { map.off("styledata", applyVisibility); };
  }, [map, visible]);

  if (!map) return null;

  return (
    <div className="maplibre-toggle-ui">
      <button
        className={
          "maplibre-ctrl-btn" +
          (!visible ? " maplibre-ctrl-btn--active" : "")
        }
        title={visible ? "隐藏 GIS 要素" : "显示 GIS 要素"}
        onClick={onToggle}
      >
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          {visible ? (
            <>
              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
              <circle cx="12" cy="12" r="3" />
            </>
          ) : (
            <>
              <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94" />
              <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19" />
              <line x1="1" y1="1" x2="23" y2="23" />
            </>
          )}
        </svg>
      </button>
    </div>
  );
}

/* ================================================================== */
import { parseWkt } from "./WktParser";

/* ================================================================== */
/*  Shared WKT / Envelope source & layer IDs + helpers                  */
/* ================================================================== */

const WKT_SOURCE = "wkt-source";
const WKT_LAYER_FILL = "wkt-fill";
const WKT_LAYER_LINE = "wkt-line";
const WKT_LAYER_POINT = "wkt-point";

const ENVELOPE_SOURCE = "envelope-source";
const ENVELOPE_LAYER_FILL = "envelope-fill";
const ENVELOPE_LAYER_LINE = "envelope-line";
const ENVELOPE_LAYER_POINT = "envelope-point";

const INTERIOR_SOURCE = "interior-source";
const INTERIOR_LAYER_FILL = "interior-fill";
const INTERIOR_LAYER_LINE = "interior-line";
const INTERIOR_LAYER_POINT = "interior-point";

const CENTROID_SOURCE = "centroid-source";
const CENTROID_LAYER_FILL = "centroid-fill";
const CENTROID_LAYER_LINE = "centroid-line";
const CENTROID_LAYER_POINT = "centroid-point";
const CENTROID_IMAGE_X = "centroid-x";

/* ---- WKT layers ---- */

function ensureWktLayers(map: import("maplibre-gl").Map) {
  if (!map.getSource(WKT_SOURCE)) {
    try { map.addSource(WKT_SOURCE, {
      type: "geojson",
      data: { type: "FeatureCollection", features: [] },
    }); } catch { /* ok */ }
  }
  const add = (def: any) => {
    if (!map.getLayer(def.id)) {
      try { map.addLayer(def); } catch { /* ok */ }
    }
  };
  add({ id: WKT_LAYER_FILL, type: "fill", source: WKT_SOURCE,
        paint: { "fill-color": "#1685a9", "fill-opacity": 0.5 },
        filter: ["==", "$type", "Polygon"] });
  add({ id: WKT_LAYER_LINE, type: "line", source: WKT_SOURCE,
        paint: { "line-color": "#003472", "line-width": 2.5, "line-opacity": 0.9 },
        filter: ["any", ["==", "$type", "LineString"], ["==", "$type", "Polygon"]] });
  add({ id: WKT_LAYER_POINT, type: "circle", source: WKT_SOURCE,
        paint: { "circle-radius": 6, "circle-color": "#1685a9", "circle-opacity": 0.9,
                 "circle-stroke-color": "#003472", "circle-stroke-width": 0.6,
                 "circle-stroke-opacity": 0.9 },
        filter: ["==", "$type", "Point"] });
}

export function clearWktData(map: import("maplibre-gl").Map) {
  try {
    const src = map.getSource(WKT_SOURCE) as any;
    if (src) src.setData({ type: "FeatureCollection", features: [] });
  } catch { /* ok */ }
  // Also clear external feature store
  wktFeatureStore.delete(map);
}

/* ---- External feature store (avoids relying on source._data) ---- */

const wktFeatureStore = new WeakMap<import("maplibre-gl").Map, Feature[]>();

/* ---- Popup per map — registered once, reads label from feature ---- */

const POPUP_LAYERS = [
  WKT_LAYER_FILL, WKT_LAYER_LINE, WKT_LAYER_POINT,
  ENVELOPE_LAYER_FILL, ENVELOPE_LAYER_LINE, ENVELOPE_LAYER_POINT,
  INTERIOR_LAYER_FILL, INTERIOR_LAYER_LINE, INTERIOR_LAYER_POINT,
  CENTROID_LAYER_FILL, CENTROID_LAYER_LINE, CENTROID_LAYER_POINT,
];

interface WktPopupState {
  popup: import("maplibre-gl").Popup | null;
  onClick: (e: import("maplibre-gl").MapMouseEvent) => void;
}

const wktPopupStates = new WeakMap<import("maplibre-gl").Map, WktPopupState>();

function ensureWktPopup(map: import("maplibre-gl").Map) {
  if (wktPopupStates.has(map)) return;

  const onClick = (e: import("maplibre-gl").MapMouseEvent) => {
    const existingLayers = POPUP_LAYERS.filter((id) => map.getLayer(id));
    if (existingLayers.length === 0) return;
    const features = map.queryRenderedFeatures(e.point, { layers: existingLayers });
    const state = wktPopupStates.get(map);
    state?.popup?.remove();
    if (state) state.popup = null;

    if (features.length === 0) return;

    const label = features[0].properties?.label as string | undefined;
    if (!label) return;

    const popup = new MaplibrePopup({ closeButton: false, closeOnClick: false })
      .setLngLat(e.lngLat)
      .setHTML(`<div class="wkt-popup-label">${label}</div>`)
      .addTo(map);
    if (state) state.popup = popup;
  };

  map.on("click", onClick);
  wktPopupStates.set(map, { popup: null, onClick });
}

/* ---- Style-switch recovery — re-create layers after setStyle ---- */

type FeatureStoreMap = WeakMap<import("maplibre-gl").Map, Feature[]>;

const allStores: {
  store: FeatureStoreMap;
  ensureLayers: (m: import("maplibre-gl").Map) => void;
  sourceId: string;
}[] = [];

function registerStore(
  store: FeatureStoreMap,
  ensureLayers: (m: import("maplibre-gl").Map) => void,
  sourceId: string,
) {
  allStores.push({ store, ensureLayers, sourceId });
}

const _restoreFlags = new WeakMap<import("maplibre-gl").Map, boolean>();

function ensureStyleRestore(map: import("maplibre-gl").Map) {
  if (_restoreFlags.has(map)) return;
  _restoreFlags.set(map, true);
  map.on("styledata", () => {
    for (const s of allStores) {
      const features = s.store.get(map);
      if (!features || features.length === 0) continue;
      s.ensureLayers(map);
      try {
        const src = map.getSource(s.sourceId) as any;
        if (src) src.setData({ type: "FeatureCollection", features });
      } catch { /* ok */ }
    }
  });
}

// Register all feature stores for recovery
registerStore(wktFeatureStore, ensureWktLayers, WKT_SOURCE);

/**
 * 解析 WKT 字符串并在指定 Map 实例上渲染为 GeoJSON 要素。
 * 多次调用会累积要素（不清除已有的）。地图定位到最新要素的范围。
 *
 * @param wkt   - Well-Known Text 格式的 GIS 要素字符串
 * @param map   - maplibre-gl Map 实例
 * @param label - 可选标签，存入 feature.properties.label。点击要素弹出信息卡
 * @returns     解析后的 GeoJSON Feature
 * @throws      解析失败或 WKT 格式无效时抛出 Error
 */
export function renderEntityContent(
  wkt: string,
  map: import("maplibre-gl").Map,
  label?: string,
): Feature {
  const feature = parseWkt(wkt);
  if (label) feature.properties = { ...feature.properties, label };

  ensureWktLayers(map);
  ensureWktPopup(map);
  ensureStyleRestore(map);

  const existing = wktFeatureStore.get(map) ?? [];
  const allFeatures = [...existing, feature];
  wktFeatureStore.set(map, allFeatures);

  const src = map.getSource(WKT_SOURCE) as any;
  if (src) src.setData({ type: "FeatureCollection", features: allFeatures });

  const b = turfBbox(feature);
  map.fitBounds([[b[0], b[1]], [b[2], b[3]]], { padding: 60, duration: 800 });

  return feature;
}

/* ================================================================== */
/*  renderEnvelope — 信封样式要素渲染（虚线边框，灰底）                    */
/* ================================================================== */

function ensureEnvelopeLayers(map: import("maplibre-gl").Map) {
  if (!map.getSource(ENVELOPE_SOURCE)) {
    try { map.addSource(ENVELOPE_SOURCE, {
      type: "geojson",
      data: { type: "FeatureCollection", features: [] },
    }); } catch { /* ok */ }
  }
  const add = (def: any) => {
    if (!map.getLayer(def.id)) {
      try { map.addLayer(def); } catch { /* ok */ }
    }
  };
  add({ id: ENVELOPE_LAYER_FILL, type: "fill", source: ENVELOPE_SOURCE,
        paint: { "fill-color": "#AAAAAA", "fill-opacity": 0.2 },
        filter: ["==", "$type", "Polygon"] });
  add({ id: ENVELOPE_LAYER_LINE, type: "line", source: ENVELOPE_SOURCE,
        paint: { "line-color": "#666666", "line-width": 1, "line-opacity": 0.4,
                 "line-dasharray": [6, 4] },
        filter: ["any", ["==", "$type", "LineString"], ["==", "$type", "Polygon"]] });
  add({ id: ENVELOPE_LAYER_POINT, type: "circle", source: ENVELOPE_SOURCE,
        paint: { "circle-radius": 5, "circle-color": "#AAAAAA", "circle-opacity": 0.2,
                 "circle-stroke-color": "#666666", "circle-stroke-width": 2,
                 "circle-stroke-opacity": 0.4 },
        filter: ["==", "$type", "Point"] });
}

const envelopeFeatureStore = new WeakMap<import("maplibre-gl").Map, Feature[]>();

export function clearEnvelopeData(map: import("maplibre-gl").Map) {
  try {
    const src = map.getSource(ENVELOPE_SOURCE) as any;
    if (src) src.setData({ type: "FeatureCollection", features: [] });
  } catch { /* ok */ }
  envelopeFeatureStore.delete(map);
}

/**
 * 与 renderEntityContent 功能相同，但使用信封样式渲染：
 * 边框 #666666 40% 不透明度 dash 线段，填充 #AAAAAA 20% 不透明度。
 */
export function renderEnvelope(
  wkt: string,
  map: import("maplibre-gl").Map,
  label?: string,
): Feature {
  const feature = parseWkt(wkt);
  if (label) feature.properties = { ...feature.properties, label };

  ensureEnvelopeLayers(map);
  ensureWktPopup(map);
  ensureStyleRestore(map);  // reuse same popup, already queries envelope layers

  const existing = envelopeFeatureStore.get(map) ?? [];
  const allFeatures = [...existing, feature];
  envelopeFeatureStore.set(map, allFeatures);

  const src = map.getSource(ENVELOPE_SOURCE) as any;
  if (src) src.setData({ type: "FeatureCollection", features: allFeatures });

  const b = turfBbox(feature);
  map.fitBounds([[b[0], b[1]], [b[2], b[3]]], { padding: 60, duration: 800 });

  return feature;
}

/* ================================================================== */
/*  renderInteriorPoint — interior 样式（黑色虚线边框，深灰浅底）         */
/* ================================================================== */

function ensureInteriorLayers(map: import("maplibre-gl").Map) {
  if (!map.getSource(INTERIOR_SOURCE)) {
    try { map.addSource(INTERIOR_SOURCE, {
      type: "geojson",
      data: { type: "FeatureCollection", features: [] },
    }); } catch { /* ok */ }
  }
  const add = (def: any) => {
    if (!map.getLayer(def.id)) {
      try { map.addLayer(def); } catch { /* ok */ }
    }
  };
  add({ id: INTERIOR_LAYER_FILL, type: "fill", source: INTERIOR_SOURCE,
        paint: { "fill-color": "#444444", "fill-opacity": 0.05 },
        filter: ["==", "$type", "Polygon"] });
  add({ id: INTERIOR_LAYER_LINE, type: "line", source: INTERIOR_SOURCE,
        paint: { "line-color": "#000000", "line-width": 2, "line-opacity": 0.2,
                 "line-dasharray": [6, 4] },
        filter: ["any", ["==", "$type", "LineString"], ["==", "$type", "Polygon"]] });
  add({ id: INTERIOR_LAYER_POINT, type: "circle", source: INTERIOR_SOURCE,
        paint: { "circle-radius": 3, "circle-color": "#444444", "circle-opacity": 0.1,
                 "circle-stroke-color": "#000000", "circle-stroke-width": 1,
                 "circle-stroke-opacity": 0.4 },
        filter: ["==", "$type", "Point"] });
}

const interiorFeatureStore = new WeakMap<import("maplibre-gl").Map, Feature[]>();

export function clearInteriorData(map: import("maplibre-gl").Map) {
  try {
    const src = map.getSource(INTERIOR_SOURCE) as any;
    if (src) src.setData({ type: "FeatureCollection", features: [] });
  } catch { /* ok */ }
  interiorFeatureStore.delete(map);
}

/**
 * 与 renderEntityContent 功能相同，但使用 interior 样式渲染：
 * 边框 #000000 20% 不透明度 dash 线段，填充 #444444 5% 不透明度。
 */
export function renderInteriorPoint(
  wkt: string,
  map: import("maplibre-gl").Map,
  label?: string,
): Feature {
  const feature = parseWkt(wkt);
  if (label) feature.properties = { ...feature.properties, label };

  ensureInteriorLayers(map);
  ensureWktPopup(map);
  ensureStyleRestore(map);

  const existing = interiorFeatureStore.get(map) ?? [];
  const allFeatures = [...existing, feature];
  interiorFeatureStore.set(map, allFeatures);

  const src = map.getSource(INTERIOR_SOURCE) as any;
  if (src) src.setData({ type: "FeatureCollection", features: allFeatures });

  const b = turfBbox(feature);
  map.fitBounds([[b[0], b[1]], [b[2], b[3]]], { padding: 60, duration: 800 });

  return feature;
}

/* ================================================================== */
/*  renderCentroidPoint — centroid 样式（X 符号点，灰虚线边框）           */
/* ================================================================== */

/** Create a small X icon as raw RGBA pixel data and register on the map. */
function ensureCentroidXIcon(map: import("maplibre-gl").Map) {
  if (map.hasImage(CENTROID_IMAGE_X)) return;

  const size = 16;
  const data = new Uint8Array(size * size * 4);
  // Draw an X shape: two diagonal lines with 2px thickness
  for (let y = 0; y < size; y++) {
    for (let x = 0; x < size; x++) {
      const onDiag1 = Math.abs(x - y) <= 1;
      const onDiag2 = Math.abs((size - 1 - x) - y) <= 1;
      if (onDiag1 || onDiag2) {
        const i = (y * size + x) * 4;
        data[i]     = 0x66; // R
        data[i + 1] = 0x66; // G
        data[i + 2] = 0x66; // B
        data[i + 3] = 102;  // A ≈ 0.4 * 255
      }
    }
  }
  try { map.addImage(CENTROID_IMAGE_X, { width: size, height: size, data }); } catch { /* ok */ }
}

function ensureCentroidLayers(map: import("maplibre-gl").Map) {
  if (!map.getSource(CENTROID_SOURCE)) {
    try { map.addSource(CENTROID_SOURCE, {
      type: "geojson",
      data: { type: "FeatureCollection", features: [] },
    }); } catch { /* ok */ }
  }
  const add = (def: any) => {
    if (!map.getLayer(def.id)) {
      try { map.addLayer(def); } catch { /* ok */ }
    }
  };
  add({ id: CENTROID_LAYER_FILL, type: "fill", source: CENTROID_SOURCE,
        paint: { "fill-color": "#AAAAAA", "fill-opacity": 0.5 },
        filter: ["==", "$type", "Polygon"] });
  add({ id: CENTROID_LAYER_LINE, type: "line", source: CENTROID_SOURCE,
        paint: { "line-color": "#666666", "line-width": 2, "line-opacity": 0.6,
                 "line-dasharray": [6, 4] },
        filter: ["any", ["==", "$type", "LineString"], ["==", "$type", "Polygon"]] });
  // Point as X symbol instead of circle
  add({ id: CENTROID_LAYER_POINT, type: "symbol", source: CENTROID_SOURCE,
        layout: { "icon-image": CENTROID_IMAGE_X, "icon-size": 0.5, "icon-allow-overlap": true,
                  "icon-ignore-placement": true },
        filter: ["==", "$type", "Point"] });
}

const centroidFeatureStore = new WeakMap<import("maplibre-gl").Map, Feature[]>();

export function clearCentroidData(map: import("maplibre-gl").Map) {
  try {
    const src = map.getSource(CENTROID_SOURCE) as any;
    if (src) src.setData({ type: "FeatureCollection", features: [] });
  } catch { /* ok */ }
  centroidFeatureStore.delete(map);
}

/**
 * 与 renderEntityContent 功能相同，但使用 centroid 样式渲染：
 * 边框 #666666 20% 不透明度 dash 线段，填充 #AAAAAA 50% 不透明度，
 * Point 要素显示为 X 符号。
 */
export function renderCentroidPoint(
  wkt: string,
  map: import("maplibre-gl").Map,
  label?: string,
): Feature {
  const feature = parseWkt(wkt);
  if (label) feature.properties = { ...feature.properties, label };

  ensureCentroidXIcon(map);
  ensureCentroidLayers(map);
  ensureWktPopup(map);
  ensureStyleRestore(map);

  const existing = centroidFeatureStore.get(map) ?? [];
  const allFeatures = [...existing, feature];
  centroidFeatureStore.set(map, allFeatures);

  const src = map.getSource(CENTROID_SOURCE) as any;
  if (src) src.setData({ type: "FeatureCollection", features: allFeatures });

  const b = turfBbox(feature);
  map.fitBounds([[b[0], b[1]], [b[2], b[3]]], { padding: 60, duration: 800 });

  return feature;
}

/* ================================================================== */
/*  clearMap — 清除所有自定义要素                                       */
/* ================================================================== */

/** Clear all WKT / Envelope / Interior / Centroid data from the map. */
export function clearMap(map: import("maplibre-gl").Map) {
  clearWktData(map);
  clearEnvelopeData(map);
  clearInteriorData(map);
  clearCentroidData(map);
}

// Deferred registrations (stores defined after the initial registration block)
registerStore(envelopeFeatureStore, ensureEnvelopeLayers, ENVELOPE_SOURCE);
registerStore(interiorFeatureStore, ensureInteriorLayers, INTERIOR_SOURCE);
registerStore(centroidFeatureStore, ensureCentroidLayers, CENTROID_SOURCE);

/* ================================================================== */
/*  WktInput — WKT 要素输入 UI 控件（调用 renderEntityContent）          */
/* ================================================================== */

export interface WktInputProps {
  placeholder?: string;
}

export function WktInput({ placeholder = "输入 WKT 要素，如 POINT(116.4 39.9)" }: WktInputProps) {
  const map = useMap();
  const [open, setOpen] = useState(false);
  const [text, setText] = useState("");
  const [error, setError] = useState("");
  const [info, setInfo] = useState("");
  const panelRef = useRef<HTMLDivElement>(null);
  const mapRef = useRef(map);
  mapRef.current = map;

  const handleClickOutside = useCallback((e: MouseEvent) => {
    if (panelRef.current && !panelRef.current.contains(e.target as Node)) setOpen(false);
  }, []);
  useEffect(() => {
    if (!open) return;
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [open, handleClickOutside]);

  if (!map) return null;

  const handleShow = () => {
    setError("");
    setInfo("");
    const m = mapRef.current;
    if (!m || !text.trim()) return;

    let feature: Feature;
    try {
      feature = renderEntityContent(text, m);
    } catch (e: any) {
      setError(`解析失败: ${e.message}`);
      return;
    }

    const geom = feature.geometry;
    let desc = geom.type;
    if (geom.type === "Point") {
      const c = geom.coordinates as Position;
      desc += ` (${c[0].toFixed(4)}, ${c[1].toFixed(4)})`;
    } else if (geom.type === "LineString") {
      desc += ` (${(geom.coordinates as Position[]).length} 个顶点)`;
    } else if (geom.type === "Polygon") {
      desc += ` (${(geom.coordinates as Position[][])[0].length - 1} 个顶点)`;
    }
    setInfo(`✅ 已显示: ${desc}`);
  };

  const handleClear = () => {
    setError(""); setInfo("");
    const m = mapRef.current;
    if (m) clearWktData(m);
  };

  return (
    <div ref={panelRef} className="maplibre-wkt-control">
      <button className="maplibre-wkt-control__btn"
        onClick={() => setOpen((v) => !v)} title="WKT 要素输入">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none"
          stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <polyline points="16 3 21 3 21 8" />
          <line x1="4" y1="20" x2="21" y2="3" />
          <polyline points="21 16 21 21 16 21" />
          <line x1="15" y1="15" x2="21" y2="21" />
          <line x1="4" y1="4" x2="9" y2="9" />
        </svg>
      </button>
      {open && (
        <div className="maplibre-wkt-control__panel">
          <div className="maplibre-wkt-control__title">WKT 要素输入</div>
          <textarea className="maplibre-wkt-control__input" rows={4}
            placeholder={placeholder} value={text}
            onChange={(e) => setText(e.target.value)} />
          {error && <div className="maplibre-wkt-control__error">{error}</div>}
          {info && <div className="maplibre-wkt-control__info">{info}</div>}
          <div className="maplibre-wkt-control__footer">
            <button className="maplibre-wkt-control__show" onClick={handleShow}>显示</button>
            <button className="maplibre-wkt-control__clear" onClick={handleClear}>清除</button>
          </div>
        </div>
      )}
    </div>
  );
}
