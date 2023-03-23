package com.example.services;

import java.util.List;

import com.example.entities.Empleado;

public interface EmpleadoService {

    public List<Empleado> findAll();
    public Empleado findById(int idEmpleado);
    public void save(Empleado empleado);
    public void deleteById(int idEmpleado);
    public void delete (Empleado empleado);

    /**
     * No es necesario un metodo update, porque el save inserta o actualiza, en dependencia
     * de que el idEmpleado exista o no, es decir, si no existe lo crea, y si existe actualiza
     * la informacion.
    */

}