package com.windforce.common.resource.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源读取器持有者
 *
 * @author frank
 */
public class ReaderHolder implements ApplicationContextAware {

    @SuppressWarnings("unused")
    private final static Logger logger = LoggerFactory.getLogger(ReaderHolder.class);
    private ConcurrentHashMap<String, ResourceReader> readers =
            new ConcurrentHashMap<String, ResourceReader>();
    private ApplicationContext applicationContext;

    @PostConstruct
    protected void initialize() {
        for (String name : this.applicationContext.getBeanNamesForType(ResourceReader.class)) {
            ResourceReader reader = this.applicationContext.getBean(name, ResourceReader.class);
            this.register(reader);
        }
    }

    /**
     * 获取指定格式的 {@link ResourceReader}
     *
     * @param format
     * @return
     */
    public ResourceReader getReader(String format) {
        return readers.get(format);
    }

    // 实现 {@link ApplicationContextAware}

    /**
     * 注册指定的 {@link ResourceReader}
     *
     * @param reader
     * @return
     */
    public ResourceReader register(ResourceReader reader) {
        return readers.putIfAbsent(reader.getFormat(), reader);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
