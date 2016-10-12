package ru.vyukov.prometheus.starter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.HypermediaDisabled;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.prometheus.client.exporter.common.TextFormat;

/**
 * Adapter to expose {@link PrometheusEndpoint} as an {@link MvcEndpoint}.
 *
 */
@ConfigurationProperties(prefix = "endpoints.prometheus")
public class PrometheusMvcEndpoint extends EndpointMvcAdapter {

	private static final Logger log = LoggerFactory.getLogger(PrometheusMvcEndpoint.class);

	public PrometheusMvcEndpoint(PrometheusEndpoint delegate) {
		super(delegate);
	}

	@Override
	@GetMapping(produces = TextFormat.CONTENT_TYPE_004)
	@HypermediaDisabled
	@ResponseBody
	public ResponseEntity<String> invoke() {
		PrometheusMetrics prometheusMetrics = (PrometheusMetrics) super.invoke();

		Writer writer = new StringWriter();
		try {
			TextFormat.write004(writer, Collections.enumeration(prometheusMetrics.getMetricFamilySamples()));
		} catch (IOException e) {
			log.error("metric write error", e);
		}

		HttpStatus status = prometheusMetrics.isUp() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

		String body = writer.toString();
		ResponseEntity<String> response = new ResponseEntity<>(body, status);
		return response;
	}

}
