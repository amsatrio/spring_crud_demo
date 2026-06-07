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
@Table(name = "m_medical_facility")
public class MMedicalFacility implements Serializable {


    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    @Length(max = 50, message = "name must be between 0-50 characters")
    @JsonProperty("name")
    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;

    @JsonProperty("medicalFacilityCategoryId")
    @Column(name = "medical_facility_category_id", columnDefinition = "bigint")
    private Long medicalFacilityCategoryId;

    @JsonProperty("locationId")
    @Column(name = "location_id", columnDefinition = "bigint")
    private Long locationId;

    @JsonProperty("fullAddress")
    @Column(name = "full_address", columnDefinition = "text")
    private String fullAddress;

    @Length(max = 100, message = "email must be between 0-100 characters")
    @JsonProperty("email")
    @Column(name = "email", columnDefinition = "varchar(100)")
    private String email;

    @Length(max = 10, message = "phone_code must be between 0-10 characters")
    @JsonProperty("phoneCode")
    @Column(name = "phone_code", columnDefinition = "varchar(10)")
    private String phoneCode;

    @Length(max = 15, message = "phone must be between 0-15 characters")
    @JsonProperty("phone")
    @Column(name = "phone", columnDefinition = "varchar(15)")
    private String phone;

    @Length(max = 15, message = "fax must be between 0-15 characters")
    @JsonProperty("fax")
    @Column(name = "fax", columnDefinition = "varchar(15)")
    private String fax;

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
    @JoinColumn(name="medical_facility_category_id", insertable = false, updatable = false)
    private MMedicalFacilityCategory mMedicalFacilityCategory;

    @ManyToOne
    @JoinColumn(name="location_id", insertable = false, updatable = false)
    private MLocation mLocation;


	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		MMedicalFacility mMedicalFacility = (MMedicalFacility) o;
		return getId() != null && Objects.equals(getId(), mMedicalFacility.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
