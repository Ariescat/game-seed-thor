package com.test.jprotobuf.pojo2proto;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import org.junit.Test;

public class Pojo2Proto {

	@Test
	public void test() {
		ProtobufIDLGenerator.getIDL(Person.class);
	}

	@Test
	public void noAnotiation() {
		// 没有对应的字段注解就不会生成对应IDL字段描述
		ProtobufIDLGenerator.getIDL(Object.class);
	}
}
