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

function generateMockExpand(clickedNodeId: string): QueryResult {
  const mockLabels = ['Person', 'Company', 'Project', 'Skill', 'City'];
  const mockRelTypes = ['KNOWS', 'WORKS_AT', 'LIVES_IN', 'HAS_SKILL', 'MANAGES', 'OWNS'];

  const newNodes: NvlNode[] = [];
  const newRels: NvlRel[] = [];

  const count = Math.min(10, 6 + Math.floor(Math.random() * 5));

  for (let i = 0; i < count; i++) {
    const newNodeId = `mock-expand-${Date.now()}-${i}`;
    const label = mockLabels[Math.floor(Math.random() * mockLabels.length)];
    newNodes.push({
      id: newNodeId,
      caption: `${label}_exp${i}`,
      color: randomHslColor(),
      labels: [label],
    });
    newRels.push({
      id: `mock-rel-expand-${Date.now()}-${i}`,
      from: clickedNodeId,
      to: newNodeId,
      caption: mockRelTypes[Math.floor(Math.random() * mockRelTypes.length)],
    });
  }

  return { nodes: newNodes, rels: newRels };
}

// ============================================================
// 公开 API
// ============================================================

/** 获取初始图数据（模拟数据） */
export async function fetchInitialGraph(): Promise<QueryResult> {
  return generateMockData();
}

/**
 * 展开节点的 1 度邻居（模拟数据）
 * @param nodeId  被点击节点的 id
 * @param _existingIds 当前图中已有的所有节点 ID 集合
 * @param _limit   返回上限
 */
export async function expandNode(
  nodeId: string,
  _existingIds: Set<string>,
  _limit: number = 10
): Promise<QueryResult> {
  return generateMockExpand(nodeId);
}
