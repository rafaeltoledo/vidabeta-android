package net.rafaeltoledo.vidabeta.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "podcasts")
public class Podcast implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5676940425170557522L;
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String titulo;
	@DatabaseField
	private String data;
	@DatabaseField
	private String descricao;
	@DatabaseField
	private String duracao;
	@DatabaseField
	private String foto;
	@DatabaseField
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

	public String getDuracao() {
		return duracao;
	}

	public void setDuracao(String duracao) {
		this.duracao = duracao;
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
