package com.wfy.spring.boot.blog.service;

import com.wfy.spring.boot.blog.domain.Comment;

/**
 *  Comment ����ӿ�.
 * @author wfy
 *
 */
public interface CommentService {
	/**
	 * ����id��ȡ Comment
	 * @param id
	 * @return
	 */
	Comment getCommentById(Long id);
	/**
	 * ɾ������
	 * @param id
	 * @return
	 */
	void removeComment(Long id);
}
