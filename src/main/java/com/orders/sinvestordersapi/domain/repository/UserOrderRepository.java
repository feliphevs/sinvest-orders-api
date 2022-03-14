package com.orders.sinvestordersapi.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.LockModeType;

import com.orders.sinvestordersapi.domain.model.UserOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> findAllByOrderByIdAsc();

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    @Query("FROM UserOrder WHERE id_user != :idUser AND id_stock = :idStock AND price <= :price AND type = 1 AND status = 0 ORDER BY price ASC, created_on ASC")
    List<UserOrder> matchVenda(Long idUser, Long idStock, BigDecimal price);

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    @Query("FROM UserOrder WHERE id_user != :idUser AND id_stock = :idStock AND price >= :price AND type = 0 AND status = 0 ORDER BY price ASC, created_on ASC")
    List<UserOrder> matchCompra(Long idUser, Long idStock, BigDecimal price);

    @Query("select MIN(price) from UserOrder where id_stock = :idStock and type = 1")
    BigDecimal askMin(Long idStock);

    @Query("select MAX(price) from UserOrder where id_stock = :idStock and type = 1")
    BigDecimal askMax(Long idStock);

    @Query("select MIN(price) from UserOrder where id_stock = :idStock and type = 0")
    BigDecimal bidMin(Long idStock);

    @Query("select MAX(price) from UserOrder where id_stock = :idStock and type = 0")
    BigDecimal bidMax(Long idStock);

    @Query("from UserOrder where id_user = :idUser")
    List<UserOrder> listarPorIdUser(Long idUser);

    @Query("from UserOrder o inner join User u on o.idUser = u.id AND u.username = :email AND o.type = 0 AND o.status = 0")
    List<UserOrder> comprasAbertasByUser(String email);

    @Query("from UserOrder o inner join User u on o.idUser = u.id AND u.username = :email AND o.type = 0 AND o.status = 1")
    List<UserOrder> comprasFechadasByUser(String email);

    @Query("from UserOrder o inner join User u on o.idUser = u.id AND u.username = :email AND o.type = 1 AND o.status = 0")
    List<UserOrder> vendasAbertasByUser(String email);

    @Query("from UserOrder o inner join User u on o.idUser = u.id AND u.username = :email AND o.type = 1 AND o.status = 1")
    List<UserOrder> vendasFechadasByUser(String email);

}
