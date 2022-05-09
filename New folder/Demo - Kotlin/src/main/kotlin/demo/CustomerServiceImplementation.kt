package demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*


@Service
class CustomerServiceImplementation @Autowired constructor(private val customerDAO: CustomerDAO, validator: Validator) :
    CustomerService {
    private val validator: Validator

    init {
        this.validator = validator
    }

    fun boundaryToEntity(cb: CustomerBoundary?): CustomerEntity {
        val entity = CustomerEntity()
        val birthdate = cb!!.birthdate!!.split("-").toTypedArray()
        entity.day = birthdate[0].toInt()
        entity.month = birthdate[1].toInt()
        entity.year = birthdate[2].toInt()
        entity.email = cb.email
        val emailParts = cb.email!!.split("@").toTypedArray()
        entity.emailDomain = emailParts[1]
        entity.firstName = cb.name!!.first
        entity.lastName = cb.name!!.last
        entity.password = cb.password
        entity.roles = cb.roles
        return entity
    }

    fun entityToBoundary(e: CustomerEntity?): CustomerBoundary {
        val cb = CustomerBoundary()
        cb.birthdate = e!!.day.toString() + "-" + e.month + "-" + e.year
        cb.email = e.email
        cb.name = Name(e.firstName, e.lastName)
        cb.password = e.password
        cb.roles = e.roles
        return cb
    }

    override fun create(consumer: CustomerBoundary?): CustomerBoundary? {
        validator.checkCustomerValidity(consumer)
        val op = consumer!!.email?.let { customerDAO.findById(it) }
        if (op != null) {
            if (op.isPresent) {
                throw CustomerAlreadyExistExecption("Create Method - There is already user with this email.")
            }
        }
        val e = boundaryToEntity(consumer)
        customerDAO.save(e)
        return consumer
    }

    override fun getByEmail(email: String?): CustomerBoundary? {
        validator.checkEmailValidity(email)
        val op = customerDAO.findById(email)
        return if (op.isPresent) {
            val tempEntity = op.get()
            entityToBoundary(tempEntity)
        } else {
            throw UserNotFoundExecption("getByEmail Method - User Not Found.")
        }
    }

    override fun login(email: String?, password: String?): CustomerBoundary? {
        validator.checkEmailValidity(email)
        val op = customerDAO.findById(email)
        if (op.isPresent) {
            val tempEntity = op.get()
            return if (tempEntity.password == password) {
                entityToBoundary(tempEntity)
            } else {
                throw UnauthorizeException("Login Method Failed.")
            }
        }
        throw UnauthorizeException("Login Method Failed.")
    }

    override fun update(email: String?, customer: CustomerBoundary?) {
        val op = customerDAO.findById(email)
        if (op.isPresent) {
            val oldEntity = op.get()
            customerDAO.deleteById(email)
            val e = CustomerEntity()
            e.email = email // Email can't be changed.
            if (customer!!.birthdate != null) {
                validator.checkBirthdateValidity(customer.birthdate)
                val birthdate = customer.birthdate!!.split("-").toTypedArray()
                e.day = birthdate[0].toInt()
                e.month = birthdate[1].toInt()
                e.year = birthdate[2].toInt()
            } else {
                e.day = oldEntity.day
                e.month = oldEntity.month
                e.year = oldEntity.year
            }
            if (customer.name != null) {
                validator.checkNameValidity(customer.name)
                e.firstName = customer.name!!.first
                e.lastName = customer.name!!.last
            } else {
                e.firstName = oldEntity.firstName
                e.lastName = oldEntity.lastName
            }
            if (customer.password != null) {
                validator.checkPasswordValidity(customer.password)
                e.password = customer.password
            } else {
                e.password = oldEntity.password
            }
            validator.checkRolesValidity(customer.roles)
            e.roles = customer.roles
            customerDAO.save(e)
        } else {
            throw UserNotFoundExecption("Update Method - User Not Found.")
        }
    }

    override fun deleteAll() {
        customerDAO.deleteAll()
    }

    override fun getAllCustomers(
        size: Int, page: Int, sortAttribute: String?, order: String?,
        criteriaType: String?, criteriaValue: String? ): List<CustomerBoundary?>? {
        if (order != "ASC" && order != "DESC") {
            throw RuntimeException("getAllCustomers: Unacceptable Order")
        }
        val direction: Sort.Direction = if (order == "ASC") {
            Sort.Direction.ASC
        } else {
            Sort.Direction.DESC
        }
        var entities: List<CustomerEntity?>? = null
        entities = if (criteriaType != null && criteriaType == "byBirthYear") {
            if (criteriaValue == null) {
                throw RuntimeException("getAllCustomers: criteriaValue is empty.")
            }
            customerDAO.findAllByYear(
                criteriaValue.toInt(),
                PageRequest.of(page, size, direction, sortAttribute)
            )
        } else if (criteriaType != null && criteriaType == "byEmailDomain") {
            if (criteriaValue == null) {
                throw RuntimeException("getAllCustomers: criteriaValue is empty.")
            }
            customerDAO.findAllByEmailDomain(
                criteriaValue,
                PageRequest.of(page, size, direction, sortAttribute)
            )
        } else {
            val pageOfEntities = customerDAO
                .findAll(PageRequest.of(page, size, direction, sortAttribute)) as Page<CustomerEntity?>
            pageOfEntities.content
        }
        val bounderies: MutableList<CustomerBoundary?> = ArrayList()
        for (entity in entities!!) {
            val boundary = entityToBoundary(entity)
            bounderies.add(boundary)
        }
        return bounderies
    }





}
