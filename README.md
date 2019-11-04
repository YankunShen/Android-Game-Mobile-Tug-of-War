# Android-Game-Mobile-Tug-of-War
It has two parts:
- Server
- Client

###For the Client part, 
We collect data from **Gravity acceleration sensor** in a smartphone based on Android Operating System. The gravity acceleration has x-y-z three dimensions, so the data includes two catagories, up/down and left/right. Then we need to send the data we collected to the server.

In the meantime, we should also receive the data from the server and **visualize** the data using some formates in real time.

###For the Server part,
We need to process the data obtained from the Client and send the correct data to the Client.

##Requirements:
1. The game has limited time
2. Mulitple players are allowed to play the game together
3. Every time the player shakes the phone, the count pluses one
