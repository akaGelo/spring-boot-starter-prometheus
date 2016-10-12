package ru.vyukov.prometheus.starter;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrometheusEndpointConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Autowired
	public PrometheusEndpoint prometheusEndpoint(Collection<PublicMetrics> publicMetrics,
			PrometeusMetricNameConverter prometeusMetricNameConverter, Map<String, HealthIndicator> healthIndicators,
			HealthAggregator healthAggregator) {

		return new PrometheusEndpoint(publicMetrics, prometeusMetricNameConverter, healthIndicators, healthAggregator);
	}

	@Bean
	public PrometheusMvcEndpoint prometheusMvcEndpoint(PrometheusEndpoint prometheusEndpoint) {
		return new PrometheusMvcEndpoint(prometheusEndpoint);
	}

	@Bean
	public PrometeusMetricNameConverter prometeusMetricNameConverter() {
		return new DefaultPrometeusMetricNameConverter();
	}

}
