package com;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import java.io.Serializable;


@Entity
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Index
    private String email;
    
    public Usuario() {
    	//Usuario vacío
    }

    public Usuario(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rest.Usuario[ email=" + email + " ]";
    }
    
}
