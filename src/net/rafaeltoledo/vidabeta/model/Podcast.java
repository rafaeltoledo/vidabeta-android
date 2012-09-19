package net.rafaeltoledo.vidabeta.model;

import java.io.Serializable;

public class Podcast implements Serializable{

	private static final long serialVersionUID = 5676940425170557522L;
	
	private String titulo;
	
	private String data;
	
	private String descricao;
	
	private String foto;
	
	private String link;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return getTitulo();
	}
}
