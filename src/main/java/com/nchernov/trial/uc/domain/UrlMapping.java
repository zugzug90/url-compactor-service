package com.nchernov.trial.uc.domain;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="url_mapping")
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "url_mapping_id")
    private long urlMappingId;

    @Column(name="url")
    private String url;

    @Column(name="pseudo_hash")
    private String pseudoHash;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "urlMapping")
    private List<UrlVisit> urlVisits = new LinkedList();

    public UrlMapping() {
    }

    public UrlMapping(String url, String pseudoHash) {
        this.url = url;
        this.pseudoHash = pseudoHash;
    }

    public String getUrl() {
        return url;
    }

    public String getPseudoHash() {
        return pseudoHash;
    }

    public long getUrlMappingId() {
        return urlMappingId;
    }
}
