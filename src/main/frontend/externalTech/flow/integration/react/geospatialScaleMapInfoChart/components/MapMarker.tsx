import { useState, useEffect, useRef, type ReactNode, type CSSProperties } from "react";
import { Marker as MaplibreMarker, type LngLatLike, type MarkerOptions as _MarkerOptions } from "maplibre-gl";
import { useMap } from "./MapContext";

export interface MarkerProps {
  /** Geographical coordinates [lng, lat] */
  lngLat: LngLatLike;
  /** Optional CSS class */
  className?: string;
  /** Optional inline style for the marker wrapper */
  style?: CSSProperties;
  /** Optional color — creates a simple default colored circle when no children */
  color?: string;
  /** Popup offset, see maplibre MarkerOptions */
  offset?: [number, number];
  /** If true the marker can be dragged */
  draggable?: boolean;
  /** Called when the user finishes dragging */
  onDragEnd?: (lngLat: LngLatLike) => void;
  /** Called when the marker is clicked */
  onClick?: (lngLat: LngLatLike) => void;
  /** Custom React children rendered inside the marker */
  children?: ReactNode;
}

/**
 * Drop-in React wrapper for a maplibre-gl `Marker`.
 *
 * When no `children` are provided it renders a simple coloured circle.
 */
export function MapMarker({
  lngLat,
  className,
  style,
  color = "#3b82f6",
  offset,
  draggable,
  onDragEnd,
  onClick,
  children,
}: MarkerProps) {
  const map = useMap();
  const markerRef = useRef<MaplibreMarker | null>(null);
  const [el] = useState(() => document.createElement("div"));

  // Style the DOM element
  useEffect(() => {
    el.className = className ?? "";
    el.setAttribute("data-marker", "true");

    if (!children) {
      // Default marker appearance
      Object.assign(el.style, {
        width: "20px",
        height: "20px",
        borderRadius: "50%",
        backgroundColor: color,
        border: "3px solid #fff",
        boxShadow: "0 1px 4px rgba(0,0,0,0.3)",
        cursor: draggable ? "grab" : "pointer",
        ...style,
      });
    } else {
      Object.assign(el.style, style ?? {});
    }
  }, [el, className, color, draggable, style, children]);

  // Create / update the Marker
  useEffect(() => {
    if (!map) return;

    const marker = new MaplibreMarker({
      element: el,
      offset: offset as Parameters<typeof MaplibreMarker.prototype.setOffset>[0],
      draggable,
    })
      .setLngLat(lngLat)
      .addTo(map);

    markerRef.current = marker;

    if (draggable && onDragEnd) {
      marker.on("dragend", () => {
        onDragEnd(marker.getLngLat());
      });
    }

    if (onClick) {
      marker.on("click", () => {
        onClick(marker.getLngLat());
      });
    }

    return () => {
      marker.remove();
      markerRef.current = null;
    };
    // Only recreate when lngLat or draggable change meaningfully
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [map, JSON.stringify(lngLat), draggable]);

  // Sync position
  useEffect(() => {
    markerRef.current?.setLngLat(lngLat);
  }, [lngLat]);

  return null; // Rendered via Maplibre GL, nothing in React tree
}

export default MapMarker;
