package de.hshn.se.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StoreMap.
 */
@Entity
@Table(name = "store_map")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StoreMap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "validity_start", nullable = false)
    private ZonedDateTime validityStart;

    @NotNull
    @Column(name = "validity_end", nullable = false)
    private ZonedDateTime validityEnd;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @NotNull
    private Store store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getValidityStart() {
        return validityStart;
    }

    public StoreMap validityStart(ZonedDateTime validityStart) {
        this.validityStart = validityStart;
        return this;
    }

    public void setValidityStart(ZonedDateTime validityStart) {
        this.validityStart = validityStart;
    }

    public ZonedDateTime getValidityEnd() {
        return validityEnd;
    }

    public StoreMap validityEnd(ZonedDateTime validityEnd) {
        this.validityEnd = validityEnd;
        return this;
    }

    public void setValidityEnd(ZonedDateTime validityEnd) {
        this.validityEnd = validityEnd;
    }

    public String getUrl() {
        return url;
    }

    public StoreMap url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Store getStore() {
        return store;
    }

    public StoreMap store(Store store) {
        this.store = store;
        return this;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreMap storeMap = (StoreMap) o;
        if (storeMap.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, storeMap.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StoreMap{" +
            "id=" + id +
            ", validityStart='" + validityStart + "'" +
            ", validityEnd='" + validityEnd + "'" +
            ", url='" + url + "'" +
            '}';
    }

	public boolean isValidPath(double x, double y, double x2, double y2) {
		// TODO Auto-generated method stub
		return false;
	}

	public double distanceToPath(double x, double y, double x2, double y2) {
		// TODO Auto-generated method stub
		return 0;
	}
}
