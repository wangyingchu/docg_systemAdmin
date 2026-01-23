import {ReactElement, useCallback, useEffect, useMemo, useRef, useState} from 'react'
import { InteractiveNvlWrapper } from '@neo4j-nvl/react'
import { getDatabase, getNeo4jDriver } from './lib/neo4j'
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";

class ExplorationResultGraphChartElement extends ReactAdapterElement {
    protected override render(hooks: RenderHooks): ReactElement | null {
        type NvlNode = {
            id: string
            labels?: string[]
            properties?: Record<string, unknown>
            caption?: string
            captionSize?: number
            color?: string
        }

        type NvlRelationship = {
            id: string
            from: string
            to: string
            type?: string
            properties?: Record<string, unknown>
            caption?: string
            captionSize?: number
        }

        const getNodeCaption = (
            labels?: string[],
            properties?: Record<string, unknown>
        ): string => {
            if (labels && labels.length > 0 && typeof labels[0] === 'string' && labels[0].length > 0) {
                return labels[0]
            }

            if (properties) {
                const preferredKeys = ['name', 'title', 'id', 'label']
                for (const key of preferredKeys) {
                    const value = properties[key]
                    if (typeof value === 'string' && value.trim().length > 0) {
                        return value
                    }
                }

                const entries = Object.entries(properties)
                if (entries.length > 0) {
                    const [key, value] = entries[0]
                    return `${key}: ${String(value)}`
                }
            }

            return ''
        }

        const [nodes, setNodes] = useState<NvlNode[]>([])
        const [relationships, setRelationships] = useState<NvlRelationship[]>([])
        const [loading, setLoading] = useState<boolean>(false)
        const [error, setError] = useState<string | null>(null)
        const expandedNodesRef = useRef<Set<string>>(new Set())
        const lastExpansionColorRef = useRef<string | null>(null)
        const minimapRef = useRef<HTMLDivElement | null>(null)
        const [minimapReady, setMinimapReady] = useState(false)
        const neo4jAvailable = Boolean(
            // @ts-ignore
            import.meta.env.VITE_NEO4J_URI &&
            // @ts-ignore
            import.meta.env.VITE_NEO4J_USERNAME &&
            // @ts-ignore
            import.meta.env.VITE_NEO4J_PASSWORD
        )

        const generateData = useMemo(() => {
            return () => {
                const nodeCount = 220
                const relCount = 120
                const generatedNodes: NvlNode[] = Array.from({ length: nodeCount }, (_, i) => {
                    const labels = ['Person']
                    return {
                        id: `n${i}`,
                        labels,
                        properties: { name: `Person ${i}`, group: i % 10 },
                        caption: getNodeCaption(labels, { name: `Person ${i}`, group: i % 10 }),
                        captionSize: 12
                    }
                })

                const generatedRels: NvlRelationship[] = []
                for (let i = 0; i < relCount; i++) {
                    const fromIdx = Math.floor(Math.random() * nodeCount)
                    let toIdx = Math.floor(Math.random() * nodeCount)
                    if (toIdx === fromIdx) toIdx = (toIdx + 1) % nodeCount
                    generatedRels.push({
                        id: `r${i}`,
                        from: `n${fromIdx}`,
                        to: `n${toIdx}`,
                        type: 'KNOWS',
                        properties: { weight: Math.random() },
                        caption: 'KNOWS',
                        captionSize: 3
                    })
                }
                return { generatedNodes, generatedRels }
            }
        }, [])

        useEffect(() => {
            // @ts-ignore
            const uri = import.meta.env.VITE_NEO4J_URI as string | undefined
            // @ts-ignore
            const username = import.meta.env.VITE_NEO4J_USERNAME as string | undefined
            // @ts-ignore
            const password = import.meta.env.VITE_NEO4J_PASSWORD as string | undefined

            async function load() {
                setError(null)
                setLoading(true)
                try {
                    if (uri && username && password) {
                        // Try real Neo4j
                        const driver = getNeo4jDriver()
                        const db = getDatabase()
                        const session = driver.session({ database: db })
                        try {
                            // Return a decent volume of nodes/relations. Adjust LIMIT if your dataset is large.
                            const result = await session.run(
                                'MATCH (a)-[r]->(b) RETURN a AS source, r AS rel, b AS target LIMIT 500'
                            )
                            const nodeMap = new Map<string, NvlNode>()
                            const rels: NvlRelationship[] = []
                            for (const record of result.records) {
                                const a = record.get('source')
                                const b = record.get('target')
                                const r = record.get('rel')
                                const aId = a.identity.toString()
                                const bId = b.identity.toString()
                                if (!nodeMap.has(aId)) {
                                    nodeMap.set(aId, {
                                        id: aId,
                                        labels: a.labels,
                                        properties: a.properties,
                                        caption: getNodeCaption(a.labels, a.properties),
                                        captionSize: 12
                                    })
                                }
                                if (!nodeMap.has(bId)) {
                                    nodeMap.set(bId, {
                                        id: bId,
                                        labels: b.labels,
                                        properties: b.properties,
                                        caption: getNodeCaption(b.labels, b.properties),
                                        captionSize: 12
                                    })
                                }
                                rels.push({
                                    id: r.identity.toString(),
                                    from: aId,
                                    to: bId,
                                    type: r.type,
                                    properties: r.properties,
                                    caption: r.type,
                                    captionSize: 3
                                })
                            }
                            const nodeArr = Array.from(nodeMap.values())
                            // Ensure minimum counts; if dataset smaller, fallback to generated
                            if (nodeArr.length >= 200 && rels.length >= 100) {
                                setNodes(nodeArr)
                                setRelationships(rels)
                                return
                            }
                        } finally {
                            await session.close()
                        }
                    }
                    // Fallback to generated data
                    const { generatedNodes, generatedRels } = generateData()
                    setNodes(generatedNodes)
                    setRelationships(generatedRels)
                } catch (e) {
                    setError(e instanceof Error ? e.message : String(e))
                    const { generatedNodes, generatedRels } = generateData()
                    setNodes(generatedNodes)
                    setRelationships(generatedRels)
                } finally {
                    setLoading(false)
                }
            }
            load()
        }, [generateData])

        useEffect(() => {
            setMinimapReady(true)
        }, [])

        const appendGraphElements = useCallback(
            (newNodes: NvlNode[], newRelationships: NvlRelationship[]) => {
                if (newNodes.length > 0) {
                    setNodes((prev) => {
                        const merged = new Map(prev.map((node) => [node.id, node]))
                        newNodes.forEach((node) => {
                            if (!merged.has(node.id)) {
                                merged.set(node.id, node)
                            }
                        })
                        return Array.from(merged.values())
                    })
                }

                if (newRelationships.length > 0) {
                    setRelationships((prev) => {
                        const merged = new Map(prev.map((rel) => [rel.id, rel]))
                        newRelationships.forEach((rel) => {
                            if (!merged.has(rel.id)) {
                                merged.set(rel.id, rel)
                            }
                        })
                        return Array.from(merged.values())
                    })
                }
            },
            []
        )

        const generateExpansionColor = useCallback(() => {
            const previous = lastExpansionColorRef.current
            let color: string | null = null
            let attempts = 0
            while (!color || color === previous || color === '#ff8c42') {
                const value = Math.floor(Math.random() * 0xffffff)
                color = `#${value.toString(16).padStart(6, '0')}`
                attempts += 1
                if (attempts > 10) {
                    color = previous === '#000000' ? '#1abc9c' : '#000000'
                    break
                }
            }
            lastExpansionColorRef.current = color
            return color
        }, [])

        const fallbackExpand = useCallback(
            (nodeId: string) => {
                const expansionColor = generateExpansionColor()
                const createdNodes: NvlNode[] = []
                const createdRelationships: NvlRelationship[] = []
                const timestamp = Date.now()
                for (let i = 0; i < 5; i++) {
                    const childId = `${nodeId}-auto-${timestamp}-${i}`
                    createdNodes.push({
                        id: childId,
                        labels: ['Generated'],
                        properties: { name: `Generated ${i}` },
                        caption: getNodeCaption(['Generated'], { name: `Generated ${i}` }),
                        captionSize: 12,
                        color: expansionColor
                    })
                    createdRelationships.push({
                        id: `${nodeId}->${childId}`,
                        from: nodeId,
                        to: childId,
                        type: 'EXPANDED',
                        properties: { generated: true },
                        caption: 'EXPANDED',
                        captionSize: 3
                    })
                }
                appendGraphElements(createdNodes, createdRelationships)
            },
            [appendGraphElements, generateExpansionColor]
        )

        const expandFromNeo4j = useCallback(
            async (nodeId: string) => {
                let session: any | null = null
                try {
                    const driver = getNeo4jDriver()
                    session = driver.session({ database: getDatabase() })
                    const result = await session.run(
                        'MATCH (n) WHERE id(n) = toInteger($id) MATCH (n)-[r]-(m) RETURN m, r',
                        { id: nodeId }
                    )

                    const newNodes: NvlNode[] = []
                    const newRelationships: NvlRelationship[] = []
                    let expansionColor: string | null = null

                    for (const record of result.records) {
                        const neighbor = record.get('m')
                        const rel = record.get('r')

                        if (neighbor) {
                            const neighborId = neighbor.identity.toString()
                            if (neighborId !== nodeId) {
                                if (!expansionColor) {
                                    expansionColor = generateExpansionColor()
                                }
                                newNodes.push({
                                    id: neighborId,
                                    labels: neighbor.labels,
                                    properties: neighbor.properties,
                                    caption: getNodeCaption(neighbor.labels, neighbor.properties),
                                    captionSize: 12,
                                    color: expansionColor
                                })
                            }
                        }

                        if (rel) {
                            const relId = rel.identity.toString()
                            const fromId = rel.start?.toString?.() ?? nodeId
                            const toId = rel.end?.toString?.() ?? nodeId
                            newRelationships.push({
                                id: relId,
                                from: fromId,
                                to: toId,
                                type: rel.type,
                                properties: rel.properties,
                                caption: rel.type,
                                captionSize: 3
                            })
                        }
                    }

                    if (newNodes.length === 0 && newRelationships.length === 0) {
                        fallbackExpand(nodeId)
                    } else {
                        if (!expansionColor) {
                            expansionColor = generateExpansionColor()
                        }
                        // Ensure all new nodes have color (covers case where only relationships were added)
                        newNodes.forEach((node) => {
                            if (!node.color) {
                                node.color = expansionColor as string
                            }
                        })
                        appendGraphElements(newNodes, newRelationships)
                    }
                } catch (err) {
                    console.error('Failed to expand Neo4j node', err)
                    fallbackExpand(nodeId)
                } finally {
                    await session?.close()
                }
            },
            [appendGraphElements, fallbackExpand, generateExpansionColor]
        )

        const handleNodeClick = useCallback(
            async (node: { id?: string }) => {
                const nodeId = node?.id
                if (!nodeId) return
                if (expandedNodesRef.current.has(nodeId)) return

                if (neo4jAvailable) {
                    await expandFromNeo4j(nodeId)
                } else {
                    fallbackExpand(nodeId)
                }

                expandedNodesRef.current.add(nodeId)
            },
            [expandFromNeo4j, fallbackExpand, neo4jAvailable]
        )

        return (
            <div style={{ width: '100vw', height: '100vh', margin: 0, position: 'relative' }}>
                {loading && <div>Loading Neo4j data...</div>}
                {error && <div style={{ color: 'red' }}>{error}</div>}
                <div
                    ref={minimapRef}
                    style={{
                        position: 'absolute',
                        right: 12,
                        bottom: 12,
                        width: 200,
                        height: 140,
                        background: 'rgba(255,255,255,0.9)',
                        border: '1px solid #e0e0e0',
                        borderRadius: 6,
                        boxShadow: '0 2px 8px rgba(0,0,0,0.15)',
                        overflow: 'hidden',
                        zIndex: 10
                    }}
                />
                <InteractiveNvlWrapper
                    key={minimapReady && minimapRef.current ? 'with-minimap' : 'no-minimap'}
                    nodes={nodes}
                    rels={relationships}
                    layout={'forceDirected' as any}
                    nvlOptions={{
                        initialZoom: 1,
                        renderer: 'canvas',
                        minimapContainer: minimapReady ? minimapRef.current ?? undefined : undefined,
                        // @ts-ignore
                        styling: {
                            defaultNodeColor: '#ff8c42',
                            defaultRelationshipColor: '#34495e',
                            selectedBorderColor: '#e74c3c',
                            nodeDefaultBorderColor: '#7f8c8d',
                            dropShadowColor: 'rgba(0,0,0,0.3)'
                        }
                    }}
                    mouseEventCallbacks={{
                        onDrag: true,
                        onDragStart: true,
                        onDragEnd: true,
                        onNodeDoubleClick: handleNodeClick,
                        onCanvasClick: true,
                        onZoom: true,
                        onPan: true
                    }}
                    nvlCallbacks={{
                        onLayoutDone: () => console.log('NVL layout completed')
                    }}
                />
            </div>
        )
    }
}

customElements.define(
    "exploration-result-graph-chart",
    ExplorationResultGraphChartElement
);



