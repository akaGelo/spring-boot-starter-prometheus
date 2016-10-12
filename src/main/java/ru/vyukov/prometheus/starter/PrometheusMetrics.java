package ru.vyukov.prometheus.starter;

import java.util.Set;

import io.prometheus.client.Collector.MetricFamilySamples;

public class PrometheusMetrics {

	private int status;// 1 == up, 0 = down and unknown
	private Set<MetricFamilySamples> metricFamilySamples;

	public PrometheusMetrics(int status, Set<MetricFamilySamples> metricFamilySamples) {
		this.status = status;
		this.metricFamilySamples = metricFamilySamples;
	}

	public Set<MetricFamilySamples> getMetricFamilySamples() {
		return metricFamilySamples;
	}

	public int getStatus() {
		return status;
	}

	public boolean isUp() {
		return 1 == status;
	}
}
