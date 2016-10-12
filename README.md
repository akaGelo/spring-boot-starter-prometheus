# spring-boot-starter-prometheus

## Russian
Это реализация metrics endpoint для Spring Boot Actuator. 

Экспортирует все зарегистрированные метрики
Возвращает HTTP Status 200 если health check в статусе UP и 500 если в статусе down или unknown. Это используется Prometheus для определения состояния 


1. Подключаем зависимость через maven
```
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
```

2. Включаем спомощью аннотации @EnablePrometheus

```
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


```
@EnableFeignClients
@SpringBootApplication
public class SpringBootApplication {
....
}

```

