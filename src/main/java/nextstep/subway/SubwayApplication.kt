package nextstep.subway

import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.boot.autoconfigure.SpringBootApplication
import kotlin.jvm.JvmStatic
import org.springframework.boot.SpringApplication

@EnableJpaRepositories
@EnableJpaAuditing
@SpringBootApplication
class SubwayApplication {
    fun main(args: Array<String>) {
        SpringApplication.run(SubwayApplication::class.java, *args)
    }
}
