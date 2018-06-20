package com.are.lecturas;

import java.io.IOException;
import java.sql.SQLException;

import com.csvreader.CsvReader;

public class UploadFileRecibe {
	private String filename;
	int registros = 0;
	int cnt=0;
	
	public int getContadorRegistros() {
		return registros;
	}
	public int getContadorRegistrosSubidos() {
		return cnt;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public UploadFileRecibe(String filename) {
		super();
		this.filename = filename;
	}
	
	public boolean Parser() throws IOException, SQLException {
		CsvReader reader = new CsvReader(this.filename);
		reader.setDelimiter('	'); // tabulador
		db conexion = new db();
		while (reader.readRecord()) {
			registros++;
			try { 
				String sql = "SELECT ID "
							+ " FROM lecturas "
							+ " WHERE unicom=? "
							+ " AND ruta = ? "
							+ " AND num_itim = ? "
							+ " AND sec_reg = ? "
							+ " AND num_apa = ? "
							+ " AND ciclo = ? "
							+ " AND tip_csmi = ?"
							+ " AND nic = ?";
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				
				pst.setString(1,reader.get(3));
				pst.setString(2,reader.get(4));
				pst.setString(3,reader.get(6));
				pst.setString(4,reader.get(7));
				pst.setString(5,reader.get(12));
				pst.setString(6,reader.get(5));
				pst.setString(7,reader.get(14));
				pst.setString(8,reader.get(19));
				
				java.sql.ResultSet rs = conexion.Query(pst);
				if (rs.next()) {  // Existe el registro
					
					sql = "UPDATE lecturas "
							+ "SET anomalia=?, "
//							+ "fecha_lectura=?, "
							+ "lectura = ?, "
							+ "file_update = ? ,"
							+ "fila_file = ? ,"
							+ "usuario_update= ?, "
							+ "fecha_update=SYSDATETIME() "
							+ " WHERE id=? AND fecha_update is null";
					java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
					pst2.setString(1,reader.get(10));
//					pst2.setString(2,reader.get(16));
					pst2.setString(2,reader.get(17));
					pst2.setString(3,this.filename);
					pst2.setInt(4,registros);
					pst2.setString(5,"robot");
					pst2.setInt(6,rs.getInt("ID"));
					
					if (conexion.Update(pst2) > 0 ) {
		            	cnt++;
		            	conexion.Commit();
		            }else {
		            	Utilidades.AgregarLog("Registro no actualizado. ID " + rs.getInt("ID") + " Fila : " + registros + " File: " + this.filename);
		            }

				}else {
					//LogSQL log = new LogSQL(conexion);
					//log.Add("No hay informacion en el registro de envio", this.filename, registros,"Archivo recibe");
					String filtro = String.format("unicom (%s), ruta (%s), num_itim(%s), sec_reg (%s), num_apa (%s), ciclo (%s), tip_csmi(%s), nic (%s) ",
							reader.get(3), reader.get(4),reader.get(6),reader.get(7),reader.get(12),reader.get(5),reader.get(14),reader.get(19));
					Utilidades.AgregarLog("Registro de recibe no encontrado en la BD, archivo " + this.filename + " Fila: " + registros + " Filtro: " + filtro );
				}
            
			}catch (SQLException e) {
				System.out.println("Error: " + e.getMessage());
				Utilidades.AgregarLog("Error: " + e.getMessage());
				//LogSQL log = new LogSQL(conexion);
				//log.Add(e.getMessage(), this.filename, registros,"Archivo recibe");
			}
			
		}
		
		System.out.println("Archivo parseado.  Registros: " + registros + " Subidos: " + cnt);
		Utilidades.AgregarLog("Archivo parseado.  Registros: " + registros + " Subidos: " + cnt);
		
		if (registros != cnt) {  // Error cargar todos los registros
			/*String sql = "INSERT INTO logUploadFiles (fecha,archivo,reg_total, reg_upload,tipo,estado) values (SYSDATETIME(),?,?,?,'recibe','ERROR')";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,this.filename);
			pst.setInt(2,registros);
			pst.setInt(3,cnt);
			conexion.Update(pst);*/
			
		}else {
			/*String sql = "INSERT INTO logUploadFiles (fecha,archivo,reg_total, reg_upload,tipo,estado) values (SYSDATETIME(),?,?,?,'recibe','UPLOAD')";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,this.filename);
			pst.setInt(2,registros);
			pst.setInt(3,cnt);
			conexion.Update(pst);*/
		}
		
		conexion.Commit();
		conexion.Close();
		reader.close();
		return true;
	}
	
	

}
