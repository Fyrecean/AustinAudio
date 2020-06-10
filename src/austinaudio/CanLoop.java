package austinaudio;

public class CanLoop {
     protected AudioPlayer[] players = new AudioPlayer[2];

     public void triggerLoop() {
          if (players[1] == null) {
               return;
          }
          players[1].start();
          players[1].play();
          AudioPlayer hold = players[0];
          players[0] = players[1];
          players[1] = hold;
     }
}
