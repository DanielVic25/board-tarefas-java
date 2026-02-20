package br.com.dio.service;

import br.com.dio.persistence.config.ConnectionConfig;
import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BoardEntity;
import java.sql.SQLException;


public class BoardService {

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        // A conexão é aberta AQUI no Service
        try (var connection = ConnectionConfig.getConnection()) {
            var dao = new BoardDAO(connection);
            var boardColumnDAO = new BoardColumnDAO(connection);

            try {
                dao.insert(entity);
                var columns = entity.getBoardColumn().stream().map(c -> {
                    c.setBoard(entity);
                    return c;
                }).toList();

                for (var column : columns) {
                    boardColumnDAO.insert(column);
                }
                connection.commit();
                return entity;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public boolean delete(final Long id) throws SQLException {
        try (var connection = ConnectionConfig.getConnection()) {
            var dao = new BoardDAO(connection);
            try {
                if (!dao.exists(id)) {
                    return false;
                }
                dao.delete(id);
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }
}