// 强制 NVL 使用主线程 fallback，避免 SharedWorker 加载问题
export const createHierarchicalLayoutWorker = () => {
  throw new Error('NVL: using fallback (Hierarchical)');
};
