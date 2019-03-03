package com.wfy.spring.boot.blog.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wfy.spring.boot.blog.domain.User;
/**
 * �û��ֿ�
 * @author wfy
 *
 */
public interface UserRepository extends JpaRepository<User, Long>{
	/**
	 * �����û�����ҳ��ѯ�û��б�
	 * @param name
	 * @param pageable
	 * @return
	 */
	Page<User> findByNameLike(String name, Pageable pageable);
	
	User findByUsername(String username);
	/**
	 * ���������б��ѯ
	 * @param usernames
	 * @return
	 */
	List<User> findByUsernameIn(Collection<String> usernames);
}
