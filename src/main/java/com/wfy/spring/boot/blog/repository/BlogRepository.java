package com.wfy.spring.boot.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wfy.spring.boot.blog.domain.Blog;
import com.wfy.spring.boot.blog.domain.Catalog;
import com.wfy.spring.boot.blog.domain.User;

public interface BlogRepository extends JpaRepository<Blog, Long>{
	/**
	 * 根据用户名分页查询博客列表（逆时）
	 * @param user
	 * @param title
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByUserAndTitleLikeOrTagsLikeOrderByCreateTimeDesc(User user, String title, String tags,Pageable pageable);
	
	/**
	 * 根据用户名分页查询博客列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);
	/**
	 * 根据分类分页查询博客列表
	 * 
	 */
	Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
	/**
	 * 根据分页查询博客列表（逆时）
	 * @param user
	 * @param title
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByTitleLikeOrTagsLikeOrderByCreateTimeDesc( String title, String tags,Pageable pageable);
	
	/**
	 * 根据用户名分页查询博客列表
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByTitleLike(String title, Pageable pageable);
	
}
