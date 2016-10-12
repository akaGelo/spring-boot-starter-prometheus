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

Возвращает **HTTP Status 200 если health check в статусе UP** и 500 если в статусе down или unknown. Это используется Prometheus для определения состояния.


Принятые правила именовая метрик отличаются в spring и prometheus, поэтому я слегда модифицировал их в этом endpoint.
Пример:
К "heap" дописаны единицы изменения, в результате метрика heap называется  heap_bytes.
Использовать рекомендации prometheus собирая метрики в _total смысла не вижу, документация spring станет неудобна.   

Чтобы отключить или изменить это поведение нужно определить свой экземпляр PrometeusMetricNameConverter. Реализацию текущего поведения можно найти в DefaultPrometeusMetricNameConverter


