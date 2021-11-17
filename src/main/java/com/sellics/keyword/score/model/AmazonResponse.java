package com.sellics.keyword.score.model;

import java.util.List;

/**
 * @author Pankaj Singh
 *
 */
public class AmazonResponse {

	private String alias;
	private String prefix;
	private String suffix;
	private List<AmazonSuggestions> suggestions;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public List<AmazonSuggestions> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<AmazonSuggestions> suggestions) {
		this.suggestions = suggestions;
	}

}
