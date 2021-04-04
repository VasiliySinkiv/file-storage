package com.example.storage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "type")
    private String type;

    @NonNull
    @Column(name = "size")
    private long size;

    @NonNull
    @Column(name = "date_uploading")
    private LocalDateTime dateUploading;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "link")
    private String link;

    @NonNull
    @JsonIgnore
    @OneToOne(cascade = {CascadeType.REMOVE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "media_data_id")
    private MediaData mediaData;

}
