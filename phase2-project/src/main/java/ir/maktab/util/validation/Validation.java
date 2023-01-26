package ir.maktab.util.validation;



import ir.maktab.util.exception.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static void checkPassword(String password) throws ValidationException {
        if (!Pattern.matches("^(?=.*[0-9])(?=.*[a-z]).{8}$",
                password))
            throw new ValidationException("This password is not correct");
    }

    public static boolean isValidName(String name) {
        String regex = "^[A-Za-z]\\w{2,29}$";
        Pattern p = Pattern.compile(regex);
        if (name == null) {
            return false;
        }
        Matcher m = p.matcher(name);
        return m.matches();
    }
}
