package view.logic;

import java.io.UnsupportedEncodingException;

public class Encryption {

    public static String[] encryption(String[] primordialText, int step, boolean text) throws UnsupportedEncodingException {
        String[] resultText = new String[primordialText.length];
        int st,bt = 0;
        if (!text) {
            byte[] arr;

            for (int i = 0; i < primordialText.length; i++) {
                arr = primordialText[i].getBytes("UTF-8");

                for (int j = 0; j < arr.length; j++)
                    if (arr[j] > -47) {
                        arr[j] += step;
                    } else {
                        arr[++j] += step;
                        if ((arr[j - 1] == -48) && (arr[j] > -65)) {
                            arr[j - 1] = -47;
                            arr[j] = (byte) (-128 - (-64 - arr[j]));
                        }
                        if((arr[j - 1] == -47 && (arr[j] > 0))){
                            arr[j - 1] = -48;
                            arr[j] = (byte) (-65 + (-127 + arr[j]));
                        }
                    }
                resultText[i] = new String(arr, "utf8");
            }
        } else {
            char[] arr;

            for (int i = 0; i < primordialText.length; i++) {
                st = step;
                arr = primordialText[i].toCharArray();

                for (int j = 0; j < arr.length; j++){
                    //РУССКИЙ ЯЗЫК
                    if(arr[j] >= 1072 && arr[j] <= 1103){
                        while (step > 31){
                            st = step - 32;
                        }
                        arr[j] += st;
                        if (arr[j] > 1103){
                            arr[j] = (char) (1072 + arr[j] - 1104);
                        }else if(arr[j] < 1072){
                            arr[j] = (char) (1103 + arr[j] - 1071);
                        }
                    }
                    if(arr[j] >= 1040 && arr[j] <= 1071) {
                        while (step > 31) {
                            st = step - 32;
                        }
                        arr[j] += st;
                        if (arr[j] > 1071) {
                            arr[j] = (char) (1040 + arr[j] - 1072);
                        } else if (arr[j] < 1040) {
                            arr[j] = (char) (1071 + arr[j] - 1039);
                        }
                    }
                    //АНГЛИЙСКИЙ ЯЗЫК
                    if(arr[j] >= 97 && arr[j] <= 122){
                        while (step > 25){
                            st = step - 26;
                        }
                        arr[j] += st;
                        if (arr[j] > 122){
                            arr[j] = (char) (97 + arr[j] - 123);
                        }else if(arr[j] < 97){
                            arr[j] = (char) (122 + arr[j] - 96);
                        }
                    }
                    if(arr[j] >= 65 && arr[j] <= 90) {
                        while (step > 25) {
                            st = step - 26;
                        }
                        arr[j] += st;
                        if (arr[j] > 90) {
                            arr[j] = (char) (65 + arr[j] - 91);
                        } else if (arr[j] < 1040) {
                            arr[j] = (char) (90 + arr[j] - 64);
                        }
                    }
                    //ЦИФРЫ
                    if(arr[j] >= 48 && arr[j] <= 57){
                        while (step > 9){
                            st = step - 10;
                        }
                        arr[j] += st;
                        if (arr[j] > 57){
                            arr[j] = (char) (48 + arr[j] - 58);
                        }else if(arr[j] < 48){
                            arr[j] = (char) (57 + arr[j] - 47);
                        }
                    }
                }

                resultText[i] = new String(arr);
            }
        }

        return resultText.clone();
    }

    public static String encryption(String primordialText, int step, boolean text) throws UnsupportedEncodingException {
        String resultStr = new String();
        int st = 0;
        if (!text) {
            byte[] arr;

            arr = primordialText.getBytes("UTF-8");

            for (int j = 0; j < arr.length; j++)
                if (arr[j] > -47) {
                    arr[j] += step;
                } else {
                    arr[++j] += step;
                    if ((arr[j - 1] == -48) && (arr[j] > -65)) {
                        arr[j - 1] = -47;
                        arr[j] = (byte) (-128 - (-64 - arr[j]));
                    }
                    if((arr[j - 1] == -47 && (arr[j] > 0))){
                        arr[j - 1] = -48;
                        arr[j] = (byte) (-65 + (-127 + arr[j]));
                    }
                }
                resultStr = new String(arr, "utf8");
        } else {
            char[] arr;

            st = step;
            arr = primordialText.toCharArray();

            for (int j = 0; j < arr.length; j++){
                //РУССКИЙ ЯЗЫК
                if(arr[j] >= 1072 && arr[j] <= 1103){
                    while (step > 31){
                        st = step - 32;
                        step -= 32;
                    }
                    arr[j] += st;
                    if (arr[j] > 1103){
                        arr[j] = (char) (1072 + arr[j] - 1104);
                    }else if(arr[j] < 1072){
                        arr[j] = (char) (1103 + arr[j] - 1071);
                    }
                }
                if(arr[j] >= 1040 && arr[j] <= 1071) {
                    while (step > 31) {
                        st = step - 32;
                        step -= 32;
                    }
                    arr[j] += st;
                    if (arr[j] > 1071) {
                        arr[j] = (char) (1040 + arr[j] - 1072);
                    } else if (arr[j] < 1040) {
                        arr[j] = (char) (1071 + arr[j] - 1039);
                    }
                }
                //АНГЛИЙСКИЙ ЯЗЫК
                if(arr[j] >= 97 && arr[j] <= 122){
                    while (step > 25){
                        st = step - 26;
                        step -= 26;
                    }
                    arr[j] += st;
                    if (arr[j] > 122){
                        arr[j] = (char) (97 + arr[j] - 123);
                    }else if(arr[j] < 97){
                        arr[j] = (char) (122 + arr[j] - 96);
                    }
                }
                if(arr[j] >= 65 && arr[j] <= 90) {
                    while (step > 25) {
                        st = step - 26;
                        step -= 26;
                    }
                    arr[j] += st;
                    if (arr[j] > 90) {
                        arr[j] = (char) (65 + arr[j] - 91);
                    } else if (arr[j] < 1040) {
                        arr[j] = (char) (90 + arr[j] - 64);
                    }
                }
                //ЦИФРЫ
                if(arr[j] >= 48 && arr[j] <= 57){
                    while (step > 9){
                        st = step - 10;
                    }
                    arr[j] += st;
                    if (arr[j] > 57){
                        arr[j] = (char) (48 + arr[j] - 58);
                    }else if(arr[j] < 48){
                        arr[j] = (char) (57 + arr[j] - 47);
                    }
                }
            }
            resultStr = new String(arr);
        }

        return resultStr;
    }
}