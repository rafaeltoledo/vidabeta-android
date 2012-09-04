package net.rafaeltoledo.vidabeta.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "podcasts")
public class Podcast {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String titulo;
	@DatabaseField(dataType = DataType.DATE)
	private long data;
	@DatabaseField
	private String descricao;
	@DatabaseField
	private int duracao;
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

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
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
}
