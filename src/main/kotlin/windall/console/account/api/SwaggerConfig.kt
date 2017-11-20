package windall.console.account.api
 
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
	
	@Bean
	fun productApi() = Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("windall.console.account.api.controllers"))
                .build()
                .apiInfo(metaData())
	
	fun metaData() = ApiInfo(
			"Console Demo Account REST API",
			"Spring Boot REST API provided as part of my job application",
			"1.0",
			"No terms of service",
			Contact("Samuel Windall", "http://yourname.xyz", "samuel.windall@gmail.com"),
			"Unlicense",
			"https://unlicense.org/UNLICENSE"
	)

}