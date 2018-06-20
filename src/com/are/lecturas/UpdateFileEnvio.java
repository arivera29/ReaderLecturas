package com.are.lecturas;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.csvreader.CsvReader;

public class UpdateFileEnvio {
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

	public UpdateFileEnvio(String filename) {
		super();
		this.filename = filename;
	}
	
	public boolean Parser() throws IOException, SQLException {
		CsvReader reader = new CsvReader(this.filename);
		reader.setDelimiter('	'); // tabulador
		
		
		
		
		db conexion = new db();
		
		while (reader.readRecord()) {
			registros++;
			
			String unicom = reader.get(0);
			String ruta = reader.get(1);
			String itin = reader.get(2);
			String ciclo = reader.get(3);
			String fecha = reader.get(9);
			
			File f = new File(this.filename);
			String fname = f.getName();
			fname = fname.replace("lese350", "lese355");
			
			try { 
				String sql = "UPDATE lecturas SET f_lect=? WHERE unicom=? and ruta=? and num_itim=? and ciclo=? ";
				
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
				//log.Add(e.getMessage(), this.filename, registros,"Archivo envio");
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
			conexion.Update(pst); */
			
		}else {
			/*
			String sql = "INSERT INTO logUploadFiles (fecha,archivo,reg_total, reg_upload,tipo,estado) values (SYSDATETIME(),?,?,?,'update','UPLOAD')";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,this.filename);
			pst.setInt(2,registros);
			pst.setInt(3,cnt);
			conexion.Update(pst); */
		}
		
		conexion.Commit();
		conexion.Close();
		reader.close();
		return true;
	}
}
