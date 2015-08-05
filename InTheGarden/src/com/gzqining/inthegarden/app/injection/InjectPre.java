package com.gzqining.inthegarden.app.injection;


class InjectPre {

	//Field | Method
	private Object entity;
	
	private Object annotation;
	
	private InjectExecute execute;
	
	public InjectPre() {
		
	}
	
	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public Object getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Object annotation) {
		this.annotation = annotation;
	}

	public InjectExecute getExecute() {
		return execute;
	}

	public void setExecute(InjectExecute execute) {
		this.execute = execute;
	}
	
}
