package com.trivecta.zipryde.framework.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.trivecta.zipryde.model.service.BookingService;
import com.trivecta.zipryde.model.service.UserService;
import com.trivecta.zipryde.mongodb.MongoDbClient;

@Configuration
@EnableScheduling
public class MongoScheduler {

	@Autowired
	UserService userService;
	
	@Scheduled(fixedRate = 300000) // 5 Minutes
    public void updateIdleDriverToOffline() {
		try {
			userService.updateIdleDriverToOffline();
		}
		catch(Exception e){
			//
		}
    }
}
