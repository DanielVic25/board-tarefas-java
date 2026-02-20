package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@AllArgsConstructor
public class CardDAO {
    private final Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        var sql = "INSERT INTO CARDS (title, description, boards_column_id) VALUES (?, ?, ?);";
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setLong(3, entity.getBoardColumn().getId());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) entity.setId(keys.getLong(1));
        }
        return entity;
    }

    public void moveToColumn(Long columnId, Long cardId) throws SQLException {
        var sql = "UPDATE CARDS SET boards_column_id = ? WHERE id = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, columnId);
            statement.setLong(2, cardId);
            statement.executeUpdate();
        }
    }

    public void updateTitle(Long cardId, String newTitle) throws SQLException {
        var sql = "UPDATE CARDS SET title = ? WHERE id = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, newTitle);
            statement.setLong(2, cardId);
            statement.executeUpdate();
        }
    }

}