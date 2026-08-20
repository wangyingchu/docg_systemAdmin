import { useState, useEffect, useCallback, useRef, useMemo } from 'react';
import { InteractiveNvlWrapper } from '@neo4j-nvl/react';
import type { MouseEventCallbacks } from '@neo4j-nvl/react';
import type { Node, Relationship, HitTargets } from '@neo4j-nvl/base';
import NVL from '@neo4j-nvl/base';
import { fetchInitialGraph, expandNode } from './api';
import type { NvlNode, NvlRel } from './api';
import { randomHslColor } from './api';
// @ts-ignore
import './NvlGraphView.css';

/* ---- 常量 ---- */
const BASE_SIZE = 28;
const SELECTED_SIZE = 36;
const BASE_REL_WIDTH = 1;
const SELECTED_REL_WIDTH = 3;
const BASE_CAPTION_SIZE = 1;
const SELECTED_CAPTION_SIZE = 3;

/* ---- 示例模板 ---- */
function buildDefaultExample(): string {
  const example = {
    nodes: [
      { id: '1', caption: 'Alice', labels: ['Person'] },
      { id: '2', caption: 'Bob', labels: ['Person'] },
      { id: '3', caption: 'Charlie', labels: ['Person'] },
      { id: '4', caption: 'Neo4j', labels: ['Company'] },
      { id: '5', caption: 'Graph DB', labels: ['Project'] },
      { id: '6', caption: 'Python', labels: ['Skill'] },
      { id: '7', caption: 'London', labels: ['City'] },
    ],
    rels: [
      { id: 'r1', from: '1', to: '2', caption: 'KNOWS' },
      { id: 'r2', from: '2', to: '3', caption: 'KNOWS' },
      { id: 'r3', from: '1', to: '4', caption: 'WORKS_AT' },
      { id: 'r4', from: '3', to: '4', caption: 'WORKS_AT' },
      { id: 'r5', from: '1', to: '5', caption: 'MANAGES' },
      { id: 'r6', from: '4', to: '5', caption: 'OWNS' },
      { id: 'r7', from: '1', to: '6', caption: 'HAS_SKILL' },
      { id: 'r8', from: '3', to: '6', caption: 'HAS_SKILL' },
      { id: 'r9', from: '1', to: '7', caption: 'LIVES_IN' },
    ],
  };
  return JSON.stringify(example, null, 2);
}

/* ================================================================
   NvlGraphView — 核心 React 组件
   ================================================================ */
