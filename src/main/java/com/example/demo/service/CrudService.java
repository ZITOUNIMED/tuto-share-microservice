package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudService<T, ID> {
	void save(T t);
	Page<T> findAll(Pageable pageableRequest);
	List<T> findAll();
	T findById(ID id);
	void deleteById(ID id);
	void delete(T t);
	void saveAll(List<T> list);
}
