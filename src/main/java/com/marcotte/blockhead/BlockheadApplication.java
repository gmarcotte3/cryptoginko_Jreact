package com.marcotte.blockhead;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(scanBasePackages = { "com.marcotte.blockhead" })
@SpringBootApplication(scanBasePackages = { "com.marcotte.blockhead.config" })
//@SpringBootApplication
public class BlockheadApplication
{
	public static void main(String[] args)
	{
		System.out.println("We Be Here ----------------------------------------------------------------------------------------------");
		SpringApplication.run(BlockheadApplication.class, args);
	}

}
