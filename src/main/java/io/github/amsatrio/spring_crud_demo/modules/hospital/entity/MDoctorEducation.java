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
@Table(name = "m_doctor_education")
public class MDoctorEducation implements Serializable {


    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    @JsonProperty("doctorId")
    @Column(name = "doctor_id", columnDefinition = "bigint")
    private Long doctorId;

    @JsonProperty("educationLevelId")
    @Column(name = "education_level_id", columnDefinition = "bigint")
    private Long educationLevelId;

    @Length(max = 100, message = "institution_name must be between 0-100 characters")
    @JsonProperty("institutionName")
    @Column(name = "institution_name", columnDefinition = "varchar(100)")
    private String institutionName;

    @Length(max = 100, message = "major must be between 0-100 characters")
    @JsonProperty("major")
    @Column(name = "major", columnDefinition = "varchar(100)")
    private String major;

    @Length(max = 4, message = "start_year must be between 0-4 characters")
    @JsonProperty("startYear")
    @Column(name = "start_year", columnDefinition = "varchar(4)")
    private String startYear;

    @Length(max = 4, message = "end_year must be between 0-4 characters")
    @JsonProperty("endYear")
    @Column(name = "end_year", columnDefinition = "varchar(4)")
    private String endYear;

    @JsonProperty("isLastEducation")
    @Column(name = "is_last_education", columnDefinition = "boolean")
    private Boolean isLastEducation;

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
    @JoinColumn(name="doctor_id", insertable = false, updatable = false)
    private MDoctor mDoctor;

    @ManyToOne
    @JoinColumn(name="education_level_id", insertable = false, updatable = false)
    private MEducationLevel mEducationLevel;


	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		MDoctorEducation mDoctorEducation = (MDoctorEducation) o;
		return getId() != null && Objects.equals(getId(), mDoctorEducation.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
