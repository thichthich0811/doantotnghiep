package com.web.entity;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="teams")
@Getter
@Setter
public class Teams{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String nameTeam;
    private Integer quantity;
    private String avatar;
    private String contact;
    private String descriptions;
    private String username;
    private LocalDate createDate;
    @ManyToOne
    private Sporttype sportType;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "teams", cascade = CascadeType.REMOVE)
    private List<Teamdetails> teamdetails;
    @Transient
    private Boolean daThamGia;
    @Transient
    private Integer activeMember;
    public Integer getActiveMember() {
        if (teamdetails == null) return 0;
        return (int) teamdetails.stream()
                .filter(td -> Boolean.TRUE.equals(td.getStatus()))
                .count();
    }
}
