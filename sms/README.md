## 短信模块

### Quick Start
1. 引入对应的模块
2. 在配置文件中添加属性
3. 添加一个SmsGroup枚举类
4. 添加一个Configuration类
5. 在exception_msg.properties中添加`SMS_CODE_NOT_VALID=验证码不正确`

### Properties
#### 公共属性
| 名称 | 说明 | 默认值 |
| ---- | ---- | ------ |
| sms.debug | 是否开启调试模式，调试模式不发送验证码，并且验证码固定为调试的验证码 | |
| sms.debugCode | 调试模式的验证码 | 123456 |
| sms.expire | 验证码过期时间(秒)，推荐为60的倍数 | 300 |
| sms.repeatLimit | 允许再次发送时间间隔(秒) | 3600 |
| sms.code.length | 验证码长度 | 6 |
| sms.code.generator | 指定验证码生成器，可选值有：number, alpha, alpha-number | number |

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

### Configuration
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

### SmsGroup
新建一个`SmsGroup`枚举类来代表短信分组
```
@AllArgsConstructor
@Getter
public enum SmsGroup implements ISmsGroup {

    /** 登录 */
    LOGIN("login", "sms.template.login"),

    ;
    private final String name;
    private final String templateKey;
}
```

### Request
1. 添加SmsCode注解
2. 实现SmsCodeBean接口
```
@Data
@SmsCode("login")
public class LoginRequest implements SmsCodeBean {

    private String countryCode;

    private String phone;

    private String code;
}
```

### SmsController
```
@Api(tags = "02. 短信验证码")
@RestController
@RequestMapping("/api/sms")
@Validated
public class SmsController extends BaseController {

    @Autowired
    private SmsService smsService;

    @ApiOperation(value = "发送验证码", notes = "类型:login(登录)")
    @PutMapping("")
    public void sendCode(@RequestBody @Validated SmsRequest request) {
        smsService.sendCode(SmsGroup.nameOf(request.getType()), request.getCountryCode(), request.getPhone());
    }
}
```

```
@ApiModel
@Data
public class SmsRequest {

    @ApiModelProperty(value = "区号", position = 1, required = true, example = "\"86\"")
    @NotNull(message = "COUNTRY_CODE_CANNOT_BE_BLANK")
    @NotBlank(message = "COUNTRY_CODE_CANNOT_BE_BLANK")
    @CountryCodeValidator
    private String countryCode;

    @ApiModelProperty(value = "手机号", position = 2, required = true, example = "\"13414850000\"")
    @NotNull(message = "PHONE_CANNOT_BE_BLANK")
    @NotBlank(message = "PHONE_CANNOT_BE_BLANK")
    @PhoneValidator
    private String phone;

    @ApiModelProperty(value = "类型", position = 3, example = "login",
            allowableValues = "login", required = true)
    @NotNull(message = "TYPE_CANNOT_BE_BLANK")
    @NotBlank(message = "TYPE_CANNOT_BE_BLANK")
    private String type;
}
```