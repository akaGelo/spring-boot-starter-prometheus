package ru.vyukov.prometheus.starter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultPrometeusMetricNameConverterTest {

	@Test
	public void testConvertName() throws Exception {
		DefaultPrometeusMetricNameConverter converter = new DefaultPrometeusMetricNameConverter();
		String result = converter.convertName("mem");
		assertEquals("mem_bytes", result);
	}

}
