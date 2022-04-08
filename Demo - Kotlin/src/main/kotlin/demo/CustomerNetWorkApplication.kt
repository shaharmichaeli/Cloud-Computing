package demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class CustomerNetWorkApplication

fun main(args: Array<String>) {
	runApplication<CustomerNetWorkApplication>(*args)
}
