import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ROM {



    private byte[] romContent;


    private byte[] fontContent;

    public ROM(String ROMfile, String FONTfile){
        try {
            romContent = readFile(ROMfile);
            fontContent = readFile(FONTfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readFile(String file) throws IOException {
        Path path = Paths.get(file);
        return Files.readAllBytes(path);
//        System.out.println(romContent.length);
    }

    public byte[] getRomContent() {
        return romContent;
    }

    public byte[] getFontContent() { return fontContent; }


    public static void main(String[] args){
        String ROMfile = "pong.rom";
        String FONTfile = "./resources/FONTS.chip8";
        ROM rom = new ROM(ROMfile, FONTfile);
        byte[] content = rom.getRomContent();
        byte[] fonts = rom.getFontContent();
        System.out.println(fonts.length);
        for(int i = 0; i < fonts.length; i++){
            System.out.println(String.format("0x%08X",fonts[i]));
        }
    }
}
