package dao;

import jpautills.JpaUtills;
import model.Artist;
import model.ArtistPersonalInfo;
import model.Producer;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.text.MessageFormat;
import java.util.List;

public class ArtistPersonalInfoDao {

    public void addPersonalInfo(ArtistPersonalInfo info) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(info);
            entityManager.getTransaction().commit();
            System.out.println(MessageFormat.format("В таблицу artist_personal_info " +
                    "добавлена информация = {0}", info));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public void updatePersonalInfo(ArtistPersonalInfo info) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("update artist_personal_info set first_name = :name, " +
                    "last_name = :lastName, age = :age where id = :id")
                    .setParameter("name", info.getFirstName())
                    .setParameter("lastName", info.getLastName())
                    .setParameter("age", info.getAge())
                    .setParameter("id", info.getId())
                    .executeUpdate();
            entityManager.getTransaction().commit();

            System.out.println(MessageFormat.format("В таблице artist_personal_info " +
                    "изменена информация с id = {0}, теперь info = {1}", info.getId(), info));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

    public List<ArtistPersonalInfo> findAll() {
        EntityManager entityManager = JpaUtills.getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArtistPersonalInfo> criteriaQuery = criteriaBuilder.createQuery(ArtistPersonalInfo.class);
        criteriaQuery.select(criteriaQuery.from(ArtistPersonalInfo.class));
        List<ArtistPersonalInfo> infoList = entityManager.createQuery(criteriaQuery).getResultList();
        entityManager.close();
        JpaUtills.close();
        System.out.println(MessageFormat.format("Из таблицы artists выбраны все записи = {0}", infoList));
        return infoList;
    }

    public void deletePersonalInfo(int id) {
        EntityManager entityManager = JpaUtills.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.find(ArtistPersonalInfo.class, id));
            entityManager.getTransaction().commit();
            System.out.println(MessageFormat.format("Из таблицы artist_personal_info " +
                    "удалена информация с id = {0}", id));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
            JpaUtills.close();
        }
    }

}
