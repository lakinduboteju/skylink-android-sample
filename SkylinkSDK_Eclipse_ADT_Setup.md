Skylink SDK : Getting Started (Android Developer Tools)
----

###**Important!** : Temasys does **not** officially support the ADT/Eclipse development tools with our SkylinkSDK for Android. This information is offered for educational purposes only.

We highly recommend using Android Studio for development purposes. However, If you are using Eclipse with ADT (Android Developer Tools) plugin, you would need to do the following, to start using the SDK with your application.

Set up the Skylink SDK for Android
---

##### 1. [Download ](http://cdn.temasys.com.sg/skylink/skylinksdk/android/latest/skylink_sdk-release.jar) the Skylink SDK JAR

##### 2. Copy the Skylink SDK JAR to the /libs folder

The Skylink SDK Jar should now be located at:

    /libs/skylink_sdk-release.jar

##### 3. Set up SocketIO Java

Follow the instructions on [socket.io-client.java](https://github.com/nkzawa/socket.io-client.java)

Set up WebRTC native dependencies
---
1. Download the required libraries. 
1.1 [Download armeabi-v7a](http://cdn.temasys.com.sg/skylink/skylinksdk/android/latest/armeabi-v7a/libjingle_peerconnection_so.so)
1.2 [Download x86](http://cdn.temasys.com.sg/skylink/skylinksdk/android/latest/x86/libjingle_peerconnection_so.so)
1.3 [Download x86-64](http://cdn.temasys.com.sg/skylink/skylinksdk/android/latest/x86-64/libjingle_peerconnection_so.so)

2. Set up libjingle\_peerconnection\_so.so in relevant architecture folders

	Make architecture specific directories inside libs folder:

    /libs/armeabi-v7a
    /libs/x86
    /libs/x86-64

3. Copy the relevant libjingle\_peerconnection\_so.so files on to the proper directories

	WebRTC .so (armeabi-v7a) file is now located at:
	
	`/libs/armeabi-v7a/libjingle_peerconnection_so.so`

	WebRTC .so (x86) file is now located at:

    `/libs/x86/libjingle_peerconnection_so.so`

	WebRTC .so (x86-64) file is now located at:

    `/libs/x86-64/libjingle_peerconnection_so.so`

### You are ready go!

For more information on the SDK usage, please refer to the [simple demo application](https://github.com/Temasys/skylink-android-sample)
application. You will need to follow the same procedure described above to add the Skylink SDK for Android to the sample application. The sample application also requires [v7 appcompat](https://developer.android.com/tools/support-library/setup.html) as a dependency.