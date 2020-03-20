package com.windforce.common.event.test;

import com.windforce.common.event.anno.ReceiverAnno;
import org.springframework.stereotype.Component;

@Component
public class TestEventReceiver {

	@ReceiverAnno
	public void receive(TestEvent event) {
		System.out.println(TestEventReceiver.class.getSimpleName() + " "
			+ Thread.currentThread().getName() + " " + event.getClass());
	}

}
