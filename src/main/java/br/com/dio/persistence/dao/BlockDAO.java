package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BlockEntity;
import lombok.AllArgsConstructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@AllArgsConstructor
public class BlockDAO {
    private final Connection connection;

    public BlockEntity block(final BlockEntity entity) throws SQLException {
        var sql = "INSERT INTO BLOCKS (blocked_at, block_reason, card_id) VALUES (CURRENT_TIMESTAMP, ?, ?);";
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getBlockReason());
            statement.setLong(2, entity.getCard().getId());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) entity.setId(keys.getLong(1));
        }
        return entity;
    }

    public void unblock(final String reason, final Long cardId) throws SQLException {
        var sql = "UPDATE BLOCKS SET unblocked_at = CURRENT_TIMESTAMP, unblock_reason = ? WHERE card_id = ? AND unblocked_at IS NULL";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, reason);
            statement.setLong(2, cardId);
            statement.executeUpdate();
        }
    }
}