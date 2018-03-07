/**
 * Created by Sean Wu on 6/03/2018.
 */
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestGUI {

    private Frame mainFrame;
    public Keyboard kb;
    public TestGUI(){
        initGUI();
        mainFrame.setVisible(true);
    }

    private void initGUI(){
        kb = new Keyboard();
        mainFrame = new Frame("Test GUI");
        mainFrame.setSize(800,600);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        mainFrame.addKeyListener(kb);
    }

    public static void main(String[] args){
        TestGUI gui = new TestGUI();
        Keyboard kb = gui.kb;
        while(true){
//            if(kb.isKeyPressed()){
//               System.out.println("KEY PRESSED: " + kb.getCurrentKeyPressed());
//            }
            System.out.println(kb.isKeyPressed());
        }
    }

}
