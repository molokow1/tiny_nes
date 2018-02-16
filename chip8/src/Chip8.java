
import java.util.concurrent.ThreadLocalRandom;

public class Chip8 {

//    private ROM rom;

    private short[] memory;
    private static final int MEM_SIZE = 4096;
    private static final int V_REG_SIZE = 16;
    private static final int STACK_SIZE = 16;

    private static final int SCREEN_WIDTH = 64;
    private static final int SCREEN_HEIGHT = 32;
    private short[] v_reg;
    private short VF;
    private short I;
    private short PC;

    private short[][] displayArray;

    private short[] stack;
    private short stackPtr;

    private short delay_timer;
    private short sound_timer;

    private int skip_inc;

    private int opcode;

    public Chip8(ROM rom){
        initCPU();
        loadFontSet(rom);
        loadROMtoMem(rom);
//        printMemContent();
    }

    private void initCPU(){
        PC = 0x200;
        I = 0;
        VF = 0;
        stackPtr = 0;
        delay_timer = 0;
        sound_timer = 0;
        skip_inc = 0;
        displayArray = new short[SCREEN_WIDTH][SCREEN_HEIGHT];
        this.memory = new short[MEM_SIZE];
        this.v_reg = new short[V_REG_SIZE];
        this.stack = new short[STACK_SIZE];
    }

    private void loadROMtoMem(ROM rom){
        for(int i = 0; i < rom.getRomContent().length; i++){
            this.memory[i + 0x200] = rom.getRomContent()[i];
        }
    }

    private void loadFontSet(ROM rom){
        for(int i = 0; i < rom.getFontContent().length ; i++){
            this.memory[i] = (short) (rom.getFontContent()[i] & 0xFF);
        }
    }

    public void printMemContent(){
        for(int i = 0; i < MEM_SIZE; i++){
            System.out.println(this.memory[i]);
        }
    }

    public void printMemRange(int min, int max){
        for(int i = min; i < max; i++){
            System.out.println(String.format("0x%08X",this.memory[i]));
        }
    }

    public void emulationCycle(){
        // Fetch Opcode
        opcode = fetchInstruction();

        // Decode and Execute Opcode
        skip_inc = decodeAndExecute(opcode);

        if(skip_inc == 0){
            PC += 2;
        }
        // Update timers

    }



    private int fetchInstruction(){
        return this.memory[PC] << 8 | this.memory[PC + 1];
    }

