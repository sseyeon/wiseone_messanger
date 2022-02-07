package com.messanger.engine.uc.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactoryUtil {
    private static ApplicationContext context 
    	= new ClassPathXmlApplicationContext("com/messanger/engine/uc/config/ServerContext.xml");;
    
    /**
     * 
     * @param id
     * @return
     */
    public static Object getBean(String id) {
        return context.getBean(id);
    }
}
