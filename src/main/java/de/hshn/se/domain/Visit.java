package de.hshn.se.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Visit.
 */
@Entity
@Table(name = "visit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Visit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_of_visit", nullable = false)
    private ZonedDateTime dateOfVisit;

    @ManyToOne
    @NotNull
    private Store store;

    @ManyToOne
    @NotNull
    private Visitor visitor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateOfVisit() {
        return dateOfVisit;
    }

    public Visit dateOfVisit(ZonedDateTime dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
        return this;
    }

    public void setDateOfVisit(ZonedDateTime dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public Store getStore() {
        return store;
    }

    public Visit store(Store store) {
        this.store = store;
        return this;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public Visit visitor(Visitor visitor) {
        this.visitor = visitor;
        return this;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Visit visit = (Visit) o;
        if (visit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, visit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Visit{" +
            "id=" + id +
            ", dateOfVisit='" + dateOfVisit + "'" +
            '}';
    }
}
