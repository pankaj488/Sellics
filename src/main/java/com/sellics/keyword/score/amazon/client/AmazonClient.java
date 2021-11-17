package com.sellics.keyword.score.amazon.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sellics.keyword.score.model.AmazonResponse;

/**
 * @author Pankaj Singh
 *
 */
@FeignClient(name = "amazon-api", url = "${client.amazon.autocomplete.api}")
public interface AmazonClient {
	@GetMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
	AmazonResponse getAmazonSearchData(@RequestParam("client-info") String clientinfo, @RequestParam("mid") String mid,
			@RequestParam("alias") String alias, @RequestParam("prefix") String prefix,
			@RequestParam("limit") String limit);

}
