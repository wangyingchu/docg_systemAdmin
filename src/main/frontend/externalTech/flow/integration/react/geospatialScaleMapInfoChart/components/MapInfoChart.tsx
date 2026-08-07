import { useState, useCallback, forwardRef, useImperativeHandle, useRef, type CSSProperties } from "react";
import {
  MaplibreMap,
  NavigationControl,
  ScaleControl,
  FullscreenControl,
  ResetViewButton,
  TerrainControl,
  StyleSwitcher,
  ExportControl,
  ToggleUIButton,
  type StyleOption,
} from "./index";
import type { MaplibreMapRef } from "./MaplibreMap";
import type { Map, StyleSpecification } from "maplibre-gl";

// OpenFreeMap styles
const BASE = "https://tiles.openfreemap.org/styles";
const STYLES: StyleOption[] = [
  { label: "标准", style: `${BASE}/liberty` },
  { label: "明亮", style: `${BASE}/bright` },
  { label: "浅色", style: `${BASE}/positron` },
  { label: "暗色", style: `${BASE}/dark` },
];

export interface MapInfoChartProps {
  containerStyle?: CSSProperties;
  /** Default center */
  center?: [number, number];
  /** Default zoom */
  zoom?: number;
  /** Called when view state changes */
  onViewStateChange?: (center: [number, number], zoom: number) => void;
  /** Called when the underlying map instance is ready */
  onMapReady?: (map: Map) => void;
}

export interface MapInfoChartRef {
  getMap: () => Map | null;
}

/**
 * Encapsulates the full map area: MaplibreMap, all overlay controls,
 * style switcher, export, GIS toggle, and WKT rendering.
 *
 * Exposes the maplibre-gl Map instance via ref so parent components
 * can call imperative functions (e.g. renderEntityContent).
 */
export const MapInfoChart = forwardRef<MapInfoChartRef, MapInfoChartProps>(
  function MapInfoChart(
    { containerStyle, center = [116.4074, 39.9042], zoom = 11, onViewStateChange, onMapReady },
    ref
  ) {
    const [mapStyle, setMapStyle] = useState<string | StyleSpecification>(STYLES[0].style);
    const [showGIS, setShowGIS] = useState(true);
    const maplibreRef = useRef<MaplibreMapRef>(null);

    useImperativeHandle(ref, () => ({
      getMap: () => maplibreRef.current?.getMap() ?? null,
    }));

    const handleLoad = useCallback(
      (map: Map) => {
        if (onMapReady) onMapReady(map);
      },
      [onMapReady]
    );

    const handleMoveEnd = useCallback(
      (map: Map) => {
        if (onViewStateChange) {
          const c = map.getCenter();
          onViewStateChange([c.lng, c.lat], map.getZoom());
        }
      },
      [onViewStateChange]
    );

    return (
      <div style={{ width: "100%", height: "100%", position: "relative", ...containerStyle }}>
        <MaplibreMap
          ref={maplibreRef}
          containerStyle={{ width: "100%", height: "100%" }}
          style={mapStyle}
          center={center}
          zoom={zoom}
          canvasContextAttributes={{ preserveDrawingBuffer: true }}
          onLoad={handleLoad}
          onMoveEnd={handleMoveEnd}
        >
          {/* Controls */}
          <NavigationControl position="top-left" />
          <FullscreenControl position="top-left" />
          <TerrainControl />
          <ScaleControl position="bottom-left" />
          <ResetViewButton center={center} zoom={zoom} />

          <StyleSwitcher
            options={STYLES}
            activeStyle={mapStyle}
            onChange={setMapStyle}
          />
          <ExportControl fileName="beijing-map" />
          <ToggleUIButton
            visible={showGIS}
            onToggle={() => setShowGIS((v) => !v)}
          />
        </MaplibreMap>
      </div>
    );
  }
);

export default MapInfoChart;
