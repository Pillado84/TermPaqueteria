package oop;

public class Transporte {
	protected String codigo;
	protected Vehiculo vehiculo;
	
	public enum Vehiculo {
		CAMION, FURGONETA, COCHE
	}
	
	public Transporte (String codigo) {
		this.codigo = codigo;
		this.vehiculo = Vehiculo.FURGONETA;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	@Override
	public String toString() {
		return "Transporte [codigo=" + codigo + ", vehiculo=" + vehiculo + "]";
	}
}
