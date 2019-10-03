package dao;

import jpautills.JpaUtills;
import model.Album;
import model.Artist;
import model.Producer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

public class ArtistDao {

    public void addArtist(Artist artist) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(artist);
            entityManager.getTransaction().commit();
            System.out.println(MessageFormat.format("В таблицу artists добавлен исполнитель = {0}", artist));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public void deleteArtist(int id) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.find(Artist.class, id));
            entityManager.getTransaction().commit();
            System.out.println(MessageFormat.format("Из таблицы artists удалён исполнитель с id = {0}", id));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public void update(Artist artist) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNamedQuery("updateArtist")
                    .setParameter("name", artist.getName())
                    .setParameter("labelName", artist.getLabelName())
                    .setParameter("id", artist.getId())
                    .setParameter("personalInfo", artist.getPersonalInfo())
                    .executeUpdate();
            entityManager.getTransaction().commit();

            System.out.println(MessageFormat.format("В таблице artists изменён исполнитель с id = {0}," +
                    " теперь artist = {1}", artist.getId(), artist));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public List<Artist> findAll() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Artist> criteriaQuery = criteriaBuilder.createQuery(Artist.class);
        criteriaQuery.select(criteriaQuery.from(Artist.class));
        List<Artist> artists = entityManager.createQuery(criteriaQuery).getResultList();
        entityManager.close();
        JpaUtills.close();
        System.out.println(MessageFormat.format("Из таблицы artists выбраны все записи = {0}", artists));
        return artists;
    }


    public void selectQuery() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        String query = "SELECT a.name, COUNT(alb) FROM Artist a LEFT JOIN a.albums alb GROUP BY a " +
                "having count(alb) > 0 order by a.name";

        List result = entityManager.createQuery(query).getResultList();
        System.out.println("Выполнен jpql запрос к таблицам");

        for (Iterator i = result.iterator(); i.hasNext(); ) {
            Object[] values = (Object[]) i.next();
            System.out.println(MessageFormat.format("Результат artist name = {0}, " +
                    "album count = {1}", values[0], values[1]));
        }
        entityManager.close();
        JpaUtills.close();
    }

    public void namedQuery() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        List result = entityManager.createNamedQuery("selectNamedQuery").getResultList();
        System.out.println("Выполнен named запрос к таблицам");

        for (Iterator i = result.iterator(); i.hasNext(); ) {
            Object[] values = (Object[]) i.next();
            System.out.println(MessageFormat.format("Результат artist name = {0}, " +
                    "album count = {1}", values[0], values[1]));
        }
        entityManager.close();
        JpaUtills.close();
    }

    public void selectCriteriaQuery() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = builder.createTupleQuery();
        Root<Album> album = criteriaQuery.from(Album.class);
        Join<Album, Artist> artist = album.join("artist", JoinType.LEFT);
        criteriaQuery.multiselect(artist.get("name"), builder.count(album))
                .groupBy(artist)
                .having(builder.ge(builder.count(album), 1))
                .orderBy(builder.asc(artist.get("name")));

        List<Tuple> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        System.out.println("Выполнен criteria запрос к таблицам");

        resultList.forEach(tuple -> {
            System.out.println(MessageFormat.format("Результат artist name = {0}, " +
                    "album count = {1}", tuple.get(0, String.class), tuple.get(1, Long.class)));
        });

        entityManager.close();
        JpaUtills.close();
    }

    public void selectNativeQuery() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        String query = "SELECT a.name, COUNT(alb.artist_id) FROM artists  AS a LEFT JOIN albums" +
                " AS alb on a.artist_id = alb.artist_id" +
                " GROUP BY a.artist_id having count(alb.artist_id) > 0 order by a.name";

        List result = entityManager.createNativeQuery(query).getResultList();
        System.out.println("Выполнен native запрос к таблицам");

        for (Iterator i = result.iterator(); i.hasNext(); ) {
            Object[] values = (Object[]) i.next();

            System.out.println(MessageFormat.format("Результат artist name = {0}, " +
                    "album count = {1}", values[0], values[1]));
        }
        entityManager.close();
        JpaUtills.close();
    }
}
