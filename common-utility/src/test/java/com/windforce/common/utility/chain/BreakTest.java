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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * 打断的测试用例
 *
 * @author frank
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "applicationContext.xml")
@Component
public class BreakTest {

    @Autowired
    private Dispatcher dispatcher;

    @Processing(name = Names.NAME_TWO, index = 1, way = Way.BOTH)
    public void test_break_in_step_one(Notice<Content> notice) {
        Content content = notice.getContent();
        int i = (Integer) content.getValue("one");
        i++;
        content.setValue("one", i);
    }

    @Processing(name = Names.NAME_TWO, index = 2, way = Way.BOTH)
    public boolean test_break_in_step_two(
            @InNotice(type = Type.CONTENT) Content content) {
        int i = (Integer) content.getValue("two");
        i++;
        content.setValue("two", i);
        return false;
    }

    @Processing(name = Names.NAME_TWO, index = 3, way = Way.IN)
    public boolean test_break_in_step_three(
            @InNotice(type = Type.CONTENT) Content content) {
        int i = (Integer) content.getValue("three");
        i++;
        content.setValue("three", i);
        return true;
    }

    /**
     * 测试在进入时打断
     */
    @Test
    public void test_break_in() {
        Content content = new Content();
        content.setValue("one", 0);
        content.setValue("two", 0);
        content.setValue("three", 0);
        Notice<Content> notice = Notice.valueOf(Names.NAME_TWO, content);
        dispatcher.process(notice);

        assertThat((Integer) content.getValue("one"), equalTo(1));
        assertThat((Integer) content.getValue("two"), equalTo(1));
        assertThat((Integer) content.getValue("three"), equalTo(0));

        assertThat(notice.isInterrupt(), is(true));
        assertThat(notice.getWay(), is(Way.IN));
        assertThat(notice.getStep(), is(2));
    }

    @Processing(name = Names.NAME_THREE, index = 1, way = Way.BOTH)
    public void test_break_out_step_one(Notice<Content> notice) {
        Content content = notice.getContent();
        int i = (Integer) content.getValue("one");
        i++;
        content.setValue("one", i);
    }

    @Processing(name = Names.NAME_THREE, index = 2, way = Way.OUT)
    public boolean test_break_out_step_two(
            @InNotice(type = Type.CONTENT) Content content) {
        int i = (Integer) content.getValue("two");
        i++;
        content.setValue("two", i);
        return false;
    }

    @Processing(name = Names.NAME_THREE, index = 3, way = Way.IN)
    public boolean test_break_out_step_three(
            @InNotice(type = Type.CONTENT) Content content) {
        int i = (Integer) content.getValue("three");
        i++;
        content.setValue("three", i);
        return true;
    }

    /**
     * 测试在返回时打断
     */
    @Test
    public void test_break_out() {
        Content content = new Content();
        content.setValue("one", 0);
        content.setValue("two", 0);
        content.setValue("three", 0);
        Notice<Content> notice = Notice.valueOf(Names.NAME_THREE, content);
        dispatcher.process(notice);

        assertThat((Integer) content.getValue("one"), equalTo(1));
        assertThat((Integer) content.getValue("two"), equalTo(1));
        assertThat((Integer) content.getValue("three"), equalTo(1));

        assertThat(notice.isInterrupt(), is(true));
        assertThat(notice.getWay(), is(Way.OUT));
        assertThat(notice.getStep(), is(2));
    }

}
