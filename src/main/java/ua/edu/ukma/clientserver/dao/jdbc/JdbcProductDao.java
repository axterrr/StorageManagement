package ua.edu.ukma.clientserver.dao.jdbc;

import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.model.ProductSearchParams;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcProductDao implements ProductDao, AutoCloseable {

    private static final String TABLE_NAME = "products";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String MANUFACTURER = "manufacturer";
    private static final String AMOUNT = "amount";
    private static final String PRICE = "price";
    private static final String GROUP_ID = "group_id";

    private final Connection connection;

    public JdbcProductDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Integer create(Product product) {
        String sql = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
                TABLE_NAME, NAME, DESCRIPTION, MANUFACTURER, PRICE, GROUP_ID
        );

        try (PreparedStatement query = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            query.setString(1, product.getName());
            query.setString(2, product.getDescription());
            query.setString(3, product.getManufacturer());
            query.setDouble(4, product.getPrice());
            query.setInt(5, product.getGroupId());
            query.executeUpdate();

            ResultSet keys = query.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Product product) {
        String sql = String.format(
                "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                TABLE_NAME, NAME, DESCRIPTION, MANUFACTURER, PRICE, ID
        );

        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setString(1, product.getName());
            query.setString(2, product.getDescription());
            query.setString(3, product.getManufacturer());
            query.setDouble(4, product.getPrice());
            query.setInt(5, product.getId());
            query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", TABLE_NAME, ID);

        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setInt(1, id);
            query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = String.format("DELETE FROM %s", TABLE_NAME);

        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> getById(Integer id) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, ID);

        Product product = null;
        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setInt(1, id);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                product = extractProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> getAll() {
        return getByParams(new ProductSearchParams());
    }

    @Override
    public Optional<Product> getByName(String name) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, NAME);

        Product product = null;
        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setString(1, name);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                product = extractProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> getByParams(ProductSearchParams params) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_NAME);
        List<Object> arguments = new ArrayList<>();
        buildWhereClause(sql, arguments, params);

        List<Product> products = new ArrayList<>();
        try (PreparedStatement query = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < arguments.size(); i++) {
                query.setObject(i+1, arguments.get(i));
            }

            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                products.add(extractProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    @Override
    public void updateAmount(Integer id, Integer amount) {
        String sql = String.format(
                "UPDATE %s SET %s = ? WHERE %s = ?",
                TABLE_NAME, AMOUNT, ID
        );

        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setInt(1, amount);
            query.setInt(2, id);
            query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getByGroup(Integer groupId) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, GROUP_ID);

        List<Product> products = new ArrayList<>();
        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setInt(1, groupId);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                products.add(extractProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    private void buildWhereClause(StringBuilder sb, List<Object> arguments, ProductSearchParams params) {
        ArrayList<String> conditions = new ArrayList<>();

        if (params.getName() != null && !params.getName().isBlank()) {
            conditions.add(NAME + " ILIKE ?");
            arguments.add("%" + params.getName() + "%");
        }

        if (params.getDescription() != null && !params.getDescription().isBlank()) {
            conditions.add(DESCRIPTION + " ILIKE ?");
            arguments.add("%" + params.getDescription() + "%");
        }

        if (params.getManufacturer() != null && !params.getManufacturer().isBlank()) {
            conditions.add(MANUFACTURER + " ILIKE ?");
            arguments.add("%" + params.getManufacturer() + "%");
        }

        if (params.getPriceFrom() != null) {
            conditions.add(PRICE + " >= ?");
            arguments.add(params.getPriceFrom());
        }

        if (params.getPriceTo() != null) {
            conditions.add(PRICE + " <= ?");
            arguments.add(params.getPriceTo());
        }

        if (params.getAmountFrom() != null) {
            conditions.add(AMOUNT + " >= ?");
            arguments.add(params.getAmountFrom());
        }

        if (params.getAmountTo() != null) {
            conditions.add(AMOUNT + " <= ?");
            arguments.add(params.getAmountTo());
        }

        if (params.getGroupId() != null) {
            conditions.add(GROUP_ID + " = ?");
            arguments.add(params.getGroupId());
        }

        if (!conditions.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(String.join(" AND ", conditions));
        }
    }

    private static Product extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        return Product.builder()
                .id(resultSet.getInt(ID))
                .name(resultSet.getString(NAME))
                .description(resultSet.getString(DESCRIPTION))
                .manufacturer(resultSet.getString(MANUFACTURER))
                .price(resultSet.getDouble(PRICE))
                .amount(resultSet.getInt(AMOUNT))
                .groupId(resultSet.getInt(GROUP_ID))
                .build();
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
