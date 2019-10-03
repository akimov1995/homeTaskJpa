import dao.AlbumDao;
import dao.ArtistDao;
import dao.ArtistPersonalInfoDao;
import dao.ProducerDao;
import model.Album;
import model.Artist;
import model.ArtistPersonalInfo;
import model.Producer;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        createData();
        ArtistDao artistDao = new ArtistDao();
        AlbumDao albumDao = new AlbumDao();
        List<Artist> resultList = artistDao.findAll();

        Album album = resultList.get(0).getAlbums().get(1);
        album.setName("MMLP2");
        albumDao.updateAlbum(album);

        albumDao.findAll();
        artistDao.findAll();

        artistDao.selectQuery();
        artistDao.selectCriteriaQuery();
        artistDao.selectNativeQuery();
        artistDao.namedQuery();

        ProducerDao producerDao = new ProducerDao();
        Producer p = producerDao.findAll().get(0);
        p.setLastName("New Test Name");
        producerDao.updateProducer(p);
        producerDao.findAll();

        Artist artist = artistDao.findAll().get(0);
        ArtistPersonalInfo info = artist.getPersonalInfo();
        info.setLastName("New Last Name");
        ArtistPersonalInfoDao personalInfoDao = new ArtistPersonalInfoDao();
        personalInfoDao.updatePersonalInfo(info);
        artistDao.findAll();

        ArtistPersonalInfo personalInfo = new ArtistPersonalInfo();
        personalInfo.setFirstName("Aron");
        personalInfo.setLastName("Smith");
        personalInfo.setAge(48);

        personalInfoDao.findAll();
        personalInfoDao.addPersonalInfo(personalInfo);
        personalInfoDao.findAll();

        producerDao.selectProducerQuery();
        producerDao.namedQuery();
        producerDao.selectProducerNativeQuery();
        producerDao.selectProducerCriteriaQuery();


        Album albumToUpdate = artistDao.findAll().get(0).getAlbums().get(0);
        Artist kendrick = artistDao.findAll().get(1);
        albumToUpdate.setArtist(kendrick);
        albumDao.updateAlbum(albumToUpdate);
        artistDao.findAll();

        ArtistPersonalInfo newPersonalInfo = new ArtistPersonalInfo();
        newPersonalInfo.setFirstName("Mike");
        newPersonalInfo.setLastName("Jones");
        newPersonalInfo.setAge(39);

        personalInfoDao.addPersonalInfo(newPersonalInfo);

        Artist artistToUpdate = artistDao.findAll().get(0);
        System.out.println(artistToUpdate);

        artistToUpdate.setPersonalInfo(newPersonalInfo);
        artistDao.update(artistToUpdate);
        artistDao.findAll();
    }


    private static void createData() {
        Artist artist = new Artist();
        artist.setName("Eminem");
        artist.setLabelName("Shady Records");

        ArtistPersonalInfo personalInfo = new ArtistPersonalInfo();
        personalInfo.setFirstName("Marshall");
        personalInfo.setLastName("Mathers");
        personalInfo.setAge(47);

        artist.setPersonalInfo(personalInfo);

        Producer producer1 = new Producer();
        producer1.setName("Andre");
        producer1.setLastName("Young");

        Producer producer2 = new Producer();
        producer2.setName("Max");
        producer2.setLastName("Porter");

        List<Producer> producers = new ArrayList<>();
        producers.add(producer1);

        Album album1 = new Album();
        album1.setGenre("rap");
        album1.setName("Kamikaze");
        album1.setProducerList(producers);
        album1.setArtist(artist);

        Album album2 = new Album();
        album2.setGenre("rap");
        album2.setName("mmlp");
        album2.setArtist(artist);

        List<Album> albums = new ArrayList<>();
        albums.add(album1);
        albums.add(album2);

        producer1.setAlbums(albums);
        artist.setAlbums(albums);

        Artist artist2 = new Artist();
        artist2.setName("kendrick lamar");
        artist2.setLabelName("interscope");

        Album a1 = new Album();
        a1.setName("album1");
        a1.setArtist(artist2);
        a1.setGenre("rap");

        Album a2 = new Album();
        a2.setName("mad city");
        a2.setArtist(artist2);
        a2.setGenre("hip-hop");

        ArtistDao artistDao = new ArtistDao();
        artistDao.addArtist(artist);

        albums.clear();
        albums.add(a1);
        albums.add(a2);
        artist2.setAlbums(albums);
        artistDao.addArtist(artist2);
    }
}
