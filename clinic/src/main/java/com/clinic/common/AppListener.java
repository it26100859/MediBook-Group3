package com.clinic.common;

import com.clinic.admin.AdminDAO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        FileHandler.init(System.getProperty("user.home") + "/clinic-data");
        AdminDAO.seed();
    }
}
