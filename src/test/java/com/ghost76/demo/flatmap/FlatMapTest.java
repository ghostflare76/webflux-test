package com.ghost76.demo.flatmap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FlatMapTest {
	
	public Flux<String> processOrFallback(Mono<String> source, Publisher<String> fallback) {
		return source.flatMapMany(phrase -> Flux.fromArray(phrase.split("\\s+")))
				.switchIfEmpty(fallback);		
	}
	
	
	private Mono<List<String>> monoOfList() {
		List<String> list = Arrays.asList("one","two","three","four","five");
		return Mono.just(list);
	}
	
	private Mono<List<String>> monoEmptyList() {
		return Mono.just(Collections.emptyList());
	}
	
	
	@Test
	void testSplit() {
		StepVerifier.create(processOrFallback(Mono.just("just a phrase with tabs"),Mono.just("EMPTY")))
		.expectNext("just","a","phrase","with","tabs")
		.verifyComplete();
	}
	
	@Test
	void whenEmptyList_ThenMonoDeferNotExecuted() {
		Mono<List<String>> emptyList = monoEmptyList();
		Flux<String> fluxList = emptyList.flatMapIterable( l -> l)
				.switchIfEmpty(Mono.just("Empty")).log();
		
		StepVerifier.create(fluxList).expectNext("Empty").verifyComplete();
				
	}
	

	@Test
	void MonoProducesListOfElements() {
		Mono<List<String>> monoList = monoOfList();
		StepVerifier.create(monoToFluxUsingFlatMapMany(monoList))
		.expectNext("one","two","three","four","five")
		.verifyComplete();		
		
		StepVerifier.create(monoToFluxUsingFlatMapIterable(monoList))
		.expectNext("one","two","three","four","five")
		.verifyComplete();		
		
	}
	
	private <T> Flux <T> monoToFluxUsingFlatMapMany(Mono<List<T>> monoList) {
		return monoList.flatMapMany(Flux::fromIterable)
				.log();
	}
	
	private <T> Flux <T> monoToFluxUsingFlatMapIterable(Mono<List<T>> monoList) {
		return monoList.flatMapIterable(l -> l)
				.log();
	}
}
