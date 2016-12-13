package de.hshn.se.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Waypoint.
 */
@Entity
@Table(name = "waypoint")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Waypoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "x", nullable = false)
    private Float x;

    @NotNull
    @Column(name = "y", nullable = false)
    private Float y;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @ManyToOne
    @NotNull
    private Visit visit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getX() {
        return x;
    }

    public Waypoint x(Float x) {
        this.x = x;
        return this;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public Waypoint y(Float y) {
        this.y = y;
        return this;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Waypoint timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Visit getVisit() {
        return visit;
    }

    public Waypoint visit(Visit visit) {
        this.visit = visit;
        return this;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Waypoint waypoint = (Waypoint) o;
        if (waypoint.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, waypoint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Waypoint{" +
            "id=" + id +
            ", x='" + x + "'" +
            ", y='" + y + "'" +
            ", timestamp='" + timestamp + "'" +
            '}';
    }
}