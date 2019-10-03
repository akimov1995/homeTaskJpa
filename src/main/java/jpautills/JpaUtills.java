package jpautills;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtills {
    private static EntityManagerFactory emFactory;

    public static EntityManager getEntityManager() {
        emFactory = Persistence.createEntityManagerFactory("homeTask");
        return emFactory.createEntityManager();
    }

    public static void close() {
        emFactory.close();
    }
}
