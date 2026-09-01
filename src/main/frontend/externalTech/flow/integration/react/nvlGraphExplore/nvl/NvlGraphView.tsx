import { useState, useEffect, useCallback, useRef, useMemo } from 'react';
import { InteractiveNvlWrapper } from '@neo4j-nvl/react';
import type { MouseEventCallbacks } from '@neo4j-nvl/react';
import type { Node, Relationship, HitTargets } from '@neo4j-nvl/base';
import NVL from '@neo4j-nvl/base';
import { fetchInitialGraph } from './api';
import type { NvlNode, NvlRel, ExpandRequest, ExpandResult } from './api';
import { randomHslColor } from './api';
import type { RenderHooks } from 'Frontend/generated/flow/ReactAdapter';
// @ts-ignore
import './NvlGraphView.css';

/* ---- 常量 ---- */
const BASE_SIZE = 28;
const SELECTED_SIZE = 28;
const BASE_REL_WIDTH = 1;
const SELECTED_REL_WIDTH = 3;
const BASE_CAPTION_SIZE = 1;
const SELECTED_CAPTION_SIZE = 3;
const EXPAND_SIZE = 18;

type ContextMenuState = {
  x: number;
  y: number;
  kind: 'node' | 'relationship';
  id: string;
  caption: string;
};

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
/** 由 ReactAdapterElement 注入的 hooks，用于与 Java 侧双向通信
export interface NvlGraphViewProps {
  adapterHooks: RenderHooks;
}
*/

