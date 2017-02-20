package de.hshn.se.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
	@Column(name = "start_lat", nullable = false)
	private Double startLat;

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

	public Double getStartLat() {
		return startLat;
    }

	public MeasurementDataset startLat(Double startLat) {
		this.startLat = startLat;
        return this;
    }

	public void setStartLat(Double startLat) {
		this.startLat = startLat;
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
				", startLat='" + startLat + "'" +
            ", startLon='" + startLon + "'" +
            ", startAccuracy='" + startAccuracy + "'" +
            '}';
    }
}
