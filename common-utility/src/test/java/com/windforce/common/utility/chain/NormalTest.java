package com.windforce.common.utility.chain;

import com.windforce.common.utility.chain.anno.InNotice;
import com.windforce.common.utility.chain.anno.Processing;
import com.windforce.common.utility.chain.anno.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * 一般情况的测试用例
 *
 * @author frank
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "applicationContext.xml")
@Component
public class NormalTest {

    @Autowired
    private Dispatcher dispatcher;

    @SuppressWarnings("unchecked")
    @Processing(name = Names.NAME_ONE, index = 1, way = Way.BOTH)
    public boolean test_in_out_step_one(Notice<Content> notice) {
        assertThat(notice.getStep(), is(1));
        assertThat(notice.getWay(), anyOf(is(Way.IN), is(Way.OUT)));
        assertThat(notice.getContent(), notNullValue());
        Content content = notice.getContent();
        int i = (Integer) content.getValue("one");
        i++;
        content.setValue("one", i);
        return true;
    }

    @Processing(name = Names.NAME_ONE, index = 2, way = Way.OUT)
    public boolean test_in_out_step_two(
            @InNotice(type = Type.STEP) int step,
            @InNotice(type = Type.WAY) Way way,
            @InNotice(type = Type.CONTENT) Content content) {
        assertThat(step, is(2));
        assertThat(way, is(Way.OUT));
        assertThat(content, notNullValue());
        int i = (Integer) content.getValue("two");
        i++;
        content.setValue("two", i);
        return true;
    }

    @Processing(name = Names.NAME_ONE, index = 3, way = Way.IN)
    public boolean test_in_out_step_three(
            @InNotice(type = Type.STEP) int step,
            @InNotice(type = Type.WAY) Way way,
            @InNotice(type = Type.CONTENT) Content content) {
        assertThat(step, is(3));
        assertThat(way, is(Way.IN));
        assertThat(content, notNullValue());
        int i = (Integer) content.getValue("three");
        i++;
        content.setValue("three", i);
        return true;
    }

    /**
     * 测试正常的进入和返回
     */
    @Test
    public void test_in_out() {
        Content content = new Content();
        content.setValue("one", 0);
        content.setValue("two", 0);
        content.setValue("three", 0);
        Notice<Content> notice = Notice.valueOf(Names.NAME_ONE, content);
        dispatcher.process(notice);

        assertThat((Integer) content.getValue("one"), equalTo(2));
        assertThat((Integer) content.getValue("two"), equalTo(1));
        assertThat((Integer) content.getValue("three"), equalTo(1));
        assertThat(notice.isInterrupt(), is(false));
    }

}
