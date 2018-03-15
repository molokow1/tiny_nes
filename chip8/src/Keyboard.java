import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter {

    private int currentKeyPressed;
    private int currentKeyReleased;

    private boolean keyPressed = false;

    public static final int[] keycodeMap = {
            KeyEvent.VK_4, // Key 1
            KeyEvent.VK_5, // Key 2
            KeyEvent.VK_6, // Key 3
            KeyEvent.VK_7, // Key 4
            KeyEvent.VK_R, // Key 5
            KeyEvent.VK_Y, // Key 6
            KeyEvent.VK_U, // Key 7
            KeyEvent.VK_F, // Key 8
            KeyEvent.VK_G, // Key 9
            KeyEvent.VK_H, // Key A
            KeyEvent.VK_J, // Key B
            KeyEvent.VK_V, // Key C
            KeyEvent.VK_B, // Key D
            KeyEvent.VK_N, // Key E
            KeyEvent.VK_M, // Key F
    };


    @Override
    public void keyPressed(KeyEvent e){
        currentKeyReleased = 0;
        currentKeyPressed = getKeyCodeValue(e.getKeyCode());
        keyPressed = true;
//        System.out.println("KEYPRESSED: " + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e){
        currentKeyPressed = 0;
        currentKeyReleased = getKeyCodeValue(e.getKeyCode());
        keyPressed = false;
//        System.out.println("KEYRELEASED: " + e.getKeyChar());
    }


    public int getCurrentKeyPressed(){
        return currentKeyPressed;
    }

    public int getKeyCodeValue(int k){
        for(int i = 0; i < keycodeMap.length; i++){
            if(keycodeMap[i] == k){
                return i + 1;
            }
        }
        return 0;
    }

    public static void main(String[] args){
        Keyboard kb = new Keyboard();
    }

}