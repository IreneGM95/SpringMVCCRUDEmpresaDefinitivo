package com.example.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Empleado;
import com.example.entities.Telefono;

public interface TelefonoService {
    public List<Telefono> findAll();

    public Telefono findById(int idTelefono);

    public void save(Telefono telefono);

    public void deleteById(int idTelefono);

    public void deleteByEmpleado(Empleado empleado);

    public List<Telefono> findByEmpleado(Empleado empleado);

}
