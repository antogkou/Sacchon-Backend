package gr.pfizer.restapi.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, K> {

    Optional<T> findById(K id) ;

    List<T> findAll() ;

    Optional<T> save(T t) ;

    Optional<T> findByName(String name) ;

    Optional<T> remove(K id);
}
