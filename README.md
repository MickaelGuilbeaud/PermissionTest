# PermissionsUtils
PermissionsUtils is a simple class simplifying the Android M new permission process. It is possible to request a single or multiple permissions, and also show an AlertDialog for explaining why the permissions are needed. The best part is that callbacks happen only in the onRequestPermissionsResult Activity method, even if the permission is already granted.

This class supports 4 use cases:
- Ask one permission
- Ask one permission and show an explanation if needed
- Ask more than one permission
- Ask more than one permission and show an explanation if needed

The result is easy to use:
- If we asked one permission, the onRequestPermissionsResult method is called with the expected result and we don't prompt the user if the permission is already granted.
- If we asked many permissions that are all granted, onRequestPermissionsResult is called with all the permissions.
- If we asked many permissions and some or all of them are denied, Android will prompt the user for them and the onRequestPermissionsResult method get a callback with the permissions that was previously denied.

This does not handle:
- Pre Android M versions. Version check is done in a single line and it can't be integrated to the API 23 process as there is no onRequestPermissionsResult method to send the callback.

How to use:
- Add the PermissionsUtils class to your project
- Create two String ressources with ids camera_permission_explanation_back and camera_permission_explanation_ask, they are used by the AlertDialog. 
