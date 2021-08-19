package com.marcotte.blockhead;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.marcotte.blockhead.config" })
public class BlockheadApplication  //extends JFrame
{
	//private ApplicationPresenter applicationPresenter;

	public static void main(String[] args)
	{
		SpringApplication.run(BlockheadApplication.class, args);
	}

}
