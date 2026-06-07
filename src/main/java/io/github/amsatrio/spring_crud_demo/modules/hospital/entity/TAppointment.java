package io.github.amsatrio.spring_crud_demo.modules.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "t_appointment")
public class TAppointment implements Serializable {


    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    @JsonProperty("customerId")
    @Column(name = "customer_id", columnDefinition = "bigint")
    private Long customerId;

    @JsonProperty("doctorOfficeId")
    @Column(name = "doctor_office_id", columnDefinition = "bigint")
    private Long doctorOfficeId;

    @JsonProperty("doctorOfficeScheduleId")
    @Column(name = "doctor_office_schedule_id", columnDefinition = "bigint")
    private Long doctorOfficeScheduleId;

    @JsonProperty("doctorOfficeTreatmentId")
    @Column(name = "doctor_office_treatment_id", columnDefinition = "bigint")
    private Long doctorOfficeTreatmentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("appointmentDate")
    @Column(name = "appointment_date", columnDefinition = "date")
    private Date appointmentDate;

    @NotNull(message = "created_by is mandatory")
    @JsonProperty("createdBy")
    @Column(name = "created_by", columnDefinition = "bigint")
    private Long createdBy;

    @NotNull(message = "created_on is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("createdOn")
    @Column(name = "created_on", columnDefinition = "datetime")
    private Date createdOn;

    @JsonProperty("modifiedBy")
    @Column(name = "modified_by", columnDefinition = "bigint")
    private Long modifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("modifiedOn")
    @Column(name = "modified_on", columnDefinition = "datetime")
    private Date modifiedOn;

    @JsonProperty("deletedBy")
    @Column(name = "deleted_by", columnDefinition = "bigint")
    private Long deletedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("deletedOn")
    @Column(name = "deleted_on", columnDefinition = "datetime")
    private Date deletedOn;

    @NotNull(message = "is_delete is mandatory")
    @JsonProperty("isDelete")
    @Column(name = "is_delete", columnDefinition = "boolean")
    private Boolean isDelete;


    @ManyToOne
    @JoinColumn(name="customer_id", insertable = false, updatable = false)
    private MCustomer mCustomer;

    @ManyToOne
    @JoinColumn(name="doctor_office_id", insertable = false, updatable = false)
    private TDoctorOffice tDoctorOffice;

    @ManyToOne
    @JoinColumn(name="doctor_office_schedule_id", insertable = false, updatable = false)
    private TDoctorOfficeSchedule tDoctorOfficeSchedule;

    @ManyToOne
    @JoinColumn(name="doctor_office_treatment_id", insertable = false, updatable = false)
    private TDoctorOfficeTreatment tDoctorOfficeTreatment;


	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		TAppointment tAppointment = (TAppointment) o;
		return getId() != null && Objects.equals(getId(), tAppointment.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
