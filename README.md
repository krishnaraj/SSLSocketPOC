A simple SSL based client/server demo using Android and an ultra simple tcp server written in Java.

Note: Currently does not work on Android M due to SSLHandshakeException.

###How to run?###
1. Run the server with the following command
`./gradlew :server:run`

2. Change the IP address in MainActivity.java to that of the server.

3. Run android app.