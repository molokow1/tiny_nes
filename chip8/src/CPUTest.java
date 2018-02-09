import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class CPUTest {
    String romFile = "pong.rom";
    String fonts = "./resources/FONTS.chip8";
    ROM rom = new ROM(romFile, fonts);
    Chip8 cpu = new Chip8(rom);

    @Test
    public void testJumpToAddress(){

        cpu.decodeAndExecute(0x1234);
        assertEquals(0x0234, cpu.getPC(), "PC must be set to 0x234");
    }

    @Test
    public void testCallSubroutine(){
        cpu.decodeAndExecute(0x2100);
        assertEquals(0x0100, cpu.getPC(), "PC must be set to 0x100");
    }

    @Test
    public void testStoreToVReg(){
        cpu.decodeAndExecute(0x6123);
        assertEquals(0x23, cpu.getV_reg()[1], "V reg[1] must be set to 0x023");
    }

    public static void main(String[] args){
    }
}
