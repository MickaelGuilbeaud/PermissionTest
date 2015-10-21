# PermissionTest
Basic application to test the Android M new permission model.

The PermissionsUtils class can help manage the permission for Android M and next versions. It integrate an AlertDialog for explaining to the user why the permission is needed, and both Granted and Denied results are send to the onRequestPermissionsResult method, simplifying the process.

This class does not handle :
- Pre Android M versions. Version check is done in a single line and it can't be integrated to the API 23 process as there is no onRequestPermissionsResult method.
