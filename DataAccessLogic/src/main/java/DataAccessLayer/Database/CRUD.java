package DataAccessLayer.Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public interface CRUD<Key, Value> {

    public boolean create(Value v);

    public boolean createAll(List<Value> v_list);

    public List<Value> readAll();

    public Value read(Key k);

    public boolean update(Value v);

    public boolean delete(Key k);

    public Value resultSetToObject(ResultSet rs);

    public ArrayList<Value> search(String search);

}
