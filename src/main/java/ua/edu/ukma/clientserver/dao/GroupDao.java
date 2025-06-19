package ua.edu.ukma.clientserver.dao;

import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.model.GroupSearchParams;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GroupDao implements AutoCloseable {

    private static final String TABLE_NAME = "product_groups";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    private final Connection connection;

    public GroupDao(Connection connection) {
        this.connection = connection;
    }

    public int create(Group group) {
        String sql = String.format(
            "INSERT INTO %s (%s, %s) VALUES (?, ?)",
            TABLE_NAME, NAME, DESCRIPTION
        );

        try (PreparedStatement query = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            query.setString(1, group.getName());
            query.setString(2, group.getDescription());
            query.executeUpdate();

            ResultSet keys = query.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Group group) {
        String sql = String.format(
            "UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
            TABLE_NAME, NAME, DESCRIPTION, ID
        );

        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setString(1, group.getName());
            query.setString(2, group.getDescription());
            query.setInt(3, group.getId());
            query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", TABLE_NAME, ID);

        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setInt(1, id);
            query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        String sql = String.format("DELETE FROM %s", TABLE_NAME);

        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Group getById(int id) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, ID);

        Group group = null;
        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setInt(1, id);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                group = extractGroupFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return group;
    }

    public List<Group> getAll() {
        return getByParams(new GroupSearchParams());
    }

    public List<Group> getByParams(GroupSearchParams params) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_NAME);
        List<Object> arguments = new ArrayList<>();
        buildWhereClause(sql, arguments, params);

        List<Group> groups = new ArrayList<>();
        try (PreparedStatement query = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < arguments.size(); i++) {
                query.setObject(i+1, arguments.get(i));
            }

            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                groups.add(extractGroupFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return groups;
    }

    private void buildWhereClause(StringBuilder sb, List<Object> arguments, GroupSearchParams params) {
        ArrayList<String> conditions = new ArrayList<>();

        if (params.getName() != null && !params.getName().isBlank()) {
            conditions.add(NAME + " ILIKE ?");
            arguments.add("%" + params.getName() + "%");
        }

        if (params.getDescription() != null && !params.getDescription().isBlank()) {
            conditions.add(DESCRIPTION + " ILIKE ?");
            arguments.add("%" + params.getDescription() + "%");
        }

        if (!conditions.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(String.join(" AND ", conditions));
        }
    }

    private static Group extractGroupFromResultSet(ResultSet resultSet) throws SQLException {
        return Group.builder()
            .id(resultSet.getInt(ID))
            .name(resultSet.getString(NAME))
            .description(resultSet.getString(DESCRIPTION))
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
