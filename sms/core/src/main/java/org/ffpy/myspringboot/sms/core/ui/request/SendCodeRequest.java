package org.ffpy.myspringboot.sms.core.ui.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.ffpy.myspringboot.sms.core.validator.CountryCodeValid;
import org.ffpy.myspringboot.sms.core.validator.PhoneValid;

/**
 * 发送短信Request
 *
 * @author wenlongsheng
 */
@ApiModel
@Data
@PhoneValid
public class SendCodeRequest {

    @ApiModelProperty(value = "国家区号，不带加号", position = 1, required = true, example = "\"86\"")
    @CountryCodeValid(emptyAble = true)
    private String countryCode;

    @ApiModelProperty(value = "手机号", position = 2, required = true, example = "\"13412347890\"")
    private String phone;

    @ApiModelProperty(value = "分组:login(登录)", position = 3, required = true, example = "login",
            allowableValues = "login")
    private String group;
}
