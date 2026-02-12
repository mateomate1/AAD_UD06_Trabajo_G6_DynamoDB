package service;

/**
 * Servicio de negocio para gestion de aulas.
 * 
 * Proporciona logica de dominio: creacion con ID unico automatico
 * y actualizacion validada. Usa AulaDAO interno.
 * 
 * @author Mario Garcia
 * @author Mateo Ayarra
 * @author Samuel Cobreros
 * @author Zacaria Daghri
 * @version 1.0
 * @since 1.0
 */
import java.util.UUID;

import domain.model.Aula;
import persistence.dao.AulaDAO;
import persistence.dynamodb.AWSClient;

public class AulaService {
    private final AWSClient cliente;

    /**
     * Constructor con cliente AWS inyectado.
     * 
     * @param cliente AWSClient configurado (local/cloud)
     */
    public AulaService(AWSClient cliente) {
        this.cliente = cliente;
    }

    /**
     * Crea aula con ID UUID unico automatico.
     * 
     * Genera IDs hasta encontrar uno disponible (evita colisiones).
     * 
     * @param a aula con nombre, capacidad, edificio (ID ignorado)
     * @return aula persistida con ID asignado
     */
    public Aula crearAula(Aula a) {
        AulaDAO dao = new AulaDAO(this.cliente.getDynamoDB());

        boolean esUnica = false;

        // Genera UUID hasta encontrar ID disponible
        while (!esUnica) {
            String id = UUID.randomUUID().toString();
            if (dao.findById(id) == null) {
                esUnica = true;
                a.setId(id);
            }
        }

        dao.save(a);
        return a;
    }

    /**
     * Actualiza aula existente (valida existencia previa).
     * 
     * @param a aula completa con ID valido
     * @return aula actualizada o null si no existia
     */
    public Aula actualizarAula(Aula a) {
        AulaDAO dao = new AulaDAO(this.cliente.getDynamoDB());
        if (dao.findById(a.getId()) == null) {
            return null;
        }
        dao.save(a);
        return a;
    }
}
