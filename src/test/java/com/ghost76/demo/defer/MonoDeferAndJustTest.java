package com.ghost76.demo.defer;

import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoDeferAndJustTest {
	int a = 5;


	@Test
	void lazyTest() {

		Mono<Integer> monoJust = Mono.just(a);
		Mono<Integer> monoDefer = Mono.defer(() -> Mono.just(a));

		monoJust.subscribe(integer -> log.info("{}", integer));
		monoDefer.subscribe(integer -> log.info("{}", integer));

		a = 7;
		monoJust.subscribe(integer -> log.info("{}", integer));
		monoDefer.subscribe(integer -> log.info("{}", integer));
	}
	/**
	 * Just (Hot Publisher) 
	 * 해당값을 즉시 방출합니다. 처음 방출된 값을 cache 해놓고 다음구독자에게 해당 값을 방출합니다.
	 * Subscribe(구독) 하지 않아도 데이터를 방출합니다
	 */
	@Test
	void justTest() {
		Mono<Double> monoJust = Mono.just(Math.random());
		
		System.out.println("Just >>>>>>>>>>>>>");
		monoJust.subscribe(System.out::println);
		monoJust.subscribe(System.out::println);
		monoJust.subscribe(System.out::println);
		
		
	}
	

	/**
	 * Defer (Cold Publisher) 
	 * 데이터의 방출을 구독전까지 지연시킵니다. 
	 * 구독을 해야 데이터를 방출한다. (Subscribe) 
	 * Exception을 호출하여 처리가 가능. cf ) Mono.just()는 exception 처리 불가
	 */
	
	@Test
	void deferTest() {
		Mono<Double> monoDefer = Mono.defer(()-> Mono.just(Math.random()));
		System.out.println("Defer >>>>>>>>>>>>>");
		monoDefer.subscribe(System.out::println);
		monoDefer.subscribe(System.out::println);
		monoDefer.subscribe(System.out::println);
	}

	@Test
	void makeError() {
		Mono<String> monoDefer = Mono.defer(() -> {
			try {
				String res = someError();
				return Mono.just("Hello");
			} catch (Exception ex) {
				return Mono.error(ex);
			}
		});

		monoDefer.subscribe(str -> log.info("{}", str));
	}

	public String someError() throws Exception {
		throw new RuntimeException("에러 발생!!");
	}



}
