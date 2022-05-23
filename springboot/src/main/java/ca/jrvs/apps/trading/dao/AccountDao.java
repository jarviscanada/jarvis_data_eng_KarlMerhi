package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
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
public class AccountDao extends JdbcCrudDao<Account> {

    private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

    private final String TABLE_NAME = "account";
    private final String ID_COLUMN = "id";
    private final String TRADER_ID_COLUMN = "trader_id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public AccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID_COLUMN);
    }

    public Optional<Account> findByTraderId(Integer id) {
        Optional<Account> entity = Optional.empty();
        String selectSql = "Select " + getIdColumnName() + " FROM " + getTableName()
                + " WHERE " + TRADER_ID_COLUMN + " =?";

        try {
            entity = Optional.ofNullable(getJdbcTemplate()
                    .queryForObject(selectSql,
                            BeanPropertyRowMapper.
                                    newInstance(getEntityClass()), id));
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.debug("Can't find trader id:" + id, e);
        }
        return entity;
    }

    @Override
    public int updateOne(Account account) {
        String updateSQL = "UPDATE " + getTableName() + " SET amount=? WHERE id=?";
        return getJdbcTemplate().update(updateSQL, makeUpdateValues(account));
    }

    private Object[] makeUpdateValues(Account account){
        return new Object[]{account.getAmount(), account.getId()};
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public SimpleJdbcInsert getSimpleJdbcInsert() {
        return simpleJdbcInsert;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdColumnName() {
        return ID_COLUMN;
    }

    @Override
    Class<Account> getEntityClass() {
        return Account.class;
    }

    @Override
    public <S extends Account> Iterable<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(Account account) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends Account> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
