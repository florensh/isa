package de.hshn.se.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MeasurementDataset.
 */
@Entity
@Table(name = "measurement_dataset")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MeasurementDataset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Lob
    @Column(name = "measurements", nullable = false)
    private String measurements;

    @NotNull
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @NotNull
    @Column(name = "device_identification", nullable = false)
    private String deviceIdentification;

    @NotNull
    @Column(name = "start_lan", nullable = false)
    private Double startLan;

    @NotNull
    @Column(name = "start_lon", nullable = false)
    private Double startLon;

    @NotNull
    @Column(name = "start_accuracy", nullable = false)
    private Float startAccuracy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeasurements() {
        return measurements;
    }

    public MeasurementDataset measurements(String measurements) {
        this.measurements = measurements;
        return this;
    }

    public void setMeasurements(String measurements) {
        this.measurements = measurements;
    }

    public Long getStoreId() {
        return storeId;
    }

    public MeasurementDataset storeId(Long storeId) {
        this.storeId = storeId;
        return this;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getDeviceIdentification() {
        return deviceIdentification;
    }

    public MeasurementDataset deviceIdentification(String deviceIdentification) {
        this.deviceIdentification = deviceIdentification;
        return this;
    }

    public void setDeviceIdentification(String deviceIdentification) {
        this.deviceIdentification = deviceIdentification;
    }

    public Double getStartLan() {
        return startLan;
    }

    public MeasurementDataset startLan(Double startLan) {
        this.startLan = startLan;
        return this;
    }

    public void setStartLan(Double startLan) {
        this.startLan = startLan;
    }

    public Double getStartLon() {
        return startLon;
    }

    public MeasurementDataset startLon(Double startLon) {
        this.startLon = startLon;
        return this;
    }

    public void setStartLon(Double startLon) {
        this.startLon = startLon;
    }

    public Float getStartAccuracy() {
        return startAccuracy;
    }

    public MeasurementDataset startAccuracy(Float startAccuracy) {
        this.startAccuracy = startAccuracy;
        return this;
    }

    public void setStartAccuracy(Float startAccuracy) {
        this.startAccuracy = startAccuracy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeasurementDataset measurementDataset = (MeasurementDataset) o;
        if (measurementDataset.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, measurementDataset.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MeasurementDataset{" +
            "id=" + id +
            ", measurements='" + measurements + "'" +
            ", storeId='" + storeId + "'" +
            ", deviceIdentification='" + deviceIdentification + "'" +
            ", startLan='" + startLan + "'" +
            ", startLon='" + startLon + "'" +
            ", startAccuracy='" + startAccuracy + "'" +
            '}';
    }
}
