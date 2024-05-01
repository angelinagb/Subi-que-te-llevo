package negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import excepciones.CantidadDePasajerosException;
import excepciones.ClienteExistenteException;
import excepciones.PedidoIncoherenteException;
import excepciones.ZonaInvalidaException;
import modelo.Chofer;
import modelo.Cliente;
import modelo.IVehiculo;
import modelo.Pedido;
import modelo.Vehiculo;
import modelo.Viaje;

public class Sistema {
	private static Sistema instance = null;
	private HashMap<String,Vehiculo> flota = new HashMap<String,Vehiculo>();
	protected ArrayList<Chofer> choferes = new ArrayList<Chofer>();
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private ArrayList<Viaje> viajes = new ArrayList<Viaje>();
	
	private Sistema() {
		
	}

	public static Sistema getInstance() {
		if (Sistema.instance == null)
			Sistema.instance = new Sistema();
		return instance;
	}
	
	public void agregar(Cliente cliente) throws ClienteExistenteException{
		assert cliente != null : "cliente invalido";
		if (clientes.contains(cliente)) {
			throw new ClienteExistenteException("El cliente no puede agregarse porque ya existe.");
		} else {
			this.clientes.add(cliente);
		}
	}
	
	public void modificar(Cliente cliente, String nombre, String usuario, String password) throws ClienteExistenteException{  //que quiero modificar
		assert cliente != null : "cliente no valido";
		if (clientes.contains(cliente)) {
			Cliente c = clientes.get(clientes.indexOf(cliente));
			if(nombre != null)
				c.setNombreReal(nombre);
			if(usuario != null) {
				int i = 0;
				while(i < clientes.size() && clientes.get(i).getNombreUsuario() != usuario) {
					i++;
				}
				if(!(i < clientes.size())) // no encontro el cliente
					c.setNombreUsuario(usuario);
				else {
					throw new ClienteExistenteException("El nombre de usuario ya existe");
				}
			}
			if(password != null)
				c.setPassword(password);
		}
	}
	
	public Cliente consultarCliente(String usuario) {
		int i = 0;
		while(i < clientes.size() && clientes.get(i).getNombreUsuario() != usuario) {
			i++;
		}
		if(i < clientes.size()) // no encontro el cliente
			return clientes.get(i);
		else
			return null;
	}
	
	public Pedido realizarPedido(LocalDateTime fechaYHora, String zona, boolean mascota, int cantPasajeros, boolean equipajeBaul, Cliente cliente) {
		assert fechaYHora != null : "fecha invalida";
		assert zona != null : "zona no puede ser null";
		assert cantPasajeros > 0 : "cantidad de pasajeros > a 0";
		assert cliente != null : "cliente no valido";
		Pedido pedido = null;
		try {
			pedido = new Pedido(fechaYHora, zona, mascota, cantPasajeros, equipajeBaul, cliente);
			System.out.println("El pedido fue realizado con exito.");
		}
		catch (CantidadDePasajerosException e) {
			System.out.println(e.getMessage());
		}
		catch (ZonaInvalidaException e) {
			System.out.println(e.getMessage());
		}
		catch (PedidoIncoherenteException e) {
			System.out.println(e.getMessage());
		}
		return pedido;
	}
	
	public void agregar(Vehiculo vehiculo,String patente) {
		assert vehiculo != null : "vehiculo invalido";
		flota.put(patente,vehiculo);
	}
	
	public void modificar(Vehiculo vehiculo, String patente) {
		assert vehiculo != null : "vehiculo no valido";
		Vehiculo v = flota.get(patente);
		if(v != null) {
			v.setNroPatente(patente);
		}
	}
	
	public Vehiculo consultarVehiculo(String patente) {
		return flota.get(patente);
	}
	
	public void agregar(Chofer chofer) {
		if(!this.choferes.contains(chofer)) {
			this.choferes.add(chofer);
		}
	}
	
	/*public void modificarChofer(ChoferTemprario chofer, String nombre, String dni) {
		
	}*/
	
	public Chofer consultarChofer(String dni) {
		int i = 0;
		while(i < choferes.size() && choferes.get(i).getDni() != dni) {
			i++;
		}
		if(i < choferes.size()) // no encontro el cliente
			return choferes.get(i);
		else
			return null;
	}
	
	
	public void pagarViaje(Cliente cliente, Viaje viaje) {
		viaje.pagar();
	}
	
	public void calificarChofer(Chofer chofer, int puntaje) {
		int puntajeAct = chofer.getPuntaje();
		chofer.setPuntaje(puntajeAct + puntaje);
	}
	
	public void verHistorialDeViaje(Cliente cliente) {  //historial de un cliente especifico
		ArrayList<Viaje> viajesCliente = cliente.getViajes();
		for (int i = 0; i < viajesCliente.size(); i++) {
			System.out.println(viajesCliente.get(i));
		}
	}
	
	public ArrayList<Cliente> listadoClientes() {
		return clientes;
	}
	
	public ArrayList<Chofer> listadoChoferes() {
		return choferes;
	}
	
	public HashMap<String,Vehiculo> listadoVehiculos() {
		return flota;
	}
	
	public double salarioChofer(Chofer chofer) {  //devuelve el salario mensual de un chofer
		return chofer.getSueldo();
	}
	
	public double totalDineroNecesario() { //devuelve el total necesario para pagar los salarios
		double total = 0;
		for (int i=0; i < this.choferes.size(); i++) {
			total += salarioChofer(this.choferes.get(i));
		}
		return total;
	}
}
