package com.are.lecturas;


import java.io.IOException;

import java.sql.SQLException;

import com.csvreader.CsvReader;

public class UploadFileEnvio {
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

	public UploadFileEnvio(String filename) {
		super();
		this.filename = filename;
	}
	
	public boolean Parser() throws IOException, SQLException {
		CsvReader reader = new CsvReader(this.filename);
		reader.setDelimiter('	'); // tabulador
		//int columnas = reader.getColumnCount();
		
		/*if (reader.getColumnCount() != 35) {
			throw new IOException("Error al Parsear.  Cantidad de columnas no validas (35) " + reader.getColumnCount() );
		}*/
		
		db conexion = new db();
		
		while (reader.readRecord()) {
			registros++;
			try { 
				String sql = "INSERT INTO lecturas (unicom,ruta,num_itim,ciclo,sec_reg,aol_fin,nif,cod_calle,num_puerta,duplicador,cgv_pm,acc_finca,acc_pm,nis_rad,cod_marca,"
	                    + "num_apa,tip_csmi,f_lect_ant,lect_ant,lect_max,lec_min,cgv_sum,nom_clie,num_rue,cod_tar,dpto,municipio,localidad,direccion,sec_nis,ref_dir,"
	                    + "tip_asoc,cte,tip_serv,nic,fecha_carga,usuario_carga,filename)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
	                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
	                    + "?,?,?,?,SYSDATETIME(),'robot',?)";
				
				java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
				pst.setString(1,reader.get(0));
				pst.setString(2,reader.get(1));
				pst.setString(3,reader.get(2));
				pst.setString(4,reader.get(3));
				pst.setString(5,reader.get(4));
				pst.setString(6,reader.get(5));
				pst.setString(7,reader.get(6));
				pst.setString(8,reader.get(7));
				pst.setString(9,reader.get(8));
				pst.setString(10,reader.get(9));
				pst.setString(11,reader.get(10));
				pst.setString(12,reader.get(11));
				pst.setString(13,reader.get(12));
				pst.setString(14,reader.get(13));
				pst.setString(15,reader.get(14));
				pst.setString(16,reader.get(15));
				pst.setString(17,reader.get(16));
				pst.setString(18,reader.get(17));
				pst.setString(19,reader.get(18));
				pst.setString(20,reader.get(19));
				pst.setString(21,reader.get(20));
				pst.setString(22,reader.get(21));
				pst.setString(23,reader.get(22));
				pst.setString(24,reader.get(23));
				pst.setString(25,reader.get(24));
				pst.setString(26,reader.get(25));
				pst.setString(27,reader.get(26));
				pst.setString(28,reader.get(27));
				pst.setString(29,reader.get(28));
				pst.setString(30,reader.get(29));
				pst.setString(31,reader.get(30));
				pst.setString(32,reader.get(31));
				pst.setString(33,reader.get(32));
				pst.setString(34,reader.get(33));
				pst.setString(35,reader.get(34));
				pst.setString(36,filename);
				
	            if (conexion.Update(pst) > 0 ) {
	            	cnt++;
	            	conexion.Commit();
	            }
            
			}catch (SQLException e) {
				System.out.println("Error: " + e.getMessage());
				//AgregarLog("Error: " + e.getMessage());
				//LogSQL log = new LogSQL(conexion);
				//log.Add(e.getMessage(), this.filename, registros,"Archivo envio");
			}
			
		}
		
		System.out.println("Archivo parseado.  Registros: " + registros + " Subidos: " + cnt);
		Utilidades.AgregarLog("Archivo parseado.  Registros: " + registros + " Subidos: " + cnt);
		
		if (registros != cnt) {  // Error cargar todos los registros
			/*String sql = "INSERT INTO logUploadFiles (fecha,archivo,reg_total, reg_upload,tipo,estado) values (SYSDATETIME(),?,?,?,'envio','ERROR')";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,this.filename);
			pst.setInt(2,registros);
			pst.setInt(3,cnt);
			conexion.Update(pst);
			*/
			
		}else {
			/*
			String sql = "INSERT INTO logUploadFiles (fecha,archivo,reg_total, reg_upload,tipo,estado) values (SYSDATETIME(),?,?,?,'envio','UPLOAD')";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1,this.filename);
			pst.setInt(2,registros);
			pst.setInt(3,cnt);
			conexion.Update(pst);
			*/
		}
		
		conexion.Commit();
		conexion.Close();
		reader.close();
		
		
		return true;
	}
}
