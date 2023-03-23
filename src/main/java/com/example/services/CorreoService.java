package com.example.services;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Empleado;
import com.example.entities.Correo;

public interface CorreoService {
    public List<Correo> findAll();

    public Correo findById(int idcorreo);

    public void save(Correo correo);

    public void deleteById(int idcorreo);

    public void deleteByEmpleado(Empleado empleado);

    public List<Correo> findByEmpleado(Empleado empleado);

}