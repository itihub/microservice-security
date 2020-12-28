package xyz.itihub.sercurity.validate.sms;


import xyz.itihub.sercurity.validate.ValidateCode;

public class SmsCode extends ValidateCode {

    public SmsCode(String code, int expireIn) {
        super(code, expireIn);
    }
}
