package ru.vyukov.prometheus.starter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.context.properties.ConfigurationProperties;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Collector.Type;

/**
 * @MetricsEndpoint in prometheus format
 */
@ConfigurationProperties(prefix = "endpoints.prometheus")
public class PrometheusEndpoint extends AbstractEndpoint<PrometheusMetrics> {

	private static final List<String> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<String>());


	private Collection<PublicMetrics> publicMetrics;

	private PrometeusMetricNameConverter prometeusMetricNameConverter;

	private Map<String, HealthIndicator> healthIndicators;

	private HealthAggregator healthAggregator;

	public PrometheusEndpoint(Collection<PublicMetrics> publicMetrics,
			PrometeusMetricNameConverter prometeusMetricNameConverter, Map<String, HealthIndicator> healthIndicators,
			HealthAggregator healthAggregator) {
		super("prometheus", false, true);
		this.publicMetrics = publicMetrics;
		this.prometeusMetricNameConverter = prometeusMetricNameConverter;
		this.healthIndicators = healthIndicators;
		this.healthAggregator = healthAggregator;

	}

	@Override
	public PrometheusMetrics invoke() {
		Set<MetricFamilySamples> mfs = new LinkedHashSet<MetricFamilySamples>();

		for (PublicMetrics publicMetric : publicMetrics) {
			for (Metric<?> metric : publicMetric.metrics()) {
				addSingleMetric(metric, mfs);
			}
		}

		// indicators
		Map<String, Health> healths = new LinkedHashMap<String, Health>();
		for (Map.Entry<String, HealthIndicator> entry : this.healthIndicators.entrySet()) {
			healths.put(formatKey(entry.getKey()), entry.getValue().health());
		}

		Health health = healthAggregator.aggregate(healths);
		Status status = health.getStatus();

		int prometheusStatus = toPrometheusStatus(status);

		PrometheusMetrics prometheusMetrics = new PrometheusMetrics(prometheusStatus, mfs);

		return prometheusMetrics;
	}

	private int toPrometheusStatus(Status status) {
		return status.getCode().equals("UP") ? 1 : 0;
	}

	private void addSingleMetric(Metric<?> metric, Set<MetricFamilySamples> mfs) {
		String springName = metric.getName();
		String prometheusName = prometeusMetricNameConverter.convertName(springName);
		double value = metric.getValue().doubleValue();

		// Spring counter is not prometheus counter because spring
		// counter implements the decrement method

		NameEqualsMetricFamilySamples metricFamilySamples = new NameEqualsMetricFamilySamples(prometheusName,
				Type.GAUGE, prometheusName, Collections
						.singletonList(new MetricFamilySamples.Sample(prometheusName, EMPTY_LIST, EMPTY_LIST, value)));
		mfs.add(metricFamilySamples);
	}

	static class NameEqualsMetricFamilySamples extends MetricFamilySamples {

		public NameEqualsMetricFamilySamples(String name, Type type, String help, List<Sample> samples) {
			super(name, type, help, samples);
		}

		@Override
		public int hashCode() {
			return this.name.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof MetricFamilySamples) {
				String otherName = ((MetricFamilySamples) obj).name;
				return this.name.equals(otherName);
			}
			return super.equals(obj);
		}
	}

	private String formatKey(String name) {
		int index = name.toLowerCase().indexOf("healthindicator");
		if (index > 0) {
			return name.substring(0, index);
		}
		return name;
	}
}
