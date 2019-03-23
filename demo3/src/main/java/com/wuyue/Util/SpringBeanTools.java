package com.wuyue.Util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述:保存上下文context
 */
@Configuration
public class SpringBeanTools implements ApplicationContextAware {
    private static ApplicationContext applicationContext;


    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static Object getBean(Class classname) {
        try {
          return applicationContext.getBean(classname);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 说明： 通过bean别名获取bean，bean别名写在对应的configure对象里
     */
    public static Object getBeanByName(String beanName) {
        try {
            return applicationContext.getBean(beanName);
        } catch (Exception e) {
            return null;
        }
    }


}
