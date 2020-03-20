package com.windforce.common.utility.chain;

import com.windforce.common.utility.chain.anno.InNotice;
import com.windforce.common.utility.chain.anno.Processing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 错误情况的测试用例
 *
 * @author frank
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "applicationContext.xml")
@Component
public class ErrorTest {

	@Autowired
	private Dispatcher dispatcher;

	@Processing(name = Names.NAME_FOUR, index = 1, way = Way.BOTH)
	public void test_arrgument_error_one(Notice<Content> notice) {
		Content content = notice.getContent();
		int i = (Integer) content.getValue("one");
		i++;
		content.setValue("one", i);
	}

	/**
	 * 测试参数异常
	 */
	@Test(expected = IllegalStateException.class)
	public void test_arrgument_error_one() {
		Notice<String> notice = Notice.valueOf(Names.NAME_FOUR, "test");
		dispatcher.process(notice);
	}

	@Processing(name = Names.NAME_FIVE, index = 1, way = Way.BOTH)
	public void test_arrgument_error_two(@InNotice Content content) {
		// 不需要做任何事
	}

	/**
	 * 测试参数异常
	 */
	@Test(expected = IllegalStateException.class)
	public void test_arrgument_error_two() {
		Notice<String> notice = Notice.valueOf(Names.NAME_FIVE, "test");
		dispatcher.process(notice);
	}

}
