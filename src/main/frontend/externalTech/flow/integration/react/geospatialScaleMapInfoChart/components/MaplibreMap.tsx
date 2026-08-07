import {
  useRef,
  useState,
  useEffect,
  forwardRef,
  useImperativeHandle,
  type ReactNode,
  type CSSProperties,
} from "react";
import { Map, type MapOptions, setWorkerUrl } from "maplibre-gl";
// @ts-ignore
import "maplibre-gl/dist/maplibre-gl.css";
import { MapContext } from "./MapContext";

setWorkerUrl("/VAADIN/externalTech/flow/integration/react/geospatialScaleMapInfoChart/maplibre-gl-worker.mjs");

export type { Map, MapOptions } from "maplibre-gl";

// ---- Props ----

export interface MaplibreMapProps extends Omit<MapOptions, "container"> {
  /** Optional CSS class name applied to the container div */
  className?: string;
  /**
   * Optional inline CSS styles applied to the container div.
   * Not to be confused with the `style` prop that defines the map's style.
   */
  containerStyle?: CSSProperties;
  /** Fires once after the map has finished loading and the first render completes */
  onLoad?: (map: Map) => void;
  /** Fires after the map's camera has been moved by any means */
  onMove?: (map: Map) => void;
  /** Fires just before the map begins a transition (pan, zoom, rotate, pitch) */
  onMoveStart?: (map: Map) => void;
  /** Fires just after the map completes a transition */
  onMoveEnd?: (map: Map) => void;
  /** Child nodes rendered inside the MapContext provider (e.g. <Marker>, <Popup>) */
  children?: ReactNode;
}

// ---- Ref handle ----

export interface MaplibreMapRef {
  /** The underlying maplibre-gl Map instance, or null if not yet ready */
  getMap: () => Map | null;
}

// ---- Component ----

/**
 * Core React wrapper around maplibre-gl's `Map`.
 *
 * The `style` prop carries the maplibre style URL or spec object (from MapOptions).
 * Use `containerStyle` for CSS styling of the wrapper `<div>`.
 *
 * @example
 * ```tsx
 * <MaplibreMap
 *   containerStyle={{ width: "100%", height: "100vh" }}
 *   style="https://basemaps.cartocdn.com/gl/positron-gl-style/style.json"
 *   center={[116.4074, 39.9042]}
 *   zoom={10}
 *   onLoad={(map) => console.log("Map ready!")}
 * >
 *   <NavigationControl />
 *   <MapMarker lngLat={[116.4074, 39.9042]} color="red" />
 * </MaplibreMap>
 * ```
 */
export const MaplibreMap = forwardRef<MaplibreMapRef, MaplibreMapProps>(
  function MaplibreMap(props, ref) {
    const {
      className,
      containerStyle,
      onLoad,
      onMove,
      onMoveStart,
      onMoveEnd,
      children,
      // Everything else goes into native MapOptions
      ...mapOptions
    } = props;

    const containerRef = useRef<HTMLDivElement>(null);
    const mapRef = useRef<Map | null>(null);
    const [ready, setReady] = useState(false);
    /** Tracks the last style we passed to the map, so we can skip no-op updates */
    const lastStyleRef = useRef<MapOptions["style"]>(undefined);
    /** Track previous camera values to avoid unnecessary jumpTo calls */
    const prevCenterRef = useRef<string>("");
    const prevZoomRef = useRef<number | undefined>(undefined);
    const prevBearingRef = useRef<number | undefined>(undefined);
    const prevPitchRef = useRef<number | undefined>(undefined);

    // Expose map instance to parent via ref
    useImperativeHandle(ref, () => ({
      getMap: () => mapRef.current,
    }));

    // ---- Create map (one-time) ----
    useEffect(() => {
      const container = containerRef.current;
      if (!container) return;

      const map = new Map({
        ...mapOptions,
        container,
      } as any);

      mapRef.current = map;
      // Memorise the style we just passed in so the sync effect won't re-apply it
      lastStyleRef.current = mapOptions.style;
      // Memorise initial camera values so the sync effect skips the first ready
      prevCenterRef.current = JSON.stringify(mapOptions.center ?? null);
      prevZoomRef.current = mapOptions.zoom;
      prevBearingRef.current = mapOptions.bearing;
      prevPitchRef.current = mapOptions.pitch;

      // Register event listeners
      const onLoadHandler = () => {
        setReady(true);
        onLoad?.(map);
      };
      map.on("load", onLoadHandler);

      // Collapse attribution once the map is fully loaded.
      // We let it load expanded (so content populates), then click the
      // summary button once to collapse it. After that, user clicks
      // toggle expand/collapse normally.
      map.once("idle", () => {
        const summary = map
          .getContainer()
          .querySelector<HTMLElement>(".maplibregl-ctrl-attrib summary");
        summary?.click();
      });

      if (onMove) map.on("move", () => onMove(map));
      if (onMoveStart) map.on("movestart", () => onMoveStart(map));
      if (onMoveEnd) map.on("moveend", () => onMoveEnd(map));

      return () => {
        map.off("load", onLoadHandler);
        map.remove();
        mapRef.current = null;
        setReady(false);
      };
      // One-time creation on mount
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    // ---- Keep map in sync with style changes ----
    useEffect(() => {
      const map = mapRef.current;
      if (!map || !ready) return;

      const nextStyle = mapOptions.style;
      if (nextStyle === undefined || nextStyle === lastStyleRef.current) return;

      // Record before calling setStyle so we don't loop
      lastStyleRef.current = nextStyle;
      map.setStyle(nextStyle as Parameters<typeof map.setStyle>[0]);
    }, [mapOptions.style, ready]);

    // ---- Keep camera in sync (only on prop change, NOT on initial ready) ----
    useEffect(() => {
      const map = mapRef.current;
      if (!map || !ready) return;

      const centerKey = JSON.stringify(mapOptions.center ?? null);
      const changed =
        centerKey !== prevCenterRef.current ||
        mapOptions.zoom !== prevZoomRef.current ||
        mapOptions.bearing !== prevBearingRef.current ||
        mapOptions.pitch !== prevPitchRef.current;

      if (!changed) return;

      prevCenterRef.current = centerKey;
      prevZoomRef.current = mapOptions.zoom;
      prevBearingRef.current = mapOptions.bearing;
      prevPitchRef.current = mapOptions.pitch;

      if (mapOptions.center !== undefined) {
        const opts: Parameters<typeof map.jumpTo>[0] = {
          center: mapOptions.center,
        };
        if (mapOptions.zoom !== undefined) opts.zoom = mapOptions.zoom;
        if (mapOptions.bearing !== undefined) opts.bearing = mapOptions.bearing;
        if (mapOptions.pitch !== undefined) opts.pitch = mapOptions.pitch;
        map.jumpTo(opts);
      }
    }, [
      // eslint-disable-next-line react-hooks/exhaustive-deps
      JSON.stringify(mapOptions.center),
      mapOptions.zoom,
      mapOptions.bearing,
      mapOptions.pitch,
      ready,
    ]);

    return (
      <div
        ref={containerRef}
        className={className}
        style={{ width: "100%", height: "100%", ...containerStyle }}
      >
        {ready && (
          <MapContext.Provider value={mapRef.current}>
            {children}
          </MapContext.Provider>
        )}
      </div>
    );
  }
);
