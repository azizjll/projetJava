package esprit.monstergym.demo.Service;

import java.sql.SQLException;
import java.util.Set;

public interface IProject<T> {

    public Set<T> afficher() throws SQLException;

    public void ajouter(T t) ;

    public  void supprimer(int id )throws SQLException;

    public  void modifier(T t ) throws SQLException;

}