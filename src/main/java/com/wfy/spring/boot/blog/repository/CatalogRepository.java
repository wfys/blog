package com.wfy.spring.boot.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wfy.spring.boot.blog.domain.Catalog;
import com.wfy.spring.boot.blog.domain.User;

/**
 * Catalog �ֿ�.
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long>{
	
	/**
	 * �����û���ѯ
	 * @param user
	 * @return
	 */
	List<Catalog> findByUser(User user);
	
	/**
	 * �����û����������Ʋ�ѯ
	 * @param user
	 * @param name
	 * @return
	 */
	List<Catalog> findByUserAndName(User user,String name);
}
