import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Solution {
    public static void main(String[] args) throws IOException {
        setAssemblyOpcodes();
        setOpcodes();
        initialize();
        bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Answer)));
        bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(symbolTable)));
        String s;

        // -------FIRST PASS------- //
        while(line <= totalLines) {
            s = instructionAddress_Instruction.get(line-1);
            if(s.contains(":")) {
                int index = s.indexOf(":");
                s = s.substring(index+2);
            }
            useOpCode(s);
            line += 1;
        }

        // -------SYMBOL TABLE------- //
        bw2.write("SYMBOL TABLE");
        bw2.newLine();
        bw2.write("-----------------");
        bw2.newLine();
        bw2.write("LABELS");
        bw2.newLine();
        for(int i = 0; i<labels.size(); i++) {
            if(labelLinks.containsKey(labels.get(i))) {
                bw2.write(labels.get(i) + " - " + decToBinary(labelLinks.get(labels.get(i))));
            }else {
                bw2.write(labels.get(i) + " - ");
            }
            bw2.newLine();
        }
        bw2.write("-----------------");
        bw2.newLine();
        bw2.write("VARIABLES");
        bw2.newLine();
        for(int i = 0; i < Variables.size(); i++) {
            if(variablesWCodes.containsKey(Variables.get(i))) {
                bw2.write(Variables.get(i) + " - " + variablesWCodes.get(Variables.get(i)));
            }else {
                String code = generateRandomAddress();
                bw2.write(Variables.get(i) + " - " + code);
            }
            bw2.newLine();
        }

        if(opcodes.get(opcodes.size()-1) != "110000000000") {
            errors.add("Program Execution hasn't been stopped");
        }

        // -------SECOND PASS------- //

        if(errors.size()>0) {
            bw1.write("ERRORS found");
            bw1.newLine();
            for(int i = 0; i<errors.size(); i++) {
                bw1.write(errors.get(i));
                bw1.newLine();
            }
        }else {
            for(int i = 0; i<opcodes.size(); i++) {
                bw1.write(opcodes.get(i));
                bw1.newLine();
            }
        }


        bw1.close();
        bw2.close();

    }
    public static ArrayList<String> mnemonics = new ArrayList<>();
    public static HashMap<Integer, String> instructionAddress_Instruction = new HashMap<>();
    public static ArrayList<String> Variables = new ArrayList<>();
    public static HashMap<String, String> variablesWCodes = new HashMap<>(); // varName - binary String
    public static ArrayList<String> labels = new ArrayList<>();
    public static HashMap<String, Integer> labelLinks = new HashMap<>();
    public static HashMap<String, String> instruction_opcode = new HashMap<>();
    public static HashMap<String, Integer> inputs = new HashMap<>();
    public static int accumulator = 0;
    public static int line = 1;
    public static int totalLines = 0;
    public static double register1 = 0;
    public static double register2 = 0;
    public static File Answer = new File("MachineCode.txt");
    public static File symbolTable = new File("SymbolTable.txt");
    public static BufferedWriter bw1;
    public static BufferedWriter bw2;
    public static ArrayList<String> AddressesUsed = new ArrayList<>(); // Binary
    public static ArrayList<String> intAddressesUsed = new ArrayList<>(); // Integer
    public static ArrayList<String> errors = new ArrayList<>();
    public static ArrayList<String> opcodes = new ArrayList<String>();
    public static void setAssemblyOpcodes(){
        mnemonics.add("CLA");
        mnemonics.add("LAC");
        mnemonics.add("SAC");
        mnemonics.add("ADD");
        mnemonics.add("SUB");
        mnemonics.add("BRZ");
        mnemonics.add("BRN");
        mnemonics.add("BRP");
        mnemonics.add("INP");
        mnemonics.add("DSP");
        mnemonics.add("MUL");
        mnemonics.add("DIV");
        mnemonics.add("STP");
    }
    public static void setOpcodes(){
        instruction_opcode.put("DSP","1001");
        instruction_opcode.put("ADD","0011");
        instruction_opcode.put("SUB","0100");
        instruction_opcode.put("BRP","0111");
        instruction_opcode.put("MUL","1010");
        instruction_opcode.put("INP","1000");
        instruction_opcode.put("BRZ","0101");
        instruction_opcode.put("STP","1100");
        instruction_opcode.put("LAC","0001");
        instruction_opcode.put("DIV","1011");
        instruction_opcode.put("SAC","0010");
        instruction_opcode.put("CLA","0000");
        instruction_opcode.put("BRN","0110");
    }
    public static void initialize() throws IOException{
        String inputLine;
        BufferedReader br  = new BufferedReader(new FileReader("Input.txt"));
        int i = 0;
        while((inputLine = br.readLine()) != null) {
            if(inputLine.contains(":")) {
                int index = inputLine.indexOf(":");
                String link = inputLine.substring(0,index);
                labelLinks.put(link, i);
                labels.add(link);
            }
            instructionAddress_Instruction.put(i, inputLine);
            i++;
        }
        int z=0;
        while(z<totalLines) {
            intAddressesUsed.add(Integer.toString(z));
            z++;
        }
        totalLines = i;
    }
    public static String decToBinary(int n) {
        int i = 0;
        int Number = n;
        int[] binaryNum = new int[32];
        String binaryString = "";
        for(int k=Number;k>0;k=k/2) {
            binaryNum[i] = k % 2;
            i++;
        }
        int j = i-1;
        while(j>=0) {
            binaryString += Integer.toString(binaryNum[j]);
            j--;
        }
        while(binaryString.length() != 8) {
            binaryString += "0";
        }
        String binstr=binaryString;
        String bString = binstr;
        return bString;
    }
    public static String generateRandomAddress() {
        Random Rand = new Random();
        int x;
        int y;
        int h = 157;
        y = h;
        x = y;
        String ranAddress;
        String interMAdd = decToBinary(x);
        ranAddress = interMAdd;
        while(AddressesUsed.contains(ranAddress)) {
            x = Rand.nextInt(256);
            ranAddress = decToBinary(x);
        }
        String gR_Add;
        gR_Add = ranAddress;
        return gR_Add;
    }
    public static boolean isNumeric(String strNum) {
        try {
            float f;
            int i;
            double d;
            d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            int g;
            double l;
            return false;
        }
        return true;
    }
    public static void addOpcode(String assemblyOP, String address) {
        if(!assemblyOP.equals("BRP") && !assemblyOP.equals("BRN") && !assemblyOP.equals("STP") && !assemblyOP.equals("BRZ") && !assemblyOP.equals("CLA")) {
            if(!isNumeric(address)) {
                opcodes.add(instruction_opcode.get(assemblyOP) + variablesWCodes.get(address));
            }else {
                opcodes.add(instruction_opcode.get(assemblyOP) + address);
            }
        }
    }
    public static void useOpCode(String s) throws IOException {
        String assemblyOP = "empty";
        String address = "empty";
        if(s.length() > 3) {
            assemblyOP = s.substring(0,3);
            address = s.substring(4);
        }else {
            assemblyOP = s;
        }
        if(assemblyOP.equals("CLA")) {
            // clear accumulator
            accumulator = 0;
            opcodes.add("000000000000");
        }
        else if(assemblyOP.equals("LAC")) {
            // Load into accumulator from the address
            if(address.contains(" ")) {
                errors.add("Invalid syntax for address in line " + line);
            }else if(!isNumeric(address)) {
                if(variablesWCodes.containsKey(address)) {
                    accumulator = inputs.get(variablesWCodes.get(address));
                    addOpcode(assemblyOP, address);
                }else {
                    Variables.add(address);
                    errors.add("Variable used in line " + line + " " + s + " hasnt been declared");
                }
            }else if(inputs.containsKey(decToBinary(Integer.valueOf(address)))) {
                address = decToBinary(Integer.valueOf(address));
                accumulator = inputs.get(address);
                addOpcode(assemblyOP, address);
            }else {
                errors.add("Invalid Address in line " + line + " " + s);
            }
        }
        else if(assemblyOP.equals("SAC")) {
            // store accumulator contents to address
            if(address.contains(" ")) {
                errors.add("Invalid syntax for address in line " + line);
            }else if(!isNumeric(address)) {
                if(variablesWCodes.containsKey(address)) {
                    address = variablesWCodes.get(address);
                }else {
                    String addressNum = generateRandomAddress();
                    variablesWCodes.put(address, addressNum);
                    Variables.add(address);
                    address = addressNum;
                }
            }
            inputs.put(address, accumulator);
            addOpcode(assemblyOP, address);
        }
        else if(assemblyOP.equals("ADD")) {
            // add address contents to the accumulator
            if(address.contains(" ")) {
                errors.add("Invalid syntax for address in line " + line);
            }else if(isNumeric(address)) {
                address = decToBinary(Integer.valueOf(address));
                if(inputs.containsKey(address)) {
                    accumulator += inputs.get(address);
                    addOpcode(assemblyOP, address);
                }else {
                    errors.add("Invalid Address in line " + line + " " + s);
                }
            }else {
                if(variablesWCodes.containsKey(address)) {
                    accumulator += inputs.get(variablesWCodes.get(address));
                    addOpcode(assemblyOP, address);
                }else {
                    Variables.add(address);
                    errors.add("Invalid Variable in line " + line + " " + s);
                }
            }
        }
        else if(assemblyOP.equals("SUB")) {
            if(address.contains(" ")) {
                errors.add("Invalid syntax for address in line " + line);
            }else if(isNumeric(address)) {
                address = decToBinary(Integer.valueOf(address));
                if(inputs.containsKey(address)) {
                    accumulator -= inputs.get(address);
                    addOpcode(assemblyOP, address);
                }else {
                    errors.add("Invalid Address in line " + line + " " + s);
                }
            }else {
                if(variablesWCodes.containsKey(address)) {
                    accumulator -= inputs.get(variablesWCodes.get(address));
                    addOpcode(assemblyOP, address);
                }else {
                    Variables.add(address);
                    errors.add("Invalid Variable in line " + line + " " + s);
                }
            }
        }
        else if(assemblyOP.equals("BRZ") || assemblyOP.equals("BRN") || assemblyOP.equals("BRP")) {
            if(address.contains(" ")) {
                errors.add("Invalid syntax for label in line " + line);
            }else if(labelLinks.containsKey(address)) {
                String link = decToBinary(labelLinks.get(address));
                opcodes.add(instruction_opcode.get(assemblyOP) + link);
            }else {
                if(!labels.contains(address)) {
                    labels.add(address);
                }
                errors.add("Label in line " + line + " " + s + " isn't assigned a line in the code ");
            }
        }
        else if(assemblyOP.equals("INP")) {
            String addressNum;
            if(address.contains(" ")) {
                errors.add("Invalid syntax for address in line " + line);
            }else if(!isNumeric(address)) {
                if(!variablesWCodes.containsKey(address)) {
                    addressNum = generateRandomAddress();
                    variablesWCodes.put(address, addressNum);
                    Variables.add(address);
                    address = addressNum;
                    intAddressesUsed.add(Integer.toString(Integer.parseInt(address, 2)));
                }
            }else {
                intAddressesUsed.add(address);
                address = decToBinary(Integer.valueOf(address));
            }
            inputs.put(address, 0);
            if(!AddressesUsed.contains(address)) {
                AddressesUsed.add(address);
            }
            addOpcode(assemblyOP, address);
        }
        else if(assemblyOP.equals("DSP")) {
            if(address.contains(" ")) {
                errors.add("Invalid syntax for address in line " + line);
            }else if(isNumeric(address)) {
                address = decToBinary(Integer.valueOf(address));
                if(inputs.containsKey(address)) {
                    addOpcode(assemblyOP, address);
                }else {
                    errors.add("Invalid Address in line " + line + " " + s);
                }
            }else {
                if(variablesWCodes.containsKey(address)) {
                    addOpcode(assemblyOP, address);
                }else {
                    Variables.add(address);
                    errors.add("Invalid Variable in line " + line + " " + s);
                }
            }
        }
        else if(assemblyOP.equals("MUL")) {
            if(address.contains(" ")) {
                errors.add("Invalid syntax for address in line " + line);
            }else if(isNumeric(address)) {
                address = decToBinary(Integer.valueOf(address));
                if(inputs.containsKey(address)) {
                    accumulator *= inputs.get(address);
                    addOpcode(assemblyOP, address);
                }else {
                    errors.add("Invalid Address in line " + line + " " + s);
                }
            }else {
                if(variablesWCodes.containsKey(address)) {
                    accumulator *= inputs.get(variablesWCodes.get(address));
                    addOpcode(assemblyOP, address);
                }else {
                    Variables.add(address);
                    errors.add("Invalid Variable in line " + line + " " + s);
                }
            }
        }
        else if(assemblyOP.equals("DIV")) {
            if(address.contains(" ")) {
                errors.add("Invalid syntax for address in line " + line);
            }else if(isNumeric(address)) {
                if(inputs.containsKey(address)) {
                    if(inputs.get(address) == 0) {
                        errors.add("Division by zero error in line " + line + " " + s);
                    }else {
                        register1 = accumulator / inputs.get(address); // Resistor
                        register2 = accumulator % inputs.get(address); // Resistor
                        addOpcode(assemblyOP, address);
                    }
                }
                else {
                    errors.add("Invalid Address in line " + line + " " + s);
                }
            }else {
                if(variablesWCodes.containsKey(address)) {
                    address = variablesWCodes.get(address);
                    if(inputs.containsKey(address)) {
                        if(inputs.get(address) == 0) {
                            errors.add("Division by zero error in line " + line + " " + s);
                        }else {
                            register1 = accumulator / inputs.get(address); // Resistor
                            register2 = accumulator % inputs.get(address); // Resistor
                            addOpcode(assemblyOP, address);
                        }
                    }
                }else {
                    Variables.add(address);
                    errors.add("Invalid Variable in line " + line + " " + s);
                }
            }
        }
        else if(s.equals("STP")) {
            if(address.equals(" ")) {
                errors.add("Invalid Syntax in line " + line);
            }else {
                opcodes.add("110000000000");
            }
        }else if(s.contains("//")) {
        }else {
            errors.add("Invalid Syntax in line " + line);
        }

    }


}