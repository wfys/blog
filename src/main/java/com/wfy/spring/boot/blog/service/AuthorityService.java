package com.wfy.spring.boot.blog.service;

import com.wfy.spring.boot.blog.domain.Authority;
/**
 * Authority ����ӿ�.
 * @author wfy
 *
 */
public interface AuthorityService {
	/**
	 * ����id��ȡ Authority
	 * @param Authority
	 * @return
	 */
	Authority getAuthorityById(Long id);
}
