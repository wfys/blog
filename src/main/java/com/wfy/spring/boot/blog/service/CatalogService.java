package com.wfy.spring.boot.blog.service;

import java.util.List;

import com.wfy.spring.boot.blog.domain.Catalog;
import com.wfy.spring.boot.blog.domain.User;

/**
 * Catalog ����ӿ�.
 */
public interface CatalogService {
	/**
	 * ����Catalog
	 * @param catalog
	 * @return
	 */
	Catalog saveCatalog(Catalog catalog);
	
	/**
	 * ɾ��Catalog
	 * @param id
	 * @return
	 */
	void removeCatalog(Long id);

	/**
	 * ����id��ȡCatalog
	 * @param id
	 * @return
	 */
	Catalog getCatalogById(Long id);
	
	/**
	 * ��ȡCatalog�б�
	 * @return
	 */
	List<Catalog> listCatalogs(User user);
}