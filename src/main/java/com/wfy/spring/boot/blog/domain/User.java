package com.wfy.spring.boot.blog.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



/**
 * User ʵ��
 * @author wfy
 *
 */
@Entity  // ʵ��
@Table(name = "user")
@XmlRootElement // MediaType תΪ XML
public class User implements UserDetails,Serializable{

	private static final long serialVersionUID = 1L;
	@Id  // ����
    @GeneratedValue(strategy=GenerationType.IDENTITY) // ����������
	private long id; // �û���Ψһ��ʶ
	@NotEmpty(message = "��������Ϊ��")
	@Size(min=2, max=20)
	@Column(nullable = false, length = 20) // ӳ��Ϊ�ֶΣ�ֵ����Ϊ��
	private String name;

	@NotEmpty(message = "���䲻��Ϊ��")
	@Size(max=50)
	@Email(message= "�����ʽ����" ) 
	@Column(nullable = false, length = 50, unique = true)
	private String email;

	@NotEmpty(message = "�˺Ų���Ϊ��")
	@Size(min=3, max=20)
	@Column(nullable = false, length = 20, unique = true)
	private String username; // �û��˺ţ��û���¼ʱ��Ψһ��ʶ

	@NotEmpty(message = "���벻��Ϊ��")
	@Size(max=100)
	@Column(length = 100)
	private String password; // ��¼ʱ����
	
	@Column(length = 200)
	private String avatar; // ͷ��ͼƬ��ַ
	
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities;
	
	protected User() {  //// JPA �Ĺ淶Ҫ���޲ι��캯������Ϊ protected ��ֱֹ��ʹ�� 
	}

	public User(String name, String email,String username,String password) {
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvater() {
		return avatar;
	}

	public void setAvater(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return String.format("User[id=%d, username='%s', name='%s', email='%s', password='%s']", 
				id, username, name, email,password);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	//  �轫 List<Authority> ת�� List<SimpleGrantedAuthority>������ǰ���ò�����ɫ�б�����
			List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
			for(GrantedAuthority authority : this.authorities){
				simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
			}
			return simpleAuthorities;
	}
    //�Ƿ���Ȩ
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//�Ƿ񱻼���
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	public void setEncodePassword(String password) {
		PasswordEncoder  encoder = new BCryptPasswordEncoder();
		String encodePasswd = encoder.encode(password);
		this.password = encodePasswd;
	}

}
