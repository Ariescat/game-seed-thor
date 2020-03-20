package com.windforce.common.resource;

import com.windforce.common.resource.other.ResourceDefinition;
import com.windforce.common.resource.reader.ReaderHolder;
import com.windforce.common.resource.reader.ResourceReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;

/**
 * 资源管理器工厂
 *
 * @author frank
 */
public class StorageManagerFactory implements FactoryBean<StorageManager>, ApplicationContextAware {

	/**
	 * 资源定义列表
	 */
	private List<ResourceDefinition> definitions;
	private ApplicationContext applicationContext;

	public void setDefinitions(List<ResourceDefinition> definitions) {
		this.definitions = definitions;
	}

	// 实现接口的方法

	@Override
	public StorageManager getObject() throws Exception {
		StorageManager result = this.applicationContext.getAutowireCapableBeanFactory()
			.createBean(StorageManager.class);
		ExecutorService service = Executors.newFixedThreadPool(definitions.size());
		CompletionService<ResourceDefinition> completionService = new ExecutorCompletionService<ResourceDefinition>(
			service);
		ReaderHolder readerHolder = applicationContext.getBean(ReaderHolder.class);

		for (ResourceDefinition definition : definitions) {
			completionService.submit(new Callable<ResourceDefinition>() {
				public ResourceDefinition call() throws Exception {
					ResourceReader reader = readerHolder.getReader(definition.getFormat());
					Resource resource = applicationContext.getResource(definition.getLocation());
					InputStream input = resource.getInputStream();
					List<?> startList = reader.read(input, definition.getClz());
					definition.setStartList(startList);
					return definition;
				}
			});
		}

		for (int i = 0; i < definitions.size(); i++) {
			completionService.take();
		}
		service.shutdown();

		for (ResourceDefinition definition : definitions) {
			result.initialize(definition);
		}

		return result;
	}

	@Override
	public Class<StorageManager> getObjectType() {
		return StorageManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
