package dao;

import jpautills.JpaUtills;
import model.Album;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.List;

public class AlbumDao {

    public void addAlbum(Album album) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(album);
            entityManager.getTransaction().commit();
            System.out.println(MessageFormat.format("В таблицу albums добавлен альбом = {0}", album));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public void deleteAlbum(int id) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.find(Album.class, id));
            entityManager.getTransaction().commit();
            System.out.println(MessageFormat.format("Из таблицы albums удалён альбом с id = {0}", id));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public List<Album> findAll() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        String queryString = "select a from Album a order by a.id";
        Query query = entityManager.createQuery(queryString, Album.class);
        List<Album> resultList = query.getResultList();
        entityManager.close();
        JpaUtills.close();
        System.out.println(MessageFormat.format("Из таблицы albums выбраны все записи = {0}", resultList));
        return resultList;
    }

    public void updateAlbum(Album album) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("update albums set name = :name, " +
                    "genre = :genre, artist_id = :artist_id where album_id = :id")
                    .setParameter("name", album.getName())
                    .setParameter("genre", album.getGenre())
                    .setParameter("id", album.getId())
                    .setParameter("artist_id", album.getArtist().getId())
                    .executeUpdate();
            entityManager.getTransaction().commit();

            System.out.println(MessageFormat.format("В таблице albums изменён альбом с id = {0}," +
                    " теперь album = {1}", album.getId(), album));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }
}
