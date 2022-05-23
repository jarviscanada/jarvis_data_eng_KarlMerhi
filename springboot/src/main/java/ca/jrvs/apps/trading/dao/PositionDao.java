package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class PositionDao extends JdbcCrudDao<Position>{

    private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);

    private final String TABLE_NAME = "position";
    private final String TICKER_COLUMN = "ticker";
    private final String ID_COLUMN = "account_id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public PositionDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID_COLUMN);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    public SimpleJdbcInsert getSimpleJdbcInsert() {
        return this.simpleJdbcInsert;
    }

    @Override
    public String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public String getIdColumnName() {
        return this.ID_COLUMN;
    }

    @Override
    Class<Position> getEntityClass() {
        return Position.class;
    }

    public Optional<Position> findById(Integer accountId) {
        Optional<Position> position = Optional.empty();
        String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";

        try {
            position = Optional.ofNullable(getJdbcTemplate().queryForObject(selectSql,
                    BeanPropertyRowMapper.newInstance(getEntityClass()), accountId));
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.debug("Can't find trader id:" + accountId, e);
        }
        return position;
    }

    @Override
    public int updateOne(Position entity) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <S extends Position> Iterable<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(Position position) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends Position> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
