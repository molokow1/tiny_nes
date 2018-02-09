public class Chip8 {

    private ROM rom;

    private short[] memory;
    private static final int MEM_SIZE = 4096;
    private static final int V_REG_SIZE = 16;
    private static final int STACK_SIZE = 16;
    private short[] v_reg;
    private short VF;
    private short I;
    private short PC;

    private short[] stack;
    private short stackPtr;

    private short delay_timer;
    private short sound_timer;

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
            this.memory[i] = rom.getFontContent()[i];
        }
    }

    public void printMemContent(){
        for(int i = 0; i < MEM_SIZE; i++){
            System.out.println(this.memory[i]);
        }
    }

    public void emulationCycle(){
        // Fetch Opcode
        opcode = fetchInstruction();

        // Decode and Execute Opcode
        decodeAndExecute(opcode);

        // Update timers

    }



    private int fetchInstruction(){
        return this.memory[PC] << 8 | this.memory[PC + 1];
    }


    public int decodeAndExecute(int opcode){
        switch(opcode & 0xF000){
            case 0x0000:
                switch(opcode & 0x00FF){
                    case 0x00E0:
                        //Clear the display
                        break;
                    case 0x00EE:
                        //return from a subroutine - set PC to the top of the stack then substracts 1 from stack pointer
                        PC = stack[stackPtr];
                        stackPtr--;
                        break;
                }
                break;
            case 0x1000:
                PC = (short)(opcode & 0x0FFF);
                //jump to location (op & 0x0FFF)
                break;

            case 0x2000:
                stackPtr++;
                stack[stackPtr] = PC;
                PC = (short)(opcode & 0x0FFF);
                //call subroutine at (op & 0x0FFF)
                break;

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
                v_reg[opcode & 0x0F00 >> 8] = (short)(opcode & 0x00FF);
                //set V[op & 0x0F00] = (op & 0x00FF)
                break;

            case 0x7000:
                //set V[op & 0x0F00] = V[op & 0x0F00] + (op & 0x00FF)
                break;

            case 0x8000:
                switch(opcode & 0x000F){
                    case 0x0000:
                        //set V[op & 0x0F00] = V[op & 0x00F0]
                        break;
                    case 0x0001:
                        //set V[op & 0x0F00] = V[op & 0x0F00] || V[op & 0x00F0]
                        break;
                    case 0x0002:
                        //set V[op & 0x0F00] = V[op & 0x0F00] && V[op & 0x00F0]
                        break;
                    case 0x0003:
                        //set V[op & 0x0F00] = V[op & 0x0F00] XOR V[op & 0x00F0]
                        break;
                    case 0x0004:
                        //set V[op & 0x0F00] = V[op & 0x0F00] + V[op & 0x00F0] set VF = carry
                        //if greater than 8 bits then VF = carry, only lowest 8bits are kept.
                        break;
                    case 0x0005:
                        //set V[op & 0x0F00] = V[op & 0x0F00] - V[op & 0x00F0] set VF = borrow
                        //if Vx > Vy then borrow is 1 else 0
                        break;
                    case 0x0006:
                        //set V[op & 0x0F00] = V[op & 0x0F00] SHR 1
                        //if the lest-significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2.
                        break;
                    case 0x0007:
                        //set V[op & 0x0F00] = V[op & 0x00F0] - V[op & 0x0F00]
                        //if Vy > Vx then VF is set to 1 else 0
                        break;
                    case 0x000E:
                        //set V[op & 0x0F00] = V[op & 0x0F00] SHL 1
                        //If the most-significant bit of Vx is 1 then VF is set to 1 other wise 0.
                        //Then Vx is multiplied by 2.
                        break;
                    default:
                        break;

                }
                break;

            case 0x9000:
                //skip then next instruction if V[op & 0x0F00] != V[op & 0x00F0]
                break;

            case 0xA000:
                //set I = (opcode & 0x0FFF)
                break;

            case 0xB000:
                // PC = (opcode & 0x0FFF) + V0
                break;

            case 0xC000:
                // V[opcode & 0x0F00] = (random byte) && (opcode & 0x00FF)
                break;

            case 0xD000:
                break;
            default:
                break;

        }
        return 0;
    }

    public short getPC(){ return PC; }

    public short[] getV_reg(){ return v_reg; }

    public static void main(String[] args){
        String romFile = "pong.rom";
        String fonts = "./resources/FONTS.chip8";
        ROM rom = new ROM(romFile, fonts);
        Chip8 cpu = new Chip8(rom);
        cpu.emulationCycle();
    }
}