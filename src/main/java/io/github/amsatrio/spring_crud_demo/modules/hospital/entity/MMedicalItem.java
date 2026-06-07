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
@Table(name = "m_medical_item")
public class MMedicalItem implements Serializable {


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

    @JsonProperty("medicalItemCategoryId")
    @Column(name = "medical_item_category_id", columnDefinition = "bigint")
    private Long medicalItemCategoryId;

    @JsonProperty("composition")
    @Column(name = "composition", columnDefinition = "text")
    private String composition;

    @JsonProperty("medicalItemSegmentationId")
    @Column(name = "medical_item_segmentation_id", columnDefinition = "bigint")
    private Long medicalItemSegmentationId;

    @Length(max = 100, message = "manufacturer must be between 0-100 characters")
    @JsonProperty("manufacturer")
    @Column(name = "manufacturer", columnDefinition = "varchar(100)")
    private String manufacturer;

    @JsonProperty("indication")
    @Column(name = "indication", columnDefinition = "text")
    private String indication;

    @JsonProperty("dosage")
    @Column(name = "dosage", columnDefinition = "text")
    private String dosage;

    @JsonProperty("directions")
    @Column(name = "directions", columnDefinition = "text")
    private String directions;

    @JsonProperty("contraindication")
    @Column(name = "contraindication", columnDefinition = "text")
    private String contraindication;

    @JsonProperty("caution")
    @Column(name = "caution", columnDefinition = "text")
    private String caution;

    @Length(max = 50, message = "packaging must be between 0-50 characters")
    @JsonProperty("packaging")
    @Column(name = "packaging", columnDefinition = "varchar(50)")
    private String packaging;

    @JsonProperty("priceMax")
    @Column(name = "price_max", columnDefinition = "bigint")
    private Long priceMax;

    @JsonProperty("priceMin")
    @Column(name = "price_min", columnDefinition = "bigint")
    private Long priceMin;

    @JsonProperty("image")
    @Column(name = "image", columnDefinition = "blob")
    private byte[] image;

    @Length(max = 100, message = "image_path must be between 0-100 characters")
    @JsonProperty("imagePath")
    @Column(name = "image_path", columnDefinition = "varchar(100)")
    private String imagePath;

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
    @JoinColumn(name="medical_item_category_id", insertable = false, updatable = false)
    private MMedicalItemCategory mMedicalItemCategory;

    @ManyToOne
    @JoinColumn(name="medical_item_segmentation_id", insertable = false, updatable = false)
    private MMedicalItemSegmentation mMedicalItemSegmentation;


	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		MMedicalItem mMedicalItem = (MMedicalItem) o;
		return getId() != null && Objects.equals(getId(), mMedicalItem.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}
