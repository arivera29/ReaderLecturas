package com.are.lecturas;

import java.sql.SQLException;

public class LogSQL {
	private db conexion;

	public LogSQL(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean Add(String mensaje, String filename, int fila, String comentario) throws SQLException {
		String sql = "INSERT INTO LogSQL (mensaje,archivo,fila,fecha,comentario) VALUES (?,?,?,SYSDATETIME(),?)";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, mensaje);
		pst.setString(2, filename);
		pst.setInt(3, fila);
		pst.setString(4, comentario);
		
		if (conexion.Update(pst) > 0) {
			conexion.Commit();
			return true;
		}
		
		return false;
	}

}