// @ts-ignore
export function NvlGraphView(props) {
  const [nodes, setNodes] = useState<Node[]>([]);
  const [rels, setRels] = useState<Relationship[]>([]);
  const [loading, setLoading] = useState(true);
  const [dataSource, setDataSource] = useState<'mock' | 'custom'>('custom');
  const [expandingId, setExpandingId] = useState<string | null>(null);
  const [initErr, setInitErr] = useState<string | null>(null);
  const [dragging, setDragging] = useState(false);

  // 单击选中
  const [selectedNodeId, setSelectedNodeId] = useState<string | null>(null);
  const [selectedRelId, setSelectedRelId] = useState<string | null>(null);

  // 自定义数据输入
  const [showCustomInput, setShowCustomInput] = useState(false);
  const [customDataText, setCustomDataText] = useState('');
  const [customDataError, setCustomDataError] = useState<string | null>(null);

  const existingIdsRef = useRef<Set<string>>(new Set());
  const expandingRef = useRef<string | null>(null);
  const [minimapEl, setMinimapEl] = useState<HTMLDivElement | null>(null);

  const nvlRef = useRef<NVL | null>(null);

  const [graphKey, setGraphKey] = useState(0);

  /* ---- 加载初始图 ---- */
  const loadInitialGraph = useCallback(async () => {
    setLoading(true);
    setInitErr(null);
    setSelectedNodeId(null);
    setSelectedRelId(null);
    expandingRef.current = null;
    setExpandingId(null);

    //using generateMockData from api.ts
    //const result = await fetchInitialGraph();
    //Using real data from server side query result
    const result = props.graphData;

    const nvlNodes: Node[] = result.nodes.map((n: NvlNode) => ({
      id: n.id, caption: n.caption+": "+n.id, color: n.color, size: BASE_SIZE,
    }));
    const nvlRels: Relationship[] = result.rels.map((r: NvlRel) => ({
      id: r.id, from: r.from, to: r.to, caption: r.caption+": "+r.id,
    }));

    existingIdsRef.current.clear();
    nvlNodes.forEach((n) => existingIdsRef.current.add(n.id));

    setNodes(nvlNodes);
    setRels(nvlRels);
    //setDataSource('mock');
    setGraphKey((k) => k + 1);
    setLoading(false);
  }, []);

  /* ---- 刷新 ---- */
  const handleRefresh = useCallback(() => { loadInitialGraph(); }, [loadInitialGraph]);

  /* ---- 自定义数据 ---- */
  const handleOpenCustomInput = useCallback(() => {
    setCustomDataText(buildDefaultExample());
    setCustomDataError(null);
    setShowCustomInput(true);
  }, []);

  const handleLoadCustomData = useCallback(() => {
    setCustomDataError(null);
    let parsed: { nodes?: unknown[]; rels?: unknown[] };
    try { parsed = JSON.parse(customDataText); }
    catch { setCustomDataError('JSON 格式无效，请检查语法'); return; }

    if (!Array.isArray(parsed.nodes) || parsed.nodes.length === 0) {
      setCustomDataError('缺少 nodes 数组或为空'); return;
    }
    if (!Array.isArray(parsed.rels)) {
      setCustomDataError('缺少 rels 数组'); return;
    }

    const existingIds = new Set<string>();
    const nvlNodes: Node[] = [];
    for (let i = 0; i < parsed.nodes.length; i++) {
      const n = parsed.nodes[i];
      if (!n || typeof n !== 'object') { setCustomDataError(`nodes[${i}] 不是有效对象`); return; }
      const id = String((n as any).id ?? '');
      const caption = String((n as any).caption ?? id);
      if (!id) { setCustomDataError(`nodes[${i}] 缺少 id`); return; }
      if (existingIds.has(id)) { setCustomDataError(`节点 id "${id}" 重复`); return; }
      existingIds.add(id);
      nvlNodes.push({
        id, caption,
        color: (n as any).color || randomHslColor(),
        size: BASE_SIZE,
      });
    }

    const nvlRels: Relationship[] = [];
    for (let i = 0; i < parsed.rels.length; i++) {
      const r = parsed.rels[i];
      if (!r || typeof r !== 'object') { setCustomDataError(`rels[${i}] 不是有效对象`); return; }
      const id = String((r as any).id ?? '');
      const from = String((r as any).from ?? '');
      const to = String((r as any).to ?? '');
      const caption = String((r as any).caption ?? '');
      if (!id) { setCustomDataError(`rels[${i}] 缺少 id`); return; }
      if (!from || !to) { setCustomDataError(`rels[${i}] 缺少 from/to`); return; }
      if (!existingIds.has(from)) { setCustomDataError(`rels[${i}].from "${from}" 引用了不存在的节点`); return; }
      if (!existingIds.has(to)) { setCustomDataError(`rels[${i}].to "${to}" 引用了不存在的节点`); return; }
      nvlRels.push({ id, from, to, caption });
    }

    setSelectedNodeId(null);
    setSelectedRelId(null);
    expandingRef.current = null;
    setExpandingId(null);
    setDataSource('custom');
    existingIdsRef.current = new Set(existingIds);
    setNodes(nvlNodes);
    setRels(nvlRels);
    setGraphKey((k) => k + 1);
    setShowCustomInput(false);
  }, [customDataText]);

  /* ---- 首次 fit-to-view ---- */
  const hasFitRef = useRef(false);
  useEffect(() => { hasFitRef.current = false; }, [graphKey]);

  const minimapWasReady = useRef(false);
  useEffect(() => {
    if (minimapEl && !minimapWasReady.current) { minimapWasReady.current = true; setGraphKey((k) => k + 1); }
  }, [minimapEl]);

  useEffect(() => {loadInitialGraph();}, []); // eslint-disable-line react-hooks/exhaustive-deps

  /* ---- 双击展开 ---- */
  const handleNodeDoubleClick = useCallback(async (node: Node, _hit: HitTargets, _evt: MouseEvent) => {
    if (dragging) return;
    const nodeId = node.id;
    if (expandingRef.current === nodeId) return;
    expandingRef.current = nodeId;
    setExpandingId(nodeId);
    try {
      const result = await expandNode(nodeId, existingIdsRef.current, 10);
      if (result.nodes.length === 0) { expandingRef.current = null; setExpandingId(null); return; }
      setNodes((prev) => {
        const newColor = randomHslColor();
        const newNodes: Node[] = result.nodes.map((n) => {
          existingIdsRef.current.add(n.id);
          return { id: n.id, caption: n.caption, color: newColor, size: BASE_SIZE };
        });
        return [...prev, ...newNodes];
      });
      setRels((prev) => [
        ...prev,
        ...result.rels.map((r) => ({ id: r.id, from: r.from, to: r.to, caption: r.caption })),
      ]);
    } catch (e) { console.error(e); }
    finally { expandingRef.current = null; setExpandingId(null); }
  }, [dragging]);

  /* ---- 单击选中 ---- */
  const handleNodeClick = useCallback((node: Node) => {
    setSelectedNodeId((prev) => prev === node.id ? null : node.id);
    setSelectedRelId(null);
  }, []);

  const handleRelationshipClick = useCallback((rel: Relationship) => {
    setSelectedRelId((prev) => prev === rel.id ? null : rel.id);
    setSelectedNodeId(null);
  }, []);

  const handleCanvasClick = useCallback(() => {
    setSelectedNodeId(null);
    setSelectedRelId(null);
  }, []);

  /* ---- 激活节点（选中节点的邻居 + 选中边的两端） ---- */
  const activatedNodeIds = useMemo(() => {
    const ids = new Set<string>();
    if (selectedNodeId) {
      rels.forEach((r) => {
        if (r.from === selectedNodeId) ids.add(r.to);
        if (r.to === selectedNodeId) ids.add(r.from);
      });
    }
    if (selectedRelId) {
      const rel = rels.find((r) => r.id === selectedRelId);
      if (rel) { ids.add(rel.from); ids.add(rel.to); }
    }
    return ids;
  }, [selectedNodeId, selectedRelId, rels]);

  /* ---- 派生显示数据 ---- */
  const displayNodes = useMemo(() => {
    return nodes.map((n) => {
      if (n.id === selectedNodeId) return { ...n, selected: true, size: SELECTED_SIZE };
      if (activatedNodeIds.has(n.id)) return { ...n, selected: false, activated: true, size: BASE_SIZE };
      return { ...n, selected: false, activated: false, size: BASE_SIZE };
    });
  }, [nodes, selectedNodeId, activatedNodeIds]);

  const displayRels = useMemo(() => {
    return rels.map((r) => {
      const isSelected = r.id === selectedRelId;
      const isNeighbor = selectedNodeId !== null && (r.from === selectedNodeId || r.to === selectedNodeId);
      return {
        ...r,
        selected: isSelected,
        width: isSelected ? SELECTED_REL_WIDTH : isNeighbor ? BASE_REL_WIDTH + 1 : BASE_REL_WIDTH,
        captionSize: isSelected ? SELECTED_CAPTION_SIZE : BASE_CAPTION_SIZE,
        color: isNeighbor ? '#88aacc' : (r as any).color ?? undefined,
      };
    });
  }, [rels, selectedRelId, selectedNodeId]);

  /* ---- NVL 配置 ---- */
  const mouseCallbacks: MouseEventCallbacks = {
    onNodeClick: handleNodeClick,
    onNodeDoubleClick: handleNodeDoubleClick,
    onRelationshipClick: handleRelationshipClick,
    onCanvasClick: handleCanvasClick,
    onDragStart: () => setDragging(true),
    onDragEnd: () => setDragging(false),
    onZoom: true,
    onPan: true,
  };

  const nvlOpts: Record<string, unknown> = {
    initialZoom: 1.2,
    renderer: 'canvas',
    styling: {
      backgroundColor: '#ffffff',
      defaultRelationshipColor: '#556677',
      minimapViewportBoxColor: '#3366cc',
      selectedBorderColor: '#FFD700',
      selectedInnerBorderColor: '#FF6B00',
      nodeDefaultBorderColor: 'rgba(0,0,0,0.12)',
    },
  };
  if (minimapEl) nvlOpts.minimapContainer = minimapEl;

  const nvlCallbacks = useMemo(() => ({
    onLayoutDone: () => {
      if (hasFitRef.current) return;
      const nvl = nvlRef.current;
      if (!nvl) return;
      const allNodes = nvl.getNodes();
      if (!allNodes) return;
      if (allNodes.length === 0) return;
      hasFitRef.current = true;
      // @ts-ignore
      nvl.fit(allNodes.map((n) => n.id));
    },
  }), []);

  /* ---- 渲染 ---- */
  if (loading) return <div className="nvl-loading"><div className="nvl-spinner" />加载中...</div>;
  if (initErr) return <div className="nvl-error"><div>NVL 初始化失败</div><div className="nvl-error-detail">{initErr}</div></div>;

  const dataSourceLabel = dataSource === 'custom' ? '自定义数据' : '模拟数据';

  /* {dataSourceLabel} | 节点: {nodes.length} | 关系: {rels.length} */
  /* <button className="nvl-btn" onClick={handleOpenCustomInput} disabled={loading} title="加载自定义图数据">✎ 自定义</button> */
  return (
    <div className="nvl-container">
      {/* 状态栏 */}
      <div className="nvl-status-bar">
        节点: {nodes.length} | 关系: {rels.length}
        {selectedNodeId && ` | 已选中: ${selectedNodeId}`}
        {dragging && ' | 拖拽中'}{expandingId && ' | 展开中...'}
        <span className="nvl-hint">单击选中 · 双击展开 · 空白取消</span>
      </div>

      {/* 小地图 */}
      <div ref={setMinimapEl} className="nvl-minimap" />

      {/* 工具栏 */}
      <div className="nvl-toolbar">
        <button className="nvl-btn" onClick={handleRefresh} disabled={loading} title="刷新图谱数据">⟳ </button>
      </div>

      {/* NVL 画布 */}
      <InteractiveNvlWrapper
        key={graphKey}
        ref={nvlRef}
        nodes={displayNodes}
        rels={displayRels}
        nvlOptions={nvlOpts}
        nvlCallbacks={nvlCallbacks}
        mouseEventCallbacks={mouseCallbacks}
        onInitializationError={(e) => setInitErr(String(e))}
      />

      {/* 自定义数据模态框 */}
      {showCustomInput && (
        <div className="nvl-modal-backdrop" onClick={() => setShowCustomInput(false)}>
          <div className="nvl-modal" onClick={(e) => e.stopPropagation()}>
            <div className="nvl-modal-header">
              <span>自定义图数据</span>
              <button className="nvl-modal-close" onClick={() => setShowCustomInput(false)}>✕</button>
            </div>
            <div className="nvl-modal-body">
              <textarea
                className="nvl-textarea"
                value={customDataText}
                onChange={(e) => { setCustomDataText(e.target.value); setCustomDataError(null); }}
                placeholder='{"nodes":[...],"rels":[...]}'
                spellCheck={false}
              />
              {customDataError && <div className="nvl-custom-error">{customDataError}</div>}
            </div>
            <div className="nvl-modal-footer">
              <button className="nvl-modal-btn nvl-modal-btn-secondary" onClick={() => setCustomDataText(buildDefaultExample())}>加载示例</button>
              <div className="nvl-modal-footer-right">
                <button className="nvl-modal-btn nvl-modal-btn-secondary" onClick={() => setShowCustomInput(false)}>取消</button>
                <button className="nvl-modal-btn nvl-modal-btn-primary" onClick={handleLoadCustomData}>加载</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
