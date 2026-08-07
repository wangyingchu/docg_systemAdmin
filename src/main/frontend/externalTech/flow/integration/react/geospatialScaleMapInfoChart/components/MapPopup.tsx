import { useEffect, useRef, type ReactNode } from "react";
import { createPortal } from "react-dom";
import { Popup as MaplibrePopup, type LngLatLike, type PopupOptions } from "maplibre-gl";
import { useMap } from "./MapContext";

export interface PopupProps extends Omit<PopupOptions, "className"> {
  /** Geographical position */
  lngLat: LngLatLike;
  /** Whether the popup is visible (default: true) */
  open?: boolean;
  /** Called when the popup is closed by the user */
  onClose?: () => void;
  /** React children rendered as the popup's HTML content */
  children?: ReactNode;
}

/**
 * React wrapper for a maplibre-gl `Popup`.
 *
 * Uses React Portal to render `children` into the native Popup's
 * DOM container so you can use interactive React elements inside popups.
 *
 * @example
 * ```tsx
 * <MapPopup lngLat={[116.4, 39.9]} open={showPopup} onClose={() => setShowPopup(false)}>
 *   <h3>Hello Beijing!</h3>
 *   <button onClick={doSomething}>Click me</button>
 * </MapPopup>
 * ```
 */
export function MapPopup({
  lngLat,
  open = true,
  onClose,
  children,
  ...popupOptions
}: PopupProps) {
  const map = useMap();
  const popupRef = useRef<MaplibrePopup | null>(null);
  const containerRef = useRef<HTMLDivElement>(document.createElement("div"));

  // Create / remove the native Popup.
  // Defer creation by one microtask so the map's click handler
  // (which dispatches synchronously) finishes before the popup
  // appears, preventing it from being immediately dismissed.
  useEffect(() => {
    if (!map || !open) return;

    let cancelled = false;
    const popup = new MaplibrePopup(popupOptions)
      .setLngLat(lngLat)
      .setDOMContent(containerRef.current);

    // Delay addTo so popup appears *after* the current click dispatch
    const raf = requestAnimationFrame(() => {
      if (cancelled) return;
      popup.addTo(map);
      popupRef.current = popup;
      if (onClose) popup.on("close", onClose);
    });

    return () => {
      cancelled = true;
      cancelAnimationFrame(raf);
      popup.remove();
      popupRef.current = null;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [map, open, JSON.stringify(lngLat)]);

  // Sync position
  useEffect(() => {
    if (open) {
      popupRef.current?.setLngLat(lngLat);
    }
  }, [lngLat, open]);

  if (!open) return null;

  // Portal React children into the native Popup's DOM container
  return createPortal(children, containerRef.current);
}

export default MapPopup;
