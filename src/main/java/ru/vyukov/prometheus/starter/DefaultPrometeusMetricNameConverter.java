package ru.vyukov.prometheus.starter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.prometheus.client.Collector;

/**
 * info
 * https://github.com/spring-projects/spring-boot/blob/v1.3.2.RELEASE/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/endpoint/SystemPublicMetrics.java
 * 
 * @author gelo
 *
 */
public class DefaultPrometeusMetricNameConverter implements PrometeusMetricNameConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPrometeusMetricNameConverter.class);

	private ConcurrentMap<String, String> prometheusMetricNames = new ConcurrentHashMap<>();

	private Map<String, String> typeSuffixs = new LinkedHashMap<>();

	public DefaultPrometeusMetricNameConverter() {
		
		setName("uptime", "uptime_millis");
		setName("instance.uptime", "uptime_millis");
		setName("gc.ps_marksweep.time", "gc_ps_marksweep_millis");
		
		addSuffixIf("mem", "bytes");
		addSuffixIf("heap", "bytes");
		addSuffixIf("nonheap", "bytes");
		addSuffixIf("thread", "pcs");
	}

	public DefaultPrometeusMetricNameConverter(boolean disableDefaultDictionary) {
	}

	private void setName(String springName, String prometheusName) {
		prometheusMetricNames.put(springName, prometheusName);
	}

	/**
	 * addSuffixIf("mem","bytes") <br/>
	 * 
	 * "mem" = "mem_bytes" "mem.free" = "mem_free_bytes" "mem.free" =
	 * "mem_free_bytes" "other.mem.free" = "other_mem_free"
	 * 
	 * @param startWith
	 * @param suffix
	 */
	private void addSuffixIf(String startWith, String suffix) {
		typeSuffixs.put(startWith, suffix);
	}

	@Override
	public String convertName(String springName) {
		String prometheusName = prometheusMetricNames.get(springName);
		if (null == prometheusName) {
			prometheusName = Collector.sanitizeMetricName(springName);

			Optional<String> suffixKey = typeSuffixs.keySet().stream()
					.filter(startWith -> springName.startsWith(startWith)).findFirst();
			if (suffixKey.isPresent()) {
				String type = typeSuffixs.get(suffixKey.get());
				prometheusName += "_" + type;
				debug("Generated [" + prometheusName + "] by suffix [" + suffixKey.get() + "]");
			} else {
				debug("No preset prometeus name for [" + springName + "]. Generated [" + prometheusName + "]");
			}

			prometheusMetricNames.put(springName, prometheusName);

		}
		return prometheusName;
	}

	private void debug(String string) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(string);
		}
	}

}
