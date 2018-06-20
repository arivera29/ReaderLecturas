package com.are.lecturas;

public class Unicom {
	private String codigo;
	private String descripcion;
	private int estado;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String desccripcion) {
		this.descripcion = desccripcion;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	public Unicom(String codigo, String descripcion, int estado) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.estado = estado;
	}
	public Unicom() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
