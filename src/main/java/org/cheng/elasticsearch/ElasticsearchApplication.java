package org.cheng.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ElasticsearchApplication {

    public static void main(final String[] args) throws InterruptedException {
        SpringApplication.run(ElasticsearchApplication.class, args);
//        while(true) {
//        	log.info("图生log----" + System.currentTimeMillis());
//        	Thread.sleep(3000l);
//        }
    }
}