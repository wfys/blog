package com.wfy.spring.boot.blog.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * ����ʵ��
 * @author wfy
 *
 */
@Entity // ʵ��
public class Vote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id // ����
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ����������
	private Long id; // �û���Ψһ��ʶ
 
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(nullable = false) // ӳ��Ϊ�ֶΣ�ֵ����Ϊ��
	@org.hibernate.annotations.CreationTimestamp  // �����ݿ��Զ�����ʱ��
	private Timestamp createTime;
 
	protected Vote() {
	}
	
	public Vote(User user) {
		this.user = user;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
 
	public Timestamp getCreateTime() {
		return createTime;
	}
 
}
