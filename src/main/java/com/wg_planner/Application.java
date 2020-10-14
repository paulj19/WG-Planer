package com.wg_planner;

import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.entity.ResidentAccount;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext(
//                "beans.xml");
//        ApplicationContext contextx = new AnnotationConfigApplicationContext(ResidentAccount.class);
//        ResidentAccountService residentAccountService = contextx.getBean(ResidentAccountService.class);
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(restDataSource());
//        sessionFactory.setPackagesToScan(
//                new String[] { "com.wg_planner.backend.entity.ResidentAccount" });
//        sessionFactory.setAnnotatedClasses(new Class[] { ResidentAccount.class });


        SpringApplication.run(Application.class, args);
    }

}
