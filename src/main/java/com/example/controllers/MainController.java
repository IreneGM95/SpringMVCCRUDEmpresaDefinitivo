package com.example.controllers;


import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.entities.Empleado;
import com.example.entities.Correo;
import com.example.entities.Departamento;
import com.example.entities.Telefono;
import com.example.services.EmpleadoService;
import com.example.services.CorreoService;
import com.example.services.DepartamentoService;
import com.example.services.TelefonoService;

@Controller
@RequestMapping("/")
public class MainController {
    /**
     * el controlador delega la peticion en un metodo que tiene en cuenta el
     * verbo(get, put, delate, post...) del protocolo http utilizado para realizar
     * la peticion
     */

    /**
     * Logger registra todo lo que pasa en esta clase, MainController, para saber
     * todo lo que pasa y poder "hacer un analisis postmortem" si algo va mal
     */
    private static final Logger LOG = Logger.getLogger("MainController");

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private TelefonoService telefonoService;

    @Autowired
    private CorreoService correoService;

    /** Este metodo devuelve un listado de empleados: */
    @GetMapping("/listarEmp")
    public ModelAndView listar() {

        List<Empleado> empleados = empleadoService.findAll();

        ModelAndView mav = new ModelAndView("views/listarEmpleado");
        mav.addObject("empleados", empleados);

        return mav;
    }

    /** Metodo: */
    @GetMapping("/frmAltaEmp") // aqui es el nombre de la url que va a resoponder y le damos el nombre que
                                    // quieras no tiene porq ser igual que el nombre de abajo
    public String formularioAltaEmpleado(Model model) {

        List<Departamento> departamentos = departamentoService.findAll();
        Empleado empleado = new Empleado();

        model.addAttribute("empleado", empleado);
        model.addAttribute("departamentos", departamentos);

        return "views/formularioAltaEmpleado";

    }

    /**
     * Metodo que recibe los datos procedentes de los controladores del formulario y
     * se muestre el último creado
     */
    @PostMapping("/altaModificacionEmpleado")

    public String altaEmpleado(@ModelAttribute Empleado empleado,
            @RequestParam(name = "numerosTelefonos") String telefonosRecibidos,
            @RequestParam(name = "emailsCorreos") String correosRecibidos) {

        // gracias al log nos da un mensaje de comprobación antes de procesar la
        // información. Es una buena práctica de programación hacer esta comprobación
        // previa
        LOG.info("Telefonos recibidos: " + telefonosRecibidos);
        // Primero se guarda el empleado para despues poder acceder a él a la hora de
        // meterle los telefonos
        empleadoService.save(empleado);

        List<String> listadoNumerosTelefono = null; // la declaramos fuera,para poder utilizarla en varios sitios. Y le
                                                    // asignamos null, porque dentro de un método siempre hay que
                                                    // inicializarla (asignarle valor) para que funcione

        // No queremos guardar telefonos si no los hay, por eso ponemos el if
        if (telefonosRecibidos != null) {
            String[] arrayTelefonos = telefonosRecibidos.split(";"); // separa el array cada vez que encuentra un ;,
                                                                     // podría pedirle que separase cada vez que
                                                                     // encuentre un espacio
            // Convertimos este array en una colección para luego pasarlo a flujo y trabajar
            // con ese flujo:
            listadoNumerosTelefono = Arrays.asList(arrayTelefonos);
        }

        // si sí hay telefonos, el flujo lo recorremos e introducimos
        if (listadoNumerosTelefono != null) {
            telefonoService.deleteByEmpleado(empleado);
            listadoNumerosTelefono.stream().forEach(n -> {
                Telefono telefonoObject = Telefono.builder()
                        .numero(n)
                        .empleado(empleado)
                        .build();

                telefonoService.save(telefonoObject);
            });
            LOG.info("correos recibidos: " + correosRecibidos);

            empleadoService.save(empleado);

            List<String> listadoMailsCorreo = null;
            if (correosRecibidos != null) {
                String[] arrayCorreos = correosRecibidos.split(";");

                listadoMailsCorreo = Arrays.asList(arrayCorreos);
            }

            if (listadoMailsCorreo != null) {
                correoService.deleteByEmpleado(empleado);
                listadoMailsCorreo.stream().forEach(n -> {
                    Correo correoObject = Correo.builder()
                            .email(n)
                            .empleado(empleado)
                            .build();

                    correoService.save(correoObject);
                });

            }

        }

        return "redirect:/listarEmp";
    }

    /** Método para actualizar un empleado dado su id */
    @GetMapping("/frmActualizar/{id}")
    // como se hace a través de un link, es un get y es visible para todos, solo es
    // post si se lo especificamos nosotros a la hora de hacer un formulario
    // recogerlo es por get y mostrarlo por post?
    public String fmrActualizarEmpleado(@PathVariable(name = "id") int idEmpleado, Model model) {

        Empleado empleado = empleadoService.findById(idEmpleado);

        List<Telefono> todosTelefonos = telefonoService.findAll();

        List<Telefono> telefonosEmpleado = todosTelefonos
                .stream()
                .filter(telefono -> telefono.getEmpleado().getId() == idEmpleado)
                .collect(Collectors.toList());
        // sería más eficiente usar una consulta de mysql, pero en este caso no lo vamos
        // a hacer pporque vamos mal de tiempo y profundizaremos en mysql mas adelante

        String numerosDeTelefono = telefonosEmpleado.stream().map(t -> t.getNumero())
                .collect(Collectors.joining(";"));

        model.addAttribute("empleado", empleado);
        model.addAttribute("telefonos", numerosDeTelefono);

        // Igual para correos:

        List<Correo> todosCorreos = correoService.findAll();

        List<Correo> correosEmpleado = todosCorreos
                .stream()
                .filter(correo -> correo.getEmpleado().getId() == idEmpleado)
                .collect(Collectors.toList());

        String emailsDeCorreo = correosEmpleado.stream().map(t -> t.getEmail())
                .collect(Collectors.joining(";"));

        model.addAttribute("correos", emailsDeCorreo);

        // Para que en el formulario nos deje modificar/visualizar la departamento de un
        // empleado ya creado:
        List<Departamento> departamentos = departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);

        return "views/formularioAltaEmpleado";
    }


    @GetMapping("/borrar/{id}")
    public String borrarEmpleado(@PathVariable(name = "id") int idEmpleado) {
        empleadoService.delete(empleadoService.findById(idEmpleado));
        return "redirect:/listarEmp";
    }

    /**
     * Métodoque encuentre los telefonso de cada empleado: (hecho por nosotras):
     */
    @GetMapping("/detalles/{id}")
    public String detallesEmpleado(@PathVariable(name = "id") int id, Model model) {

        Empleado empleado = empleadoService.findById(id);
        List<Telefono> telefonos = telefonoService.findByEmpleado(empleado);
        List<String> numerosTelefono = telefonos.stream().map(t -> t.getNumero()).toList();

        List<Correo> correos = correoService.findByEmpleado(empleado);
        List<String> emailsCorreo = correos.stream().map(t -> t.getEmail()).toList();

        model.addAttribute("telefonos", numerosTelefono);
        model.addAttribute("correos", emailsCorreo);
        model.addAttribute("empleado", empleado);
        return "views/detalleEmpleado";
    }
}
