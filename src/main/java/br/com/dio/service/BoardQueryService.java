package br.com.dio.service;

import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BoardEntity;
import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


@AllArgsConstructor
public class BoardQueryService {

    private final Connection connection;

    public Optional<BoardEntity> findById(final  Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        var bordColumnDAO = new BoardColumnDAO(connection);
        var option = dao.findbyId(id);
        if (option.isPresent()){
            var entity = option.get();
            entity.setBoardColumn(bordColumnDAO.findByBoardId(entity.getId()));
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
