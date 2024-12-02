package ExamenFinal.ExamenFinal.repository;

import ExamenFinal.ExamenFinal.entity.Cliente;
import ExamenFinal.ExamenFinal.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByDocumentoAndTipoDocumento(String documento, TipoDocumento tipoDocumento);
}
