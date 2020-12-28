package xyz.itihub.sercurity.validate;


public enum ValidateCodeType {
    /**
     * 短信验证码
	 */
    SMS {
        @Override
        public String getParamNameOnValidate() {
            return "sms_code";
        }
    },
    /**
     * 图片验证码
     */
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return "image_code";
        }
    };

    /**
     * 校验时从请求中获取的参数的名字
     * @return
     */
    public abstract String getParamNameOnValidate();
}
