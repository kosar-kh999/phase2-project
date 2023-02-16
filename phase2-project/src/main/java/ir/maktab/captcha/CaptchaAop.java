package ir.maktab.captcha;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class CaptchaAop {

    @Autowired
    ValidateCaptcha service;

    @Around("@annotation(ir.maktab.captcha.RequiresCaptcha)")
    public Object validateCaptchaResponse(final ProceedingJoinPoint point)
            throws Throwable {
        final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final String captchaResponse = request.getHeader("captcha-response");
        final boolean isValidCaptcha = service.validateCaptcha(captchaResponse);
        if (!isValidCaptcha) {
            log.info("Throwing forbidden exception as the captcha is invalid.");
            throw new ForbiddenException("INVALID_CAPTCHA");
        }
        return point.proceed();
    }
}
