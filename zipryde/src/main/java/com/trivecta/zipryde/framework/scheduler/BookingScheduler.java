package com.trivecta.zipryde.framework.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.trivecta.zipryde.model.service.BookingService;

@Configuration
@EnableScheduling
//@Component
public class BookingScheduler {

	@Autowired
	BookingService bookingService;
	
	@Scheduled(fixedRate = 300000) // 5 Minutes
    public void updateBookinStatusUnAnswered() {
		try {
			bookingService.updateBookinStatusUnAnswered();
		}
		catch(Exception e){
			//
		}
    }
}
