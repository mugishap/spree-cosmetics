package com.spreecosmetics.api.v1.fileHandling;


import com.spreecosmetics.api.v1.audits.InitiatorAudit;
import com.spreecosmetics.api.v1.enums.EFileSizeType;
import com.spreecosmetics.api.v1.enums.EFileStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files", uniqueConstraints = {@UniqueConstraint(columnNames = "path")})
public class File extends InitiatorAudit {

    @Value("${server.host}")
    private String host;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Transient
    private String url;

    @Column(name = "size")
    private int size;

    @Column(name = "size_type")
    @Enumerated(EnumType.STRING)
    private EFileSizeType sizeType;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EFileStatus status;

    public File(String directory, String fileName, String extension, String fileBaseName) {
        super();
    }


    public String getUrl() {
        return host + "/api/v1/files/load-file/" + "/" + this.getName();
    }
}

