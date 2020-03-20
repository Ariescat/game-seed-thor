package com.windforce.common.utility;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * {@link SelectRandom}的单元测试
 *
 * @author kuanghao
 */
public class SelectRandomTest {

    @Test
    public void test_run() {
        SelectRandom<Integer> selects = new SelectRandom<Integer>();
        for (int i = 0; i < 100; i++) {
            selects.addElement(1, 1);
        }
        for (int i = 0; i < 10; i++) {
            selects.run();
        }
        assertThat(selects.run(Integer.MAX_VALUE).size(), is(100));
    }

    @Test
    public void test_runNumber() {
        SelectRandom<Integer> selects = new SelectRandom<Integer>();
        int count = 3000;
        int runCount = 2888;
        for (int i = 0; i < count; i++) {
            selects.addElement(1, 1);
        }
        for (int i = 0; i < runCount; i++) {
            selects.run(1);
        }
        assertThat(selects.run(Integer.MAX_VALUE).size(), is(count - runCount));
    }
}
