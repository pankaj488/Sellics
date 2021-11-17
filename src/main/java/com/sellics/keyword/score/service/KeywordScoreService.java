package com.sellics.keyword.score.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sellics.keyword.score.amazon.client.AmazonClient;
import com.sellics.keyword.score.model.AmazonResponse;

@Service
public class KeywordScoreService {
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	AmazonClient amazonClient;
	@Autowired
	Environment environment;

	public int calculateScoreForKeyword(String keyword) {
		double score = calculateScore(keyword) * 100;
		return (int) Math.round(score);

	}

	private double calculateScore(String keyword) {
		int slaBreachLimit = Integer.parseInt(environment.getProperty("client.amazon.autocomplete.api.slabreachlimit"));
		String limit = environment.getProperty("client.amazon.autocomplete.api.limit");
		return calculateScoreWithAmazonData(keyword, limit, slaBreachLimit);

	}

	private double calculateScoreWithAmazonData(String keyword, String limit, int slaBreachLimit) {
		int countOfAPICalls = 0;
		long occuranceOfKeywordInAllCalls = 0;
		int substringSize = keyword.length() / slaBreachLimit;
		for (int i = 0; i < keyword.length(); i++) {
			String substring = null;
			if (keyword.length() > slaBreachLimit) {
				int indexIncreaseBy = substringSize * (i + 1);
				substring = keyword.substring(0,
						(keyword.length() < indexIncreaseBy ? keyword.length() : indexIncreaseBy));
				if (keyword.length() < indexIncreaseBy) {
					break;
				}
			} else {
				substring = keyword.substring(0, (keyword.length() == i ? i : i + 1));
			}
			AmazonResponse amazonResponse = amazonAPICall(limit, substring);
			countOfAPICalls++;
			occuranceOfKeywordInAllCalls = occuranceOfKeywordInAllCalls + amazonResponse.getSuggestions().stream()
					.filter(suggestion -> suggestion.getValue().contains(keyword)).count();

		}
		double totalExpectedResults = countOfAPICalls * Integer.parseInt(limit);
		return occuranceOfKeywordInAllCalls / totalExpectedResults;
	}

	private AmazonResponse amazonAPICall(String limit, String substring) {
		AmazonResponse amazonResponse = amazonClient.getAmazonSearchData("amazon-search-ui",
				environment.getProperty("client.amazon.autocomplete.api.mid"),
				environment.getProperty("client.amazon.autocomplete.api.search-alias"), substring, limit);
		return amazonResponse;
	}

}
