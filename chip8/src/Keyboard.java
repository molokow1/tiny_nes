import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter {

    private char currentKeyPressed;
    private char currentKeyReleased;

    private boolean keyPressed = false;

    @Override
    public void keyPressed(KeyEvent e){
        currentKeyPressed = e.getKeyChar();
        keyPressed = true;
//        System.out.println("KEYPRESSED: " + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e){
        currentKeyReleased = e.getKeyChar();
        keyPressed = false;
//        System.out.println("KEYRELEASED: " + e.getKeyChar());
    }


    public boolean isKeyPressed(){
        return keyPressed;
    }

    public char getCurrentKeyPressed(){
        return currentKeyPressed;
    }



    public static void main(String[] args){
        Keyboard kb = new Keyboard();
    }

}