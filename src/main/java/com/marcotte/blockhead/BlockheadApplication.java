package com.marcotte.blockhead;

import com.marcotte.blockhead.gui.ApplicationPresenter;
import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.gui.SwingMainFrame;
import com.marcotte.blockhead.gui.util.LookAndFeelUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication(scanBasePackages = { "com.marcotte.blockhead.config" })
public class BlockheadApplication
{
	public static boolean springBoot = true; //false;

	public static void main(String[] args)
	{
		if (springBoot) {
			SpringApplication.run(BlockheadApplication.class, args);
		} else {
		LookAndFeelUtils.setWindowsLookAndFeel();
			ConfigurableApplicationContext context = createApplicationContext(args);
			ApplicationServicesBean applicationServicesBean = context.getBean(ApplicationServicesBean.class);
			displayMainFrame(applicationServicesBean);
		}
	}

	private static ConfigurableApplicationContext createApplicationContext(String[] args) {
		return new SpringApplicationBuilder(BlockheadApplication.class)
				.headless(false)
				.run(args);
	}

	private static void displayMainFrame(ApplicationServicesBean applicationServicesBean) {
		ApplicationPresenter applicationPresenter = new ApplicationPresenter(applicationServicesBean);
		SwingMainFrame swingMainFrame = new SwingMainFrame(applicationPresenter);
		SwingUtilities.invokeLater(() -> {
			swingMainFrame.prepareAndOpenFrame();
		});
	}

}