    private void clearDisplay(){
        for(int i = 0; i < SCREEN_WIDTH; i++){
            for(int j = 0; j < SCREEN_HEIGHT; j++){
                displayArray[i][j] = 0;
            }
        }
    }
    public int decodeAndExecute(int opcode){
        switch(opcode & 0xF000){
            case 0x0000:
                switch(opcode & 0x00FF){
                    case 0x00E0:
                        clearDisplay();
                        //Clear the display
                        break;
                    case 0x00EE:
                        //return from a subroutine - set PC to the top of the stack then substracts 1 from stack pointer
                        PC = stack[stackPtr];
                        stackPtr--;
                        return 1;
                        //return command
//                        break;
                }
                break;
            case 0x1000:
                PC = (short)(opcode & 0x0FFF);
                //if its a jump command
                return 1;
                //jump to location (op & 0x0FFF)
                //break;

            case 0x2000:
                stackPtr++;
                stack[stackPtr] = PC;
                PC = (short)(opcode & 0x0FFF);
                //call subroutine at (op & 0x0FFF)
                return 1;

            case 0x3000:
                if(v_reg[opcode & 0x0F00] == v_reg[opcode & 0x00FF]){
                    PC += 2;
                }
                //skip next instruction if V[op & 0x0F00] == (op & 0x00FF)
                break;

            case 0x4000:
                if(v_reg[opcode & 0x0F00] != v_reg[opcode & 0x00FF]){
                    PC += 2;
                }
                //skip next instruction if V[op & 0x0F00] != (op & 0x00FF)
                break;

            case 0x5000:
                if(v_reg[opcode & 0x0F00] == v_reg[opcode & 0x00FF]){
                    PC += 2;
                }
                //skip next instruction if V[op & 0x0F00] == V[op & 0x00F0]
                break;

            case 0x6000:
                v_reg[(opcode & 0x0F00) >> 8] = (short)(opcode & 0x00FF);
                //set V[op & 0x0F00] = (op & 0x00FF)
                break;

            case 0x7000:
                v_reg[(opcode & 0x0F00) >> 8] += (opcode & 0x00FF);
                //set V[op & 0x0F00] = V[op & 0x0F00] + (op & 0x00FF)
                break;

            case 0x8000:
                switch(opcode & 0x000F){
                    case 0x0000:
                        v_reg[(opcode & 0x0F00) >> 8] = v_reg[(opcode & 0x00F0) >> 4];
                        //set V[op & 0x0F00] = V[op & 0x00F0]
                        break;
                    case 0x0001:
                        v_reg[(opcode & 0x0F00) >> 8] = (short) (v_reg[(opcode & 0x0F00) >> 8] | v_reg[(opcode & 0x00F0) >> 4]);
                        //set V[op & 0x0F00] = V[op & 0x0F00] || V[op & 0x00F0]
                        break;
                    case 0x0002:
                        v_reg[(opcode & 0x0F00) >> 8] = (short) (v_reg[(opcode & 0x0F00) >> 8] & v_reg[(opcode & 0x00F0) >> 4]);
                        //set V[op & 0x0F00] = V[op & 0x0F00] && V[op & 0x00F0]
                        break;
                    case 0x0003:
                        //set V[op & 0x0F00] = V[op & 0x0F00] XOR V[op & 0x00F0]
                        v_reg[(opcode & 0x0F00) >> 8] = (short) (v_reg[(opcode & 0x0F00) >> 8] ^ v_reg[(opcode & 0x00F0) >> 4]);
                        break;
                    case 0x0004:

                        int result = (v_reg[(opcode & 0x0F00) >> 8] + v_reg[(opcode & 0x00F0) >> 4]);
                        v_reg[(opcode & 0x0F00) >> 8] = (short)(result & 0x00FF);
                        if(result > 255){
                            VF = 1;
                        } else {
                            VF = 0;
                        }
                        //set V[op & 0x0F00] = V[op & 0x0F00] + V[op & 0x00F0] set VF = carry
                        //if greater than 8 bits then VF = carry, only lowest 8bits are kept.
                        break;
                    case 0x0005:
                        //set V[op & 0x0F00] = V[op & 0x0F00] - V[op & 0x00F0] set VF = not borrow
                        //if Vx > Vy then VF is 1 else 0
                        if(v_reg[(opcode & 0x0F00) >> 8] > v_reg[(opcode & 0x00F0) >> 4]){
                            VF = 1;
                        } else {
                            VF = 0;
                        }
                        v_reg[(opcode & 0x0F00) >> 8] = (short)((v_reg[(opcode & 0x0F00) >> 8] - v_reg[(opcode & 0x00F0) >> 4]) & 0xFF);

                        break;
                    case 0x0006:
                        if((v_reg[(opcode & 0x0F00) >> 8] & 0x0001) == 1){
                            VF = 1;
                        } else {
                            VF = 0;
                        }
                        v_reg[(opcode & 0x0F00) >> 8] = (short) (v_reg[(opcode & 0x0F00) >> 8] / 2);
                        //set V[op & 0x0F00] = V[op & 0x0F00] SHR 1
                        //if the least-significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2.
                        break;
                    case 0x0007:
                        //set V[op & 0x0F00] = V[op & 0x00F0] - V[op & 0x0F00]
                        if((v_reg[(opcode & 0x00F0) >> 4] > v_reg[(opcode & 0x0F00) >> 4])){
                            VF = 1;
                        } else {
                            VF = 0;
                        }
                        v_reg[(opcode & 0x0F00) >> 8] = (short)((v_reg[(opcode & 0x00F0) >> 4] - v_reg[(opcode & 0x0F00) >> 8]) & 0xFF);
                        //if Vy > Vx then VF is set to 1 else 0
                        break;
                    case 0x000E:
                        short regContent = v_reg[(opcode & 0x0F00) >> 8];
                        //System.out.println(String.format("REG CONTENT: 0x%08X",regContent));
                        //System.out.println(String.format("MHS: 0x%08X",(regContent >> 7 & 0x1)));
                        if ((regContent >> 7 & 0x0001) == 1){
                            VF = 1;
                        } else {
                            VF = 0;
                        }

                        v_reg[(opcode & 0x0F00) >> 8] = (short)((regContent * 2) & 0xFF);
                        //set V[op & 0x0F00] = V[op & 0x0F00] SHL 1
                        //If the most-significant bit of Vx is 1 then VF is set to 1 other wise 0.
                        //Then Vx is multiplied by 2.
                        break;
                    default:
                        break;

                }
                break;

            case 0x9000:
                if(v_reg[(opcode & 0x0F00) >> 8] != v_reg[(opcode & 0x00F0) >> 4]){
                    PC += 2;
                }
                //skip then next instruction if V[op & 0x0F00] != V[op & 0x00F0]
                break;

            case 0xA000:
                //set I = (opcode & 0x0FFF)
                I = (short)(opcode & 0x0FFF);
                break;

            case 0xB000:
                PC = (short)((opcode & 0x0FFF) + v_reg[0]);
                // PC = (opcode & 0x0FFF) + V0
                break;

            case 0xC000:
                int randomByte = ThreadLocalRandom.current().nextInt(0, 255 + 1);
                // V[opcode & 0x0F00] = (random byte) & (opcode & 0x00FF)
//                System.out.println("Randombyte: " + randomByte);
                v_reg[(opcode & 0x0F00) >> 8] = (short) (randomByte & (opcode & 0x00FF));
                break;

            case 0xD000:
                //Dxyn
                int length = (opcode & 0xF);
                byte[] spriteArray = generateSpriteArrayFromMemory(I, (short)(length));
                short x = (short)((opcode & 0x0F00) >> 8);
                short y = (short)((opcode & 0x00F0) >> 4);
                if(drawSprite(x, y, spriteArray, length)){
                    VF = 1;
                } else {
                    VF = 0;
                }
                break;
            default:
                break;

        }
        return 0;
    }

