/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author nacho
 */
@Entity
public class Operacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String descripcion;
    private Date fecha;
    @Parent
    private Key<Aviso> tAviso;
    private Key<Usuario> theUser;

    public Operacion() {
    }

    public Key<Aviso> getAviso() {
        return tAviso;
    }

    public void setAviso(Key<Aviso> aviso) {
        this.tAviso = aviso;
    }

    public Key<Usuario> getTheUser() {
        return theUser;
    }

    public void setTheUser(Key<Usuario> theUser) {
        this.theUser = theUser;
    }

    public Operacion(String id) {
        this.id = id;
    }

    public Operacion(String id, String descripcion, Date fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }
    
    public Operacion(Aviso aviso, Usuario usuario,String id, String descripcion, Date fecha){
        this(id,descripcion,fecha);
        if(aviso != null){
            tAviso = Key.create(Aviso.class, aviso.getId());
        }
        if(usuario != null){
            theUser = Key.create(Usuario.class, usuario.getEmail());
        }
        else{
            theUser = Key.create(Usuario.class, "default");
        }
    
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
