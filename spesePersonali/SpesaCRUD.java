package spesePersonali;

import java.math.BigDecimal;
import java.sql.*;
import java.time.YearMonth;
import java.util.*;

public class SpesaCRUD {
	//metodi crud
	//create
	public void addSpesa(Spesa spesa) {
		String query="INSERT INTO spese (nome_spesa, importo, data, id_categoria)VALUES (?,?,?,?)";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)){
			pstmt.setString(1, spesa.getNomeSpesa());
			pstmt.setFloat(2, spesa.getImporto());
			pstmt.setString(3, spesa.getData());
			pstmt.setInt(4, spesa.getIdCategoria());
			
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//read
	public List<Spesa> getAllSpesa(){
		List<Spesa> spese = new ArrayList<>();
		String query="SELECT * FROM spese";
		try(Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)
				){
			while(rs.next()) {
				Spesa spesa = new Spesa();
				spesa.setIdSpesa(rs.getInt("id_spese"));
				spesa.setNomeSpesa(rs.getString("nome_spesa"));
				spesa.setImporto(rs.getFloat("importo"));
				spesa.setData(rs.getString("data"));
				spesa.setIdCategoria(rs.getInt("id_categoria"));
				spese.add(spesa);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return spese;
	}
	//update
	public void updateSpesa(Spesa spesa) {
		String query = "UPDATE spese SET nome_spesa = ?, importo = ?, data = ?, id_categoria = ? WHERE id_spese = ?";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)){
			pstmt.setString(1, spesa.getNomeSpesa());
			pstmt.setFloat(2, spesa.getImporto());
			pstmt.setString(3, spesa.getData());
			pstmt.setInt(4, spesa.getIdCategoria());
			pstmt.setInt(5, spesa.getIdSpesa());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//delete
	public void deleteSpesa(int idSpesa) {
		String query="DELETE FROM spese WHERE id_spese = ?";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)){
			pstmt.setInt(1, idSpesa);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//ricerca per data o categoria
	public List<Spesa> searchSpesa(String searchTerm) {
        List<Spesa> spese = new ArrayList<>();
        
        String query = "SELECT spese.id_spese, spese.nome_spesa, spese.importo, spese.data, spese.id_categoria, categorie.nome_categoria, categorie.descrizione "
                     + "FROM spese "
                     + "JOIN categorie ON spese.id_categoria = categorie.id_categoria "
                     + "WHERE categorie.nome_categoria LIKE ? OR spese.data LIKE ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idSpesa = resultSet.getInt("id_spese");
                String nomeSpesa = resultSet.getString("nome_spesa");
                float importo = resultSet.getFloat("importo");
                String data = resultSet.getString("data");
                int idCategoria = resultSet.getInt("id_categoria");
                String nomeCategoria = resultSet.getString("nome_categoria");
                String descrizione = resultSet.getString("descrizione");
                
                
                Categoria categoria = new Categoria(nomeCategoria, descrizione);
                Spesa spesa = new Spesa(idSpesa, nomeSpesa, importo, data, idCategoria);
                spesa.setCategoria(categoria); // Associa la categoria alla spesa
                
                spese.add(spesa);
                
                
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return spese;
    }
	//read
		public List<Spesa> getAllSpesaNuova(){
			List<Spesa> spese = new ArrayList<>();
			String query="select spese.id_spese, spese.nome_spesa, spese.importo, spese.data, categorie.nome_categoria, categorie.descrizione "
					+ "from spese "
					+ "JOIN categorie ON spese.id_categoria = categorie.id_categoria";
			try(Connection conn = DatabaseConnection.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(query)
					){
				while(rs.next()) {
					Spesa spesa = new Spesa();
					
					spesa.setIdSpesa(rs.getInt("id_spese"));
					spesa.setNomeSpesa(rs.getString("nome_spesa"));
					spesa.setImporto(rs.getFloat("importo"));
					spesa.setData(rs.getString("data"));

					String nomeCategoria = rs.getString("nome_categoria");
					String descrizione = rs.getString("descrizione");
					
					
					Categoria categoria = new Categoria(nomeCategoria,descrizione);
					spesa.setCategoria(categoria);
					
					spese.add(spesa);
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			return spese;
		}
		//metodo per il report mensile
				public Map<YearMonth, BigDecimal> generateReportMensile() {
				    Map<YearMonth, BigDecimal> reportMensile = new LinkedHashMap<>();
				    String query = "SELECT YEAR(data) AS anno, MONTH(data) AS mese, SUM(importo) AS totale_spese " +
				                   "FROM spese " +
				                   "GROUP BY anno, mese " +
				                   "ORDER BY anno DESC, mese DESC";
				    try (Connection conn = DatabaseConnection.getConnection();
				         Statement stmt = conn.createStatement();
				         ResultSet rs = stmt.executeQuery(query)) {
				        while (rs.next()) {
				            int anno = rs.getInt("anno");
				            int mese = rs.getInt("mese");
				            BigDecimal totaleSpese = rs.getBigDecimal("totale_spese");
				            YearMonth yearMonth = YearMonth.of(anno, mese);
				            reportMensile.put(yearMonth, totaleSpese);
				        }
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
				    return reportMensile;
				}
				
				//metodo per il report annuale
				public Map<Integer, BigDecimal> generateReportAnnuale() {
				    Map<Integer, BigDecimal> reportAnnuale = new LinkedHashMap<>();
				    String query = "SELECT YEAR(data) AS anno, SUM(importo) AS totale_spese " +
				                   "FROM spese " +
				                   "GROUP BY anno " +
				                   "ORDER BY anno DESC";
				    try (Connection conn = DatabaseConnection.getConnection();
				         Statement stmt = conn.createStatement();
				         ResultSet rs = stmt.executeQuery(query)) {
				        while (rs.next()) {
				            int anno = rs.getInt("anno");
				            BigDecimal totaleSpese = rs.getBigDecimal("totale_spese");
				            reportAnnuale.put(anno, totaleSpese);
				        }
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
				    return reportAnnuale;
				}
}
