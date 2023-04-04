package hu.webuni.hr.alagi.repository;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface MyCrudLikeRepository<T, ID> extends Repository<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(T entity);
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll();
    long count();
    boolean existsById(ID id);

}