// @ts-ignore
export function NvlGraphView(props) {
  const [nodes, setNodes] = useState<Node[]>([]);
  const [rels, setRels] = useState<Relationship[]>([]);
  const [loading, setLoading] = useState(true);
  const [dataSource, setDataSource] = useState<'mock' | 'custom'>('custom');
  const [expandingId, setExpandingId] = useState<string | null>(null);
  const [initErr, setInitErr] = useState<string | null>(null);
  const [dragging, setDragging] = useState(false);
  const [contextMenu, setContextMenu] = useState<ContextMenuState | null>(null);

  const adapterHooks = props.adapterHooks
  /* ---- Vaadin ReactAdapter 双向通信状态 ----
     - expandRequest:  React → Java，双击节点时请求 Java 生成扩展数据
     - expandResult:   Java → React，Java 返回的节点/边数据
     - selectedNodeId: React → Java，单击选中节点时通知 Java 打印日志 */
  // @ts-ignore
  const [expandResult] = adapterHooks.useState<ExpandResult | null>('expandResult', null);
  // @ts-ignore
  const [, setExpandRequest] = adapterHooks.useState<ExpandRequest | null>('expandRequest', null);
  // @ts-ignore
  const [, setSelectedNodeIdState] = adapterHooks.useState<string | null>('selectedNodeId', null);
  // @ts-ignore
  const [, setRefreshGraphState] = adapterHooks.useState<string | null>('refreshGraph', null);
  // @ts-ignore
  const [, setClickGraphContextMenuState] = adapterHooks.useState<string | null>('clickGraphContextMenu', null);

  // 单击选中
  const [selectedNodeId, setSelectedNodeId] = useState<string | null>(null);
  const [selectedRelId, setSelectedRelId] = useState<string | null>(null);

  // 自定义数据输入
  const [showCustomInput, setShowCustomInput] = useState(false);
  const [customDataText, setCustomDataText] = useState('');
  const [customDataError, setCustomDataError] = useState<string | null>(null);

  const existingIdsRef = useRef<Set<string>>(new Set());
  const expandingRef = useRef<string | null>(null);
  const latestExpandRequestRef = useRef<string | null>(null);
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
    setContextMenu(null);

    //using generateMockData from api.ts
    //const result = await fetchInitialGraph();
    //Using real data from server side query result
    const result = props.graphData;
    const currentColor = randomHslColor();
    const nvlNodes: Node[] = result.nodes.map((n: NvlNode) => ({
      id: n.id, caption: n.caption+": "+n.id, color: currentColor, size: BASE_SIZE
    }));
    const nvlRels: Relationship[] = result.rels.map((r: NvlRel) => ({
      //id: r.id, from: r.from, to: r.to, caption: r.caption+": "+r.id,
      // @ts-ignore
      id: r.id, from: r.from, to: r.to, caption: r.caption+": "+r.id, initialRel: r.initialRel, mainRel:r.mainRel, pathRel:r.pathRel,
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
  const handleRefresh = useCallback(() => {
    loadInitialGraph();
    setRefreshGraphState("FRESH");
    }, [loadInitialGraph]);

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
    setContextMenu(null);
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

  /* ---- 双击展开：请求 Java 侧生成扩展数据 ---- */
  const handleNodeDoubleClick = useCallback((node: Node, _hit: HitTargets, _evt: MouseEvent) => {
    if (dragging) return;
    const nodeId = node.id;
    if (expandingRef.current === nodeId) return;
    expandingRef.current = nodeId;
    setExpandingId(nodeId);
    setContextMenu(null);
    // 生成唯一 requestId，便于将 Java 的响应与本次双击请求关联
    const requestId = `${Date.now()}-${Math.random().toString(36).slice(2)}`;
    latestExpandRequestRef.current = requestId;

    // 通过 ReactAdapter 状态通道向 Java 发送 expandRequest，
    // Java 侧 generateExpandData() 生成数据后通过 expandResult 回传。
    // 注意：字段名必须用 clickedNodeId，不能用 nodeId，
    // 否则 Flow 的 MapSyncRpcHandler 会把 nodeId 当作 StateNode 的数字引用。
    setExpandRequest({ clickedNodeId: nodeId, requestId });
  }, [dragging, setExpandRequest]);

  /* ---- 接收 Java 返回的扩展数据并合并到图中 ---- */
  useEffect(() => {
    if (!expandResult) return;
    // 只处理与最近一次双击请求匹配的响应。从 Java 端发起 expandRequest 时 requestId 值为空，忽略双击请求匹配检查
    if(expandResult.requestId){
      if (expandResult.requestId !== latestExpandRequestRef.current) return;
    }

    const newNodes: Node[] = [];
    const newRels: Relationship[] = [];

    for (const n of expandResult.nodes) {
      if (existingIdsRef.current.has(n.id)) continue;
      existingIdsRef.current.add(n.id);
      newNodes.push({ id: n.id, caption: n.caption, color: n.color, size: EXPAND_SIZE});
    }
    for (const r of expandResult.rels) {
      if (!existingIdsRef.current.has(r.from) || !existingIdsRef.current.has(r.to)) continue;
      //newRels.push({ id: r.id, from: r.from, to: r.to, caption: r.caption});
      // @ts-ignore
      newRels.push({ id: r.id, from: r.from, to: r.to, caption: r.caption,initialRel: r.initialRel});
    }

    if (newNodes.length > 0) setNodes((prev) => [...prev, ...newNodes]);
    if (newRels.length > 0) setRels((prev) => [...prev, ...newRels]);

    expandingRef.current = null;
    setExpandingId(null);
  }, [expandResult]);

  /* ---- 单击选中：同步到 Java 侧打印日志 ---- */
  const handleNodeClick = useCallback((node: Node) => {
    const next = selectedNodeId === node.id ? null : node.id;
    setSelectedNodeId(next);
    setSelectedRelId(null);
    setContextMenu(null);
    // 通过 selectedNodeId 状态通道向 Java 发送节点选中事件
    setSelectedNodeIdState(next);
  }, [selectedNodeId, setSelectedNodeIdState]);

  const handleRelationshipClick = useCallback((rel: Relationship) => {
    setSelectedRelId((prev) => prev === rel.id ? null : rel.id);
    setSelectedNodeId(null);
    setContextMenu(null);
  }, []);

  const handleCanvasClick = useCallback(() => {
    setSelectedNodeId(null);
    setSelectedRelId(null);
    setContextMenu(null);

    //make sure edge color will recover for all clicked node or relation
    // @ts-ignore
    setRels(nvlRef.current?.getRelationships());
    //console.log("--Redraw Relations Completed--");
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
      //return { ...n, selected: false, activated: false, size: BASE_SIZE};
      return { ...n, selected: false, activated: false};
    });
  }, [nodes, selectedNodeId, activatedNodeIds]);

  const displayRels = useMemo(() => {
    return rels.map((r) => {
      const isSelected = r.id === selectedRelId;
      const isNeighbor = selectedNodeId !== null && (r.from === selectedNodeId || r.to === selectedNodeId);
      // @ts-ignore
      let currentColor = r.initialRel ? (r as any).color ?? undefined : '#ABABAB';
      // @ts-ignore
      if (r.mainRel === true){
        currentColor = '#85144B';
      }
      // @ts-ignore
      if (r.pathRel === true){
        currentColor = '#0074D9';
      }
      return {
        ...r,
        selected: isSelected,
        width: isSelected ? SELECTED_REL_WIDTH : isNeighbor ? BASE_REL_WIDTH + 1 : BASE_REL_WIDTH,
        captionSize: isSelected ? SELECTED_CAPTION_SIZE : BASE_CAPTION_SIZE,
        //color: isNeighbor ? '#88aacc' : (r as any).color ?? undefined,
        color: isNeighbor ? '#88aacc' : currentColor,
      };
    });
  }, [rels, selectedRelId, selectedNodeId]);

  // 右键节点：弹出上下文菜单
  const handleNodeRightClick = useCallback((node: Node, _hit: HitTargets, event: MouseEvent) => {

    setContextMenu({
      x: Math.max(0, Math.min(event.layerX, window.innerWidth - 160)),
      y: Math.max(0, Math.min(event.layerY, window.innerHeight - 100)),
      //x:event.layerX,
      //y:event.layerY,
      kind: 'node',
      id: node.id,
      caption: node.caption ?? '',
    });
  }, []);

  // 右键边：弹出上下文菜单
  const handleRelationshipRightClick = useCallback((rel: Relationship, _hit: HitTargets, event: MouseEvent) => {

    setContextMenu({
      x: Math.max(0, Math.min(event.layerX, window.innerWidth - 160)),
      y: Math.max(0, Math.min(event.layerY, window.innerHeight - 100)),
      kind: 'relationship',
      id: rel.id,
      caption: rel.caption ?? '',
    });
  }, []);

  // 右键空白：关闭上下文菜单
  const handleCanvasRightClick = useCallback(() => {
    setContextMenu(null);
  }, []);

  // 承接 context menu 菜单项的点击事件：
  // 打印菜单选项名称、右键点击对象类型（节点/边）、caption 和 id
  const handleContextMenuAction = useCallback((actionName: string) => {
    if (!contextMenu) return;
    console.log(
        `菜单选项: ${actionName} | 对象类型: ${contextMenu.kind === 'node' ? '节点' : '边'} | caption: ${contextMenu.caption} | id: ${contextMenu.id}`
    );
    setContextMenu(null);
    if('拓展子图' == actionName){
      // @ts-ignore
      handleNodeDoubleClick({id:contextMenu.id});
    }else{
      setClickGraphContextMenuState({
        menuOptionName:actionName,
        graphElementType:contextMenu.kind,
        clickedElementCaption:contextMenu.caption,
        clickedElementId:contextMenu.id
      });
    }
  }, [contextMenu]);

  /* ---- NVL 配置 ---- */
  const mouseCallbacks: MouseEventCallbacks = {
    onNodeClick: handleNodeClick,
    onNodeDoubleClick: handleNodeDoubleClick,
    onRelationshipClick: handleRelationshipClick,
    onCanvasClick: handleCanvasClick,
    onNodeRightClick: handleNodeRightClick,
    onRelationshipRightClick: handleRelationshipRightClick,
    onCanvasRightClick: handleCanvasRightClick,
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

      //make sure edge color will recover for the first clicked node or relation
      // @ts-ignore
      setRels(nvlRef.current?.getRelationships());
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

      {/* 右键上下文菜单 */}
      {contextMenu && (
          <div
              className="nvl-context-menu"
              style={{ left: contextMenu.x, top: contextMenu.y }}
              onContextMenu={(e) => e.preventDefault()}
          >
            <button type="button" className="nvl-context-menu-item" onClick={() => handleContextMenuAction('显示详情')}>
              显示详情
            </button>
            {contextMenu.kind === 'node' && (
                <button type="button" className="nvl-context-menu-item" onClick={() => handleContextMenuAction('拓展子图')}>
                  拓展子图
                </button>
            )}
          </div>
      )}

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
