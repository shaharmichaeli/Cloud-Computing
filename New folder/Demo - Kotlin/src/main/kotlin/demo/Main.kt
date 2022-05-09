package demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Main {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<Main>(*args)
		}
	}
}

