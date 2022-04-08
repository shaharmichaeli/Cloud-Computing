package demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
open class CustomerController  {
    @Autowired
    lateinit var customerService : CustomerService


    @PostMapping("/customers",consumes = ["application/json"], produces = ["application/json"])
    fun create(@RequestBody Customer: CustomerBoundary?): ResponseEntity<CustomerBoundary?> =
        ResponseEntity(customerService.create(Customer), HttpStatus.CREATED)


    @GetMapping("/customers/byEmail/{email}", produces = ["application/json"])
    fun getByEmail(@PathVariable email: String?): ResponseEntity<CustomerBoundary?> =
    ResponseEntity(customerService.getByEmail(email), HttpStatus.OK)


    @GetMapping("/customers/login/{email}", produces = ["application/json"])
    fun login(
        @PathVariable email: String?, @RequestParam(name = "password", required = false) password: String?
    ): ResponseEntity<CustomerBoundary?> =
        ResponseEntity(customerService.login(email, password), HttpStatus.OK)

    @PutMapping("/customers/{email}",consumes = ["application/json"])
    fun update(@PathVariable email: String?, @RequestBody customer: CustomerBoundary?): ResponseEntity<Unit> =
        ResponseEntity(customerService.update(email, customer), HttpStatus.OK)


    @DeleteMapping("/customers")
    fun deleteAll() = customerService.deleteAll()

    @GetMapping("/customers/search", produces = ["application/json"])
    fun search(
        @RequestParam(name = "size", required = false, defaultValue = "10") size: Int,
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "sortBy", required = false, defaultValue = "email") sortAttribute: String?,
        @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") order: String?,
        @RequestParam(name = "criteriaType", required = false) criteriaType: String?,
        @RequestParam(name = "criteriaValue", required = false) criteriaValue: String?
    ): ResponseEntity<List<CustomerBoundary?>?> = ResponseEntity(
        customerService.getAllCustomers(size, page, sortAttribute, order, criteriaType, criteriaValue),
        HttpStatus.OK
    )

}