# spring-boot-starter-prometheus


#Quick start

1. dependency (maven central  coming soon)
```xml
	<repositories>
		<repository>
			<id>spring-boot-starter-prometheus</id>
			<url>https://raw.github.com/akaGelo/spring-boot-starter-prometheus/mvn-repo/</url>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
	</repositories>
	
 ...
	
	<dependencies>
 ...
		<dependency>
			<groupId>ru.vyukov</groupId>
			<artifactId>spring-boot-starter-prometheus</artifactId>
			<version>0.1</version>
		</dependency>
...
</dependencies>
```

2. Add annotation to enable **@EnablePrometheus**



```java
@EnableFeignClients
@SpringBootApplication
public class SpringBootApplication {
....
}

```


## Russian
Это реализация metrics endpoint для Spring Boot Actuator в текстовом формате применяемом в Prometheus. 

Экспортирует все зарегистрированные метрики.

Возвращает **HTTP Status 200 если health check в статусе UP** и **500 если в статусе down или unknown**. Это используется Prometheus для определения состояния.


Принятые правила именовая метрик отличаются в Spring и Prometheus, поэтому я слегда модифицировал их в этом endpoint.

Пример:
К "heap" дописаны единицы изменения, в результате метрика **heap** называется  **heap_bytes**.
Использовать рекомендации prometheus собирая метрики в _total смысла не вижу, документация spring станет неудобна.   

Чтобы отключить или изменить это поведение нужно определить свой экземпляр **PrometeusMetricNameConverter**. Реализацию текущего поведения можно найти в DefaultPrometeusMetricNameConverter




## English
This is the implementation of metrics endpoint for Spring Boot Actuator in the text format applied in Prometheus.

It exploits all the registered metrics.

It returns **HTTP Status 200 if health check is in the status UP** and **500 if it is in the status down or unknown**. It is used in Prometheus to identify the status.

The accepted rules of naming the metrics are different in Spring and Prometheus, that’s why I’ve slightly modified them in this endpoint. For example, there are some added units to "heap", as a result, the metric **heap** is called **heap_bytes**.

In order to turn off or change this behaviour it is necessary to define your instance of **PrometeusMetricNameConverter**. It’s possible to find the implementation of the current behaviour in DefaultPrometeusMetricNameConverter.



