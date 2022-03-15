package exercise1;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerDAO extends PagingAndSortingRepository<CustomerEntity, String> {

}
