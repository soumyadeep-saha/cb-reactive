
package com.example.demo.service.resilience4j;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import reactor.core.publisher.Mono;

import com.example.demo.dto.SellerDto;

@Service
public class UserRegistrationResilience4j {
	Logger logger = LoggerFactory.getLogger(UserRegistrationResilience4j.class);

	WebClient webClient = WebClient.create("http://localhost:8081");

    //@RateLimiter(name = "service1", fallbackMethod = "rateLimiterfallback")
    @Retry(name = "retryService1", fallbackMethod = "retryfallback")
    @CircuitBreaker(name = "service1", fallbackMethod = "fallbackForRegisterSeller")
    //@Bulkhead(name = "bulkheadService1", fallbackMethod = "bulkHeadFallback")
    public Mono<SellerDto> registerSeller(SellerDto sellerDto) {
        logger.info("inside registerSeller(SellerDto sellerDto)");
        return this.webClient.get()
                .uri("/external/employee")
                .retrieve()
                .bodyToMono(SellerDto.class);
    }

    @CircuitBreaker(name = "service2", fallbackMethod = "fallbackForGetSeller")
    public List<SellerDto> getSellersList() {
        logger.info("calling getSellerList()");
        return (List<SellerDto>) webClient.get().uri("/sellersList").retrieve().bodyToFlux(List.class);

    }
    public String rateLimiterfallback(SellerDto sellerDto, Throwable t) {
        logger.error("Inside rateLimiterfallback, cause - {}", t.toString());
        return "Inside rateLimiterfallback method. Some error occurred while calling service for seller registration";
    }
    public String bulkHeadFallback(SellerDto sellerDto, Throwable t) {
        logger.error("Inside bulkHeadFallback, cause - {}", t.toString());
        return "Inside bulkHeadFallback method. Some error occurred while calling service for seller registration";
    }

    public Mono<SellerDto>  retryfallback(SellerDto sellerDto, Throwable t) {
        logger.error("Inside retryfallback, cause - {}", t.toString());
        return Mono.just(sellerDto);
    }

    public Mono<SellerDto> fallbackForRegisterSeller(SellerDto sellerDto, CallNotPermittedException e) {
        logger.error("Inside circuit breaker fallbackForRegisterSeller, cause - {}", e.toString());
        return Mono.just(sellerDto);
    }

    public List<SellerDto> fallbackForGetSeller(Throwable t) {
        logger.error("Inside fallbackForGetSeller, cause - {}", t.toString());
        SellerDto sd = new SellerDto();
        sd.setFirstName("john");
        sd.setId(1111);
        sd.setEmailId("default");
        List<SellerDto> defaultList = new ArrayList<>();
        defaultList.add(sd);
        return defaultList;
    }
}
