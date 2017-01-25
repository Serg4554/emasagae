package com;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.googlecode.objectify.Key;

import static com.OfyHelper.ofy;

import com.googlecode.objectify.annotation.Load;
import com.gsonmaps.GoogleMaps;
import com.gsonmaps.Result;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class ControlBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final String GOOGLE_ID = "893681487329-8djeugqf5ahn7t55b8dpnlnq466v05uf.apps.googleusercontent.com";
    private final String GOOGLE_SECRET = "uV0DD-f1y2AERZ4RqR0pNNo3";
    
	@Load
	private Aviso avisoSeleccionado;

	@Load
	private Usuario usuarioActual;
	
	@Load
	private Operacion operacionSeleccionada;
	
	private String calle;
	private String numero;
	private String codigoPostal;
	private String descripcion;
	private String emailUsuario;
	private String error;
	private String googleID;
	private String googleToken;
	private List<Aviso> listaAvisos;
	private List<Aviso> listaAvisosUsuario;
	private List<Operacion> listaOperaciones;
	private String etiqueta;
	private double latitud;
	private double longitud;
	

	public ControlBean() {
	}
	
	@PostConstruct
    public void init() {
		error = "";
		usuarioActual = null;
		avisoSeleccionado = null;
		operacionSeleccionada = null;
		googleID = "";
		googleToken = "";
		emailUsuario = "";
		listaAvisos = getAllAvisos();
    }

	public void doGeocoding()
	{
		String respuesta = ""; //variable para almacenar la string de respuesta del servidor rest
		URL urlPeticionRest = null; //Url de petici�n y generaci�n de la string que la originar�
		String stringUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String calle = avisoSeleccionado.getCalle().trim(); 
		calle = calle.replace(" ", "+");
		String numero = Integer.toString(avisoSeleccionado.getNumero()).trim();
		String codigoPostal = Integer.toString(avisoSeleccionado.getCodigoPostal()).trim();
		stringUrl = stringUrl+calle+","+numero+","+codigoPostal;
		try {
			urlPeticionRest = new URL(stringUrl);
		} catch (MalformedURLException e) {
			System.err.println("Error al introducir al url");
			e.printStackTrace();
		}//creamos la url a geocodificar
		try {
			BufferedReader reader = new BufferedReader (new InputStreamReader (urlPeticionRest.openStream()));
			String linea;//abrimos un reader para procesar la respuesta y la procesamos
			while ((linea = reader.readLine())!=null)
			{
				respuesta+=linea;
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Error al procesar respuesta");
			e.printStackTrace();
		}
		//una vez tenemos la cadena JSON lo convertimos en un objeto JSON
		
		Gson gson = new Gson();
		GoogleMaps resultado = gson.fromJson(respuesta, GoogleMaps.class);
		
		List<Result> listaResultados = resultado.getResults();
		if(!listaResultados.isEmpty()) {
			latitud = listaResultados.get(0).getGeometry().getLocation().getLat();
			longitud = listaResultados.get(0).getGeometry().getLocation().getLng();
		} else {
			latitud = 0;
			longitud = 0;
		}
		//ya tenemos la longitud y la latitud en los atributos latitud y longitud del bean
	}
	
	public Aviso getAvisoSeleccionado() {
		return avisoSeleccionado;
	}

	public void setAvisoSeleccionado(Aviso avisoSeleccionado) {
		this.avisoSeleccionado = avisoSeleccionado;
	}
	
	public Operacion getOperacionSeleccionada() {
		return operacionSeleccionada;
	}
	
	public void setOperacionSeleccionada(Operacion operacionSeleccionada) {
		this.operacionSeleccionada = operacionSeleccionada;
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public void setUsuarioActual(Usuario usuarioActual) {
		this.usuarioActual = usuarioActual;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public String getError() {
		return error;
	}

	public List<Aviso> getListaAvisos() {
		return listaAvisos;
	}

	public void setListaAvisos(List<Aviso> listaAvisos) {
		this.listaAvisos = listaAvisos;
	}

	public List<Aviso> getListaAvisosUsuario() {
		return listaAvisosUsuario;
	}

	public void setListaAvisosUsuario(List<Aviso> listaAvisosUsuario) {
		this.listaAvisosUsuario = listaAvisosUsuario;
	}

	public List<Operacion> getListaOperaciones() {
		return listaOperaciones;
	}

	public void setListaOperaciones(List<Operacion> listaOperaciones) {
		this.listaOperaciones = listaOperaciones;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public List<Aviso> getAllAvisos() {
		return new ArrayList<>(ofy().load().type(Aviso.class).order("fechaCreacion").list());
	}
	
	public List<Aviso> getAllAvisos(Usuario usuario) {
		Key<Usuario> tUsuario = Key.create(Usuario.class, emailUsuario);
		ArrayList<Aviso> avisos = new ArrayList<>(ofy().load().type(Aviso.class).ancestor(tUsuario).order("fechaCreacion").list());
		
		return avisos;
	}

	private List<Operacion> getListaOperacionesAviso(Aviso aviso) {
		ArrayList<Operacion> operaciones = null;
		if (aviso != null) {
			Key<Aviso> tAviso = Key.create(Aviso.class, aviso.getId());
			operaciones = new ArrayList<>(ofy().load().type(Operacion.class).ancestor(tAviso).order("fecha").list());
		}
		
		return operaciones;
	}

	public String verAviso(Aviso aviso) {
		String redirect;
		
		if (aviso != null) {
			avisoSeleccionado = aviso;
			listaOperaciones = getListaOperacionesAviso(aviso);
			redirect = "detalleAviso";
		} else {
			redirect = "index";
		}
		
		return redirect;
	}
	
	public String doNuevoAviso() {
		error = "";
		calle = "";
		numero = "";
		codigoPostal = "";
		descripcion = "";
		avisoSeleccionado = new Aviso();
		
		return "editarAviso?faces-redirect=true";
	}

	public String doGuardarAviso() {
		error = "";
		
		Key<Usuario> tUsuario = Key.create(Usuario.class, emailUsuario);
		avisoSeleccionado.setTOriginador(tUsuario);
		
		avisoSeleccionado.setFechaCreacion(new Date());

		if (calle != null && !calle.isEmpty()) {
			avisoSeleccionado.setCalle(calle);
		} else {
			error = "Debe especificar una calle";
			return "crearAviso";
		}

		if (numero != null && !numero.isEmpty()) {
			try {
				avisoSeleccionado.setNumero(Integer.parseInt(numero));
			} catch(NumberFormatException e) {
				error = "El número debe ser numérico";
				return "crearAviso";
			}
		} else {
			error = "Debe especificar un número";
			return "editarAviso";
		}

		if (codigoPostal != null && !codigoPostal.isEmpty()) {
			try {
				avisoSeleccionado.setCodigoPostal(Integer.parseInt(codigoPostal));
			} catch(NumberFormatException e) {
				error = "El código postal debe ser numérico";
				return "editarAviso";
			}
		} else {
			error = "Debe especificar un código postal";
			return "editarAviso";
		}
		
		if (descripcion != null && !descripcion.isEmpty()) {
			avisoSeleccionado.setDescripcion(descripcion);
		} else {
			error = "Debe especificar una descripción";
			return "editarAviso";
		}
		
		if (etiqueta != null && !etiqueta.isEmpty()) {
			avisoSeleccionado.setEtiqueta(etiqueta);
		} else {
			error = "Debe especificar una etiqueta";
			return "editarAviso";
		}
		if (error.equals(""))
		{//si ha llegado hasta aqu� sin errores se a�ade la geolocalizacion
			doGeocoding();
			avisoSeleccionado.setLatitud(latitud);
			avisoSeleccionado.setLongitud(longitud);
		}
		
		ofy().save().entity(avisoSeleccionado).now();
		listaAvisos = getAllAvisos();
		listaAvisosUsuario = getAllAvisos(usuarioActual);
		avisoSeleccionado = null;
		
		return "index";
	}

	public String doEditar(Aviso aviso) {
		error = "";
		avisoSeleccionado = aviso;
		calle = aviso.getCalle();
		numero = ""+aviso.getNumero();
		codigoPostal = ""+aviso.getCodigoPostal();
		descripcion = aviso.getDescripcion();
		
		return "editarAviso";
	}
	
	public String doNuevaOperacion() {
		error = "";
		descripcion = "";
		operacionSeleccionada = new Operacion();
		//avisoSeleccionado
		
		return "editarOperacion?faces-redirect=true";
	}
	
	public String doEditarOperacion(Operacion operacion) {
		error = "";
		operacionSeleccionada = operacion;
		descripcion = operacion.getDescripcion();
		
		return "editarOperacion";
	}

	public String doGuardarOperacion() {
		error = "";
		
		
		operacionSeleccionada.setOriginador(emailUsuario);
		 
		Key<Aviso> tAviso = Key.create(Aviso.class, avisoSeleccionado.getId());
		operacionSeleccionada.setTAviso(tAviso);
		//operacionSeleccionada.setAviso(avisoSeleccionado);
		//ARREGLAR LO DE AVISO DE ARRIBA
		//operacionSeleccionada.setOriginador();
		//HACER LO DE ARRIBA
		operacionSeleccionada.setFecha(new Date());
	
		if (descripcion != null && !descripcion.isEmpty()) {
			operacionSeleccionada.setDescripcion(descripcion);
		} else {
			error = "Debe especificar una descripci�n";
			return "editarOperacion";
		}
		
		
		
		ofy().save().entity(operacionSeleccionada);
		
		operacionSeleccionada = ofy().load().type(Operacion.class).ancestor(tAviso).order("fecha").list().get(ofy().load().type(Operacion.class).ancestor(tAviso).order("fecha").list().size() -1);
		//ATENTO A ESTO
		
		Key<Operacion> tOperacion = Key.create(Operacion.class, operacionSeleccionada.getId());
		Collection<Key<Operacion>> aux = avisoSeleccionado.getOperacionesCollection();
		
		if(aux == null) {
			aux = new ArrayList<Key<Operacion>>();
		}
		aux.add(tOperacion);
		avisoSeleccionado.setOperacionesCollection(aux);
		
		
		listaOperaciones = this.getListaOperacionesAviso(avisoSeleccionado);
		operacionSeleccionada = null;
		
		//Hacer return al aviso concreto
		return verAviso(avisoSeleccionado);
	}
	
	public String doBorrarOperacion(Operacion operacion) {
		ofy().delete().entity(operacion).now();
		
		listaOperaciones = this.getListaOperacionesAviso(avisoSeleccionado);
		return verAviso(avisoSeleccionado);
	}
	
	public String doBorrar(Aviso aviso) {
		ofy().delete().entity(aviso).now();
		listaAvisos = getAllAvisos();
		listaAvisosUsuario = getAllAvisos(usuarioActual);
		
		return "index";
	}
	
	public String verOperacion(Operacion operacion) {
		if (operacion != null) {
			operacionSeleccionada = operacion;
			return "detalleOperacion";
		} else {
			return verAviso(avisoSeleccionado);
		}
	}
	
	public String getSuccessUrl() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestScheme() + "://" +
				FacesContext.getCurrentInstance().getExternalContext().getRequestServerName() + ":" +
				FacesContext.getCurrentInstance().getExternalContext().getRequestServerPort() +
				"/faces/loginSuccess.xhtml";
	}
	
	public void doGoogleLogin() {
		try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

			String code = request.getParameter("code");

			if (code != null && !code.equals("")) {
				URL url;
				URLConnection con;
				BufferedReader in;
				String content, linea;
				JSONObject tokenResult, infoResult;

				url = new URL("https://www.googleapis.com/oauth2/v4/token");
				con = url.openConnection();
				
				String urlParameters = "client_id=" + GOOGLE_ID + "&redirect_uri="+ getSuccessUrl() +
						"&client_secret=" + GOOGLE_SECRET + "&grant_type=authorization_code&code="+ code;
				System.out.println(urlParameters);
				byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
				int postDataLength = postData.length;

				con.setDoOutput(true);
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				con.setRequestProperty("charset", "utf-8");
				con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
				con.setUseCaches(false);

				try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
					wr.write(postData);
				}

				in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				content = "";
				while ((linea = in.readLine()) != null) {
					content += linea;
				}
				tokenResult = new JSONObject(content);

				if (tokenResult.getString("access_token") != null
						&& !tokenResult.getString("access_token").equals("")) {
					url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="
							+ tokenResult.getString("access_token"));
					con = url.openConnection();
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));

					content = "";
					while ((linea = in.readLine()) != null) {
						content += linea;
					}
					infoResult = new JSONObject(content);

					this.googleID = infoResult.getString("id");
					this.googleToken = tokenResult.getString("access_token");
					this.emailUsuario = infoResult.has("email") ? infoResult.getString("email") : "sin@email.com";
				} else {
					try {
						ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
						context.redirect(context.getRequestContextPath() + "/");
					} catch (IOException ex) {
						Logger.getLogger(ControlBean.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			} else {
				ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
				context.redirect(context.getRequestContextPath() + "/");
			}
		} catch (MalformedURLException ex) {
			Logger.getLogger(ControlBean.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException | JSONException | IllegalStateException ex) {
			Logger.getLogger(ControlBean.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		if(!googleID.isEmpty() && !googleToken.isEmpty() && !emailUsuario.isEmpty()) {
			Key<Usuario> usuario = Key.create(Usuario.class, emailUsuario);

			usuarioActual = ofy().load().key(usuario).get();
			if (usuarioActual == null) {
				usuarioActual = new Usuario(emailUsuario);
				ofy().save().entity(usuarioActual).now();
			}
			listaAvisosUsuario = getAllAvisos(usuarioActual);
		}
		
		try {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			context.redirect(context.getRequestContextPath() + "/");
		} catch (IOException ex) {
			Logger.getLogger(ControlBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public String doLogout() {
		error = "";
		usuarioActual = null;
		avisoSeleccionado = null;
		googleID = "";
		googleToken = "";
		emailUsuario = "";
		listaAvisosUsuario = null;
	    
	    return "index?faces-redirect=true";
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
}
