package com.example.services;


import java.util.List;

import com.example.entities.Empleado;
import com.example.entities.Departamento;

public interface DepartamentoService {
    public List<Departamento> findAll();
    public Departamento findById(int iddepartamento);
    public void save(Departamento departamento);
    public void deleteById(int iddepartamento);
    
}