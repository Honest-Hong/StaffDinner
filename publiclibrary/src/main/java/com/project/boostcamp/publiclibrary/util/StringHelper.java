package com.project.boostcamp.publiclibrary.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hong Tae Joon on 2017-07-26.
 * 문자열을 손쉽게 조작하는 도구
 */

public class StringHelper {
    /**
     * 문자열의 길이에 따라 끝을 자르는 함수
     * @param str 문자열
     * @param length 최대 길이
     * @return 잘려진 문자열
     */
    public static String cutEnd(String str, int length) {
        if(str.length() > length - 3) {
            return str.substring(0, length - 2) + "...";
        }
        return str;
    }

    /**
     * 문자열의 길이에 따라 앞을 자르는 함수
     * @param str 문자열
     * @param length 최대 길이
     * @return 잘려진 문자열
     */
    public static String cutStart(String str, int length) {
        if(str.length() > length - 3) {
            int startIndex = str.length() - length;
            return "..." + str.substring(startIndex);
        }
        return str;
    }

    /**
     * 문자열이 이메일 형식에 맞는지 확인하는 함수
     * @param email 이메일
     * @return 맞으면 true, 아니면 false
     */
    public static boolean isValidEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    /**
     * 문자열이 이동식 전화번호 형식에 맞는지 확인하는 함수
     * @param cellphoneNumber 전화번호
     * @return 맞으면 true, 아니면 false
     */
    public static boolean isValidCellPhoneNumber(String cellphoneNumber) {
        boolean returnValue = false;
        String regex = "^\\s*(010|011|012|013|014|015|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(cellphoneNumber);
        if (m.matches()) {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * 문자열이 집 전화번호 형식에 맞는지 확인하는 함수
     * @param phoneNumber 전화번호
     * @return 맞으면 true, 아니면 false
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        boolean returnValue = false;
        String regex = "^\\s*(02|031|032|033|041|042|043|051|052|053|054|055|061|062|063|064|070)?(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneNumber);
        if (m.matches()) {
            returnValue = true;
        }
        return returnValue;
    }
}
