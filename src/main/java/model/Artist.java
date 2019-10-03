package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
@NamedQuery(name = "updateArtist", query = "update Artist set name = :name," +
        " labelName = :labelName, personalInfo = :personalInfo Where id = :id")
@NamedQuery(name = "selectNamedQuery", query = "SELECT a.name, COUNT(alb) FROM Artist a LEFT JOIN a.albums alb GROUP BY a " +
        "having count(alb) > 0 order by a.name")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "label_name")
    private String labelName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "artist")
    private List<Album> albums = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_info")
    private ArtistPersonalInfo personalInfo;

    public Artist() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public ArtistPersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(ArtistPersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", labelName='" + labelName + '\'' +
                ", albums=" + albums +
                ", personalInfo=" + personalInfo +
                '}';
    }
}
