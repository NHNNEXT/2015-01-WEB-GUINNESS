package org.nhnnext.guinness;

import java.io.File;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerLauncher {
	private static final Logger logger = LoggerFactory.getLogger(WebServerLauncher.class);
	
	public static void main(String[] args) throws Exception {
		String webappDirLocation = "webapp/";
		Tomcat tomcat = new Tomcat();
		String webPort = System.getenv("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8080";
		}
		tomcat.setPort(Integer.valueOf(webPort));
		tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		logger.debug("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath()); 
		tomcat.start();
		tomcat.getServer().await();
	}
}