    private byte[] generateSpriteArrayFromMemory(short address, short length){
        byte[] retArr = new byte[length];
//        for(int i = address; i < address + (length / 2); i++){
//            retArr[i * 2] = (byte)(this.memory[i] >> 8 & 0xFF);
//            retArr[i * 2 + 1] = (byte)(this.memory[i] & 0xFF);
//        }
        for(int i = 0; i < length; i++){
            retArr[i] = (byte)(this.memory[i + address]);
        }
        return retArr;
    }

    private boolean drawSprite(short x, short y, byte[] sprite, int numRows){
        boolean erased = false;
        for(int j = y; j < y + numRows; j++){
            for(int i = x; i < x + 8; i++){
                int currentBit = (sprite[j] >> (7 - (i - x))) & 0x1;
                short xPos = (short)(i % SCREEN_WIDTH);
                short yPos = (short)(j % SCREEN_HEIGHT);
                erased = xorPixel(xPos,yPos,currentBit);

            }
        }
        return erased;
    }

    private boolean xorPixel(short x, short y, int value){

        short oldVal = this.displayArray[x][y];
        displayArray[x][y] = (short) (oldVal ^ value);
        return oldVal > displayArray[x][y];

    }

    public void drawDisplayArray(){
        for(int j = 0; j < SCREEN_HEIGHT; j++){
            for(int i = 0; i < SCREEN_WIDTH; i++){
                System.out.print(displayArray[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    public short getPC(){ return PC; }

    public short[] getV_reg(){ return v_reg; }


    public short getVF(){ return VF; }

    public short getI(){ return I; }

    public void printV_reg(){
        for(int i = 0; i < V_REG_SIZE; i++){
            System.out.println("V_REG[" + i +"]: " + String.format("0x%08X",v_reg[i]));
        }
    }

    public static void main(String[] args){
        String romFile = "pong.rom";
        String fonts = "./resources/FONTS.chip8";
        ROM rom = new ROM(romFile, fonts);
        Chip8 cpu = new Chip8(rom);
        cpu.emulationCycle();
    }
}
