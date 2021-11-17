package com.sellics.keyword.score.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sellics.keyword.score.amazon.client.AmazonClient;
import com.sellics.keyword.score.model.AmazonResponse;

/**
 * @author Pankaj Singh
 *
 */
@Service
public class KeywordScoreService {
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	AmazonClient amazonClient;
	@Autowired
	Environment environment;

	/**
	 * The method is returning actual keyword score to controller class.
	 * 
	 * The subsequent calls going from this method is giving results which needs to
	 * be converted in range to 0 to 100 So a multiplication with 100 is done.
	 * Double value returned is rounded off to an integer and returned.
	 * 
	 * @param keyword
	 * @return
	 */
	public int calculateScoreForKeyword(String keyword) {
		double score = calculateScore(keyword) * 100;
		return (int) Math.round(score);

	}

	/**
	 * This method is reading properties from application.properties file and
	 * passing their values for score calculation.
	 * 
	 * @param keyword
	 * @return
	 */
	private double calculateScore(String keyword) {
		int slaBreachLimit = Integer.parseInt(environment.getProperty("client.amazon.autocomplete.api.slabreachlimit"));
		String limit = environment.getProperty("client.amazon.autocomplete.api.limit");
		return calculateScoreWithAmazonData(keyword, limit, slaBreachLimit);

	}

	/**
	 * This method is calculating score of the keyword. Parameter <code>limit<code>
	 * is used to limit records from Amazon in API call. Value is set to 10 as
	 * mentioned in assignment.
	 * 
	 * Parameter <code>slaBreachLimit<code> is used to limit API calls to
	 * Amazon.Value is set to 15 as SLA of 10 seconds start to breach if more than
	 * 15 API calls.
	 * 
	 * If length of any keyword is less than <code>slaBreachLimit<code> then Amazon
	 * API is called for each character in the keyword. If length of keyword is more
	 * than <code>slaBreachLimit<code> then <code>substring<code> of more than one
	 * character would be taken from keyword to make API calls with these
	 * Substrings.
	 * 
	 * As soon as <code>substring<code> becomes same as <code>keyword<code> then
	 * loop break and score is calculated.
	 * 
	 * For both the scenarios score would be calculated on basis of Sum of total
	 * keyword found in all API calls divided by sum of all records returned by all
	 * API calls.
	 * 
	 * 
	 * @param keyword
	 * @param limit
	 * @param slaBreachLimit
	 * @return
	 */
	private double calculateScoreWithAmazonData(String keyword, String limit, int slaBreachLimit) {
		int countOfAPICalls = 0;
		long occuranceOfKeywordInAllCalls = 0;
		int substringSize = keyword.length() / slaBreachLimit;
		for (int i = 0; i < keyword.length(); i++) {
			String substring = null;
			if (keyword.length() > slaBreachLimit) {
				int indexIncreaseBy = (substringSize + 1) * (i + 1);
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

	/**
	 * This method is used to call Amazon API and return AmazonResponse POJO with
	 * the results.
	 * 
	 * @param limit
	 * @param substring
	 * @return {@link AmazonResponse}
	 */
	private AmazonResponse amazonAPICall(String limit, String substring) {
		AmazonResponse amazonResponse = amazonClient.getAmazonSearchData("amazon-search-ui",
				environment.getProperty("client.amazon.autocomplete.api.mid"),
				environment.getProperty("client.amazon.autocomplete.api.search-alias"), substring, limit);
		return amazonResponse;
	}

}
