Keycloak configuration (English)

Overview:
This folder holds Keycloak-related assets used to configure authentication for the Book Social Network project. It includes a realm export that can be imported into a Keycloak server for local development or testing.

How to use:
- Import the realm JSON into Keycloak (Admin Console -> Realm -> Add/import realm) to create the realm, clients, and default users.
- Adjust URLs and client settings to match local backend and frontend addresses.

Key files and folders:
- realm/ - exported Keycloak realm configuration (JSON)
- README.md - local notes (Italian)

Notes:
- Keycloak is optional for development but recommended to reproduce authentication and authorization flows used in production.