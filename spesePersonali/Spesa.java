package spesePersonali;

public class Spesa {
	private int idSpesa;
	private String nomeSpesa;
	private float importo;
	private String data;
	private int idCategoria;
	private Categoria categoria;
	
	//costruttore vuoto
	public Spesa() {
		
	}
	//altri costruttori
	
	
	public Spesa( int idSpesa, String nomeSpesa, float importo, String data, int idCategoria) {
		this.idSpesa = idSpesa;
		this.nomeSpesa = nomeSpesa;
		this.importo = importo;
		this.data = data;
		this.idCategoria = idCategoria;
	}
	public Spesa( String nomeSpesa, float importo, String data, int idCategoria) {
		this.nomeSpesa = nomeSpesa;
		this.importo = importo;
		this.data = data;
		this.idCategoria = idCategoria;
	}
	//metodi set e get
	public void setIdSpesa(int idSpesa) {
		this.idSpesa = idSpesa;
	}
	public int getIdSpesa() {
		return idSpesa;
	}
	public void setNomeSpesa(String nomeSpesa) {
		this.nomeSpesa = nomeSpesa;
	}
	public String getNomeSpesa() {
		return nomeSpesa;
	}
	
	public void setImporto(float importo) {
		this.importo = importo;	
	}
	public float getImporto() {
		return importo;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getData() {
		return data;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	// Metodo per impostare la categoria associata alla spesa
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // Metodo per ottenere la categoria associata alla spesa
    public Categoria getCategoria() {
        return categoria;
    }

}
