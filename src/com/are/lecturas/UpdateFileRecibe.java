package com.are.lecturas;

import java.io.IOException;

import java.sql.SQLException;

import com.csvreader.CsvReader;

public class UpdateFileRecibe {
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

	public UpdateFileRecibe(String filename) {
		super();
		this.filename = filename;
	}
	
	public boolean Parser() throws IOException, SQLException {
		CsvReader reader = new CsvReader(this.filename);
		reader.setDelimiter('	'); // tabulador
		
		
		
		
		db conexion = new db();
		
		while (reader.readRecord()) {
			registros++;
			
			String unicom = reader.get(3);
			String ruta = reader.get(4);
			String itin = reader.get(5);
			String ciclo = reader.get(6);
			String fecha = reader.get(13);
			String cadena = String.format("unicom (%s) ruta (%s) itin (%s) ciclo (%s) fecha (%s)", 
					reader.get(3), reader.get(4), reader.get(5), reader.get(6), reader.get(13));
			System.out.println("Leyendo fila: " + cadena);
			
			try { 
				String sql = "UPDATE lecturas SET fecha_lectura=? WHERE unicom=? and ruta=? and num_itim=? and ciclo=? ";
				
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1,fecha);
				pst.setString(2,unicom);
				pst.setString(3,ruta);
				pst.setString(4,itin);
				pst.setString(5,ciclo);
				int reg = conexion.Update(pst);
	            if (reg > 0 ) {
	            	cnt = cnt + reg;
	            	conexion.Commit();
	            }
            
			}catch (SQLException e) {
				System.out.println("Error: " + e.getMessage());
				Utilidades.AgregarLog("Error: " + e.getMessage());
				//LogSQL log = new LogSQL(conexion);
				//log.Add(e.getMessage(), this.filename, registros,"Archivo Recibe");
			}
			
		}
		
		System.out.println("Archivo parseado.  Registros: " + registros + " Subidos: " + cnt);
		Utilidades.AgregarLog("Archivo parseado.  Registros: " + registros + " Subidos: " + cnt);
		
		if (registros != cnt) {  // Error cargar todos los registros
			/*String sql = "INSERT INTO logUploadFiles (fecha,archivo,reg_total, reg_upload,tipo,estado) values (SYSDATETIME(),?,?,?,'update','ERROR')";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,this.filename);
			pst.setInt(2,registros);
			pst.setInt(3,cnt);
			conexion.Update(pst);*/
			
		}else {
			/*String sql = "INSERT INTO logUploadFiles (fecha,archivo,reg_total, reg_upload,tipo,estado) values (SYSDATETIME(),?,?,?,'update','UPLOAD')";
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
