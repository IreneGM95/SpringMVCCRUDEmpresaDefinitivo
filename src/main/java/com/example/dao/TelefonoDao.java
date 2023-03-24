package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Empleado;
import com.example.entities.Telefono;

@Repository
public interface TelefonoDao extends JpaRepository<Telefono, Integer> {
    
    // @Query(value = "delete from telefonos where empleado_id = :idEmpleado", nativeQuery = true)
    // long deleteByEmpleado(@Param("idEmpleado") Integer idEmpleado);

    long deleteByEmpleado(Empleado empleado);
    


    /**Es necesario crear un método que encuentre los telefonso de cada empleado: */
    List<Telefono> findByEmpleado(Empleado empleado);
}