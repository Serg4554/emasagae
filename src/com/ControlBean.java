package com;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.Key;
import static com.OfyHelper.ofy;
import com.googlecode.objectify.annotation.Load;
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
	Aviso avisoSeleccionado;

	@Load
	Usuario usuarioActual;
	
	String calle;
	String numero;
	String codigoPostal;
	String descripcion;
	String emailUsuario;
	String error;
	String googleID;
	String googleToken;
	List<Aviso> listaAvisos;
	List<Aviso> listaAvisosUsuario;
	List<Operacion> listaOperaciones;

	public ControlBean() {
	}
	
	@PostConstruct
    public void init() {
		error = "";
		usuarioActual = null;
		avisoSeleccionado = null;
		googleID = "";
		googleToken = "";
		emailUsuario = "";
		listaAvisos = getAllAvisos();
    }

	public Aviso getAvisoSeleccionado() {
		return avisoSeleccionado;
	}

	public void setAvisoSeleccionado(Aviso avisoSeleccionado) {
		this.avisoSeleccionado = avisoSeleccionado;
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
	
	public List<Aviso> getAllAvisos() {
		return new ArrayList<>(ofy().load().type(Aviso.class).order("fechaCreacion").list());
	}
	
	public List<Aviso> getAllAvisos(Usuario usuario) {
		Key<Usuario> tUsuario = Key.create(Usuario.class, emailUsuario);
		ArrayList<Aviso> avisos = new ArrayList<>(ofy().load().type(Aviso.class).ancestor(tUsuario).order("fechaCreacion").list());
		
		return avisos;
	}

	private List<Operacion> getListaOperacionesAviso(Aviso aviso) {
		List<Operacion> operaciones = null;
		if (aviso != null) {
			Key<Aviso> tAviso = Key.create(Aviso.class, aviso.getId());
			operaciones = ofy().load().type(Operacion.class).ancestor(tAviso).list();
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
	
	public String doBorrar(Aviso aviso) {
		ofy().delete().entity(aviso).now();
		listaAvisos = getAllAvisos();
		listaAvisosUsuario = getAllAvisos(usuarioActual);
		
		return "index";
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
				String urlParameters = "client_id=" + GOOGLE_ID
						+ "&redirect_uri=http%3A%2F%2Flocalhost%3A8888%2Ffaces%2FloginSuccess.xhtml&client_secret=" + GOOGLE_SECRET + "&grant_type=authorization_code&code=" + code;
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
}
