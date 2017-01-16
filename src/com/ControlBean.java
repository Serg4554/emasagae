package com;
 

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import com.googlecode.objectify.Key;

import static com.OfyHelper.ofy;
import com.googlecode.objectify.annotation.Load;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.List;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.ServletContextEvent;



//import operacionws.Operacion;
//import operacionws.OperacionWS_Service;


/**
 *
 * @author shiba
 */
@ManagedBean
@SessionScoped
public class ControlBean implements Serializable {

	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Load
	Aviso avisoSeleccionado;
    String latitud, longitud, error, ubicacion, dia, mes, anyo, observaciones;
    String emailUsuario;
    List<Aviso> listaAvisosUsuario;
    List<Operacion> listaOperaciones;
    @Load
    Usuario usuarioActual;

    String latitudGPS;
    String longitudGPS;
    

    /**
     * Creates a new instance of ControlBean
     */
    public ControlBean() {
    }

    @PostConstruct
    public void Init(){
    	latitud = "";
    	longitud = ""; 
    	error = "";
    	ubicacion = "";
    	dia = "";
    	mes = "";
    	anyo = "";
    	observaciones = "";
    	latitudGPS = "";
    	longitudGPS = "";
    }


    public String getLatitudGPS() {
        return latitudGPS;
    }

    public void setLatitudGPS(String latitudGPS) {
        this.latitudGPS = latitudGPS;
    }

    public String getLongitudGPS() {
        return longitudGPS;
    }

    public void setLongitudGPS(String longitudGPS) {
        this.longitudGPS = longitudGPS;
    }
    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAnyo() {
        return anyo;
    }

    public void setAnyo(String anyo) {
        this.anyo = anyo;
    }


    public Aviso getAvisoSeleccionado() {
        return avisoSeleccionado;
    }

    public void setAvisoSeleccionado(Aviso avisoSeleccionado) {
        this.avisoSeleccionado = avisoSeleccionado;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
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

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public String mostrarAvisos() {
    	comprobarUsuario();
        //obtenemos la lista de avisos del usuario
        Key<Usuario> user = Key.create(Usuario.class, emailUsuario);
       listaAvisosUsuario = ofy().load().type(Aviso.class).ancestor(user).list();
       if(listaAvisosUsuario == null){
    	   listaAvisosUsuario = new ArrayList<Aviso>();
       }
        return "mostrarAvisos";
        
        
        
    }

    public String verAviso(Aviso aviso) {
    	if(aviso != null){
        avisoSeleccionado = aviso;
        listaOperaciones = getListaOperacionesAviso(aviso);
        if(avisoSeleccionado.getPosGPS() != null && !avisoSeleccionado.getPosGPS().isEmpty()) {
            String[] posGPS = avisoSeleccionado.getPosGPS().split(";");
            latitudGPS = posGPS[0];
            longitudGPS = posGPS[1];
        } else {
            latitudGPS = null;
            longitudGPS = null;
        }
        return "detalleAviso";
    	}
    	else{
    		return"mostrarAvisos";
    	}
    }

    private List<Operacion> getListaOperacionesAviso(Aviso aviso) {
        List<Operacion> res = null;
        if(aviso != null){
        Key<Aviso> tAviso = Key.create(Aviso.class, aviso.getId());
        res = ofy().load().type(Operacion.class).ancestor(tAviso).list();
        }
        return res;
    }


    public void comprobarUsuario() {
        Key<Usuario> theUser = Key.create(Usuario.class, emailUsuario);

            usuarioActual = ofy().load().key(theUser).get();
            if (usuarioActual == null) {
                usuarioActual = new Usuario();
                usuarioActual.setEmail(emailUsuario);
                usuarioActual.setOperador(false);
                ofy().save().entity(usuarioActual).now();
            }
    
        
    }

    public String crearAviso() {
        error="";
        return "crearAviso";
    }

    public String doGuardar() {
    	Key<Usuario> theUser = Key.create(Usuario.class, emailUsuario);
        error="";
        String fecha;
        avisoSeleccionado = new Aviso();
        if(!dia.isEmpty() && !mes.isEmpty() && !anyo.isEmpty()){
            fecha = dia + "-" + mes + "-" + anyo;
        }else{
            error="Fecha no v�lida";
            return "crearAviso";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        formatter.applyPattern("dd-MM-yyyy");
        if (fecha != null && !fecha.trim().isEmpty()) {
            try {
                Date date = formatter.parse(fecha);
                avisoSeleccionado.setFechacreacion(date);
            } catch (ParseException ex) {
                error = "Fecha no v�lida";
                return "crearAviso";
            }
        }
        if(ubicacion==null || ubicacion.isEmpty()){
            error="La ubicaci�n no puede ser vac�a";
            return "crearAviso";
        }else{
            avisoSeleccionado.setUbicacion(ubicacion);
        }
        if(observaciones==null || observaciones.isEmpty()){
            error="El campo de observaciones no puede estar vac�o";
            return "crearAviso";
        }else{
            avisoSeleccionado.setObservaciones(observaciones);
        }
       /* 
        if(latitud==null || latitud.isEmpty() || longitud == null || longitud.isEmpty()){
            error="Los campos del posicionamiento GPS no pueden ser vac�os";
            return "crearAviso";
        }else{
            double lat = Double.parseDouble(latitud);
            double longi = Double.parseDouble(longitud);
            avisoSeleccionado.setPosGPS(lat+";"+longi);
        }
        */
        avisoSeleccionado.setPosGPS(1.1+";"+1.1);
        avisoSeleccionado.setEstado("En espera de confirmacion");
        avisoSeleccionado.setTheUser(theUser);
        ofy().save().entity(avisoSeleccionado).now();
        listaAvisosUsuario.add(avisoSeleccionado);
        return this.mostrarAvisos();
    }  
    
}
