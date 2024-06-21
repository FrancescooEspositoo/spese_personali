package spesePersonali;

public class Categoria {
	private int idCategoria;
	private String nomeCategoria;
	private String descrizione;
	//costruttori
	public Categoria() {
		
	}
	public Categoria(int idCategoria, String nomeCategoria, String descrizione) {
		this.idCategoria = idCategoria;
		this.nomeCategoria = nomeCategoria;
		this.descrizione = descrizione;
	}
	public Categoria( String nomeCategoria) {
		
		this.nomeCategoria = nomeCategoria;
		
	}
	public Categoria( String nomeCategoria, String descrizione) {
		
		this.nomeCategoria = nomeCategoria;
		this.descrizione = descrizione;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
	public String getNomeCategoria() {
		return nomeCategoria;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getDescrizione() {
		return descrizione;
	}
	@Override
    public String toString() {
        return nomeCategoria; // Ritorna il nome della categoria per la rappresentazione nella JComboBox
    }
}
