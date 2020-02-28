package com.ganguomob.dev.myspringboot.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
class RandomUtilsTests {

    @Test
    void getUuid() {
        System.out.println(RandomUtils.getUuid());
    }
}