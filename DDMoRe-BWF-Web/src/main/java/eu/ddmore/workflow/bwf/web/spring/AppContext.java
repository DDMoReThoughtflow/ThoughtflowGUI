package eu.ddmore.workflow.bwf.web.spring;

import org.springframework.context.ApplicationContext;

/**
 * This class provides application-wide access to the Spring ApplicationContext.
 * The ApplicationContext is injected by the class "ApplicationContextProvider".
 */  
public class AppContext {

    private static ApplicationContext ctx; 
    
    /**
     * Injected from the class "ApplicationContextProvider" which is automatically
     * loaded during Spring-Initialization.
     */ 
    public static void setApplicationContext(ApplicationContext applicationContext) { 
        ctx = applicationContext; 
    } 
 
    /**
     * Get access to the Spring ApplicationContext from everywhere in your Application.
     */ 
    public static ApplicationContext getApplicationContext() { 
        return ctx; 
    }  
    
    public static <T> T getBean(Class<T> clazz) { 
        return ctx.getBean(clazz); 
    }

    @SuppressWarnings("unchecked")
	public static <T> T getBean(String name, Class<T> clazz) { 
        return (T)ctx.getBean(name); 
    }

    public static Object getBean(String name) { 
        return ctx.getBean(name); 
    }
}
