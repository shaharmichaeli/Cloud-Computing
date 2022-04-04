package exercise1.data;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

public interface CustomerDAO extends PagingAndSortingRepository<CustomerEntity, String> {

	public List<CustomerEntity> findAllByEmailDomain(@Param("emailDomain") String emailDomain, Pageable pageable);

	public List<CustomerEntity> findAllByYear(@Param("year") int year, Pageable pageable);
//	public List<CustomerEntity> findAllByBirthdateYear(@Param("birthdateYear") int birthdateYear, Pageable pageable);

	public List<CustomerEntity> findAllByEmailIn(@Param("email") Collection<String> emails, Pageable pageable);

}
