package spesePersonali;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import java.sql.*;
import java.io.IOException;

public class Pdf {

	public void salvaPdf(){
	//public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/spese_personali";
		String user = "root";
		String password = "Partenopeo.1926";
		try (
				//Connection conn = DatabaseConnection.getConnection();
				Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement();
				){
			
			//Query di estrazione 
			String query = "SELECT spese.nome_spesa, spese.importo, spese.data, categorie.nome_categoria"
					+ " FROM spese"
					+ " JOIN categorie ON spese.id_categoria = categorie.id_categoria";;
			ResultSet rs = stmt.executeQuery(query);
			
			//Creazione del documento pdf
			PDDocument document = new PDDocument();
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);
			
			//apertura del contentStream
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			
			//impostiamo il font e il testo del documento
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
			contentStream.beginText();
			contentStream.newLineAtOffset(220, 750);
			contentStream.showText("Spese");
			contentStream.endText();
			
			contentStream.setFont(PDType1Font.HELVETICA, 12);
			
			//Definiamo la tabella che conterrÃ  i dati
			//Intestazione tabella
			float yPosition = 700;
			contentStream.beginText();
			contentStream.newLineAtOffset(50, yPosition);
			contentStream.showText("Nome Spesa");
			contentStream.newLineAtOffset(200, 0);
			contentStream.showText("Importo Spesa");
			contentStream.newLineAtOffset(150, 0);
			contentStream.showText("Data Spesa");
			contentStream.newLineAtOffset(100, 0);
			contentStream.showText("Categoria");
			contentStream.endText();
			
			yPosition -= 20;
			//lettura da rs e scrittura su pdf
			while(rs.next()) {
				String nome = rs.getString("nome_spesa");
				double importo = rs.getDouble("importo");
				String data = rs.getString("data");
				String nomeCategoria = rs.getString("nome_categoria");
				
				//puntiamo sul pdf
				contentStream.beginText();
				contentStream.newLineAtOffset(50, yPosition);
				contentStream.showText(nome);
				contentStream.newLineAtOffset(200, 0);
				contentStream.showText(String.format("%.2f",importo));
				contentStream.newLineAtOffset(150, 0);
				contentStream.showText(data);
				contentStream.newLineAtOffset(100, 0);
				contentStream.showText(nomeCategoria);
				contentStream.endText();
				
				yPosition -= 20;
				
			}
			
			//chiusura dello stream
			contentStream.close();
			
			//salvataggio del documento
			document.save("Spese.pdf");
			
			//chiusura documento
			document.close();
			
			System.out.println("Fattura creata con successo!");			
			
		}catch(SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	}