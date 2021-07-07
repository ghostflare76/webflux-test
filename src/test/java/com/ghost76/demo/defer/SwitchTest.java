package com.ghost76.demo.defer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class SwitchTest {

	@Test
	void test() {
		StepVerifier.create(justResult()).expectNext("foo").verifyComplete();
		StepVerifier.create(deferResult()).expectNext("foo").verifyComplete();
	}

	private Mono<String> justResult() {
		return Mono.just("foo").switchIfEmpty(Mono.just("bar"));
	}
	
	private Mono<String> deferResult() {
		return Mono.just("foo").switchIfEmpty(Mono.defer(() -> Mono.just("bar")));
	}

}
