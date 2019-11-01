## 短信模块

### 步骤
1. 引入对应的(阿里云、腾讯云、容联云通讯)模块
2. 在配置文件中添加属性
3. 添加SmsGroup枚举类
4. 添加Configuration类
5. 添加SendCodeRequest类
6. 添加SmsController类
7. 在exception_msg.properties中添加键值
8. 在Request中校验验证码

### Properties
#### 公共属性
| 名称 | 说明 | 默认值 |
| ---- | ---- | ------ |
| sms.debug | 是否开启调试模式，调试模式不发送验证码，并且验证码固定为调试的验证码 | |
| sms.namespace | Redis键命名空间 | |
| sms.debugCode | 调试模式的验证码 | 123456 |
| sms.expire | 验证码过期时间(秒)，推荐为60的倍数 | 300 |
| sms.repeatLimit | 允许再次发送时间间隔(秒) | 3600 |
| sms.code.length | 验证码长度 | 6 |
| sms.code.generator | 指定验证码生成器，可选值有：number, alpha, alpha-number | number | 
| sms.countryCode.default | 默认的国家区号 | 86 |
| sms.countryCode.allows | 支持的区号列表，格式[国家名称]_[国家区号]，如中国_86,澳洲_61，空值代表无限制 | 空值 |

#### 阿里云
| 名称 | 说明 | 默认值 |
| ---- | ---- | ------ |
| sms.aliyun.accessKey | 阿里云AccessKey | | 
| sms.aliyun.accessSecret | 阿里云AccessSecret | | 
| sms.signName | 短信签名 | | 
| sms.template.param.code | 短信模板验证码参数名 | code | 
| sms.template.param.expire | 短信模板验证码过期时间(分钟)参数名 | expire | 

#### 腾讯云
| 名称 | 说明 | 默认值 |
| ---- | ---- | ------ | 
| sms.qcloud.appId | 腾讯云appId | | 
| sms.qcloud.appKey | 腾讯云appKey | | 
| sms.signName | 短信签名 | | 

#### 容联云通讯
| 名称 | 说明 | 默认值 |
| ---- | ---- | ------ | 
| sms.yuntongxun.sid | 账号SID | |
| sms.yuntongxun.token | 认证TOKEN | |
| sms.yuntongxun.appId | 应用ID | |
| sms.yuntongxun.baseUrl | Rest URL | https://app.cloopen.com:8883 |

### SmsGroup
新建一个`SmsGroup`枚举类来代表短信分组
- name: 分组名称
- templateKey: 分组对应的模板的在properties文件中的键名
```
@AllArgsConstructor
@Getter
public enum SmsGroup implements ISmsGroup {

    /** 登录 */
    LOGIN(Names.LOGIN, "sms.template.login"),

    ;

    private final String name;
    private final String templateKey;

    public interface Names {
        String LOGIN = "login";
    }
}
```

### SmsConfiguration
```
@Configuration
public class SmsConfiguration {

    /**
     * 注入短信分组类型
     */
    @Bean(name = "smsGroupClass")
    public Class<SmsGroup> smsGroup() {
        return SmsGroup.class;
    }
}
```

### SmsController
```
@Api(tags = "短信")
@RestController
@RequestMapping("/api/sms")
@Validated
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private CountryCodeService countryCodeService;

    @ApiOperation("发送短信验证码")
    @PostMapping("/send")
    public String sendCode(@Validated SendCodeRequest request) throws SendSmsFailException {
        return smsService.sendCode(ISmsGroup.ofName(request.getType(), SmsGroup.class),
                request.getCountryCode(), request.getPhone());
    }

    @ApiOperation("获取支持的国家区号列表")
    @GetMapping("/allowed-country-code")
    public List<CountryCodeResponse> getAllowedCountryCodes() {
        return countryCodeService.getAllowedCountryCodes();
    }
}
```

### SendCodeRequest
```
@ApiModel
@Data
@PhoneValid
public class SendCodeRequest {

    @ApiModelProperty(value = "国家区号，不带加号", position = 1, required = true, example = "86")
    @CountryCodeValidator
    private String countryCode;

    @ApiModelProperty(value = "手机号", position = 2, required = true, example = "13412347890")
    private String phone;

    @ApiModelProperty(value = "类型:login(登录)", position = 3, required = true, example = "login",
            allowableValues = "login")
    private String type;
}
```

### exception_msg.properties
在exception_msg.properties和ExceptionMsg.class中添加以下键值
```
SMS_CODE_NOT_VALID=验证码不正确
COUNTRY_CODE_FORMAT_IS_INCORRECT=国家区号格式不正确
NOT_ALLOW_COUNTRY_CODE=不是允许的国家区号
COUNTRY_CODE_CANNOT_BE_EMPTY=国家区号不能为空
PHONE_CANNOT_BE_EMPTY=手机号不能为空
PHONE_FORMAT_IS_INCORRECT=手机号格式不正确
```

### 在Request中校验验证码
添加SmsCodeValid注解
```
@Data
@SmsCodeValid(SmsGroup.Names.LOGIN)
public class LoginRequest {

    private String countryCode;

    private String phone;

    private String code;
}
```