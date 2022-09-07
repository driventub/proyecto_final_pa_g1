package ec.edu.uce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import ec.edu.uce.modelo.Cliente;
import ec.edu.uce.modelo.Vehiculo;
import ec.edu.uce.service.IClienteService;
import ec.edu.uce.service.IGestorClienteService;
import ec.edu.uce.service.IGestorEmpleadoService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    

    @Autowired
    private IGestorClienteService clienteeGestorServic;
      
    @Autowired
    private IClienteService clienteService;

    // c.a
    @GetMapping("/vehiculoBuscar")
	public String obtenerVehiculo(Vehiculo vehiculo) {
		return "v_buscar_marca";
	}

    @GetMapping("/buscar/marcaModelo")
	public String buscarVehiculo(Vehiculo vehiculo, BindingResult result, Model model,
			RedirectAttributes redirectAttrs) {

		List<Vehiculo> listaVehiculo = this.clienteeGestorServic.buscarVehiculosDisponibles( vehiculo.getMarca(),vehiculo.getModelo());
        model.addAttribute("listaVehiculo", listaVehiculo);
		return "v_m_marca";
	}


    // c.c

    @GetMapping("/registro")	
	public String registroCliente(Cliente cliente) {
		return "c_registro";

	}

    @GetMapping("/insertar")
	public String insertarClienteC(Cliente cliente, BindingResult result, Model modelo,
			RedirectAttributes redirectAttrs) {
    		this.clienteeGestorServic.registrarCliente(cliente);
		//return "c_registro_valido";
		return "redirect:/clientes/registro?exito";
	}
    
    
    

    @GetMapping("/editar/{clieCedula}")
    public String editarCliente(@PathVariable("clieCedula") String cedula, Cliente cliente,Model modelo) {
    	Cliente clie = this.clienteService.buscarClientePorCedula(cedula);
    	System.out.println(clie.toString());
    	modelo.addAttribute("clie", clie);
    	return "clieActualiza";
    }
    
    @PutMapping("/actualizar/{clieCedula}")
    public String actualizarCliente(@PathVariable("clieCedula")String cedula,Cliente clie,RedirectAttributes redirectAttrs) {
    	Cliente cliente = this.clienteService.buscarClientePorCedula(clie.getCedula());
    	clie.setId(cliente.getId());
    	clie.setTipoRegistro(cliente.getTipoRegistro());
    	clie.setReservaActiva(cliente.getReservaActiva());
    	this.clienteService.actualizarCliente(clie);
    	redirectAttrs
        .addFlashAttribute("mensaje", "Cliente cedula: "+clie.getCedula()+" editado correctamente")
        .addFlashAttribute("clase", "success");
    	return "redirect:/clientes/todos";
    }
    
    @GetMapping("/todos")
   public String listarClientes(Model modelo) {
	   List<Cliente> clientes = this.clienteService.listarClientes();
	   modelo.addAttribute("clientes", clientes);
    	return "todosClientes";
   }
}
