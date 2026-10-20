// Public URLs of the backend exposed through Tailscale Funnel (see deploy/README.md):
// gateway on :8443, Keycloak on :10000 (:443 is used by another app on the server).
export const environment = {
  apiUrl: 'https://gpr-server.tailed5107.ts.net:8443/api/v1',
  keycloak: {
    url: 'https://gpr-server.tailed5107.ts.net:10000',
    realm: 'book-social-network',
    clientId: 'bsn'
  }
};
