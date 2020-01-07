
package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SellerDto;
import com.example.demo.service.UserRegistrationService;
import reactor.core.publisher.Mono;

@RestController
public class UserRegistrationController {

	@Autowired
	private UserRegistrationService userRegistrationService;

	@PostMapping("/register/seller")
	public Mono<SellerDto> registerAsSeller(@RequestBody SellerDto sellerDto) {
		return userRegistrationService.registerSeller(sellerDto);
	}
}
