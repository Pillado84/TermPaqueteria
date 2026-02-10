package oop;

import java.time.LocalDate;
import java.time.ZoneId;

import bibliotecas.GeneradorCodigoSeguimiento;

public class Envio {
	protected Long id;
	protected String codigoSeguimiento;
	protected Direccion destinatario; // direccion de envio
	protected Direccion remitente; // direccion de origen
	protected Transporte transporte;
	protected LocalDate fechaInicio;
	protected LocalDate fechaFin;
	protected Estado estado;
	
	public enum Estado {
		INICIO, TRANSITO, FIN, CANCELADO
	}
	
	public Envio (String codigoSeguimiento, Direccion destinatario, Direccion remitente, Transporte transporte) {
		this.codigoSeguimiento = codigoSeguimiento;
		this.destinatario = destinatario;
		this.remitente = remitente;
		this.transporte = transporte;
		this.fechaInicio = LocalDate.now(ZoneId.of("Europe/Madrid"));
		this.estado = Estado.INICIO;
	}
	
	public Envio (Direccion destinatario, Direccion remitente, Transporte transporte) {
		GeneradorCodigoSeguimiento generador = new GeneradorCodigoSeguimiento();
		this.codigoSeguimiento = generador.generarUnico();
		this.destinatario = destinatario;
		this.remitente = remitente;
		this.transporte = transporte;
		this.fechaInicio = LocalDate.now(ZoneId.of("Europe/Madrid"));
		this.estado = Estado.INICIO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoSeguimiento() {
		return codigoSeguimiento;
	}

	public void setCodigoSeguimiento(String codigoSeguimiento) {
		this.codigoSeguimiento = codigoSeguimiento;
	}

	public Direccion getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Direccion destinatario) {
		this.destinatario = destinatario;
	}

	public Direccion getRemitente() {
		return remitente;
	}

	public void setRemitente(Direccion remitente) {
		this.remitente = remitente;
	}

	public Transporte getTransporte() {
		return transporte;
	}

	public void setTransporte(Transporte transporte) {
		this.transporte = transporte;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Envio: [Codigo de seguimiento=" + codigoSeguimiento + ", destinatario=" + destinatario
				+ ", remitente=" + remitente + ", transporte=" + transporte + ", fechaInicio=" + fechaInicio
				+ ", fechaFin=" + fechaFin + ", estado=" + estado + "]";
	}
}
