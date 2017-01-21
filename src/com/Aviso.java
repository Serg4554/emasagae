package com;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


@Entity
public class Aviso implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    private Long id;
    
    @Parent
    private Key<Usuario> tOriginador;

    @Index
    private Date fechaCreacion;
    
    private Usuario originador;
    private String descripcion;
    private String calle;
    private int numero;
    private int codigoPostal;
    private Collection<Key<Operacion>> operacionesCollection;
    private String etiqueta;

    public Aviso() {
    	//Aviso vacío
    }
    
    public Aviso(Usuario originador, Date fechaCreacion, String calle, int numero, int codigoPostal) {
    	this.originador = originador;
        this.tOriginador = Key.create(Usuario.class, originador.getEmail());
        this.fechaCreacion = fechaCreacion;
        this.calle = calle;
        this.numero = numero;
        this.codigoPostal = codigoPostal;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getOriginador() {
		return originador;
	}

	public void setOriginador(Usuario originador) {
		this.originador = originador;
	}

	public Key<Usuario> getTOriginador() {
		return tOriginador;
	}

	public void setTOriginador(Key<Usuario> tOriginador) {
		this.tOriginador = tOriginador;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(int codigoPostal) {
		this.codigoPostal = codigoPostal;
	}	

	public Collection<Key<Operacion>> getOperacionesCollection() {
		return operacionesCollection;
	}

	public void setOperacionesCollection(Collection<Key<Operacion>> operacionesCollection) {
		this.operacionesCollection = operacionesCollection;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aviso)) {
            return false;
        }
        Aviso other = (Aviso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rest.Aviso[ id=" + id + " ]";
    }
    
}
