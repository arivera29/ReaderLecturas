package com.are.lecturas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorUnicom {
	private db conexion;
	private Unicom unicom;
	
	public db getConexion() {
		return conexion;
	}
	public void setConexion(db conexion) {
		this.conexion = conexion;
	}
	public Unicom getUnicom() {
		return unicom;
	}
	public ControladorUnicom(db conexion) {
		super();
		this.conexion = conexion;
	}
	
	public boolean add(Unicom unicom) throws SQLException{
		boolean result = false;
			String sql = "INSERT INTO unicom (unicom,descripcion,estado) VALUES (?,?,?)";
			
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, unicom.getCodigo());
			pst.setString(2, unicom.getDescripcion());
			pst.setInt(3, unicom.getEstado());
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
		return result;
	}

	public boolean update(Unicom unicom,String key) throws SQLException {
		boolean result = false;

		String sql = "UPDATE unicom SET unicom=?,descripcion=?, estado=? WHERE unicom=?";
		
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		pst.setString(1, unicom.getCodigo());
		pst.setString(2, unicom.getDescripcion());
		pst.setInt(3, unicom.getEstado());
		pst.setString(4, key);
		
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean remove(String key) throws SQLException {
		boolean result = false;
			String sql = "DELETE FROM unicom WHERE unicom=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, key);
			
			if (conexion.Update(pst) > 0) {
				conexion.Commit();
				result = true;
			}
			
		return result;	
	}
	
	public boolean Find(String key) throws SQLException {
		boolean result = false;
		    this.unicom = null;
			String sql = "SELECT unicom,descripcion,estado FROM unicom WHERE unicom=?";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			pst.setString(1, key);
			ResultSet rs = conexion.Query(pst);
			if (rs.next()) {
				unicom = new Unicom();
				unicom.setCodigo((String)rs.getString("unicom"));
				unicom.setDescripcion((String)rs.getString("descripcion"));
				unicom.setEstado(rs.getInt("estado"));
				result = true;
			}
			rs.close();
			
		return result;	
	}
	
	public ArrayList<Unicom> List() throws SQLException {
		ArrayList<Unicom> lista = new ArrayList<Unicom>();
		String sql = "SELECT unicom,descripcion,estado FROM unicom ORDER BY unicom";
		java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
		ResultSet rs = conexion.Query(pst);
		while (rs.next()) {
			Unicom unicom = new Unicom();
			unicom.setCodigo((String)rs.getString("unicom"));
			unicom.setDescripcion((String)rs.getString("descripcion"));
			unicom.setEstado(rs.getInt("estado"));
			lista.add(unicom);
		}
		
		return lista;
	}
	
	public String CreateSelectHTML(String id) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		try {
			ArrayList<Unicom> lista = this.List();
			if (lista.size() > 0) {
				for (Unicom unicom : lista) {
					strHtml += "<option value='" + unicom.getCodigo() + "'>" + unicom.getDescripcion()+ "  (" + unicom.getCodigo() + ")</option>";
				}
			}
		} catch (SQLException e) {
			
		}
		strHtml += "<select>";
		return strHtml;
	}
	
	public String CreateSelectHTML(String id,String key) {
		String strHtml = "<select id='" + id + "' name='" + id + "'>" ;
		try {
			ArrayList<Unicom> lista = this.List();
			if (lista.size() > 0) {
				for (Unicom unicom : lista) {
					String c = "";
					if (key.equals(unicom.getCodigo())) c="selected";
		
					strHtml += "<option value='" + unicom.getCodigo() + "' " + c + ">" + unicom.getDescripcion() + "  (" + unicom.getCodigo() + ")</option>";
				}
			}
		} catch (SQLException e) {
			
		}
		strHtml += "<select>";
		return strHtml;
	}

}
