package com.windforce.common.utility;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * {@link ByteUtils}的单元测试
 *
 * @author frank
 */
public class ByteUtilsTest {

    /**
     * 测试 int 相关的转换方法(1)
     */
    @Test
    public void test_int_1() {
        int number = 1;
        byte[] array = ByteUtils.intToByte(number);
        assertThat(array[0], is((byte) 0));
        assertThat(array[1], is((byte) 0));
        assertThat(array[2], is((byte) 0));
        assertThat(array[3], is((byte) 1));

        int result = ByteUtils.intFromByte(array);
        assertThat(result, is(number));

        byte[] target = {127, 127, 127, 127, 127};
        target = ByteUtils.intToByte(number, target, 1);
        assertThat(target[0], is((byte) 127));
        assertThat(target[1], is((byte) 0));
        assertThat(target[2], is((byte) 0));
        assertThat(target[3], is((byte) 0));
        assertThat(target[4], is((byte) 1));

        result = ByteUtils.intFromByte(target, 1);
        assertThat(result, is(number));
    }

    /**
     * 测试 int 相关的转换方法(2)
     */
    @Test
    public void test_int_2() {
        int number = 16777216; // {1,0,0,0}
        byte[] array = ByteUtils.intToByte(number);
        assertThat(array[0], is((byte) 1));
        assertThat(array[1], is((byte) 0));
        assertThat(array[2], is((byte) 0));
        assertThat(array[3], is((byte) 0));

        int result = ByteUtils.intFromByte(array);
        assertThat(result, is(number));

        byte[] target = {127, 127, 127, 127, 127};
        target = ByteUtils.intToByte(number, target, 1);
        assertThat(target[0], is((byte) 127));
        assertThat(target[1], is((byte) 1));
        assertThat(target[2], is((byte) 0));
        assertThat(target[3], is((byte) 0));
        assertThat(target[4], is((byte) 0));

        result = ByteUtils.intFromByte(target, 1);
        assertThat(result, is(number));
    }

    /**
     * 测试 long 相关的转换方法(1)
     */
    @Test
    public void test_long_1() {
        long number = 1;
        byte[] array = ByteUtils.longToByte(number);
        assertThat(array[0], is((byte) 0));
        assertThat(array[1], is((byte) 0));
        assertThat(array[2], is((byte) 0));
        assertThat(array[3], is((byte) 0));
        assertThat(array[4], is((byte) 0));
        assertThat(array[5], is((byte) 0));
        assertThat(array[6], is((byte) 0));
        assertThat(array[7], is((byte) 1));

        long result = ByteUtils.longFromByte(array);
        assertThat(result, is(number));

        byte[] target = {127, 127, 127, 127, 127, 127, 127, 127, 127};
        target = ByteUtils.longToByte(number, target, 1);
        assertThat(target[0], is((byte) 127));
        assertThat(target[1], is((byte) 0));
        assertThat(target[2], is((byte) 0));
        assertThat(target[3], is((byte) 0));
        assertThat(target[4], is((byte) 0));
        assertThat(target[5], is((byte) 0));
        assertThat(target[6], is((byte) 0));
        assertThat(target[7], is((byte) 0));
        assertThat(target[8], is((byte) 1));

        result = ByteUtils.longFromByte(target, 1);
        assertThat(result, is(number));
    }

    /**
     * 测试 long 相关的转换方法(2)
     */
    @Test
    public void test_long_2() {
        long number = 72057594037927936L; // {1,0,0,0,0,0,0,0}
        byte[] array = ByteUtils.longToByte(number);
        assertThat(array[0], is((byte) 1));
        assertThat(array[1], is((byte) 0));
        assertThat(array[2], is((byte) 0));
        assertThat(array[3], is((byte) 0));
        assertThat(array[4], is((byte) 0));
        assertThat(array[5], is((byte) 0));
        assertThat(array[6], is((byte) 0));
        assertThat(array[7], is((byte) 0));

        long result = ByteUtils.longFromByte(array);
        assertThat(result, is(number));

        byte[] target = {127, 127, 127, 127, 127, 127, 127, 127, 127};
        target = ByteUtils.longToByte(number, target, 1);
        assertThat(target[0], is((byte) 127));
        assertThat(target[1], is((byte) 1));
        assertThat(target[2], is((byte) 0));
        assertThat(target[3], is((byte) 0));
        assertThat(target[4], is((byte) 0));
        assertThat(target[5], is((byte) 0));
        assertThat(target[6], is((byte) 0));
        assertThat(target[7], is((byte) 0));
        assertThat(target[8], is((byte) 0));

        result = ByteUtils.longFromByte(target, 1);
        assertThat(result, is(number));
    }

}
