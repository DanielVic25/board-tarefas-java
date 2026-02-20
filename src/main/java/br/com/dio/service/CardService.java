package br.com.dio.service;

import br.com.dio.persistence.dao.BlockDAO;
import br.com.dio.persistence.dao.CardDAO;
import br.com.dio.persistence.entity.BlockEntity;
import br.com.dio.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;
import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class CardService {
    private final Connection connection;

    public CardEntity create(CardEntity entity) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            dao.insert(entity);
            connection.commit();
            return entity;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void moveToColumn(Long columnId, Long cardId) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            dao.moveToColumn(columnId, cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void block(BlockEntity entity) throws SQLException {
        try {
            var dao = new BlockDAO(connection);
            dao.block(entity);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void unblock(Long cardId, String reason) throws SQLException {
        try {
            var dao = new BlockDAO(connection);
            dao.unblock(reason, cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
}