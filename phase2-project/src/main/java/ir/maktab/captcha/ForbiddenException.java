package ir.maktab.captcha;

public class ForbiddenException extends Throwable {
    public ForbiddenException(String invalid_captcha) {
    }
}
