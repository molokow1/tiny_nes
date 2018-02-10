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
        cpu.decodeAndExecute(0x6A02);
        assertEquals(0x02, cpu.getV_reg()[0xA], "V reg[0xA] must be set to 0x02");
    }

    @Test
    public void testAdditionWithNumber(){
        cpu.decodeAndExecute(0x6512);
        cpu.decodeAndExecute(0x7523);
        assertEquals(0x23 + 0x12, cpu.getV_reg()[5], "V reg[1] must be set to 0x23 + 0x12");

    }

    @Test
    public void testStoreRegToReg(){
        storeToReg(5,0x12);
        storeToReg(6,0x13);

        cpu.decodeAndExecute(0x8560);

        assertEquals(0x13, cpu.getV_reg()[5], "V reg[5] must be set to 0x13");
    }


    @Test
    public void testBitwiseOrWithRegContent(){
        storeToReg(5,0x12);
        storeToReg(6,0x13);
        cpu.decodeAndExecute(0x8561);
        assertEquals(0x12 | 0x13, cpu.getV_reg()[5], "V reg[5] must  be set to 0x12 | 0x13");
    }

    @Test
    public void testBitwiseAndWithRegContent(){
        storeToReg(5,0x12);
        storeToReg(6,0x13);
        cpu.decodeAndExecute(0x8562);
        assertEquals(0x12 & 0x13, cpu.getV_reg()[5], "V reg[5] must  be set to 0x12 & 0x13");
    }

    @Test
    public void testBitwiseXORwithRegContent(){
        storeToReg(5,0x12);
        storeToReg(6,0x13);
        cpu.decodeAndExecute(0x8563);
        assertEquals(0x12 ^ 0x13, cpu.getV_reg()[5], "V reg[5] must  be set to 0x12 ^ 0x13");
    }

    @Test
    public void testAdditionWithRegContent(){
        storeToReg(5,0x12);
        storeToReg(6,0x13);
        cpu.decodeAndExecute(0x8564);
        cpu.printV_reg();
        assertEquals(0x12 + 0x13, cpu.getV_reg()[5], "V reg[5] must be set to 0x12 + 0x13");
        assertEquals(0, cpu.getVF(), "Should not overflow");
        storeToReg(6, 255);
        cpu.decodeAndExecute(0x8564);
        assertEquals(1, cpu.getVF(), "Should overflow");
        assertEquals(0x24, cpu.getV_reg()[5], "V reg[5] should overflow to 0x24");
        cpu.printV_reg();
    }


    public void storeToReg(int reg, int num){
        int command = 0x6000;
        command = command | reg << 8 | num;
        cpu.decodeAndExecute(command);
    }

    public static void main(String[] args){
    }
}
