package serviceImpl;

import service.ExecuteService;

import java.rmi.RemoteException;


public class ExecuteImpl implements ExecuteService {
    @Override
    public String bfExecute(String code, String param) throws RemoteException {
        // TODO Auto-generated method stub
        return bfInterprete(code, param);
    }
    private String ookInterprete(String code, String param) {
        code = code.replaceAll(" ","");//删去所有的空格
        code = code.replaceAll("[\\t\\n\\r]",""); //删去所有空白符
        StringBuilder translate2Bf = new StringBuilder();
        int maxUnit = code.length() / 8;
        for(int i = 0; i < maxUnit; i++) {
            switch (code.substring(i*8,(i+1)*8)) {
                case "Ook.Ook?": translate2Bf.append('>');break;
                case "Ook?Ook.": translate2Bf.append('<');break;
                case "Ook.Ook.": translate2Bf.append('+');break;
                case "Ook!Ook!": translate2Bf.append('-');break;
                case "Ook!Ook.": translate2Bf.append('.');break;
                case "Ook.Ook!": translate2Bf.append(',');break;
                case "Ook!Ook?": translate2Bf.append('[');break;
                case "Ook?Ook!": translate2Bf.append(']');
            }
        }
        return bfInterprete(translate2Bf.toString(),param);
    }
    private String bfInterprete(String code, String param) {
        int pointer = 0;
        char[] memory = new char[5000];
        char[] codeArr = code.toCharArray();
        StringBuilder result = new StringBuilder();
        int readIndex = 0;
        for (int index = 0; index < code.length(); index++) {
            char c = codeArr[index];
            switch (c) {
                case '>': pointer++;break;
                case '<': pointer--;break;
                case '+': memory[pointer]++;break;
                case '-': memory[pointer]--;break;
                case ',': memory[pointer] = param.charAt(readIndex++);break;
                case '.': result.append(memory[pointer]);break;
                case '[':
                    //计数法寻找对应的‘]’
                    if (memory[pointer] == 0){
                        int count = 1;
                        while (count > 0) {
                            index++;
                            if (code.charAt(index) == '[')
                                count++;
                            else if (code.charAt(index) == ']')
                                count--;
                        }
                    }
                    break;
                case ']':
                    //计数法寻找对应的‘[’
                    if (memory[pointer] != 0) {
                        int count = 1;
                        while (count > 0) {
                            index--;
                            if (code.charAt(index) == '[')
                                count--;
                            else if (code.charAt(index) == ']')
                                count++;
                        }
                        index--;
                    }
            }
        }
        return result.toString();
    }
    @Override
    public String ookExecute(String code, String param) throws RemoteException {
        // TODO Auto-generated method stub
        return ookInterprete(code, param);

    }


}