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
@Table(name = "m_customer")
public class MCustomer implements Serializable {


    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "id is mandatory")
    @JsonProperty("id")
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    @JsonProperty("biodataId")
    @Column(name = "biodata_id", columnDefinition = "bigint")
    private Long biodataId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("dob")
    @Column(name = "dob", columnDefinition = "date")
    private Date dob;

    @Length(max = 1, message = "gender must be between 0-1 characters")
    @JsonProperty("gender")
    @Column(name = "gender", columnDefinition = "varchar(1)")
    private String gender;

    @JsonProperty("bloodGroupId")
    @Column(name = "blood_group_id", columnDefinition = "bigint")
    private Long bloodGroupId;

    @Length(max = 5, message = "rhesus_type must be between 0-5 characters")
    @JsonProperty("rhesusType")
    @Column(name = "rhesus_type", columnDefinition = "varchar(5)")
    private String rhesusType;

    @JsonProperty("height")
    @Column(name = "height", columnDefinition = "decimal")
    private Float height;

    @JsonProperty("weight")
    @Column(name = "weight", columnDefinition = "decimal")
    private Float weight;

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
    @JoinColumn(name="biodata_id", insertable = false, updatable = false)
    private MBiodata mBiodata;

    @ManyToOne
    @JoinColumn(name="blood_group_id", insertable = false, updatable = false)
    private MBloodGroup mBloodGroup;


	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		MCustomer mCustomer = (MCustomer) o;
		return getId() != null && Objects.equals(getId(), mCustomer.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
