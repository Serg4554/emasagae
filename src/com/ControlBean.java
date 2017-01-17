package com;

import com.googlecode.objectify.Key;
import static com.OfyHelper.ofy;
import com.googlecode.objectify.annotation.Load;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ControlBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
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
	List<Aviso> listaAvisos;
	List<Operacion> listaOperaciones;

	public ControlBean() {
	}
	
	@PostConstruct
    public void init(){
		error = "";
		usuarioActual = null;
		avisoSeleccionado = null;
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

	public List<Operacion> getListaOperaciones() {
		return listaOperaciones;
	}

	public void setListaOperaciones(List<Operacion> listaOperaciones) {
		this.listaOperaciones = listaOperaciones;
	}

	public void comprobarUsuario() {
		Key<Usuario> usuario = Key.create(Usuario.class, emailUsuario);

		usuarioActual = ofy().load().key(usuario).get();
		if (usuarioActual == null) {
			usuarioActual = new Usuario();
			usuarioActual.setEmail(emailUsuario);
			ofy().save().entity(usuarioActual).now();
		}

	}
	
	public List<Aviso> getAllAvisos() {
		List<Aviso> avisos;
		
		if(usuarioActual == null) {
			avisos = new ArrayList<>(ofy().load().type(Aviso.class).list());
		} else {
			Key<Usuario> tUsuario = Key.create(Usuario.class, emailUsuario);
			avisos = new ArrayList<>(ofy().load().type(Aviso.class).ancestor(tUsuario).order("fechaCreacion").list());
		}
		
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
		listaAvisos.add(avisoSeleccionado);
		
		return "index";
	}

}
