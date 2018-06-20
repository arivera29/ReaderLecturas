package com.are.lecturas;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class UploadInfo {

	final static int BUFFER = 2048;
	static long reg_total = 0;
	static long reg_subidos = 0;
	final static String DIR_FILES_LECTURA = "C:\\LECTURAS\\";
	final static String DIR_FILES_TEMPORAL = "C:\\TEMPORAL\\";
	final static String DIR_PROCESADOS_RECIBE = "\\procesados\\recibe";
	final static String DIR_PROCESADOS_ENVIO = "\\procesados\\envio";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Date fecha = new Date();
		System.out.println("Fecha y hora de Inicio: " + fecha.toString() );
		Utilidades.AgregarLog("Fecha y hora de Inicio: " + fecha.toString() );

		db conexion = null;
		ArrayList<Unicom> lista = new ArrayList<>();
		try {
			conexion = new db();
			ControladorUnicom controlador = new ControladorUnicom(conexion);
			lista = controlador.List();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}



		for (Unicom unicom : lista) {
			String directorio = DIR_FILES_LECTURA + unicom.getCodigo().trim() + "\\bacenv";
			String OutDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + "\\envio" ;
			File fout = new File(OutDir);
			if (!fout.exists()) {
				fout.mkdirs();
			}
			
			File dir = new File(directorio);
			if (dir.exists()) {
				File[] ficheros = dir.listFiles(Utilidades.FilterFileLess355Comprimido());
				for (int x = 0; x < ficheros.length; x++) {
					String ruta = ficheros[x].getPath();
					if (Utilidades.DescomprimirArchivo(ruta, OutDir)) {
						Utilidades.moverFichero(ruta, DIR_FILES_LECTURA + unicom.getCodigo().trim() + DIR_PROCESADOS_ENVIO);
					}
				}
				
				Utilidades.moverFicherosNoComprimidos(directorio, OutDir, "lese0355.");
				
			} else {
				System.out.println("Directorio no existe: " + dir.getPath());
				Utilidades.AgregarLog("Directorio no existe: " + dir.getPath());
			}
			
			

		}
		
		
		ProcesarArchivosEnvio();  // Upload Files Database
		
		System.out.println("Proceso Upload Archivos Envio finalizado");
		Utilidades.AgregarLog("Proceso Upload Archivos Envio finalizado");
		System.out.println("Registros leidos: " + reg_total);
		Utilidades.AgregarLog("Registros leidos: " + reg_total);
		System.out.println("Registros subidos: " + reg_subidos);
		Utilidades.AgregarLog("Registros subidos: " + reg_subidos);
		
		
		reg_total=0;
		reg_subidos =0;
		
		
		
		for (Unicom unicom : lista) {
			String directorio = DIR_FILES_LECTURA + unicom.getCodigo().trim() + "\\bacenv";
			String OutDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + "\\envio" ;
			File fout = new File(OutDir);
			if (!fout.exists()) {
				fout.mkdirs();
			}
			
			File dir = new File(directorio);
			if (dir.exists()) {
				File[] ficheros = dir.listFiles(Utilidades.FilterFileLess350Comprimido());
				for (int x = 0; x < ficheros.length; x++) {

					String ruta = ficheros[x].getPath();
					if (Utilidades.DescomprimirArchivo(ruta, OutDir)) {
						Utilidades.moverFichero(ruta, DIR_FILES_LECTURA + unicom.getCodigo().trim() + DIR_PROCESADOS_ENVIO);
					}
				}
				
				Utilidades.moverFicherosNoComprimidos(directorio, OutDir, "lese0350");
				
			} else {
				System.out.println("Directorio no existe: " + dir.getPath());
				Utilidades.AgregarLog("Directorio no existe: " + dir.getPath());
			}
			
			
		}
		
		ActualizarArchivosEnvio();
		
		System.out.println("Proceso Actualizacion registros Envio finalizado");
		Utilidades.AgregarLog("Proceso Actualizacion registros Envio finalizado");
		System.out.println("Registros leidos: " + reg_total);
		Utilidades.AgregarLog("Registros leidos: " + reg_total);
		System.out.println("Registros subidos: " + reg_subidos);
		Utilidades.AgregarLog("Registros subidos: " + reg_subidos);
		
		
		
		// Iniciando proceso de actualizacion de lecturas
		
		
		
		reg_total=0;
		reg_subidos =0;
		
		for (Unicom unicom : lista) {
			String directorio = DIR_FILES_LECTURA + unicom.getCodigo().trim() + "\\bacrec";
			String OutDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + "\\recibe" ;
			File fout = new File(OutDir);
			if (!fout.exists()) {
				fout.mkdirs();
			}
			
			File dir = new File(directorio);
			if (dir.exists()) {
				File[] ficheros = dir.listFiles(Utilidades.FilterFileLess452Comprimido());
				for (int x = 0; x < ficheros.length; x++) {
					String ruta = ficheros[x].getPath();
					if (Utilidades.DescomprimirArchivo(ruta, OutDir)) {
						Utilidades.moverFichero(ruta, DIR_FILES_LECTURA + unicom.getCodigo().trim() +DIR_PROCESADOS_RECIBE);
					}
				}
				
				Utilidades.moverFicherosNoComprimidos(directorio, OutDir, "lese0452");
				
			}else {
				System.out.println("Directorio no existe: " + dir.getPath());
				Utilidades.AgregarLog("Directorio no existe: " + dir.getPath());
			}
			
			
		}
		

		// Procesando Otros archivos descomprimidos
		
		
		ProcesarArchivosRecibe();
		
		
		System.out.println("Proceso Upload Archivos Recibe finalizado");
		Utilidades.AgregarLog("Proceso Upload Archivos Recibe finalizado");
		System.out.println("Registros leidos: " + reg_total);
		Utilidades.AgregarLog("Registros leidos: " + reg_total);
		System.out.println("Registros subidos: " + reg_subidos);
		Utilidades.AgregarLog("Registros subidos: " + reg_subidos);
		
		
		
		
		reg_total=0;
		reg_subidos =0;
		
		for (Unicom unicom : lista) {
			String directorio = DIR_FILES_LECTURA + unicom.getCodigo().trim() + "\\bacrec";
			String OutDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + "\\recibe" ;
			File fout = new File(OutDir);
			if (!fout.exists()) {
				fout.mkdirs();
			}
			
			File dir = new File(directorio);
			if (dir.exists()) {
				File[] ficheros = dir.listFiles(Utilidades.FilterFileLess451Comprimido());
				for (int x = 0; x < ficheros.length; x++) {
					String ruta = ficheros[x].getPath();
					if (Utilidades.DescomprimirArchivo(ruta, OutDir)) {
						Utilidades.moverFichero(ruta, DIR_FILES_LECTURA + unicom.getCodigo().trim() +  DIR_PROCESADOS_RECIBE);
					}
				}
				
				Utilidades.moverFicherosNoComprimidos(directorio, OutDir, "lese0451");
				
			}else {
				System.out.println("Directorio no existe: " + dir.getPath());
				Utilidades.AgregarLog("Directorio no existe: " + dir.getPath());
			}
			
			
		}
		
		ActualizarArchivosRecive();
		
		System.out.println("Proceso Actualizar Archivos Recibe finalizado");
		Utilidades.AgregarLog("Proceso Actualizar Archivos Recibe finalizado");
		System.out.println("Registros leidos: " + reg_total);
		Utilidades.AgregarLog("Registros leidos: " + reg_total);
		System.out.println("Registros subidos: " + reg_subidos);
		Utilidades.AgregarLog("Registros subidos: " + reg_subidos);
		
		
		Clasificar();
		
		fecha = new Date();
		System.out.println("Fecha y hora de Fin: " + fecha.toString() );
		Utilidades.AgregarLog("Fecha y hora de Fin: " + fecha.toString());
		
	}

	public static void ProcesarArchivosEnvio() {
		System.out.println("Iniciando proceso de Upload lecturas");
		Utilidades.AgregarLog("Iniciando proceso de Upload lecturas");
		db conexion = null;
		ArrayList<Unicom> lista = new ArrayList<>();
		try {
			conexion = new db();
			ControladorUnicom controlador = new ControladorUnicom(conexion);
			lista = controlador.List();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for (Unicom unicom : lista) {
			System.out.println("Procesando unicom " + unicom.getCodigo() );
			Utilidades.AgregarLog("Procesando unicom " + unicom.getCodigo());
			String OutDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + "\\envio" ;
			String moveDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + DIR_PROCESADOS_ENVIO ;
			
			File dir = new File(OutDir);
			if (dir.exists()) {
				File[] ficheros = dir.listFiles();
				for (int x = 0; x < ficheros.length; x++) {
					System.out.println("Procesando archivo: " + ficheros[x].getPath());
					Utilidades.AgregarLog("Procesando archivo: " + ficheros[x].getPath());
					if (ficheros[x].exists()) {
						UploadFileEnvio parser = new UploadFileEnvio(ficheros[x].getPath());
						try {
							if (parser.Parser()) {
								System.out.println("Archivo " + ficheros[x].getPath() + " UPLOAD!!");
								File f = new File(ficheros[x].getPath());
								if (f.exists()) {
									Utilidades.moverFichero(f.getPath(), moveDir);
									System.out.println("Archivo movido" + ficheros[x].getPath());
									Utilidades.AgregarLog("Archivo movido " + ficheros[x].getPath());
								}
							}else {
								System.out.println("Archivo " + ficheros[x].getPath() + " ERROR!!");
								Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " ERROR!!");
							}
							reg_total += parser.getContadorRegistros();
							reg_subidos += parser.getContadorRegistrosSubidos();
							
						} catch (IOException | SQLException e) {
							// TODO Auto-generated catch block
							System.out.println(e.getMessage());
							Utilidades.AgregarLog(e.getMessage());
						}
					}else {
						System.out.println("Archivo " + ficheros[x].getPath() + " no existe.");
						Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " no existe.");
					}
				}
			}
		}
	}
	

	public static void ActualizarArchivosEnvio() {
		System.out.println("Iniciando proceso de Actualizacion fecha lecturas");
		Utilidades.AgregarLog("Iniciando proceso de Actualizacion fecha lecturas");
		db conexion = null;
		ArrayList<Unicom> lista = new ArrayList<>();
		try {
			conexion = new db();
			ControladorUnicom controlador = new ControladorUnicom(conexion);
			lista = controlador.List();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for (Unicom unicom : lista) {
			System.out.println("Procesando unicom " + unicom.getCodigo() );
			Utilidades.AgregarLog("Procesando unicom " + unicom.getCodigo() );
			String OutDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + "\\envio" ;
			String moveDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + DIR_PROCESADOS_ENVIO ;
			File dir = new File(OutDir);
			if (dir.exists()) {
				File[] ficheros = dir.listFiles();
				for (int x = 0; x < ficheros.length; x++) {
					if (ficheros[x].getName().contains("lese0350")) {
						System.out.println("Procesando archivo: " + ficheros[x].getPath());
						Utilidades.AgregarLog("Procesando archivo: " + ficheros[x].getPath());
						if (ficheros[x].exists()) {
							UpdateFileEnvio parser = new UpdateFileEnvio(ficheros[x].getPath());
							try {
								if (parser.Parser()) {
									System.out.println("Archivo " + ficheros[x].getPath() + " UPLOAD!!");
									Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " UPLOAD!!");
									File f = new File(ficheros[x].getPath());
									if (f.exists()) {
										Utilidades.moverFichero(f.getPath(), moveDir);
										System.out.println("Archivo movido " + ficheros[x].getPath());
										Utilidades.AgregarLog("Archivo movido" + ficheros[x].getPath());
									}
								}else {
									System.out.println("Archivo " + ficheros[x].getPath() + " ERROR!!");
									Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " ERROR!!");
								}
								reg_total += parser.getContadorRegistros();
								reg_subidos += parser.getContadorRegistrosSubidos();
								
							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								System.out.println(e.getMessage());
								Utilidades.AgregarLog(e.getMessage());
							}
						}else {
							System.out.println("Archivo " + ficheros[x].getPath() + " no existe.");
							Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " no existe.");
						}
					}
				}
			}
		}
	}
	
	public static void ActualizarArchivosRecive() {
		System.out.println("Iniciando proceso de Actualizacion fecha Recibe");
		Utilidades.AgregarLog("Iniciando proceso de Actualizacion fecha Recibe");
		db conexion = null;
		ArrayList<Unicom> lista = new ArrayList<>();
		try {
			conexion = new db();
			ControladorUnicom controlador = new ControladorUnicom(conexion);
			lista = controlador.List();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for (Unicom unicom : lista) {
			System.out.println("Procesando unicom " + unicom.getCodigo() );
			Utilidades.AgregarLog("Procesando unicom " + unicom.getCodigo() );
			String OutDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + "\\recibe" ;
			String moveDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + DIR_PROCESADOS_RECIBE ;
			File dir = new File(OutDir);
			if (dir.exists()) {
				File[] ficheros = dir.listFiles();
				for (int x = 0; x < ficheros.length; x++) {
					if (ficheros[x].getName().contains("lese0451")) { 
						System.out.println("Procesando archivo: " + ficheros[x].getPath());
						Utilidades.AgregarLog("Procesando archivo: " + ficheros[x].getPath());
						if (ficheros[x].exists()) {
							UpdateFileRecibe parser = new UpdateFileRecibe(ficheros[x].getPath());
							try {
								if (parser.Parser()) {
									System.out.println("Archivo " + ficheros[x].getPath() + " UPLOAD!!");
									Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " UPLOAD!!");
									File f = new File(ficheros[x].getPath());
									if (f.exists()) {
										Utilidades.moverFichero(f.getPath(), moveDir);
										System.out.println("Archivo movido " + ficheros[x].getPath());
										Utilidades.AgregarLog("Archivo movido" + ficheros[x].getPath());
									}
								}else {
									System.out.println("Archivo " + ficheros[x].getPath() + " ERROR!!");
									Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " ERROR!!");
								}
								reg_total += parser.getContadorRegistros();
								reg_subidos += parser.getContadorRegistrosSubidos();
								
							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								System.out.println(e.getMessage());
								Utilidades.AgregarLog(e.getMessage());
							}
						}else {
							System.out.println("Archivo " + ficheros[x].getPath() + " no existe.");
							Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " no existe.");
						}
					}
				}
			}
			
		}
	}

	
	public static void ProcesarArchivosRecibe() {
		db conexion = null;
		ArrayList<Unicom> lista = new ArrayList<>();
		try {
			conexion = new db();
			ControladorUnicom controlador = new ControladorUnicom(conexion);
			lista = controlador.List();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for (Unicom unicom : lista) {
			String OutDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + "\\recibe" ;
			String moveDir = DIR_FILES_TEMPORAL + unicom.getCodigo().trim() + DIR_PROCESADOS_RECIBE ;
			File dir = new File(OutDir);
			if (dir.exists()) {
				File[] ficheros = dir.listFiles();
				for (int x = 0; x < ficheros.length; x++) {
					System.out.println("Procesando archivo: " + ficheros[x].getPath());
					Utilidades.AgregarLog("Procesando archivo: " + ficheros[x].getPath());
					
					UploadFileRecibe parser = new UploadFileRecibe(ficheros[x].getPath());
					
					try {
						if (parser.Parser()) {
							System.out.println("Archivo " + ficheros[x].getPath() + " UPLOAD!!");
							Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " UPLOAD!!");
							
							File f = new File(ficheros[x].getPath());
							Utilidades.moverFichero(f.getPath(), moveDir);
							
							System.out.println("Archivo movido " +ficheros[x].getPath());
							Utilidades.AgregarLog("Archivo movido " + ficheros[x].getPath());
						}else {
							System.out.println("Archivo " + ficheros[x].getPath() + " ERROR!!");
							Utilidades.AgregarLog("Archivo " + ficheros[x].getPath() + " ERROR!!");
						}
						reg_total += parser.getContadorRegistros();
						reg_subidos += parser.getContadorRegistrosSubidos();
						
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
						Utilidades.AgregarLog(e.getMessage());
					}
				}
				
			}
		}
	}

	public static void Clasificar() {
		System.out.println("Iniciando proceso de clasificacion de lecturas");
		Utilidades.AgregarLog("Iniciando proceso de clasificacion de lecturas");
		db conexion = null;
		long contador=0;
		try {
			conexion = new db();
			String sql = "SELECT distinct unicom, num_itim,ruta FROM lecturas WHERE fecha_clasificacion is null ORDER BY unicom,ruta,num_itim";
			java.sql.PreparedStatement pst = conexion.getConnection().prepareStatement(sql);
			java.sql.ResultSet rs = conexion.Query(pst);
			while (rs.next()) {
				sql = "select clasificacion,tipologia,mixto from itinerario where unicom=? and ruta=? and itinerario=? and estado=1";
				java.sql.PreparedStatement pst2 = conexion.getConnection().prepareStatement(sql);
				pst2.setString(1, rs.getString("unicom"));
				pst2.setString(2, rs.getString("ruta"));
				pst2.setString(3, rs.getString("num_itim"));
				
				java.sql.ResultSet rs2 = conexion.Query(pst2);
				if (rs2.next()) {  // Se encontro el itinerario
					System.out.println("Clasificando lecturas: Unicom: " +  rs.getString("unicom") + " Ruta: " + rs.getString("ruta") + " Itinerario: " + rs.getString("num_itim") );
					Utilidades.AgregarLog("Clasificando lecturas: Unicom: " +  rs.getString("unicom") + " Ruta: " + rs.getString("ruta") + " Itinerario: " + rs.getString("num_itim"));
					sql = "UPDATE lecturas SET clasificacion=?, tipologia=?, mixto=?, "
							+ "usuario_clasificacion='robot', fecha_clasificacion =SYSDATETIME() "
							+ " WHERE unicom=? AND ruta=? AND num_itim=? "
							+ " AND fecha_clasificacion is null ";
					
					java.sql.PreparedStatement pst3 = conexion.getConnection().prepareStatement(sql);
					pst3.setString(1, rs2.getString("clasificacion"));
					pst3.setString(2, rs2.getString("tipologia"));
					pst3.setString(3, rs2.getString("mixto"));
					pst3.setString(4, rs.getString("unicom"));
					pst3.setString(5, rs.getString("ruta"));
					pst3.setString(6, rs.getString("num_itim"));
					int filas = conexion.Update(pst3);
					if (filas > 0 ) {
						contador += filas;
						System.out.println("Lecturas clasificadas: " + filas);
						Utilidades.AgregarLog("Lecturas clasificadas: " + filas);
						conexion.Commit();
					}
					
				}
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conexion != null) {
				try {
					conexion.Close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Proceso finalizado");
		Utilidades.AgregarLog("Proceso finalizado");
		System.out.println("Total lecturas clasificadas: " + contador);
		Utilidades.AgregarLog("Total lecturas clasificadas: " + contador);
		
	}
	
	
	
	
}
