// Public URLs of the backend exposed through Tailscale Funnel (see deploy/README.md).
// Replace booktribe.tailXXXX.ts.net with the server's Funnel hostname.
export const environment = {
  apiUrl: 'https://booktribe.tailXXXX.ts.net/api/v1',
  keycloak: {
    url: 'https://booktribe.tailXXXX.ts.net:8443',
    realm: 'book-social-network',
    clientId: 'bsn'
  }
};
