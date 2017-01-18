package com;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import java.io.Serializable;
import java.util.Date;


@Entity
public class Operacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private Long id;
    
    @Parent
    private Key<Aviso> tAviso;
    private Aviso aviso;
    private Date fecha;
    private String descripcion;
    private Key<Usuario> tOriginador;
    private Usuario originador;

    public Operacion() {
    	//Operación vacía
    }
    
    public Operacion(Aviso aviso, Usuario originador, String descripcion, Date fecha) {
    	this.tAviso = Key.create(Aviso.class, aviso.getId());
    	this.aviso = aviso;
    	this.originador = originador;
    	this.tOriginador = Key.create(Usuario.class, originador.getEmail());
    	this.descripcion = descripcion;
    	this.fecha = fecha;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<Aviso> getTAviso() {
		return tAviso;
	}

	public void setTAviso(Key<Aviso> tAviso) {
		this.tAviso = tAviso;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Key<Usuario> getTOriginador() {
		return tOriginador;
	}

	public void setTOriginador(Key<Usuario> tOriginador) {
		this.tOriginador = tOriginador;
	}
	
	public Usuario getOriginador() {
		return this.originador;
	}
	
	public void setOriginador(Usuario originador) {
		this.originador = originador;
	}
	
	public Aviso getAviso() {
		return this.aviso;
	}
	
	public void setAviso(Aviso aviso) {
		this.aviso = aviso;
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
        if (!(object instanceof Operacion)) {
            return false;
        }
        Operacion other = (Operacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rest.Operacion[ id=" + id + " ]";
    }
    
}
