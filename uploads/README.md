Uploads directory (English)

Overview:
This folder is used at runtime to store uploaded files (for example user avatars, book images, or other media). It is an environment-specific storage area and typically excluded from version control.

Notes:
- Do not commit runtime files. The uploads folder is often listed in .gitignore.
- Backup or clear uploads as part of environment maintenance if necessary.
- The backend application reads/writes files here; configure path and permissions according to deployment environment.

See top-level README for integration and development instructions.