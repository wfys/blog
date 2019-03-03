package com.wfy.spring.boot.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wfy.spring.boot.blog.domain.Blog;
import com.wfy.spring.boot.blog.domain.Catalog;
import com.wfy.spring.boot.blog.domain.User;

/**
 * Blog ����ӿ�.
 */
public interface BlogService {
	/**
	 * ����Blog
	 * @param Blog
	 * @return
	 */
	Blog saveBlog(Blog blog);
	
	/**
	 * ɾ��Blog
	 * @param id
	 * @return
	 */
	void removeBlog(Long id);
	
	/**
	 * ����Blog
	 * @param Blog
	 * @return
	 */
	Blog updateBlog(Blog blog);
	
	/**
	 * ����id��ȡBlog
	 * @param id
	 * @return
	 */
	Blog getBlogById(Long id);
	
	/**
	 * �����û������з�ҳģ����ѯ�����£�
	 * @param user
	 * @return
	 */
	Page<Blog> listBlogsByTitleLike(User user, String title,String tags, Pageable pageable);
 
	/**
	 * �����û������з�ҳģ����ѯ�����ȣ�
	 * @param user
	 * @return
	 */
	Page<Blog> listBlogsByTitleLikeAndSort(User suser, String title, Pageable pageable);
	
	/**
	 * �Ķ�������
	 * @param id
	 */
	void readingIncrease(Long id);
	
	/**
	 * ��������
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	Blog createComment(Long blogId, String commentContent);
	
	/**
	 * ɾ������
	 * @param blogId
	 * @param commentId
	 * @return
	 */
	void removeComment(Long blogId, Long commentId);
	/**
	 * ����
	 * @param blogId
	 * @return
	 */
	Blog createVote(Long blogId);
	
	/**
	 * ȡ������
	 * @param blogId
	 * @param voteId
	 * @return
	 */
	void removeVote(Long blogId, Long voteId);
	/**
	 * ���ݷ�����в�ѯ
	 * @param catalog
	 * @param pageable
	 * @return
	 */
	Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable); 
	
	/**
	 * ���ݽ��з�ҳģ����ѯ�����£�
	 * @param user
	 * @return
	 */
	Page<Blog> listBlogsByTitle(String title,String tags, Pageable pageable);
 
	/**
	 * �����û������з�ҳģ����ѯ�����ȣ�
	 * @param user
	 * @return
	 */
	Page<Blog> listBlogsByTitleLikes( String title, Pageable pageable);
}
