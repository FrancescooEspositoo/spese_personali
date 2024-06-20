package spesePersonali;
import java.sql.*;
import java.util.*;
public class CategoriaCRUD {
	//metodi crud
	//create
		public void addCategoria(Categoria categoria) {
			String query="INSERT INTO categorie (nome_categoria , descrizione)VALUES (?,?)";
			try(Connection conn = DatabaseConnection.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(query)){
				pstmt.setString(1, categoria.getNomeCategoria());
				pstmt.setString(2, categoria.getDescrizione());
				pstmt.executeUpdate();
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		//read
		public List<Categoria> getAllCategoria(){
			List<Categoria> categorie = new ArrayList<>();
			String query="SELECT * FROM categorie";
			try(Connection conn = DatabaseConnection.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(query)
					){
				while(rs.next()) {
					Categoria categoria = new Categoria();
					categoria.setIdCategoria(rs.getInt("id_categoria"));
					categoria.setNomeCategoria(rs.getString("nome_categoria"));
					categoria.setDescrizione(rs.getString("descrizione"));
					categorie.add(categoria);
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			return categorie;
		}
		//delete
		public void deleteCategoria(int idCategoria) {
			String query= "DELETE FROM categorie WHERE id_categoria = ?";
			try(Connection conn = DatabaseConnection.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(query)){
				pstmt.setInt(1, idCategoria);
				pstmt.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		//update
		public void updateCategoria(Categoria categoria) {
			String query="UPDATE categorie SET nome_categoria = ? , descrizione = ? WHERE id_categoria = ?";
			try(Connection conn = DatabaseConnection.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(query))
			{
				pstmt.setString(1, categoria.getNomeCategoria());
				pstmt.setString(2, categoria.getDescrizione());
				pstmt.setInt(3, categoria.getIdCategoria());
				pstmt.executeUpdate();
						
					}catch(SQLException e) {
						e.printStackTrace();
					}
		}
		public int getIdCategoriaFromNome(String nomeCategoria) {
		    String query = "SELECT id_categoria FROM categorie WHERE nome_categoria = ?";
		    try (Connection conn = DatabaseConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(query)) {
		        pstmt.setString(1, nomeCategoria);
		        try (ResultSet rs = pstmt.executeQuery()) {
		            if (rs.next()) {
		                return rs.getInt("id_categoria");
		            } else {
		                throw new IllegalArgumentException("Categoria non trovata: " + nomeCategoria);
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        throw new RuntimeException("Errore durante il recupero dell'id della categoria", e);
		    }
		}

}
