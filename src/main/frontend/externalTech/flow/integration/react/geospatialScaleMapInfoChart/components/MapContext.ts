import { createContext, useContext } from "react";
import type { Map } from "maplibre-gl";

/** React Context that holds the maplibre-gl Map instance */
export const MapContext = createContext<Map | null>(null);

/**
 * Hook to access the maplibre-gl Map instance from any descendant component.
 * Returns `null` if called outside a `<MaplibreMap>`.
 */
export function useMap(): Map | null {
  return useContext(MapContext);
}
