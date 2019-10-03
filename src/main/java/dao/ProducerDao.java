package dao;

import jpautills.JpaUtills;
import model.Album;
import model.Producer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

public class ProducerDao {

    public void addProducer(Producer producer) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(producer);
            entityManager.getTransaction().commit();
            System.out.println(MessageFormat.format("В таблицу producers добавлен продюсер = {0}", producer));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public void deleteProducer(int id) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.find(Producer.class, id));
            entityManager.getTransaction().commit();
            System.out.println(MessageFormat.format("Из таблицы artists удалён продюсер с id = {0}", id));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public List<Producer> findAll() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        String queryString = "select p from Producer p order by p.name";
        Query query = entityManager.createQuery(queryString, Producer.class);
        List<Producer> resultList = query.getResultList();
        entityManager.close();
        JpaUtills.close();
        System.out.println(MessageFormat.format("Из таблицы producers выбраны все записи = {0}", resultList));
        return resultList;
    }

    public void updateProducer(Producer producer) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNamedQuery("updateProducer")
                    .setParameter("name", producer.getName())
                    .setParameter("lastName", producer.getLastName())
                    .setParameter("id", producer.getId())
                    .executeUpdate();
            entityManager.getTransaction().commit();

            System.out.println(MessageFormat.format("В таблице producers изменён исполнитель с id = {0}," +
                    " теперь producer = {1}", producer.getId(), producer));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public void selectProducerQuery() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        String query = "SELECT p.name, COUNT(alb) FROM Producer p JOIN p.albums alb " +
                "GROUP BY p having count(alb) > 0 order by p.name";

        List result = entityManager.createQuery(query).getResultList();
        System.out.println("Выполнен jpql запрос к таблицам");

        for (Iterator i = result.iterator(); i.hasNext(); ) {
            Object[] values = (Object[]) i.next();
            System.out.println(MessageFormat.format("Результат producer name = {0}, " +
                    "album count = {1}", values[0], values[1]));
        }
        entityManager.close();
        JpaUtills.close();
    }

    public void selectProducerCriteriaQuery() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = builder.createTupleQuery();
        Root<Producer> producer = criteriaQuery.from(Producer.class);
        Join<Producer, Album> album = producer.join("albums");
        criteriaQuery.multiselect(producer.get("name"), builder.count(album))
                .groupBy(producer)
                .having(builder.ge(builder.count(album), 0))
                .orderBy(builder.asc(producer.get("name")));

        List<Tuple> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        System.out.println("Выполнен criteria запрос к таблицам");

        resultList.forEach(tuple -> {
            System.out.println(MessageFormat.format("Результат producer name = {0}, " +
                    "album count = {1}", tuple.get(0, String.class), tuple.get(1, Long.class)));
        });

        entityManager.close();
        JpaUtills.close();
    }


    public void selectProducerNativeQuery() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        String query = "select p.name, count(alb.album_id) from producers p inner join producers_albums t " +
                "on p.producer_id = t.producer_producer_id inner join albums alb on t.albums_album_id = alb.album_id " +
                "group by p.producer_id having count(alb.album_id)>0 order by p.name";

        List result = entityManager.createNativeQuery(query).getResultList();
        System.out.println("Выполнен native запрос к таблицам");

        for (Iterator i = result.iterator(); i.hasNext(); ) {
            Object[] values = (Object[]) i.next();
            System.out.println(MessageFormat.format("Результат producer name = {0}, " +
                    "album count = {1}", values[0], values[1]));
        }
        entityManager.close();
        JpaUtills.close();
    }

    public void namedQuery() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        List result = entityManager.createNamedQuery("selectNamedQuery2").getResultList();
        System.out.println("Выполнен named запрос к таблицам");

        for (Iterator i = result.iterator(); i.hasNext(); ) {
            Object[] values = (Object[]) i.next();
            System.out.println(MessageFormat.format("Результат producer name = {0}, " +
                    "album count = {1}", values[0], values[1]));
        }
        entityManager.close();
        JpaUtills.close();
    }
}
