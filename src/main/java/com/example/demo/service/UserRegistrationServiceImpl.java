
package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import com.example.demo.dto.SellerDto;
import com.example.demo.service.resilience4j.UserRegistrationResilience4j;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

	Logger logger = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);
	private UserRegistrationResilience4j userRegistrationResilience4j;


	public UserRegistrationServiceImpl(UserRegistrationResilience4j userRegistrationResilience4j) {
		this.userRegistrationResilience4j = userRegistrationResilience4j;
	}

    @Override
    public Mono<SellerDto> registerSeller(SellerDto sellerDto) {

        Mono<SellerDto> registerSeller = null;

        long start = System.currentTimeMillis();

        registerSeller = userRegistrationResilience4j.registerSeller(sellerDto);

        logger.info("add seller call returned in - {}", System.currentTimeMillis() - start);

        return registerSeller;

    }
}
