package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.AppCollection;

public interface AppCollectionService extends CrudService<AppCollection, Long> {

	List<AppCollection> findFavoriteCollections();
}
