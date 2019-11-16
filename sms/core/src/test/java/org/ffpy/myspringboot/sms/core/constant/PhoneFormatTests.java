package org.ffpy.myspringboot.sms.core.constant;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PhoneFormatTests {

    @Test
    public void valid() {
        Assertions.assertThat(PhoneFormat.CHINA.valid("13414850894")).isTrue();
        Assertions.assertThat(PhoneFormat.CHINA.valid("8613414850894")).isFalse();
        Assertions.assertThat(PhoneFormat.CHINA.valid("134148508941")).isFalse();
    }

    @Test
    public void validWithCountryCode() {
        Assertions.assertThat(PhoneFormat.CHINA.validWithCountryCode("13414850894")).isTrue();
        Assertions.assertThat(PhoneFormat.CHINA.validWithCountryCode("8613414850894")).isTrue();
        Assertions.assertThat(PhoneFormat.CHINA.validWithCountryCode("+8613414850894")).isTrue();
        Assertions.assertThat(PhoneFormat.CHINA.validWithCountryCode("134148508941")).isFalse();
        Assertions.assertThat(PhoneFormat.CHINA.validWithCountryCode("86134148508941")).isFalse();
        Assertions.assertThat(PhoneFormat.CHINA.validWithCountryCode("+86134148508941")).isFalse();
    }
}