
package com.example.demo.service;


import com.example.demo.dto.SellerDto;
import reactor.core.publisher.Mono;

public interface UserRegistrationService {

	Mono<SellerDto> registerSeller(SellerDto sellerDto);
}
