/** WktCoord: [lng, lat] or [lng, lat, z] */
type WktCoord = import("geojson").Position;

/** Parse a single coordinate: "30 10" → [30, 10] */
function parseCoord(s: string): WktCoord {
  const parts = s.trim().split(/\s+/).map(Number);
  if (parts.length < 2) throw new Error(`无效坐标: "${s}"`);
  return parts as WktCoord;
}

/** Parse comma-separated coordinate list: "30 10, 10 30, 40 40" → [[30,10],[10,30],[40,40]] */
function parseCoordList(s: string): WktCoord[] {
  return s.split(",").map(parseCoord);
}

/** Parse a ring (parenthesised coord list): "(30 10, 10 30, 40 40, 30 10)" */
function parseRing(s: string): WktCoord[] {
  const m = s.match(/^\s*\((.+)\)\s*$/);
  if (!m) throw new Error(`无效环: "${s}"`);
  return parseCoordList(m[1]);
}

/** Parse one or more rings: "((...))" or "((...), (...))" */
function parseRings(s: string): WktCoord[][] {
  const depth = (s.match(/\(/g) || []).length;
  if (depth === 1) return [parseRing(s.trim())];
  const inner = s.trim().replace(/^\s*\((.+)\)\s*$/, "$1");
  const rings: WktCoord[][] = [];
  let d = 0, start = 0;
  for (let i = 0; i < inner.length; i++) {
    if (inner[i] === "(") { if (d === 0) start = i + 1; d++; }
    else if (inner[i] === ")") { d--; if (d === 0) rings.push(parseCoordList(inner.slice(start, i))); }
  }
  if (rings.length === 0) throw new Error(`无法解析多边形环: "${s}"`);
  return rings;
}

/**
 * Given a WKT string, locate the first '(' and return its matching closing ')'
 * position using depth counting.
 */
function findBody(wkt: string): { type: string; body: string } {
  const s = wkt.trim();
  const typeEnd = s.search(/\s*\(/);
  if (typeEnd < 0) throw new Error("无效 WKT: 缺少几何类型和括号");
  const type = s.slice(0, typeEnd).trim();
  let depth = 0, start = -1;
  for (let i = typeEnd; i < s.length; i++) {
    if (s[i] === "(") {
      if (depth === 0) start = i + 1;
      depth++;
    } else if (s[i] === ")") {
      depth--;
      if (depth === 0) return { type, body: s.slice(start, i) };
    }
  }
  throw new Error("无效 WKT: 括号不匹配");
}

/**
 * Split a WKT body by top-level commas (depth 0) into sub-bodies.
 */
function splitTopLevel(body: string): string[] {
  const parts: string[] = [];
  let depth = 0, start = 0;
  for (let i = 0; i < body.length; i++) {
    if (body[i] === "(") depth++;
    else if (body[i] === ")") depth--;
    else if (body[i] === "," && depth === 0) {
      parts.push(body.slice(start, i).trim());
      start = i + 1;
    }
  }
  if (start < body.length) parts.push(body.slice(start).trim());
  return parts.filter(Boolean);
}

/**
 * Parse a WKT (Well-Known Text) string into a GeoJSON Feature.
 *
 * Supported types: POINT, MULTIPOINT, LINESTRING, MULTILINESTRING,
 * POLYGON, MULTIPOLYGON, GEOMETRYCOLLECTION.
 */
export function parseWkt(wkt: string): import("geojson").Feature {
  const { type, body } = findBody(wkt);
  const typeU = type.toUpperCase();

  switch (typeU) {
    case "POINT":
      return { type: "Feature", properties: {}, geometry: { type: "Point", coordinates: parseCoord(body) } };
    case "MULTIPOINT":
      return { type: "Feature", properties: {}, geometry: { type: "MultiPoint", coordinates: parseCoordList(body) } };
    case "LINESTRING":
      return { type: "Feature", properties: {}, geometry: { type: "LineString", coordinates: parseCoordList(body) } };
    case "MULTILINESTRING": {
      const lines: WktCoord[][] = [];
      for (const frag of splitTopLevel(body)) {
        const inner = frag.replace(/^\s*\((.+)\)\s*$/, "$1").trim();
        lines.push(parseCoordList(inner));
      }
      if (lines.length === 0) throw new Error("MULTILINESTRING 至少需要一条线");
      return { type: "Feature", properties: {}, geometry: { type: "MultiLineString", coordinates: lines } };
    }
    case "POLYGON":
      return { type: "Feature", properties: {}, geometry: { type: "Polygon", coordinates: parseRings(`(${body})`) } };
    case "MULTIPOLYGON": {
      const polys: WktCoord[][][] = [];
      for (const frag of splitTopLevel(body)) polys.push(parseRings(frag));
      if (polys.length === 0) throw new Error("MULTIPOLYGON 至少需要一个面");
      return { type: "Feature", properties: {}, geometry: { type: "MultiPolygon", coordinates: polys } };
    }
    case "GEOMETRYCOLLECTION": {
      const geometries: any[] = [];
      for (const frag of splitTopLevel(body)) geometries.push(parseWkt(frag).geometry);
      return { type: "Feature", properties: {}, geometry: { type: "GeometryCollection", geometries } };
    }
    default:
      throw new Error(`不支持的 WKT 类型: "${type}"`);
  }
}
