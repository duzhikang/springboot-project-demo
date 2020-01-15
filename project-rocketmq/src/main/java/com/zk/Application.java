package com.zk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringBootApplication包括三个注解：
 * 1. @EnableAutoConfiguration：SpringBoot根据应用所声明的依赖来对Spring框架进行自动配置。简单概括一下就是，是借助@Import的帮助，
 * 将所有符合自动配置条件的bean定义加载到IoC容器。
 * 2. @ComponentScan：组件扫描，可自动发现和装配Bean，功能其实就是自动扫描并加载符合条件的组件或者bean定义，最终将这些bean定义加载到
 * IoC容器中。
 * 3.
 */
@RestController
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/test")
	public String test() {
		return "application";
	}

}
