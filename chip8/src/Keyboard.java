import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter {


    @Override
    public void keyPressed(KeyEvent e){
        System.out.println("KEYPRESSED: " + e.toString());
    }

    @Override
    public void keyReleased(KeyEvent e){
        System.out.println("KEYRELEASED: " + e.toString());
    }

    public static void main(String[] args){
        Keyboard kb = new Keyboard();
    }

}