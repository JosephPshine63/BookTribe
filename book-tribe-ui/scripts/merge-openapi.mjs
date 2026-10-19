// Builds src/openapi/openapi.json by merging the OpenAPI specs of book-service and feedback-service,
// so that `npm run api-gen` can generate a single client for everything routed by the gateway.
//
// Usage: npm run openapi:fetch
// Override the sources with BOOK_API_DOCS / FEEDBACK_API_DOCS if the services are not on localhost.
import {mkdir, writeFile} from 'node:fs/promises';

const sources = [
  process.env.BOOK_API_DOCS ?? 'http://localhost:8091/api/v1/v3/api-docs',
  process.env.FEEDBACK_API_DOCS ?? 'http://localhost:8082/api/v1/v3/api-docs'
];

const specs = await Promise.all(sources.map(async (url) => {
  const res = await fetch(url);
  if (!res.ok) {
    throw new Error(`GET ${url} -> ${res.status}`);
  }
  return res.json();
}));

const [first] = specs;
const merged = {
  openapi: first.openapi,
  info: {...first.info, title: 'BookTribe API'},
  security: first.security,
  paths: {},
  components: {schemas: {}, securitySchemes: {}}
};

for (const spec of specs) {
  for (const [path, item] of Object.entries(spec.paths ?? {})) {
    if (merged.paths[path]) {
      throw new Error(`Duplicate path ${path}`);
    }
    merged.paths[path] = item;
  }
  for (const [section, entries] of Object.entries(spec.components ?? {})) {
    merged.components[section] = {...merged.components[section], ...entries};
  }
}

await mkdir('src/openapi', {recursive: true});
await writeFile('src/openapi/openapi.json', JSON.stringify(merged, null, 2) + '\n');
console.log(`Wrote src/openapi/openapi.json (${Object.keys(merged.paths).length} paths)`);
