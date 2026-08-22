/**
 * 图数据模块
 *
 * 全部使用模拟数据生成，不依赖 Neo4j 数据库。
 * 提供：初始随机图 + 双击展开邻居 + HSL 颜色工具。
 */

// ============================================================
// 类型定义
// ============================================================

/** NVL 节点 */
export interface NvlNode {
  id: string;
  caption: string;
  color: string;
  labels: string[];
}

/** NVL 关系 */
export interface NvlRel {
  id: string;
  from: string;
  to: string;
  caption: string;
}

/** 查询结果 */
export interface QueryResult {
  nodes: NvlNode[];
  rels: NvlRel[];
}

/** Java 侧生成的扩展节点（与 NvlGraphComponent.GraphNode 对应） */
export interface JavaGraphNode {
  id: string;
  caption: string;
  color: string;
}

/** Java 侧生成的扩展关系（与 NvlGraphComponent.GraphRel 对应） */
export interface JavaGraphRel {
  id: string;
  from: string;
  to: string;
  caption: string;
}

/** React → Java：双击节点扩展请求（字段名避免使用 nodeId，Flow 会将其误判为 StateNode 引用） */
export interface ExpandRequest {
  clickedNodeId: string;
  requestId: string;
}

/** Java → React：双击节点扩展结果 */
export interface ExpandResult {
  requestId: string;
  nodes: JavaGraphNode[];
  rels: JavaGraphRel[];
}

// ============================================================
// 工具函数
// ============================================================

export function randomHslColor(): string {
  const h = Math.floor(Math.random() * 360);
  const s = 55 + Math.floor(Math.random() * 25);
  const l = 42 + Math.floor(Math.random() * 16);
  const rgb = hslToRgb(h / 360, s / 100, l / 100);
  return `#${rgb.r.toString(16).padStart(2, '0')}${rgb.g.toString(16).padStart(2, '0')}${rgb.b.toString(16).padStart(2, '0')}`;
}

function hslToRgb(h: number, s: number, l: number): { r: number; g: number; b: number } {
  let r: number, g: number, b: number;
  if (s === 0) {
    r = g = b = l;
  } else {
    const hue2rgb = (p: number, q: number, t: number) => {
      if (t < 0) t += 1;
      if (t > 1) t -= 1;
      if (t < 1 / 6) return p + (q - p) * 6 * t;
      if (t < 1 / 2) return q;
      if (t < 2 / 3) return p + (q - p) * (2 / 3 - t) * 6;
      return p;
    };
    const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
    const p = 2 * l - q;
    r = hue2rgb(p, q, h + 1 / 3);
    g = hue2rgb(p, q, h);
    b = hue2rgb(p, q, h - 1 / 3);
  }
  return {
    r: Math.round(r * 255),
    g: Math.round(g * 255),
    b: Math.round(b * 255),
  };
}

// ============================================================
// 模拟数据
// ============================================================

function generateMockData(): QueryResult {
  const mockLabels = ['Person', 'Company', 'Project', 'Skill', 'City'];
  const mockRelTypes = ['KNOWS', 'WORKS_AT', 'LIVES_IN', 'HAS_SKILL', 'MANAGES', 'OWNS'];

  const nodes: NvlNode[] = [];
  const rels: NvlRel[] = [];

  for (let i = 0; i < 20; i++) {
    const label = mockLabels[Math.floor(Math.random() * mockLabels.length)];
    nodes.push({
      id: `mock-node-${i}`,
      caption: `${label}_${i}`,
      color: randomHslColor(),
      labels: [label],
    });
  }

  const usedPairs = new Set<string>();
  for (let i = 0; i < 15; i++) {
    const fromIdx = Math.floor(Math.random() * nodes.length);
    let toIdx = Math.floor(Math.random() * nodes.length);
    while (toIdx === fromIdx) {
      toIdx = Math.floor(Math.random() * nodes.length);
    }
    const pairKey = `${fromIdx}-${toIdx}`;
    if (usedPairs.has(pairKey)) continue;
    usedPairs.add(pairKey);

    rels.push({
      id: `mock-rel-${i}`,
      from: nodes[fromIdx].id,
      to: nodes[toIdx].id,
      caption: mockRelTypes[Math.floor(Math.random() * mockRelTypes.length)],
    });
  }

  return { nodes, rels };
}

// ============================================================
// 公开 API
// ============================================================

/** 获取初始图数据（模拟数据） */
export async function fetchInitialGraph(): Promise<QueryResult> {
  return generateMockData();
}
