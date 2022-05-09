package demo


interface CustomerService {
    fun create(consumer: CustomerBoundary?): CustomerBoundary?
    fun getByEmail(email: String?): CustomerBoundary?
    fun login(email: String?, password: String?): CustomerBoundary?
    fun update(email: String?, consumer: CustomerBoundary?)
    fun deleteAll()
    fun getAllCustomers(
        size: Int, page: Int, sortAttribute: String?, order: String?,
        criteriaType: String?, criteriaValue: String?
    ): List<CustomerBoundary?>?

}