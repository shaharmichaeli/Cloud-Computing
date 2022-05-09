package demo

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param


interface CustomerDAO : PagingAndSortingRepository<CustomerEntity?, String?> {
    fun findAllByEmailDomain(@Param("emailDomain") emailDomain: String?, pageable: Pageable?): List<CustomerEntity?>?
    fun findAllByYear(@Param("year") year: Int, pageable: Pageable?): List<CustomerEntity?>?

    //	public List<CustomerEntity> findAllByBirthdateYear(@Param("birthdateYear") int birthdateYear, Pageable pageable);
    fun findAllByEmailIn(@Param("email") emails: Collection<String?>?, pageable: Pageable?): List<CustomerEntity?>?
}
