package io.github.amsatrio.spring_crud_demo.modules.hospital.m_biodata;

import java.io.Serializable;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MBiodataRequest implements Serializable {
    
    @JsonProperty("id")
    private Long id;

    @Length(max = 255, message = "fullname must be between 0-255 characters")
    @JsonProperty("fullname")
    private String fullname;

    @Length(max = 15, message = "mobile_phone must be between 0-15 characters")
    @JsonProperty("mobilePhone")
    private String mobilePhone;

    @JsonProperty("image")
    private byte[] image;

    @Length(max = 255, message = "image_path must be between 0-255 characters")
    @JsonProperty("imagePath")
    private String imagePath;

    @JsonProperty("isDelete")
    private Boolean isDelete = false;
}
