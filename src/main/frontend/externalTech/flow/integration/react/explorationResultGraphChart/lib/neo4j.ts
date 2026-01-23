import neo4j, { Driver } from 'neo4j-driver'

let cachedDriver: Driver | null = null

export function getNeo4jDriver(): Driver {
  if (cachedDriver) return cachedDriver
  // @ts-ignore
  const uri = import.meta.env.VITE_NEO4J_URI as string | undefined
  // @ts-ignore
  const username = import.meta.env.VITE_NEO4J_USERNAME as string | undefined
  // @ts-ignore
  const password = import.meta.env.VITE_NEO4J_PASSWORD as string | undefined

  if (!uri || !username || !password) {
    throw new Error('Missing VITE_NEO4J_URI / VITE_NEO4J_USERNAME / VITE_NEO4J_PASSWORD')
  }

  cachedDriver = neo4j.driver(uri, neo4j.auth.basic(username, password))
  return cachedDriver
}

export function getDatabase(): string | undefined {
  // @ts-ignore
  return (import.meta.env.VITE_NEO4J_DATABASE as string | undefined) || undefined
}

export async function closeDriver(): Promise<void> {
  if (cachedDriver) {
    await cachedDriver.close()
    cachedDriver = null
  }
}


