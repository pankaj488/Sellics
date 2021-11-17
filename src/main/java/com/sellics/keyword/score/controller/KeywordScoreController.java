package com.sellics.keyword.score.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sellics.keyword.score.model.KeywordScore;
import com.sellics.keyword.score.service.KeywordScoreService;

/**
 * @author Pankaj Singh
 *
 */
@RestController
@RequestMapping("/estimate")
public class KeywordScoreController {
	@Autowired
	KeywordScoreService keywordScoreService;

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public KeywordScore calculateKeywordScore(@RequestParam("keyword") @NonNull String keyword) {
		KeywordScore keywordScore = new KeywordScore();
		keywordScore.setKeyword(keyword);
		keywordScore.setScore(keywordScoreService.calculateScoreForKeyword(keyword.toLowerCase()));
		return keywordScore;

	}

}
