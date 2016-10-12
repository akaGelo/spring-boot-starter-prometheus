package ru.vyukov.prometheus.starter;

public interface PrometeusMetricNameConverter {

	public String convertName(String springName);

}
