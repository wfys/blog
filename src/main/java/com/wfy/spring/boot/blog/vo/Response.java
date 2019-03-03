package com.wfy.spring.boot.blog.vo;
/**
 *  ��Ӧ ֵ����.�����ض���
 * @author wfy
 *
 */
public class Response {
	private boolean success; //�����Ƿ�ɹ�
	private String message;//�������Ϣ��ʾ
	private Object body;//��������
	
	/** ��Ӧ�����Ƿ�ɹ� */
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	/** ��Ӧ�������Ϣ */
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	/** ��Ӧ����ķ������� */
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}

	public Response(boolean success, String message) {
		this.success = success;
		this.message = message;
	}
	
	public Response(boolean success, String message, Object body) {
		this.success = success;
		this.message = message;
		this.body = body;
	}
}
