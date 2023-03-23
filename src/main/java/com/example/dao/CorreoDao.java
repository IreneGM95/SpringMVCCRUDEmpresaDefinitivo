package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entities.Empleado;
import com.example.entities.Correo;

@Repository
public interface CorreoDao extends JpaRepository<Correo, Integer> {
    
    // @Query(value = "delete from telefonos where empleado_id = :idEmpleado", nativeQuery = true)
    // long deleteByEmpleado(@Param("idEmpleado") Integer idEmpleado);

    long deleteByEmpleado(Empleado empleado);
    


    /**Es necesario crear un método que encuentre los correos de cada empleado: */
    List<Correo> findByEmpleado(Empleado empleado);
}