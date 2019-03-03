package com.wfy.spring.boot.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wfy.spring.boot.blog.domain.Blog;
import com.wfy.spring.boot.blog.domain.Catalog;
import com.wfy.spring.boot.blog.domain.User;

public interface BlogRepository extends JpaRepository<Blog, Long>{
	/**
	 * �����û�����ҳ��ѯ�����б���ʱ��
	 * @param user
	 * @param title
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByUserAndTitleLikeOrTagsLikeOrderByCreateTimeDesc(User user, String title, String tags,Pageable pageable);
	
	/**
	 * �����û�����ҳ��ѯ�����б�
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);
	/**
	 * ���ݷ����ҳ��ѯ�����б�
	 * 
	 */
	Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
	/**
	 * ���ݷ�ҳ��ѯ�����б���ʱ��
	 * @param user
	 * @param title
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByTitleLikeOrTagsLikeOrderByCreateTimeDesc( String title, String tags,Pageable pageable);
	
	/**
	 * �����û�����ҳ��ѯ�����б�
	 * @param user
	 * @param title
	 * @param sort
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByTitleLike(String title, Pageable pageable);
	
}
